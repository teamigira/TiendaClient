/*
 * To change this license header, choose License
 * Headers in Project Properties. To change this
 * template file, choose Tools | Templates and
 * open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Product;
import Database.DBConnection;
import Interface.UserInterface;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Nkanabo
 */
public class Products {
  public static boolean addProduct(
      String product_name, int brand_id,
      int category_id, int model_year,
      String expiry_date, double list_price,
      double retail_price)
      throws URISyntaxException,
             ClassNotFoundException,
             ParseException {
    // To change body of generated methods, choose
    // Tools | Templates

    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt = conna.createStatement();
      // STEP 3: Execute a query
      String check;
      check =
          "SELECT product_name FROM production_products"
          + " WHERE product_name = '"
          + product_name + "'";
      ResultSet rscheck =
          stmt.executeQuery(check);
      if (rscheck.next()) {
        JOptionPane.showMessageDialog(
            new UserInterface(), "Product exists",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
        return false;
      } else {
        String sql =
            "INSERT INTO production_products "
            + "(product_name,brand_id,category_id,model_year,"
            + "expiry_date,list_price,retail_price) VALUES"
            + " ('" + product_name + "','"
            + brand_id + "','" + category_id
            + "',"
            + "'" + model_year + "','"
            + expiry_date + "','" + list_price
            + "',"
            + "'" + retail_price + "')";
        int i = stmt.executeUpdate(sql);
        if (i > 0) {
          System.out.println(sql);
        } else {
          return false;
        }
      }
      // STEP 4: Clean-up environment

    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();
    }
    return true;
  }

  public static boolean RegisterProduct(
      Object[] obj) throws URISyntaxException,
                           ClassNotFoundException,
                           ParseException {
    ArrayList<ProductDetails> product =
        new ArrayList<ProductDetails>();

    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt = conna.createStatement();
      // STEP 1: Ensure if the product does not
      // exist
      String check;
      check =
          "SELECT product_name FROM production_products"
          + " WHERE code = '" + obj[0]
          + "' OR product_id ='" + obj[0] + "'";
      ResultSet rscheck =
          stmt.executeQuery(check);
      if (rscheck.next()) {
        JOptionPane.showMessageDialog(
            new UserInterface(), "Product exists",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
        return false;
      } else {
        /*
OBJECT POSITIONS AND VALUES
    0    codeinput,
    1    barcodeinput,
    2    categoryname,
    3    brandname,
    4    descriptioninput,
    5    locationinput,
    6    costinput,
    7    salepriceinput,
    8    stocklevelinput,
    9    unitofmeasure,
    10    minimumstocklevelinput,
    11   commentsinput
         */
        String sql =
            "INSERT INTO production_products "
            + "(product_name,brand_id,category_id,"
            + "expiry_date,list_price,retail_price,code,ean_gtin,location,"
                + "unit_of_measure,comments) VALUES"
            + " ('" + obj[4] + "','" + obj[3]
            + "','" + obj[2] + "',"
            + "'','" + obj[7] + "','" + obj[6]
            + "',"
            + "'" + obj[0] + "','" + obj[1]
            + "','" + obj[5] + "','" + obj[9]
            + "','" + obj[11] + "')";
        int i = stmt.executeUpdate(sql);
        if (i > 0) {
          System.out.println(sql);
        } else {
          return false;
        }
      }
      // STEP 4: Clean-up environment

    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();
    }
    return true;
  }

  public static Boolean editBrand(
      String id, String brandname)
      throws SQLException,
             ClassNotFoundException {
    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt = conna.createStatement();
      // STEP 3: Execute a query

      String updatequery =
          "UPDATE production_brands set brand_name='"
          + brandname + "'"
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

  public static Boolean deleteBrand(int id)
      throws SQLException,
             ClassNotFoundException {
    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt;
      // STEP 3: Execute a query
      stmt = conna.createStatement();

      String updatequery =
          "DELETE production_brands where brand_id='"
          + id + "'";
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

  public static ArrayList listProducts()
      throws SQLException,
             ClassNotFoundException {
    ArrayList<Product> list =
        new ArrayList<Product>();
    ArrayList rowValues = new ArrayList();
    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt;
      // STEP 3: Execute a query
      stmt = conna.createStatement();
      String sqlquery =
          "SELECT * FROM production_products JOIN"
          + " production_brands ON production_products.brand_id ="
          + " production_brands.brand_id JOIN production_categories ON"
          + " production_products.category_id = "
          + "production_categories.category_id";
      ResultSet rs = stmt.executeQuery(sqlquery);
      while (rs.next()) {
        int productid = Integer.parseInt(
            rs.getString("product_id"));
        String product_name =
            rs.getString("product_name");
        String brand_name =
            rs.getString("brand_name");
        String category_name =
            rs.getString("category_name");
        String model = rs.getString("model_year");
        String expiry_date =
            rs.getString("expiry_date");
        String list_price =
            rs.getString("list_price");
        String retail_price =
            rs.getString("retail_price");
        list.add(new Product(productid,
            product_name, brand_name,
            category_name,
            Integer.parseInt(model), expiry_date,
            Double.parseDouble(list_price),
            Double.parseDouble(retail_price)));
      }
      // STEP 4: Clean-up environment
    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();
    }
    return list;
  }

  // These are the products that are in stock.
  public static ArrayList<Product>
  listStockProducts()
      throws SQLException,
             ClassNotFoundException {
    ArrayList<Product> list = new ArrayList<>();
    ArrayList rowValues = new ArrayList();
    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt;
      // STEP 3: Execute a query
      stmt = conna.createStatement();
      String sqlquery =
          "SELECT * FROM production_products JOIN"
          + " production_brands ON production_products.brand_id ="
          + " production_brands.brand_id JOIN production_categories ON"
          + " production_products.category_id = "
          + " production_categories.category_id "
          + "JOIN production_stocks ON production_products.product_id = "
          + "production_stocks.product_id WHERE production_stocks.quantity>0.0";
      ResultSet rs = stmt.executeQuery(sqlquery);
      while (rs.next()) {
        int productid = Integer.parseInt(
            rs.getString("product_id"));
        String product_name =
            rs.getString("product_name");
        String brand_name =
            rs.getString("brand_name");
        String category_name =
            rs.getString("category_name");
        String model = rs.getString("model_year");
        String expiry_date =
            rs.getString("expiry_date");
        String list_price =
            rs.getString("list_price");
        String retail_price =
            rs.getString("retail_price");
        String quantity =
            rs.getString("quantity");
        list.add(new Product(productid,
            product_name, brand_name,
            category_name,
            Integer.parseInt(model), expiry_date,
            Double.parseDouble(list_price),
            Double.parseDouble(retail_price)));
      }
      // STEP 4: Clean-up environment
    } catch (SQLException se) {
      // Handle errors for JDBC
    }
    return list;
  }

  // These are the products that are not located
  // in stocks.
  public static ArrayList listProductOnly()
      throws SQLException,
             ClassNotFoundException {
    ArrayList<Product> list = new ArrayList<>();
    ArrayList rowValues = new ArrayList();
    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt;
      // STEP 3: Execute a query
      stmt = conna.createStatement();
      String sqlquery =
          "SELECT * FROM production_products JOIN"
          + " production_brands ON production_products.brand_id ="
          + " production_brands.brand_id JOIN production_categories ON"
          + " production_products.category_id = "
          + " production_categories.category_id";
      ResultSet rs = stmt.executeQuery(sqlquery);
      while (rs.next()) {
        int productid = Integer.parseInt(
            rs.getString("product_id"));
        String product_name =
            rs.getString("product_name");
        String brand_name =
            rs.getString("brand_name");
        String category_name =
            rs.getString("category_name");
        String model = rs.getString("model_year");
        String expiry_date =
            rs.getString("expiry_date");
        String list_price =
            rs.getString("list_price");
        String retail_price =
            rs.getString("retail_price");
        int quantity = 0;

        list.add(new Product(productid,
            product_name, brand_name,
            category_name,
            Integer.parseInt(model), expiry_date,
            Double.parseDouble(list_price),
            Double.parseDouble(retail_price)));
      }
      // STEP 4: Clean-up environment
    } catch (SQLException se) {
      // Handle errors for JDBC
    }
    return list;
  }

  public static boolean deleteProduct(
      int productId)
      throws ClassNotFoundException {
    try {
      Connection conna =
          DBConnection.getConnectionInstance()
              .getConnection();
      Statement stmt = conna.createStatement();
      String sql =
          "DELETE from production_products where product_id = '"
          + productId + "'";
      int i = stmt.executeUpdate(sql);
      if (i != 1) {
        JOptionPane.showMessageDialog(null,
            "There was a problem item "
                + productId + " not found",
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

  /**
   *
   * @param product_id
   * @param product_name
   * @param brand_id
   * @param category_id
   * @param model_year
   * @param expiry_date
   * @param list_price
   * @param retail_price
   * @return
   * @throws ClassNotFoundException
   * @throws URISyntaxException
   * @throws ParseException
   * @throws java.sql.SQLException
   */
  public static boolean editProduct(
      String product_id, String product_name,
      int brand_id, int category_id,
      int model_year, String expiry_date,
      double list_price, double retail_price)
      throws ClassNotFoundException,
             URISyntaxException, ParseException,
             SQLException {
    Connection conna =
        DBConnection.getConnectionInstance()
            .getConnection();
    Statement stmt = conna.createStatement();
    // STEP 3: Execute a query
    String sql =
        "UPDATE production_products SET product_name ='"
        + product_name + "', "
        + "brand_id = '" + brand_id
        + "',category_id='" + category_id
        + "',model_year='" + model_year + "',"
        + "list_price='" + list_price
        + "',retail_price='" + retail_price
        + "' WHERE product_id='" + product_id
        + "'";
    int i = stmt.executeUpdate(sql);
    if (i > 0) {
      System.out.println(sql);
    } else {
      return false;
    }
    return true;
  }

  public static String getProductPrice(String id)
      throws SQLException,
             ClassNotFoundException {
    Connection conna =
        DBConnection.getConnectionInstance()
            .getConnection();
    Statement stmt = conna.createStatement();
    String price = "0";
    String check =
        "SELECT list_price FROM production_products"
        + " WHERE product_id = '" + id + "'";
    ResultSet rs = stmt.executeQuery(check);
    if (rs.next()) {
      price = rs.getString("list_price");
    }
    return price;
  }

  public static String getProductPricefromName(
      String name) throws SQLException,
                          ClassNotFoundException {
    Connection conna =
        DBConnection.getConnectionInstance()
            .getConnection();
    Statement stmt = conna.createStatement();
    String price = "0";
    String check =
        "SELECT list_price FROM production_products"
        + " WHERE product_name = '" + name + "'";
    ResultSet rs = stmt.executeQuery(check);
    if (rs.next()) {
      price = rs.getString("list_price");
    }
    return price;
  }

  public static String getProductId(String name)
      throws SQLException,
             ClassNotFoundException {
    Connection conna =
        DBConnection.getConnectionInstance()
            .getConnection();
    Statement stmt = conna.createStatement();
    String id = "0";
    String check =
        "SELECT product_id FROM production_products"
        + " WHERE product_name = '" + name + "'";
    ResultSet rs = stmt.executeQuery(check);
    if (rs.next()) {
      id = rs.getString("product_id");
    }
    return id;
  }
}
