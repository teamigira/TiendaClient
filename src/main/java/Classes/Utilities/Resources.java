/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Utilities;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Nkanabo
 */
public class Resources {
         private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    
    public File getFileFromResource(String fileName) throws URISyntaxException{

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
            
        } else if ("file".equals(resource.getProtocol())) {
            return new File(resource.toURI());
        } else {
            // Inside a jar (the installed app) there is no file to point at,
            // so copy the resource to a cache folder once and return that.
            File cached = new File(new File(com.nkanabo.Tienda.AppPaths.dataDir(),
                    "cache" + File.separator + com.nkanabo.Tienda.AppInfo.VERSION), fileName);
            if (!cached.isFile()) {
                cached.getParentFile().mkdirs();
                try (InputStream in = resource.openStream()) {
                    java.nio.file.Files.copy(in, cached.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (java.io.IOException e) {
                    throw new IllegalArgumentException("cannot read " + fileName, e);
                }
            }
            return cached;
        }

    }
}
