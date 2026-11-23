package Interface;

import Authentication.LicenseManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Enter a licence key. Shows this computer's code (which the customer sends to
 * the seller), a box to paste the key the seller sends back, and, while the
 * free trial lasts, a button to keep using the trial.
 *
 * @author Nkanabo
 */
public class ActivationDialog extends JFrame {

    private final JTextArea keyBox = new JTextArea(4, 40);
    private final JLabel message = new JLabel(" ");
    private final Runnable onDone;

    /**
     * @param onDone runs after a successful activation or "Continue trial";
     *               {@code null} just closes the window
     * @param exitOnClose true at startup (closing the window quits the app)
     */
    public ActivationDialog(Runnable onDone, boolean exitOnClose) {
        super("Activate Tienda POS");
        this.onDone = onDone;
        setDefaultCloseOperation(exitOnClose ? WindowConstants.EXIT_ON_CLOSE : WindowConstants.DISPOSE_ON_CLOSE);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 0, 4, 0);

        JLabel title = new JLabel("Activate Tienda POS");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        body.add(title, c);

        String status;
        if (LicenseManager.currentTier() != LicenseManager.TIER_INVALID && !LicenseManager.isTrial()) {
            status = "Licensed to " + LicenseManager.customer() + " ("
                    + LicenseManager.tierName(LicenseManager.currentTier()) + ") until "
                    + LicenseManager.expiryFormatted() + ".";
        } else {
            long left = LicenseManager.trialDaysLeft();
            status = left > 0 ? "Free trial: " + left + (left == 1 ? " day" : " days") + " left."
                    : "Your free trial has ended. Enter a licence key to continue.";
        }
        body.add(new JLabel(status), c);

        body.add(new JLabel("<html><br>1. Send us this computer's code:</html>"), c);
        JTextField code = new JTextField(LicenseManager.machineCode());
        code.setEditable(false);
        code.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        JButton copy = new JButton("Copy");
        copy.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(code.getText()), null);
            message.setForeground(new Color(0x2E7D32));
            message.setText("Code copied.");
        });
        JPanel codeRow = new JPanel(new BorderLayout(8, 0));
        codeRow.add(code, BorderLayout.CENTER);
        codeRow.add(copy, BorderLayout.EAST);
        body.add(codeRow, c);

        body.add(new JLabel("<html><br>2. Paste the licence key we send you:</html>"), c);
        keyBox.setLineWrap(true);
        keyBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(keyBox);
        scroll.setPreferredSize(new Dimension(460, 90));
        body.add(scroll, c);

        message.setForeground(new Color(0xC62828));
        body.add(message, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton activate = new JButton("Activate");
        activate.addActionListener(e -> activate());
        buttons.add(activate);
        boolean licensed = LicenseManager.currentTier() != LicenseManager.TIER_INVALID && !LicenseManager.isTrial();
        if (!licensed && LicenseManager.trialDaysLeft() > 0) {
            JButton trial = new JButton("Continue trial");
            trial.addActionListener(e -> {
                if (LicenseManager.isActivatedForToday()) {
                    finish();
                }
            });
            buttons.add(trial);
        }
        JButton close = new JButton(exitOnClose ? "Exit" : "Close");
        close.addActionListener(e -> {
            if (exitOnClose) {
                System.exit(0);
            }
            dispose();
        });
        buttons.add(close);
        getRootPane().setDefaultButton(activate);

        getContentPane().add(body, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void activate() {
        int tier = LicenseManager.validate(keyBox.getText());
        if (tier == LicenseManager.TIER_INVALID) {
            message.setForeground(new Color(0xC62828));
            message.setText("<html>" + LicenseManager.lastError() + "</html>");
            pack();
            return;
        }
        LicenseManager.recordActivation(tier);
        javax.swing.JOptionPane.showMessageDialog(this,
                "Tienda is activated for " + LicenseManager.customer() + " ("
                + LicenseManager.tierName(tier) + ") until " + LicenseManager.expiryFormatted() + ".",
                "Activated", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        finish();
    }

    private void finish() {
        dispose();
        if (onDone != null) {
            onDone.run();
        }
    }
}
