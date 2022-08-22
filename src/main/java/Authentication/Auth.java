/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Authentication;

import Database.DBConnection;
import Interface.Login;
import static com.nkanabo.Tienda.Utilities.milliConverter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nkanabo
 */
public class Auth {
           
 
    public static boolean authenticateProduct() throws SQLException, ClassNotFoundException {
        try {
            //if oriduct activation is null then
            //request for product activation
            //else
            //request for log in
            LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));
            long today;

            today = milliConverter(String.valueOf(d1));

            DBConnection dbc = DBConnection.getConnectionInstance();
            Connection con = dbc.getConnection();
            Statement stmt;
            stmt = con.createStatement();
            String sql = "SELECT * FROM app_key"
                    + " where activation_status = 0 OR "
                    + "expire_date <= '" + today + "'";
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 4: Extract data from result set
            if (rs.next()) {
                //Retrieve by column name
                String id = rs.getString("product_id");
                int sts = rs.getInt("activation_status");
                String expire_date = rs.getString("expire_date");
                return false;
                // response
            }

            // STEP 5: Clean-up environment
            rs.close();
            // finally block used to close resources
        } catch (ParseException ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public static void Login() throws URISyntaxException {
        Login lg = new Login();
        lg.login();
    }
}
