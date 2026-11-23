package Database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Authentication.Encrpytion;
import Authentication.Passwords;
import Classes.Functions.Crudes;
import Classes.Functions.PremiumFeatures;
import Classes.Functions.Products;
import com.nkanabo.Tienda.AppPaths;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Runs against a fresh database in target/test-data (set by the surefire
 * configuration in pom.xml), never the shop's real data.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseTest {

    static Connection conn;

    @BeforeAll
    static void freshDatabase() throws Exception {
        File data = AppPaths.dataDir();
        assertTrue(data.getPath().contains("test-data"), "tests must not touch real data: " + data);
        if (data.exists()) {
            try (java.util.stream.Stream<java.nio.file.Path> walk = Files.walk(data.toPath())) {
                walk.sorted(Comparator.reverseOrder()).map(java.nio.file.Path::toFile).forEach(File::delete);
            }
        }
        DBConnection db = DBConnection.getConnectionInstance();
        db.migrate();
        MasterData.InsertMasterData();
        conn = db.getConnection();
    }

    static int count(String sql, Object... args) throws Exception {
        // App code closes the shared connection; always ask for a live one.
        conn = DBConnection.getConnectionInstance().getConnection();
        try (ResultSet rs = Sql.prepare(conn, sql, args).executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    @Test
    @Order(1)
    void factoryAdminHasHashedPassword() throws Exception {
        try (ResultSet rs = Sql.prepare(conn,
                "SELECT password FROM Tienda.sales_staffs WHERE first_name = ?", "admin").executeQuery()) {
            assertTrue(rs.next());
            String stored = rs.getString(1);
            assertTrue(Passwords.isHashed(stored));
            assertTrue(Passwords.verify("12345", stored));
        }
    }

    @Test
    @Order(2)
    void loginQueryIgnoresInjection() throws Exception {
        String sql = "SELECT COUNT(*) FROM Tienda.sales_staffs ss "
                + "INNER JOIN Tienda.roles r ON ss.role_id = r.role_id "
                + "WHERE ss.first_name = ? AND ss.active = '1'";
        assertEquals(1, count(sql, "admin"));
        assertEquals(0, count(sql, "' OR '1'='1' --"));
        assertEquals(0, count(sql, "admin' --"));
    }

    @Test
    @Order(3)
    void legacyPasswordsAreConverted() throws Exception {
        conn = DBConnection.getConnectionInstance().getConnection();
        Sql.prepare(conn, "UPDATE Tienda.sales_staffs SET password = ? WHERE first_name = ?",
                Encrpytion.encrypt("Duka2026"), "admin").executeUpdate();
        assertEquals(1, Passwords.migrateLegacy(conn));
        assertEquals(0, Passwords.migrateLegacy(conn));
        try (ResultSet rs = Sql.prepare(conn,
                "SELECT password FROM Tienda.sales_staffs WHERE first_name = ?", "admin").executeQuery()) {
            rs.next();
            assertTrue(Passwords.verify("Duka2026", rs.getString(1)));
        }
    }

    @Test
    @Order(4)
    void quotesInNamesNoLongerBreakQueries() throws Exception {
        assertTrue(Crudes.addBrand("Mama's Kitchen"));
        assertEquals(1, count("SELECT COUNT(*) FROM Tienda.production_brands WHERE brand_name = ?", "Mama's Kitchen"));
        assertFalse(Products.checkCodeValidity("x' OR '1'='1"));
        assertFalse(Products.checkCodeValidity("'; DROP TABLE Tienda.production_products; --"));
        assertEquals(0, count("SELECT COUNT(*) FROM Tienda.production_products"));
    }

    @Test
    @Order(5)
    void backupAndRestoreRoundTrip() throws Exception {
        File zip = new File(AppPaths.backupsDir(), "test.zip");
        PremiumFeatures.backupDatabase(zip.getAbsolutePath());
        assertTrue(PremiumFeatures.isValidBackup(zip));

        Crudes.addBrand("Added after backup");
        int withExtra = count("SELECT COUNT(*) FROM Tienda.production_brands");

        PremiumFeatures.restoreDatabase(zip.getAbsolutePath());
        conn = DBConnection.getConnectionInstance().getConnection();
        assertEquals(withExtra - 1, count("SELECT COUNT(*) FROM Tienda.production_brands"));

        String[] backups = AppPaths.backupsDir().list();
        assertTrue(java.util.Arrays.stream(backups).anyMatch(n -> n.startsWith("Tienda_before_restore_")));
    }

    @Test
    @Order(6)
    void notABackupIsRefused() throws Exception {
        File junk = new File(AppPaths.backupsDir(), "junk.zip");
        Files.write(junk.toPath(), "not a zip".getBytes());
        assertFalse(PremiumFeatures.isValidBackup(junk));
        assertThrows(IllegalArgumentException.class, () -> PremiumFeatures.restoreDatabase(junk.getAbsolutePath()));
    }
}
