package Classes.Functions;

import Classes.AbstractClasses.Email;
import static Classes.Functions.Notifications.confirmCheck;
import Database.DBConnection;
import Interface.UIv2;

import static com.nkanabo.Tienda.Utilities.DateMilli;
import static com.nkanabo.Tienda.Utilities.milliConverter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nkanabo
 */

public class Notifications {
    
    

    public static LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));
    public static String today = "" + d1;

    public static boolean deleteRow(int emailId) throws ClassNotFoundException, ClassNotFoundException {
               try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            String updatequery
                    = "DELETE notifications where notice_id='" + emailId + "'";
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

    
    public void Notifications() throws URISyntaxException, ClassNotFoundException {
//         UserInterface fc = new UserInterface();
         
    }
    //Public final int
    public static boolean notify(String product_name, String quantity, String productId) throws ClassNotFoundException, ParseException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String pid;

            String message = "Product " + product_name + " is nearly depleted. Remaining " + quantity;
            String title = "Out of Stock";
            String sql
            = "INSERT INTO notifications (date,title,message,viewed,code)"
            + "VALUES ('" + milliConverter(today) + "','" + title + "','" + message + "',0,'" + productId + "')";
            if(!confirmCheck(productId)){
                int i = stmt.executeUpdate(sql);
                return i > 0;
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC
        }
        return true;
    }

    public static boolean confirmCheck(String code) throws ClassNotFoundException, ParseException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query
            stmt = conna.createStatement();
            String sql
            = "SELECT code from Tienda.system_notifications WHERE code = '" + code + "'";
            ResultSet rs = stmt.executeQuery(sql);
             if (rs.next()) {
            return true;
             }
            // STEP 4: Clean-up environment
        } catch (SQLException se) {
            // Handle errors for JDBC
        }
        return false;
    }

    public static void crawlEmails() throws ClassNotFoundException, URISyntaxException, ParseException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query
            stmt = conna.createStatement();
            String sql
                    = "SELECT code,message from Tienda.system_notifications WHERE viewed = '0' ORDER BY notice_id DESC";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
             String message = rs.getString("message");
             UIv2.setEmailNotification(message);
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC
        }
    }
    
        public static ArrayList listNotifications() throws ClassNotFoundException{
        ArrayList<Email> list = new ArrayList<Email>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String sqlquery = "SELECT * FROM Tienda.system_notifications ORDER BY notice_id DESC";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                //rowValues.add(rs.getInt("brand_id"), rs.getString("brand_name"));
                list.add(new Email(rs.getString("notice_id"),
                         DateMilli(rs.getString("date")),rs.getString("title"),rs.getString("message")));
            }
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;
    }
        
         public static Boolean deleteEmails() throws SQLException, ClassNotFoundException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            String updatequery
                    = "DELETE notifications";
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
