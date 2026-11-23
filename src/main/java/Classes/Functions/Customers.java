package Classes.Functions;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access helpers for the {@code sales_customers} table.
 *
 * <p>Columns: customer_id, first_name, last_name, phone, email, password,
 * street, city, state, zip_code. {@code customer_id} is a plain INT primary key
 * (not auto-increment), so new ids are allocated as {@code MAX(id)+1}.
 *
 * @author Nkanabo
 */
public final class Customers {

    /** Immutable-ish view of one customer row used by the UI. */
    public static final class Customer {
        public int id;
        public String firstName = "";
        public String lastName = "";
        public String phone = "";
        public String email = "";
        public String street = "";
        public String city = "";
        public String state = "";
        public String zip = "";

        public String fullName() {
            return (firstName + " " + lastName).trim();
        }
    }

    private Customers() {
    }

    /** Column titles for the customers table, in display order. */
    public static String[] columns() {
        return new String[]{"ID", "First name", "Last name", "Phone", "Email",
                "Street", "City", "State", "Zip"};
    }

    private static Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.id = rs.getInt("customer_id");
        c.firstName = nz(rs.getString("first_name"));
        c.lastName = nz(rs.getString("last_name"));
        c.phone = nz(rs.getString("phone"));
        c.email = nz(rs.getString("email"));
        c.street = nz(rs.getString("street"));
        c.city = nz(rs.getString("city"));
        c.state = nz(rs.getString("state"));
        c.zip = nz(rs.getString("zip_code"));
        return c;
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    /**
     * @param search optional case-insensitive filter on name/phone/email;
     *               null or blank returns everyone
     */
    public static List<Customer> list(String search) throws SQLException {
        List<Customer> out = new ArrayList<>();
        boolean filtered = search != null && !search.trim().isEmpty();
        String sql = "SELECT * FROM Tienda.sales_customers";
        if (filtered) {
            sql += " WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?"
                    + " OR LOWER(phone) LIKE ? OR LOWER(email) LIKE ?";
        }
        sql += " ORDER BY customer_id";

        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (filtered) {
                String like = "%" + search.trim().toLowerCase() + "%";
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, like);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(map(rs));
                }
            }
        }
        return out;
    }

    public static Customer find(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM Tienda.sales_customers WHERE customer_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private static int nextId(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT COALESCE(MAX(customer_id), 0) + 1 AS nid FROM Tienda.sales_customers")) {
            return rs.next() ? rs.getInt("nid") : 1;
        }
    }

    /** Insert a new customer; sets {@code c.id} to the allocated id. */
    public static void insert(Customer c) throws SQLException {
        String sql = "INSERT INTO Tienda.sales_customers "
                + "(customer_id, first_name, last_name, phone, email, street, city, state, zip_code) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            c.id = nextId(conn);
            bind(ps, c, true);
            ps.executeUpdate();
        }
    }

    public static void update(Customer c) throws SQLException {
        String sql = "UPDATE Tienda.sales_customers SET "
                + "first_name = ?, last_name = ?, phone = ?, email = ?, "
                + "street = ?, city = ?, state = ?, zip_code = ? WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, c, false);
            ps.setInt(9, c.id);
            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM Tienda.sales_customers WHERE customer_id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static int count() throws SQLException {
        try (Connection conn = DBConnection.getConnectionInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Tienda.sales_customers")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Bind the common columns. When {@code withId} the first param is the id. */
    private static void bind(PreparedStatement ps, Customer c, boolean withId) throws SQLException {
        int i = 1;
        if (withId) {
            ps.setInt(i++, c.id);
        }
        ps.setString(i++, c.firstName);
        ps.setString(i++, c.lastName);
        ps.setString(i++, c.phone);
        ps.setString(i++, c.email);
        ps.setString(i++, c.street);
        ps.setString(i++, c.city);
        ps.setString(i++, c.state);
        ps.setString(i++, c.zip);
    }
}
