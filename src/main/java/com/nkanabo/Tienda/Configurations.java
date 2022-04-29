/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nkanabo.Tienda;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
 
 /**
 *
 * @author Nkanabo
 */

public class Configurations {
    static Properties properties;

    public static Configurations getInstance(URL resource) {
        //Properties.
        return ConfigurationsHolder.INSTANCE;
    }

    public Configurations() {
        properties = new Properties();
    }
    
    public static Configurations getInstance() {
        return ConfigurationsHolder.INSTANCE;
    }

    public static void setConfPath(InputStream inStream) throws IOException {
        properties.load(inStream);
    }
    
    private static class ConfigurationsHolder {
        private static final Configurations INSTANCE = new Configurations();
    }
    
    public String getSystemPath()
    {
        String property = System.getProperty("os.name");
        if (property.equalsIgnoreCase("Windows")){
            return properties.getProperty("window.system.path");
        }
            return properties.getProperty("unix.system.path");
    }
    
    /**
     * Getting Database Location | path
     * @return 
     */

    public String getDBLocation()
    {
        return System.getProperty("user.home")+"/Tienda/";
    }
    
   }
