/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Order;
import static Classes.Functions.Products.getProductId;
import static Classes.Functions.Products.getProductPrice;
import static Classes.Functions.Products.getProductPricefromName;
import static Classes.Functions.Stocks.Sales;
import Database.DBConnection;
import Interface.UserInterface;
import static com.nkanabo.Tienda.Utilities.IntegerConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import static com.nkanabo.Tienda.Utilities.milliConverter;
import static com.nkanabo.Tienda.Utilities.unique;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nkanabo
 */
public class Orders {
    

    public static boolean addOrder(String product_id, double quantity, Double discount, String backdated) throws ParseException, ClassNotFoundException {
        //To change body of generated methods, choose Tools | Templates
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            //System.out.println(product_id.substring(0, product_id.indexOf('-')));
            //product_id = (product_id.split(" - ", 2)[0]);
            product_id = product_id.split(":")[1];
            LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));
            if (backdated != null) {
                d1 = LocalDate.parse(backdated);
            }
            String today = "" + d1;
            String price = getProductPrice(product_id);
            String sql
                    = "INSERT INTO sales_order_items"
                    + "(product_id,quantity,list_price,discount,date) VALUES ('" + product_id + "',"
                    + "'" + quantity + "','"+ price +"','" + discount + "','" + milliConverter(today) + "')";
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

   
    public static Boolean editBrand(String id, String brandname)
            throws SQLException, ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 

            String updatequery
                    = "UPDATE production_brands set brand_name='" + brandname + "'"
                    + "where brand_id='" + id + "'";
            int rsu = stmt.executeUpdate(updatequery);
            if (rsu != 1) {
                return false;
            }

            // STEP 4: Clean-up environment

        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return true;
    }

    public static Boolean deleteBrand(int id) throws SQLException, ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            String updatequery
                    = "DELETE production_brands where brand_id='" + id + "'";
            int rsu = stmt.executeUpdate(updatequery);
            if (rsu != 1) {
                return false;
            }

            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return true;

    }

    public static ArrayList listOrders() throws SQLException, ClassNotFoundException, ParseException {

        ArrayList<Order> list = new ArrayList<Order>();
        ArrayList rowValues = new ArrayList();
        try {
//            Connection conna = DBConnection.getConnectionInstance().getConnection();
            DBConnection dbc = DBConnection.getConnectionInstance();
            LocalDate today = LocalDate.now(ZoneId.of("Europe/Paris"));
            String dateToday = String.valueOf(today);
            Long dateofOrder;
            dateofOrder = milliConverter(dateToday);
            // STEP 3: Execute a query 
            String sqlquery = "SELECT * FROM Tienda.sales_order_items"
                    + " JOIN production_products ON"
                    + " sales_order_items.product_id = production_products.product_id"
                    + " WHERE date = '"+ dateofOrder + "'"
                    + " ORDER BY order_id DESC";
//             Statement stmt = conna.createStatement();
                    Connection con = dbc.getConnection();
                    Statement stmt;
                    stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {

                String quantity = rs.getString("quantity");
                String listprice = rs.getString("production_products.list_price");
                String discount = rs.getString("discount");
                list.add(new Order(false,rs.getString("order_id"),
                        rs.getString("product_name"),
                        quantity,
                        Double.parseDouble(listprice),
                        Double.parseDouble(discount)));
            }
            // STEP 4: Clean-up environment 
            
        } catch (SQLException se) {
            // Handle errors for JDBC
        }
        return list;
    }
    
    public static ArrayList listOrdersDated(String date) throws ParseException, ClassNotFoundException{
       ArrayList<Order> list = new ArrayList<Order>();
        ArrayList rowValues = new ArrayList();
        try {
//            Connection conna = DBConnection.getConnectionInstance().getConnection();
            DBConnection dbc = DBConnection.getConnectionInstance();
            Long dateofOrder;
            dateofOrder = milliConverter(date);
            // STEP 3: Execute a query
            String sqlquery = "SELECT * FROM Tienda.sales_order_items"
                    + " JOIN production_products ON"
                    + " sales_order_items.product_id = production_products.product_id"
                    + " WHERE date = '"+ dateofOrder + "'"
                    + " ORDER BY order_id DESC";
//             Statement stmt = conna.createStatement();
                    Connection con = dbc.getConnection();
                    Statement stmt;
                    stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {

                String quantity = rs.getString("quantity");
                String listprice = rs.getString("production_products.list_price");
                String discount = rs.getString("discount");
                list.add(new Order(false,rs.getString("order_id"),
                        rs.getString("product_name"),
                        quantity,
                        Double.parseDouble(listprice),
                        Double.parseDouble(discount)));
            }
            // STEP 4: Clean-up environment 
            
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;  
    }

    public static boolean deleteAll() throws ClassNotFoundException {
        try {
           Connection conna = DBConnection.getConnectionInstance().getConnection();
           Statement stmt = conna.createStatement();

            String updatequery
                    = "DELETE from Tienda.system_sales_order_items";
            int rsu = stmt.executeUpdate(updatequery);
            if (rsu != 1) {
                return false;
            }

            // STEP 4: Clean-up environment 
           

        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return true;
    }

    public static boolean deleteRow(int row, double quantity) throws ClassNotFoundException {
        int productCode = 0;
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            String filterProduct
                    = "Select product_id from Tienda.sales_order_items where order_id = '" + row + "'";
             ResultSet rst = stmt.executeQuery(filterProduct);
            while (rst.next()) {
                productCode = IntegerConverter(rst.getString("product_id"));
            }
            
            String updatequery
                    = "DELETE from Tienda.sales_order_items where order_id = '" + row + "'";
            
            String sql
                    = "UPDATE production_stocks SET quantity = quantity+"+quantity+" WHERE product_id='" + productCode + "'";
            int i = stmt.executeUpdate(sql);
          
             
            int rsu = stmt.executeUpdate(updatequery);
            if (rsu != 1) {
                JOptionPane.showMessageDialog(null, "There was a problem item "+row+" not found",
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

    public static boolean updateOrder(int order_id, String product_name, double quantity, Double price, Double discount, String backdated) throws ParseException, ClassNotFoundException {
        //To change body of generated methods, choose Tools | Templates
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
//            // STEP 3: Execute a query
            Products products = new Products();
            String productCode;
            productCode = Products.getProductId(product_name);
            LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));

//            if (backdated != null) {
//                d1 = LocalDate.parse(backdated);
//            }
            
            String today = "" + d1;
            String sql
             ="UPDATE sales_order_items SET quantity = '" + quantity + "', list_price = '"+ price +"', discount = '" + discount + "' WHERE order_id = '" + order_id + "'";
            int i = stmt.executeUpdate(sql);
            if (i > 0) {
                System.out.println(sql);
            } else {
                JOptionPane.showMessageDialog(null, "There was a problem item "+sql+" not found",
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
    
   public static boolean addOrderFromName(String product_name, double quantity, Double discount, String backdated) throws ClassNotFoundException, ParseException{
         //To change body of generated methods, choose Tools | Templates
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query
            LocalDate d1;
            backdated = null;
            if (backdated != null) {
                d1 = LocalDate.parse(backdated);
            }
            else {
                d1 = LocalDate.now(ZoneId.of("Europe/Paris"));
            }
            String today = "" + d1;
            String price = getProductPricefromName(product_name);
            String product_id = getProductId(product_name);
            String sql
                    = "INSERT INTO Tienda.sales_order_items"
                    + "(product_id, quantity, list_price, discount, date) VALUES ('" + product_id + "',"
                    + "'" + quantity + "','"+ price +"','" + discount + "','" + milliConverter(today) + "')";
            int i = stmt.executeUpdate(sql);
            if (i > 0) {
                System.out.println(sql);
            } else {
                return false;
            }

            //STEP 4: Clean-up environment 

        } catch (SQLException se) {
            // Handle errors for JDBC

        }
        return true;
   }
}
