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
import javax.swing.JCheckBox;
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
 * Tax-rate manager (Phase 4 — the "Tax" tab). CRUD against
 * {@code production_tax}; exactly one rate can be flagged as the default.
 *
 * @author Nkanabo
 */
public class TaxDialog extends JDialog {

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Tax name", "Rate (%)", "Default"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public TaxDialog(Frame owner) {
        super(owner, "Tax rates", true);
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
        JButton setDefault = new JButton("Set as default");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> add());
        edit.addActionListener(e -> edit());
        del.addActionListener(e -> delete());
        setDefault.addActionListener(e -> makeDefault());
        refresh.addActionListener(e -> reload());
        bar.add(add);
        bar.add(edit);
        bar.add(del);
        bar.add(setDefault);
        bar.add(refresh);

        add(bar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
        setPreferredSize(new Dimension(560, 380));
        pack();
        setLocationRelativeTo(owner);
    }

    private void reload() {
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT tax_id, tax_name, rate, is_default FROM Tienda.production_tax "
                     + "ORDER BY is_default DESC, tax_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("tax_id"), rs.getString("tax_name"),
                    rs.getString("rate"), rs.getBoolean("is_default") ? "★ default" : ""});
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void add() {
        Object[] v = form(null, null, false);
        if (v == null) {
            return;
        }
        try (Connection c = DBConnection.getConnectionInstance().getConnection()) {
            if ((Boolean) v[2]) {
                clearDefault(c);
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Tienda.production_tax (tax_name, rate, is_default) VALUES (?,?,?)")) {
                ps.setString(1, (String) v[0]);
                ps.setDouble(2, (Double) v[1]);
                ps.setBoolean(3, (Boolean) v[2]);
                ps.executeUpdate();
            }
            reload();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void edit() {
        int r = selected();
        if (r < 0) {
            return;
        }
        String id = String.valueOf(model.getValueAt(r, 0));
        Object[] v = form(str(model.getValueAt(r, 1)), str(model.getValueAt(r, 2)),
                str(model.getValueAt(r, 3)).contains("default"));
        if (v == null) {
            return;
        }
        try (Connection c = DBConnection.getConnectionInstance().getConnection()) {
            if ((Boolean) v[2]) {
                clearDefault(c);
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE Tienda.production_tax SET tax_name=?, rate=?, is_default=? WHERE tax_id=?")) {
                ps.setString(1, (String) v[0]);
                ps.setDouble(2, (Double) v[1]);
                ps.setBoolean(3, (Boolean) v[2]);
                ps.setString(4, id);
                ps.executeUpdate();
            }
            reload();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void delete() {
        int r = selected();
        if (r < 0) {
            return;
        }
        String id = String.valueOf(model.getValueAt(r, 0));
        String name = str(model.getValueAt(r, 1));
        if (JOptionPane.showConfirmDialog(this, "Delete \"" + name + "\"?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM Tienda.production_tax WHERE tax_id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
            reload();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void makeDefault() {
        int r = selected();
        if (r < 0) {
            return;
        }
        String id = String.valueOf(model.getValueAt(r, 0));
        try (Connection c = DBConnection.getConnectionInstance().getConnection()) {
            clearDefault(c);
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE Tienda.production_tax SET is_default=TRUE WHERE tax_id=?")) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
            reload();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void clearDefault(Connection c) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE Tienda.production_tax SET is_default=FALSE")) {
            ps.executeUpdate();
        }
    }

    private int selected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a tax rate first.");
            return -1;
        }
        return table.convertRowIndexToModel(r);
    }

    /** Returns {name(String), rate(Double), isDefault(Boolean)} or null. */
    private Object[] form(String curName, String curRate, boolean curDefault) {
        JTextField name = new JTextField(curName == null ? "" : curName);
        JTextField rate = new JTextField(curRate == null ? "" : curRate);
        JCheckBox def = new JCheckBox("Use as default tax", curDefault);
        JPanel p = new JPanel(new GridLayout(0, 1, 4, 4));
        p.add(new JLabel("Tax name *"));
        p.add(name);
        p.add(new JLabel("Rate (%) *"));
        p.add(rate);
        p.add(def);
        while (true) {
            int ok = JOptionPane.showConfirmDialog(this, p,
                    curName == null ? "Add tax" : "Edit tax",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) {
                return null;
            }
            if (name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tax name is required.");
                continue;
            }
            double rateVal;
            try {
                rateVal = Double.parseDouble(rate.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Rate must be a number, e.g. 18 or 18.5");
                continue;
            }
            return new Object[]{name.getText().trim(), rateVal, def.isSelected()};
        }
    }

    private static String str(Object o) {
        return o == null ? "" : o.toString();
    }

    private void error(Exception ex) {
        Classes.Utilities.Log.error(ex);
        JOptionPane.showMessageDialog(this, "Operation failed: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
