/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import static Authentication.Auth.Login;
import static Authentication.Encrpytion.encrypt;
import static Database.DBConnect.getConnection;
import static com.nkanabo.Tienda.Utilities.milliConverter;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Nkanabo
 */
public class launcher extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public launcher() throws URISyntaxException {
        initComponents();
        URL resource = getClass().getResource("/images/icons8.jpg");
        File file = Paths.get(resource.toURI()).toFile(); // return a file
        String filepath = Paths.get(resource.toURI()).toFile().getAbsolutePath();
        ImageIcon icon = new ImageIcon(filepath);
        setIconImage(icon.getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ActivationCode = new javax.swing.JPanel();
        KeyLabel = new javax.swing.JLabel();
        key1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        key3 = new javax.swing.JTextField();
        key4 = new javax.swing.JTextField();
        key2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        response = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        ActivationCode.setBackground(new java.awt.Color(204, 204, 204));
        ActivationCode.setBorder(javax.swing.BorderFactory.createTitledBorder("Product activation"));

        KeyLabel.setFont(new java.awt.Font("Lucida Fax", 1, 16)); // NOI18N
        KeyLabel.setText("Enter the Product Code");

        jButton1.setText("SUBMIT");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        jLabel1.setText("-");

        jLabel2.setText("-");

        jLabel3.setText("-");

        response.setForeground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout ActivationCodeLayout = new javax.swing.GroupLayout(ActivationCode);
        ActivationCode.setLayout(ActivationCodeLayout);
        ActivationCodeLayout.setHorizontalGroup(
            ActivationCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ActivationCodeLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(KeyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(ActivationCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ActivationCodeLayout.createSequentialGroup()
                        .addComponent(key1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(key2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(key3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(key4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(response, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(186, Short.MAX_VALUE))
            .addGroup(ActivationCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ActivationCodeLayout.createSequentialGroup()
                    .addGap(461, 461, 461)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(642, Short.MAX_VALUE)))
        );
        ActivationCodeLayout.setVerticalGroup(
            ActivationCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ActivationCodeLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(response, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ActivationCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(KeyLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ActivationCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(key1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(key4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(key3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(key2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
            .addGroup(ActivationCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ActivationCodeLayout.createSequentialGroup()
                    .addGap(171, 171, 171)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(252, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ActivationCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ActivationCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        // TODO add your handling code here:
        //keys input submit button

    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String box1 = key1.getText();
        String box2 = key2.getText();
        String box3 = key3.getText();
        String box4 = key4.getText();

        String app_keys = box1 + box2 + box3 + box4;
        app_keys = encrypt(app_keys);

        LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));

        long dateofExpiry = 0;
        try {
             //60000 is one day into milliseconds
            dateofExpiry = milliConverter(String.valueOf(d1)) + 60000;
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM app_key"
                    + " where product_id = '" + app_keys + "'";
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 4: Extract data from result set 
            if (rs.next()) {
                response.setText("");
                
                //Retrieve by column name 
                //Change to the next page after updating the
                // activation status column
                try {
                    // STEP 3: Execute a query 
                    String updatequery = "UPDATE app_key set activation_status='1',"
                            + "expire_date = '" + dateofExpiry + "'";
                    int rsu = stmt.executeUpdate(updatequery);
                    this.dispose();
                    UserInterface UI = new UserInterface();
                    UI.UserIntfc();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                    this.dispose();
                    Login();
            } else {
                response.setText("Keys provided are invalid");
                //prompt the user to enter the valid keys
            }

            // STEP 5: Clean-up environment 
            rs.close();
            // finally block used to close resources
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
        } catch (Exception e) {
            // Handle errors for Class.forName
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     */
    public static void launcher() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new launcher().setVisible(true);
            } catch (URISyntaxException ex) {
                Logger.getLogger(launcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActivationCode;
    private javax.swing.JLabel KeyLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField key1;
    private javax.swing.JTextField key2;
    private javax.swing.JTextField key3;
    private javax.swing.JTextField key4;
    private javax.swing.JLabel response;
    // End of variables declaration//GEN-END:variables
}
