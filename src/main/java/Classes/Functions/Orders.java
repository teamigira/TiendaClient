/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import static Authentication.Sessions.LoggedUser;
import Classes.AbstractClasses.Order;
import static Classes.Functions.Products.getProductId;
import static Classes.Functions.Products.getProductPrice;
import static Classes.Functions.Products.getProductPricefromName;
import static Classes.Functions.Stocks.Sales;
import Database.DBConnection;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
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
            LocalDate d1 = LocalDate.now(ZoneId.systemDefault());
            if (backdated != null) {
                d1 = LocalDate.parse(backdated);
            }
            String today = "" + d1;
            String price = getProductPrice(product_id);
            int i = Database.Sql.prepare(conna,
                    "INSERT INTO sales_order_items"
                    + "(product_id,quantity,list_price,discount,date) VALUES (?,?,?,?,?)",
                    product_id, quantity, price, discount, milliConverter(today)).executeUpdate();
            if (i <= 0) {
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

            int rsu = Database.Sql.prepare(conna,
                    "UPDATE production_brands SET brand_name = ? WHERE brand_id = ?",
                    brandname, id).executeUpdate();
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
            int rsu = Database.Sql.prepare(conna,
                    "DELETE FROM production_brands WHERE brand_id = ?", id).executeUpdate();
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

    //Shows orders that are not saved. In  sales order table
    public static ArrayList listOrders() throws SQLException, ClassNotFoundException, ParseException {

        ArrayList<Order> list = new ArrayList<Order>();
        ArrayList rowValues = new ArrayList();
        try {
//            Connection conna = DBConnection.getConnectionInstance().getConnection();
            DBConnection dbc = DBConnection.getConnectionInstance();
            LocalDate today = LocalDate.now(ZoneId.systemDefault());
            String dateToday = String.valueOf(today);
            Long dateofOrder;
            
            dateofOrder = milliConverter(dateToday);
            
            String sqlquery = "SELECT * FROM Tienda.sales_order_items"
                + " JOIN Tienda.production_products ON"
                + " Tienda.sales_order_items.item_id = Tienda.production_products.code"
                + " WHERE NOT EXISTS ("
                + "     SELECT 1 FROM Tienda.sales_orders"
                + "     WHERE Tienda.sales_order_items.order_id = Tienda.sales_orders.order_id"
                + ")"
                + " AND (Tienda.sales_order_items.status IS NULL"
                + "      OR Tienda.sales_order_items.status = 'CART')";

            // STEP 3: Execute a query 
//            String sqlquery = "SELECT * FROM Tienda.sales_order_items"
//                    + " JOIN Tienda.production_products ON"
//                    + " Tienda.sales_order_items.item_id = Tienda.production_products.code"
//                    + " ORDER BY order_id DESC";
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
                        rs.getString("sales_order_items.product_id"),
                        rs.getString("production_products.product_name"),
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
    
    //This is used to fetch specific date report
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
                    + " WHERE date = ?"
                    + " ORDER BY order_id DESC";
            Connection con = dbc.getConnection();
            ResultSet rs = Database.Sql.prepare(con, sqlquery, dateofOrder).executeQuery();
            while (rs.next()) {

                String quantity = rs.getString("quantity");
                String listprice = rs.getString("production_products.list_price");
                String discount = rs.getString("discount");
                list.add(new Order(false,rs.getString("order_id"),
                             rs.getString("product_id"),
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
            ResultSet rst = Database.Sql.prepare(conna,
                    "SELECT product_id FROM Tienda.sales_order_items WHERE order_id = ?", row).executeQuery();
            while (rst.next()) {
                productCode = IntegerConverter(rst.getString("product_id"));
            }
            
            Database.Sql.prepare(conna,
                    "UPDATE production_stocks SET quantity = quantity + CAST(? AS DOUBLE PRECISION) WHERE product_id = ?",
                    quantity, productCode).executeUpdate();
            int rsu = Database.Sql.prepare(conna,
                    "DELETE FROM Tienda.sales_order_items WHERE order_id = ?", row).executeUpdate();
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
            LocalDate d1 = LocalDate.now(ZoneId.systemDefault());

//            if (backdated != null) {
//                d1 = LocalDate.parse(backdated);
//            }
            
            String today = "" + d1;
            int i = Database.Sql.prepare(conna,
                    "UPDATE sales_order_items SET quantity = ?, list_price = ?, discount = ? WHERE order_id = ?",
                    quantity, price, discount, order_id).executeUpdate();
            if (i <= 0) {
                JOptionPane.showMessageDialog(null, "There was a problem: order item " + order_id + " not found",
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
                d1 = LocalDate.now(ZoneId.systemDefault());
            }
            String today = "" + d1;
            String price = getProductPricefromName(product_name);
            String product_id = getProductId(product_name);
            int i = Database.Sql.prepare(conna,
                    "INSERT INTO Tienda.sales_order_items"
                    + "(product_id, quantity, item_id, discount, date) VALUES (?,?,?,?,?)",
                    product_id, quantity, product_id, discount, milliConverter(today)).executeUpdate();
            if (i <= 0) {
                return false;
            }

            //STEP 4: Clean-up environment 

        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();

        }
        return true;
   }
   
   //Checks if the ID is already used.
   public static boolean checkOrderIdValidity(String code) throws SQLException, ClassNotFoundException {
        Connection conna
                = DBConnection.getConnectionInstance()
                        .getConnection();
        Statement stmt = conna.createStatement();
        String id = "0";
        ResultSet rs = Database.Sql.prepare(conna,
                "SELECT customer_id FROM Tienda.sales_orders WHERE customer_id = ?", code).executeQuery();
        return rs.next();
    }
   
   public static boolean saveOrderItem(
           String order_id,
           String product_id,int randomNumber) throws ParseException, ClassNotFoundException {
        //To change body of generated methods, choose Tools | Templates
        try {
        // Get a connection to the database
        Connection connection = DBConnection.getConnectionInstance().getConnection();

        // Create a statement
        Statement statement = connection.createStatement();

        // Get the current date and time
        Date now = new Date();

        // Create a SimpleDateFormat object with the desired date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // Format the Date object using the SimpleDateFormat object
        String formattedDate = sdf.format(now);

        // Define other variables needed for the insertion
        int status = 1;
        String price = getProductPrice(product_id);
        
        // Construct the SQL query for insertion
        String sql = "INSERT INTO Tienda.sales_orders "
                   + "(order_id, customer_id, order_status, product, order_date, shipped_date, store_id, staff_id) "
                   + "VALUES (?,?,?,?,?,?,?,?)";
        int rowsAffected = Database.Sql.prepare(connection, sql, order_id, randomNumber, status,
                product_id, formattedDate, formattedDate, formattedDate, status).executeUpdate();

        // Check if the insertion was successful
        if (rowsAffected > 0) {
            System.out.println("Insertion successful: " + sql);
        } else {
            System.out.println("Insertion failed: " + sql);
            return false;
        }

    } catch (SQLException se) {
        // Handle SQL exceptions
        se.printStackTrace();
        return false;
    }
    return true;

   }

   // ===================================================================
   // Hold / Reopen support (Phase 5)
   // ===================================================================

   /** True if there is at least one item in the active cart. */
   public static boolean hasCartItems() {
       String sql = "SELECT COUNT(*) FROM Tienda.sales_order_items i "
               + "WHERE (i.status IS NULL OR i.status = 'CART') "
               + "AND NOT EXISTS (SELECT 1 FROM Tienda.sales_orders o WHERE o.order_id = i.order_id)";
       try (Connection c = DBConnection.getConnectionInstance().getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
           return rs.next() && rs.getInt(1) > 0;
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * Parks every item in the active cart under a new hold reference so the
    * cashier can start a fresh sale and come back to this one later.
    *
    * @param label a human note (e.g. customer name) shown in the reopen list
    * @return the hold reference, or {@code null} if nothing was held
    */
   public static String holdCurrentCart(String label) {
       if (!hasCartItems()) {
           return null;
       }
       String ref = "H" + System.currentTimeMillis();
       String sql = "UPDATE Tienda.sales_order_items i "
               + "SET status = 'HELD', hold_ref = ?, hold_label = ? "
               + "WHERE (i.status IS NULL OR i.status = 'CART') "
               + "AND NOT EXISTS (SELECT 1 FROM Tienda.sales_orders o WHERE o.order_id = i.order_id)";
       try (Connection c = DBConnection.getConnectionInstance().getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
           ps.setString(1, ref);
           ps.setString(2, label == null ? "" : label.trim());
           return ps.executeUpdate() > 0 ? ref : null;
       } catch (SQLException e) {
           e.printStackTrace();
           return null;
       }
   }

   /**
    * Lists the parked (held) sales, one row per hold reference.
    *
    * @return rows of {hold_ref, label, item count, total value}
    */
   public static ArrayList<String[]> listHeldOrders() {
       ArrayList<String[]> out = new ArrayList<>();
       String sql = "SELECT i.hold_ref, MAX(i.hold_label) AS label, COUNT(*) AS items, "
               + "SUM(CAST(i.quantity AS DOUBLE) * p.list_price) AS total "
               + "FROM Tienda.sales_order_items i "
               + "JOIN Tienda.production_products p ON i.item_id = p.code "
               + "WHERE i.status = 'HELD' AND i.hold_ref IS NOT NULL "
               + "GROUP BY i.hold_ref ORDER BY i.hold_ref DESC";
       try (Connection c = DBConnection.getConnectionInstance().getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
           while (rs.next()) {
               out.add(new String[]{
                   rs.getString("hold_ref"),
                   rs.getString("label") == null ? "" : rs.getString("label"),
                   rs.getString("items"),
                   rs.getString("total")});
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return out;
   }

   /** Returns a held sale's items to the active cart. */
   public static boolean reopenHeld(String ref) {
       String sql = "UPDATE Tienda.sales_order_items "
               + "SET status = 'CART', hold_ref = NULL, hold_label = NULL "
               + "WHERE hold_ref = ? AND status = 'HELD'";
       try (Connection c = DBConnection.getConnectionInstance().getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
           ps.setString(1, ref);
           return ps.executeUpdate() > 0;
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }

   /**
    * Lists recently completed sales, one row per receipt number.
    *
    * @return rows of {receipt no (customer_id), item count, order date}
    */
   public static ArrayList<String[]> listRecentCompleted(int limit) {
       ArrayList<String[]> out = new ArrayList<>();
       String sql = "SELECT customer_id, COUNT(*) AS items, MAX(order_date) AS d "
               + "FROM Tienda.sales_orders "
               + "GROUP BY customer_id ORDER BY MAX(order_date) DESC, customer_id DESC "
               + "LIMIT ?";
       try (Connection c = DBConnection.getConnectionInstance().getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
           ps.setInt(1, limit);
           try (ResultSet rs = ps.executeQuery()) {
               while (rs.next()) {
                   out.add(new String[]{
                       rs.getString("customer_id"),
                       rs.getString("items"),
                       rs.getString("d")});
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return out;
   }

   /**
    * Reopens a completed sale: deletes its sales_orders rows so the items fall
    * back into the active cart (the customer forgot something). Stock is left
    * as-is since it was already deducted when the items were first added.
    */
   public static boolean reopenCompleted(String receiptNo) {
       String sql = "DELETE FROM Tienda.sales_orders WHERE customer_id = ?";
       try (Connection c = DBConnection.getConnectionInstance().getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
           ps.setString(1, receiptNo);
           return ps.executeUpdate() > 0;
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }

}
