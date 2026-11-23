package Authentication;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordsTest {

    @Test
    void hashVerifiesOnlyTheRightPassword() {
        String h = Passwords.hash("Sokoni#2026");
        assertTrue(Passwords.isHashed(h));
        assertTrue(Passwords.verify("Sokoni#2026", h));
        assertFalse(Passwords.verify("sokoni#2026", h));
        assertFalse(Passwords.verify("", h));
        assertFalse(Passwords.verify(null, h));
    }

    @Test
    void samePasswordGetsDifferentSalts() {
        assertNotEquals(Passwords.hash("12345"), Passwords.hash("12345"));
    }

    @Test
    void oldReversibleValuesAreNotAcceptedAsHashes() {
        String legacy = Encrpytion.encrypt("12345");
        assertFalse(Passwords.isHashed(legacy));
        assertFalse(Passwords.verify("12345", legacy));
        assertFalse(Passwords.verify("x", "pbkdf2$broken"));
    }
}
