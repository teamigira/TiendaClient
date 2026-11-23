package Interface.Products;

import Classes.Utilities.AppOptions;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Product / inventory options (Phase 4 — the "Options" tab). Edits the shared
 * {@link AppOptions} (low-stock warning threshold, currency symbol) that the
 * rest of the app reads.
 *
 * @author Nkanabo
 */
public class OptionsDialog extends JDialog {

    public OptionsDialog(Frame owner) {
        super(owner, "Options", true);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JTextField lowStock = new JTextField(String.valueOf(AppOptions.getLowStockThreshold()));
        JTextField currency = new JTextField(AppOptions.getCurrencySymbol());
        JTextField secCurrency = new JTextField(AppOptions.getSecondaryCurrency());
        double rate = AppOptions.getSecondaryRate();
        JTextField secRate = new JTextField(rate > 0 ? String.valueOf(rate) : "");

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.add(new JLabel("Low-stock warning threshold"));
        form.add(lowStock);
        form.add(new JLabel("(products at or below this quantity trigger a stock warning)"));
        form.add(new JLabel(" "));
        form.add(new JLabel("Currency symbol"));
        form.add(currency);
        form.add(new JLabel(" "));
        form.add(new JLabel("Secondary currency (Premium — e.g. USD), leave blank to disable"));
        form.add(secCurrency);
        form.add(new JLabel("Secondary rate (units per 1 " + AppOptions.getCurrencySymbol() + ")"));
        form.add(secRate);
        add(form, BorderLayout.CENTER);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        save.addActionListener(e -> {
            double v;
            try {
                v = Double.parseDouble(lowStock.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Threshold must be a number.");
                return;
            }
            if (v < 0) {
                JOptionPane.showMessageDialog(this, "Threshold cannot be negative.");
                return;
            }
            double secR = 0;
            String secTxt = secRate.getText().trim();
            if (!secTxt.isEmpty()) {
                try {
                    secR = Double.parseDouble(secTxt);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Secondary rate must be a number.");
                    return;
                }
            }
            AppOptions.setLowStockThreshold(v);
            AppOptions.setCurrencySymbol(currency.getText().trim());
            AppOptions.setSecondaryCurrency(secCurrency.getText().trim());
            AppOptions.setSecondaryRate(secR);
            JOptionPane.showMessageDialog(this, "Options saved.");
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}
