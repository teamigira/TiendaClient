package Classes.Functions;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Low-stock detection for the notification ("mail") indicator.
 *
 * <p>Stock is held in {@code production_stocks} keyed by the product's
 * {@code code}; this joins back to {@code production_products} for the name and
 * returns anything at or below a threshold so the cashier is warned before a
 * line runs out.
 *
 * @author Nkanabo
 */
public final class StockAlerts {

    /** Default low-stock threshold. */
    public static final double DEFAULT_THRESHOLD = 5;

    /** One low/empty stock line. */
    public static final class Alert {
        public final String name;
        public final double quantity;
        public Alert(String name, double quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }

    private StockAlerts() {
    }

    /** Products at or below {@code threshold}, lowest first. */
    public static List<Alert> low(double threshold) {
        List<Alert> out = new ArrayList<>();
        String sql = "SELECT p.product_name AS name, s.quantity AS qty "
                + "FROM Tienda.production_stocks s "
                + "JOIN Tienda.production_products p "
                + "  ON CAST(p.code AS VARCHAR) = CAST(s.product_id AS VARCHAR) "
                + "WHERE CAST(s.quantity AS DOUBLE) <= ? "
                + "ORDER BY CAST(s.quantity AS DOUBLE) ASC";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double q;
                    try {
                        q = Double.parseDouble(rs.getString("qty"));
                    } catch (Exception e) {
                        q = 0;
                    }
                    out.add(new Alert(rs.getString("name"), q));
                }
            }
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
        }
        return out;
    }

    public static List<Alert> low() {
        return low(Classes.Utilities.AppOptions.getLowStockThreshold());
    }

    // ===================================================================
    // Expiry alerts (used by the Premium Alerts Center)
    // ===================================================================

    private static final java.time.format.DateTimeFormatter[] DATE_FORMATS = {
        java.time.format.DateTimeFormatter.ISO_LOCAL_DATE,
        java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"),
        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy"),
        java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")
    };

    /** One product with an upcoming / passed expiry date. */
    public static final class Expiry {
        public final String name;
        public final String dateText;
        public final long daysLeft; // negative = already expired

        public Expiry(String name, String dateText, long daysLeft) {
            this.name = name;
            this.dateText = dateText;
            this.daysLeft = daysLeft;
        }
    }

    /** Products that have expired or expire within {@code withinDays}, soonest first. */
    public static List<Expiry> expiring(long withinDays) {
        List<Expiry> out = new ArrayList<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        String sql = "SELECT product_name, expiry_date FROM Tienda.production_products "
                + "WHERE expiry_date IS NOT NULL AND TRIM(expiry_date) <> ''";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.time.LocalDate d = parseDate(rs.getString("expiry_date"));
                if (d == null) {
                    continue;
                }
                long days = java.time.temporal.ChronoUnit.DAYS.between(today, d);
                if (days <= withinDays) {
                    out.add(new Expiry(rs.getString("product_name"),
                            d.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE), days));
                }
            }
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
        }
        out.sort((a, b) -> Long.compare(a.daysLeft, b.daysLeft));
        return out;
    }

    private static java.time.LocalDate parseDate(String raw) {
        String s = raw.trim();
        for (java.time.format.DateTimeFormatter f : DATE_FORMATS) {
            try {
                return java.time.LocalDate.parse(s, f);
            } catch (Exception ignore) {
                // try next
            }
        }
        return null;
    }
}
