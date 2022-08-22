/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Category;
import Classes.AbstractClasses.Brands;
import Database.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Nkanabo
 */
public class Categories {

    public static Boolean addCategory(String categoryname) throws SQLException, ClassNotFoundException {
        try {
            Connection conn = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conn.createStatement();
            String sql
                    = "INSERT INTO production_categories (category_name)" + "VALUES ('" + categoryname + "')";
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
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();

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
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();

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

    public static ArrayList listBrands() throws SQLException, ClassNotFoundException {

        ArrayList<Brands> list = new ArrayList<Brands>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM production_brands";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
//           rowValues.add(rs.getInt("brand_id"), rs.getString("brand_name"));
                list.add(new Brands(Integer.parseInt(rs.getString("brand_id")), rs.getString("brand_name")));
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;
    }

    public static ArrayList listCategories() throws SQLException, ClassNotFoundException {
        ArrayList<Category> list = new ArrayList<Category>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM production_categories";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
//           rowValues.add(rs.getInt("brand_id"), rs.getString("brand_name"));
                list.add(new Category(rs.getString("category_id"), rs.getString("category_name")));
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;
    }
    
        public static Boolean deleteCategory(int id)
                throws SQLException, ClassNotFoundException {
        try {
            Connection conna =
            DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            String updatequery
                    = "DELETE production_categories where category_id='" + id + "'";
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
}
