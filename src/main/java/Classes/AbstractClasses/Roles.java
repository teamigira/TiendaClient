/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.AbstractClasses;

/**
 *
 * @author Nkanabo
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nkanabo
 */
public class Roles {

    public int role_id;
    public String role_name;
    public String description;

    public Roles(
            int role_id,
            String role_name,
            String description
    ) {
        this.role_id = role_id;
        this.role_name = role_name;
        this.description = description;
    }

    public String getRoleName() {
        return role_name;
    }
}
