package Classes.Functions;

import static Classes.Functions.Notifications.confirmCheck;
import Database.DBConnection;
import static com.nkanabo.Tienda.Utilities.milliConverter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;

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

    //Public final int
    public static boolean notify(String product_name, String quantity, String productId) throws ClassNotFoundException, ParseException {
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            String pid;

            String message = "Product " + product_name + " is nearly depleted. Remaining " + quantity;
            String title = "Out of Stock";
            String sql
            = "INSERT INTO notifications (date,title,message,viewed,code)"
            + "VALUES ('" + milliConverter(today) + "','" + title + "','" + message + "',0,'" + productId + "')";
            if(confirmCheck(productId)){
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
            Statement stmt = conna.createStatement();
            // STEP 3: Execute a query
            stmt = conna.createStatement();
            String sql
            = "SELECT code from notifications WHERE code = '" + code + "'";
            int i = stmt.executeUpdate(sql);
            return i > 0;
            // STEP 4: Clean-up environment 
        } catch (SQLException se) {
            // Handle errors for JDBC
        }
        return true;
    }
}
