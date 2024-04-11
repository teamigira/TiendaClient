/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Utilities;

import javax.swing.JOptionPane;

/**
 *
 * @author Nkanabo
 */
public class NotificationManager {
    public static void showPopupNotification(String message, NotificationType type) {
        String title = type.toString();
        int dialogType = getDialogType(type);

        JOptionPane.showMessageDialog(null, message, title, dialogType);
    }

    public static void showConsoleNotification(String message, NotificationType type) {
        String prefix = "[" + type.toString() + "]";
        System.out.println(prefix + " " + message);
    }

    private static int getDialogType(NotificationType type) {
        switch (type) {
            case INFO:
                return JOptionPane.INFORMATION_MESSAGE;
            case WARNING:
                return JOptionPane.WARNING_MESSAGE;
            case ERROR:
                return JOptionPane.ERROR_MESSAGE;
            case SUCCESS: // Add case for SUCCESS notification type
                return JOptionPane.INFORMATION_MESSAGE; // Use the same dialog type as INFO
            default:
                return JOptionPane.PLAIN_MESSAGE;
        }
    }

    public enum NotificationType {
        INFO,
        WARNING,
        ERROR,
        SUCCESS // Add SUCCESS notification type
    }

    public static void main(String[] args) {
        String message = "This is a notification message.";
        showPopupNotification(message, NotificationType.INFO);
        showPopupNotification(message, NotificationType.ERROR);
        showPopupNotification(message, NotificationType.SUCCESS); // Example of showing a SUCCESS notification
        showConsoleNotification(message, NotificationType.ERROR);
        showConsoleNotification("CRILIN HAYUPO", NotificationType.WARNING);
    }
}