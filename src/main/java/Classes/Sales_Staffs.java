/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.AbstractClasses.Staff;
import static Authentication.Encrpytion.encrypt;
import static Database.DBConnect.getConnection;
import Database.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Nkanabo
 */
public class Sales_Staffs {
    
    public static boolean addStaff(String staff_name, String sur_name, String staff_email, int role) throws ClassNotFoundException{
        
        try {
           Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
        // STEP 3: Execute a query 
        stmt = conna.createStatement(); 
        String password = "12345";
        password = encrypt(password);
        String sql =
        "INSERT INTO sales_staffs (first_name,last_name,email,password,active,role_id) " + "VALUES ('"+staff_name+"','"+sur_name+"','"+staff_email+"','"+password+"','1','"+role+"')";
        int i = stmt.executeUpdate(sql);
        
        if (i > 0) {
            System.out.println(sql);
        } else {
            return false;
        }
        
        // STEP 4: Clean-up environment 
                 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
        return true;
    }
   
    public static ArrayList LoadStaffs() throws ClassNotFoundException{
        ArrayList<Staff> list = new ArrayList<Staff>();
        ArrayList rowValues = new ArrayList();
        try {
           Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         stmt = conna.createStatement();  
         String sqlquery = "SELECT * FROM sales_staffs"; 
         ResultSet rs = stmt.executeQuery(sqlquery);
         while(rs.next()){
           String firstname = rs.getString("first_name");
           String lastname = rs.getString("last_name");
           String email = rs.getString("email");
           String role = rs.getString("role_id");
           int roles = Integer.parseInt(role);
           list.add(
                   new Staff(firstname, lastname, email, roles));           
         }
         // STEP 4: Clean-up environment 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
            System.out.println(se);
      } 
      return list; 
    }
}
