package Classes.Utilities;

import java.io.File;

public class OS {

    // Define class variables
    public static String systemPath;
    public static String systemDB;
    public static String baseUrl;
    public static String duplicatesPath;
    public static String hash;
    public static String homeDir;

    // Constructor to initialize class variables
    public OS() {
        getsysEnv();
    }

    // Method to retrieve the operating system name
    public static String getsysEnv() {
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        return operatingSystem;
    }

    // Method to set the system path based on the operating system
    public static String getSystemPath() {
        homeDir = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("windows")) {
            systemPath = homeDir + "\\Tienda\\";
            systemDB = "jdbc:h2:file:\\C:\\Program Files\\Tienda\\DB\\Tienda;USER=root;PASSWORD=tazamaroad";
            baseUrl = "C:\\Program Files\\Tienda\\Resources\\";
            hash = "\\";
        } else if (os.contains("mac")) {
            systemPath = homeDir + "/Tienda/";
            systemDB = "jdbc:h2:file:///Applications/Tienda.app/Contents/DB/Tienda;USER=root;PASSWORD=tazamaroad";
            baseUrl = "/Applications/Tienda.app/Contents/Resources/";
            hash = "/";
        }
        return systemPath;
    }
}
