/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Brands;
import static Database.DBConnect.getConnection;
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
public class Crudes {

    public static Boolean addBrand(String brandname) throws SQLException, ClassNotFoundException {
        try {
            
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            stmt = conna.createStatement();

            String sql
                    = "INSERT INTO production_brands (brand_name)" + "VALUES ('" + brandname + "')";
            int i = stmt.executeUpdate(sql);
            if (i > 0) {
                System.out.println(sql);
            } else {
                return false;
            }


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
            stmt = conna.createStatement();

            String updatequery
                    = "UPDATE production_brands set brand_name='" + brandname + "'"
                    + "where brand_id='" + id + "'";
            int rsu = stmt.executeUpdate(updatequery);
            if (rsu != 1) {
                return false;
            }

            // STEP 4: Clean-up environment 
            stmt.close();
            conna.close();

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
            stmt = conna.createStatement();

            String updatequery
                    = "DELETE production_brands where brand_id='" + id + "'";
            int rsu = stmt.executeUpdate(updatequery);
            if (rsu != 1) {
                return false;
            }

            // STEP 4: Clean-up environment 
            stmt.close();
            conna.close();

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
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM production_brands";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
//           rowValues.add(rs.getInt("brand_id"), rs.getString("brand_name"));
                list.add(new Brands(Integer.parseInt(rs.getString("brand_id")), rs.getString("brand_name")));
            }
            // STEP 4: Clean-up environment 
            stmt.close();
            conna.close();
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;
    }

    public static void listBrandstest() throws SQLException, ClassNotFoundException {
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM production_brands";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                System.out.println(rs.getString(1));
//             System.out.println(rs.getString("brand_id"));
//             System.out.println(rs.getString("brand_name"));
                rowValues.add(rs.getString("brand_id"));
                rowValues.add(rs.getString("brand_name"));
            }
            // STEP 4: Clean-up environment 
            stmt.close();
            conna.close();
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        //return rowValues; 
    }
}
