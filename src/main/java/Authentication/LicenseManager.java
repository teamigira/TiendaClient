package Authentication;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Monthly activation + premium licensing for Tienda.
 *
 * <p>Design goals (from product owner):
 * <ul>
 *   <li>A new activation code every month. One code is valid for the whole
 *       calendar month it was issued for; a fresh code is issued each month.</li>
 *   <li>Once a valid code is entered the activation lasts one month, so the app
 *       launches straight to login until it expires (state is remembered in
 *       {@link Preferences}, including the expiry date).</li>
 *   <li>The code determines the feature tier (STANDARD vs PREMIUM). Premium
 *       unlocks features such as receipt templates and advanced reports.</li>
 *   <li>Validation is done locally for now using a deterministic HMAC so codes
 *       can be generated offline. A seam ({@link #validateOnline}) is provided
 *       for the future "compare against our online database" check.</li>
 * </ul>
 *
 * <p>The code is 16 digits, shown to the user as four groups of four, matching
 * the four input boxes on the launcher screen, e.g. {@code 1234 5678 9012 3456}.
 * The first digit encodes the tier; the remaining fifteen are derived from
 * HMAC-SHA256(secret, date + tier).
 *
 * @author Nkanabo
 */
public final class LicenseManager {

    public static final int TIER_INVALID = -1;
    public static final int TIER_STANDARD = 1;
    public static final int TIER_PREMIUM = 2;

    /**
     * Shared secret used to derive daily codes. Keep this private to the build.
     * Changing it invalidates every previously issued code. The online service,
     * when added, must use the same secret (or sign codes server-side).
     */
    private static final String SECRET = "Tienda-POS-2.0-daily-activation-secret";

    /** Time zone the "current day" is measured in (matches the rest of the app). */
    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");

    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /** Codes are keyed by calendar month (a code is valid for its whole month). */
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("yyyy-MM");
    /** Human-friendly expiry date shown in the UI. */
    private static final DateTimeFormatter PRETTY = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    private static final Preferences PREFS = Preferences.userRoot().node("TiendaLicense");

    /** Set to true once an online endpoint is available. */
    private static final boolean ONLINE_CHECK_ENABLED = false;

    /** Current tier for this running session (set after a successful validation/restore). */
    private static int currentTier = TIER_INVALID;

    private LicenseManager() {
    }

    // ------------------------------------------------------------------
    // Code generation
    // ------------------------------------------------------------------

    /** Today's date in the app's zone. */
    public static LocalDate today() {
        return LocalDate.now(ZONE);
    }

    /**
     * Generate the activation code for the month containing {@code day} and the
     * given tier. A single code is valid for the whole calendar month, so the
     * owner issues one code per month per tier.
     *
     * @param day  any day in the month the code is valid for
     * @param tier {@link #TIER_STANDARD} or {@link #TIER_PREMIUM}
     * @return a 16-digit code (no spaces)
     */
    public static String generate(LocalDate day, int tier) {
        if (tier != TIER_STANDARD && tier != TIER_PREMIUM) {
            throw new IllegalArgumentException("Unknown tier: " + tier);
        }
        String payload = day.format(MONTH) + "|" + tier;
        byte[] mac = hmac(payload);
        // Build a non-negative 15-digit number from the first 8 bytes of the MAC.
        long value = 0L;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) | (mac[i] & 0xFFL);
        }
        value = Math.floorMod(value, 1_000_000_000_000_000L); // 15 digits
        String digits = String.format("%015d", value);
        return tier + digits; // 16 digits total, first digit = tier
    }

    /** This month's code for a tier, formatted as four groups of four for display. */
    public static String todaysCodeFormatted(int tier) {
        return format(generate(today(), tier));
    }

    /** Alias for clarity: this month's code for a tier, formatted for display. */
    public static String thisMonthsCodeFormatted(int tier) {
        return todaysCodeFormatted(tier);
    }

    /** Format a 16-digit code as {@code 1234 5678 9012 3456}. */
    public static String format(String code16) {
        String c = code16.replaceAll("\\s", "");
        if (c.length() != 16) {
            return code16;
        }
        return c.substring(0, 4) + " " + c.substring(4, 8) + " "
                + c.substring(8, 12) + " " + c.substring(12, 16);
    }

    // ------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------

    /**
     * Validate a typed code for the current month.
     *
     * @param typed the code as typed (spaces/dashes ignored)
     * @return the tier if the code is this month's code, otherwise
     *         {@link #TIER_INVALID}
     */
    public static int validate(String typed) {
        if (typed == null) {
            return TIER_INVALID;
        }
        String code = typed.replaceAll("[^0-9]", "");
        if (code.length() != 16) {
            return TIER_INVALID;
        }

        // Future: authoritative check against the online database.
        if (ONLINE_CHECK_ENABLED) {
            int online = validateOnline(code);
            if (online != TIER_INVALID) {
                return online;
            }
        }

        LocalDate day = today();
        for (int tier : new int[]{TIER_STANDARD, TIER_PREMIUM}) {
            if (constantTimeEquals(code, generate(day, tier))) {
                return tier;
            }
        }
        return TIER_INVALID;
    }

    /**
     * Placeholder for the future online validation against our server.
     * Returns {@link #TIER_INVALID} until implemented.
     */
    private static int validateOnline(String code) {
        // TODO: POST the code to the licensing endpoint and map the response
        //       to TIER_STANDARD / TIER_PREMIUM. On network failure, fall back
        //       to the local check (handled by the caller).
        return TIER_INVALID;
    }

    // ------------------------------------------------------------------
    // Activation state ("valid for one month from activation")
    // ------------------------------------------------------------------

    /**
     * Record a successful activation. The licence is then valid for one month
     * from today, so the app launches straight to login until it expires.
     */
    public static void recordActivation(int tier) {
        currentTier = tier;
        LocalDate now = today();
        PREFS.put("activatedDate", now.format(DAY));
        PREFS.put("expiryDate", now.plusMonths(1).format(DAY));
        PREFS.putInt("tier", tier);
    }

    /**
     * @return true if a non-expired activation is on record (within one month of
     *         activation). Also restores the session tier when true. The method
     *         name is kept for backwards compatibility with existing callers.
     */
    public static boolean isActivatedForToday() {
        LocalDate expiry = getExpiry();
        if (expiry != null && !today().isAfter(expiry)) {
            currentTier = PREFS.getInt("tier", TIER_STANDARD);
            return true;
        }
        return false;
    }

    /**
     * @return the date the current activation expires, or {@code null} if there
     *         is no (valid) stored expiry.
     */
    public static LocalDate getExpiry() {
        String stored = PREFS.get("expiryDate", "");
        if (stored.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(stored, DAY);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return a friendly "valid until" date (e.g. {@code 21-Dec-2026}) for the
     *         active licence, or an empty string when nothing is active.
     */
    public static String expiryFormatted() {
        LocalDate expiry = getExpiry();
        return expiry == null ? "" : expiry.format(PRETTY);
    }

    /** Clear the stored activation (forces re-activation on next launch). */
    public static void clearActivation() {
        currentTier = TIER_INVALID;
        PREFS.remove("activatedDate");
        PREFS.remove("expiryDate");
        PREFS.remove("tier");
    }

    // ------------------------------------------------------------------
    // Feature gating
    // ------------------------------------------------------------------

    public static int currentTier() {
        return currentTier;
    }

    /** @return true when the active licence unlocks premium features. */
    public static boolean isPremium() {
        return currentTier == TIER_PREMIUM;
    }

    public static String tierName(int tier) {
        switch (tier) {
            case TIER_STANDARD: return "Standard";
            case TIER_PREMIUM:  return "Premium";
            default:            return "Invalid";
        }
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private static byte[] hmac(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to compute licence code", e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            diff |= a.charAt(i) ^ b.charAt(i);
        }
        return diff == 0;
    }
}
