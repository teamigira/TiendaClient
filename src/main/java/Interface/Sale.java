/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package Interface;

import Classes.AbstractClasses.Order;
import Classes.Functions.Orders;
import static Classes.Functions.Orders.checkOrderIdValidity;
import static Classes.Functions.Orders.listOrders;
import Classes.Functions.Products;
import Classes.Functions.Stocks;
import Classes.Utilities.NumericalFormats;
import static Classes.Utilities.RandomNumbers.generateNumber;
import Classes.Utilities.SearchEngine;
import UserSettings.UserSettings;
import static com.nkanabo.Tienda.Utilities.DoubleConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Nkanabo
 */
public class Sale extends javax.swing.JFrame {

    /**
     * Creates new form Sale
     */
    private final DefaultListModel mod;
    int row, column;
      
    ArrayList<Order> orderlist;
    String headers[] = {"Order Id", "Product Id","Product", "Quantity", "List price", "Discount"};
    DefaultTableModel ordersModel;

    public Sale() throws SQLException, ClassNotFoundException, ParseException {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setExtendedState(Sale.MAXIMIZED_BOTH);
        SearchProductForm.setText("Code, Product name, Bar code scanner");
        jPopupMenu1.add(Autocomplete);
        mod = new DefaultListModel();
        autocompleteProducts.setModel(mod);
        
        //Orders list 
        orderlist = new ArrayList<>();
        ordersModel = new DefaultTableModel(headers, 0);
        OrdersTable.setModel(ordersModel);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        OrdersTable.setDefaultRenderer(String.class, centerRenderer);
        
        loadJtableValues();
        setTotal();
        installOrderMenu();
        installSaleLabels();
    }

    /** Optional note attached to the current order, printed on the receipt. */
    private String orderComment = "";

    /**
     * QA fix: the Discount / Comments / Shipping / Administrator / Customers
     * labels looked clickable (hand cursor) but had no listeners. Wire them.
     */
    private void installSaleLabels() {
        addClick(discountlabel, this::applyLineDiscount);
        addClick(commentslabel, this::editOrderComment);
        addClick(shippinglabel, this::showShippingInfo);
        addClick(adminlabel, this::backToDashboard);
        addClick(customerslabel, this::openCustomers);
    }

    private void addClick(javax.swing.JLabel label, Runnable action) {
        if (label == null) {
            return;
        }
        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
    }

    /** Apply a discount to the selected cart line. */
    private void applyLineDiscount() {
        int r = OrdersTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a cart item first.");
            return;
        }
        try {
            int orderId = com.nkanabo.Tienda.Utilities.IntegerConverter(
                    String.valueOf(ordersModel.getValueAt(r, 0)));
            String name = String.valueOf(ordersModel.getValueAt(r, 2));
            double qty = DoubleConverter(String.valueOf(ordersModel.getValueAt(r, 3)));
            double price = DoubleConverter(String.valueOf(ordersModel.getValueAt(r, 4)));
            String cur = String.valueOf(ordersModel.getValueAt(r, 5));
            String in = JOptionPane.showInputDialog(this,
                    "Discount amount for \"" + name + "\":", cur);
            if (in == null) {
                return;
            }
            double disc = DoubleConverter(in.trim());
            Classes.Functions.Orders.updateOrder(orderId, name, qty, price, disc, "");
            refreshCart();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not apply discount: " + ex.getMessage(),
                    "Discount", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Capture a note that will be printed on the receipt footer. */
    private void editOrderComment() {
        String in = (String) JOptionPane.showInputDialog(this, "Order note (printed on receipt):",
                "Comments", JOptionPane.PLAIN_MESSAGE, null, null, orderComment);
        if (in != null) {
            orderComment = in.trim();
            JOptionPane.showMessageDialog(this, orderComment.isEmpty()
                    ? "Note cleared." : "Note saved — it will appear on the receipt.");
        }
    }

    private void showShippingInfo() {
        JOptionPane.showMessageDialog(this,
                "Shipping is not configured for this store (walk-in sales).\n"
                + "Use the Customers screen to record delivery details if needed.",
                "Shipping", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Return to the main dashboard. */
    private void backToDashboard() {
        try {
            this.dispose();
            new UIv2();
        } catch (ClassNotFoundException | ParseException | SQLException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Open the Customers manager in a dialog. */
    private void openCustomers() {
        try {
            javax.swing.JDialog d = new javax.swing.JDialog(this, "Customers", true);
            d.setContentPane(new Interface.Customers.CustomersPanel());
            d.setSize(820, 480);
            d.setLocationRelativeTo(this);
            d.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not open customers: " + ex.getMessage(),
                    "Customers", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds an "Order" menu bar (Hold / Complete / Reopen) to the Sale window.
     * Done programmatically via {@code setJMenuBar} so the generated GroupLayout
     * of the content pane is left untouched.
     */
    private void installOrderMenu() {
        javax.swing.JMenuBar bar = new javax.swing.JMenuBar();
        javax.swing.JMenu order = new javax.swing.JMenu("Order");

        javax.swing.JMenuItem hold = new javax.swing.JMenuItem("Hold current order");
        javax.swing.JMenuItem complete = new javax.swing.JMenuItem("Complete order (save)");
        javax.swing.JMenu reopen = new javax.swing.JMenu("Reopen order");
        javax.swing.JMenuItem reopenHeld = new javax.swing.JMenuItem("Reopen a held order…");
        javax.swing.JMenuItem reopenDone = new javax.swing.JMenuItem("Reopen a completed order…");
        reopen.add(reopenHeld);
        reopen.add(reopenDone);

        hold.addActionListener(e -> holdOrder());
        complete.addActionListener(e -> completeOrder());
        reopenHeld.addActionListener(e -> reopenHeldOrder());
        reopenDone.addActionListener(e -> reopenCompletedOrder());

        order.add(hold);
        order.add(complete);
        order.addSeparator();
        order.add(reopen);
        bar.add(order);
        setJMenuBar(bar);
    }

    private void refreshCart() {
        try {
            loadJtableValues();
            setTotal();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Park the current cart so a fresh sale can be started. */
    private void holdOrder() {
        if (!Classes.Functions.Orders.hasCartItems()) {
            JOptionPane.showMessageDialog(this, "The cart is empty — nothing to hold.");
            return;
        }
        String label = JOptionPane.showInputDialog(this,
                "Hold this order under a name/note (optional):", "Hold order",
                JOptionPane.PLAIN_MESSAGE);
        if (label == null) {
            return; // cancelled
        }
        String ref = Classes.Functions.Orders.holdCurrentCart(label);
        if (ref != null) {
            JOptionPane.showMessageDialog(this, "Order held. Reopen it later from Order ▸ Reopen.");
            refreshCart();
        } else {
            JOptionPane.showMessageDialog(this, "Could not hold the order.");
        }
    }

    /** Finalise the current cart (same as the Save action). */
    private void completeOrder() {
        if (!Classes.Functions.Orders.hasCartItems()) {
            JOptionPane.showMessageDialog(this, "The cart is empty — nothing to complete.");
            return;
        }
        saveLabelMouseClicked(null);
    }

    private void reopenHeldOrder() {
        java.util.ArrayList<String[]> held = Classes.Functions.Orders.listHeldOrders();
        if (held.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no held orders.");
            return;
        }
        String[] labels = new String[held.size()];
        for (int i = 0; i < held.size(); i++) {
            String[] h = held.get(i);
            String name = h[1] == null || h[1].isEmpty() ? "(no name)" : h[1];
            labels[i] = name + "  —  " + h[2] + " item(s)  —  total " + fmt(h[3]);
        }
        Object choice = JOptionPane.showInputDialog(this, "Pick a held order to reopen:",
                "Reopen held order", JOptionPane.PLAIN_MESSAGE, null, labels, labels[0]);
        if (choice == null) {
            return;
        }
        int idx = java.util.Arrays.asList(labels).indexOf(choice.toString());
        if (idx < 0) {
            return;
        }
        if (Classes.Functions.Orders.reopenHeld(held.get(idx)[0])) {
            JOptionPane.showMessageDialog(this, "Held order reopened into the cart.");
            refreshCart();
        } else {
            JOptionPane.showMessageDialog(this, "Could not reopen that order.");
        }
    }

    private void reopenCompletedOrder() {
        java.util.ArrayList<String[]> done = Classes.Functions.Orders.listRecentCompleted(20);
        if (done.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no completed orders to reopen.");
            return;
        }
        String[] labels = new String[done.size()];
        for (int i = 0; i < done.size(); i++) {
            String[] d = done.get(i);
            labels[i] = "Receipt #" + d[0] + "  —  " + d[1] + " item(s)  —  " + d[2];
        }
        Object choice = JOptionPane.showInputDialog(this,
                "Pick a completed order to reopen (its items return to the cart):",
                "Reopen completed order", JOptionPane.PLAIN_MESSAGE, null, labels, labels[0]);
        if (choice == null) {
            return;
        }
        int idx = java.util.Arrays.asList(labels).indexOf(choice.toString());
        if (idx < 0) {
            return;
        }
        if (Classes.Functions.Orders.reopenCompleted(done.get(idx)[0])) {
            JOptionPane.showMessageDialog(this, "Completed order reopened into the cart.");
            refreshCart();
        } else {
            JOptionPane.showMessageDialog(this, "Could not reopen that order.");
        }
    }

    private String fmt(String raw) {
        try {
            return new NumericalFormats().accountsFormat(Double.parseDouble(raw));
        } catch (Exception e) {
            return raw == null ? "0" : raw;
        }
    }

    public double setProductPrice() throws SQLException, ClassNotFoundException {
        String product_price;
        product_price = Products.getProductPricefromName(SearchProductForm.getText().trim());
        Double a = DoubleConverter(product_price);
        Double b;
        b = DoubleConverter(qtyfield.getText());
        //quantitylabelSales.
        String Totalprice = NumericalFormats.accountsFormat(a*b);
        String prd_price = NumericalFormats.accountsFormat(a);
        pricelabel.setText("Price: " + prd_price);
        totallabel.setText("Total: " + Totalprice);
        return 0;
    }
    
    /*
    Loading functions
     */
    public final void loadJtableValues() throws SQLException, ClassNotFoundException, ParseException {
        ordersModel.setRowCount(0);
        orderlist = listOrders();
        for (int i = 0; i < orderlist.size(); i++) {
            Object[] obj = {
                orderlist.get(i).orderid,
                orderlist.get(i).productid,
                orderlist.get(i).product,
                orderlist.get(i).quantity,
                orderlist.get(i).listprice,
                orderlist.get(i).discount};
            ordersModel.addRow(obj);
        }
        OrdersTable.repaint();
        OrdersTable.revalidate();
    }

    public final void setTotal(){
          Double Total = 0.0;
          int c =   ordersModel.getRowCount();
          
         
          for(int i=0; i<c; i++){
          Double qty = DoubleConverter(ordersModel.getValueAt(i, 3).toString());
          Double price = DoubleConverter(ordersModel.getValueAt(i, 4).toString());
          Total += qty*price;
          }
          UserSettings us = new UserSettings();
          NumericalFormats nf = new NumericalFormats();
          TotalLabel.setText(nf.accountsFormat(Total));
    }

    /**
     * Build a printable receipt from the items currently in the cart table.
     * Company / address / footer come from the last saved receipt settings (if
     * any); the cashier is the logged-in user. Columns: 2=name, 3=qty, 4=price.
     */
    private Classes.Functions.Printers.ReceiptPrinter buildReceipt(String receiptNo) {
        String company = "RECEIPT", address = "", footer = "Thank you for your business!";
        try {
            Classes.AbstractClasses.ReceiptDetails d =
                    Classes.Functions.Printers.Receipts.getLastReceiptDetails();
            if (d != null) {
                if (d.getCompanyName() != null && !d.getCompanyName().trim().isEmpty()) {
                    company = d.getCompanyName();
                }
                if (d.getAddress() != null) {
                    address = d.getAddress();
                }
                if (d.getFooterMessage() != null && !d.getFooterMessage().trim().isEmpty()) {
                    footer = d.getFooterMessage();
                }
            }
        } catch (Exception ignore) {
            // No saved receipt settings yet — use sensible defaults.
        }
        String cashier = Authentication.Sessions.LoggedUser != null
                ? Authentication.Sessions.LoggedUser : "";
        if (orderComment != null && !orderComment.isEmpty()) {
            footer = footer + "\nNote: " + orderComment;
        }
        Classes.Functions.Printers.ReceiptPrinter receipt =
                new Classes.Functions.Printers.ReceiptPrinter(
                        company, address, footer, cashier, receiptNo, "Tsh");
        double total = 0;
        for (int i = 0; i < ordersModel.getRowCount(); i++) {
            String name = String.valueOf(ordersModel.getValueAt(i, 2));
            double qty = DoubleConverter(String.valueOf(ordersModel.getValueAt(i, 3)));
            double price = DoubleConverter(String.valueOf(ordersModel.getValueAt(i, 4)));
            receipt.addItem(name, qty, price);
            total += qty * price;
        }
        receipt.total = total;
        receipt.withSettings(); // printer, template & premium flag from settings
        return receipt;
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
        jSeparator1 = new javax.swing.JSeparator();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        salePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        OrdersTable = new javax.swing.JTable();
        totalPanel = new javax.swing.JPanel();
        TotalLabel = new javax.swing.JLabel();
        TotalLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        SearchProductForm = new javax.swing.JTextField("Code, Product name or use Barcode Scanner");
        quantitylabelSales = new javax.swing.JLabel();
        pricelabel = new javax.swing.JLabel();
        totallabel = new javax.swing.JLabel();
        qtyfield = new javax.swing.JTextField();
        SaveLabel = new javax.swing.JLabel();
        topSalePanel = new javax.swing.JPanel();
        saveLabel = new javax.swing.JLabel();
        discountlabel = new javax.swing.JLabel();
        commentslabel = new javax.swing.JLabel();
        shippinglabel = new javax.swing.JLabel();
        adminlabel = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        closelabel = new javax.swing.JLabel();
        customerslabel = new javax.swing.JLabel();

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
                .addComponent(jSeparator1)
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
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Sale"); // NOI18N
        setUndecorated(true);

        OrdersTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        OrdersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(OrdersTable);

        javax.swing.GroupLayout salePanelLayout = new javax.swing.GroupLayout(salePanel);
        salePanel.setLayout(salePanelLayout);
        salePanelLayout.setHorizontalGroup(
            salePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        salePanelLayout.setVerticalGroup(
            salePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );

        totalPanel.setBorder(new javax.swing.border.MatteBorder(null));

        TotalLabel.setBackground(new java.awt.Color(204, 204, 255));
        TotalLabel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 24)); // NOI18N
        TotalLabel.setForeground(new java.awt.Color(102, 102, 102));
        TotalLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 204, 255), 1, true));

        TotalLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 24)); // NOI18N
        TotalLabel1.setForeground(new java.awt.Color(0, 153, 255));
        TotalLabel1.setText("Total :");

        javax.swing.GroupLayout totalPanelLayout = new javax.swing.GroupLayout(totalPanel);
        totalPanel.setLayout(totalPanelLayout);
        totalPanelLayout.setHorizontalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, totalPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TotalLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        totalPanelLayout.setVerticalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, totalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TotalLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TotalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));

        SearchProductForm.setBackground(new java.awt.Color(255, 255, 204));
        SearchProductForm.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        SearchProductForm.setForeground(new java.awt.Color(204, 204, 204));
        SearchProductForm.setText("Code, Product name, Bar code scanner");
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

        quantitylabelSales.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        quantitylabelSales.setText("QTY.");

        pricelabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pricelabel.setText("Price:");

        totallabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totallabel.setText("Total:");

        qtyfield.setBackground(new java.awt.Color(242, 242, 242));
        qtyfield.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        qtyfield.setForeground(new java.awt.Color(0, 153, 255));
        qtyfield.setText("1");
        qtyfield.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                qtyfieldPropertyChange(evt);
            }
        });
        qtyfield.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtyfieldKeyReleased(evt);
            }
        });

        SaveLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        SaveLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-save-32.png"))); // NOI18N
        SaveLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SaveLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SaveLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(SearchProductForm, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(quantitylabelSales)
                .addGap(18, 18, 18)
                .addComponent(qtyfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pricelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(totallabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SaveLabel)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(SaveLabel))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SearchProductForm)
                        .addComponent(quantitylabelSales)
                        .addComponent(pricelabel)
                        .addComponent(totallabel)
                        .addComponent(qtyfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        topSalePanel.setBackground(new java.awt.Color(204, 204, 204));
        topSalePanel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        saveLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        saveLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-checkmark-16.png"))); // NOI18N
        saveLabel.setText("Save (F2)");
        saveLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saveLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveLabelMouseClicked(evt);
            }
        });

        discountlabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        discountlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-discount.png"))); // NOI18N
        discountlabel.setText("Discount (F3)");
        discountlabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        commentslabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        commentslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icomments.png"))); // NOI18N
        commentslabel.setText("Comments (F4)");
        commentslabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        shippinglabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        shippinglabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-supply-16.png"))); // NOI18N
        shippinglabel.setText("Shipping (F9)");
        shippinglabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        adminlabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        adminlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-male-user-16.png"))); // NOI18N
        adminlabel.setText("Administrator (F6)");
        adminlabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jCheckBox1.setText("Print");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        closelabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2crushpage.png"))); // NOI18N
        closelabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        closelabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closelabelMouseClicked(evt);
            }
        });

        customerslabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        customerslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-users-16.png"))); // NOI18N
        customerslabel.setText("Customers");

        javax.swing.GroupLayout topSalePanelLayout = new javax.swing.GroupLayout(topSalePanel);
        topSalePanel.setLayout(topSalePanelLayout);
        topSalePanelLayout.setHorizontalGroup(
            topSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topSalePanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(saveLabel)
                .addGap(35, 35, 35)
                .addComponent(customerslabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commentslabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(shippinglabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminlabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 198, Short.MAX_VALUE)
                .addComponent(closelabel)
                .addContainerGap())
        );
        topSalePanelLayout.setVerticalGroup(
            topSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topSalePanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(topSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(topSalePanelLayout.createSequentialGroup()
                        .addComponent(closelabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(topSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(saveLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(discountlabel)
                        .addComponent(commentslabel)
                        .addComponent(shippinglabel)
                        .addComponent(adminlabel)
                        .addComponent(jCheckBox1)
                        .addComponent(customerslabel)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(salePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(topSalePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(totalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(topSalePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(salePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closelabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closelabelMouseClicked
        // TODO add your handling code here:
        try {
            this.dispose();
            new UIv2();
        } catch (ClassNotFoundException | ParseException | SQLException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_closelabelMouseClicked

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
                    mod.addElement(item);
                }
            } catch (ClassNotFoundException | ParseException | SQLException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            }
            jPopupMenu1.show(SearchProductForm, 0, SearchProductForm.getHeight());
        }
    }//GEN-LAST:event_SearchProductFormKeyReleased

    private void SearchProductFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchProductFormActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchProductFormActionPerformed

    private void SearchProductFormMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMousePressed
        if (SearchProductForm.getText().equals("Code, Product name, Bar code scanner")) {
            SearchProductForm.setText("");
        }
    }//GEN-LAST:event_SearchProductFormMousePressed

    private void SearchProductFormMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMouseExited
        // TODO add your handling code here:
        if (SearchProductForm.getText().equals("")) {
            SearchProductForm.setText("Code, Product name, Bar code scanner");
        }
    }//GEN-LAST:event_SearchProductFormMouseExited

    private void SearchProductFormMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMouseEntered
        // TODO add your handling code here:

    }//GEN-LAST:event_SearchProductFormMouseEntered

    private void SearchProductFormFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchProductFormFocusLost
        // TODO add your handling code here:
        if (SearchProductForm.getText().equals("")) {
            SearchProductForm.setText("Code, Product name, Bar code scanner");
        }
    }//GEN-LAST:event_SearchProductFormFocusLost

    private void SearchProductFormFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchProductFormFocusGained
        // TODO add your handling code here:
        if (SearchProductForm.getText().equals("")) {
            SearchProductForm.setText("Code, Product name, Bar code scanner");
        }
    }//GEN-LAST:event_SearchProductFormFocusGained

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // "Print" toggle — its state is read when the sale is saved; nothing to
        // do on toggle. (Previously ran a stray product search here.)
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void SearchProductFormMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductFormMouseClicked
        // TODO add your handling code here:  
        if (SearchProductForm.getText().equals("Code, Product name, Bar code scanner")) {
            SearchProductForm.setText("");
        }

    }//GEN-LAST:event_SearchProductFormMouseClicked

    private void autocompleteProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autocompleteProductsMouseClicked
        //if the products drop down list is created.
        int index = autocompleteProducts.getSelectedIndex();
        String s = (String) autocompleteProducts.getSelectedValue();
        SearchProductForm.setText(s);
    }//GEN-LAST:event_autocompleteProductsMouseClicked

    private void SearchProductFormPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_SearchProductFormPropertyChange
        try {
            // This is the function that will be called when the product is changed:
            setProductPrice();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_SearchProductFormPropertyChange

    private void qtyfieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_qtyfieldPropertyChange
        try {
            // When the value of the counter is changed
            //Multiply the quantity to the total.
            if (!SearchProductForm.getText().equals("Code, Product name, Bar code scanner")
                    || !SearchProductForm.getText().equals("")) {
                double setProductPrice = setProductPrice();
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_qtyfieldPropertyChange

    private void qtyfieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtyfieldKeyReleased
        // When key is released change the total value.
        try {
            // When the value of the counter is changed
            //Multiply the quantity to the total.
            if (!SearchProductForm.getText().equals("Code, Product name, Bar code scanner")
                    || !SearchProductForm.getText().equals("")) {
                double setProductPrice = setProductPrice();
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_qtyfieldKeyReleased

    private void SaveLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveLabelMouseClicked
            try {                                       
                //Save the order:
                Double quantity;
                Double discount=0.0;
                quantity = Double.parseDouble(qtyfield.getText());
                String product_id = SearchProductForm.getText();
                SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
                //String backdated = dcn.format(backdate.getDate());
                String backdated = "";
                boolean stockUpdate = false;
                setTotal();
                try {
                    // Make sure the product actually resolves before judging stock,
                    // so an unrecognised name says so instead of "out of stock".
                    String resolved = Products.getProductId(product_id);
                    if (resolved == null || resolved.equals("0")) {
                        JOptionPane.showMessageDialog(this,
                                "Product not found. Please pick it from the suggestions list.");
                    } else {
                        stockUpdate = Stocks.editStock(product_id, quantity);
                        if (stockUpdate) {
                            if (Orders.addOrderFromName(product_id, quantity, discount, backdated)) {
                                JOptionPane.showMessageDialog(this, "Succesfully");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Item Out of stock");
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                loadJtableValues();
                SearchProductForm.setText("Code, Product name, Bar code scanner");
//                LoadStockProducts();        
            } catch (SQLException | ClassNotFoundException | ParseException ex) {
                    Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
                }
    }//GEN-LAST:event_SaveLabelMouseClicked

    private void saveLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveLabelMouseClicked
        // TODO add your handling code here:
            int randomNumber = generateNumber();
            try {
                // Here i am setting text of the code to something random:
                if (!checkOrderIdValidity(
                        String.valueOf(randomNumber)
                )) {
                    row = OrdersTable.getSelectedRow();
                    column = OrdersTable.getColumnCount();
                    
                    
                    //Saving all orders that exists on the table.
                    //Initialising counter.
                    int c =   ordersModel.getRowCount();
                    // Capture the receipt from the current cart BEFORE the items
                    // are moved into the saved order (after saving they leave the cart).
                    Classes.Functions.Printers.ReceiptPrinter receipt =
                            buildReceipt(String.valueOf(randomNumber));
                    try {
                          for(int i=0; i<c; i++){
                        //The lists of Ids
                        String orderId = ordersModel.getValueAt(i, 0).toString();
                        String product_id = ordersModel.getValueAt(i, 1).toString();
                        Orders.saveOrderItem(orderId,product_id, randomNumber);
                        }

                            JOptionPane.showMessageDialog(this,
                                    "Order saved succesfully");
                            loadJtableValues();
                            salePanel.repaint();
                            salePanel.revalidate();
                            // Honour the "Print" checkbox: show the receipt page.
                            if (jCheckBox1.isSelected() && receipt != null) {
                                receipt.showPreview(this);
                            }
                    } catch (ParseException ex) {
                        Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                        System.out.println("Random number fail 687.Sale");
                }

            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
    }//GEN-LAST:event_saveLabelMouseClicked

    public String[] searchSuggestions(String search) throws MalformedURLException, IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        search = search.replace(" ", "+");
        URL oracle
                = new URL("http://suggestqueries.google.com/complete/search?q=" + search + "&client=firefox&hl=fr");
        URLConnection yc = oracle.openConnection();
        String val;
        try ( BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
            val = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Object obj = parser.parse(inputLine);
                JSONArray array = new JSONArray();
                array.put(obj);
                for (Object o : array) {
                    val = o.toString();
                }
            }
        }
        String v[] = val.replace("[", "").replace("\"", "").split(",");
        if (v.length == 1 && v[0].equals("")) {
            return new String[0];
        } else {
            return v;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

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
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Sale().setVisible(true);
            } catch (SQLException | ClassNotFoundException | ParseException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Autocomplete;
    private javax.swing.JTable OrdersTable;
    private javax.swing.JLabel SaveLabel;
    private javax.swing.JTextField SearchProductForm;
    private javax.swing.JLabel TotalLabel;
    private javax.swing.JLabel TotalLabel1;
    private javax.swing.JLabel adminlabel;
    private javax.swing.JList<String> autocompleteProducts;
    private javax.swing.JLabel closelabel;
    private javax.swing.JLabel commentslabel;
    private javax.swing.JLabel customerslabel;
    private javax.swing.JLabel discountlabel;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel pricelabel;
    private javax.swing.JTextField qtyfield;
    private javax.swing.JLabel quantitylabelSales;
    private javax.swing.JPanel salePanel;
    private javax.swing.JLabel saveLabel;
    private javax.swing.JLabel shippinglabel;
    private javax.swing.JPanel topSalePanel;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JLabel totallabel;
    // End of variables declaration//GEN-END:variables

}
