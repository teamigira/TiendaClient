/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserSettings;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Nkanabo
 */
public class UserSettings {
    
    Preferences myprefs = Preferences.userNodeForPackage(UserSettings.class);
    
     public void soundSettings(boolean value){
         if(value == true){
         myprefs.put("sounds", "true");
         }
         else  
         myprefs.put("sounds", "false");
    }
     
     public String getSystemCurrency(Double price){
         myprefs.put("Currency", "Tsh");
         return "Tsh " + price;
     }
     
    public void testKeys(){
          //myprefs.remove("sounds");
          System.out.println("The current status" +myprefs.get("product_key", "root"));
    }
    
    public boolean getKeysValidity(){
        return myprefs.get("product_key", "root") != "0";          
    }
       
    public void SetKey(){
        myprefs.put("product_key", "0");
    }
    
    
   public void UserSettings() throws FileNotFoundException{
       try {
           myprefs.exportNode(new FileOutputStream("Tienda-Preferences.xml"));
       } catch (IOException | BackingStoreException ex) {
           Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
}


