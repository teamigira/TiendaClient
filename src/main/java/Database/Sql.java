package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Prepared statements with values bound separately, so text typed by a user
 * can never change the SQL itself (SQL injection).
 *
 * <pre>
 *   ResultSet rs = Sql.prepare(conn, "SELECT * FROM t WHERE code = ?", code).executeQuery();
 *   int n = Sql.prepare(conn, "UPDATE t SET name = ? WHERE id = ?", name, id).executeUpdate();
 * </pre>
 *
 * Values are bound as text, the same way the old {@code '" + value + "'}
 * queries quoted them, so the database converts them to the column type as
 * before. {@code null} is bound as SQL NULL.
 *
 * @author Nkanabo
 */
public final class Sql {

    private Sql() {
    }

    public static PreparedStatement prepare(Connection conn, String sql, Object... values)
            throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            Object v = values[i];
            ps.setString(i + 1, v == null ? null : String.valueOf(v));
        }
        return ps;
    }

    /** A LIKE pattern that matches {@code text} literally anywhere (escapes % and _). */
    public static String contains(String text) {
        String t = text == null ? "" : text;
        return "%" + t.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
    }
}
