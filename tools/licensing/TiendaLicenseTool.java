import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Seller-side tool that creates Tienda licence keys. NOT shipped with the app.
 *
 * <pre>
 *   java TiendaLicenseTool.java init
 *   java TiendaLicenseTool.java pubkey
 *   java TiendaLicenseTool.java issue --customer "Mama Asha Shop" --tier premium \
 *        --machine K3P9Q-7XW2M-D4R8T-B6N1C --months 12
 * </pre>
 *
 * The private key lives in ~/.tienda-licensing/private.key (or $TIENDA_LICENSING_DIR).
 * Back it up somewhere safe and offline: without it you cannot issue or renew
 * licences, and anyone who gets it can make their own.
 */
public class TiendaLicenseTool {

    static Path dir() {
        String env = System.getenv("TIENDA_LICENSING_DIR");
        return env != null ? Paths.get(env) : Paths.get(System.getProperty("user.home"), ".tienda-licensing");
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            usage();
            return;
        }
        switch (args[0]) {
            case "init": init(); break;
            case "pubkey": System.out.println(new String(Files.readAllBytes(dir().resolve("public.key")),
                    StandardCharsets.US_ASCII).trim()); break;
            case "issue": issue(options(args)); break;
            default: usage();
        }
    }

    static void usage() {
        System.out.println("init                      create the signing key pair (once)\n"
                + "pubkey                    print the public key to put in LicenseManager.PUBLIC_KEY\n"
                + "issue --customer NAME --tier standard|premium --machine CODE|ANY\n"
                + "      (--months N | --until yyyy-MM-dd)");
    }

    static void init() throws Exception {
        Path d = dir();
        Files.createDirectories(d);
        Path priv = d.resolve("private.key");
        if (Files.exists(priv)) {
            System.err.println("A key already exists at " + priv + ". Refusing to overwrite it:"
                    + " a new key would invalidate every licence issued so far.");
            System.exit(1);
        }
        KeyPair kp = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
        Files.write(priv, Base64.getEncoder().encode(kp.getPrivate().getEncoded()));
        Files.write(d.resolve("public.key"), Base64.getEncoder().encode(kp.getPublic().getEncoded()));
        try {
            Files.setPosixFilePermissions(priv, PosixFilePermissions.fromString("rw-------"));
        } catch (UnsupportedOperationException ignore) {
            // Windows: rely on the user folder's permissions.
        }
        System.out.println("Key pair written to " + d);
        System.out.println("Public key (put this in LicenseManager.PUBLIC_KEY):");
        System.out.println(Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
        System.out.println("BACK UP " + priv + " somewhere safe and offline.");
    }

    static void issue(Map<String, String> o) throws Exception {
        String customer = require(o, "customer").replace("|", "/").trim();
        String tierName = require(o, "tier").toLowerCase();
        int tier = tierName.equals("premium") ? 2 : tierName.equals("standard") ? 1 : -1;
        if (tier < 0) {
            throw new IllegalArgumentException("--tier must be standard or premium");
        }
        String machine = require(o, "machine").trim().toUpperCase();
        LocalDate issued = LocalDate.now();
        LocalDate until = o.containsKey("until") ? LocalDate.parse(o.get("until"))
                : issued.plusMonths(Long.parseLong(require(o, "months")));

        String payload = customer + "|" + tier + "|" + machine + "|" + until + "|" + issued;
        byte[] pkcs8 = Base64.getDecoder().decode(new String(
                Files.readAllBytes(dir().resolve("private.key")), StandardCharsets.US_ASCII).trim());
        PrivateKey pk = KeyFactory.getInstance("Ed25519").generatePrivate(new PKCS8EncodedKeySpec(pkcs8));
        Signature s = Signature.getInstance("Ed25519");
        s.initSign(pk);
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        s.update(bytes);
        Base64.Encoder b64 = Base64.getUrlEncoder().withoutPadding();
        String key = "TIENDA1." + b64.encodeToString(bytes) + "." + b64.encodeToString(s.sign());

        try (PrintWriter log = new PrintWriter(new FileWriter(dir().resolve("issued.csv").toFile(), true))) {
            log.println(issued + "," + quote(customer) + "," + tierName + "," + machine + "," + until + "," + key);
        } catch (IOException e) {
            System.err.println("Warning: could not write issued.csv: " + e.getMessage());
        }
        System.out.println(key);
    }

    static String quote(String s) {
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    static String require(Map<String, String> o, String k) {
        String v = o.get(k);
        if (v == null || v.isEmpty()) {
            throw new IllegalArgumentException("missing --" + k);
        }
        return v;
    }

    static Map<String, String> options(String[] args) {
        Map<String, String> o = new HashMap<>();
        for (int i = 1; i + 1 < args.length; i += 2) {
            if (!args[i].startsWith("--")) {
                throw new IllegalArgumentException("unexpected " + args[i]);
            }
            o.put(args[i].substring(2), args[i + 1]);
        }
        return o;
    }
}
