package Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.time.LocalDate;
import java.util.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Licence checks with a throwaway key pair (the real private key is never needed). */
class LicenseManagerTest {

    static KeyPair keys;
    static KeyPair otherKeys;

    @BeforeAll
    static void makeKeys() throws Exception {
        keys = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
        otherKeys = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
    }

    static String sign(KeyPair kp, String payload) throws Exception {
        Signature s = Signature.getInstance("Ed25519");
        s.initSign(kp.getPrivate());
        byte[] b = payload.getBytes(StandardCharsets.UTF_8);
        s.update(b);
        Base64.Encoder e = Base64.getUrlEncoder().withoutPadding();
        return "TIENDA1." + e.encodeToString(b) + "." + e.encodeToString(s.sign());
    }

    static String payload(String machine, LocalDate expiry, int tier) {
        return "Duka la Mama|" + tier + "|" + machine + "|" + expiry + "|" + LocalDate.now();
    }

    @Test
    void validKeyForThisComputer() throws Exception {
        String key = sign(keys, payload(LicenseManager.machineCode(), LocalDate.now().plusMonths(1), 2));
        LicenseManager.Licence lic = LicenseManager.check(key, keys.getPublic());
        assertNotNull(lic, LicenseManager.lastError());
        assertEquals(LicenseManager.TIER_PREMIUM, lic.tier);
        assertEquals("Duka la Mama", lic.customer);
    }

    @Test
    void keyWithSpacesAndLineBreaksStillWorks() throws Exception {
        String key = sign(keys, payload("ANY", LocalDate.now().plusDays(1), 1));
        String messy = key.substring(0, 30) + "\n  " + key.substring(30);
        assertNotNull(LicenseManager.check(messy, keys.getPublic()));
    }

    @Test
    void anotherComputerIsRefused() throws Exception {
        String key = sign(keys, payload("AAAAA-BBBBB-CCCCC-DDDDD", LocalDate.now().plusMonths(1), 2));
        assertNull(LicenseManager.check(key, keys.getPublic()));
        assertTrue(LicenseManager.lastError().contains("another computer"));
    }

    @Test
    void expiredKeyIsRefused() throws Exception {
        String key = sign(keys, payload("ANY", LocalDate.now().minusDays(1), 2));
        assertNull(LicenseManager.check(key, keys.getPublic()));
        assertTrue(LicenseManager.lastError().contains("expired"));
    }

    @Test
    void editedKeyIsRefused() throws Exception {
        String key = sign(keys, payload("ANY", LocalDate.now().plusMonths(1), 1));
        String[] p = key.split("\\.");
        String edited = new String(Base64.getUrlDecoder().decode(p[1]), StandardCharsets.UTF_8)
                .replace("|1|", "|2|"); // try to upgrade Standard to Premium
        String forged = p[0] + "." + Base64.getUrlEncoder().withoutPadding()
                .encodeToString(edited.getBytes(StandardCharsets.UTF_8)) + "." + p[2];
        assertNull(LicenseManager.check(forged, keys.getPublic()));
    }

    @Test
    void keySignedByAnotherKeyIsRefused() throws Exception {
        String key = sign(otherKeys, payload("ANY", LocalDate.now().plusMonths(1), 2));
        assertNull(LicenseManager.check(key, keys.getPublic()));
    }

    @Test
    void garbageIsRefused() {
        assertNull(LicenseManager.check("1234 5678 9012 3456", keys.getPublic()));
        assertNull(LicenseManager.check("", keys.getPublic()));
        assertNull(LicenseManager.check("TIENDA1.abc.def", keys.getPublic()));
    }

    @Test
    void realPublicKeyRefusesTestKeys() throws Exception {
        String key = sign(keys, payload("ANY", LocalDate.now().plusMonths(1), 2));
        assertEquals(LicenseManager.TIER_INVALID, LicenseManager.validate(key));
    }

    @Test
    void computerCodeLooksRight() {
        assertTrue(LicenseManager.machineCode().matches("[0-9A-Z]{5}(-[0-9A-Z]{5}){3}"));
    }
}
