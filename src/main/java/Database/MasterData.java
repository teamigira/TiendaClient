/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author Nkanabo
 */
import Classes.Utilities.NotificationManager.NotificationType;

import static Classes.Functions.Permissions.PermissionFileManager.addPermissionsRole;
import static Classes.Utilities.NotificationManager.showConsoleNotification;
import static Classes.Utilities.NotificationManager.showPopupNotification;
import static Database.DBConnect.getConnection;
import static com.nkanabo.Tienda.Main.app_version;
import static com.nkanabo.Tienda.Main.product_key;
import static com.nkanabo.Tienda.Utilities.date;
import static com.nkanabo.Tienda.Utilities.expiredate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MasterData {

    public static void InsertMasterData() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        try {
            // Check if the admin staff exists
            String adminCheckQuery = "SELECT staff_id FROM Tienda.sales_staffs WHERE staff_id='1'";
            ResultSet adminResultSet = stmt.executeQuery(adminCheckQuery);
            if (!adminResultSet.next()) {
                String adminInsertQuery = "INSERT INTO Tienda.sales_staffs "
                        + " (first_name, last_name, email, password, phone, active, store_id, manager_id, role_id) VALUES "
                        + "('admin', 'admin', 'admin@mail.com', 'X85ITzVZ7xLqf6S3DdrMGQ==', "
                        + "'+255000000', '1', '1', '1', '1')";
                stmt.executeUpdate(adminInsertQuery);
            }

            // Check if the app key exists
            String appKeyCheckQuery = "SELECT activation_status FROM Tienda.system_app_key WHERE activation_status='1' OR activation_status='0'";
            ResultSet appKeyResultSet = stmt.executeQuery(appKeyCheckQuery);
            if (!appKeyResultSet.next()) {
                String appKeyInsertQuery = "INSERT INTO Tienda.system_app_key VALUES "
                        + "(1, '" + app_version + "', '" + product_key + "', 0, '" + date + "', '" + expiredate + "')";
                stmt.executeUpdate(appKeyInsertQuery);
            }

            // Insert permissions into permissions table
            String[] usersPermissions = {"view_users", "edit_users", "disable_users", "enable_users", "activate_users"};
            String[] salesPermissions = {"view_sales"};
            String[] productsPermissions = {"view_products"};
            String[] marketingPermissions = {"view_customers"};
            String[] superPermissions = {"view_users", "view_roles_permissions"};
            String[] reportsPermissions = {"view_reports"};

            insertPermissions(stmt, usersPermissions, "users");
            insertPermissions(stmt, salesPermissions, "sales");
            insertPermissions(stmt, marketingPermissions, "marketing");
            insertPermissions(stmt, superPermissions, "super");
            insertPermissions(stmt, productsPermissions, "products");
            insertPermissions(stmt, reportsPermissions, "reports");


            String roleName = "Administrator";
            String description = "This role has full access and control over all functionalities and features of the system.";
            insertRole(stmt, roleName, description);
            addPermissionsForRole(roleName,usersPermissions);
            addPermissionsForRole(roleName, superPermissions);
            addPermissionsForRole(roleName, salesPermissions);
            addPermissionsForRole(roleName, marketingPermissions);
            addPermissionsForRole(roleName, productsPermissions);
            addPermissionsForRole(roleName, reportsPermissions);
            



            // STEP 4: Clean-up environment 
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources 
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            } // nothing we can do 

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } // end finally try 
        } // end try 
    }
    private static void insertPermissions(Statement stmt, String[] permissions, String category) throws SQLException {
        // Prepare the permission names as a comma-separated list for the SQL query
        // String permissionNames = String.join(",", permissions);
         // Prepare the permission names as a comma-separated list for the SQL query

        String permissionNames = Arrays.stream(permissions)
        .map(permission -> "'" + permission + "'") // Quote each permission name
        .collect(Collectors.joining(","));
    
        // Check if the permissions already exist
        String permissionCheckQuery = "SELECT permission_name FROM Tienda.permissions WHERE permission_name IN (" + permissionNames + ")";
        ResultSet permissionResultSet = stmt.executeQuery(permissionCheckQuery);
        List<String> existingPermissions = new ArrayList<>();
        while (permissionResultSet.next()) {
            existingPermissions.add(permissionResultSet.getString("permission_name"));
        }
    
        // Insert only the permissions that do not already exist
        List<String> permissionsToInsert = Arrays.stream(permissions)
                .filter(permission -> !existingPermissions.contains(permission))
                .collect(Collectors.toList());
    
        if (!permissionsToInsert.isEmpty()) {
            // Prepare the permission insert query
            StringBuilder permissionInsertQuery = new StringBuilder("INSERT INTO Tienda.permissions (permission_name, category) VALUES ");
          
            for (int i = 0; i < permissionsToInsert.size(); i++) {
                permissionInsertQuery.append("('").append(permissionsToInsert.get(i)).append("', '").append(category).append("')");
                if (i < permissionsToInsert.size() - 1) {
                      //Insert them for Administrator
                    addPermissionsRole("Administrator",permissionsToInsert.get(i));
                    permissionInsertQuery.append(",");
                }
            }
    
            // Execute the permission insert query
            stmt.executeUpdate(permissionInsertQuery.toString());
    
            // Notify user of successful insertion
            String successMessage = "Permissions inserted successfully.";
            showPopupNotification(successMessage, NotificationType.SUCCESS);
            showConsoleNotification(successMessage, NotificationType.SUCCESS);
        } else {
            // Notify user that no permissions were inserted
            String infoMessage = "No new permissions to insert.";
            showPopupNotification(infoMessage, NotificationType.INFO);
            showConsoleNotification(infoMessage, NotificationType.INFO);
        }
    }
    

    private static void addPermissionsForRole(String roleName, String[] permissions) {
        for (String permission : permissions) {
            addPermissionsRole(roleName, permission);
        }
        
        // Handle SQL exceptions
        // Notify user of successful insertion
        String successMessage = "Permissions added successfully for role: " + roleName;
        showConsoleNotification(successMessage, NotificationType.SUCCESS);
        }
        
    

    public static void insertRole(Statement stmt, String roleName, String description) throws SQLException {
        // Check if the role already exists
        boolean roleExists = checkRoleExists(stmt, roleName);
        
        if (!roleExists) {
            // Insert the role
            String roleInsertQuery = "INSERT INTO Tienda.roles (role_name, description) VALUES ('" + roleName + "', '" + description + "')";
            stmt.executeUpdate(roleInsertQuery);
            
            // Notify user of successful insertion
            String successMessage = "Role '" + roleName + "' inserted successfully.";
            showPopupNotification(successMessage, NotificationType.SUCCESS);
            showConsoleNotification(successMessage, NotificationType.SUCCESS);
        } else {
            // Notify user that the role already exists
            String infoMessage = "Role '" + roleName + "' already exists.";
            showPopupNotification(infoMessage, NotificationType.ERROR);
            showConsoleNotification(infoMessage, NotificationType.ERROR);
        }
    }

    private static boolean checkRoleExists(Statement stmt, String roleName) throws SQLException {
        String roleCheckQuery = "SELECT COUNT(*) AS count FROM Tienda.roles WHERE role_name = '" + roleName + "'";
        ResultSet resultSet = stmt.executeQuery(roleCheckQuery);
        resultSet.next();
        int count = resultSet.getInt("count");
        return count > 0;
    }

}
