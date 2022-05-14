/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interface;

import static Authentication.Sessions.LoggedUser;
import Classes.AbstractClasses.DailyReport;
import Classes.Accounts;
import static Classes.Accounts.getTransfers;
import Classes.AbstractClasses.Brands;
import Classes.Categories;
import static Classes.Categories.listBrands;
import static Classes.Categories.listCategories;
import Classes.AbstractClasses.Category;
import Classes.Crudes;
import Classes.AbstractClasses.Order;
import Classes.Orders;
import static Classes.Orders.listOrders;
import Classes.AbstractClasses.Product;
import Classes.Products;
import static Classes.Products.listProductOnly;
import static Classes.Products.listProducts;
import static Classes.Products.listStockProducts;
import static Classes.Reports.DatedReport;
import static Classes.Reports.listDailyReport;
import static Classes.Reports.listMonthlyReport;
import static Classes.Reports.listWeeklyReport;
import Classes.AbstractClasses.Staff;
import Classes.Sales_Staffs;
import static Classes.Sales_Staffs.LoadStaffs;
import Classes.AbstractClasses.Stock;
import Classes.Stocks;
import static Classes.Stocks.listStocks;
import Classes.AbstractClasses.Transfer;
import Classes.Utilities.AudioFile;
import com.nkanabo.Tienda.Utilities.*;
import UserSettings.UserSettings;
import static com.nkanabo.Tienda.Utilities.unique;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Nkanabo
 */

public class UserInterface extends javax.swing.JFrame {
    
    ArrayList<Order> orderlist;
    String headers[]= {"Order Id","Product","Quantity", "List price", "Discount"};
    DefaultTableModel ordersModel;
    //Brands
    ArrayList<Brands> brandlist;
    String brandheaders[]= {"Brand Id","Brand Name"};
    DefaultTableModel brandsModel;
    //end of brands
    //Categories
    ArrayList<Category> categorylist;
    String categoryheader[] = {"category_id","category_name"};
    DefaultTableModel categoryModel;
    //End of categories
    
    //Products
    ArrayList<Product> productlist; //Products with 0 stock
    ArrayList<Product> stockproductlist; //products that has stock>0
    ArrayList<Product> productsonly; //products with no stock
    String productheader[] = {"Product","Brand","Category","Model","Expire Date","Price","Retail"};
    DefaultTableModel productModel;
    //End of products
    
        //Products
    ArrayList<Stock> stocklist;
    String stockheader[] = {"Product","Quantity"};
    DefaultTableModel stockModel;
    //End of products
    
    DefaultTableModel dailySalesModel;
    String dailyreportheader[] = {"Product Name","Retail Price","List Price","Profit","quantity","date"};
    ArrayList<DailyReport>  dailyreportlist;
    
    
    DefaultTableModel StaffsModel;
    String staffsheader[] = {"First Name","Last Name","Email","Role"};
    ArrayList<Staff>  staffslist;
    
    
    DefaultTableModel TransferModel;
    String transferheader[] = {"Amount","Date","Collected by"};
    ArrayList<Transfer>  transferedlist;
    
    DefaultTableModel weeklyModel;
    ArrayList<DailyReport> weeklyreportlist;
    
    DefaultTableModel monthlyModel;
    ArrayList<DailyReport> monthlyreportlist;
    
    DefaultTableModel datedModel;
    ArrayList<DailyReport> datedreportlist;
    

    public long quantity;
    int row,column;
    String[] allBrands;
    String[] allCats;
    String[] allProducts;
    String[] nonStock;
    String[] productonly;
    
   
     /**
     * Creates new form UserInterface
     */
    public UserInterface() throws URISyntaxException, ClassNotFoundException {
        initComponents();
        
        URL resource = getClass().getResource("/images/icons8.jpg");
        File file = Paths.get(resource.toURI()).toFile(); // return a file
        String filepath = Paths.get(resource.toURI()).toFile().getAbsolutePath();
        ImageIcon icon = new ImageIcon(filepath);
        setIconImage(icon.getImage());
        
        
        usernameLabel.setText(LoggedUser);
        this.pack();
        String today = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        todays.setText(String.valueOf(Calendar.getInstance().getTime()));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setExtendedState(UserPanel.MAXIMIZED_BOTH);
        orderlist = new ArrayList<>();
        ordersModel = new DefaultTableModel(headers,0);
        ordersTable.setModel(ordersModel);
        
        //Brands
        brandlist = new ArrayList<>();
        brandsModel = new DefaultTableModel(brandheaders,0);
        brandsTable.setModel(brandsModel);
        
        //End of Brands
        
        
        //Categories
        categorylist = new ArrayList<>();
        categoryModel = new DefaultTableModel(categoryheader,0);
        categoryTable.setModel(categoryModel);
        //End of categories
        
        //Categories
        productlist = new ArrayList<>();
        stockproductlist = new ArrayList<>();
        productsonly = new ArrayList<>();
        productModel = new DefaultTableModel(productheader,0);
        productsTable.setModel(productModel);
        //End of categories
        
       //Stocks
        stocklist = new ArrayList<>();
        stockModel = new DefaultTableModel(stockheader,0);
        stocksTable.setModel(stockModel);
        //End of categories
        
        dailyreportlist = new ArrayList<>();
        dailySalesModel = new DefaultTableModel(dailyreportheader,0);
        dailySalesTable.setModel(dailySalesModel);
        
        weeklyreportlist = new ArrayList<>();
        weeklyModel = new DefaultTableModel(dailyreportheader,0);
        weeklyreporttable.setModel(weeklyModel);
        
                
        monthlyreportlist = new ArrayList<>();
        monthlyModel = new DefaultTableModel(dailyreportheader,0);
        monthlyreporttable.setModel(monthlyModel);
        
             
        datedreportlist = new ArrayList<>();
        datedModel = new DefaultTableModel(dailyreportheader,0);
        datedreporttable.setModel(datedModel);
        
        //Staffs
        staffslist = new ArrayList<>();
        StaffsModel = new DefaultTableModel(staffsheader,0);
        staffTable.setModel(StaffsModel);
       
        //Transfers
        transferedlist = new ArrayList<>();
        TransferModel = new DefaultTableModel(transferheader,0);
        transferedCash.setModel(TransferModel);
        //Roles
        
        //Permissions
        //Transfers
        
       
        

        this.setLocationRelativeTo(null);
        try {
            loadJtableValues();
            loadBrandsJtableValues();
            LoadCategories();
            LoadProducts();
            LoadStockProducts();
            LoadProductsOnly();
            LoadStocks();
            } catch (SQLException ex) {
            Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        DefaultComboBoxModel model = new DefaultComboBoxModel(allBrands);
        brands.setModel(model);
        
        DefaultComboBoxModel catsmodel = new DefaultComboBoxModel(allCats);
        categories.setModel(catsmodel);

        DefaultComboBoxModel prodsmodel = new DefaultComboBoxModel(productonly);
        ProductsOnly.setModel(prodsmodel);
        
        List list = Arrays.asList(allProducts);
        List productions = Arrays.asList(productonly);
        
        AutoCompleteDecorator.decorate(brands);
        AutoCompleteDecorator.decorate(categories);
        AutoCompleteDecorator.decorate(ProductsOnly);
        AutoCompleteDecorator.decorate(SearchProductForm, list,true);
//      //AutoCompleteDecorator.decorate(ProductName, productions,true);
        
        datelabel.setVisible(false);
        backdate.setVisible(false);
        
      }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Orderbtn = new javax.swing.JButton();
        prdbtn = new javax.swing.JButton();
        stocksbtn = new javax.swing.JButton();
        CategoryBtn = new javax.swing.JButton();
        staffButton = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        usernameLabel = new javax.swing.JLabel();
        ParentLayout = new javax.swing.JPanel();
        OrderPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        qntlabel = new javax.swing.JLabel();
        quantityfield = new javax.swing.JTextField();
        oneBtn = new javax.swing.JButton();
        twoBtn = new javax.swing.JButton();
        threeBtn = new javax.swing.JButton();
        fourBtn = new javax.swing.JButton();
        fiveBtn = new javax.swing.JButton();
        sixBtn = new javax.swing.JButton();
        sevenBtn = new javax.swing.JButton();
        eightBtn = new javax.swing.JButton();
        zeroBtn1 = new javax.swing.JButton();
        zeroBtn = new javax.swing.JButton();
        nineBtn = new javax.swing.JButton();
        saleproductbtn = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        SearchProductForm = new javax.swing.JTextArea();
        todays = new javax.swing.JLabel();
        datelabel = new javax.swing.JLabel();
        backdate = new com.toedter.calendar.JCalendar();
        searchLabelIcon = new javax.swing.JLabel();
        deleteLabelIcon = new javax.swing.JLabel();
        eraserLabelIcon = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        saleproductbtnUpdate = new javax.swing.JButton();
        BrandPanel = new javax.swing.JPanel();
        insertBrand = new javax.swing.JButton();
        brandname = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        brandsTable = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        CategoryPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        categoryTable = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        categoryname = new javax.swing.JTextField();
        categorybtn = new javax.swing.JButton();
        ProductsPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        categorybtn1 = new javax.swing.JButton();
        model = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        productname = new javax.swing.JTextField();
        brands = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        categories = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        expiredate = new javax.swing.JTextField();
        retailprice = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        listprice = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        StaffPanel = new javax.swing.JPanel();
        categorybtn2 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        staffname = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        staffTable = new javax.swing.JTable();
        jLabel47 = new javax.swing.JLabel();
        surname = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        staff_role = new javax.swing.JComboBox<>();
        ReportsPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        dailySalesTable = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        tinvest = new javax.swing.JLabel();
        totalreturns = new javax.swing.JLabel();
        profit = new javax.swing.JLabel();
        ConfigPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        soundSetting = new javax.swing.JRadioButton();
        StockPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        stocksTable = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        insertBrand1 = new javax.swing.JButton();
        StockQuantity = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        ProductsOnly = new javax.swing.JComboBox<>();
        WeeklyReport = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        weeklyreporttable = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        tinvest1 = new javax.swing.JLabel();
        totalreturns1 = new javax.swing.JLabel();
        profit1 = new javax.swing.JLabel();
        MonthlyReport = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        monthlyreporttable = new javax.swing.JTable();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        tinvest2 = new javax.swing.JLabel();
        totalreturns2 = new javax.swing.JLabel();
        profit2 = new javax.swing.JLabel();
        DatedReport = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        datedreporttable = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        tinvest3 = new javax.swing.JLabel();
        totalreturns3 = new javax.swing.JLabel();
        profit3 = new javax.swing.JLabel();
        datedrep = new com.toedter.calendar.JCalendar();
        jButton12 = new javax.swing.JButton();
        CashTransfers = new javax.swing.JPanel();
        categorybtn3 = new javax.swing.JButton();
        transferamount = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        transferedCash = new javax.swing.JTable();
        jLabel53 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        MenuPanel.setBackground(new java.awt.Color(203, 196, 201));
        MenuPanel.setMaximumSize(null);

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel1.setText("Nkanabo Microsystems");

        jButton1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        jButton1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jButton1.setText("Brands");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Orderbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        Orderbtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        Orderbtn.setText("Orders");
        Orderbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Orderbtn.setContentAreaFilled(false);
        Orderbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Orderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderbtnActionPerformed(evt);
            }
        });

        prdbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        prdbtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        prdbtn.setText("Products");
        prdbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        prdbtn.setContentAreaFilled(false);
        prdbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        prdbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prdbtnActionPerformed(evt);
            }
        });

        stocksbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        stocksbtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        stocksbtn.setText("Stocks");
        stocksbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        stocksbtn.setContentAreaFilled(false);
        stocksbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stocksbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stocksbtnActionPerformed(evt);
            }
        });

        CategoryBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        CategoryBtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        CategoryBtn.setText("Categories");
        CategoryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        CategoryBtn.setContentAreaFilled(false);
        CategoryBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CategoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoryBtnActionPerformed(evt);
            }
        });

        staffButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        staffButton.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        staffButton.setText("Staff");
        staffButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        staffButton.setContentAreaFilled(false);
        staffButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        staffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffButtonActionPerformed(evt);
            }
        });

        jButton7.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        jButton7.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jButton7.setText("Reports");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        jButton8.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jButton8.setText("Config");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        usernameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        usernameLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(Orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(prdbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stocksbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(CategoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(staffButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        MenuPanelLayout.setVerticalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prdbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stocksbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staffButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CategoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(MenuPanel, java.awt.BorderLayout.PAGE_START);

        ParentLayout.setLayout(new java.awt.CardLayout());

        OrderPanel.setBackground(java.awt.Color.lightGray);

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel8.setText("Search");

        ordersTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        ordersTable.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        ordersTable.setModel(new javax.swing.table.DefaultTableModel(
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
        ordersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ordersTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ordersTable);

        qntlabel.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        qntlabel.setText("Quantity");

        quantityfield.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        quantityfield.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quantityfieldMouseExited(evt);
            }
        });
        quantityfield.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                quantityfieldKeyReleased(evt);
            }
        });

        oneBtn.setBackground(new java.awt.Color(102, 102, 102));
        oneBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        oneBtn.setForeground(new java.awt.Color(240, 240, 240));
        oneBtn.setText("1");
        oneBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        oneBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        oneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneBtnActionPerformed(evt);
            }
        });

        twoBtn.setBackground(new java.awt.Color(102, 102, 102));
        twoBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        twoBtn.setForeground(new java.awt.Color(240, 240, 240));
        twoBtn.setText("2");
        twoBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        twoBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        twoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twoBtnActionPerformed(evt);
            }
        });

        threeBtn.setBackground(new java.awt.Color(102, 102, 102));
        threeBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        threeBtn.setForeground(new java.awt.Color(240, 240, 240));
        threeBtn.setText("3");
        threeBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        threeBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        threeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threeBtnActionPerformed(evt);
            }
        });

        fourBtn.setBackground(new java.awt.Color(102, 102, 102));
        fourBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        fourBtn.setForeground(new java.awt.Color(240, 240, 240));
        fourBtn.setText("4");
        fourBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fourBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        fourBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fourBtnActionPerformed(evt);
            }
        });

        fiveBtn.setBackground(new java.awt.Color(102, 102, 102));
        fiveBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        fiveBtn.setForeground(new java.awt.Color(240, 240, 240));
        fiveBtn.setText("5");
        fiveBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fiveBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        fiveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveBtnActionPerformed(evt);
            }
        });

        sixBtn.setBackground(new java.awt.Color(102, 102, 102));
        sixBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        sixBtn.setForeground(new java.awt.Color(240, 240, 240));
        sixBtn.setText("6");
        sixBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sixBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        sixBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sixBtnActionPerformed(evt);
            }
        });

        sevenBtn.setBackground(new java.awt.Color(102, 102, 102));
        sevenBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        sevenBtn.setForeground(new java.awt.Color(240, 240, 240));
        sevenBtn.setText("7");
        sevenBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sevenBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        sevenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sevenBtnActionPerformed(evt);
            }
        });

        eightBtn.setBackground(new java.awt.Color(102, 102, 102));
        eightBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        eightBtn.setForeground(new java.awt.Color(240, 240, 240));
        eightBtn.setText("8");
        eightBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        eightBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        eightBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eightBtnActionPerformed(evt);
            }
        });

        zeroBtn1.setBackground(new java.awt.Color(102, 102, 102));
        zeroBtn1.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        zeroBtn1.setForeground(new java.awt.Color(240, 240, 240));
        zeroBtn1.setText("C");
        zeroBtn1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        zeroBtn1.setMaximumSize(new java.awt.Dimension(50, 49));
        zeroBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeroBtn1ActionPerformed(evt);
            }
        });

        zeroBtn.setBackground(new java.awt.Color(102, 102, 102));
        zeroBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        zeroBtn.setForeground(new java.awt.Color(240, 240, 240));
        zeroBtn.setText("0");
        zeroBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        zeroBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        zeroBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeroBtnActionPerformed(evt);
            }
        });

        nineBtn.setBackground(new java.awt.Color(102, 102, 102));
        nineBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        nineBtn.setForeground(new java.awt.Color(240, 240, 240));
        nineBtn.setText("9");
        nineBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nineBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        nineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nineBtnActionPerformed(evt);
            }
        });

        saleproductbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        saleproductbtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        saleproductbtn.setText("Sale Product");
        saleproductbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        saleproductbtn.setContentAreaFilled(false);
        saleproductbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saleproductbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleproductbtnActionPerformed(evt);
            }
        });

        SearchProductForm.setColumns(20);
        SearchProductForm.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        SearchProductForm.setRows(5);
        jScrollPane7.setViewportView(SearchProductForm);

        todays.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        todays.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        datelabel.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        datelabel.setText("Date");
        datelabel.setEnabled(false);

        searchLabelIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/searchicon.png"))); // NOI18N
        searchLabelIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchLabelIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchLabelIconMouseClicked(evt);
            }
        });

        deleteLabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/deleteicon.png"))); // NOI18N
        deleteLabelIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteLabelIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteLabelIconMouseClicked(evt);
            }
        });

        eraserLabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/erasericon.png"))); // NOI18N
        eraserLabelIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eraserLabelIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraserLabelIconMouseClicked(evt);
            }
        });

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/calendaricon.png"))); // NOI18N
        jLabel39.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel39MouseClicked(evt);
            }
        });

        saleproductbtnUpdate.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        saleproductbtnUpdate.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        saleproductbtnUpdate.setText("Update");
        saleproductbtnUpdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        saleproductbtnUpdate.setContentAreaFilled(false);
        saleproductbtnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saleproductbtnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleproductbtnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OrderPanelLayout = new javax.swing.GroupLayout(OrderPanel);
        OrderPanel.setLayout(OrderPanelLayout);
        OrderPanelLayout.setHorizontalGroup(
            OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrderPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(todays, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addComponent(searchLabelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteLabelIcon)
                        .addGap(18, 18, 18)
                        .addComponent(eraserLabelIcon)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel39))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1066, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63)
                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quantityfield, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(oneBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fiveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nineBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(zeroBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(twoBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sixBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(OrderPanelLayout.createSequentialGroup()
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(threeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sevenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fourBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(eightBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(zeroBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(backdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qntlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addComponent(saleproductbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saleproductbtnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        OrderPanelLayout.setVerticalGroup(
            OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrderPanelLayout.createSequentialGroup()
                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(todays, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(datelabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(searchLabelIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(eraserLabelIcon)
                                .addComponent(deleteLabelIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel39))
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addComponent(backdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(qntlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(quantityfield, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oneBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(twoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(threeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fourBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fiveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sixBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sevenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(eightBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zeroBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nineBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zeroBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saleproductbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saleproductbtnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        ParentLayout.add(OrderPanel, "card2");

        BrandPanel.setBackground(new java.awt.Color(186, 164, 164));

        insertBrand.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        insertBrand.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        insertBrand.setText("Add");
        insertBrand.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        insertBrand.setContentAreaFilled(false);
        insertBrand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        insertBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertBrandActionPerformed(evt);
            }
        });

        brandname.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        brandname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                brandnameKeyReleased(evt);
            }
        });

        brandsTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        brandsTable.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        brandsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(brandsTable);

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel11.setText("Add brands");

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel12.setText("Brands");

        jLabel2.setText("Brand Name");

        javax.swing.GroupLayout BrandPanelLayout = new javax.swing.GroupLayout(BrandPanel);
        BrandPanel.setLayout(BrandPanelLayout);
        BrandPanelLayout.setHorizontalGroup(
            BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BrandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BrandPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(insertBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(brandname, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        BrandPanelLayout.setVerticalGroup(
            BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BrandPanelLayout.createSequentialGroup()
                .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(brandname, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(BrandPanel, "card2");

        CategoryPanel.setBackground(new java.awt.Color(186, 164, 164));

        jLabel13.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel13.setText("Categories");

        categoryTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        categoryTable.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        categoryTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(categoryTable);

        jLabel14.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel14.setText("Add categories");

        jLabel7.setText("Category Name");

        categoryname.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        categoryname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                categorynameKeyReleased(evt);
            }
        });

        categorybtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        categorybtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        categorybtn.setText("Add");
        categorybtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        categorybtn.setContentAreaFilled(false);
        categorybtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        categorybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorybtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CategoryPanelLayout = new javax.swing.GroupLayout(CategoryPanel);
        CategoryPanel.setLayout(CategoryPanelLayout);
        CategoryPanelLayout.setHorizontalGroup(
            CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CategoryPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(categorybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(categoryname, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        CategoryPanelLayout.setVerticalGroup(
            CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryPanelLayout.createSequentialGroup()
                .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoryname, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categorybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(CategoryPanel, "card2");

        ProductsPanel.setBackground(new java.awt.Color(186, 164, 164));

        jLabel15.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel15.setText("Products");

        productsTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        productsTable.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        productsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(productsTable);

        categorybtn1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        categorybtn1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        categorybtn1.setText("Add");
        categorybtn1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        categorybtn1.setContentAreaFilled(false);
        categorybtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        categorybtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorybtn1ActionPerformed(evt);
            }
        });

        model.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        model.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                modelKeyReleased(evt);
            }
        });

        jLabel16.setText("Product Name");

        jLabel17.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel17.setText("Add Product");

        productname.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        productname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productnameActionPerformed(evt);
            }
        });
        productname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productnameKeyReleased(evt);
            }
        });

        brands.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        brands.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Brand");

        categories.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        categories.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Category *");

        jLabel18.setText("Model");

        jLabel19.setText("Expire date");

        expiredate.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        retailprice.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        retailprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retailpriceActionPerformed(evt);
            }
        });

        jLabel20.setText("Retail Price");

        listprice.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        jLabel21.setText("List Price");

        javax.swing.GroupLayout ProductsPanelLayout = new javax.swing.GroupLayout(ProductsPanel);
        ProductsPanel.setLayout(ProductsPanelLayout);
        ProductsPanelLayout.setHorizontalGroup(
            ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(brands, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(model, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categorybtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(productname, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categories, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(listprice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                        .addComponent(retailprice, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(expiredate, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ProductsPanelLayout.setVerticalGroup(
            ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsPanelLayout.createSequentialGroup()
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productname, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categories, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(brands, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(model, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expiredate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(retailprice, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listprice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categorybtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        ParentLayout.add(ProductsPanel, "card2");

        StaffPanel.setBackground(new java.awt.Color(186, 164, 164));

        categorybtn2.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        categorybtn2.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        categorybtn2.setText("Add");
        categorybtn2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        categorybtn2.setContentAreaFilled(false);
        categorybtn2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        categorybtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorybtn2ActionPerformed(evt);
            }
        });

        jLabel42.setText("Role");

        jLabel43.setText("Email");

        jLabel44.setText("Sur Name");

        staffname.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        staffname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffnameActionPerformed(evt);
            }
        });
        staffname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                staffnameKeyReleased(evt);
            }
        });

        jLabel45.setText("Name");

        jLabel46.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel46.setText("Add Staff");

        staffTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        staffTable.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        staffTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane12.setViewportView(staffTable);

        jLabel47.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel47.setText("Staff");

        staff_role.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout StaffPanelLayout = new javax.swing.GroupLayout(StaffPanel);
        StaffPanel.setLayout(StaffPanelLayout);
        StaffPanelLayout.setHorizontalGroup(
            StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StaffPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel45)
                    .addComponent(staffname, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(surname)
                    .addComponent(email)
                    .addComponent(categorybtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staff_role, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        StaffPanelLayout.setVerticalGroup(
            StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StaffPanelLayout.createSequentialGroup()
                .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StaffPanelLayout.createSequentialGroup()
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(StaffPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staffname, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(surname, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(staff_role, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categorybtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(StaffPanel, "card2");

        ReportsPanel.setBackground(new java.awt.Color(186, 164, 164));

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel4.setText("Daily Sales Report");

        dailySalesTable.setAutoCreateRowSorter(true);
        dailySalesTable.setBackground(new java.awt.Color(212, 198, 198));
        dailySalesTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        dailySalesTable.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        dailySalesTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(dailySalesTable);

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel9.setText("Total Investment:");

        jLabel24.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel24.setText("Total Returns:");

        jLabel26.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel26.setText("Profit:");

        tinvest.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tinvest.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        totalreturns.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        totalreturns.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        profit.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        profit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
        ReportsPanel.setLayout(ReportsPanelLayout);
        ReportsPanelLayout.setHorizontalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tinvest, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profit, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalreturns, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ReportsPanelLayout.setVerticalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalreturns, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(19, 19, 19)
                        .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tinvest, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ReportsPanelLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportsPanelLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(profit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(ReportsPanel, "card2");

        ConfigPanel.setBackground(new java.awt.Color(186, 164, 164));

        jButton3.setText("Delete All Orders");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel3.setText("User settings");

        jPanel2.setBackground(getBackground());
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 3, true), "Notifications & Sounds", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Calibri", 0, 16), new java.awt.Color(0, 153, 255))); // NOI18N
        jPanel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        soundSetting.setText("Play Notification Sounds");
        soundSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundSettingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(soundSetting)
                .addContainerGap(277, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(soundSetting)
                .addContainerGap(168, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ConfigPanelLayout = new javax.swing.GroupLayout(ConfigPanel);
        ConfigPanel.setLayout(ConfigPanelLayout);
        ConfigPanelLayout.setHorizontalGroup(
            ConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfigPanelLayout.createSequentialGroup()
                .addGroup(ConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ConfigPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ConfigPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ConfigPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1092, Short.MAX_VALUE))
        );
        ConfigPanelLayout.setVerticalGroup(
            ConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(247, Short.MAX_VALUE))
        );

        ParentLayout.add(ConfigPanel, "card2");

        StockPanel.setBackground(new java.awt.Color(186, 164, 164));

        stocksTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        stocksTable.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        stocksTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(stocksTable);

        jLabel22.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel22.setText("Stock");

        jLabel25.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel25.setText("Add Stock");

        jLabel10.setText("Product");

        insertBrand1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        insertBrand1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        insertBrand1.setText("Add");
        insertBrand1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        insertBrand1.setContentAreaFilled(false);
        insertBrand1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        insertBrand1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertBrand1ActionPerformed(evt);
            }
        });

        jLabel23.setText("Quantity");

        ProductsOnly.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout StockPanelLayout = new javax.swing.GroupLayout(StockPanel);
        StockPanel.setLayout(StockPanelLayout);
        StockPanelLayout.setHorizontalGroup(
            StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StockPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(StockPanelLayout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(StockPanelLayout.createSequentialGroup()
                                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(ProductsOnly, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(StockQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(insertBrand1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        StockPanelLayout.setVerticalGroup(
            StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StockPanelLayout.createSequentialGroup()
                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProductsOnly, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StockQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(insertBrand1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(StockPanel, "card2");

        WeeklyReport.setBackground(new java.awt.Color(186, 164, 164));

        jLabel27.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel27.setText("Weekly Sales Report");

        weeklyreporttable.setAutoCreateRowSorter(true);
        weeklyreporttable.setBackground(new java.awt.Color(212, 198, 198));
        weeklyreporttable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        weeklyreporttable.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        weeklyreporttable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(weeklyreporttable);

        jLabel28.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel28.setText("Total Investment:");

        jLabel29.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel29.setText("Total Returns:");

        jLabel30.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel30.setText("Profit:");

        tinvest1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tinvest1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        totalreturns1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        totalreturns1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        profit1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        profit1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout WeeklyReportLayout = new javax.swing.GroupLayout(WeeklyReport);
        WeeklyReport.setLayout(WeeklyReportLayout);
        WeeklyReportLayout.setHorizontalGroup(
            WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeeklyReportLayout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addGroup(WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WeeklyReportLayout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tinvest1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(WeeklyReportLayout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profit1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(WeeklyReportLayout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalreturns1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        WeeklyReportLayout.setVerticalGroup(
            WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeeklyReportLayout.createSequentialGroup()
                .addGroup(WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WeeklyReportLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(WeeklyReportLayout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addGroup(WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalreturns1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addGap(19, 19, 19)
                        .addGroup(WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tinvest1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addGroup(WeeklyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(WeeklyReportLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel30))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WeeklyReportLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(profit1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(WeeklyReport, "card2");

        MonthlyReport.setBackground(new java.awt.Color(186, 164, 164));

        jLabel31.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel31.setText("Monthly Sales Report");

        monthlyreporttable.setAutoCreateRowSorter(true);
        monthlyreporttable.setBackground(new java.awt.Color(212, 198, 198));
        monthlyreporttable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monthlyreporttable.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        monthlyreporttable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane10.setViewportView(monthlyreporttable);

        jLabel32.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel32.setText("Total Investment:");

        jLabel33.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel33.setText("Total Returns:");

        jLabel34.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel34.setText("Profit:");

        tinvest2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tinvest2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        totalreturns2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        totalreturns2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        profit2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        profit2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout MonthlyReportLayout = new javax.swing.GroupLayout(MonthlyReport);
        MonthlyReport.setLayout(MonthlyReportLayout);
        MonthlyReportLayout.setHorizontalGroup(
            MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MonthlyReportLayout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MonthlyReportLayout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tinvest2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MonthlyReportLayout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profit2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MonthlyReportLayout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalreturns2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MonthlyReportLayout.setVerticalGroup(
            MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MonthlyReportLayout.createSequentialGroup()
                .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MonthlyReportLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MonthlyReportLayout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalreturns2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addGap(19, 19, 19)
                        .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tinvest2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33))
                        .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MonthlyReportLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel34))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MonthlyReportLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(profit2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(MonthlyReport, "card2");

        DatedReport.setBackground(new java.awt.Color(186, 164, 164));

        jLabel35.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel35.setText("Dated Sales Report");

        datedreporttable.setAutoCreateRowSorter(true);
        datedreporttable.setBackground(new java.awt.Color(212, 198, 198));
        datedreporttable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        datedreporttable.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        datedreporttable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane11.setViewportView(datedreporttable);

        jLabel36.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel36.setText("Total Investment:");

        jLabel37.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel37.setText("Total Returns:");

        jLabel38.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel38.setText("Profit:");

        tinvest3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tinvest3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        totalreturns3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        totalreturns3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        profit3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        profit3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        datedrep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                datedrepMouseExited(evt);
            }
        });

        jButton12.setText("search");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DatedReportLayout = new javax.swing.GroupLayout(DatedReport);
        DatedReport.setLayout(DatedReportLayout);
        DatedReportLayout.setHorizontalGroup(
            DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DatedReportLayout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12)
                    .addGroup(DatedReportLayout.createSequentialGroup()
                        .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 1159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(datedrep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(DatedReportLayout.createSequentialGroup()
                                .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(DatedReportLayout.createSequentialGroup()
                                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tinvest3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(DatedReportLayout.createSequentialGroup()
                                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(profit3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(DatedReportLayout.createSequentialGroup()
                                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(totalreturns3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        DatedReportLayout.setVerticalGroup(
            DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DatedReportLayout.createSequentialGroup()
                .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(DatedReportLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(DatedReportLayout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(totalreturns3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addGap(19, 19, 19)
                        .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tinvest3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DatedReportLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel38))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DatedReportLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(profit3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(datedrep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(31, 31, 31)
                .addComponent(jButton12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(DatedReport, "card2");

        CashTransfers.setBackground(new java.awt.Color(186, 164, 164));

        categorybtn3.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        categorybtn3.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        categorybtn3.setText("Add");
        categorybtn3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        categorybtn3.setContentAreaFilled(false);
        categorybtn3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        categorybtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorybtn3ActionPerformed(evt);
            }
        });

        transferamount.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        transferamount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferamountActionPerformed(evt);
            }
        });
        transferamount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                transferamountKeyReleased(evt);
            }
        });

        jLabel51.setText("Amount");

        jLabel52.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel52.setText("Add Transfer");

        transferedCash.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        transferedCash.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        transferedCash.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane13.setViewportView(transferedCash);

        jLabel53.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel53.setText("Cash Transfer");

        javax.swing.GroupLayout CashTransfersLayout = new javax.swing.GroupLayout(CashTransfers);
        CashTransfers.setLayout(CashTransfersLayout);
        CashTransfersLayout.setHorizontalGroup(
            CashTransfersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CashTransfersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CashTransfersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(CashTransfersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51)
                    .addComponent(transferamount, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categorybtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CashTransfersLayout.setVerticalGroup(
            CashTransfersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CashTransfersLayout.createSequentialGroup()
                .addGroup(CashTransfersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CashTransfersLayout.createSequentialGroup()
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CashTransfersLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transferamount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(categorybtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(CashTransfers, "card2");

        getContentPane().add(ParentLayout, java.awt.BorderLayout.CENTER);

        jMenu1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153), null));
        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jCheckBoxMenuItem1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Home");
        jMenu1.add(jCheckBoxMenuItem1);

        jMenuItem1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));
        jMenu4.setText("Accounts");
        jMenu4.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jMenu6.setText("Cash Transfers");
        jMenu6.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem4.setText("Record Transfer");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem4);

        jMenuItem7.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem7.setText("Weekly Report");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem7);

        jMenuItem8.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem8.setText("Monthly Report");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem8);

        jMenuItem9.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem9.setText("Dated Report");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem9);

        jMenu4.add(jMenu6);

        jMenuBar1.add(jMenu4);

        jMenu2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));
        jMenu2.setText("Reports");
        jMenu2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jMenu3.setText("Sales Reports");
        jMenu3.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jMenuItem2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem2.setText("Daily Report");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem3.setText("Weekly Report");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem5.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem5.setText("Monthly Report");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem6.setText("Dated Report");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenu2.add(jMenu3);

        jMenu5.setText("Stock Reports");
        jMenu5.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenu5.setHideActionText(true);
        jMenu2.add(jMenu5);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
        public void loadJtableValues() throws SQLException, ClassNotFoundException {
             ordersModel.setRowCount(0);
            orderlist = listOrders();
            for(int i=0; i < orderlist.size(); i++){
                Object[] obj = {
                orderlist.get(i).orderid,
                orderlist.get(i).product,
                orderlist.get(i).quantity,
                orderlist.get(i).listprice,
                orderlist.get(i).discount};
                ordersModel.addRow(obj);
            }
    }
        
        public void loadBrandsJtableValues() throws SQLException, ClassNotFoundException {
            brandsModel.setRowCount(0);
            brandlist = listBrands();
            allBrands = new String[brandlist.size()];
            for(int i=0; i < brandlist.size(); i++){
                allBrands[i] = brandlist.get(i).brand_name;
                Object[] obj = {
                brandlist.get(i).brand_id,
                brandlist.get(i).brand_name};
                brandsModel.addRow(obj);
            }
    }
        
        public void LoadCategories() throws SQLException, ClassNotFoundException {
            categoryModel.setRowCount(0);
            categorylist = listCategories();
            allCats = new String[categorylist.size()];
            for(int i=0; i < categorylist.size(); i++){
                allCats[i] = categorylist.get(i).category_name; 
                Object[] obj = {
                categorylist.get(i).category_id,
                categorylist.get(i).category_name};
                categoryModel.addRow(obj);
            }
    }
           
    private void LoadProducts() throws ClassNotFoundException {
        productModel.setRowCount(0);
        try {
            productlist = listProducts();
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        nonStock = new String[productlist.size()];
            for(int i=0; i < productlist.size(); i++){
                nonStock[i] = productlist.get(i).product_name + " - Tsh "+  productlist.get(i).list_price + " : " + productlist.get(i).productid;
                Object[] obj = {
                productlist.get(i).product_name,
                productlist.get(i).brand_id,
                productlist.get(i).category_id,
                productlist.get(i).model_year,
                productlist.get(i).expiry_date,
                productlist.get(i).list_price,
                productlist.get(i).retail_price
                };
                productModel.addRow(obj);
            }
    }
    
    public void LoadStockProducts() throws ClassNotFoundException{
        try {
            stockproductlist = listStockProducts();
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, 
                    null, ex);
        }
        allProducts = new String[stockproductlist.size()];
            for(int i=0; i < stockproductlist.size(); i++){
            allProducts[i] = stockproductlist.get(i).product_name + " - Tsh "+ 
                    stockproductlist.get(i).list_price + " : " +
                    stockproductlist.get(i).productid;
            }
    }
    
    public void LoadProductsOnly() throws SQLException, ClassNotFoundException{
            productsonly = listProductOnly();
            productonly = new String[productsonly.size()];
            for(int i=0; i < productsonly.size(); i++){
                productonly[i] = productsonly.get(i).product_name + " - Tsh "
                        +  productsonly.get(i).list_price + " : "
                        + productsonly.get(i).productid;
            }   
    }
            
    public void LoadStocks() throws ClassNotFoundException{          
          stockModel.setRowCount(0);
          stocklist = listStocks();  
            for(int i=0; i < stocklist.size(); i++){
                Object[] obj = {
                stocklist.get(i).product_id,
                stocklist.get(i).quantity
                };
                stockModel.addRow(obj);
            }
    }
    
    public void LoadDailySalesReport() throws ParseException, ClassNotFoundException{
        dailySalesModel.setRowCount(0);
        dailyreportlist = listDailyReport();
        
        if(dailyreportlist.isEmpty()){
            
        }
        else {
        tinvest.setText("Tsh "+dailyreportlist.get(dailyreportlist.size()-1).totalinvestment);
        totalreturns.setText("Tsh "+dailyreportlist.get(dailyreportlist.size()-1).totalreturns);
        profit.setText("Tsh "+dailyreportlist.get(dailyreportlist.size()-1).superprofit);
        for(int i=0; i < dailyreportlist.size(); i++){
            Object[] obj = {
                dailyreportlist.get(i).productname,
                dailyreportlist.get(i).retailprice,
                dailyreportlist.get(i).listprice,
                dailyreportlist.get(i).profit,
                dailyreportlist.get(i).quantity,
                dailyreportlist.get(i).date
                };
                dailySalesModel.addRow(obj);
        }
     }
    }
    
    public void LoadweeklyReport() throws ParseException, ClassNotFoundException{
        weeklyModel.setRowCount(0);
        weeklyreportlist = listWeeklyReport();
        tinvest1.setText("Tsh "+weeklyreportlist.get(weeklyreportlist.size()-1).totalinvestment);
        totalreturns1.setText("Tsh "+weeklyreportlist.get(weeklyreportlist.size()-1).totalreturns);
        profit1.setText("Tsh "+weeklyreportlist.get(weeklyreportlist.size()-1).superprofit);
        for(int i=0; i < weeklyreportlist.size(); i++){
            Object[] obj = {
                weeklyreportlist.get(i).productname,
                weeklyreportlist.get(i).retailprice,
                weeklyreportlist.get(i).listprice,
                weeklyreportlist.get(i).profit,
                weeklyreportlist.get(i).quantity,
                weeklyreportlist.get(i).date
                };
                weeklyModel.addRow(obj);
        }
    }
    
    public void LoadmonthlyReport() throws ParseException, ClassNotFoundException{
        monthlyModel.setRowCount(0);
        monthlyreportlist = listMonthlyReport(); 
        tinvest2.setText("Tsh "+monthlyreportlist.get(monthlyreportlist.size()-1).totalinvestment);
        totalreturns2.setText("Tsh "+monthlyreportlist.get(monthlyreportlist.size()-1).totalreturns);
        profit2.setText("Tsh "+monthlyreportlist.get(monthlyreportlist.size()-1).superprofit);
        for(int i=0; i < monthlyreportlist.size(); i++){
            Object[] obj = {
                monthlyreportlist.get(i).productname,
                monthlyreportlist.get(i).retailprice,
                monthlyreportlist.get(i).listprice,
                monthlyreportlist.get(i).profit,
                monthlyreportlist.get(i).quantity,
                monthlyreportlist.get(i).date
                };
                monthlyModel.addRow(obj);
        }
    }
    
    public void LoaddatedReport(String dt) throws ParseException, ClassNotFoundException{
        datedModel.setRowCount(0);
        datedreportlist = DatedReport(dt);
        totalreturns.setText("Tsh "+datedreportlist.get(datedreportlist.size()-1).totalreturns);
        profit.setText("Tsh "+datedreportlist.get(datedreportlist.size()-1).superprofit);
        for(int i=0; i < datedreportlist.size(); i++){
            Object[] obj = {
                datedreportlist.get(i).productname,
                datedreportlist.get(i).retailprice,
                datedreportlist.get(i).listprice,
                datedreportlist.get(i).profit,
                datedreportlist.get(i).quantity,
                datedreportlist.get(i).date
                };
                datedModel.addRow(obj);
        }
    }
        
        public void LoadSatffs() throws ParseException, ClassNotFoundException{
        StaffsModel.setRowCount(0);
        staffslist = LoadStaffs();
        if(staffslist.isEmpty()){
            
        }
        else{
             for(int i=0; i < staffslist.size(); i++){
            Object[] obj = {
                staffslist.get(i).staff_name,
                staffslist.get(i).sur_name,
                staffslist.get(i).staff_email,
                staffslist.get(i).role
                };
                StaffsModel.addRow(obj);
         }
        }
       }
        
        
       public void LoadTransfers() throws ParseException, ClassNotFoundException{
        TransferModel.setRowCount(0);
        transferedlist = getTransfers();
        
        for(int i=0; i < transferedlist.size(); i++){
            Object[] obj = {
                transferedlist.get(i).amount,
                transferedlist.get(i).date,
                transferedlist.get(i).collector,
                transferedlist.get(i).total
                };
                TransferModel.addRow(obj);
        }
       }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ParentLayout.removeAll();
        ParentLayout.add(BrandPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void OrderbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderbtnActionPerformed
        // TODO add your handling code here:
         ParentLayout.removeAll();
        ParentLayout.add(OrderPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
        try {
            loadJtableValues();
            LoadStockProducts();
            
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_OrderbtnActionPerformed

    private void prdbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prdbtnActionPerformed
        try {
            // TODO add your handling code here:
            loadBrandsJtableValues();
            LoadCategories();
            LoadCategories();
             } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
            ParentLayout.removeAll();
            ParentLayout.add(ProductsPanel);
            ParentLayout.repaint();
            ParentLayout.revalidate();
       
    }//GEN-LAST:event_prdbtnActionPerformed

    private void stocksbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stocksbtnActionPerformed
        // TODO add your handling code here:
        ParentLayout.removeAll();
        ParentLayout.add(StockPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_stocksbtnActionPerformed

    private void staffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffButtonActionPerformed
        try {
            // TODO add your handling code here:
            ParentLayout.removeAll();
            ParentLayout.add(StaffPanel);
            ParentLayout.repaint();
            ParentLayout.revalidate();
            LoadSatffs();
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_staffButtonActionPerformed

    private void CategoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategoryBtnActionPerformed
        // TODO add your handling code here:
        ParentLayout.removeAll();
        ParentLayout.add(CategoryPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_CategoryBtnActionPerformed

    private void quantityfieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantityfieldKeyReleased
        // TODO add your handling code here:
        quantity = Integer.parseInt(quantityfield.getText());
    }//GEN-LAST:event_quantityfieldKeyReleased

    private void oneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneBtnActionPerformed
        // TODO add your handling code here:
        int i = 1;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity = k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_oneBtnActionPerformed

    private void twoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twoBtnActionPerformed
        // TODO add your handling code here:

        int i = 2;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_twoBtnActionPerformed

    private void threeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threeBtnActionPerformed
        // TODO add your handling code here:

        int i = 3;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_threeBtnActionPerformed

    private void fourBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fourBtnActionPerformed
        // TODO add your handling code here:
        int i = 4;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_fourBtnActionPerformed

    private void fiveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveBtnActionPerformed
        // TODO add your handling code here:
        int c = 5;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(c));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_fiveBtnActionPerformed

    private void sixBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sixBtnActionPerformed
        // TODO add your handling code here:
        int i = 6;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_sixBtnActionPerformed

    private void sevenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sevenBtnActionPerformed
        // TODO add your handling code here:

        int i = 7;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_sevenBtnActionPerformed

    private void eightBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eightBtnActionPerformed
        // TODO add your handling code here:

        int i = 8;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_eightBtnActionPerformed

    private void zeroBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroBtn1ActionPerformed
        // TODO add your handling code here:
        quantityfield.setText("");
        quantity = 0;
    }//GEN-LAST:event_zeroBtn1ActionPerformed

    private void zeroBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroBtnActionPerformed
        // TODO add your handling code here:
        int i = 0;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_zeroBtnActionPerformed

    private void nineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nineBtnActionPerformed
        // TODO add your handling code here:
        int i = 9;
        long k = Integer.valueOf(String.valueOf(quantity) + String.valueOf(i));
        quantity=k;
        quantityfield.setText(String.valueOf(k));
    }//GEN-LAST:event_nineBtnActionPerformed

    private void saleproductbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleproductbtnActionPerformed
        saleproductbtn.setEnabled(false);
        saleproductbtn.setText("Wait");
        try {
            // TODO add your handling code here:
            //this is the saling button
            /**
             * All codes associating with selling comes here
             */
            
            quantity = Integer.parseInt(quantityfield.getText());
            String order_id = unique();
            String item_id = unique();
            String product_id = SearchProductForm.getText();
            String product = SearchProductForm.getText();
            Double price = 1.0;
            Double discount = 0.00;
            SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
            String backdated = dcn.format(backdate.getDate());
            
            try {
                if(Orders.addOrder(order_id,item_id,product_id,quantity,price,discount,backdated)){
                    Stocks.editStock(product_id,quantity);
                    JOptionPane.showMessageDialog(this,"Succesfully");
                    try {
                        loadJtableValues();
                    } catch (SQLException ex) {
                        Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        saleproductbtn.setEnabled(true);
        saleproductbtn.setText("Sale");
    }//GEN-LAST:event_saleproductbtnActionPerformed

    private void brandnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_brandnameKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_brandnameKeyReleased

    private void insertBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertBrandActionPerformed
        try {
            // TODO add your handling code here:
            
//        String brand_id = unique();
String brand_name = brandname.getText();
if(Crudes.addBrand(brand_name)){
    JOptionPane.showMessageDialog(this,"Succesfully");
        loadBrandsJtableValues();
   
}
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertBrandActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        ParentLayout.removeAll();
        ParentLayout.add(ReportsPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
        try {
            LoadDailySalesReport();
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void categorynameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_categorynameKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_categorynameKeyReleased

    private void categorybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorybtnActionPerformed
        // TODO add your handling code here:
           try {
            // TODO add your handling code here:
            //String brand_id = unique();
            String category_name = categoryname.getText();
            if(Categories.addCategory(category_name)){
            JOptionPane.showMessageDialog(this,"Succesfully");
            LoadCategories();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_categorybtnActionPerformed

    private void categorybtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorybtn1ActionPerformed
        // TODO add your handling code here:
        
        String product_name = productname.getText();
        int brand_id = brands.getSelectedIndex()+1;
        int category_id = categories.getSelectedIndex()+1;
        int model_year = Integer.parseInt(model.getText());
        String expiry_date = expiredate.getText();
        Double list_price = Double.parseDouble(listprice.getText());
        Double retail_price = Double.parseDouble(retailprice.getText());
                
        try {
            try {
                if(Products.addProduct(product_name,
                        brand_id,
                        category_id,
                        model_year,
                        expiry_date,
                        list_price,
                        retail_price)){
                    JOptionPane.showMessageDialog(this,"Succesfully");
                    LoadProducts();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      clearFields();
    }//GEN-LAST:event_categorybtn1ActionPerformed

    public void clearFields(){
        productname.setText("");
        categories.getEditor().setItem("");
        brands.getEditor().setItem("");
        model.setText("");
        expiredate.setText("");
        retailprice.setText("");
        listprice.setText("");
    }
    
    private void modelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_modelKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_modelKeyReleased

    private void productnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productnameKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_productnameKeyReleased

    private void productnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productnameActionPerformed

    private void retailpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retailpriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_retailpriceActionPerformed

    private void insertBrand1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertBrand1ActionPerformed
        try {
            // TODO add your handling code here:
            
            String productname = ProductsOnly.getSelectedItem().toString();
            int productquantity =  Integer.parseInt(StockQuantity.getText());
            
            if(Stocks.addStock(productname,productquantity)){
                JOptionPane.showMessageDialog(this,"Succesfully");
                LoadStocks();
                try {
                    LoadProductsOnly();
                } catch (SQLException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertBrand1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        ParentLayout.removeAll();
        ParentLayout.add(ConfigPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_jButton8ActionPerformed

    
    private void ordersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersTableMouseClicked
        // TODO add your handling code here:
        row = ordersTable.getSelectedRow();
        column = ordersTable.getColumnCount();
        SearchProductForm.setText(ordersModel.getValueAt(row, 1).toString());
        quantityfield.setText(ordersModel.getValueAt(row, 2).toString());
//        String productname = ordersModel.getValueAt(row, 2).toString();
    }//GEN-LAST:event_ordersTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            Orders.deleteAll();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(this,"Succesfully");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
            ParentLayout.removeAll();
            ParentLayout.add(ReportsPanel);
            ParentLayout.repaint();
            ParentLayout.revalidate();
        try {
            LoadDailySalesReport();
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            // TODO add your handling code here:
            ParentLayout.removeAll();
            ParentLayout.add(WeeklyReport);
            ParentLayout.repaint();
            ParentLayout.revalidate();
            LoadweeklyReport();
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
            ParentLayout.removeAll();
            ParentLayout.add(MonthlyReport);
            ParentLayout.repaint();
            ParentLayout.revalidate();
        try {
            LoadmonthlyReport();
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
            ParentLayout.removeAll();
            ParentLayout.add(DatedReport);
            ParentLayout.repaint();
            ParentLayout.revalidate();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void datedrepMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datedrepMouseExited
        // TODO add your handling code here:
      
    }//GEN-LAST:event_datedrepMouseExited

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
        String backdated = dcn.format(datedrep.getDate());
        try {
            LoaddatedReport(backdated);
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void categorybtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorybtn2ActionPerformed
        try {
            // TODO add your handling code here:
            //Save to the staffs table.
            
            String product_name = productname.getText();
            String staff_name = staffname.getText();
            String sur_name = surname.getText();
            String staff_email = email.getText();
            int role = staff_role.getSelectedIndex();
            
            if(Sales_Staffs.addStaff(
                    staff_name,
                    sur_name,
                    staff_email,
                    role)){
                JOptionPane.showMessageDialog(this,"Succesfully");
                try {
                    LoadSatffs();
                } catch (ParseException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            clearUserFields();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_categorybtn2ActionPerformed

    public void clearUserFields(){
        staffname.setText("");
        surname.setText("");
        email.setText("");
        staff_role.setSelectedIndex(0);
    }
    private void staffnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffnameActionPerformed

    private void staffnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_staffnameKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_staffnameKeyReleased

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
            ParentLayout.removeAll();
            ParentLayout.add(CashTransfers);
            ParentLayout.repaint();
            ParentLayout.revalidate();
            
        try {
            LoadTransfers();
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void categorybtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorybtn3ActionPerformed
        try {
            // TODO add your handling code here:
            String transfer_amounts;
            transfer_amounts = transferamount.getText();
            if(Accounts.addTransfer(transfer_amounts)){
                try {
                    JOptionPane.showMessageDialog(this,"Succesfully");
                    LoadTransfers();
                } catch (ParseException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_categorybtn3ActionPerformed

    private void transferamountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferamountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transferamountActionPerformed

    private void transferamountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transferamountKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_transferamountKeyReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void soundSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundSettingActionPerformed
        // TODO add your handling code here:
        AudioFile Af = new AudioFile();
        String a = soundSetting.getText();
        UserSettings us = new UserSettings();
        try {
            Af.Playme("success");
            if("Play Notification Sounds".equals(a)){
             us.soundSettings(true);   
            }
            else
            {
                us.soundSettings(false);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }//GEN-LAST:event_soundSettingActionPerformed

    private void searchLabelIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchLabelIconMouseClicked
     
        //Search engine.
        row = ordersTable.getSelectedRow();
        column = ordersTable.getColumnCount();
        //Row sorters
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(ordersModel);
        ordersTable.setRowSorter(sorter);
        //End of row sorters
        String input = JOptionPane.showInputDialog(this, "Search row");
         if (input.length() == 0) {
               sorter.setRowFilter(null);
            } else {
               sorter.setRowFilter(RowFilter.regexFilter(input));
            }
    }//GEN-LAST:event_searchLabelIconMouseClicked

    private void deleteLabelIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteLabelIconMouseClicked
       //Delete sales.
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            ordersModel.removeRow(row);
            orderlist.remove(row);
            ordersModel.setRowCount(0);
            try {
                Orders.deleteRow(row);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < orderlist.size(); i++) {
                Object[] obj = {
                    orderlist.get(i).orderid,
                    orderlist.get(i).product,
                    orderlist.get(i).quantity,
                    orderlist.get(i).listprice,
                    orderlist.get(i).discount};
                ordersModel.addRow(obj);
            }
        }
    }//GEN-LAST:event_deleteLabelIconMouseClicked

    private void eraserLabelIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraserLabelIconMouseClicked
        try {
            // TODO add your handling code here:
            this.ordersTable.setAutoCreateRowSorter(false);
            this.ordersTable.setAutoCreateRowSorter(true);
            this.ordersTable.repaint();
            SearchProductForm.setText("");
            quantityfield.setText("");
            loadJtableValues();
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
                                    
    }//GEN-LAST:event_eraserLabelIconMouseClicked

    private void jLabel39MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel39MouseClicked
        // TODO add your handling code here:
         // this is the back dating button:
        datelabel.setVisible(true);
        backdate.setVisible(true);
        LocalDate d1 = LocalDate.now(ZoneId.of("Europe/Paris"));
    }//GEN-LAST:event_jLabel39MouseClicked

    private void quantityfieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quantityfieldMouseExited
        // TODO add your handling code here:
        int x;
         try {
        x = Integer.parseInt(quantityfield.getText());
        } catch (NumberFormatException nfe) {
            quantityfield.setText("");
             quantity = 0;
        }
    }//GEN-LAST:event_quantityfieldMouseExited

    private void saleproductbtnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleproductbtnUpdateActionPerformed
        // Updating the values of order
        //This is conditional to users (administrator's) configurations
        saleproductbtnUpdate.setEnabled(false);
        saleproductbtnUpdate.setText("Wait");
        try {
            // TODO add your handling code here:
            //this is the saling button
            /**
             * All codes associating with selling comes here
             */
            
            quantity = Integer.parseInt(quantityfield.getText());
            int order_id = row;
            String item_id = unique();
            String product_id = SearchProductForm.getText();
            Double price = 1.0;
            Double discount = 0.00;
            SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
            String backdated = dcn.format(backdate.getDate());
            
            try {
                if(Orders.updateOrder(order_id,item_id,product_id,quantity,price,discount,backdated)){
                    Stocks.editStock(product_id,quantity);
                    JOptionPane.showMessageDialog(this,"Succesfully edited");
                    try {
                        loadJtableValues();
                    } catch (SQLException ex) {
                        Logger.getLogger(UserPanel.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } catch (ParseException ex) {
            Logger.getLogger(UserInterface.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        saleproductbtnUpdate.setEnabled(true);
        saleproductbtnUpdate.setText("Update");
    }//GEN-LAST:event_saleproductbtnUpdateActionPerformed

    /**
     */
    
    public static void PlayNotification(String type) {
     AudioFile Af = new AudioFile();
        try {
            Af.Playme(type);
        } catch (URISyntaxException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public static void UserIntfc() {
        
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
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new UserInterface().setVisible(true);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BrandPanel;
    private javax.swing.JPanel CashTransfers;
    private javax.swing.JButton CategoryBtn;
    private javax.swing.JPanel CategoryPanel;
    private javax.swing.JPanel ConfigPanel;
    private javax.swing.JPanel DatedReport;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JPanel MonthlyReport;
    private javax.swing.JPanel OrderPanel;
    private javax.swing.JButton Orderbtn;
    private javax.swing.JPanel ParentLayout;
    private javax.swing.JComboBox<String> ProductsOnly;
    private javax.swing.JPanel ProductsPanel;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JTextArea SearchProductForm;
    private javax.swing.JPanel StaffPanel;
    private javax.swing.JPanel StockPanel;
    private javax.swing.JTextField StockQuantity;
    private javax.swing.JPanel WeeklyReport;
    private com.toedter.calendar.JCalendar backdate;
    private javax.swing.JTextField brandname;
    private javax.swing.JComboBox<String> brands;
    private javax.swing.JTable brandsTable;
    private javax.swing.JComboBox<String> categories;
    private javax.swing.JTable categoryTable;
    private javax.swing.JButton categorybtn;
    private javax.swing.JButton categorybtn1;
    private javax.swing.JButton categorybtn2;
    private javax.swing.JButton categorybtn3;
    private javax.swing.JTextField categoryname;
    private javax.swing.JTable dailySalesTable;
    private com.toedter.calendar.JCalendar datedrep;
    private javax.swing.JTable datedreporttable;
    private javax.swing.JLabel datelabel;
    private javax.swing.JLabel deleteLabelIcon;
    private javax.swing.JButton eightBtn;
    private javax.swing.JTextField email;
    private javax.swing.JLabel eraserLabelIcon;
    private javax.swing.JTextField expiredate;
    private javax.swing.JButton fiveBtn;
    private javax.swing.JButton fourBtn;
    private javax.swing.JButton insertBrand;
    private javax.swing.JButton insertBrand1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField listprice;
    private javax.swing.JTextField model;
    private javax.swing.JTable monthlyreporttable;
    private javax.swing.JButton nineBtn;
    private javax.swing.JButton oneBtn;
    private javax.swing.JTable ordersTable;
    private javax.swing.JButton prdbtn;
    private javax.swing.JTextField productname;
    private javax.swing.JTable productsTable;
    private javax.swing.JLabel profit;
    private javax.swing.JLabel profit1;
    private javax.swing.JLabel profit2;
    private javax.swing.JLabel profit3;
    private javax.swing.JLabel qntlabel;
    private javax.swing.JTextField quantityfield;
    private javax.swing.JTextField retailprice;
    private javax.swing.JButton saleproductbtn;
    private javax.swing.JButton saleproductbtnUpdate;
    private javax.swing.JLabel searchLabelIcon;
    private javax.swing.JButton sevenBtn;
    private javax.swing.JButton sixBtn;
    private javax.swing.JRadioButton soundSetting;
    private javax.swing.JButton staffButton;
    private javax.swing.JTable staffTable;
    private javax.swing.JComboBox<String> staff_role;
    private javax.swing.JTextField staffname;
    private javax.swing.JTable stocksTable;
    private javax.swing.JButton stocksbtn;
    private javax.swing.JTextField surname;
    private javax.swing.JButton threeBtn;
    private javax.swing.JLabel tinvest;
    private javax.swing.JLabel tinvest1;
    private javax.swing.JLabel tinvest2;
    private javax.swing.JLabel tinvest3;
    private javax.swing.JLabel todays;
    private javax.swing.JLabel totalreturns;
    private javax.swing.JLabel totalreturns1;
    private javax.swing.JLabel totalreturns2;
    private javax.swing.JLabel totalreturns3;
    private javax.swing.JTextField transferamount;
    private javax.swing.JTable transferedCash;
    private javax.swing.JButton twoBtn;
    public static javax.swing.JLabel usernameLabel;
    private javax.swing.JTable weeklyreporttable;
    private javax.swing.JButton zeroBtn;
    private javax.swing.JButton zeroBtn1;
    // End of variables declaration//GEN-END:variables
 
}
