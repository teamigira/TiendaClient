package Classes.Functions;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer loyalty points (Premium). Points are a simple running balance on
 * {@code sales_customers.loyalty_points}; the Premium screen awards points on a
 * purchase and redeems them for a discount.
 *
 * @author Nkanabo
 */
public final class LoyaltyManager {

    private LoyaltyManager() {
    }

    /** One customer with their current points balance. */
    public static final class Member {
        public final String id;
        public final String name;
        public final String phone;
        public final int points;

        public Member(String id, String name, String phone, int points) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.points = points;
        }
    }

    /** All customers with their points, highest balance first. */
    public static List<Member> list() {
        List<Member> out = new ArrayList<>();
        String sql = "SELECT customer_id, first_name, last_name, phone, loyalty_points "
                + "FROM Tienda.sales_customers "
                + "ORDER BY loyalty_points DESC, first_name";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = (opt(rs.getString("first_name")) + " "
                        + opt(rs.getString("last_name"))).trim();
                out.add(new Member(rs.getString("customer_id"),
                        name.isEmpty() ? "(no name)" : name,
                        opt(rs.getString("phone")),
                        rs.getInt("loyalty_points")));
            }
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
        }
        return out;
    }

    /** Adds (or, with a negative value, removes) points for a customer. */
    public static boolean addPoints(String customerId, int delta) {
        String sql = "UPDATE Tienda.sales_customers "
                + "SET loyalty_points = GREATEST(0, loyalty_points + ?) "
                + "WHERE customer_id = ?";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setString(2, customerId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
            return false;
        }
    }

    /** Redeems points (never below zero). Returns true if the customer had enough. */
    public static boolean redeem(String customerId, int points) {
        if (points <= 0) {
            return false;
        }
        String sql = "UPDATE Tienda.sales_customers "
                + "SET loyalty_points = loyalty_points - ? "
                + "WHERE customer_id = ? AND loyalty_points >= ?";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, points);
            ps.setString(2, customerId);
            ps.setInt(3, points);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
            return false;
        }
    }

    private static String opt(String s) {
        return s == null ? "" : s;
    }
}
