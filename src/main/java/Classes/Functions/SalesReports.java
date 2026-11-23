package Classes.Functions;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregations for the Reports page.
 *
 * <p>Sales are stored one line per product in {@code sales_order_items}
 * (quantity, list_price captured at sale time, and {@code date} as an epoch-
 * millisecond string). Rather than fight H2's VARCHAR/BIGINT typing in SQL, we
 * pull the line items once (joined to their category/brand) and aggregate in
 * Java by day, month, category and brand.
 *
 * @author Nkanabo
 */
public final class SalesReports {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    /** One sold line item, already resolved to a date and revenue. */
    public static final class Line {
        public LocalDate date;
        public double revenue;
        public String category = "Uncategorised";
        public String brand = "Unbranded";
    }

    private SalesReports() {
    }

    /** Load every sold line item resolved to date + revenue. */
    public static List<Line> lines() throws SQLException {
        String sql = "SELECT oi.date AS ms, oi.quantity AS qty, oi.list_price AS price, "
                + "COALESCE(c.category_name, 'Uncategorised') AS category, "
                + "COALESCE(b.brand_name, 'Unbranded') AS brand "
                + "FROM Tienda.sales_order_items oi "
                + "LEFT JOIN Tienda.production_products p "
                + "  ON CAST(p.product_id AS VARCHAR) = oi.product_id "
                + "LEFT JOIN Tienda.production_categories c "
                + "  ON CAST(c.category_id AS VARCHAR) = CAST(p.category_id AS VARCHAR) "
                + "LEFT JOIN Tienda.production_brands b "
                + "  ON CAST(b.brand_id AS VARCHAR) = CAST(p.brand_id AS VARCHAR)";

        List<Line> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate d = parseDate(rs.getString("ms"));
                if (d == null) {
                    continue;
                }
                Line ln = new Line();
                ln.date = d;
                ln.revenue = toDouble(rs.getString("qty")) * toDouble(rs.getString("price"));
                ln.category = rs.getString("category");
                ln.brand = rs.getString("brand");
                out.add(ln);
            }
        }
        return out;
    }

    // ---- time series ------------------------------------------------

    /** Revenue per day for the last {@code days} days (oldest first, zero-filled). */
    public static LinkedHashMap<LocalDate, Double> dailyRevenue(int days) throws SQLException {
        LinkedHashMap<LocalDate, Double> map = new LinkedHashMap<>();
        LocalDate today = LocalDate.now(ZONE);
        for (int i = days - 1; i >= 0; i--) {
            map.put(today.minusDays(i), 0.0);
        }
        for (Line ln : lines()) {
            if (map.containsKey(ln.date)) {
                map.merge(ln.date, ln.revenue, Double::sum);
            }
        }
        return map;
    }

    /** Revenue per month ("yyyy-MM") for the last {@code months} months. */
    public static LinkedHashMap<String, Double> monthlyRevenue(int months) throws SQLException {
        LinkedHashMap<String, Double> map = new LinkedHashMap<>();
        YearMonth now = YearMonth.now(ZONE);
        for (int i = months - 1; i >= 0; i--) {
            map.put(now.minusMonths(i).format(MONTH), 0.0);
        }
        for (Line ln : lines()) {
            String key = YearMonth.from(ln.date).format(MONTH);
            if (map.containsKey(key)) {
                map.merge(key, ln.revenue, Double::sum);
            }
        }
        return map;
    }

    /** Total revenue grouped by category (descending), from all history. */
    public static LinkedHashMap<String, Double> revenueByCategory() throws SQLException {
        return grouped(true);
    }

    /** Total revenue grouped by brand (descending), from all history. */
    public static LinkedHashMap<String, Double> revenueByBrand() throws SQLException {
        return grouped(false);
    }

    private static LinkedHashMap<String, Double> grouped(boolean byCategory) throws SQLException {
        Map<String, Double> tmp = new java.util.HashMap<>();
        for (Line ln : lines()) {
            String key = byCategory ? ln.category : ln.brand;
            tmp.merge(key, ln.revenue, Double::sum);
        }
        LinkedHashMap<String, Double> out = new LinkedHashMap<>();
        tmp.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> out.put(e.getKey(), e.getValue()));
        return out;
    }

    // ---- headline totals -------------------------------------------

    /** {today, thisWeek, thisMonth, thisYear, allTime} revenue. */
    public static double[] totals() throws SQLException {
        LocalDate today = LocalDate.now(ZONE);
        LocalDate weekStart = today.minusDays(6);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate yearStart = today.withDayOfYear(1);
        double tDay = 0, tWeek = 0, tMonth = 0, tYear = 0, tAll = 0;
        for (Line ln : lines()) {
            tAll += ln.revenue;
            if (!ln.date.isBefore(yearStart)) {
                tYear += ln.revenue;
            }
            if (!ln.date.isBefore(monthStart)) {
                tMonth += ln.revenue;
            }
            if (!ln.date.isBefore(weekStart)) {
                tWeek += ln.revenue;
            }
            if (ln.date.equals(today)) {
                tDay += ln.revenue;
            }
        }
        return new double[]{tDay, tWeek, tMonth, tYear, tAll};
    }

    /** Number of sold line items (rough order volume). */
    public static int itemCount() throws SQLException {
        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Tienda.sales_order_items")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ---- one-shot snapshot (performance) ---------------------------

    /**
     * All figures the Reports page needs, computed from a <b>single</b> pass
     * over the sold line items.
     *
     * <p>The per-metric methods above each re-query and re-join the whole sales
     * history; calling six of them to paint one screen meant six full scans on
     * the UI thread. {@link #snapshot(int, int)} reads the data once and derives
     * everything from it, so the page opens quickly and can be built off the EDT.
     */
    public static final class Snapshot {
        public double[] totals = new double[5];                       // day, week, month, year, all
        public int itemCount;
        public LinkedHashMap<LocalDate, Double> daily = new LinkedHashMap<>();
        public LinkedHashMap<String, Double> monthly = new LinkedHashMap<>();
        public LinkedHashMap<String, Double> byCategory = new LinkedHashMap<>();
        public LinkedHashMap<String, Double> byBrand = new LinkedHashMap<>();
    }

    /** Build a full {@link Snapshot} with one database read. */
    public static Snapshot snapshot(int days, int months) throws SQLException {
        Snapshot s = new Snapshot();
        LocalDate today = LocalDate.now(ZONE);
        LocalDate weekStart = today.minusDays(6);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate yearStart = today.withDayOfYear(1);

        // Zero-filled time buckets so charts always show a full axis.
        for (int i = days - 1; i >= 0; i--) {
            s.daily.put(today.minusDays(i), 0.0);
        }
        YearMonth nowM = YearMonth.now(ZONE);
        for (int i = months - 1; i >= 0; i--) {
            s.monthly.put(nowM.minusMonths(i).format(MONTH), 0.0);
        }

        Map<String, Double> cat = new java.util.HashMap<>();
        Map<String, Double> brand = new java.util.HashMap<>();

        List<Line> all = lines();                 // the single scan
        s.itemCount = all.size();
        for (Line ln : all) {
            s.totals[4] += ln.revenue;
            if (!ln.date.isBefore(yearStart)) {
                s.totals[3] += ln.revenue;
            }
            if (!ln.date.isBefore(monthStart)) {
                s.totals[2] += ln.revenue;
            }
            if (!ln.date.isBefore(weekStart)) {
                s.totals[1] += ln.revenue;
            }
            if (ln.date.equals(today)) {
                s.totals[0] += ln.revenue;
            }
            if (s.daily.containsKey(ln.date)) {
                s.daily.merge(ln.date, ln.revenue, Double::sum);
            }
            String mk = YearMonth.from(ln.date).format(MONTH);
            if (s.monthly.containsKey(mk)) {
                s.monthly.merge(mk, ln.revenue, Double::sum);
            }
            cat.merge(ln.category, ln.revenue, Double::sum);
            brand.merge(ln.brand, ln.revenue, Double::sum);
        }
        cat.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> s.byCategory.put(e.getKey(), e.getValue()));
        brand.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> s.byBrand.put(e.getKey(), e.getValue()));
        return s;
    }

    // ---- helpers ----------------------------------------------------

    private static LocalDate parseDate(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        raw = raw.trim();
        try {
            long ms = Long.parseLong(raw);
            return Instant.ofEpochMilli(ms).atZone(ZONE).toLocalDate();
        } catch (NumberFormatException ignore) {
            // fall through
        }
        try {
            return LocalDate.parse(raw);
        } catch (Exception ignore) {
            return null;
        }
    }

    private static double toDouble(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
