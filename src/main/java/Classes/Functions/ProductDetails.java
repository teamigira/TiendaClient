/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Functions;

/**
 *
 * @author Nkanabo
 */
public class ProductDetails {

    public int productid;
    public String code;
    public String product_name;//description
    public String barcode;
    public String brand_id;
    public String category_id;
    public String location;
    public double stock;//--->other tables
    public String unit_of_measure;
    public String expiry_date;//From other table
    public double list_price;  //sale price
    public double retail_price; //cost
    public double minimimun_stock_level;
    public String comments;

    ProductDetails(
    int productid,
    String code,
    String product_name,//description
    String barcode,
    String brand_id,
    String category_id,
    String location,
    double stock,//--->other tables
    String unit_of_measure,
    String expiry_date,//From other table
    double list_price,  //sale price
    double retail_price, //cost
    double minimimun_stock_level,
    String comments)
    {
    this.productid = productid;
    this.code = code;
    this.product_name = product_name;//description
    this.barcode = barcode;
    this.brand_id = brand_id;
    this.category_id = category_id;
    this.location = location;
    this.stock = stock;//--->other tables
    this.unit_of_measure = unit_of_measure;
    this.expiry_date = expiry_date;//From other table
    this.list_price = list_price;  //sale price
    this.retail_price = retail_price; //cost
    this.minimimun_stock_level = minimimun_stock_level;
    this.comments = comments;
    }
}
