/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nkanabo.Tienda;

import Database.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nkanabo
 */
public class Version {
    
    private String VersionNum;
    private String VersionDate;
    private String VersionDescr;
    
      public Version() throws Exception{
    
        try {
            Version();
        } catch (SQLException ex) {
        System.out.println(ex + "VersionControl");
        }
        
    }
    private String Version() throws Exception {
        
    String[] versionDetails = null;        
//    DBConnection db =new DBConnection();
    Connection conn = DBConnection.getConnectionInstance().getConnection();
    Statement sm = null;
    sm = conn.createStatement();
    String SQL = "SELECT * FROM VERSION";    
    try{
    ResultSet rss = sm.executeQuery(SQL);
      while(rss.next()){
        String vn = rss.getString("version_number");
        String rd = rss.getString("release_date");
        String dtl = rss.getString("description");
        
        this.VersionNum = vn;
        this.VersionDate = rd;
        this.VersionDescr = dtl;
       } 
    }
    catch(SQLException e){
        System.out.println(e);
    }
            
        return null;
    }
    
    
     public String getVersionNum() {
        return this.VersionNum;
        
    }
     
     
     public String VersionDate() {
        return this.VersionDate;
        
    }
     
     
     public String VersionDescr() {
        return this.VersionDescr;
        
    }
    
}
