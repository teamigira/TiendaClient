/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Functions;

import Classes.AbstractClasses.Transfer;
import Database.DBConnection;
import static com.nkanabo.Tienda.Utilities.DateMilli;
import static com.nkanabo.Tienda.Utilities.milliConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nkanabo
 */
public class Accounts {

    public static boolean addTransfer(String amount) throws ClassNotFoundException {

        try {
            try (
                 Connection conna = DBConnection.getConnectionInstance().getConnection();
                 Statement stmt = conna.createStatement();
            ) {
                
                LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));
                
                String today = String.valueOf(d1);
                String mysql
                        = "INSERT INTO accounts (amount,date)" + "VALUES ('" + amount + "','" + milliConverter(today) + "')";
                int i = stmt.executeUpdate(mysql);
                if (i > 0) {
                    System.out.println(mysql);
                } else {
                    return false;
                }
                // STEP 4: Clean-up environment
            }
            // STEP 3: Execute a query ;
            // STEP 3: Execute a query ;
                    } catch (SQLException se) {
            // Handle errors for JDBC

        } catch (ParseException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static ArrayList getTransfers() throws ClassNotFoundException {
        ArrayList<Transfer> list = new ArrayList<Transfer>();
        ArrayList rowValues = new ArrayList();
        try {
            Connection conna = DBConnection.getConnectionInstance().getConnection();
            Statement stmt;
            // STEP 3: Execute a query 
            stmt = conna.createStatement();
            // STEP 3: Execute a query 
            String sqlquery = "SELECT * FROM accounts";
            ResultSet rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                String amount = rs.getString("amount");
                String date = rs.getString("date");
                String collector = rs.getString("collected_by");
                Double total = 0.0;
                String ddate = DateMilli(date);
                list.add(
                        new Transfer(amount, ddate, collector, total));
            }
        } catch (SQLException se) {
            // Handle errors for JDBC 
            se.printStackTrace();
        }
        return list;
    }
}
