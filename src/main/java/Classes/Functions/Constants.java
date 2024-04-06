/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Functions;

/**
 *
 * @author Nkanabo
 */
public class Constants {
    public static final int ACTIVE = 1;
    public static final int SUSPENDED = 0;
    public static final int DELETED = -1;
    
    // Method to convert status value to label
    public static String getStatusLabel(int status) {
        switch (status) {
            case ACTIVE:
                return "Active";
            case SUSPENDED:
                return "Suspended";
            case DELETED:
                return "Deleted";
            default:
                return "Unknown";
        }
    }
}
