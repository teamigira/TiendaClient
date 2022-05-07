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
    
    
   public void main() throws FileNotFoundException{
    Preferences myprefs = Preferences.userNodeForPackage(UserSettings.class);
    myprefs.put("role","administrator");
    System.out.println(myprefs.get("role", "root"));
       try {
           myprefs.exportNode(new FileOutputStream("Preferences.xml"));
      
       } catch (IOException ex) {
           Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
       } catch (BackingStoreException ex) {
           Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
}


