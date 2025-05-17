package Classes.Functions.Printers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Statement;

import Authentication.Sessions;
import Classes.AbstractClasses.ReceiptDetails;
import Classes.AbstractClasses.ReceiptSettings;
import Classes.Utilities.NotificationManager.NotificationType;
import static Classes.Utilities.NotificationManager.showConsoleNotification;
import static Classes.Utilities.NotificationManager.showPopupNotification;

import static Classes.Utilities.RandomNumbers.generateNumber;
import Database.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Receipts {

    public static void saveReceiptDetails(boolean print, String printer, String company, String footer, String address,String template) throws SQLException {
        // Get other required data
        int salesId = getSalesId(); // Implement this method to get sales ID
        String createdBy = Sessions.LoggedUserId; // Assuming Sessions class has this method
        Date salesDate = new Date(); // Use appropriate method to get sales date
        Date orderDate = new Date(); // Use appropriate method to get order date
        Date createdDate = new Date(); // Use appropriate method to get created date
        int barcode = generateNumber(); // Assuming generateNumber method exists in Utilities class
        System.out.println("_-------------------"+barcode);
        String sold_to = "";

        // Database connection
        try (
                 Connection connection = DBConnection.getConnectionInstance().getConnection();  Statement statement = connection.createStatement();) {
            // Prepare the SQL statement
            String sqlReceiptsDetails = "INSERT INTO Tienda.receipts_details (sales_id, company_name, footer_message, address, created_by, sold_to, sales_date, order_date, created_date, barcode) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String sqlReceiptSettings = "INSERT INTO Tienda.receipt_settings (created_by, printer_name, automatically, template) "
                    + "VALUES (?, ?, ?, ?)";

            try (
                     PreparedStatement preparedStatementReceiptsDetails = connection.prepareStatement(sqlReceiptsDetails, Statement.RETURN_GENERATED_KEYS);  PreparedStatement preparedStatementReceiptSettings = connection.prepareStatement(sqlReceiptSettings);) {
                // Set parameters for receipts_details table
                preparedStatementReceiptsDetails.setInt(1, salesId);
                preparedStatementReceiptsDetails.setString(2, company);
                preparedStatementReceiptsDetails.setString(3, footer);
                preparedStatementReceiptsDetails.setString(4, address);
                preparedStatementReceiptsDetails.setString(5, createdBy);
                preparedStatementReceiptsDetails.setString(6, sold_to);
                preparedStatementReceiptsDetails.setDate(7, new java.sql.Date(salesDate.getTime())); // Convert Java Date to SQL Date
                preparedStatementReceiptsDetails.setDate(8, new java.sql.Date(orderDate.getTime())); // Convert Java Date to SQL Date
                preparedStatementReceiptsDetails.setTimestamp(9, new java.sql.Timestamp(createdDate.getTime())); // Convert Java Date to SQL Timestamp
                preparedStatementReceiptsDetails.setInt(10, barcode);
              
                // Execute the preparedStatement for receipts_details table
                preparedStatementReceiptsDetails.executeUpdate();

              
                // Set parameters for receipt_settings table
                preparedStatementReceiptSettings.setString(1, createdBy);
                preparedStatementReceiptSettings.setString(2, printer);
                preparedStatementReceiptSettings.setBoolean(3, false); // Assuming automatically should be false by default
                preparedStatementReceiptSettings.setString(4, template);

                // Execute the preparedStatement for receipt_settings table
                preparedStatementReceiptSettings.executeUpdate();

                String message = "Receipt details and settings saved successfully.";
                showPopupNotification(message, NotificationType.SUCCESS); // Example of showing a SUCCESS notification
                showConsoleNotification(message, NotificationType.INFO);
            } catch (SQLException e) {
                String message = "Error saving receipt details and settings: " + e.getMessage();
                showPopupNotification(message, NotificationType.ERROR); // Example of showing an ERROR notification
                showConsoleNotification(message, NotificationType.ERROR);
            }
        }
    }
        
        // Dummy methods, implement according to your project requirements
    private static int getSalesId() {
        // Implement this method to get sales ID
        return 0;
    }


    public static ReceiptDetails getLastReceiptDetails() throws SQLException, ClassNotFoundException {
        ReceiptDetails receiptDetails = null;
        try {
            DBConnection dbc = DBConnection.getConnectionInstance();
            Connection con = dbc.getConnection();
            Statement stmt = con.createStatement();
            String sqlQuery = "SELECT * FROM Tienda.receipts_details ORDER BY receipt_id DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                int receiptId = rs.getInt("receipt_id");
                int salesId = rs.getInt("sales_id");
                String companyName = rs.getString("company_name");
                String footerMessage = rs.getString("footer_message");
                String address = rs.getString("address");
                String createdBy = rs.getString("created_by");
                String soldTo = rs.getString("sold_to");
                String created = rs.getString("created_date");
                String barcode = rs.getString("barcode");
                // Assuming sales_date, order_date, and created_date are of appropriate types in ReceiptDetails class
                // You may need to convert them based on their actual types
                receiptDetails = new ReceiptDetails(receiptId, salesId, companyName, footerMessage, address, createdBy, soldTo, null, null, created, barcode);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return receiptDetails;
    }

    public static ReceiptSettings getLastReceiptSettings() throws SQLException, ClassNotFoundException {
        ReceiptSettings receiptSettings = null;
        try {
            DBConnection dbc = DBConnection.getConnectionInstance();
            Connection con = dbc.getConnection();
            Statement stmt = con.createStatement();
            String sqlQuery = "SELECT * FROM Tienda.receipt_settings ORDER BY settings_id DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                int settingsId = rs.getInt("settings_id");
                int createdBy = rs.getInt("created_by");
                String printerName = rs.getString("printer_name");
                boolean automatically = rs.getBoolean("automatically");
                String template = rs.getString("template");
                receiptSettings = new ReceiptSettings(createdBy, printerName, automatically, template);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return receiptSettings;
    }
}
