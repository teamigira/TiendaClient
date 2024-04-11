/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.AbstractClasses;

/**
 *
 * @author Nkanabo
 */
import java.util.ArrayList;
import java.util.List;

public class RolePermissionMapping {

    private int roleId;
    private int permissionId;
    private String roleName;
    private String permissionName;
    private String category;

    // Constructor, getters, and setters
    public RolePermissionMapping(int roleId, int permissionId, String roleName,
            String permissionName, String category) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.roleName = roleName;
        this.permissionName = permissionName;
        this.category = category;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
