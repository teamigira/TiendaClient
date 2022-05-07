/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import static Database.DBConnect.getConnection;
import static Database.Schema.SchemaQuery;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nkanabo
 */
public class MasterTables {
     public static void createTables() throws Exception {
        
         String dropTables = "DROP TABLE app_key;"
                 + "DROP TABLE production_brands; "
                 + "DROP TABLE production_products; "
                 + "DROP TABLE sales_customers; "
                 + "DROP TABLE sales_staffs; "
                 + "DROP TABLE sales_order_items; "
                 + "DROP TABLE  app_key "
                 + "DROP TABLE Accounts ";
         
         String dbsql = SchemaQuery;
         
         Connection conn = getConnection();
         Statement stmt = conn.createStatement();
//         stmt.executeUpdate(dropTables);
         stmt.executeUpdate(dbsql);
         System.out.println("Created tables in database"); 
         // STEP 4: Clean-up environment 
         stmt.close(); 
         conn.close();  
         //Handle errors for JDBC 
        
         try{ 
            if(stmt!=null) stmt.close(); 
         } catch(SQLException se2) { 
         } // nothing we can do 
         try { 
            if(conn!=null) conn.close(); 
         } catch(SQLException se){ 
            se.printStackTrace(); 
         } //end finally try 
      } //end try 
}
