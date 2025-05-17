/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interface.Sales;

import Authentication.Sessions;
import Classes.AbstractClasses.Order;
import Classes.AbstractClasses.ReceiptDetails;
import Classes.AbstractClasses.ReceiptSettings;
import Classes.Utilities.Resources;
import Interface.launcher;

import static Classes.Functions.Orders.listOrders;
import static Classes.Functions.Printers.BarcodeGenerator.generateCode;
import static Classes.Functions.Printers.PrinterSelector.getAvailablePrinters;
import static Classes.Functions.Printers.Receipts.getLastReceiptDetails;
import static Classes.Functions.Printers.Receipts.getLastReceiptSettings;
import static Classes.Functions.Printers.Receipts.saveReceiptDetails;
import Classes.Utilities.OS;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Line2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.plaf.basic.BasicSeparatorUI;
import static sun.jvm.hotspot.HelloWorld.e;
import java.awt.CardLayout;
import java.util.Date;

/**
 *
 * @author Nkanabo
 */
public class PrinterSettings extends javax.swing.JFrame implements ItemListener {

    /**
     * Creates new form Return
     */
    /*Sales variables declaration*/
    int row, column, clickcount;
    Object[] columnNames = {"Select", "Order Id", "Product",
        "Quantity", "List price", "Discount"
    };
    ReceiptDetails lasttReceipts;
    ReceiptSettings PrinterSettings;

    public static String template;




    DefaultTableModel ReturnModel = new DefaultTableModel(0, 0) {
        @Override
        public Class getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Boolean.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                case 3:
                    return String.class;
                case 4:
                    return String.class;
                case 5:
                    return String.class;
                default:
                    return String.class;
            }
        }
    };

    public ArrayList<String> prd_details = new ArrayList<>();
    // Initialize CardLayout and JPanel to hold cards
        
        
    /*End of variables declaration*/
    public PrinterSettings() throws SQLException, ClassNotFoundException, ParseException {
        initComponents();

        this.setLocationRelativeTo(null);

        this.setResizable(true);

        // Initialize CardLayout and JPanel to hold cards
   
        
        JSeparator jSeparator1 = new JSeparator();
        setDottedStyle(jSeparatorbottom);
        setDottedStyle(jSeparator1);
        setDottedStyle(jSeparator2);
        setDottedStyle(jSeparator3);
        setDottedStyle(jSeparator4);
        setDottedStyle(jSeparator5);
        setDottedStyle(jSeparator6);
        setDottedStyle(jSeparator7);
        setDottedStyle(jSeparator8);
        String today = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        todays.setText(String.valueOf(Calendar.getInstance().getTime()));
        jLabel88.setText(String.valueOf(Calendar.getInstance().getTime()));
        String customername = Sessions.LoggedUser;
        Customernamelabel.setText(customername);
        jLabel89.setText(customername);

        // Set preferred size to control width
        String label11 = "Description: This template includes the "
                + "company's address and a footer section.";

        String label12 = "Description: This template features a logo, "
                + "customer details section, itemized list of purchased"
                + " items, and a total amount.";

        String label13 = "Description: This template displays a header with the "
                + "company's name and logo, customer information, "
                + "itemized list of products, subtotal, taxes, and"
                + " total amount.";

        String label14 = "Description: This template showcases a minimalist"
                + " design with " + "essential information such as company details, "
                + "customer details, and a summary of purchased items.";

        String label15 = "Description: This template includes a header with "
                + "the company's logo and name, a table layout for itemized "
                + "purchases, and a footer section with payment information "
                + "and thank you message.";

        jLabel101.setText("<html>" + label11 + "</html>");
        jLabel102.setText("<html>" + label12 + "</html>");
        jLabel103.setText("<html>" + label13 + "</html>");
        jLabel104.setText("<html>" + label14 + "</html>");
        jLabel105.setText("<html>" + label15 + "</html>");

        // Assuming AddressText contains the sentence you want to break
        String AddressText = Address.getText();
        // Replace commas with <br> to break lines
        AddressText = AddressText.replaceAll(",\\s*", ",<br>");

        // Set the text of the JLabel with HTML formatting
        Address.setText("<html><div style='text-align: center;'>" + AddressText + "</div></html>");
        Address1.setText("<html><div style='text-align: center;'>" + AddressText + "</div></html>");
        Address2.setText("<html><div style='text-align: center;'>" + AddressText + "</div></html>");
        // Set the text of the JLabel with HTML formatting
//        Address.setText("<html>" + AddressText + "</div></html>");

        // Optionally, set preferred size
        Address.setPreferredSize(new Dimension(300, Address.getPreferredSize().height));
        Address1.setPreferredSize(new Dimension(300, Address.getPreferredSize().height));
        Address2.setPreferredSize(new Dimension(300, Address.getPreferredSize().height));

        jLabel101.setPreferredSize(new Dimension(300, jLabel101.getPreferredSize().height));
        jLabel102.setPreferredSize(new Dimension(300, jLabel102.getPreferredSize().height));
        jLabel103.setPreferredSize(new Dimension(300, jLabel103.getPreferredSize().height));
        jLabel104.setPreferredSize(new Dimension(300, jLabel104.getPreferredSize().height));
        jLabel105.setPreferredSize(new Dimension(300, jLabel105.getPreferredSize().height));

        tmp1.addItemListener(this);
        tmp2.addItemListener(this);
        tmp3.addItemListener(this);
        tmp4.addItemListener(this);
        tmp5.addItemListener(this);

        try {

            String url = "resources/images/icons8.jpg";
            Resources rs = new Resources();
            File is = rs.getFileFromResource(url);

            String filepath = Paths.get(is.toURI()).toFile().getAbsolutePath();
            ImageIcon icon = new ImageIcon(filepath);
            setIconImage(icon.getImage());

        } catch (URISyntaxException ex) {
            Logger.getLogger(launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        // add header in table model     
        ReturnModel.setColumnIdentifiers(columnNames);
        //OrdersTable.setModel(ReturnModel);
     
        printerSelector();
    }

    // Method to set the dotted style for the JSeparator
    private static void setDottedStyle(JSeparator separator) {
        separator.setUI(new BasicSeparatorUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();

                int startX = 0;
                int endX = c.getWidth();
                int y = c.getHeight() / 2;

                // Adjust the spacing and length of each dot as needed
                int dotSpacing = 10; // Space between dots
                int dotLength = 5; // Length of each dot

                // Draw the dotted line
                for (int x = startX; x < endX; x += dotSpacing + dotLength) {
                    g2d.drawLine(x, y, x + dotLength, y);
                }

                g2d.dispose();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Header = new javax.swing.JPanel();
        back = new javax.swing.JLabel();
        front = new javax.swing.JLabel();
        cancel = new javax.swing.JLabel();
        ReturnSaleBody = new javax.swing.JPanel();
        SelectionMeans = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        PrintAutomatically = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        PrinterComboBox = new javax.swing.JComboBox<>();
        SelectRow = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        tmp1 = new javax.swing.JCheckBox();
        tmp2 = new javax.swing.JCheckBox();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Template = new javax.swing.JPanel();
        EmptyTemplate = new javax.swing.JPanel();
        Template103 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        Address1 = new javax.swing.JLabel();
        Barcode = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        Template104 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        Address2 = new javax.swing.JLabel();
        Barcode1 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        Template105 = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        Template101 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSeparatorbottom = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        Terminal = new javax.swing.JLabel();
        terminalval = new javax.swing.JLabel();
        DateLabel = new javax.swing.JLabel();
        todays = new javax.swing.JLabel();
        Terminal1 = new javax.swing.JLabel();
        Customernamelabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        Template102 = new javax.swing.JPanel();
        Address = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        tmp3 = new javax.swing.JCheckBox();
        jLabel103 = new javax.swing.JLabel();
        tmp4 = new javax.swing.JCheckBox();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        tmp5 = new javax.swing.JCheckBox();
        SelectAmount = new javax.swing.JPanel();
        SelectionArea = new javax.swing.JPanel();
        company = new javax.swing.JTextField();
        qtyamounttobereturned = new javax.swing.JLabel();
        savebutton = new javax.swing.JButton();
        prdnametobereturned = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        footermsg = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        AddressTexts = new javax.swing.JTextArea();
        ReturnMethod = new javax.swing.JPanel();
        viewReceipt = new javax.swing.JPanel();
        EmptyTemplate1 = new javax.swing.JPanel();
        Template106 = new javax.swing.JPanel();
        jLabel95 = new javax.swing.JLabel();
        Address3 = new javax.swing.JLabel();
        Barcode2 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel113 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        Template108 = new javax.swing.JPanel();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        Template109 = new javax.swing.JPanel();
        jLabel152 = new javax.swing.JLabel();
        jSeparatorbottom1 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        Terminal2 = new javax.swing.JLabel();
        terminalval1 = new javax.swing.JLabel();
        DateLabel1 = new javax.swing.JLabel();
        todays1 = new javax.swing.JLabel();
        Terminal3 = new javax.swing.JLabel();
        Customernamelabel1 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        jLabel153 = new javax.swing.JLabel();
        jLabel154 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        jLabel159 = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        jLabel165 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        jSeparator22 = new javax.swing.JSeparator();
        Template110 = new javax.swing.JPanel();
        Address5 = new javax.swing.JLabel();
        jSeparator23 = new javax.swing.JSeparator();
        jLabel167 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        jLabel169 = new javax.swing.JLabel();
        jLabel170 = new javax.swing.JLabel();
        jLabel171 = new javax.swing.JLabel();
        jLabel172 = new javax.swing.JLabel();
        jLabel173 = new javax.swing.JLabel();
        jLabel174 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        jLabel176 = new javax.swing.JLabel();
        jLabel177 = new javax.swing.JLabel();
        jLabel178 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        jLabel179 = new javax.swing.JLabel();
        jLabel180 = new javax.swing.JLabel();
        jLabel181 = new javax.swing.JLabel();
        jLabel182 = new javax.swing.JLabel();
        jLabel183 = new javax.swing.JLabel();
        Template107 = new javax.swing.JPanel();
        jLabel120 = new javax.swing.JLabel();
        Address4 = new javax.swing.JLabel();
        Barcode3 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        SelectShipping = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jRadioButton5 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Header.setBorder(new javax.swing.border.MatteBorder(null));

        back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-back-16.png"))); // NOI18N
        back.setText("Back");
        back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backMouseClicked(evt);
            }
        });

        front.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        front.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-forward-16.png"))); // NOI18N
        front.setText("Next");
        front.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        front.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frontMouseClicked(evt);
            }
        });

        cancel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-ban-32.png"))); // NOI18N
        cancel.setText("Cancel");
        cancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(front, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(front)
                    .addComponent(cancel))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        ReturnSaleBody.setLayout(new java.awt.CardLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-print-32.png"))); // NOI18N
        jLabel4.setText("PRINTER SETTINGS");

        PrintAutomatically.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        PrintAutomatically.setText("Print Automatically");
        PrintAutomatically.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("1");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("2");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Select Printer");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        PrinterComboBox.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        PrinterComboBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout SelectionMeansLayout = new javax.swing.GroupLayout(SelectionMeans);
        SelectionMeans.setLayout(SelectionMeansLayout);
        SelectionMeansLayout.setHorizontalGroup(
            SelectionMeansLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectionMeansLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(SelectionMeansLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(SelectionMeansLayout.createSequentialGroup()
                        .addGroup(SelectionMeansLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SelectionMeansLayout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(SelectionMeansLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PrinterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PrintAutomatically, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(1535, Short.MAX_VALUE))
                    .addGroup(SelectionMeansLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1708, 1708, 1708))))
        );
        SelectionMeansLayout.setVerticalGroup(
            SelectionMeansLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectionMeansLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(SelectionMeansLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(PrintAutomatically))
                .addGap(47, 47, 47)
                .addGroup(SelectionMeansLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PrinterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1339, Short.MAX_VALUE))
        );

        ReturnSaleBody.add(SelectionMeans, "card2");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Select Template and click next");

        tmp1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tmp1.setText("Template 101: ");
        tmp1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tmp2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tmp2.setText("Template 102: ");
        tmp2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel101.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel101.setText("jLabel8");

        jLabel102.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel102.setText("jLabel8");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("3.");

        Template.setBackground(new java.awt.Color(255, 255, 255));
        Template.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RECEIPT TEMPLATE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(153, 153, 153))); // NOI18N
        Template.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout EmptyTemplateLayout = new javax.swing.GroupLayout(EmptyTemplate);
        EmptyTemplate.setLayout(EmptyTemplateLayout);
        EmptyTemplateLayout.setHorizontalGroup(
            EmptyTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        EmptyTemplateLayout.setVerticalGroup(
            EmptyTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1500, Short.MAX_VALUE)
        );

        Template.add(EmptyTemplate, "card7");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 0, 255));
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-dolphin-48.png"))); // NOI18N
        jLabel25.setText("Dolphin Printers");

        Address1.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        Address1.setText("Solar MicroSystems Pte Ltd, 33Ubi Ave 3 #07-50, VERTEX (Tower A) singapore 408868 Tel: + +65 6336 0300, UEN: 123456789A GST No: 123456789A, Opening Hours: 10am to 10pm (Daily) Refund no: SB1005 Date: 2/4/17");
        Address1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Barcode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel43.setText("Qty");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel44.setText("1");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel45.setText("2");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel46.setText("P104 MB Cable ");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel47.setText("MB Cable 101");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel48.setText("Description");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel49.setText("Amount");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel50.setText("172.26");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel51.setText("96.30");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel52.setText("245.37");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel53.setText("B108 - MB Board ");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel54.setText("3");

        jSeparator9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel55.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel55.setText("Subtotal");

        jLabel56.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel56.setText("Discount");

        jLabel57.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel57.setText("Total");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel58.setText("100.26");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel59.setText("85.96");

        jLabel60.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel60.setText("12%");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel61.setText("105.6");

        javax.swing.GroupLayout Template103Layout = new javax.swing.GroupLayout(Template103);
        Template103.setLayout(Template103Layout);
        Template103Layout.setHorizontalGroup(
            Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template103Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template103Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator9)
                            .addGroup(Template103Layout.createSequentialGroup()
                                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel43))
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel54))
                                .addGap(36, 36, 36)
                                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel48)
                                    .addComponent(jLabel47)
                                    .addComponent(jLabel46)
                                    .addComponent(jLabel53))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(112, 112, 112))))
                    .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template103Layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template103Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(163, 163, 163))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template103Layout.createSequentialGroup()
                        .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Address1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Template103Layout.createSequentialGroup()
                                .addComponent(Barcode)
                                .addGap(76, 76, 76)))
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template103Layout.createSequentialGroup()
                        .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel57)
                            .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel56)
                                .addComponent(jLabel55))
                            .addComponent(jLabel60))
                        .addGap(53, 53, 53)
                        .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel58)
                            .addComponent(jLabel59)
                            .addComponent(jLabel61))
                        .addGap(120, 120, 120))))
        );
        Template103Layout.setVerticalGroup(
            Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template103Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(Address1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49))
                .addGap(18, 18, 18)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47)
                    .addComponent(jLabel50))
                .addGap(18, 18, 18)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jLabel46)
                    .addComponent(jLabel51))
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template103Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54)
                            .addComponent(jLabel53)))
                    .addGroup(Template103Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel52)))
                .addGap(25, 25, 25)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jLabel58))
                .addGap(18, 18, 18)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jLabel59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel60)
                .addGap(2, 2, 2)
                .addGroup(Template103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(jLabel61))
                .addContainerGap(805, Short.MAX_VALUE))
        );

        Template.add(Template103, "card4");

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(51, 0, 255));
        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-dolphin-48.png"))); // NOI18N
        jLabel62.setText("Dolphin Printers");

        Address2.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        Address2.setText("Solar MicroSystems Pte Ltd, 33Ubi Ave 3 #07-50, VERTEX (Tower A) singapore 408868 Tel: + +65 6336 0300, UEN: 123456789A GST No: 123456789A, Opening Hours: 10am to 10pm (Daily) Refund no: SB1005 Date: 2/4/17");
        Address2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Barcode1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel63.setText("Qty");

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel64.setText("Description");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel65.setText("Amount");

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel66.setText("1");

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel67.setText("2");

        jLabel68.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel68.setText("MB Cable 101");

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel69.setText("P104 MB Cable ");

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel70.setText("172.26");

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel71.setText("96.30");

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel72.setText("3");

        jLabel73.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel73.setText("B108 - MB Board ");

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel74.setText("245.37");

        jSeparator11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel75.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel75.setText("100.26");

        jLabel76.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel76.setText("Subtotal");

        jLabel77.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel77.setText("Discount");

        jLabel78.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel78.setText("85.96");

        jLabel79.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel79.setText("12%");

        jLabel80.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel80.setText("Total");

        jLabel81.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel81.setText("105.6");

        jSeparator12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel82.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel82.setText("Order #:");

        jLabel83.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel83.setText("Sold To:");

        jLabel84.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel84.setText("Order Date:");

        jLabel85.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel85.setText("Sales Type:");

        jLabel86.setText("01908383892938");

        jLabel87.setText("WASTER PRINTERS");

        jLabel88.setText("jLabel88");

        jLabel89.setText("jLabel89");

        jLabel90.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel90.setText("Sales Person:");

        jLabel91.setText("Closed");

        javax.swing.GroupLayout Template104Layout = new javax.swing.GroupLayout(Template104);
        Template104.setLayout(Template104Layout);
        Template104Layout.setHorizontalGroup(
            Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template104Layout.createSequentialGroup()
                .addGap(94, 94, Short.MAX_VALUE)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template104Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addGap(184, 184, 184))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template104Layout.createSequentialGroup()
                        .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Address2, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Template104Layout.createSequentialGroup()
                                .addComponent(Barcode1)
                                .addGap(76, 76, 76)))
                        .addGap(77, 77, 77))))
            .addGroup(Template104Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator11)
                    .addGroup(Template104Layout.createSequentialGroup()
                        .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel63))
                            .addComponent(jLabel67)
                            .addComponent(jLabel72))
                        .addGap(36, 36, 36)
                        .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel64)
                            .addComponent(jLabel68)
                            .addComponent(jLabel69)
                            .addComponent(jLabel73))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel70, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel74, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel65, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(112, 112, 112))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template104Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel80)
                            .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel77)
                                .addComponent(jLabel76))
                            .addComponent(jLabel79))
                        .addGap(53, 53, 53)
                        .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel75)
                            .addComponent(jLabel78)
                            .addComponent(jLabel81))
                        .addGap(114, 114, 114)))
                .addContainerGap())
            .addComponent(jSeparator12)
            .addGroup(Template104Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template104Layout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel91))
                    .addGroup(Template104Layout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel89))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template104Layout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel88))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template104Layout.createSequentialGroup()
                        .addComponent(jLabel83)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel87))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template104Layout.createSequentialGroup()
                        .addComponent(jLabel82)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel86)))
                .addGap(135, 135, 135))
        );
        Template104Layout.setVerticalGroup(
            Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template104Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addComponent(Address2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Barcode1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(jLabel86))
                .addGap(18, 18, 18)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(jLabel87))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(jLabel88))
                .addGap(23, 23, 23)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(jLabel90))
                .addGap(18, 18, 18)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(jLabel91))
                .addGap(42, 42, 42)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(jLabel64)
                    .addComponent(jLabel65))
                .addGap(18, 18, 18)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68)
                    .addComponent(jLabel70))
                .addGap(18, 18, 18)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(jLabel69)
                    .addComponent(jLabel71))
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template104Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel72)
                            .addComponent(jLabel73)))
                    .addGroup(Template104Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel74)))
                .addGap(25, 25, 25)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(jLabel75))
                .addGap(18, 18, 18)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(jLabel78))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel79)
                .addGap(2, 2, 2)
                .addGroup(Template104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(jLabel81))
                .addContainerGap(575, Short.MAX_VALUE))
        );

        Template.add(Template104, "card5");

        jLabel92.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel92.setText("Premium Template");

        jLabel93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-password-32.png"))); // NOI18N

        javax.swing.GroupLayout Template105Layout = new javax.swing.GroupLayout(Template105);
        Template105.setLayout(Template105Layout);
        Template105Layout.setHorizontalGroup(
            Template105Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template105Layout.createSequentialGroup()
                .addGap(275, 275, 275)
                .addComponent(jLabel92)
                .addContainerGap(179, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template105Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel93)
                .addGap(246, 246, 246))
        );
        Template105Layout.setVerticalGroup(
            Template105Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template105Layout.createSequentialGroup()
                .addGap(254, 254, 254)
                .addComponent(jLabel93)
                .addGap(33, 33, 33)
                .addComponent(jLabel92)
                .addContainerGap(1156, Short.MAX_VALUE))
        );

        Template.add(Template105, "card6");

        jLabel8.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel8.setText("RECEIPT");

        jSeparatorbottom.setBackground(new java.awt.Color(204, 204, 204));
        jSeparatorbottom.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator2.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator2.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator1.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        Terminal.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        Terminal.setText("CUSTOMER");

        terminalval.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        terminalval.setText("Supermarket");

        DateLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        DateLabel.setText("DATE: ");

        todays.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        todays.setText("today");

        Terminal1.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        Terminal1.setText("TERMINAL");

        Customernamelabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        Customernamelabel.setText("customer");

        jSeparator3.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator3.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator4.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator4.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator5.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator5.setForeground(new java.awt.Color(204, 204, 204));

        jLabel10.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel11.setText("TOTAL");

        jLabel13.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel13.setText("THANK YOU");

        jLabel14.setText("179,000");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("1. Product");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("2. Product");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("3. Product");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setText("4. Product");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("5. Product");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel20.setText("50,000");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setText("12,000");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel22.setText("25,000");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setText("43,000");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setText("39,000");

        jSeparator6.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator6.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout Template101Layout = new javax.swing.GroupLayout(Template101);
        Template101.setLayout(Template101Layout);
        Template101Layout.setHorizontalGroup(
            Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template101Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparatorbottom)
                    .addComponent(jSeparator2))
                .addContainerGap())
            .addGroup(Template101Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator3)
                .addContainerGap())
            .addGroup(Template101Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator5)
                .addContainerGap())
            .addGroup(Template101Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(77, 77, 77))
            .addGroup(Template101Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20)
                    .addComponent(jLabel24)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addGap(110, 110, 110))
            .addGroup(Template101Layout.createSequentialGroup()
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template101Layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Template101Layout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addComponent(jLabel13))
                    .addGroup(Template101Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Template101Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Terminal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Customernamelabel))
                    .addGroup(Template101Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Terminal1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(terminalval)))
                .addContainerGap(178, Short.MAX_VALUE))
            .addGroup(Template101Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(DateLabel)
                .addGap(18, 18, 18)
                .addComponent(todays)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6)
                .addContainerGap())
            .addGroup(Template101Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(Template101Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator4)
                .addContainerGap())
        );
        Template101Layout.setVerticalGroup(
            Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template101Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template101Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jSeparatorbottom, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(terminalval)
                            .addComponent(Terminal1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Template101Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DateLabel)
                            .addComponent(todays))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Terminal)
                    .addComponent(Customernamelabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel21))
                .addGap(27, 27, 27)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel22))
                .addGap(28, 28, 28)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel23))
                .addGap(28, 28, 28)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 655, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(Template101Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel13)
                .addGap(26, 26, 26))
        );

        Template.add(Template101, "card2");

        Address.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        Address.setText("Solar MicroSystems Pte Ltd, 33Ubi Ave 3 #07-50, VERTEX (Tower A) singapore 408868 Tel: + +65 6336 0300, UEN: 123456789A GST No: 123456789A, Opening Hours: 10am to 10pm (Daily) Refund no: SB1005 Date: 2/4/17");
        Address.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel26.setText("Qty");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setText("Description");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel28.setText("Amount");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel29.setText("1");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel30.setText("2");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setText("3");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel32.setText("MB Cable 101");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel33.setText("172.26");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel34.setText("P104 MB Cable ");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel35.setText("96.30");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel36.setText("B108 - MB Board ");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel37.setText("245.37");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel38.setText("Total Tax Inclusive");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel39.setText("513.94");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel40.setText("Paid By Cash");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel41.setText("Thank You!");

        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        javax.swing.GroupLayout Template102Layout = new javax.swing.GroupLayout(Template102);
        Template102.setLayout(Template102Layout);
        Template102Layout.setHorizontalGroup(
            Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template102Layout.createSequentialGroup()
                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template102Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator8)
                            .addGroup(Template102Layout.createSequentialGroup()
                                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel31))
                                .addGap(36, 36, 36)
                                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(Template102Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel28))
                                    .addGroup(Template102Layout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addGap(201, 201, 201)
                                        .addComponent(jLabel33))
                                    .addGroup(Template102Layout.createSequentialGroup()
                                        .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel34)
                                            .addComponent(jLabel36))
                                        .addGap(173, 173, 173)
                                        .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel37)
                                            .addComponent(jLabel35)))))))
                    .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Template102Layout.createSequentialGroup()
                        .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(Template102Layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(Address, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Template102Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addGroup(Template102Layout.createSequentialGroup()
                                        .addComponent(jLabel38)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel39)
                                        .addGap(95, 95, 95)))))
                        .addGap(0, 53, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template102Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template102Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(115, 115, 115))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template102Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(237, 237, 237))))
        );
        Template102Layout.setVerticalGroup(
            Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template102Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(Address, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33))
                .addGap(18, 18, 18)
                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(jLabel36))
                    .addComponent(jLabel37))
                .addGap(18, 18, 18)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39))
                .addGap(26, 26, 26)
                .addComponent(jLabel40)
                .addGap(42, 42, 42)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 712, Short.MAX_VALUE)
                .addComponent(jLabel42)
                .addGap(108, 108, 108))
        );

        Template.add(Template102, "card3");

        tmp3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tmp3.setText("Template 103:");
        tmp3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel103.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel103.setText("jLabel103");

        tmp4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tmp4.setText("Template 104:");
        tmp4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel104.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel104.setText("jLabel104");

        jLabel105.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel105.setText("jLabel105");

        tmp5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tmp5.setText("Template 105:");
        tmp5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout SelectRowLayout = new javax.swing.GroupLayout(SelectRow);
        SelectRow.setLayout(SelectRowLayout);
        SelectRowLayout.setHorizontalGroup(
            SelectRowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectRowLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SelectRowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tmp2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(tmp1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tmp5, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tmp4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tmp3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 717, Short.MAX_VALUE)
                .addComponent(Template, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );
        SelectRowLayout.setVerticalGroup(
            SelectRowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectRowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SelectRowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Template, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SelectRowLayout.createSequentialGroup()
                        .addGroup(SelectRowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addComponent(tmp1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tmp2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel102)
                        .addGap(18, 18, 18)
                        .addComponent(tmp3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel103)
                        .addGap(18, 18, 18)
                        .addComponent(tmp4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel104)
                        .addGap(26, 26, 26)
                        .addComponent(tmp5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel105)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        ReturnSaleBody.add(SelectRow, "card3");

        SelectionArea.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receipt Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(204, 204, 204))); // NOI18N

        company.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        qtyamounttobereturned.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        qtyamounttobereturned.setText("Footer Message:");

        savebutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-save-32.png"))); // NOI18N
        savebutton.setText("Save");
        savebutton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        savebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebuttonActionPerformed(evt);
            }
        });

        prdnametobereturned.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        prdnametobereturned.setText("Company Name: ");

        jLabel94.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel94.setText("Address");

        footermsg.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        footermsg.setText("Thank You!");

        AddressTexts.setColumns(10);
        AddressTexts.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        AddressTexts.setLineWrap(true);
        AddressTexts.setRows(5);
        AddressTexts.setText("Solar MicroSystems Pte Ltd, 33Ubi Ave 3 #07-50, VERTEX (Tower A) singapore 408868 Tel: + +65 6336 0300, UEN: 123456789A GST No: 123456789A, Opening Hours: 10am to 10pm (Daily) Refund no: SB1005 Date: 2/4/17");
        jScrollPane1.setViewportView(AddressTexts);

        javax.swing.GroupLayout SelectionAreaLayout = new javax.swing.GroupLayout(SelectionArea);
        SelectionArea.setLayout(SelectionAreaLayout);
        SelectionAreaLayout.setHorizontalGroup(
            SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectionAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(qtyamounttobereturned)
                    .addComponent(jLabel94)
                    .addComponent(prdnametobereturned, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SelectionAreaLayout.createSequentialGroup()
                        .addGroup(SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(footermsg, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(543, Short.MAX_VALUE))
                    .addGroup(SelectionAreaLayout.createSequentialGroup()
                        .addGroup(SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(savebutton)
                            .addComponent(company, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        SelectionAreaLayout.setVerticalGroup(
            SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectionAreaLayout.createSequentialGroup()
                .addGroup(SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SelectionAreaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(prdnametobereturned)
                            .addComponent(company, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addComponent(qtyamounttobereturned))
                    .addGroup(SelectionAreaLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(footermsg, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(SelectionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SelectionAreaLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel94))
                    .addGroup(SelectionAreaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(savebutton)
                .addContainerGap(167, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout SelectAmountLayout = new javax.swing.GroupLayout(SelectAmount);
        SelectAmount.setLayout(SelectAmountLayout);
        SelectAmountLayout.setHorizontalGroup(
            SelectAmountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectAmountLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(SelectionArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(591, Short.MAX_VALUE))
        );
        SelectAmountLayout.setVerticalGroup(
            SelectAmountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectAmountLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(SelectionArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(945, Short.MAX_VALUE))
        );

        ReturnSaleBody.add(SelectAmount, "card4");

        viewReceipt.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "View Receipt\n", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(204, 204, 204))); // NOI18N
        viewReceipt.setForeground(new java.awt.Color(204, 204, 204));
        viewReceipt.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout EmptyTemplate1Layout = new javax.swing.GroupLayout(EmptyTemplate1);
        EmptyTemplate1.setLayout(EmptyTemplate1Layout);
        EmptyTemplate1Layout.setHorizontalGroup(
            EmptyTemplate1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 667, Short.MAX_VALUE)
        );
        EmptyTemplate1Layout.setVerticalGroup(
            EmptyTemplate1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1375, Short.MAX_VALUE)
        );

        viewReceipt.add(EmptyTemplate1, "card7");

        jLabel95.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(51, 0, 255));
        jLabel95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-dolphin-48.png"))); // NOI18N
        jLabel95.setText("Dolphin Printers");

        Address3.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        Address3.setText("Solar MicroSystems Pte Ltd, 33Ubi Ave 3 #07-50, VERTEX (Tower A) singapore 408868 Tel: + +65 6336 0300, UEN: 123456789A GST No: 123456789A, Opening Hours: 10am to 10pm (Daily) Refund no: SB1005 Date: 2/4/17");
        Address3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Barcode2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        jLabel96.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel96.setText("Qty");

        jLabel97.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel97.setText("1");

        jLabel98.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel98.setText("2");

        jLabel99.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel99.setText("P104 MB Cable ");

        jLabel100.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel100.setText("MB Cable 101");

        jLabel106.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel106.setText("Description");

        jLabel107.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel107.setText("Amount");

        jLabel108.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel108.setText("172.26");

        jLabel109.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel109.setText("96.30");

        jLabel110.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel110.setText("245.37");

        jLabel111.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel111.setText("B108 - MB Board ");

        jLabel112.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel112.setText("3");

        jSeparator13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel113.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel113.setText("Subtotal");

        jLabel114.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel114.setText("Discount");

        jLabel115.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel115.setText("Total");

        jLabel116.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel116.setText("100.26");

        jLabel117.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel117.setText("85.96");

        jLabel118.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel118.setText("12%");

        jLabel119.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel119.setText("105.6");

        javax.swing.GroupLayout Template106Layout = new javax.swing.GroupLayout(Template106);
        Template106.setLayout(Template106Layout);
        Template106Layout.setHorizontalGroup(
            Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template106Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template106Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator13)
                            .addGroup(Template106Layout.createSequentialGroup()
                                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel96))
                                    .addComponent(jLabel98)
                                    .addComponent(jLabel112))
                                .addGap(36, 36, 36)
                                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel106)
                                    .addComponent(jLabel100)
                                    .addComponent(jLabel99)
                                    .addComponent(jLabel111))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel108, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel110, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel107, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel109, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(112, 112, 112))))
                    .addComponent(jSeparator14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template106Layout.createSequentialGroup()
                .addContainerGap(179, Short.MAX_VALUE)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template106Layout.createSequentialGroup()
                        .addComponent(jLabel95)
                        .addGap(163, 163, 163))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template106Layout.createSequentialGroup()
                        .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Address3, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Template106Layout.createSequentialGroup()
                                .addComponent(Barcode2)
                                .addGap(76, 76, 76)))
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template106Layout.createSequentialGroup()
                        .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel115)
                            .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel114)
                                .addComponent(jLabel113))
                            .addComponent(jLabel118))
                        .addGap(53, 53, 53)
                        .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel116)
                            .addComponent(jLabel117)
                            .addComponent(jLabel119))
                        .addGap(120, 120, 120))))
        );
        Template106Layout.setVerticalGroup(
            Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template106Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel95)
                .addGap(18, 18, 18)
                .addComponent(Address3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Barcode2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(jLabel106)
                    .addComponent(jLabel107))
                .addGap(18, 18, 18)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel100)
                    .addComponent(jLabel108))
                .addGap(18, 18, 18)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel98)
                    .addComponent(jLabel99)
                    .addComponent(jLabel109))
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template106Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel112)
                            .addComponent(jLabel111)))
                    .addGroup(Template106Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel110)))
                .addGap(25, 25, 25)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel113)
                    .addComponent(jLabel116))
                .addGap(18, 18, 18)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel114)
                    .addComponent(jLabel117))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel118)
                .addGap(2, 2, 2)
                .addGroup(Template106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel115)
                    .addComponent(jLabel119))
                .addContainerGap(680, Short.MAX_VALUE))
        );

        viewReceipt.add(Template106, "card4");

        jLabel150.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel150.setText("Premium Template");

        jLabel151.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-password-32.png"))); // NOI18N

        javax.swing.GroupLayout Template108Layout = new javax.swing.GroupLayout(Template108);
        Template108.setLayout(Template108Layout);
        Template108Layout.setHorizontalGroup(
            Template108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template108Layout.createSequentialGroup()
                .addContainerGap(331, Short.MAX_VALUE)
                .addGroup(Template108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template108Layout.createSequentialGroup()
                        .addComponent(jLabel151)
                        .addGap(246, 246, 246))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template108Layout.createSequentialGroup()
                        .addComponent(jLabel150)
                        .addGap(187, 187, 187))))
        );
        Template108Layout.setVerticalGroup(
            Template108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template108Layout.createSequentialGroup()
                .addGap(254, 254, 254)
                .addComponent(jLabel151)
                .addGap(29, 29, 29)
                .addComponent(jLabel150)
                .addContainerGap(1035, Short.MAX_VALUE))
        );

        viewReceipt.add(Template108, "card6");

        jLabel152.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel152.setText("RECEIPT");

        jSeparatorbottom1.setBackground(new java.awt.Color(204, 204, 204));
        jSeparatorbottom1.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator17.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator17.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator18.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator18.setForeground(new java.awt.Color(204, 204, 204));

        Terminal2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        Terminal2.setText("CUSTOMER");

        terminalval1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        terminalval1.setText("Supermarket");

        DateLabel1.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        DateLabel1.setText("DATE: ");

        todays1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        todays1.setText("today");

        Terminal3.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        Terminal3.setText("TERMINAL");

        Customernamelabel1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        Customernamelabel1.setText("customer");

        jSeparator19.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator19.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator20.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator20.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator21.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator21.setForeground(new java.awt.Color(204, 204, 204));

        jLabel153.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel153.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        jLabel154.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel154.setText("TOTAL");

        jLabel155.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel155.setText("THANK YOU");

        jLabel156.setText("179,000");

        jLabel157.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel157.setText("1. Product");

        jLabel158.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel158.setText("2. Product");

        jLabel159.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel159.setText("3. Product");

        jLabel160.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel160.setText("4. Product");

        jLabel161.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel161.setText("5. Product");

        jLabel162.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel162.setText("50,000");

        jLabel163.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel163.setText("12,000");

        jLabel164.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel164.setText("25,000");

        jLabel165.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel165.setText("43,000");

        jLabel166.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel166.setText("39,000");

        jSeparator22.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator22.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout Template109Layout = new javax.swing.GroupLayout(Template109);
        Template109.setLayout(Template109Layout);
        Template109Layout.setHorizontalGroup(
            Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template109Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparatorbottom1)
                    .addComponent(jSeparator17))
                .addContainerGap())
            .addGroup(Template109Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator19)
                .addContainerGap())
            .addGroup(Template109Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator21)
                .addContainerGap())
            .addGroup(Template109Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel154)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel156)
                .addGap(77, 77, 77))
            .addGroup(Template109Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel159, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel161, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel163)
                    .addComponent(jLabel162)
                    .addComponent(jLabel166)
                    .addComponent(jLabel164)
                    .addComponent(jLabel165))
                .addGap(110, 110, 110))
            .addGroup(Template109Layout.createSequentialGroup()
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template109Layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(jLabel152, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Template109Layout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addComponent(jLabel155))
                    .addGroup(Template109Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jLabel153, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Template109Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Terminal2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Customernamelabel1))
                    .addGroup(Template109Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Terminal3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(terminalval1)))
                .addContainerGap(242, Short.MAX_VALUE))
            .addGroup(Template109Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(DateLabel1)
                .addGap(18, 18, 18)
                .addComponent(todays1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator22)
                .addContainerGap())
            .addGroup(Template109Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator18)
                .addContainerGap())
            .addGroup(Template109Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator20)
                .addContainerGap())
        );
        Template109Layout.setVerticalGroup(
            Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template109Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel152, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template109Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jSeparatorbottom1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(terminalval1)
                            .addComponent(Terminal3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Template109Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DateLabel1)
                            .addComponent(todays1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Terminal2)
                    .addComponent(Customernamelabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel157)
                    .addComponent(jLabel162))
                .addGap(18, 18, 18)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel158)
                    .addComponent(jLabel163))
                .addGap(27, 27, 27)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel159)
                    .addComponent(jLabel164))
                .addGap(28, 28, 28)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel160)
                    .addComponent(jLabel165))
                .addGap(28, 28, 28)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel161)
                    .addComponent(jLabel166))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 592, Short.MAX_VALUE)
                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(Template109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel154)
                    .addComponent(jLabel156))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(jLabel153, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel155)
                .addGap(26, 26, 26))
        );

        viewReceipt.add(Template109, "card2");

        Address5.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        Address5.setText("Solar MicroSystems Pte Ltd, 33Ubi Ave 3 #07-50, VERTEX (Tower A) singapore 408868 Tel: + +65 6336 0300, UEN: 123456789A GST No: 123456789A, Opening Hours: 10am to 10pm (Daily) Refund no: SB1005 Date: 2/4/17");
        Address5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel167.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel167.setText("Qty");

        jLabel168.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel168.setText("Description");

        jLabel169.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel169.setText("Amount");

        jLabel170.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel170.setText("1");

        jLabel171.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel171.setText("2");

        jLabel172.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel172.setText("3");

        jLabel173.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel173.setText("MB Cable 101");

        jLabel174.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel174.setText("172.26");

        jLabel175.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel175.setText("P104 MB Cable ");

        jLabel176.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel176.setText("96.30");

        jLabel177.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel177.setText("B108 - MB Board ");

        jLabel178.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel178.setText("245.37");

        jLabel179.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel179.setText("Total Tax Inclusive");

        jLabel180.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel180.setText("513.94");

        jLabel181.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel181.setText("Paid By Cash");

        jLabel182.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel182.setText("Thank You!");

        jLabel183.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        javax.swing.GroupLayout Template110Layout = new javax.swing.GroupLayout(Template110);
        Template110.setLayout(Template110Layout);
        Template110Layout.setHorizontalGroup(
            Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template110Layout.createSequentialGroup()
                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template110Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator24)
                            .addGroup(Template110Layout.createSequentialGroup()
                                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel170, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel167, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel171)
                                    .addComponent(jLabel172))
                                .addGap(36, 36, 36)
                                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(Template110Layout.createSequentialGroup()
                                        .addComponent(jLabel168)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel169))
                                    .addGroup(Template110Layout.createSequentialGroup()
                                        .addComponent(jLabel173)
                                        .addGap(201, 201, 201)
                                        .addComponent(jLabel174))
                                    .addGroup(Template110Layout.createSequentialGroup()
                                        .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel175)
                                            .addComponent(jLabel177))
                                        .addGap(173, 173, 173)
                                        .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel178)
                                            .addComponent(jLabel176)))))))
                    .addComponent(jSeparator23, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Template110Layout.createSequentialGroup()
                        .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(Template110Layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(Address5, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Template110Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel181)
                                    .addGroup(Template110Layout.createSequentialGroup()
                                        .addComponent(jLabel179)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel180)
                                        .addGap(95, 95, 95)))))
                        .addGap(0, 117, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template110Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template110Layout.createSequentialGroup()
                        .addComponent(jLabel183)
                        .addGap(115, 115, 115))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template110Layout.createSequentialGroup()
                        .addComponent(jLabel182)
                        .addGap(270, 270, 270))))
        );
        Template110Layout.setVerticalGroup(
            Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template110Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(Address5, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel167)
                    .addComponent(jLabel168)
                    .addComponent(jLabel169))
                .addGap(18, 18, 18)
                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel170, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel173)
                    .addComponent(jLabel174))
                .addGap(18, 18, 18)
                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel171)
                    .addComponent(jLabel175)
                    .addComponent(jLabel176))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel172)
                        .addComponent(jLabel177))
                    .addComponent(jLabel178))
                .addGap(18, 18, 18)
                .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Template110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel179)
                    .addComponent(jLabel180))
                .addGap(26, 26, 26)
                .addComponent(jLabel181)
                .addGap(48, 48, 48)
                .addComponent(jLabel182)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 581, Short.MAX_VALUE)
                .addComponent(jLabel183)
                .addGap(108, 108, 108))
        );

        viewReceipt.add(Template110, "card3");

        jLabel120.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(51, 0, 255));
        jLabel120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-dolphin-48.png"))); // NOI18N
        jLabel120.setText("Dolphin Printers");

        Address4.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        Address4.setText("Solar MicroSystems Pte Ltd, 33Ubi Ave 3 #07-50, VERTEX (Tower A) singapore 408868 Tel: + +65 6336 0300, UEN: 123456789A GST No: 123456789A, Opening Hours: 10am to 10pm (Daily) Refund no: SB1005 Date: 2/4/17");
        Address4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Barcode3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/barcode/barcode12345.png"))); // NOI18N

        jLabel121.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel121.setText("Qty");

        jLabel122.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel122.setText("Description");

        jLabel123.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel123.setText("Amount");

        jLabel124.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel124.setText("1");

        jLabel125.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel125.setText("2");

        jLabel126.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel126.setText("MB Cable 101");

        jLabel127.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel127.setText("P104 MB Cable ");

        jLabel128.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel128.setText("172.26");

        jLabel129.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel129.setText("96.30");

        jLabel130.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel130.setText("3");

        jLabel131.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel131.setText("B108 - MB Board ");

        jLabel132.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel132.setText("245.37");

        jSeparator15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel133.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel133.setText("100.26");

        jLabel134.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel134.setText("Subtotal");

        jLabel135.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel135.setText("Discount");

        jLabel136.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel136.setText("85.96");

        jLabel137.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel137.setText("12%");

        jLabel138.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel138.setText("Total");

        jLabel139.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel139.setText("105.6");

        jSeparator16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel140.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel140.setText("Order #:");

        jLabel141.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel141.setText("Sold To:");

        jLabel142.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel142.setText("Order Date:");

        jLabel143.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel143.setText("Sales Type:");

        jLabel144.setText("01908383892938");

        jLabel145.setText("WASTER PRINTERS");

        jLabel146.setText("jLabel88");

        jLabel147.setText("jLabel89");

        jLabel148.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel148.setText("Sales Person:");

        jLabel149.setText("Closed");

        javax.swing.GroupLayout Template107Layout = new javax.swing.GroupLayout(Template107);
        Template107.setLayout(Template107Layout);
        Template107Layout.setHorizontalGroup(
            Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator16)
            .addGroup(Template107Layout.createSequentialGroup()
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                        .addGap(163, 493, Short.MAX_VALUE)
                        .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel133)
                            .addComponent(jLabel136)
                            .addComponent(jLabel139))
                        .addGap(114, 114, 114))
                    .addGroup(Template107Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator15)
                            .addGroup(Template107Layout.createSequentialGroup()
                                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel124, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel121))
                                    .addComponent(jLabel130)
                                    .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Template107Layout.createSequentialGroup()
                                        .addComponent(jLabel127)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel129))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                                        .addComponent(jLabel126)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel128))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                                        .addComponent(jLabel122)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel123))
                                    .addGroup(Template107Layout.createSequentialGroup()
                                        .addComponent(jLabel131)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel132)))
                                .addGap(113, 113, 113))))
                    .addGroup(Template107Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel140)
                            .addComponent(jLabel141)
                            .addComponent(jLabel142)
                            .addComponent(jLabel148)
                            .addComponent(jLabel143))
                        .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(Template107Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel149))
                            .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(Template107Layout.createSequentialGroup()
                                    .addGap(214, 214, 214)
                                    .addComponent(jLabel144))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel146)
                                        .addComponent(jLabel145)
                                        .addComponent(jLabel147)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                        .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel137)
                            .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel135)
                                .addComponent(jLabel134))
                            .addComponent(jLabel138))
                        .addGap(216, 216, 216))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                        .addComponent(Barcode3)
                        .addGap(181, 181, 181))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                        .addComponent(Address4, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Template107Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel120)
                .addGap(203, 203, 203))
        );
        Template107Layout.setVerticalGroup(
            Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Template107Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel120)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Address4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Barcode3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel140)
                    .addComponent(jLabel144))
                .addGap(18, 18, 18)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel141)
                    .addComponent(jLabel145))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel142)
                    .addComponent(jLabel146))
                .addGap(23, 23, 23)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel148)
                    .addComponent(jLabel147))
                .addGap(19, 19, 19)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel149)
                    .addComponent(jLabel143))
                .addGap(18, 18, 18)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel121)
                    .addComponent(jLabel122)
                    .addComponent(jLabel123))
                .addGap(18, 18, 18)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel126)
                    .addComponent(jLabel128))
                .addGap(18, 18, 18)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel127)
                    .addComponent(jLabel129)
                    .addComponent(jLabel125))
                .addGap(18, 18, 18)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel130)
                    .addComponent(jLabel131)
                    .addComponent(jLabel132))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel134)
                    .addComponent(jLabel133))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel135)
                    .addComponent(jLabel136))
                .addGroup(Template107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Template107Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel137)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel138))
                    .addGroup(Template107Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel139)))
                .addContainerGap(512, Short.MAX_VALUE))
        );

        viewReceipt.add(Template107, "card5");

        javax.swing.GroupLayout ReturnMethodLayout = new javax.swing.GroupLayout(ReturnMethod);
        ReturnMethod.setLayout(ReturnMethodLayout);
        ReturnMethodLayout.setHorizontalGroup(
            ReturnMethodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReturnMethodLayout.createSequentialGroup()
                .addGap(580, 580, 580)
                .addComponent(viewReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(591, Short.MAX_VALUE))
        );
        ReturnMethodLayout.setVerticalGroup(
            ReturnMethodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReturnMethodLayout.createSequentialGroup()
                .addComponent(viewReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 1407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 137, Short.MAX_VALUE))
        );

        ReturnSaleBody.add(ReturnMethod, "card5");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-document-delivery-16.png"))); // NOI18N
        jLabel2.setText("Select Delivery Company");

        jLabel3.setText("Cost");

        jRadioButton5.setText("Free - (No cost)");

        javax.swing.GroupLayout SelectShippingLayout = new javax.swing.GroupLayout(SelectShipping);
        SelectShipping.setLayout(SelectShippingLayout);
        SelectShippingLayout.setHorizontalGroup(
            SelectShippingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SelectShippingLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1476, Short.MAX_VALUE)
                .addGroup(SelectShippingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton5)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );
        SelectShippingLayout.setVerticalGroup(
            SelectShippingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectShippingLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(SelectShippingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton5)
                .addContainerGap(1422, Short.MAX_VALUE))
        );

        ReturnSaleBody.add(SelectShipping, "card6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ReturnSaleBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReturnSaleBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void itemStateChanged(ItemEvent e) {
        JCheckBox selectedCheckBox = (JCheckBox) e.getItem();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // Deselect all other checkboxes except the selected one
            if (selectedCheckBox != tmp1) {
                tmp1.setSelected(false);
            }
            if (selectedCheckBox != tmp2) {
                tmp2.setSelected(false);
            }
            if (selectedCheckBox != tmp3) {
                tmp3.setSelected(false);
            }
            if (selectedCheckBox != tmp4) {
                tmp4.setSelected(false);
            }
            if (selectedCheckBox != tmp5) {
                tmp5.setSelected(false);
            }
        }

        // Show the relevant template based on the selected checkbox
        template = selectedCheckBox.getText().trim();
        switch (selectedCheckBox.getText().trim()) {
            case "Template 101:":
                showTemplate101();
                break;
            case "Template 102:":
                showTemplate102();
                break;
            case "Template 103:":
                showTemplate103();
                break;
            case "Template 104:":
                showTemplate104();
                break;
            case "Template 105:":
                showTemplate105();
                break;
            default:
                break;
        }
    }

//    Templates
    private void showTemplate101() {
        // Clear the Template JPanel
        Template.removeAll();
        Template.add(Template101);
        Template.repaint();
        Template.revalidate();
    }

    private void showTemplate102() {
        // Clear the Template JPanel
        // Clear the Template JPanel
        Template.removeAll();
        Template.add(Template102);
        Template.repaint();
        Template.revalidate();
    }

    private void showTemplate103() {
        // Clear the Template JPanel
        Template.removeAll();
        Template.add(Template103);
        Template.repaint();
        Template.revalidate();
    }

    private void showTemplate104() {
        // Clear the Template JPanel
        Template.removeAll();
        Template.add(Template104);
        Template.repaint();
        Template.revalidate();
    }

    private void showTemplate105() {
        // Clear the Template JPanel
        Template.removeAll();
        Template.add(Template105);
        Template.repaint();
        Template.revalidate();
    }

    private void showTemplate0() {
        // Clear the Template JPanel
        Template.removeAll();
        Template.add(EmptyTemplate);
        Template.repaint();
        Template.revalidate();
    }

//    Templates
    private void showPrinout101() {
        // Clear the Template JPanel
        viewReceipt.removeAll();
        viewReceipt.add(Template109);
        viewReceipt.repaint();
        viewReceipt.revalidate();
    }

    private void showPrintout102() {
        // Clear the viewReceipt JPanel
        // Clear the viewReceipt JPanel
        viewReceipt.removeAll();
        viewReceipt.add(Template110);
        viewReceipt.repaint();
        viewReceipt.revalidate();
    }

    private void showPrintout103() {
        // Clear the viewReceipt JPanel
        System.out.println("I have been summoned++");
        viewReceipt.removeAll();
        viewReceipt.add(Template106);
        viewReceipt.repaint();
        viewReceipt.revalidate();
    }

    private void showPrintout104() {
        // Clear the viewReceipt JPanel
        viewReceipt.removeAll();
        viewReceipt.add(Template107);
        viewReceipt.repaint();
        viewReceipt.revalidate();
    }

    private void showPrintout105() {
        // Clear the viewReceipt JPanel
        viewReceipt.removeAll();
        viewReceipt.add(Template110);
        viewReceipt.repaint();
        viewReceipt.revalidate();
    }

    private void showPrintout0() {
        // Clear the viewReceipt JPanel
        viewReceipt.removeAll();
        viewReceipt.add(EmptyTemplate1);
        viewReceipt.repaint();
        viewReceipt.revalidate();
    }
//    End of templates
    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        // Closing
         this.dispose();
    }//GEN-LAST:event_cancelMouseClicked

    private void frontMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_frontMouseClicked
        // TODO add your handling code here:
         switch (clickcount) {
        case 0:
            SelectionMeans.setVisible(false);
            SelectRow.setVisible(true);
            front.setText("Next");
            back.setEnabled(true);
            break;
        case 1:
            SelectRow.setVisible(false);
            SelectAmount.setVisible(true);
            break;
        case 2:
            SelectAmount.setVisible(false);
            ReturnMethod.setVisible(true);
            try {
                loadPrinterSettings();
            } catch (SQLException | ClassNotFoundException | ParseException ex) {
                Logger.getLogger(PrinterSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
        case 3:
            ReturnMethod.setVisible(false);
            SelectShipping.setVisible(true);
            front.setText("Finish");
            break;
        default:
            SelectionMeans.setVisible(true);
            SelectRow.setVisible(false);
            SelectAmount.setVisible(false);
            this.clickcount = 0;
            front.setText("Next");
            break;
    }

    // Increment clickcount if it's less than 3
    if (this.clickcount < 3) {
        this.clickcount += 1;
    }

    System.out.println(clickcount);
    }//GEN-LAST:event_frontMouseClicked

    private void backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backMouseClicked
        switch (clickcount) {
            case 1:
                SelectionMeans.setVisible(true);
                SelectRow.setVisible(false);
                break;
            case 2:
                SelectRow.setVisible(true);
                SelectAmount.setVisible(false);
                break;
            default:
                SelectionMeans.setVisible(true);
                SelectRow.setVisible(false);
                SelectAmount.setVisible(false);
                this.clickcount = 0;
        }
        if (this.clickcount < 3) {
            if (this.clickcount > 0) {
                this.clickcount -= 1;
            }
        }
        System.out.println(clickcount);
    }//GEN-LAST:event_backMouseClicked

    private void savebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebuttonActionPerformed
        // Saving all data.
        boolean print = PrintAutomatically.isSelected();
        String printer = (String) PrinterComboBox.getSelectedItem();
        String compan = company.getText();
        String footer = footermsg.getText();
        String AddressTex = AddressTexts.getText();
        try {
            saveReceiptDetails(print, printer, compan, footer, AddressTex, template);
        } catch (SQLException ex) {
            Logger.getLogger(PrinterSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_savebuttonActionPerformed

    /**
     * @param args the command line arguments
     */
    /**
     * Function to all the sales status
     *
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
     */
    public void printerSelector() {
        List<String> printers = getAvailablePrinters();
        System.out.println("Available Printers:");
        for (String printer : printers) {
            PrinterComboBox.addItem(printer);
        }
    }

    public final void loadPrinterSettings() throws SQLException,
            ClassNotFoundException, ParseException {
            System.out.println("You are at the right place");
            PrinterSettings = getLastReceiptSettings();
            lasttReceipts = getLastReceiptDetails();
            // Print out the details
            System.out.println("Receipt Details:");
            int Receipt_ID = lasttReceipts.getReceiptId();
            //generate bar code
            System.out.println("Sales ID: " + lasttReceipts.getSalesId());
            String created_date = lasttReceipts.getCreatedDate();
            String Company = lasttReceipts.getCompanyName();
            String Footer = lasttReceipts.getFooterMessage();
            String addres = lasttReceipts.getAddress();
            System.out.println("Created By: " + lasttReceipts.getCreatedBy());
            String Sold_To  = lasttReceipts.getSoldTo();
            
            String barcode = lasttReceipts.getBarcode();
            System.out.println("ee0000"+barcode);
            generateCode(barcode);
            System.out.println("hre++++++++++++"+barcode);
            
            //Address Labels
            Address4.setText(addres);
            Address3.setText(addres);
            Address5.setText(addres);
            
            //Wrapping texts
             addres = addres.replaceAll(",\\s*", ",<br>");

            // Set the text of the JLabel with HTML formatting
            Address3.setText("<html><div style='text-align: center;'>" + addres + "</div></html>");
            Address4.setText("<html><div style='text-align: center;'>" + addres + "</div></html>");
            Address5.setText("<html><div style='text-align: center;'>" + addres + "</div></html>");
        
            Address3.setPreferredSize(new Dimension(300, Address.getPreferredSize().height));
            Address4.setPreferredSize(new Dimension(300, Address.getPreferredSize().height));
            Address5.setPreferredSize(new Dimension(300, Address.getPreferredSize().height));
        
            
            //Company Labels
            jLabel95.setText(Company);
            terminalval1.setText(Company);
            jLabel120.setText(Company);
            
            //Dates
            todays1.setText("" + created_date);
            
            //Customers Label
            Customernamelabel1.setText(Sold_To);
            
            //Thank you message
            jLabel182.setText(Footer);
            jLabel155.setText(Footer);
            String barcodename = barcode + ".png";
            String systemPath = OS.getSystemPath();
            String path = systemPath + "resources/usergenerated/barcodes/" + barcodename;

            // Create an ImageIcon from the image file
            ImageIcon icon = new ImageIcon(path);

            // Set the icon to the JLabel
            Barcode2.setIcon(icon);
            Barcode3.setIcon(icon);

        // Print out the settings
            System.out.println("\nReceipt Settings:");
            System.out.println("Created By: " + PrinterSettings.getCreatedBy());
            System.out.println("Printer Name: " + PrinterSettings.getPrinterName());
            System.out.println("Automatically: " + PrinterSettings.isAutomatically());
            String selectedtemplate = PrinterSettings.getTemplate();
        
         System.out.println("++++++++++++++++: " + selectedtemplate);
        switch (selectedtemplate) {
            case "Template 101:":
                showPrinout101();
                break;
            case "Template 102:":
                showPrintout102();
                break;
            case "Template 103:":
                showPrintout103();
                break;
            case "Template 104:":
                showPrintout104();
                break;
            case "Template 105:":
                showPrintout105();
                break;
            default:
                break;
         }
        
    } 


/*end of it*/
public static void launch() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        System.setProperty("sun.java2d.dpiaware", "false");
        // Retrieve the current user role
        FlatGitHubIJTheme.setup();
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new PrinterSettings().setVisible(true);

} catch (SQLException | ClassNotFoundException | ParseException ex) {
                Logger.getLogger(PrinterSettings.class  

.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Address;
    private javax.swing.JLabel Address1;
    private javax.swing.JLabel Address2;
    private javax.swing.JLabel Address3;
    private javax.swing.JLabel Address4;
    private javax.swing.JLabel Address5;
    private javax.swing.JTextArea AddressTexts;
    private javax.swing.JLabel Barcode;
    private javax.swing.JLabel Barcode1;
    private javax.swing.JLabel Barcode2;
    private javax.swing.JLabel Barcode3;
    private javax.swing.JLabel Customernamelabel;
    private javax.swing.JLabel Customernamelabel1;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JLabel DateLabel1;
    private javax.swing.JPanel EmptyTemplate;
    private javax.swing.JPanel EmptyTemplate1;
    private javax.swing.JPanel Header;
    private javax.swing.JCheckBox PrintAutomatically;
    private javax.swing.JComboBox<String> PrinterComboBox;
    private javax.swing.JPanel ReturnMethod;
    private javax.swing.JPanel ReturnSaleBody;
    private javax.swing.JPanel SelectAmount;
    private javax.swing.JPanel SelectRow;
    private javax.swing.JPanel SelectShipping;
    private javax.swing.JPanel SelectionArea;
    private javax.swing.JPanel SelectionMeans;
    private javax.swing.JPanel Template;
    private javax.swing.JPanel Template101;
    private javax.swing.JPanel Template102;
    private javax.swing.JPanel Template103;
    private javax.swing.JPanel Template104;
    private javax.swing.JPanel Template105;
    private javax.swing.JPanel Template106;
    private javax.swing.JPanel Template107;
    private javax.swing.JPanel Template108;
    private javax.swing.JPanel Template109;
    private javax.swing.JPanel Template110;
    private javax.swing.JLabel Terminal;
    private javax.swing.JLabel Terminal1;
    private javax.swing.JLabel Terminal2;
    private javax.swing.JLabel Terminal3;
    private javax.swing.JLabel back;
    private javax.swing.JLabel cancel;
    private javax.swing.JTextField company;
    private javax.swing.JTextField footermsg;
    private javax.swing.JLabel front;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSeparator jSeparatorbottom;
    private javax.swing.JSeparator jSeparatorbottom1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel prdnametobereturned;
    private javax.swing.JLabel qtyamounttobereturned;
    private javax.swing.JButton savebutton;
    private javax.swing.JLabel terminalval;
    private javax.swing.JLabel terminalval1;
    private javax.swing.JCheckBox tmp1;
    private javax.swing.JCheckBox tmp2;
    private javax.swing.JCheckBox tmp3;
    private javax.swing.JCheckBox tmp4;
    private javax.swing.JCheckBox tmp5;
    private javax.swing.JLabel todays;
    private javax.swing.JLabel todays1;
    private javax.swing.JPanel viewReceipt;
    // End of variables declaration//GEN-END:variables
}
