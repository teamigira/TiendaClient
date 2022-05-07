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

import static Database.DBConnect.getConnection;
import static com.nkanabo.Tienda.Main.app_version;
import static com.nkanabo.Tienda.Main.product_key;
import static com.nkanabo.Tienda.Utilities.date;
import static com.nkanabo.Tienda.Utilities.expiredate;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.sql.Statement;

public class MasterData {
    
    public static void Master() throws SQLException{    
      Connection conn = getConnection();
       Statement stmt = conn.createStatement();
        
      try{
         // STEP 3: Execute a query 
         stmt = conn.createStatement();  
         
         String sqlquery = "SELECT staff_id FROM sales_staffs"
                 + " where staff_id='1'"; 
         ResultSet rs = stmt.executeQuery(sqlquery); 
         if(rs.next()) { 
         }
         else {
         String sql =
                 "INSERT INTO sales_staffs " + "VALUES (1, 'administrator',"
               + "'user1', 'admin@gmail.com','X85ITzVZ7xLqf6S3DdrMGQ==','+2558374893','1','1','1','1')";
         
         String sql2 =
        "INSERT INTO production_categories (category_name)" + "VALUES ('General')";
         
         String sql3 =
        "INSERT INTO production_brands (brand_name)" + "VALUES ('General')";
        
         stmt.executeUpdate(sql); 
         stmt.executeUpdate(sql2); 
         stmt.executeUpdate(sql3);
         }
         
                     
         String appquery = "SELECT activation_status FROM app_key where activation_status='1'"
                 + " OR activation_status='0'"; 
         ResultSet res = stmt.executeQuery(appquery);
         if(res.next()) {
             //rows exist
         }
         else
         {
         String appsql =
                "INSERT INTO app_key VALUES (1,'" + app_version +"','"
                + product_key + "',"
                + "0,'" + date + "','"+ expiredate +"')";         
         stmt.executeUpdate(appsql);
         }
         
         // STEP 4: Clean-up environment 
         stmt.close(); 
         conn.close(); 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } catch(Exception e) { 
         // Handle errors for Class.forName 
         e.printStackTrace(); 
      } finally { 
         // finally block used to close resources 
         try {
            if(stmt!=null) stmt.close();  
         } catch(SQLException se2) { 
         } // nothing we can do 
         try { 
            if(conn!=null) conn.close(); 
         } catch(SQLException se) { 
            se.printStackTrace(); 
         } // end finally try 
      } // end try 
   } 

    }

