/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.AbstractClasses;

/**
 * public String product_id;
        public int quantity ;
        
        public Stock(String product_id, int quantity)
        {
            this.product_id = product_id;
            this.quantity = quantity;
        }
 * @author Nkanabo
 */
public class DailyReport {
    
    public String productname;
    public String retailprice;
    public String listprice;
    public double profit;
    public double totalinvestment;
    public double totalreturns;
    public int quantity;
    public double superprofit;
    public String date;
    
    public DailyReport(
    String productname,
    String retailprice,
    String listprice,
    double profit,
    double totalinvestment,
    double totalreturns,
    int quantity,
    double superprofit,
    String date)
    {
        this.productname = productname;
        this.retailprice = retailprice;
        this.listprice = listprice;
        this.profit = profit;
        this.totalinvestment = totalinvestment;
        this.totalreturns = totalreturns;
        this.quantity = quantity;
        this.superprofit = superprofit;
        this.date = date;
    }
}
