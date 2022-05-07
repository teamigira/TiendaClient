/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.AbstractClasses.Stock;
import static Database.DBConnect.getConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Nkanabo
 */
public class Stocks {
    public int product_id;
    public int quantity;
    
    
    public static boolean addStock(String product_id, int quantity){
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        // STEP 3: Execute a query 
        stmt = conn.createStatement();  
        String pid;
        pid = product_id.split(":")[1];
        String sql =
        "INSERT INTO production_stocks (product_id,quantity)" + "VALUES ('"+pid+"','"+quantity+"')";
        int i = stmt.executeUpdate(sql);
        if (i > 0) {
            System.out.println(sql);
        } else {
            return false;
        }
        
        // STEP 4: Clean-up environment 
        stmt.close(); 
        conn.close(); 
        
         
         
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
        return true;
    }
    
    
    public static boolean editStock(String product_id, long quantity){
        String id;
        id = product_id.split(":")[1];
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        // STEP 3: Execute a query 
        stmt = conn.createStatement();  
        
        String sql =
        "UPDATE production_stocks SET quantity = quantity-"+quantity+" WHERE product_id='"+id+"'";
        int i = stmt.executeUpdate(sql);
        if (i > 0) {
            System.out.println(sql);
        } else {
            return false;
        }
        
        // STEP 4: Clean-up environment 
        stmt.close(); 
        conn.close(); 
        
         
         
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
        return true;
    }
    
    public static ArrayList listStocks(){
        ArrayList<Stock> list = new ArrayList<Stock>();
        ArrayList rowValues = new ArrayList();
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
         // STEP 3: Execute a query 
         stmt = conn.createStatement();  
         String sqlquery = "SELECT * FROM production_stocks JOIN production_products ON production_stocks.product_id=production_products.product_id"; 
         ResultSet rs = stmt.executeQuery(sqlquery);
         while(rs.next()){
        //rowValues.add(rs.getInt("brand_id"), rs.getString("brand_name"));
        list.add(new Stock(rs.getString("product_name"),
                Integer.parseInt(rs.getString("quantity"))));
         }
         // STEP 4: Clean-up environment 
         stmt.close(); 
         conn.close(); 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
      return list; 
    }
}
