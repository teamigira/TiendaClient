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
import java.util.Arrays;

/**
 *
 * @author Nkanabo
 */
public class MasterTables {
     public static void createTables() throws Exception {

         String dropTables[] = {
                "DROP TABLE IF EXISTS  app_key;",
                "DROP TABLE IF EXISTS  production_brands;",
                "DROP TABLE IF EXISTS production_products;",
                "DROP TABLE IF EXISTS  sales_customers",
                "DROP TABLE IF EXISTS  sales_staffs;",
                "DROP TABLE IF EXISTS  sales_order_items;",
                "DROP TABLE IF EXISTS  Accounts;",
                "DROP TABLE IF EXISTS production_categories;",
                "DROP TABLE IF EXISTS production_stocks;",
                "DROP TABLE IF EXISTS sales_stores ;",
                "DROP TABLE IF EXISTS sales_orders ;",
     };
         
         String dbsql = SchemaQuery;
         Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         /*
         for (int i = 0; i < dropTables.length; i++) {
             stmt.executeUpdate(dropTables[i]);
         } */
         
        System.out.println(dbsql);
        stmt.executeUpdate(dbsql);
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
