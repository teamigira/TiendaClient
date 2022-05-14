/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nkanabo.Tienda;

import static Authentication.Auth.Login;
import static Authentication.Auth.authenticateProduct;
import Database.DBConnection;
import static Database.MasterData.Master;
import Interface.launcher;
import static Database.MasterTables.createTables;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import UserSettings.UserSettings;

/**
 *
 * @author Nkanabo
 */
public class Main {
     public static final String app_version = "1.0";   
     public static final String product_key = "9ZJaYoeC8N0mA3wUg0T55gPvw0HDNaBypu2QNtEoWFU=";   
     public static final int KeyExpiryDays = 1;
     public static String resourceName = "application.properties";
     public static ClassLoader loader = Thread.currentThread().getContextClassLoader();

  
     Properties props = new Properties();
     
    /*
    *this function is responsible for loading all the necessary
    information for system loading
    
    Starts with creating all the tables using H2 database engine
    2. Then inserts all master data
    3. Then ensures if the product is licensed by examining the key
    4. Then authenticates the user */
    
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, URISyntaxException {
        
        //Load user preferences
            UserSettings pref = new UserSettings();
            pref.UserSettings();
            
         // Load Configuration file
         try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            //props.load(resourceStream);
            Configurations configurations = new Configurations().getInstance();
            configurations.setConfPath(resourceStream);
            
            //Stating Database Connection
            DBConnection dBConnection = DBConnection.getConnectionInstance();
            dBConnection.setUrl(Optional.of(configurations.getDBLocation()));
            dBConnection.getConnection();
            dBConnection.migrate();
        } 
              
        try {  
            
            createTables();
            Master();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      //returns true if authenticated.
      UserSettings usS = new UserSettings();
      usS.testKeys();
      if(authenticateProduct() == true && usS.getKeysValidity() == true){
           Login();
          }
      else {
           LaunchLicenseEntry();
      }
    }
    
    public static void LaunchLicenseEntry() {
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> {
                try {
                    new launcher().setVisible(true);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
    }
}
