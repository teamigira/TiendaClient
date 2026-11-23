package Interface.Products;

import Database.DBConnection;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Suppliers manager (Phase 4 — the "Suppliers" tab). Full CRUD against
 * {@code production_suppliers} with prepared statements.
 *
 * @author Nkanabo
 */
public class SuppliersDialog extends JDialog {

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Supplier", "Contact", "Phone", "Email", "Address"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public SuppliersDialog(Frame owner) {
        super(owner, "Suppliers", true);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);

        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> add());
        edit.addActionListener(e -> edit());
        del.addActionListener(e -> delete());
        refresh.addActionListener(e -> reload());
        bar.add(add);
        bar.add(edit);
        bar.add(del);
        bar.add(refresh);

        add(bar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
        setPreferredSize(new Dimension(760, 420));
        pack();
        setLocationRelativeTo(owner);
    }

    private void reload() {
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT supplier_id, supplier_name, contact_name, phone, email, address "
                     + "FROM Tienda.production_suppliers ORDER BY supplier_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("supplier_id"), rs.getString("supplier_name"),
                    rs.getString("contact_name"), rs.getString("phone"),
                    rs.getString("email"), rs.getString("address")});
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void add() {
        String[] v = form(null);
        if (v == null) {
            return;
        }
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO Tienda.production_suppliers "
                     + "(supplier_name, contact_name, phone, email, address) VALUES (?,?,?,?,?)")) {
            ps.setString(1, v[0]);
            ps.setString(2, v[1]);
            ps.setString(3, v[2]);
            ps.setString(4, v[3]);
            ps.setString(5, v[4]);
            ps.executeUpdate();
            reload();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void edit() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a supplier to edit.");
            return;
        }
        r = table.convertRowIndexToModel(r);
        String id = String.valueOf(model.getValueAt(r, 0));
        String[] cur = {
            str(model.getValueAt(r, 1)), str(model.getValueAt(r, 2)),
            str(model.getValueAt(r, 3)), str(model.getValueAt(r, 4)), str(model.getValueAt(r, 5))};
        String[] v = form(cur);
        if (v == null) {
            return;
        }
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE Tienda.production_suppliers SET supplier_name=?, contact_name=?, "
                     + "phone=?, email=?, address=? WHERE supplier_id=?")) {
            ps.setString(1, v[0]);
            ps.setString(2, v[1]);
            ps.setString(3, v[2]);
            ps.setString(4, v[3]);
            ps.setString(5, v[4]);
            ps.setString(6, id);
            ps.executeUpdate();
            reload();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void delete() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a supplier to delete.");
            return;
        }
        r = table.convertRowIndexToModel(r);
        String id = String.valueOf(model.getValueAt(r, 0));
        String name = str(model.getValueAt(r, 1));
        if (JOptionPane.showConfirmDialog(this, "Delete \"" + name + "\"?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM Tienda.production_suppliers WHERE supplier_id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
            reload();
        } catch (Exception ex) {
            error(ex);
        }
    }

    /** Edit form; returns {name,contact,phone,email,address} or null if cancelled. */
    private String[] form(String[] cur) {
        JTextField name = new JTextField(cur == null ? "" : cur[0]);
        JTextField contact = new JTextField(cur == null ? "" : cur[1]);
        JTextField phone = new JTextField(cur == null ? "" : cur[2]);
        JTextField email = new JTextField(cur == null ? "" : cur[3]);
        JTextField address = new JTextField(cur == null ? "" : cur[4]);
        JPanel p = new JPanel(new GridLayout(0, 1, 4, 4));
        p.add(new JLabel("Supplier name *"));
        p.add(name);
        p.add(new JLabel("Contact person"));
        p.add(contact);
        p.add(new JLabel("Phone"));
        p.add(phone);
        p.add(new JLabel("Email"));
        p.add(email);
        p.add(new JLabel("Address"));
        p.add(address);
        while (true) {
            int ok = JOptionPane.showConfirmDialog(this, p,
                    cur == null ? "Add supplier" : "Edit supplier",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) {
                return null;
            }
            if (name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Supplier name is required.");
                continue;
            }
            return new String[]{name.getText().trim(), contact.getText().trim(),
                phone.getText().trim(), email.getText().trim(), address.getText().trim()};
        }
    }

    private static String str(Object o) {
        return o == null ? "" : o.toString();
    }

    private void error(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Operation failed: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
