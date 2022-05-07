/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.AbstractClasses;

/**
 *
 * @author Nkanabo
 * 
 */
public class Order {
    public String orderid;
    public String product;
    public int quantity;
    public double listprice;
    public double discount;
    
    public Order(String orderid, String prd, int qty, double lprc, double dsc){
        this.orderid = orderid;
        this.product = prd;
        this.quantity = qty;
        this.listprice = lprc;
        this.discount = dsc;
        
    }
    
}
