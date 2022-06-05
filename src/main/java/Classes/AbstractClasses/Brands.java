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
public class Brands {
        public int id;
        public int brand_id;
        public String brand_name ;
        
        public Brands(int brand_id, String brand_name)
        {
            this.brand_id = brand_id;
            this.brand_name = brand_name;
        }
}
