/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Utilities;

import Classes.AbstractClasses.Brand;
import Classes.AbstractClasses.Product;
import Classes.Functions.Crudes;
import Classes.Functions.Products;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Nkanabo
 */
public class SearchEngine {

    public String[] productsSearch(String Search) throws ClassNotFoundException, ParseException, SQLException {
        ArrayList<Product> products = Products.listProductOnly();
        final String needle = Search == null ? "" : Search.trim().toLowerCase();
        // Case-insensitive match so the product is found whatever case was typed,
        // and no trailing empty slot (the old size+1 array left a blank entry
        // that, if clicked, cleared the field and caused a false "out of stock").
        List<Product> line = products.stream()
                .filter(p -> p.product_name != null
                        && p.product_name.toLowerCase().contains(needle))
                .toList();
        String[] ch = new String[line.size()];
        for (int i = 0; i < line.size(); i++) {
            ch[i] = line.get(i).product_name;
        }
        return ch;
    }
    
        public String[] brandSearch(String Search) throws ClassNotFoundException, ParseException, SQLException {
        ArrayList<Brand> brands = Crudes.listBrands();
        List<Brand> line = brands.stream().filter(p -> (p.brand_name.contains(Search))).toList();
        String[] ch = new String[line.size() + 1];
        
        for (int i = 0; i < line.size(); i++) {
            Brand po = line.get(i);
            ch[i] = po.brand_name;
        }
        return ch;

    }
}
