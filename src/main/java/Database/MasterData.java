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
import static Classes.Functions.Categories.addCategory;
import static Classes.Functions.Crudes.addBrand;
import Classes.Utilities.NotificationManager.NotificationType;

import static Classes.Functions.Permissions.PermissionFileManager.addPermissionsRole;
import static Classes.Utilities.NotificationManager.showConsoleNotification;
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
                // Factory password 12345; the login screen makes the owner
                // choose a new one the first time it is used.
                try (java.sql.PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO Tienda.sales_staffs "
                        + " (first_name, last_name, email, password, phone, active, store_id, manager_id, role_id) VALUES "
                        + "('admin', 'admin', 'admin@mail.com', ?, '+255000000', '1', '1', '1', '1')")) {
                    ins.setString(1, Authentication.Passwords.hash("12345"));
                    ins.executeUpdate();
                }
            }

            // Check if the app key exists
            String appKeyCheckQuery = "SELECT activation_status FROM Tienda.system_app_key WHERE activation_status='1' OR activation_status='0'";
            ResultSet appKeyResultSet = stmt.executeQuery(appKeyCheckQuery);
            if (!appKeyResultSet.next()) {
                String appKeyInsertQuery = "INSERT INTO Tienda.system_app_key VALUES (1, ?, ?, 0, ?, ?)";
                Database.Sql.prepare(stmt.getConnection(), appKeyInsertQuery, app_version, product_key, date, expiredate).executeUpdate();
            }

            // Insert permissions into permissions table
            String[] usersPermissions = {"view_users", "edit_users", "disable_users", "enable_users", "activate_users"};
            String[] salesPermissions = {"view_sales","sale"};
            String[] productsPermissions = {"view_products","save_product"};
            String[] marketingPermissions = {"view_customers"};
            String[] superPermissions = {"view_users", "view_roles_permissions","edit_role","delete_role"};
            String[] reportsPermissions = {"view_reports"};
            //For new category add in in inserPermissions to save it also
            //insert into addPermissionForRole to assign them for administrator.

            // Create the role first: insertPermissions also grants new
            // permissions to it.
            String roleName = "Administrator";
            String description = "This role has full access and control over all functionalities and features of the system.";
            insertRole(stmt, roleName, description);

            insertPermissions(stmt, usersPermissions, "users");
            insertPermissions(stmt, salesPermissions, "sales");
            insertPermissions(stmt, marketingPermissions, "marketing");
            insertPermissions(stmt, superPermissions, "super");
            insertPermissions(stmt, productsPermissions, "products");
            insertPermissions(stmt, reportsPermissions, "reports");


            addPermissionsForRole(roleName,usersPermissions);
            addPermissionsForRole(roleName, superPermissions);
            addPermissionsForRole(roleName, salesPermissions);
            addPermissionsForRole(roleName, marketingPermissions);
            addPermissionsForRole(roleName, productsPermissions);
            addPermissionsForRole(roleName, reportsPermissions);
            
            addBrandsCategories();


            // STEP 4: Clean-up environment 
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            Classes.Utilities.Log.error(se);
        } catch (Exception e) {
            // Handle errors for Class.forName
            Classes.Utilities.Log.error(e);
        } finally {
            // finally block used to close resources 
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                Classes.Utilities.Log.error(se2);
            } // nothing we can do 

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                Classes.Utilities.Log.error(se);
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
          
            // Names and categories are fixed in the code above, not user input.
            for (int i = 0; i < permissionsToInsert.size(); i++) {
                permissionInsertQuery.append("('").append(permissionsToInsert.get(i)).append("', '").append(category).append("')");
                if (i < permissionsToInsert.size() - 1) {
                    permissionInsertQuery.append(",");
                }
            }
            stmt.executeUpdate(permissionInsertQuery.toString());

            // Grant every new permission to Administrator (after inserting them).
            for (String permission : permissionsToInsert) {
                addPermissionsRole("Administrator", permission);
            }
    
            // Notify user of successful insertion
            String successMessage = "Permissions inserted successfully.";
           
            showConsoleNotification(successMessage, NotificationType.SUCCESS);
        } else {
            // Notify user that no permissions were inserted
            String infoMessage = "No new permissions to insert.";
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
            String roleInsertQuery = "INSERT INTO Tienda.roles (role_name, description) VALUES (?, ?)";
            Database.Sql.prepare(stmt.getConnection(), roleInsertQuery, roleName, description).executeUpdate();
            
            // Notify user of successful insertion
            String successMessage = "Role '" + roleName + "' inserted successfully.";
            showConsoleNotification(successMessage, NotificationType.SUCCESS);
        }
        // Already there on every start after the first: nothing to do.
    }
    
    

    private static boolean checkRoleExists(Statement stmt, String roleName) throws SQLException {
        String roleCheckQuery = "SELECT COUNT(*) AS count FROM Tienda.roles WHERE role_name = ?";
        ResultSet resultSet = Database.Sql.prepare(stmt.getConnection(), roleCheckQuery, roleName).executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        return count > 0;
    }

    private static void addBrandsCategories() throws SQLException, ClassNotFoundException {
        addBrand("General");
        addCategory("General");
    }

}
