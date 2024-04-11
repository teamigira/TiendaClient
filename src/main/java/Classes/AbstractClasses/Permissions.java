/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.AbstractClasses;

/**
 *
 * @author Nkanabo
 */
public class Permissions {
    private int permissionId;
    private String permissionName;
    private String category;

    public Permissions(int permissionId, String permissionName, String category) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.category = category;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
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
