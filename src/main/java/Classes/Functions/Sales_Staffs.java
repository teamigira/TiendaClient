/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Staff;
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
    
    public static boolean addStaff(
    String staffname, String last_name, String email, String pass,
            String phone, int active, int str, int manager, int role  ,int type      
    ) throws SQLException, ClassNotFoundException{
      
           Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();      
            int c = generateNumber();
        try {
            String sql = "INSERT INTO Tienda.sales_staffs (staff_id, first_name, last_name, email, password, phone, active, store_id, manager_id, role_id) " 
           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected;
               // Set parameter values
               try ( // Create a prepared statement
                       PreparedStatement pstmt = conna.prepareStatement(sql)) {
                   // Set parameter values
                   pstmt.setInt(1, c); // Assuming c is the staff_id
                   pstmt.setString(2, staffname);
                   pstmt.setString(3, last_name);
                   pstmt.setString(4, email);
                   pstmt.setString(5, pass);
                   pstmt.setString(6, phone);
                   pstmt.setInt(7, active);
                   pstmt.setInt(8, str);
                   pstmt.setInt(9, manager);
                   pstmt.setInt(10, role);
                   // Execute the statement
                   // Execute the statement
                   rowsAffected = pstmt.executeUpdate();
               } // Assuming c is the staff_id
    return rowsAffected > 0;
} catch (SQLException se) {
    // Handle SQL exceptions
    se.printStackTrace();
    return false;
}      
    }
    
     public static boolean addtestUsers() throws SQLException, ClassNotFoundException{
         Connection connection = DBConnection.getConnectionInstance().getConnection();
            try {
                // Prepare the SQL statement
                String sql = "INSERT INTO Tienda.sales_staffs (staff_id, first_name, last_name, email, password, phone, active, store_id, manager_id, role_id) " 
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // Create a prepared statement
                PreparedStatement pstmt = connection.prepareStatement(sql);

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
                    pstmt.setInt(5, managerId);
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
    public static ArrayList LoadStaffs() throws ClassNotFoundException{
        ArrayList<Staff> list = new ArrayList<Staff>();
        ArrayList rowValues = new ArrayList();
        try {
           Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         stmt = conna.createStatement();  
         String sqlquery = "SELECT * FROM Tienda.sales_staffs"; 
         ResultSet rs = stmt.executeQuery(sqlquery);
         while(rs.next()){
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
                   new Staff(firstname, lastname, email,phone_no,store, Status,manager_id, roles,staff_id));           
         }
         // STEP 4: Clean-up environment 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace();
      } 
      return list; 
    }

        public static boolean editStaff(
        String staffname, String last_name, String email, String pass, 
                String phone, int active, int str, int manager,
                int role, int type, boolean selectToChange, String staffId)
                throws SQLException, ClassNotFoundException {
        
            if (type == 0) { // Check if the type is 0 (indicating editing is allowed)
           Connection conna = DBConnection.getConnectionInstance().getConnection();
           Statement stmt = conna.createStatement();
                // Get database connection
            
            // SQL query to update staff information
            String sql = "UPDATE Tienda.sales_staffs " +
                    "SET first_name = ?, last_name = ?, email = ?, " +
                    (selectToChange ? "password = ?, " : "") + // Include password update if selectToChange is true
                    "phone = ?, active = ?, store_id = ?, manager_id = ?, role_id = ? " +
                    "WHERE staff_id = ?";

            // Create prepared statement
            PreparedStatement pstmt  = conna.prepareStatement(sql);

            // Set parameters
            int parameterIndex = 1;
            pstmt.setString(parameterIndex++, staffname);
            pstmt.setString(parameterIndex++, last_name);
            pstmt.setString(parameterIndex++, email);
            if (selectToChange) {
                pstmt.setString(parameterIndex++, pass);
            }
            pstmt.setString(parameterIndex++, phone);
            pstmt.setInt(parameterIndex++, active);
            pstmt.setInt(parameterIndex++, str);
            pstmt.setInt(parameterIndex++, manager);
            pstmt.setInt(parameterIndex++, role);
            pstmt.setString(parameterIndex++, staffId); // Assuming staffId is the ID of the staff being edited

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();

            // Return true if rows were affected (update successful), false otherwise
            return rowsAffected > 0;
            } else {
        // Editing not allowed when type is not 0
        return false;
    }
    }

    public static boolean StaffStatus(String staffId,int type) throws SQLException, ClassNotFoundException {
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
        } catch (SQLException | ClassNotFoundException ex) {
            // Handle exceptions
            ex.printStackTrace();
            return false; // Return false if an error occurred
        }

        }
    
}
