/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.AbstractClasses;

/**
 *
 * @author Nkanabo
 */
public class Product {
    public int productid;
    public String product_name;
    public String brand_id;
    public String category_id;
    public int model_year;
    public String expiry_date;
    public double list_price;
    public double retail_price;
    
    public Product(
    int productid,
    String product_name,
    String brand_id,
    String category_id,
    int model_year,
    String expiry_date,
    double list_price,
    double retail_price){
    this.productid = productid;
    this.product_name = product_name;
    this.brand_id = brand_id;
    this.category_id = category_id;
    this.model_year = model_year;
    this.expiry_date = expiry_date;
    this.list_price = list_price;
    this.retail_price = retail_price;
    }
    
}
