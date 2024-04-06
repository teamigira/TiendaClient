/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Utilities;


/**
 *
 * @author Nkanabo
 */
public class OS {
    public static String systempath = path();
    public static String systemDB;
    public static String base_Url;
    public static String duplicatesPath;
    public static String hash;
    
               
    OS(){      
        getsysEnv();
    }
    
    /**
     *
     * @return 
     */
    
      public static String getsysEnv(){
      String operatings= System.getProperty("os.name");                       
      String systemDB = "";            
      return operatings;
    }
      
      public static String path(){
        if(getsysEnv().contains("Windows")){ 
        systempath = "C:\\Tienda\\resources\\usergenerated\\";  
        systemDB = "jdbc:h2:file:\\C:\\Program Files\\Tienda\\DB\\Tienda"
                 + ";USER=root;PASSWORD=tazamaroad";
        System.out.println("Windows");
        base_Url = "C:\\Program Files\\Tienda\\Resources\\";
        duplicatesPath = "\\Duplicates";
        hash = "\\";
        }
        else 
        if(getsysEnv().contains("Mac")){
        systempath= "/Applications/Tienda.app/Contents/Files/images/";            
        systemDB = 
        "jdbc:h2:file://Applications/Tienda.app/Contents/DB/Tienda"
                 + ";USER=root;PASSWORD=tazamaroad"; 
        base_Url ="/Applications/Tienda.app/Contents/Resources/";        
        duplicatesPath = "/Duplicates/";
        hash = "/";
        }
        return systempath;
    }
        
    public static void main(String[] args){
        getsysEnv();
    }
}
