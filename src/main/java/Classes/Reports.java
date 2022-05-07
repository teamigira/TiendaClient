/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.AbstractClasses.DailyReport;
import static Database.DBConnect.getConnection;
import static com.nkanabo.Tienda.Utilities.DateMilli;
import static com.nkanabo.Tienda.Utilities.milliConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import static javax.xml.datatype.DatatypeConstants.DATETIME;

/**
 *
 * @author Nkanabo
 */
public class Reports {
    
   public static DateFormat df;
   public static ArrayList listDailyReport() throws ParseException{
        ArrayList<DailyReport> list = new ArrayList<DailyReport>();
        ArrayList rowValues = new ArrayList();
        
        
        
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
         // STEP 3: Execute a query 
         stmt = conn.createStatement();
         LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));
         String mydate = d1 + "";
         String query2 = "SELECT * from production_products JOIN production_categories ON production_products.category_id = production_categories.category_id"
                 + " JOIN production_brands ON production_brands.brand_id = production_products.brand_id JOIN sales_order_items ON sales_order_items.product_id = "
             + "production_products.product_id WHERE date = '" + milliConverter(mydate) +"' ";
         ResultSet rs = stmt.executeQuery(query2);
                  
          int b = 0;
          double profit = 0.0;
          double totalreturns = 0.0;
          double totalinvestment = 0.0;
          double netProfit = 0.0;
          double superprofit = 0.0;
    
         while(rs.next()){
           String product_name = rs.getString("production_products.product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String list_price = rs.getString("production_products.list_price");
           String retail_price = rs.getString("production_products.retail_price");
           String quantity = rs.getString("sales_order_items.quantity");
           String date = rs.getString("sales_order_items.date");
           date = DateMilli(date);       
           
      
           profit = (Double.parseDouble(list_price) - Double.parseDouble(retail_price));
           int c = Integer.parseInt(quantity);
           totalreturns +=Double.parseDouble(list_price) * c;
           totalinvestment += Double.parseDouble(retail_price)* c;
           superprofit += profit * c;
          
           
           list.add( new DailyReport(
           product_name,
           retail_price,
           list_price,
           profit,
           totalreturns,
           totalinvestment,
           c,
           superprofit,
           date));  
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
    
      public static ArrayList listWeeklyReport() throws ParseException{
        ArrayList<DailyReport> list = new ArrayList<DailyReport>();
        ArrayList rowValues = new ArrayList();
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
         //STEP 3: Execute a query 
         stmt = conn.createStatement();  
         
         LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris")).minusDays( 7 );
         long weekend = milliConverter(String.valueOf(d1));
         
         String query2 = "SELECT * from production_products JOIN production_categories ON production_products.category_id = production_categories.category_id"
                 + " JOIN production_brands ON production_brands.brand_id = production_products.brand_id JOIN sales_order_items ON sales_order_items.product_id = "
             + "production_products.product_id where sales_order_items.date > '" +weekend+"'";
         
         ResultSet rs = stmt.executeQuery(query2);
                  
          int b = 0;
          double profit = 0.0;
          double totalreturns = 0.0;
          double totalinvestment = 0.0;
          double netProfit = 0.0;
          double superprofit = 0.0;
    
         while(rs.next()){
           String product_name = rs.getString("production_products.product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String list_price = rs.getString("production_products.list_price");
           String retail_price = rs.getString("production_products.retail_price");
           String quantity = rs.getString("sales_order_items.quantity");
           String date = rs.getString("sales_order_items.date");
           date = DateMilli(date);
           
      
           profit = (Double.parseDouble(list_price) - Double.parseDouble(retail_price));
           int c = Integer.parseInt(quantity);
           totalreturns +=Double.parseDouble(list_price) * c;
           totalinvestment += Double.parseDouble(retail_price)* c;
           superprofit += profit * c;
          
           
           list.add( new DailyReport(
           product_name,
           retail_price,
           list_price,
           profit,
           totalreturns,
           totalinvestment,
           c,
           superprofit,
           date));  
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
        
      public static ArrayList listMonthlyReport() throws ParseException{
        ArrayList<DailyReport> list = new ArrayList<DailyReport>();
        ArrayList rowValues = new ArrayList();
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
         // STEP 3: Execute a query 
         stmt = conn.createStatement();
         
         LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris")).minusDays( 31 );
         long weekend = milliConverter(String.valueOf(d1));
         
         String query2 = "SELECT * from production_products JOIN production_categories ON production_products.category_id = production_categories.category_id"
                 + " JOIN production_brands ON production_brands.brand_id = production_products.brand_id JOIN sales_order_items ON sales_order_items.product_id = "
                 + "production_products.product_id where sales_order_items.date > '" +weekend+"'";
         ResultSet rs = stmt.executeQuery(query2);
                  
          int b = 0;
          double profit = 0.0;
          double totalreturns = 0.0;
          double totalinvestment = 0.0;
          double netProfit = 0.0;
          double superprofit = 0.0;
    
         while(rs.next()){
           String product_name = rs.getString("production_products.product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String list_price = rs.getString("production_products.list_price");
           String retail_price = rs.getString("production_products.retail_price");
           String quantity = rs.getString("sales_order_items.quantity");
           String date = rs.getString("sales_order_items.date");
           date = DateMilli(date);
           
      
           profit = (Double.parseDouble(list_price) - Double.parseDouble(retail_price));
           int c = Integer.parseInt(quantity);
           totalreturns +=Double.parseDouble(list_price) * c;
           totalinvestment += Double.parseDouble(retail_price)* c;
           superprofit += profit * c;
          
           
           list.add( new DailyReport(
           product_name,
           retail_price,
           list_price,
           profit,
           totalreturns,
           totalinvestment,
           c,
           superprofit,
           date));  
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
         
      public static ArrayList DatedReport(String db) throws ParseException{
        ArrayList<DailyReport> list = new ArrayList<DailyReport>();
        ArrayList rowValues = new ArrayList();
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
         // STEP 3: Execute a query 
         stmt = conn.createStatement();  
         long fb = milliConverter(db);
         
         
        String query2 = "SELECT * from production_products JOIN production_categories ON production_products.category_id = production_categories.category_id"
                 + " JOIN production_brands ON production_brands.brand_id = production_products.brand_id JOIN sales_order_items ON sales_order_items.product_id = "
             + "production_products.product_id WHERE date='"+fb+"'";
         ResultSet rs = stmt.executeQuery(query2);
                  
          int b = 0;
          double profit = 0.0;
          double totalreturns = 0.0;
          double totalinvestment = 0.0;
          double netProfit = 0.0;
          double superprofit = 0.0;
    
         while(rs.next()){
           String product_name = rs.getString("production_products.product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String list_price = rs.getString("production_products.list_price");
           String retail_price = rs.getString("production_products.retail_price");
           String quantity = rs.getString("sales_order_items.quantity");
           String date = rs.getString("sales_order_items.date");
           date = DateMilli(date);
      
           profit = (Double.parseDouble(list_price) - Double.parseDouble(retail_price));
           int c = Integer.parseInt(quantity);
           totalreturns +=Double.parseDouble(list_price) * c;
           totalinvestment += Double.parseDouble(retail_price)* c;
           superprofit += profit * c;
          
           
           list.add( new DailyReport(
           product_name,
           retail_price,
           list_price,
           profit,
           totalreturns,
           totalinvestment,
           c,
           superprofit,
           date));  
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
          
       public static ArrayList MonthlyReport(){
        ArrayList<DailyReport> list = new ArrayList<DailyReport>();
        ArrayList rowValues = new ArrayList();
        try {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
         // STEP 3: Execute a query 
         stmt = conn.createStatement();  
         
         String query2 = "SELECT * from production_products JOIN production_categories ON production_products.category_id = production_categories.category_id"
                 + " JOIN production_brands ON production_brands.brand_id = production_products.brand_id JOIN sales_order_items ON sales_order_items.product_id = "
             + "production_products.product_id";
         ResultSet rs = stmt.executeQuery(query2);
                  
          int b = 0;
          double profit = 0.0;
          double totalreturns = 0.0;
          double totalinvestment = 0.0;
          double netProfit = 0.0;
          double superprofit = 0.0;
    
         while(rs.next()){
           String product_name = rs.getString("production_products.product_name");
           String brand_name = rs.getString("brand_name");
           String category_name = rs.getString("category_name");
           String list_price = rs.getString("production_products.list_price");
           String retail_price = rs.getString("production_products.retail_price");
           String quantity = rs.getString("sales_order_items.quantity");
           String date = rs.getString("sales_order_items.date");
           date = DateMilli(date);
                 
           profit = (Double.parseDouble(list_price) - Double.parseDouble(retail_price));
           int c = Integer.parseInt(quantity);
           totalreturns +=Double.parseDouble(list_price) * c;
           totalinvestment += Double.parseDouble(retail_price)* c;
           superprofit += profit * c;
          
           
           list.add( new DailyReport(
           product_name,
           retail_price,
           list_price,
           profit,
           totalreturns,
           totalinvestment,
           c,
           superprofit,
           date));  
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
