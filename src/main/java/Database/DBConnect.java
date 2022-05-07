/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nkanabo
 */

public class DBConnect {
    
   // JDBC driver name and database URL 
   static final String JDBC_DRIVER = "org.h2.Driver";   
   static final String DB_URL = "jdbc:h2:~\\Tienda";  
   
   //  Database credentials 
   static final String USER = "root"; 
   static final String PASS = "tazamaroad"; 
   
   public static Connection getConnection() throws SQLException{
       try {
           Connection conn = null;
           Statement stmt = null;
           
           // STEP 1: Register JDBC driver
           Class.forName(JDBC_DRIVER);
           
           //STEP 2: Open a connection
           //System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           
           //STEP 3: Execute a query
           stmt = conn.createStatement();
           return conn;
       } catch (ClassNotFoundException ex) {
           Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       }
   }
  
   } 
    