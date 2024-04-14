package Interface;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import  UserSettings.AppColors;
import static UserSettings.AppColors.DISABLED_COLOR;
import static UserSettings.AppColors.DISABLED_FORE_COLOR;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    
    private static final ImageIcon SUSPENDED_ICON = new ImageIcon(StatusCellRenderer.class.getResource("/resources/images/icons8-ban-32.png"));
    private static final ImageIcon ACTIVE_ICON = new ImageIcon(StatusCellRenderer.class.getResource("/resources/images/check-mark.png"));
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                column);

        // Assuming active status column index is 5
        if (column == 5) {
            String status = (String) value;
            if ("Suspended".equals(status)) {
                // Set background color to red for suspended status
                cellComponent.setBackground(DISABLED_COLOR);
                setForeground(DISABLED_FORE_COLOR);
                // Set icon for suspended status
                setIcon(SUSPENDED_ICON);
            } else {
                // Reset background color and icon for other statuses
                cellComponent.setBackground(table.getBackground());
                setIcon(ACTIVE_ICON);
            }
        }

        return cellComponent;
    }
}
