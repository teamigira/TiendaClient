package Authentication;

import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * Signed, computer-bound licences.
 *
 * <p>A licence key is {@code TIENDA1.<payload>.<signature>} (base64url). The
 * payload is {@code customer|tier|machine|expiry|issued}; the signature is
 * Ed25519 over the payload, made with the private key that only the seller
 * holds (see {@code tools/licensing}). The app contains only the public key,
 * so it can check a licence but cannot create one, and decompiling the app
 * does not help anyone forge keys.
 *
 * <p>The key is stored as-is and re-verified on every start: changing the
 * stored tier or expiry, or copying the key to another computer, does not
 * work, because the signature or the computer code no longer matches.
 *
 * <p>Without a licence the app runs as a {@value #TRIAL_DAYS}-day Standard
 * trial.
 *
 * @author Nkanabo
 */
public final class LicenseManager {

    public static final int TIER_INVALID = -1;
    public static final int TIER_STANDARD = 1;
    public static final int TIER_PREMIUM = 2;

    /** Length of the free trial, counted from the first start. */
    public static final int TRIAL_DAYS = 14;

    /**
     * Public half of the seller's Ed25519 signing key (X.509, base64).
     * Generated with {@code tools/licensing}; the private half is kept by the
     * seller and must never be added to this repository.
     */
    private static final String PUBLIC_KEY = "MCowBQYDK2VwAyEADh4ky4ATk64x1syyTZEuYNMTC2lo93f8e7mazzHCkV8=";

    private static final String PREFIX = "TIENDA1";
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter PRETTY = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
    private static final Preferences PREFS = Preferences.userRoot().node("TiendaLicense");

    /** The contents of a licence key that passed the checks. */
    public static final class Licence {
        public final String customer;
        public final int tier;
        public final String machine;
        public final LocalDate expiry;
        public final LocalDate issued;

        Licence(String customer, int tier, String machine, LocalDate expiry, LocalDate issued) {
            this.customer = customer;
            this.tier = tier;
            this.machine = machine;
            this.expiry = expiry;
            this.issued = issued;
        }
    }

    private static int currentTier = TIER_INVALID;
    private static Licence current;
    private static boolean trial;
    /** Key accepted by {@link #validate} and saved by {@link #recordActivation}. */
    private static String pendingKey;
    /** Why the last check failed, for the activation screen. */
    private static String lastError = "";

    private LicenseManager() {
    }

    /** Today's date in this computer's time zone. */
    public static LocalDate today() {
        return LocalDate.now(ZoneId.systemDefault());
    }

    // ------------------------------------------------------------------
    // Checking keys
    // ------------------------------------------------------------------

    /**
     * Check a licence key against this computer and today's date.
     *
     * @return the licence, or {@code null} (see {@link #lastError()})
     */
    public static Licence check(String key) {
        lastError = "";
        if (key == null || key.trim().isEmpty()) {
            lastError = "Enter a licence key.";
            return null;
        }
        String[] parts = key.replaceAll("\\s", "").split("\\.");
        if (parts.length != 3 || !PREFIX.equals(parts[0])) {
            lastError = "This is not a Tienda licence key.";
            return null;
        }
        byte[] payload;
        try {
            payload = Base64.getUrlDecoder().decode(parts[1]);
            Signature v = Signature.getInstance("Ed25519");
            v.initVerify(publicKey());
            v.update(payload);
            if (!v.verify(Base64.getUrlDecoder().decode(parts[2]))) {
                lastError = "This licence key is not valid.";
                return null;
            }
        } catch (Exception e) {
            lastError = "This licence key is not valid.";
            return null;
        }
        String[] f = new String(payload, StandardCharsets.UTF_8).split("\\|", -1);
        Licence lic;
        try {
            lic = new Licence(f[0], Integer.parseInt(f[1]), f[2],
                    LocalDate.parse(f[3], DAY), LocalDate.parse(f[4], DAY));
        } catch (RuntimeException e) {
            lastError = "This licence key is not valid.";
            return null;
        }
        if (f.length != 5 || (lic.tier != TIER_STANDARD && lic.tier != TIER_PREMIUM)) {
            lastError = "This licence key is not valid.";
            return null;
        }
        if (!machineMatches(lic.machine)) {
            lastError = "This licence is for another computer. This computer's code is " + machineCode() + ".";
            return null;
        }
        if (today().isAfter(lic.expiry)) {
            lastError = "This licence expired on " + lic.expiry.format(PRETTY) + ".";
            return null;
        }
        if (clockWentBack()) {
            lastError = "This computer's date is earlier than when Tienda last ran. Correct the date and try again.";
            return null;
        }
        return lic;
    }

    /**
     * Check a typed key (activation screen).
     *
     * @return the tier, or {@link #TIER_INVALID}
     */
    public static int validate(String typed) {
        Licence lic = check(typed);
        if (lic == null) {
            return TIER_INVALID;
        }
        pendingKey = typed.replaceAll("\\s", "");
        return lic.tier;
    }

    /** Why the last check failed (empty if it succeeded). */
    public static String lastError() {
        return lastError;
    }

    /** Save the key accepted by the last successful {@link #validate}. */
    public static void recordActivation(int tier) {
        if (pendingKey == null) {
            return;
        }
        PREFS.put("licenceKey", pendingKey);
        current = check(pendingKey);
        currentTier = current != null ? current.tier : TIER_INVALID;
        trial = false;
        pendingKey = null;
        touchClock();
    }

    /**
     * Restore the licence at startup: a stored key that still checks out, or
     * else a running trial. (Name kept for existing callers.)
     *
     * @return true if the app may start (licensed or in trial)
     */
    public static boolean isActivatedForToday() {
        String stored = PREFS.get("licenceKey", "");
        if (!stored.isEmpty()) {
            current = check(stored);
            if (current != null) {
                currentTier = current.tier;
                trial = false;
                touchClock();
                return true;
            }
        }
        current = null;
        if (trialDaysLeft() > 0 && !clockWentBack()) {
            currentTier = TIER_STANDARD;
            trial = true;
            touchClock();
            return true;
        }
        currentTier = TIER_INVALID;
        trial = false;
        return false;
    }

    // ------------------------------------------------------------------
    // Trial
    // ------------------------------------------------------------------

    /** Days left in the free trial (0 when it has ended). */
    public static long trialDaysLeft() {
        String start = PREFS.get("trialStart", "");
        LocalDate first;
        if (start.isEmpty()) {
            first = today();
            PREFS.put("trialStart", first.format(DAY));
        } else {
            try {
                first = LocalDate.parse(start, DAY);
            } catch (RuntimeException e) {
                return 0;
            }
        }
        long used = Math.max(0, ChronoUnit.DAYS.between(first, today()));
        return Math.max(0, TRIAL_DAYS - used);
    }

    /** True while running on the free trial rather than a licence. */
    public static boolean isTrial() {
        return trial;
    }

    // ------------------------------------------------------------------
    // Clock rollback
    // ------------------------------------------------------------------

    private static boolean clockWentBack() {
        String seen = PREFS.get("lastSeen", "");
        if (seen.isEmpty()) {
            return false;
        }
        try {
            // One day of slack for time-zone changes.
            return today().isBefore(LocalDate.parse(seen, DAY).minusDays(1));
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static void touchClock() {
        String seen = PREFS.get("lastSeen", "");
        LocalDate t = today();
        try {
            if (!seen.isEmpty() && !t.isAfter(LocalDate.parse(seen, DAY))) {
                return;
            }
        } catch (RuntimeException ignore) {
        }
        PREFS.put("lastSeen", t.format(DAY));
    }

    // ------------------------------------------------------------------
    // Computer code
    // ------------------------------------------------------------------

    /**
     * This computer's code, e.g. {@code K3P9Q-7XW2M-D4R8T-B6N1C}. The customer
     * sends it to the seller, who puts it in the licence.
     */
    public static String machineCode() {
        return machineIds().get(0);
    }

    private static boolean machineMatches(String code) {
        if ("ANY".equals(code)) {
            return true; // site licence, not tied to one computer
        }
        return machineIds().contains(code);
    }

    /**
     * Candidate codes: computer name + system + each physical network card.
     * Any one matching is enough, so plugging in a USB network adapter does
     * not break the licence. The first is the one shown to the customer.
     */
    private static List<String> machineIds() {
        String host = firstNonBlank(System.getenv("COMPUTERNAME"), System.getenv("HOSTNAME"), hostName());
        String base = host.toUpperCase(Locale.ROOT) + "|" + System.getProperty("os.name", "");
        List<String> macs = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while (nics != null && nics.hasMoreElements()) {
                NetworkInterface ni = nics.nextElement();
                byte[] mac = ni.getHardwareAddress();
                String name = ni.getName().toLowerCase(Locale.ROOT);
                if (mac == null || mac.length != 6 || ni.isLoopback() || ni.isVirtual()
                        || name.matches("^(vir|docker|veth|br-|vmnet|vbox|tun|tap|zt|wg|vethernet).*")) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                for (byte b : mac) {
                    sb.append(String.format("%02X", b));
                }
                macs.add(sb.toString());
            }
        } catch (Exception ignore) {
        }
        Collections.sort(macs);
        List<String> ids = new ArrayList<>();
        for (String m : macs) {
            ids.add(code(base + "|" + m));
        }
        if (ids.isEmpty()) {
            ids.add(code(base));
        }
        return ids;
    }

    /** First 100 bits of SHA-256, Crockford base32, four groups of five. */
    private static String code(String s) {
        try {
            byte[] h = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            String alphabet = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";
            StringBuilder out = new StringBuilder();
            int bits = 0, value = 0, chars = 0;
            for (byte b : h) {
                value = ((value << 8) | (b & 0xFF)) & 0xFFFF;
                bits += 8;
                while (bits >= 5 && chars < 20) {
                    out.append(alphabet.charAt((value >> (bits - 5)) & 31));
                    bits -= 5;
                    chars++;
                    if (chars % 5 == 0 && chars < 20) {
                        out.append('-');
                    }
                }
                if (chars >= 20) {
                    break;
                }
            }
            return out.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static String hostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "";
        }
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v.trim();
            }
        }
        return "";
    }

    private static PublicKey publicKey() throws Exception {
        return KeyFactory.getInstance("Ed25519")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY)));
    }

    // ------------------------------------------------------------------
    // Status
    // ------------------------------------------------------------------

    public static int currentTier() {
        return currentTier;
    }

    /** @return true when the active licence unlocks premium features. */
    public static boolean isPremium() {
        return currentTier == TIER_PREMIUM;
    }

    /** Who the licence was issued to ("" on trial). */
    public static String customer() {
        return current == null ? "" : current.customer;
    }

    public static String tierName(int tier) {
        switch (tier) {
            case TIER_STANDARD: return trial ? "Trial" : "Standard";
            case TIER_PREMIUM:  return "Premium";
            default:            return "Invalid";
        }
    }

    /** Expiry of the licence, or the last day of the trial. */
    public static LocalDate getExpiry() {
        if (current != null) {
            return current.expiry;
        }
        if (trial) {
            return today().plusDays(trialDaysLeft());
        }
        return null;
    }

    /** A friendly expiry date (e.g. {@code 21-Dec-2026}), or "" when nothing is active. */
    public static String expiryFormatted() {
        LocalDate expiry = getExpiry();
        return expiry == null ? "" : expiry.format(PRETTY);
    }

    /** Remove the stored licence (the next start asks for a key again). */
    public static void clearActivation() {
        currentTier = TIER_INVALID;
        current = null;
        trial = false;
        PREFS.remove("licenceKey");
        // Old monthly-code activation, no longer used.
        PREFS.remove("activatedDate");
        PREFS.remove("expiryDate");
        PREFS.remove("tier");
    }
}
