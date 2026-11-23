package Classes.Utilities;

import java.io.File;

public class OS {

    // Define class variables
    public static String systemPath;
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
    /**
     * The app data folder with a trailing separator; generated files
     * (barcodes etc.) are stored under it. See {@link com.nkanabo.Tienda.AppPaths}.
     */
    public static String getSystemPath() {
        homeDir = System.getProperty("user.home");
        hash = java.io.File.separator;
        systemPath = com.nkanabo.Tienda.AppPaths.dataDir().getAbsolutePath() + hash;
        return systemPath;
    }
}
