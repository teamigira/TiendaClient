package Classes.Functions;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Premium-only business features (gated by {@link Authentication.LicenseManager}
 * at the call site). These lean on H2's built-in {@code CSVWRITE} and
 * {@code BACKUP TO} so exports and backups are consistent even while the app is
 * running.
 *
 * @author Nkanabo
 */
public final class PremiumFeatures {

    private PremiumFeatures() {
    }

    /** Exports the product catalogue to a CSV file. Returns rows written. */
    public static int exportProductsCsv(String path) throws Exception {
        String query =
                "SELECT p.code AS Code, p.product_name AS Product, "
                + "b.brand_name AS Brand, c.category_name AS Category, "
                + "p.list_price AS Cost, p.retail_price AS Price, "
                + "p.expiry_date AS Expiry, COALESCE(s.quantity, '0') AS Stock "
                + "FROM Tienda.production_products p "
                + "LEFT JOIN Tienda.production_brands b ON CAST(b.brand_id AS VARCHAR) = p.brand_id "
                + "LEFT JOIN Tienda.production_categories c ON CAST(c.category_id AS VARCHAR) = p.category_id "
                + "LEFT JOIN Tienda.production_stocks s ON CAST(s.product_id AS VARCHAR) = CAST(p.code AS VARCHAR) "
                + "ORDER BY p.product_name";
        return csvWrite(path, query);
    }

    /** Exports completed sales line-items to a CSV file. Returns rows written. */
    public static int exportSalesCsv(String path) throws Exception {
        String query =
                "SELECT o.customer_id AS Receipt, p.product_name AS Product, "
                + "i.quantity AS Qty, p.list_price AS UnitPrice, "
                + "(CAST(i.quantity AS DOUBLE) * p.list_price) AS LineTotal, "
                + "i.discount AS Discount, o.order_date AS SoldOn "
                + "FROM Tienda.sales_order_items i "
                + "JOIN Tienda.sales_orders o ON o.order_id = i.order_id "
                + "JOIN Tienda.production_products p ON i.item_id = p.code "
                + "ORDER BY o.order_date DESC, o.customer_id";
        return csvWrite(path, query);
    }

    /** Runs H2 CSVWRITE and returns the number of data rows written. */
    private static int csvWrite(String path, String query) throws Exception {
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("CALL CSVWRITE(?, ?)")) {
            ps.setString(1, path);
            ps.setString(2, query);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * Backs up the whole database to a .zip file using H2's online BACKUP.
     *
     * @param zipPath destination .zip path
     */
    public static void backupDatabase(String zipPath) throws Exception {
        // BACKUP TO takes a string literal; escape embedded single quotes.
        String safe = zipPath.replace("'", "''");
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             java.sql.Statement st = c.createStatement()) {
            st.execute("BACKUP TO '" + safe + "'");
        }
    }

    /**
     * Restores the database from a backup .zip (Premium). Shuts the database
     * down first so the file lock is released, then extracts the backup over the
     * live files. The application MUST be restarted afterwards (the caller exits).
     *
     * @param zipPath a backup produced by {@link #backupDatabase(String)}
     */
    public static void restoreDatabase(String zipPath) throws Exception {
        java.io.File zip = new java.io.File(zipPath);
        if (!zip.isFile()) {
            throw new IllegalArgumentException("Backup file not found: " + zipPath);
        }
        if (!isValidBackup(zip)) {
            throw new IllegalArgumentException("This file is not a Tienda backup: " + zip.getName());
        }
        DBConnection dbc = DBConnection.getConnectionInstance();
        String dir = dbc.getDbDirectory();
        String name = dbc.getDbName();

        // Keep a copy of the current data first, so a restore can be undone.
        String stamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        backupDatabase(new java.io.File(com.nkanabo.Tienda.AppPaths.backupsDir(),
                "Tienda_before_restore_" + stamp + ".zip").getAbsolutePath());

        // Close the database cleanly so the file lock is released.
        try (Connection c = dbc.getConnection();
             java.sql.Statement st = c.createStatement()) {
            st.execute("SHUTDOWN");
        } catch (Exception ignore) {
            // Already closing / not open — proceed to restore.
        }

        // Extract the backup over the database files.
        org.h2.tools.Restore.execute(zipPath, dir, name);
    }

    /**
     * Creates at most one automatic backup per day in {@code <home>/TiendaBackups}
     * and keeps the most recent {@code keep} files. Safe to call on every launch;
     * it is a no-op if today's backup already exists. Available to all tiers
     * (data safety), independent of the Premium restore gate.
     *
     * @return the backup file created, or {@code null} if none was needed/possible
     */
    public static java.io.File autoBackupDaily(int keep) {
        try {
            java.io.File dir = com.nkanabo.Tienda.AppPaths.backupsDir();
            if (!dir.exists() && !dir.mkdirs()) {
                return null;
            }
            String stamp = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            java.io.File target = new java.io.File(dir, "Tienda_auto_" + stamp + ".zip");
            if (target.exists()) {
                return null; // already backed up today
            }
            backupDatabase(target.getAbsolutePath());
            if (!isValidBackup(target)) {
                target.delete();
                throw new java.io.IOException("Backup check failed for " + target);
            }
            pruneBackups(dir, keep);
            copyToExtraFolder(target, keep);
            return target;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * True if the file is a zip holding a non-empty Tienda database, i.e. it
     * can be restored.
     */
    public static boolean isValidBackup(java.io.File zip) {
        String want = com.nkanabo.Tienda.AppPaths.DB_NAME + ".mv.db";
        try (java.util.zip.ZipFile z = new java.util.zip.ZipFile(zip)) {
            java.util.zip.ZipEntry e = z.getEntry(want);
            return e != null && e.getSize() > 0;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    /**
     * Copy a backup to the extra folder from settings (USB drive or cloud
     * folder), if one is set and reachable, keeping the newest {@code keep}.
     *
     * @return true if copied
     */
    public static boolean copyToExtraFolder(java.io.File backup, int keep) {
        String extra = Classes.Utilities.AppOptions.getExtraBackupDir();
        if (extra.isEmpty()) {
            return false;
        }
        java.io.File dir = new java.io.File(extra);
        if (!dir.isDirectory()) {
            return false; // USB drive not plugged in, etc.
        }
        try {
            java.nio.file.Files.copy(backup.toPath(), new java.io.File(dir, backup.getName()).toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            pruneBackups(dir, keep);
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Keeps only the newest {@code keep} auto-backups in {@code dir}. */
    private static void pruneBackups(java.io.File dir, int keep) {
        java.io.File[] files = dir.listFiles((d, n) ->
                n.startsWith("Tienda_auto_") && n.endsWith(".zip"));
        if (files == null || files.length <= keep) {
            return;
        }
        java.util.Arrays.sort(files,
                java.util.Comparator.comparingLong(java.io.File::lastModified).reversed());
        for (int i = keep; i < files.length; i++) {
            files[i].delete();
        }
    }

    /** One best-selling product row. */
    public static final class TopSeller {
        public final String product;
        public final double qty;
        public final double revenue;

        public TopSeller(String product, double qty, double revenue) {
            this.product = product;
            this.qty = qty;
            this.revenue = revenue;
        }
    }

    /** Best-selling products across completed sales, highest quantity first. */
    public static List<TopSeller> topSellers(int limit) {
        List<TopSeller> out = new ArrayList<>();
        String sql =
                "SELECT p.product_name AS name, "
                + "SUM(CAST(i.quantity AS DOUBLE)) AS qty, "
                + "SUM(CAST(i.quantity AS DOUBLE) * p.list_price) AS revenue "
                + "FROM Tienda.sales_order_items i "
                + "JOIN Tienda.sales_orders o ON o.order_id = i.order_id "
                + "JOIN Tienda.production_products p ON i.item_id = p.code "
                + "GROUP BY p.product_name ORDER BY qty DESC LIMIT ?";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new TopSeller(rs.getString("name"),
                            rs.getDouble("qty"), rs.getDouble("revenue")));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return out;
    }
}
