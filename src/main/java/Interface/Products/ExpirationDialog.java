package Interface.Products;

import Database.DBConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Expiration report (Phase 4 — the "Expiration" tab). Lists every product that
 * carries an expiry date, sorted soonest-first, with a colour-coded status so
 * the shopkeeper can see at a glance what has expired or is about to.
 *
 * <ul>
 *   <li>red  — already expired</li>
 *   <li>amber — expires within 30 days</li>
 *   <li>normal — more than 30 days away</li>
 * </ul>
 *
 * @author Nkanabo
 */
public class ExpirationDialog extends JDialog {

    private static final int SOON_DAYS = 30;

    private static final DateTimeFormatter[] FORMATS = {
        DateTimeFormatter.ISO_LOCAL_DATE,                 // 2026-11-21
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("dd MMM yyyy"),
        DateTimeFormatter.ofPattern("yyyy.MM.dd")
    };

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Code", "Product", "Expiry date", "Days left", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public ExpirationDialog(Frame owner) {
        super(owner, "Expiration report", true);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel heading = new JLabel("Products by expiry date (soonest first)");
        heading.setBorder(BorderFactory.createEmptyBorder(0, 2, 8, 0));
        add(heading, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setDefaultRenderer(Object.class, new StatusRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        load();
        setPreferredSize(new Dimension(720, 440));
        pack();
        setLocationRelativeTo(owner);
    }

    private void load() {
        model.setRowCount(0);
        LocalDate today = LocalDate.now();
        try (Connection c = DBConnection.getConnectionInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT code, product_name, expiry_date FROM Tienda.production_products "
                     + "WHERE expiry_date IS NOT NULL AND TRIM(expiry_date) <> ''");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("product_name");
                String raw = rs.getString("expiry_date");
                LocalDate date = parse(raw);
                if (date == null) {
                    model.addRow(new Object[]{code, name, raw, "?", "Unrecognised date"});
                    continue;
                }
                long days = ChronoUnit.DAYS.between(today, date);
                String status;
                if (days < 0) {
                    status = "EXPIRED";
                } else if (days <= SOON_DAYS) {
                    status = "Expiring soon";
                } else {
                    status = "OK";
                }
                model.addRow(new Object[]{code, name,
                    date.format(DateTimeFormatter.ISO_LOCAL_DATE), days, status});
            }
        } catch (Exception ex) {
            Classes.Utilities.Log.error(ex);
        }
        sortSoonestFirst();
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"", "No products have an expiry date set.", "", "", ""});
        }
    }

    /** Simple stable sort by the Days-left column (nulls / '?' go last). */
    private void sortSoonestFirst() {
        int n = model.getRowCount();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (dayVal(j) > dayVal(j + 1)) {
                    model.moveRow(j, j, j + 1);
                }
            }
        }
    }

    private long dayVal(int row) {
        Object v = model.getValueAt(row, 3);
        if (v instanceof Long) {
            return (Long) v;
        }
        try {
            return Long.parseLong(String.valueOf(v));
        } catch (NumberFormatException e) {
            return Long.MAX_VALUE;
        }
    }

    private static LocalDate parse(String raw) {
        String s = raw.trim();
        for (DateTimeFormatter f : FORMATS) {
            try {
                return LocalDate.parse(s, f);
            } catch (Exception ignore) {
                // try next format
            }
        }
        return null;
    }

    /** Colours rows by the Status column. */
    private static final class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object value,
                boolean sel, boolean focus, int row, int col) {
            Component comp = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
            String status = String.valueOf(t.getValueAt(row, 4));
            if (!sel) {
                if ("EXPIRED".equals(status)) {
                    comp.setBackground(new Color(0xFF, 0xCD, 0xD2));
                    comp.setForeground(new Color(0xB7, 0x1C, 0x1C));
                } else if ("Expiring soon".equals(status)) {
                    comp.setBackground(new Color(0xFF, 0xF3, 0xCD));
                    comp.setForeground(new Color(0x8A, 0x6D, 0x00));
                } else {
                    comp.setBackground(t.getBackground());
                    comp.setForeground(t.getForeground());
                }
            }
            if (comp instanceof JComponent) {
                ((JComponent) comp).setOpaque(true);
            }
            return comp;
        }
    }
}
