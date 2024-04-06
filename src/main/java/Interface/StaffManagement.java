/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interface;

/**
 *
 * @author Nkanabo
 */
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class StaffManagement extends JFrame {
    private DefaultTableModel StaffsModel;
    private JTable staffTable;
    private ArrayList<Staff> staffsList;
    private String[] staffsHeader = {"First Name", "Last Name", "Email", "Phone", "Store", "Status", "Manager ID", "Role", "Actions"};

    public StaffManagement() {
        setTitle("Staff Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        StaffsModel = new DefaultTableModel(staffsHeader, 0);
        staffTable = new JTable(StaffsModel);

        // Set custom renderer for the "Actions" column
        staffTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());

        JScrollPane scrollPane = new JScrollPane(staffTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load staffs and populate the table
        loadStaffs();

        setVisible(true);
    }

    private void loadStaffs() {
        System.out.println("abdul");
        // Clear previous data
        StaffsModel.setRowCount(0);

        // Load staffs data
        staffsList = LoadStaffs();
        if (!staffsList.isEmpty()) {
            for (Staff staff : staffsList) {
                System.out.println("kadiria");
                Object[] obj = {
                    staff.staff_name,
                    staff.sur_name,
                    staff.staff_email,
                    staff.phone_no,
                    staff.store,
                    Constants.getStatusLabel(Integer.parseInt(staff.Status)),
                    staff.Status,
                    staff.manager_id,
                    staff.role
                };
                StaffsModel.addRow(obj);
            }
        }
    }

    // Dummy method for loading staffs data
    private ArrayList<Staff> LoadStaffs() {
        // Dummy implementation to load staffs data
        return new ArrayList<>(); // Return an empty list for demonstration
    }

    // Custom renderer for buttons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton("Actions");
            button.addActionListener(e -> {
                // Perform actions when the button is clicked
                // For example, show a dialog for editing or deactivating staff
                JOptionPane.showMessageDialog(StaffManagement.this, "Button clicked for row: " + row);
            });
            return button;
        }
    }

    // Dummy class to represent Staff
    class Staff {
        String staff_name;
        String sur_name;
        String staff_email;
        String phone_no;
        String store;
        String Status;
        String manager_id;
        String role;
        // Constructor, getters, setters...
    }

    // Dummy class to represent Constants
    static class Constants {
        static String getStatusLabel(int status) {
            // Dummy implementation to get status label based on status value
            return "Active"; // For demonstration
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StaffManagement::new);
    }
}
