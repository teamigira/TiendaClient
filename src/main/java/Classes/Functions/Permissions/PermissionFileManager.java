package Classes.Functions.Permissions;

import Classes.AbstractClasses.Permissions;
import Classes.AbstractClasses.RolePermissionMapping;
import Classes.AbstractClasses.Roles;
import Classes.Utilities.NotificationManager;
import static Classes.Utilities.NotificationManager.showConsoleNotification;
import static Classes.Utilities.NotificationManager.showPopupNotification;
import Database.DBConnection;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PermissionFileManager {

    private static final String JSON_FILE_PATH = "/resources/props/permissions.json";

    // getFileFromResource method to get file from resources
    public static File getFileFromResource(String resourcePath) throws URISyntaxException {
        URL url = PermissionFileManager.class.getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return new File(url.toURI());
    }

    public static boolean addRole(String role, String description) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnectionInstance().getConnection();

            // Check if the role already exists
            String checkQuery = "SELECT COUNT(*) FROM Tienda.roles WHERE role_name = ?";
            pstmt = connection.prepareStatement(checkQuery);
            pstmt.setString(1, role);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                String msg = "Role already exists.";
                showPopupNotification(msg, NotificationManager.NotificationType.ERROR);
                showConsoleNotification(msg, NotificationManager.NotificationType.ERROR);
                return false; // Role already exists, return false
            }

            // Role doesn't exist, proceed with insertion
            String insertQuery = "INSERT INTO Tienda.roles (role_name, description) VALUES (?,?)";
            pstmt = connection.prepareStatement(insertQuery);
            pstmt.setString(1, role);
            pstmt.setString(2, description);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException se) {
            showPopupNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            showConsoleNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
        }
        return false;
    }

    public static ArrayList<Roles> loadRoles() {
        ArrayList<Roles> list = new ArrayList<>();
        try {
            Connection connection = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = connection.createStatement();
            String sqlquery = "SELECT * FROM Tienda.roles";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                int roleid = rs.getInt("role_id");
                String role = rs.getString("role_name");
                String desc = rs.getString("description");
                list.add(new Roles(roleid,role, desc));
            }
        } catch (SQLException se) {
            showPopupNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            showConsoleNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
        }
        return list;
    }

    public static ArrayList<Permissions> loadPermissions() {
        ArrayList<Permissions> list = new ArrayList<>();
        try {
            Connection connection = DBConnection.getConnectionInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM Tienda.permissions";
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                int permissionId = resultSet.getInt("permission_id");
                String permissionName = resultSet.getString("permission_name");
                String category = resultSet.getString("category");
                list.add(new Permissions(permissionId, permissionName, category));
            }
        } catch (SQLException se) {
            showPopupNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            showConsoleNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
        }
        return list;
    }

    public static ArrayList<RolePermissionMapping> loadRolePermissions() {
        ArrayList<RolePermissionMapping> mappings = new ArrayList<>();
        try {
            Connection connection = DBConnection.getConnectionInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT rp.role_id, rp.permission_id, r.role_name, p.permission_name, p.category "
                    + "FROM Tienda.role_permissions rp "
                    + "INNER JOIN Tienda.roles r ON rp.role_id = r.role_id "
                    + "INNER JOIN Tienda.permissions p ON rp.permission_id = p.permission_id";
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                int roleId = resultSet.getInt("role_id");
                int permissionId = resultSet.getInt("permission_id");
                String roleName = resultSet.getString("role_name");
                String permissionName = resultSet.getString("permission_name");
                String category = resultSet.getString("category");
                mappings.add(new RolePermissionMapping(roleId, permissionId, roleName, permissionName, category));
            }
        } catch (SQLException se) {
            showPopupNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            showConsoleNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
        }
        return mappings;
    }

    private static int getPermissionId(String permissionName) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        connection = DBConnection.getConnectionInstance().getConnection();
        String query = "SELECT permission_id FROM Tienda.permissions WHERE permission_name = ?";
        pstmt = connection.prepareStatement(query);
        pstmt.setString(1, permissionName);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("permission_id");
        }
        return -1; // Permission not found

    }
    
    public static boolean addPermissionsRole(String roleName, String permissionName) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = DBConnection.getConnectionInstance().getConnection();
            int roleId = getRoleId(roleName, connection);
            if (roleId == -1) {
                String msg = "Role '" + roleName + "' does not exist.";
                showPopupNotification(msg, NotificationManager.NotificationType.ERROR);
                showConsoleNotification(msg, NotificationManager.NotificationType.ERROR);
                return false;
            }
            int permissionId = getPermissionId(permissionName);
            if (permissionId == -1) {
                String msg = "Permission '" + permissionName + "' does not exist.";
                showPopupNotification(msg, NotificationManager.NotificationType.ERROR);
                showConsoleNotification(msg, NotificationManager.NotificationType.ERROR);
                return false;
            }
            String Addquery = "INSERT INTO Tienda.role_permissions (role_id, permission_id) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM Tienda.role_permissions WHERE role_id = ? AND permission_id = ?)";
        
            String insertQuery = "INSERT INTO Tienda.role_permissions (role_id, permission_id) VALUES (?, ?)";
            pstmt = connection.prepareStatement(Addquery);
            pstmt.setInt(1, roleId);
            pstmt.setInt(2, permissionId);
            pstmt.setInt(3, roleId);
            pstmt.setInt(4, permissionId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                String msg = "Permission '" + permissionName + "' added to role '" + roleName + "'.";
                showPopupNotification(msg, NotificationManager.NotificationType.SUCCESS);
                showConsoleNotification(msg, NotificationManager.NotificationType.SUCCESS);
                return true;
            } else {
                String msg = "Failed to add permission to role.";
                showPopupNotification(msg, NotificationManager.NotificationType.ERROR);
                showConsoleNotification(msg, NotificationManager.NotificationType.ERROR);
                return false;
            }
        } catch (SQLException e) {
            String errorMsg = "Error occurred while adding permission to role: " + e.getMessage();
            showPopupNotification(errorMsg, NotificationManager.NotificationType.ERROR);
            showConsoleNotification(errorMsg, NotificationManager.NotificationType.ERROR);
            return false;
        }
    }

    public static boolean renameRole(String role, String roleName) {
        try {
            Connection connection = DBConnection.getConnectionInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Tienda.roles SET role_name = ? WHERE role_name = ?");
            statement.setString(1, roleName); // New role name
            statement.setString(2, role); // Old role name
            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            if (rowsAffected > 0) {
                showPopupNotification("Role renamed successfully.", NotificationManager.NotificationType.SUCCESS);
                showConsoleNotification("Role renamed successfully.", NotificationManager.NotificationType.SUCCESS);
                return true;
            } else {
                showPopupNotification("Failed to rename role.", NotificationManager.NotificationType.ERROR);
                showConsoleNotification("Failed to rename role.", NotificationManager.NotificationType.ERROR);
                return false;
            }
        } catch (SQLException se) {
            showPopupNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            showConsoleNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            return false;
        }
    }

    public static boolean deletePermissionRow(String selectedNode) {
        try {
            Connection connection = DBConnection.getConnectionInstance().getConnection();
            int id = getPermissionId(selectedNode);
            System.out.println(id+" that is the ID" + selectedNode);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Tienda.role_permissions WHERE permission_id = ?");
            statement.setInt(1, id); // Permission ID
            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            if (rowsAffected > 0) {
                showPopupNotification("Permission removed successfully.", NotificationManager.NotificationType.SUCCESS);
                showConsoleNotification("Permission removed successfully.", NotificationManager.NotificationType.SUCCESS);
                return true;
            } else {
                showPopupNotification("Failed to remove permission.", NotificationManager.NotificationType.ERROR);
                showConsoleNotification("Failed to remove permission.", NotificationManager.NotificationType.ERROR);
                return false;
            }
        } catch (SQLException se) {
            showPopupNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            showConsoleNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            return false;
        }
    }

    public static boolean deleteRow(String selectedNode) {
        try {
            Connection connection = DBConnection.getConnectionInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Tienda.roles WHERE role_name = ?");
            statement.setString(1, selectedNode); // Role name
            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            if (rowsAffected > 0) {
                showPopupNotification("Role deleted successfully.", NotificationManager.NotificationType.SUCCESS);
                showConsoleNotification("Role deleted successfully.", NotificationManager.NotificationType.SUCCESS);
                return true;
            } else {
                showPopupNotification("Failed to delete role.", NotificationManager.NotificationType.ERROR);
                showConsoleNotification("Failed to delete role.", NotificationManager.NotificationType.ERROR);
                return false;
            }
        } catch (SQLException se) {
            showPopupNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            showConsoleNotification(se.getMessage(), NotificationManager.NotificationType.ERROR);
            return false;
        }
    }

    public static int getRoleId(String roleName, Connection connection) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String query = "SELECT role_id FROM Tienda.roles WHERE role_name = ?";
        pstmt = connection.prepareStatement(query);
        pstmt.setString(1, roleName);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("role_id");
        }
        return -1; // Role not found
    }


    
    public static String getRoleName(int id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = DBConnection.getConnectionInstance().getConnection();

        String query = "SELECT role_name FROM Tienda.roles WHERE role_id = ?";
        pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("role_name");
        }
        return query; // Role not found
    }
}
