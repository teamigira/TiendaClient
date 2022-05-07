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
public class Staff {
        public String staff_name;
        public String sur_name;
        public String staff_email;
        public int role;
        
        public Staff(
                String staff_name,
                String sur_name,
                String staff_email,
                int role)
        {
            this.staff_name = staff_name;
            this.sur_name = sur_name;
            this.staff_email = staff_email;
            this.role = role;
        }
}
