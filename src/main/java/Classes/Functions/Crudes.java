/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Brand;
import static Classes.Utilities.OS.getSystemPath;
import static Classes.Utilities.OS.systemPath;
import static Database.DBConnect.getConnection;
import Database.DBConnection;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            Statement stmt;
            // STEP 3: Execute a query
            stmt = conna.createStatement();

            // Brand names are unique: skip if one already exists (prevents the
            // duplicate "General" rows that used to be seeded on every startup).
            try (java.sql.PreparedStatement chk = conna.prepareStatement(
                    "SELECT 1 FROM Tienda.production_brands WHERE brand_name = ?")) {
                chk.setString(1, brandname);
                try (java.sql.ResultSet crs = chk.executeQuery()) {
                    if (crs.next()) {
                        return true; // already present
                    }
                }
            }

            String sql = "INSERT INTO Tienda.production_brands (brand_name)VALUES (?)";
            int i = Database.Sql.prepare(stmt.getConnection(), sql, brandname).executeUpdate();
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

    public static Boolean addFromCSVFile() throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {
        String csvFilePath = getSystemPath() + "Brands.csv";
        //xlsx
        try {

            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            /*
             * Batch reading for uploading
             */
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
            int count = 0;

            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String brand = data[0];
                //String studentName = data[1];
                //String timestamp = data[2];
                //String rating = data[3];
                //String comment = data.length == 5 ? data[4] : "";
                
            duplicatedRowAvoidance(brand);
            String sql = "INSERT INTO Tienda.production_brands (brand_name)VALUES (?)";
            int i = Database.Sql.prepare(stmt.getConnection(), sql, brand).executeUpdate();
            // execute the rema
            /*End of batch reading*/
            if (i > 0) {
                System.out.println(sql);
            } else {
                return false;
            }
            }

            lineReader.close();

           

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

            String updatequery = "UPDATE production_brands set brand_name=? WHERE brand_id=?";
            int rsu = Database.Sql.prepare(stmt.getConnection(), updatequery, brandname, id).executeUpdate();
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
            Statement stmt;
            stmt = conna.createStatement();

            String updatequery = "DELETE production_brands where brand_id=?";
            int rsu = Database.Sql.prepare(stmt.getConnection(), updatequery, id).executeUpdate();
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
        ArrayList<Brand> list;
        list = new ArrayList<>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance()
                    .getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM Tienda.production_brands";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                list.add(new Brand(Integer.parseInt(rs.getString("brand_id")), rs.getString("brand_name")));
            }
            // STEP 4: Clean-up environment 
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
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM Tienda.production_brands";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
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

    public static boolean deleteRow(int brandId) throws ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            String updatequery = "DELETE production_brands where brand_id=?";
            int rsu = Database.Sql.prepare(stmt.getConnection(), updatequery, brandId).executeUpdate();
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
    
        public static boolean duplicatedRowAvoidance(String brandname) throws ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            String searchquery = "SELECT * FROM Tienda.production_brands where brand_name=?";
            ResultSet rs = Database.Sql.prepare(stmt.getConnection(), searchquery, brandname).executeQuery();
            // STEP 4: Clean-up environment
            return rs.next();

        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return false;
    }
}
