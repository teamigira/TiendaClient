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


public class Transfer {
    
    public String amount;
    public String date;
    public String collector;
    public Double total;
    
    public Transfer(
    String amount,
    String date,
    String collector,
    Double total
){
        this.amount = amount;
        this.date = date;
        this.collector = collector;
        this.total = total;
        
    }
    
}
