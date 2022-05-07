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

public class Stock {
        public String product_id;
        public int quantity ;
        
        public Stock(String product_id, int quantity)
        {
            this.product_id = product_id;
            this.quantity = quantity;
        }
}
