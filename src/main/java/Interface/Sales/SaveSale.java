/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interface.Sales;

import Classes.Functions.Orders;
import Classes.Functions.Stocks;
import Classes.Utilities.SearchEngine;
import static com.nkanabo.Tienda.Utilities.DoubleConverter;
import static com.nkanabo.Tienda.Utilities.IntegerConverter;
import java.awt.Component;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Nkanabo
 */
public class SaveSale extends javax.swing.JFrame {

    /**
     * Creates new form SaveSale
     */
    private final DefaultListModel mod;
    private static ArrayList<String> productDetails;
    private static String id;
    public static String quantity;
    public static String name;
    public static String product_price;
    public static String discountoffere;

    public SaveSale() throws ClassNotFoundException, ParseException {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setExtendedState(getExtendedState() | SaveSale.MAXIMIZED_BOTH);
        mod = new DefaultListModel();
        autocompleteProducts.setModel(mod);
        Component add;
        add = jPopupMenu1.add(Autocomplete);
        price.setText(product_price);
        quantityreturn.setText(quantity);
        SearchProductForm.setText(name);
        discount.setText(discountoffere);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Autocomplete = new javax.swing.JPanel();
        autocompleteProducts = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        EditSalePanel = new javax.swing.JPanel();
        SaveLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        selectprdlabel = new javax.swing.JLabel();
        SearchProductForm = new javax.swing.JTextField();
        returnquantitylabel = new javax.swing.JLabel();
        quantityreturn = new javax.swing.JTextField();
        price = new javax.swing.JTextField();
        returnquantitylabel1 = new javax.swing.JLabel();
        discount = new javax.swing.JTextField();
        returnquantitylabel2 = new javax.swing.JLabel();

        autocompleteProducts.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        autocompleteProducts.setForeground(new java.awt.Color(0, 153, 255));
        autocompleteProducts.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        autocompleteProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autocompleteProductsMouseClicked(evt);
            }
        });

        jLabel8.setText("Image");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(108, 108, 108))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel8)
                .addGap(40, 40, 40)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AutocompleteLayout = new javax.swing.GroupLayout(Autocomplete);
        Autocomplete.setLayout(AutocompleteLayout);
        AutocompleteLayout.setHorizontalGroup(
            AutocompleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AutocompleteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(autocompleteProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        AutocompleteLayout.setVerticalGroup(
            AutocompleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AutocompleteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AutocompleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(autocompleteProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPopupMenu1.setFocusable(false);

        SaveLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        SaveLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-checkmark-32.png"))); // NOI18N
        SaveLabel.setText("Save");
        SaveLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SaveLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SaveLabelMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-ban-32.png"))); // NOI18N
        jLabel1.setText("Cancel");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/erasericon.png"))); // NOI18N
        jLabel2.setText("Clear");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jSeparator1.setForeground(javax.swing.UIManager.getDefaults().getColor("Actions.GreyInline"));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 255), 5));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sale Details"));

        selectprdlabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        selectprdlabel.setText("Select Product");

        SearchProductForm.setBackground(new java.awt.Color(255, 255, 204));
        SearchProductForm.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        SearchProductForm.setForeground(new java.awt.Color(204, 204, 204));
        SearchProductForm.setBorder(null);
        SearchProductForm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SearchProductFormFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                SearchProductFormFocusLost(evt);
            }
        });
        SearchProductForm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SearchProductFormMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SearchProductFormMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SearchProductFormMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                SearchProductFormMousePressed(evt);
            }
        });
        SearchProductForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchProductFormActionPerformed(evt);
            }
        });
        SearchProductForm.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                SearchProductFormPropertyChange(evt);
            }
        });
        SearchProductForm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchProductFormKeyReleased(evt);
            }
        });

        returnquantitylabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        returnquantitylabel.setText("Quantity");

        quantityreturn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        price.setEditable(false);
        price.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        price.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        returnquantitylabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        returnquantitylabel1.setText("Price");

        discount.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        discount.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        returnquantitylabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        returnquantitylabel2.setText("Discount");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectprdlabel)
                    .addComponent(returnquantitylabel)
                    .addComponent(returnquantitylabel1)
                    .addComponent(returnquantitylabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SearchProductForm, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discount, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectprdlabel)
                    .addComponent(SearchProductForm))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnquantitylabel)
                    .addComponent(quantityreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnquantitylabel1)
                    .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnquantitylabel2)
                    .addComponent(discount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(410, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EditSalePanelLayout = new javax.swing.GroupLayout(EditSalePanel);
        EditSalePanel.setLayout(EditSalePanelLayout);
        EditSalePanelLayout.setHorizontalGroup(
            EditSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(EditSalePanelLayout.createSequentialGroup()
                .addGroup(EditSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EditSalePanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(SaveLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EditSalePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        EditSalePanelLayout.setVerticalGroup(
            EditSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EditSalePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(EditSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(EditSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SaveLabel)
                        .addComponent(jLabel2))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(EditSalePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(EditSalePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SearchProductFormFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchProductFormFocusGained
        // TODO add your handling code here:
        if (SearchProductForm.getText().equals("")) {
            SearchProductForm.setText("Code, Product name, Bar code scanner");
        }
    }//GEN-LAST:event_SearchProductFormFocusGained

    private void SearchProductFormFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchProductFormFocusLost
        // TODO add your handling code here:
        if (SearchProductForm.getText().equals("")) {
            SearchProductForm.setText("Code, Product name, Bar code scanner");
        }
    }//GEN-LAST:event_SearchProductFormFocusLost

    private void SearchProductFormMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMouseClicked
        // TODO add your handling code here:
        if (SearchProductForm.getText().equals("Code, Product name, Bar code scanner")) {
            SearchProductForm.setText("");
        }
    }//GEN-LAST:event_SearchProductFormMouseClicked

    private void SearchProductFormMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchProductFormMouseEntered

    private void SearchProductFormMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMouseExited
        // TODO add your handling code here:
        if (SearchProductForm.getText().equals("")) {
            SearchProductForm.setText("Code, Product name, Bar code scanner");
        }
    }//GEN-LAST:event_SearchProductFormMouseExited

    private void SearchProductFormMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMousePressed
        if (SearchProductForm.getText().equals("Code, Product name, Bar code scanner")) {
            SearchProductForm.setText("");
        }
    }//GEN-LAST:event_SearchProductFormMousePressed

    private void SearchProductFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchProductFormActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchProductFormActionPerformed

    private void SearchProductFormPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_SearchProductFormPropertyChange

    }//GEN-LAST:event_SearchProductFormPropertyChange

    private void SearchProductFormKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchProductFormKeyReleased
        // Search engine
        String Search = SearchProductForm.getText().trim();
        if (Search.equals("Code, Product name, Bar code scanner")) {
        } else {
            mod.removeAllElements();
            SearchEngine searchEngine = new SearchEngine();
            try {
                //                for(String item:searchEngine.ProductsearchSuggestions(Search)){
                for (String item : searchEngine.productsSearch(Search)) {
                    System.out.println("selected item" + item);
                    mod.addElement(item);
                }
            } catch (ClassNotFoundException | ParseException | SQLException ex) {
                Logger.getLogger(SaveSale.class.getName()).log(Level.SEVERE, null, ex);
            }
            jPopupMenu1.show(SearchProductForm, 0, SearchProductForm.getHeight());
        }
    }//GEN-LAST:event_SearchProductFormKeyReleased

    private void autocompleteProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autocompleteProductsMouseClicked
        //if the products drop down list is created.
        int index = autocompleteProducts.getSelectedIndex();
        String s = (String) autocompleteProducts.getSelectedValue();
        SearchProductForm.setText(s);
    }//GEN-LAST:event_autocompleteProductsMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        price.setText("");
        quantityreturn.setText("");
        SearchProductForm.setText("");
    }//GEN-LAST:event_jLabel2MouseClicked

    private void SaveLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveLabelMouseClicked
        Double qty = DoubleConverter(quantityreturn.getText());
        Double prc = DoubleConverter(product_price);
        Double orderdiscount = DoubleConverter(discount.getText());
            LocalDateTime backdate = LocalDateTime.now();
            String day = backdate.toString();
        try {
            SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(backdate);
//          String backdated = dcn.format(backdate);
            int iid = IntegerConverter(id);
            Stocks.editStockFromOrdersEd(iid, name, qty);
            if (Orders.updateOrder(iid, name, qty, prc,
                    orderdiscount, day)) {
                JOptionPane.showMessageDialog(this, "Succesfully");
            }
        } catch (ClassNotFoundException | ParseException  ex) {
            Logger.getLogger(SaveSale.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_SaveLabelMouseClicked

    /**
     * @param c
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
     */
    
    public static void test(){
        
    }
    
    public static void setMeUp(ArrayList<String> c) throws ClassNotFoundException, ParseException {
        id = c.get(0);
        name = c.get(1);
        product_price = c.get(2);
        quantity = c.get(3);
        discountoffere = c.get(4);
        main(null);
    }

    public static void main(String[] args) throws ClassNotFoundException,
            ParseException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SaveSale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new SaveSale().setVisible(true);
            } catch (ClassNotFoundException | ParseException ex) {
                Logger.getLogger(SaveSale.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Autocomplete;
    private javax.swing.JPanel EditSalePanel;
    private javax.swing.JLabel SaveLabel;
    public static javax.swing.JTextField SearchProductForm;
    private javax.swing.JList<String> autocompleteProducts;
    public static javax.swing.JTextField discount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    public static javax.swing.JTextField price;
    public static javax.swing.JTextField quantityreturn;
    private javax.swing.JLabel returnquantitylabel;
    private javax.swing.JLabel returnquantitylabel1;
    private javax.swing.JLabel returnquantitylabel2;
    private javax.swing.JLabel selectprdlabel;
    // End of variables declaration//GEN-END:variables
}
