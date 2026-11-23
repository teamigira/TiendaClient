package Interface.Products;

import Database.DBConnection;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Brands &amp; Categories manager — the "see brands" view that the menu never
 * opened. Two side-by-side tables, each with Add / Edit / Delete / Refresh,
 * backed by prepared-statement CRUD (the old Categories helpers were copy-paste
 * bugs that edited the brands table).
 *
 * @author Nkanabo
 */
public class BrandsPanel extends JPanel {

    private final DefaultTableModel brandModel = nonEditable(new String[]{"ID", "Brand name"});
    private final DefaultTableModel catModel = nonEditable(new String[]{"ID", "Category name"});
    private final JTable brandTable = new JTable(brandModel);
    private final JTable catTable = new JTable(catModel);

    public BrandsPanel() {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel grid = new JPanel(new GridLayout(1, 2, 12, 0));
        grid.add(section("Brands", brandTable, true));
        grid.add(section("Categories", catTable, false));
        add(grid, BorderLayout.CENTER);

        reload();
    }

    private JPanel section(String title, JTable table, boolean isBrand) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);

        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> add(isBrand));
        edit.addActionListener(e -> edit(isBrand));
        del.addActionListener(e -> delete(isBrand));
        refresh.addActionListener(e -> reload());
        bar.add(add);
        bar.add(edit);
        bar.add(del);
        bar.add(refresh);

        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(bar, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    /** Public so the menu can refresh it on re-open. */
    public final void reload() {
        loadBrands();
        loadCategories();
    }

    // ---- loads -------------------------------------------------------

    private void loadBrands() {
        brandModel.setRowCount(0);
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT brand_id, brand_name FROM Tienda.production_brands ORDER BY brand_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                brandModel.addRow(new Object[]{rs.getString("brand_id"), rs.getString("brand_name")});
            }
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
        }
    }

    private void loadCategories() {
        catModel.setRowCount(0);
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT category_id, category_name FROM Tienda.production_categories ORDER BY category_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                catModel.addRow(new Object[]{rs.getString("category_id"), rs.getString("category_name")});
            }
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
        }
    }

    // ---- CRUD --------------------------------------------------------

    private void add(boolean isBrand) {
        String what = isBrand ? "brand" : "category";
        String name = JOptionPane.showInputDialog(this, "New " + what + " name:", "Add " + what,
                JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        String table = isBrand ? "production_brands" : "production_categories";
        String col = isBrand ? "brand_name" : "category_name";
        // Avoid duplicates.
        if (exists(table, col, name.trim())) {
            JOptionPane.showMessageDialog(this, "That " + what + " already exists.");
            return;
        }
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO Tienda." + table + " (" + col + ") VALUES (?)")) {
            ps.setString(1, name.trim());
            ps.executeUpdate();
        } catch (Exception ex) {
            error(ex);
            return;
        }
        reload();
    }

    private void edit(boolean isBrand) {
        JTable table = isBrand ? brandTable : catTable;
        DefaultTableModel model = isBrand ? brandModel : catModel;
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to edit.");
            return;
        }
        r = table.convertRowIndexToModel(r);
        String id = String.valueOf(model.getValueAt(r, 0));
        String current = String.valueOf(model.getValueAt(r, 1));
        String name = (String) JOptionPane.showInputDialog(this, "Name:", "Edit",
                JOptionPane.PLAIN_MESSAGE, null, null, current);
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        String tbl = isBrand ? "production_brands" : "production_categories";
        String nameCol = isBrand ? "brand_name" : "category_name";
        String idCol = isBrand ? "brand_id" : "category_id";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE Tienda." + tbl + " SET " + nameCol + " = ? WHERE " + idCol + " = ?")) {
            ps.setString(1, name.trim());
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            error(ex);
            return;
        }
        reload();
    }

    private void delete(boolean isBrand) {
        JTable table = isBrand ? brandTable : catTable;
        DefaultTableModel model = isBrand ? brandModel : catModel;
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        r = table.convertRowIndexToModel(r);
        String id = String.valueOf(model.getValueAt(r, 0));
        String name = String.valueOf(model.getValueAt(r, 1));
        if (JOptionPane.showConfirmDialog(this, "Delete \"" + name + "\"?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        String tbl = isBrand ? "production_brands" : "production_categories";
        String idCol = isBrand ? "brand_id" : "category_id";
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM Tienda." + tbl + " WHERE " + idCol + " = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            error(ex);
            return;
        }
        reload();
    }

    private boolean exists(String table, String col, String value) {
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT 1 FROM Tienda." + table + " WHERE UPPER(" + col + ") = UPPER(?)")) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception ex) {
            return false;
        }
    }

    private void error(Exception ex) {
        Classes.Utilities.Log.error(ex);
        JOptionPane.showMessageDialog(this, "Operation failed: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static DefaultTableModel nonEditable(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }
}
