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
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Nkanabo
 */
public class SearchEngine {

    public String[] ProductsearchSuggestions(String search) throws MalformedURLException, IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        search = search.replace(" ", "+");
        URL oracle
                = new URL("http://suggestqueries.google.com/complete/search?q=" + search + "&client=firefox&hl=fr");
        URLConnection yc = oracle.openConnection();
        String val;
        try ( BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
            val = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Object obj = parser.parse(inputLine);
                JSONArray array = new JSONArray();
                array.put(obj);
                for (Object o : array) {
                    val = o.toString();
                }
            }
        }
        System.out.println("this is the data type" + val.getClass().getName());
        System.out.println("this is the content" + val);
        String v[] = val.replace("[", "").replace("\"", "").split(",");
        if (v.length == 1 && v[0].equals("")) {
            return new String[0];
        } else {
            System.out.println("V emitted" + v.toString());
            return v;
        }
    }

    public String[] productsSearch(String Search) throws ClassNotFoundException, ParseException, SQLException {
        ArrayList<Product> products = Products.listProductOnly();
        System.out.println("calling search");
        System.out.println(products.size());
        List<Product> line = products.stream().filter(p -> (p.product_name.contains(Search))).toList();
        System.out.println(line);
        String[] ch = new String[line.size() + 1];
        System.out.println("searching");
        for (int i = 0; i < line.size(); i++) {
            Product po = line.get(i);
            ch[i] = po.product_name;
            System.out.println(ch[i]);
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
