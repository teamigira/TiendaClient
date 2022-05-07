/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserSettings;

import java.util.prefs.Preferences;

/**
 *
 * @author Nkanabo
 */
public class UserSettings {
    
    
   public void main(String[] args){
    Preferences myprefs = Preferences.userNodeForPackage(UserSettings.class);
    myprefs.put("role","administrator");
    
//    try {
//        myprefs.exportNode(new FileOutputStream("settings.xml"));
//    } catch (Exception ex) {
//        Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
//    }
   }
}


