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
public class Category {
        public int id;
        public String category_id;
        public String category_name ;
        
        public Category(String category_id, String category_name)
        {
            this.category_id = category_id;
            this.category_name = category_name;
        }
}
