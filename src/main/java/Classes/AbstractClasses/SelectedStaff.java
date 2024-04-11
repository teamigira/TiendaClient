package Classes.AbstractClasses;

/*/
* @author Nkanabo
*/

public class SelectedStaff {
   public String staff_name;
   public String sur_name;
   public String staff_email;
   public String phone_no;
   public String store;
   public String Status;
   public String manager_id;
   public String role;
   public String userid;
   public String password; // Added password field

   public SelectedStaff(
           String staff_name,
           String sur_name,
           String staff_email,
           String phone_no,
           String store,
           String Status,
           String manager_id,
           String role,
           String userid) {
       this(staff_name, sur_name, staff_email, phone_no, store, Status, manager_id, role, userid, "");
   }

   public SelectedStaff(
           String staff_name,
           String sur_name,
           String staff_email,
           String phone_no,
           String store,
           String Status,
           String manager_id,
           String role,
           String userid,
           String password) {
       this.staff_name = staff_name;
       this.sur_name = sur_name;
       this.staff_email = staff_email;
       this.phone_no = phone_no;
       this.store = store;
       this.Status = Status;
       this.manager_id = manager_id;
       this.role = role;
       this.userid = userid;
       this.password = password;
   }
}
