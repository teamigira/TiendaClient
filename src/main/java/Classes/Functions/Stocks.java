/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Stock;
import static Classes.Functions.Products.getProductId;
import Database.DBConnection;
import static com.nkanabo.Tienda.Utilities.DoubleConverter;
import static com.nkanabo.Tienda.Utilities.IntegerConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nkanabo
 */
public class Stocks {

    public int product_id;
    public int quantity;
    public static Double Sales;

    public static boolean addStock(String product_id, int quantity)
            throws ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().
                    getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String pid;
            pid = product_id.split(":")[1];
            String sql
                    = "INSERT INTO production_stocks (product_id,quantity)"
                    + "VALUES ('" + pid + "','" + quantity + "')";
            String sql2
                    = "INSERT INTO production_stocks_report (store_id,product_id,quantity)"
                    + "VALUES ('1'," + pid + "','" + quantity + "')";
            int i = stmt.executeUpdate(sql);
            int j = stmt.executeUpdate(sql2);
            if (i > 0) {
                System.out.println(sql);
            } else {
                return false;
            }

            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return true;
    }

    public static String getRemainingStock(String id) throws ClassNotFoundException {
        
        String remainder = "0";
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM Tienda.production_stocks"
                    + " where product_id = '"+id+"'";
            ResultSet rs = stmt.executeQuery(sqlquery);
            if (rs.next()) {
                remainder = rs.getString("quantity");
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return remainder;
    }
    
    
    public static boolean editStock(String product_name, Double quantity)
            throws ClassNotFoundException, SQLException {
        String id;
        id = getProductId(product_name);
//        id = product_id.split(":")[1];
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            
            Double remainder = Double.parseDouble(getRemainingStock(id));
//            Double qty = Double.parseDouble(quantity);
            Double count = remainder - quantity;
            if(count < 0){
                return false;
            }
            String sql
                    = "UPDATE production_stocks SET quantity ='"+count+"' WHERE product_id='" + id + "'";
            int i = stmt.executeUpdate(sql);
            if (i > 0) {
                System.out.println(sql);
            } else {
                return false;
            }

            // STEP 4: Clean-up environment
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return true;
    }

    
     public static boolean updateStock(int id, long quantity)
            throws ClassNotFoundException {
        
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();

            String sql
                    = "UPDATE Tienda.production_stocks SET quantity = " + quantity + " WHERE product_id='" + id + "'";
            
            String sql2
                    = "UPDATE Tienda.production_stocks_report SET quantity = " + quantity + " WHERE product_id='" + id + "'"
                    + " AND date01 = (SELECT MAX(date01) FROM production_stocks_report)";
            
            int i=0;
            try{
            i = stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            }catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
            if (i > 0) {
                System.out.println(sql2);
            } else {
                return false;
            }

            // STEP 4: Clean-up environment
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return true;
    }

     
    public static ArrayList listStocks() throws ClassNotFoundException {
        ArrayList<Stock> list = new ArrayList<>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM Tienda.production_stocks JOIN Tienda.production_products ON production_stocks.product_id=production_products.product_id";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                //rowValues.add(rs.getInt("brand_id"), rs.getString("brand_name"));
                list.add(new Stock(rs.getString("product_id"),rs.getString("product_name"),
                        rs.getString("quantity")));
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;
    }

    public static boolean editStockFromOrdersEd(int order_id, String product_name, Double quantity)
            throws ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            String productCode = getProductId(product_name);
            String sqlquery = "SELECT * FROM Tienda.sales_order_items where product_id = "
                    + productCode + " AND order_id=" + order_id + "";
            ResultSet rst = stmt.executeQuery(sqlquery);
            while (rst.next()) {
                String Sales_Quantity = rst.getString("quantity");
                Sales = DoubleConverter(Sales_Quantity);
            }
            try {
                String sql
                        = "UPDATE production_stocks SET quantity = quantity+" + Sales + "-" + quantity + " WHERE product_id='" + productCode + "'";
                int i = stmt.executeUpdate(sql);
                if (i > 0) {
                    System.out.println("Success " + sql);
                } else {
                    System.out.println("Failed " + sql);
                    return false;
                }
            } catch (SQLException me) {
                JOptionPane.showMessageDialog(null, me + "",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            // STEP 4: Clean-up environment
        } catch (SQLException se) {
            // Handle errors for JDBC

        }
        return true;
    }

    public static boolean deleteStocks(int productId) throws ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            String sql
                    = "DELETE from Tienda.production_stocks where product_id = '" + productId + "'";
            int i = stmt.executeUpdate(sql);
            if (i != 1) {
                JOptionPane.showMessageDialog(null, "There was a problem item " + productId + " not found",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return true;
    }

    public static void crawlStock() throws ClassNotFoundException, ParseException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery
                    = "SELECT"
                    + " production_stocks.quantity,production_products.product_name,"
                    + "production_products.product_id"
                    + " FROM Tienda.production_stocks JOIN Tienda.production_products ON "
                    + "production_stocks.product_id"
                    + "=production_products.product_id where"
                    + " production_stocks.quantity <= 3";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                //Operation to populate the message dialog
                //for the user to notice the depletion
                String a = rs.getString("product_name");
                String b = rs.getString("quantity");
                String c = rs.getString("product_id");
                Notifications.notify(a, b, c);
            }
            // STEP 4: Clean-up environment
        } catch (SQLException se) {
            // Handle errors for JDBC

        }
    }
}
