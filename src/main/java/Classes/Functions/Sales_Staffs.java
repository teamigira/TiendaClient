/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Authentication.Encrpytion;
import Classes.AbstractClasses.EditedUser;
import Classes.AbstractClasses.EditedUserData;
import Classes.AbstractClasses.SelectedStaff;
import Classes.AbstractClasses.Staff;
import Classes.AbstractClasses.UserData;
import Classes.Utilities.NotificationManager.NotificationType;
import static Classes.Utilities.NotificationManager.showConsoleNotification;
import static Classes.Utilities.NotificationManager.showPopupNotification;
import static Classes.Utilities.RandomNumbers.generateNumber;
import Database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Nkanabo
 */
public class Sales_Staffs {

    public static boolean addStaff(Staff newStaff) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Get database connection
            conn = DBConnection.getConnectionInstance().getConnection();
    
            // SQL statement for insertion
            String sql = "INSERT INTO Tienda.sales_staffs (first_name, last_name, email, password, phone, active, store_id, manager_id, role_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
            // Create prepared statement
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
            // Set parameter values
            pstmt.setString(1, newStaff.staff_name);
            pstmt.setString(2, newStaff.sur_name);
            pstmt.setString(3, newStaff.staff_email);
            pstmt.setString(4, newStaff.password);
            pstmt.setString(5, newStaff.phone_no);
            pstmt.setString(6, newStaff.Status);
            pstmt.setString(7, newStaff.store);
            pstmt.setString(8, newStaff.manager_id);
            pstmt.setInt(9, newStaff.role);
    
            // Execute the statement
            int rowsAffected = pstmt.executeUpdate();
            
            // Check if insertion was successful
            if (rowsAffected > 0) {
                // Retrieve the generated staff_id
                rs = pstmt.getGeneratedKeys();
                int staffId = 0;
                if (rs.next()) {
                    staffId = rs.getInt(1);
                }
                // Show success notification
                showPopupNotification("Staff added successfully with ID: " + staffId, NotificationType.SUCCESS);
                showConsoleNotification("Staff added successfully with ID: " + staffId, NotificationType.SUCCESS);
                return true;
            } else {
                // Show failure notification
                showPopupNotification("Failed to add staff. Please try again.", NotificationType.ERROR);
                showConsoleNotification("Failed to add staff. Please try again.", NotificationType.ERROR);
                return false;
            }
        } catch (SQLException se) {
            // Handle SQL exceptions
            se.printStackTrace();
            // Show failure notification
            showPopupNotification("An error occurred while adding staff. Please try again.", NotificationType.ERROR);
            showConsoleNotification("An error occurred while adding staff. Please try again.", NotificationType.ERROR);
            return false;
        } finally {
            // Close resources
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    

    public static boolean addtestUsers() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnectionInstance().getConnection();
        try {
            // Prepare the SQL statement
            String sql = "INSERT INTO Tienda.sales_staffs (staff_id, first_name, last_name, email, password, phone, active, store_id, manager_id, role_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Create a prepared statement
            PreparedStatement pstmt = connection.prepareStatement(sql);
            String pass = "12345";
            Encrpytion enc = new Encrpytion();
            String password = enc.encrypt(pass);
            // Set up loop to add more than 50 random users
            int totalUsers = 100; // Change this to the desired number of users
            int rowsAffected = 0;

            for (int i = 0; i < totalUsers; i++) {
                // Generate random values for each user
                int staffId = generateNumber(); // Assuming generateNumber() generates unique staff IDs
                String staffName = "User" + i;
                String surName = "Lastname" + i;
                String staffEmail = "user" + i + "@example.com";
                String phoneNo = "+1" + String.format("%09d", i); // Generate random 10-digit phone number
                String store = "" + (i % 10); // Assuming there are 10 stores
                int status = i % 2; // Alternates between 0 and 1
                int managerId = i % 10; // Assuming there are 10 managers
                int role = i % 10; // Assuming there are 10 roles

                // Set parameter values in the prepared statement
                pstmt.setInt(1, staffId);
                pstmt.setString(2, staffName);
                pstmt.setString(3, surName);
                pstmt.setString(4, staffEmail);
                pstmt.setString(5, password);
                pstmt.setString(6, phoneNo);
                pstmt.setInt(7, status);
                pstmt.setInt(8, i % 10);
                pstmt.setInt(9, managerId);
                pstmt.setInt(10, role);

                // Execute the statement
                rowsAffected += pstmt.executeUpdate();
            }

            // Print the total number of rows affected
            System.out.println("Total rows affected: " + rowsAffected);

            // Close the prepared statement
            pstmt.close();
            // Close the connection
            connection.close();

            return rowsAffected > 0;
        } catch (SQLException se) {
            // Handle SQL exceptions
            se.printStackTrace();
            return false;
        }
    }

    public static ArrayList LoadStaffs() throws ClassNotFoundException {
        ArrayList<Staff> list = new ArrayList<Staff>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM Tienda.sales_staffs";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                String firstname = rs.getString("first_name");
                String lastname = rs.getString("last_name");
                String email = rs.getString("email");
                String role = rs.getString("role_id");
                String phone_no = rs.getString("phone");
                String store = rs.getString("store_id");

                String Status = rs.getString("active");
                String manager_id = rs.getString("manager_id");
                int roles = Integer.parseInt(role);
                String staff_id = rs.getString("staff_id");
                list.add(
                        new Staff(firstname, lastname,
                                email, phone_no, store, Status,
                                manager_id, roles, staff_id));
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;
    }

    public static boolean editStaff(EditedUserData editedUser)
            throws SQLException, ClassNotFoundException {
    
        // Get the Staff object from the UserData object
        boolean changePassword = editedUser.changePassword;
        // Get the changePassword flag
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnectionInstance().getConnection();

            // SQL query to update staff information
            String sql = "UPDATE Tienda.sales_staffs "
                    + "SET first_name = ?, last_name = ?, email = ?, "
                    + (changePassword ? "password = ?, " : "")
                    + // Include password update if changePassword is true
                    "phone = ?, active = ?, store_id = ?, manager_id = ?, role_id = ? "
                    + "WHERE staff_id = ?";

            // Create prepared statement
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            int parameterIndex = 1;
            pstmt.setString(parameterIndex++, editedUser.editedUser.staff_name);
            pstmt.setString(parameterIndex++, editedUser.editedUser.sur_name);
            pstmt.setString(parameterIndex++, editedUser.editedUser.staff_email);
            if (changePassword) {
                pstmt.setString(parameterIndex++, editedUser.editedUser.password);
            }
            pstmt.setString(parameterIndex++, editedUser.editedUser.phone_no);
            pstmt.setString(parameterIndex++, editedUser.editedUser.Status);
            pstmt.setInt(parameterIndex++, Integer.parseInt(editedUser.editedUser.store));
            pstmt.setInt(parameterIndex++, Integer.parseInt(editedUser.editedUser.manager_id));
            pstmt.setInt(parameterIndex++, editedUser.editedUser.role);
            pstmt.setString(parameterIndex++, editedUser.editedUser.userid); // Assuming staffId is the ID of the staff being edited

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();

            // Set success flag based on whether rows were affected
            success = rowsAffected > 0;

            // Show notification
            if (success) {
                showPopupNotification("Staff information updated successfully", NotificationType.SUCCESS);
                showConsoleNotification("Staff information updated successfully", NotificationType.SUCCESS);
            } else {
                showPopupNotification("Failed to update staff information", NotificationType.ERROR);
                showConsoleNotification("Failed to update staff information", NotificationType.ERROR);
            }
        } catch (SQLException ex) {
            // Handle exceptions
            showPopupNotification("An error occurred: " + ex.getMessage(), NotificationType.ERROR);
            showConsoleNotification("An error occurred: " + ex.getMessage(), NotificationType.ERROR);
        } finally {
            // Close resources
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public static boolean StaffStatus(String staffId, int type) throws SQLException, ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            String sql = "UPDATE Tienda.sales_staffs SET active = ? WHERE staff_id = ?";
            PreparedStatement pstmt = conna.prepareStatement(sql);

            // Set parameters based on the type
            switch (type) {
                case 0:
                    pstmt.setInt(1, 0); // Disable user
                    break;
                case 1:
                    pstmt.setInt(1, 1); // Enable user
                    break;
                default:
                    // Invalid type, handle error
                    throw new IllegalArgumentException("Invalid type parameter");
            }

            pstmt.setString(2, staffId); // Set staff ID

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0; // Return true if rows were affected
        } catch (SQLException ex) {
            // Handle exceptions
            ex.printStackTrace();
            return false; // Return false if an error occurred
        }

    }

}
