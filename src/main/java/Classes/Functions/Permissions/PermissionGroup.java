/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Functions.Permissions;

/**
 *
 * @author Nkanabo
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// PermissionGroup class to represent a single permission group
public class PermissionGroup {

    @JsonProperty("heading")
    private String heading;

    @JsonProperty("name")
    private String name;

    @JsonProperty("permissions")
    private List<String> permissions;

    // Getter methods for heading, name, and permissions
    public String getHeading() {
        return heading;
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    // Setter methods for heading, name, and permissions
    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
