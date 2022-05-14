/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.AbstractClasses.Product;
import static Database.DBConnect.getConnection;
import Database.DBConnection;
import Interface.UserInterface;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nkanabo
 */
public class Products {
    public static boolean addProduct(String product_name,
    int brand_id,
    int category_id,
    int model_year,
    String expiry_date,
    double list_price,
    double retail_price) throws URISyntaxException, ClassNotFoundException {
      //To change body of generated methods, choose Tools | Templates
      
      try {
           Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         String check;
         check="SELECT product_name FROM production_products"
                 + " WHERE product_name = '"+product_name+"'";
         ResultSet rscheck = stmt.executeQuery(check);
          if(rscheck.next()) {
              
              JOptionPane.showMessageDialog(new UserInterface(), 
                      "Product exists", "Warning",
                      JOptionPane.WARNING_MESSAGE);
              return false;
              }
            
                 else {
             
         String sql =
         "INSERT INTO production_products "
                 + "(product_name,brand_id,category_id,model_year,"
                 + "expiry_date,list_price,retail_price) VALUES"
                 + " ('"+product_name+"','"+brand_id+"','"+category_id+"',"
                 + "'"+model_year+"','"+expiry_date+"','"+list_price+"',"
                 + "'"+retail_price+"')";
        int i = stmt.executeUpdate(sql);
        if (i > 0) {
            System.out.println(sql);
        } else {
            return false;
        }
          }
         // STEP 4: Clean-up environment 
        
         
         
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
        return true;
    }
    
    
    public static Boolean editBrand(String id, String brandname)
            throws SQLException, ClassNotFoundException{
      try {
          Connection conna = DBConnection.getConnectionInstance().getConnection();
          Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         
          String updatequery =
                  "UPDATE production_brands set brand_name='"+brandname+"'"
                  + "where brand_id='"+id+"'"; 
                int rsu = stmt.executeUpdate(updatequery); 
        if(rsu!=1){
            return false;
        } 
       
         // STEP 4: Clean-up environment
        
         
         
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
        return true;
    }
    
    
    public static Boolean deleteBrand(int id) throws SQLException, ClassNotFoundException{
      try {
         Connection conna = DBConnection.getConnectionInstance().getConnection();
         Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         stmt = conna.createStatement();  
        
          String updatequery =
          "DELETE production_brands where brand_id='"+id+"'"; 
                int rsu = stmt.executeUpdate(updatequery);
                if(rsu!=1) {
                return false;
                }
         // STEP 4: Clean-up environment 
        
         
         
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
        return true;
    
    }
    
    public static ArrayList listProducts() throws SQLException, ClassNotFoundException{
        ArrayList<Product> list = new ArrayList<Product>();
        ArrayList rowValues = new ArrayList();
        try {
           Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         stmt = conna.createStatement();  
         String sqlquery = "SELECT * FROM production_products JOIN"
                 + " production_brands ON production_products.brand_id ="
                 + " production_brands.brand_id JOIN production_categories ON"
                 + " production_products.category_id = "
                 + "production_categories.category_id"; 
         ResultSet rs = stmt.executeQuery(sqlquery);
         while(rs.next()){
           int productid = Integer.parseInt(rs.getString("product_id"));
           String product_name = rs.getString("product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String model = rs.getString("model_year");
           String expiry_date = rs.getString("expiry_date");
           String list_price = rs.getString("list_price");
           String retail_price = rs.getString("retail_price");         
                 System.out.println(""+product_name+category_name);   
           list.add(
           new Product(
           productid,
           product_name,
           brand_name,
           category_name,
           Integer.parseInt(model),
           expiry_date,
           Double.parseDouble(list_price),
           Double.parseDouble(retail_price)
                             )
                    );           
         }
         // STEP 4: Clean-up environment 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
      return list; 
    }
    
    
    
    public static ArrayList listStockProducts() throws SQLException, ClassNotFoundException{
              
        ArrayList<Product> list = new ArrayList<Product>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         stmt = conna.createStatement();  
         String sqlquery = "SELECT * FROM production_products JOIN"
                 + " production_brands ON production_products.brand_id ="
                 + " production_brands.brand_id JOIN production_categories ON"
                 + " production_products.category_id = "
                 + " production_categories.category_id "
                 + "JOIN production_stocks ON production_products.product_id = "
                 + "production_stocks.product_id  WHERE production_stocks.quantity>0"; 
         ResultSet rs = stmt.executeQuery(sqlquery);
         while(rs.next()){
           int productid = Integer.parseInt(rs.getString("product_id"));
           String product_name = rs.getString("product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String model = rs.getString("model_year");
           String expiry_date = rs.getString("expiry_date");
           String list_price = rs.getString("list_price");
           String retail_price = rs.getString("retail_price");
           int quantity = Integer.parseInt(rs.getString("quantity"));
             
           list.add(
           new Product(
           productid,
           product_name,
           brand_name,
           category_name,
           Integer.parseInt(model),
           expiry_date,
           Double.parseDouble(list_price),
           Double.parseDouble(retail_price)
                             )
                    );           
         }
         // STEP 4: Clean-up environment 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
      return list; 
    }
    
    
    
    public static ArrayList listProductOnly() throws SQLException, ClassNotFoundException{
              
        ArrayList<Product> list = new ArrayList<Product>();
        ArrayList rowValues = new ArrayList();
        try {
           Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
         // STEP 3: Execute a query 
         stmt = conna.createStatement();  
         String sqlquery = "SELECT * FROM production_products JOIN"
                 + " production_brands ON production_products.brand_id ="
                 + " production_brands.brand_id JOIN production_categories ON"
                 + " production_products.category_id = "
                 + " production_categories.category_id"; 
         ResultSet rs = stmt.executeQuery(sqlquery);
         while(rs.next()){
           int productid = Integer.parseInt(rs.getString("product_id"));
           String product_name = rs.getString("product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String model = rs.getString("model_year");
           String expiry_date = rs.getString("expiry_date");
           String list_price = rs.getString("list_price");
           String retail_price = rs.getString("retail_price");
           int quantity = 0;
           
           list.add(
           new Product(
           productid,
           product_name,
           brand_name,
           category_name,
           Integer.parseInt(model),
           expiry_date,
           Double.parseDouble(list_price),
           Double.parseDouble(retail_price)
                             )
                    );           
         }
         // STEP 4: Clean-up environment 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } 
      return list; 
    }
}
