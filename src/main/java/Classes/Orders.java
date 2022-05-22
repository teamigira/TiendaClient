/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.AbstractClasses.Order;
import static Classes.Stocks.Sales;
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
import java.text.ParseException;
import javax.swing.JOptionPane;

/**
 *
 * @author Nkanabo
 */
public class Orders {

    public static boolean addOrder(String order_id, String item_id,
            String product_id, long quantity, Double price, Double discount, String backdated) throws ParseException, ClassNotFoundException {
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

            String sql
                    = "INSERT INTO sales_order_items"
                    + "(item_id,product_id,quantity,list_price,discount,date) VALUES ('" + item_id + "','" + product_id + "',"
                    + "'" + quantity + "','" + price + "','" + discount + "','" + milliConverter(today) + "')";
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

    public static ArrayList listOrders() throws SQLException, ClassNotFoundException {

        ArrayList<Order> list = new ArrayList<Order>();
        ArrayList rowValues = new ArrayList();
        try {
//            Connection conna = DBConnection.getConnectionInstance().getConnection();
            DBConnection dbc = DBConnection.getConnectionInstance();
            // STEP 3: Execute a query 
            String sqlquery = "SELECT * FROM sales_order_items JOIN production_products ON"
                    + " sales_order_items.product_id = production_products.product_id ORDER BY order_id DESC ";
//             Statement stmt = conna.createStatement();
                    Connection con = dbc.getConnection();
                    Statement stmt = null;
                    stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {

                String quantity = rs.getString("quantity");
                String listprice = rs.getString("production_products.list_price");
                String discount = rs.getString("discount");
                list.add(new Order(rs.getString("order_id"),
                        rs.getString("product_name"),
                        Integer.parseInt(quantity),
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
                    = "DELETE from sales_order_items";
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

    public static boolean deleteRow(int row, int quantity) throws ClassNotFoundException {
        int productCode = 0;
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            String filterProduct
                    = "Select product_id from sales_order_items where order_id = '" + row + "'";
             ResultSet rst = stmt.executeQuery(filterProduct);
            while (rst.next()) {
                productCode = IntegerConverter(rst.getString("product_id"));
            }
            
            String updatequery
                    = "DELETE from sales_order_items where order_id = '" + row + "'";
            
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

    public static boolean updateOrder(int order_id, String item_id, String product_id, long quantity, Double price, Double discount, String backdated) throws ParseException, ClassNotFoundException {
        //To change body of generated methods, choose Tools | Templates
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
//            // STEP 3: Execute a query
              String id;
              id = product_id.split(":")[1];
              int productCode = IntegerConverter(product_id);
            LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));

            if (backdated != null) {
                d1 = LocalDate.parse(backdated);
            }
            
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

}
