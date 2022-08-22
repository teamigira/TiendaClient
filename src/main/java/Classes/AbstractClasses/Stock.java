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
        public String prdcode;
        public String product_id;
        public String quantity ;
        
        public Stock(String prdcode, String product_id, String quantity)
        {
            this.prdcode = prdcode;
            this.product_id = product_id;
            this.quantity = quantity;
        }
}
