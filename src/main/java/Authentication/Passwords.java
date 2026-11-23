package Authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * One-way password hashing for staff logins.
 *
 * <p>Stored form: {@code pbkdf2$<iterations>$<salt>$<hash>} (PBKDF2-HMAC-SHA256,
 * a random 16-byte salt per password, base64). A hash cannot be turned back
 * into the password, unlike the old {@link Encrpytion} values, which used a key
 * written in the program. {@link #migrateLegacy} converts those old values once
 * at startup.
 *
 * @author Nkanabo
 */
public final class Passwords {

    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private Passwords() {
    }

    /** Hash a password for storage. */
    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] h = pbkdf2(password, salt, ITERATIONS);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + b64.encodeToString(salt) + "$" + b64.encodeToString(h);
    }

    /** True if {@code password} matches the stored hash. */
    public static boolean verify(String password, String stored) {
        if (password == null || stored == null || !isHashed(stored)) {
            return false;
        }
        try {
            String[] parts = stored.split("\\$");
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            return MessageDigest.isEqual(expected, pbkdf2(password, salt, iterations));
        } catch (RuntimeException e) {
            return false;
        }
    }

    /** True if the value is already in the hashed format. */
    public static boolean isHashed(String stored) {
        return stored != null && stored.startsWith(PREFIX + "$") && stored.split("\\$").length == 4;
    }

    /**
     * Replace every old reversible password in {@code sales_staffs} with a
     * hash. Safe to run on every start: hashed rows are skipped.
     *
     * @return how many passwords were converted
     */
    public static int migrateLegacy(Connection conn) throws SQLException {
        List<String[]> legacy = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT staff_id, password FROM Tienda.sales_staffs WHERE password IS NOT NULL");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String pw = rs.getString(2);
                if (!pw.isEmpty() && !isHashed(pw)) {
                    legacy.add(new String[]{rs.getString(1), pw});
                }
            }
        }
        int converted = 0;
        try (PreparedStatement up = conn.prepareStatement(
                "UPDATE Tienda.sales_staffs SET password = ? WHERE staff_id = ?")) {
            for (String[] row : legacy) {
                String plain = Encrpytion.decrypt(row[1]);
                // A value that does not decrypt cannot be used to log in, so
                // store an unusable hash rather than leave it as is.
                up.setString(1, hash(plain != null ? plain : randomSecret()));
                up.setString(2, row[0]);
                converted += up.executeUpdate();
            }
        }
        return converted;
    }

    private static String randomSecret() {
        byte[] b = new byte[24];
        RANDOM.nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    private static byte[] pbkdf2(String password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, HASH_BITS);
            try {
                return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            } finally {
                spec.clearPassword();
            }
        } catch (Exception e) {
            throw new IllegalStateException("PBKDF2 unavailable", e);
        }
    }

    static byte[] utf8(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }
}
