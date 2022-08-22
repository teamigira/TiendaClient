/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nkanabo.Tienda;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Nkanabo
 */
public class testFile {
     public static void main(String[] args) throws IOException {
        
        
         
        testFile app = new testFile();
        // read all files from a resources folder
        try {
            
            // files from src/main/resources/json
            List<File> result = app.getAllFilesFromResource(".");
            for (File file : result) {
                System.out.println("file : " + file);
                printFile(file);
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    private List<File> getAllFilesFromResource(String folder)
        throws URISyntaxException, IOException {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(folder);

        // dun walk the root path, we will walk all the classes
        List<File> collect = Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .map(x -> x.toFile())
                .collect(Collectors.toList());

        return collect;
    }

    // print a file
    private static void printFile(File file) {

        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
