/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nkanabo.Tienda;

import static Authentication.Auth.Login;
import static Authentication.Auth.authenticateProduct;
import static Classes.Utilities.OS.systempath;
import Database.DBConnection;
import Database.MasterData;
import Interface.ConsoleFrame;
import Interface.launcher;
import UserSettings.UserSettings;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.net.URL;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Nkanabo
 */
public class Main {

    public static final String app_version = "2.0";
    public static final String product_key = "9ZJaYoeC8N0mA3wUg0T55gPvw0HDNaBypu2QNtEoWFU=";
    public static final int KeyExpiryDays = 31;
    public static String resourceName = "resources/application.properties";
    public static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Properties props = new Properties();

    /*
    *this function is responsible for loading all the necessary
    information for system loading
    
    Starts with creating all the tables using H2 database engine
    2. Then inserts all master data
    3. Then ensures if the product is licensed by examining the key
    4. Then authenticates the user */
    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(String fileName) {
        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }

    private static void makeFolder() {
        File theDir = new File(systempath);
        if (!theDir.exists()) {
            theDir.mkdirs();
            System.out.println("Created images folder in ");
        }
    }

    public static void main(String[] args) throws IOException,
            SQLException, ClassNotFoundException, URISyntaxException, UnsupportedLookAndFeelException {

        System.setProperty("sun.java2d.dpiaware", "false");
        System.setProperty("sun.java2d.uiScale", "1");
        FlatGitHubIJTheme.setup();

// create UI here...
        //Load user preferences
        UserSettings pref = new UserSettings();
        pref.UserSettings();

         // Create and show the console frame
         ConsoleFrame consoleFrame = new ConsoleFrame();

        Main app = new Main();
        //https://mkyong.com/java/java-read-a-file-from-resources-folder/
        //#:~:text=In%20Java%2C%20we%20can%20use,
        //content%20InputStream%20is%20%3D%20getClass().
        //String fileName = "database.properties";
        String fileName = resourceName;
        InputStream is = app.getFileFromResourceAsStream(resourceName);
        // Load Configuration file
        // for static access, uses the class name directly
        try ( InputStream resourceStream = is) {
            //props.load(resourceStream);
            Configurations configurations = Configurations.getInstance();
            Configurations.setConfPath(resourceStream);
            //Stating Database Connection
            DBConnection dBConnection = DBConnection.getConnectionInstance();
            dBConnection.setUrl(Optional.of(
                    configurations.getDBLocation()));
            dBConnection.getConnection();
            /* This is to be used when we want to repeat the prior migration */
            // dBConnection.repair();
            dBConnection.migrate();
        }

        try {
            MasterData.InsertMasterData();

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        //returns true if authenticated.
        UserSettings usS = new UserSettings();
        usS.testKeys();
        if (authenticateProduct() == true && usS.getKeysValidity() == true) {
            Login();
        } else {
            LaunchLicenseEntry();
        }
        makeFolder();
    }

    public static void LaunchLicenseEntry() {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new launcher().setVisible(true);
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
