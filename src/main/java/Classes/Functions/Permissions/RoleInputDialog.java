package Classes.Functions.Permissions;

import static Classes.Functions.Permissions.PermissionFileManager.renameRole;
import javax.swing.*;
public class RoleInputDialog {

    public static void showInputDialog(String roleName) {
        // Create input fields
        JTextField roleNameField = new JTextField(roleName);
        JTextField descriptionField = new JTextField();

        // Create a panel and add input fields
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Role Name:"));
        panel.add(roleNameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        // Create an icon for save
        Icon saveIcon = new ImageIcon(RoleInputDialog.class.getResource("/resources/images/v2icons8-edit-32.png"));

        // Show input dialog
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Role Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, saveIcon);
        if (result == JOptionPane.OK_OPTION) {
            // Get values from input fields
            String roleNameInput = roleNameField.getText();
            String description = descriptionField.getText();
            renameRole(roleName,roleNameInput);
            // Handle the save operation here
        }
    }
}
