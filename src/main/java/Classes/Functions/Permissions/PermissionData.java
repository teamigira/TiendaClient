/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Functions.Permissions;

import java.util.List;

public class PermissionData {
    private String heading;
    private List<String> permissions;

    // Default constructor
    public PermissionData() {
    }

    // Constructor
    public PermissionData(String heading, List<String> permissions) {
        this.heading = heading;
        this.permissions = permissions;
    }

    // Getter and setter methods
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
