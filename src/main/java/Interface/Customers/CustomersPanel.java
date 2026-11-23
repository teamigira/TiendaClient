package Interface.Customers;

import Classes.Functions.Customers;
import Classes.Functions.Customers.Customer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Self-contained Customers page: list, search, add, edit and delete customers.
 *
 * <p>Built in plain Swing (not a NetBeans {@code .form}) so it can be dropped
 * into the main window's card panel without touching generated code. The main
 * window just adds an instance of this panel to its {@code TablePanel} and calls
 * {@link #reload()}.
 *
 * @author Nkanabo
 */
public class CustomersPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField searchField = new JTextField(18);
    private final JLabel countLabel = new JLabel();

    public CustomersPanel() {
        super(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ---- header / toolbar ----
        JLabel title = new JLabel("Customers");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton searchBtn = new JButton("Search");

        addBtn.addActionListener(this::onAdd);
        editBtn.addActionListener(this::onEdit);
        deleteBtn.addActionListener(this::onDelete);
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            reload();
        });
        searchBtn.addActionListener(e -> reload());
        searchField.addActionListener(e -> reload()); // Enter in the field

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        actions.add(new JLabel("Search:"));
        actions.add(searchField);
        actions.add(searchBtn);
        actions.add(addBtn);
        actions.add(editBtn);
        actions.add(deleteBtn);
        actions.add(refreshBtn);

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(countLabel, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // ---- table ----
        model = new DefaultTableModel(Customers.columns(), 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);
        // Double-click a row to edit.
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    onEdit(null);
                }
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
    }

    /** Reload the table from the database, honouring the search box. */
    public final void reload() {
        try {
            List<Customer> rows = Customers.list(searchField.getText());
            model.setRowCount(0);
            for (Customer c : rows) {
                model.addRow(new Object[]{c.id, c.firstName, c.lastName, c.phone,
                        c.email, c.street, c.city, c.state, c.zip});
            }
            countLabel.setText(rows.size() + " customer(s)");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load customers:\n"
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer selectedId() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            return null;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        return (Integer) model.getValueAt(modelRow, 0);
    }

    private void onAdd(ActionEvent e) {
        CustomerDialog dlg = new CustomerDialog(SwingUtilities.getWindowAncestor(this), null);
        dlg.setVisible(true);
        if (dlg.saved) {
            reload();
        }
    }

    private void onEdit(ActionEvent e) {
        Integer id = selectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a customer to edit.");
            return;
        }
        try {
            Customer c = Customers.find(id);
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Customer no longer exists.");
                reload();
                return;
            }
            CustomerDialog dlg = new CustomerDialog(SwingUtilities.getWindowAncestor(this), c);
            dlg.setVisible(true);
            if (dlg.saved) {
                reload();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete(ActionEvent e) {
        Integer id = selectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a customer to delete.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
        String name = model.getValueAt(modelRow, 1) + " " + model.getValueAt(modelRow, 2);
        int ok = JOptionPane.showConfirmDialog(this,
                "Delete customer \"" + name.trim() + "\" (id " + id + ")?",
                "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                Customers.delete(id);
                reload();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not delete:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ------------------------------------------------------------------
    // Add / Edit dialog
    // ------------------------------------------------------------------
    private static final class CustomerDialog extends JDialog {

        private boolean saved = false;
        private final Customer editing; // null => creating

        private final JTextField first = new JTextField(20);
        private final JTextField last = new JTextField(20);
        private final JTextField phone = new JTextField(20);
        private final JTextField email = new JTextField(20);
        private final JTextField street = new JTextField(20);
        private final JTextField city = new JTextField(20);
        private final JTextField state = new JTextField(20);
        private final JTextField zip = new JTextField(20);

        CustomerDialog(Window owner, Customer existing) {
            super(owner, existing == null ? "Add customer" : "Edit customer",
                    ModalityType.APPLICATION_MODAL);
            this.editing = existing;

            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
            int row = 0;
            addField(form, "First name", first, row++);
            addField(form, "Last name", last, row++);
            addField(form, "Phone", phone, row++);
            addField(form, "Email", email, row++);
            addField(form, "Street", street, row++);
            addField(form, "City", city, row++);
            addField(form, "State", state, row++);
            addField(form, "Zip", zip, row++);

            if (existing != null) {
                first.setText(existing.firstName);
                last.setText(existing.lastName);
                phone.setText(existing.phone);
                email.setText(existing.email);
                street.setText(existing.street);
                city.setText(existing.city);
                state.setText(existing.state);
                zip.setText(existing.zip);
            }

            JButton save = new JButton("Save");
            JButton cancel = new JButton("Cancel");
            save.addActionListener(e -> onSave());
            cancel.addActionListener(e -> dispose());
            getRootPane().setDefaultButton(save);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttons.add(cancel);
            buttons.add(save);

            setLayout(new BorderLayout());
            add(form, BorderLayout.CENTER);
            add(buttons, BorderLayout.SOUTH);
            pack();
            setMinimumSize(new Dimension(360, getHeight()));
            setLocationRelativeTo(owner);
        }

        private void addField(JPanel form, String label, JTextField field, int row) {
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(4, 4, 4, 4);
            gc.gridx = 0;
            gc.gridy = row;
            gc.anchor = GridBagConstraints.LINE_END;
            form.add(new JLabel(label + ":"), gc);
            gc.gridx = 1;
            gc.anchor = GridBagConstraints.LINE_START;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 1;
            form.add(field, gc);
        }

        private void onSave() {
            if (first.getText().trim().isEmpty() && last.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter at least a first or last name.");
                return;
            }
            if (phone.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Phone is required.");
                return;
            }
            Customer c = editing == null ? new Customer() : editing;
            c.firstName = first.getText().trim();
            c.lastName = last.getText().trim();
            c.phone = phone.getText().trim();
            c.email = email.getText().trim();
            c.street = street.getText().trim();
            c.city = city.getText().trim();
            c.state = state.getText().trim();
            c.zip = zip.getText().trim();
            try {
                if (editing == null) {
                    Customers.insert(c);
                } else {
                    Customers.update(c);
                }
                saved = true;
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not save:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
