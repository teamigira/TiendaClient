/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import static Authentication.Auth.Login;
import static Authentication.Sessions.LoggedUser;
import Classes.AbstractClasses.DailyReport;
import Classes.Functions.Accounts;
import static Classes.Functions.Accounts.getTransfers;
import Classes.AbstractClasses.Brands;
import Classes.Functions.Categories;
import static Classes.Functions.Categories.listBrands;
import static Classes.Functions.Categories.listCategories;
import Classes.AbstractClasses.Category;
import Classes.AbstractClasses.Email;
import Classes.Functions.Crudes;
import Classes.AbstractClasses.Order;
import Classes.Functions.Orders;
import static Classes.Functions.Orders.listOrders;
import Classes.AbstractClasses.Product;
import Classes.Functions.Products;
import static Classes.Functions.Products.listProductOnly;
import static Classes.Functions.Products.listProducts;
import static Classes.Functions.Products.listStockProducts;
import static Classes.Functions.Reports.DatedReport;
import static Classes.Functions.Reports.listDailyReport;
import static Classes.Functions.Reports.listMonthlyReport;
import static Classes.Functions.Reports.listWeeklyReport;
import Classes.AbstractClasses.Staff;
import Classes.Functions.Sales_Staffs;
import static Classes.Functions.Sales_Staffs.LoadStaffs;
import Classes.AbstractClasses.Stock;
import Classes.Functions.Stocks;
import static Classes.Functions.Stocks.listStocks;
import Classes.AbstractClasses.Transfer;
import Classes.Functions.Notifications;
import static Classes.Functions.Notifications.listNotifications;
import static Classes.Functions.Orders.listOrdersDated;
import static Classes.Functions.Reports.Summaryeport;
import Classes.Utilities.AudioPlayer;
import Classes.Utilities.ExcelFormat;
import UserSettings.UserSettings;
import static com.nkanabo.Tienda.Utilities.IntegerConverter;
import static com.nkanabo.Tienda.Utilities.unique;
import Classes.Utilities.StockThread;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Nkanabo
 */
public final class UserInterface extends javax.swing.JFrame {

    static UserInterface Instance;
    
     int productId;
    
    ArrayList<Order> orderlist;
    String headers[] = {"Order Id", "Product", "Quantity", "List price", "Discount"};
    DefaultTableModel ordersModel;
    //Brands
    ArrayList<Brands> brandlist;
    String brandheaders[] = {"Brand Id", "Brand Name"};
    DefaultTableModel brandsModel;
    
    //end of brands
    //Categories
    ArrayList<Category> categorylist;
    String categoryheader[] = {"category_id", "category_name"};
    DefaultTableModel categoryModel;
    //End of categories

    //Products
    ArrayList<Product> productlist; //Products with 0 stock
    ArrayList<Product> stockproductlist; //products that has stock>0
    ArrayList<Product> productsonly; //products with no stock
    String productheader[] = {"Id", "Product", "Brand", "Category", "Model", "Expire Date", "Price", "Retail"};
    DefaultTableModel productModel;
    //End of products

    //Products
    ArrayList<Stock> stocklist;
    String stockheader[] = {"Product Id","Product", "Quantity"};
    DefaultTableModel stockModel;
    //End of products

    DefaultTableModel dailySalesModel;
    String dailyreportheader[] = {"Product Name", "Retail Price", "List Price", "Profit", "quantity", "date"};
    ArrayList<DailyReport> dailyreportlist;

    DefaultTableModel StaffsModel;
    String staffsheader[] = {"First Name", "Last Name", "Email", "Role"};
    ArrayList<Staff> staffslist;

    DefaultTableModel TransferModel;
    String transferheader[] = {"Amount", "Date", "Collected by"};
    ArrayList<Transfer> transferedlist;

    DefaultTableModel weeklyModel;
    ArrayList<DailyReport> weeklyreportlist;

    DefaultTableModel monthlyModel;
    ArrayList<DailyReport> monthlyreportlist;

    DefaultTableModel datedModel;
    ArrayList<DailyReport> datedreportlist;
    
    DefaultTableModel emailNotifications;
    String emailHeader[] = {"Count", "Sent Date", "Subject","Message"};
    ArrayList<Email> emailList;

    public String quantity = "";
    int row, column;
    String[] allBrands;
    String[] allCats;
    String[] allProducts;
    String[] nonStock;
    String[] productonly;

    /**
     * Creates new form UserInterface
     * @throws java.net.URISyntaxException
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
    */
    
    public UserInterface() throws URISyntaxException, ClassNotFoundException, ParseException {
        System.setProperty("prism.allowhidpi", "false");
        initComponents();
        StockThread th = new StockThread();
        StockThread.main();

           if (Instance != null) {
            try {
                Instance = new UserInterface();
                String url = "resources/images/icons8.jpg";
                File is = Instance.getFileFromResource(url);
                
                String filepath = Paths.get(is.toURI()).toFile().getAbsolutePath();
                ImageIcon icon = new ImageIcon(filepath);
                setIconImage(icon.getImage()); 
        
            } catch (URISyntaxException ex) {
                Logger.getLogger(launcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       

        usernameLabel.setText(LoggedUser);
        this.pack();
        String today = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        todays.setText(String.valueOf(Calendar.getInstance().getTime()));
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setExtendedState(UserInterface.MAXIMIZED_BOTH);
        
        orderlist = new ArrayList<>();
        ordersModel = new DefaultTableModel(headers, 0);
        ordersTable.setModel(ordersModel);

        //Brands
        brandlist = new ArrayList<>();
        brandsModel = new DefaultTableModel(brandheaders, 0);
        brandsTable.setModel(brandsModel);

        //End of Brands
        //Categories
        categorylist = new ArrayList<>();
        categoryModel = new DefaultTableModel(categoryheader, 0);
        categoryTable.setModel(categoryModel);
        //End of categories

        //Categories
        productlist = new ArrayList<>();
        stockproductlist = new ArrayList<>();
        productsonly = new ArrayList<>();
        productModel = new DefaultTableModel(productheader, 0);
        productsTable.setModel(productModel);
        //End of categories

        //Stocks
        stocklist = new ArrayList<>();
        stockModel = new DefaultTableModel(stockheader, 0);
        stocksTable.setModel(stockModel);
        //End of categories

        dailyreportlist = new ArrayList<>();
        dailySalesModel = new DefaultTableModel(dailyreportheader, 0);
        dailySalesTable.setModel(dailySalesModel);

        weeklyreportlist = new ArrayList<>();
        weeklyModel = new DefaultTableModel(dailyreportheader, 0);
        weeklyreporttable.setModel(weeklyModel);

        monthlyreportlist = new ArrayList<>();
        monthlyModel = new DefaultTableModel(dailyreportheader, 0);
        monthlyreporttable.setModel(monthlyModel);

        datedreportlist = new ArrayList<>();
        datedModel = new DefaultTableModel(dailyreportheader, 0);
        datedreporttable.setModel(datedModel);

        //Staffs
        staffslist = new ArrayList<>();
        StaffsModel = new DefaultTableModel(staffsheader, 0);
        staffTable.setModel(StaffsModel);

        //Transfers
        transferedlist = new ArrayList<>();
        TransferModel = new DefaultTableModel(transferheader, 0);
        transferedCash.setModel(TransferModel);
        
        
         //Notifications
        emailList = new ArrayList<>();
        emailNotifications = new DefaultTableModel(emailHeader, 0);
        EmailTable.setModel(emailNotifications);
        
        

        this.setLocationRelativeTo(null);
        try {
            loadJtableValues();
            loadBrandsJtableValues();
            LoadCategories();
            LoadProducts();
            LoadStockProducts();
            LoadProductsOnly();
            LoadStocks();
            LoadNotificationsEmails();
            LoadNotificationsEmails();
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(allBrands);
        brands.setModel(model);

        DefaultComboBoxModel catsmodel = new DefaultComboBoxModel(allCats);
        categoriesCombo.setModel(catsmodel);

        DefaultComboBoxModel prodsmodel = new DefaultComboBoxModel(productonly);
        ProductsOnly.setModel(prodsmodel);

        String[] unittypes = {"Set", "Unit"};
        DefaultComboBoxModel unittype = new DefaultComboBoxModel(unittypes);
        producttype.setModel(unittype);

        List list = Arrays.asList(allProducts);
        List productions = Arrays.asList(productonly);
        AutoCompleteDecorator.decorate(brands);
        AutoCompleteDecorator.decorate(categoriesCombo);
        AutoCompleteDecorator.decorate(ProductsOnly);
        AutoCompleteDecorator.decorate(SearchProductForm, list, false);
        //AutoCompleteDecorator.decorate(ProductName, productions,true);
        datelabel.setVisible(false);
        backdate.setVisible(false);
        productid.setVisible(false);
        EmailnotificationLabel.setVisible(false);
        LabelProduct.setVisible(false);
        updateProductbtn.setVisible(false);
        productid.setVisible(false);

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
        Brandsbtn = new javax.swing.JButton();
        Orderbtn = new javax.swing.JButton();
        prdbtn = new javax.swing.JButton();
        stocksbtn = new javax.swing.JButton();
        CategoryBtn = new javax.swing.JButton();
        staffButton = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        usernameLabel = new javax.swing.JLabel();
        EmailnotificationLabel = new javax.swing.JLabel();
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
        productid = new javax.swing.JTextField();
        backbutton = new javax.swing.JLabel();
        eightBtn1 = new javax.swing.JButton();
        eightBtn2 = new javax.swing.JButton();
        eightBtn3 = new javax.swing.JButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jLabel23 = new javax.swing.JLabel();
        BrandPanel = new javax.swing.JPanel();
        insertBrand = new javax.swing.JButton();
        brandname = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        brandsTable = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton8 = new javax.swing.JToggleButton();
        searchLabelIcon3 = new javax.swing.JLabel();
        deleteLabelIcon4 = new javax.swing.JLabel();
        eraserLabelIcon3 = new javax.swing.JLabel();
        CategoryPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        categoryTable = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        categoryname = new javax.swing.JTextField();
        categorybtn = new javax.swing.JButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        searchLabelIcon4 = new javax.swing.JLabel();
        deleteLabelIcon5 = new javax.swing.JLabel();
        eraserLabelIcon4 = new javax.swing.JLabel();
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
        jToggleButton5 = new javax.swing.JToggleButton();
        ProductsPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        categorybtn1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        productname = new javax.swing.JTextField();
        brands = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        categoriesCombo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        retailprice = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        listprice = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        eraserLabelIcon1 = new javax.swing.JLabel();
        deleteLabelIcon1 = new javax.swing.JLabel();
        searchLabelIcon1 = new javax.swing.JLabel();
        producttype = new javax.swing.JComboBox<>();
        expiredate = new com.toedter.calendar.JCalendar();
        updateProductbtn = new javax.swing.JButton();
        hiddenProductId = new javax.swing.JTextField();
        jToggleButton6 = new javax.swing.JToggleButton();
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
        jToggleButton4 = new javax.swing.JToggleButton();
        ConfigPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        soundSetting = new javax.swing.JRadioButton();
        StockPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        stocksTable = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        addstocklbl = new javax.swing.JLabel();
        prdlabel = new javax.swing.JLabel();
        StockAddBtn = new javax.swing.JButton();
        StockQuantity = new javax.swing.JTextField();
        qtylabel = new javax.swing.JLabel();
        ProductsOnly = new javax.swing.JComboBox<>();
        searchLabelIcon2 = new javax.swing.JLabel();
        deleteLabelIcon2 = new javax.swing.JLabel();
        eraserLabelIcon2 = new javax.swing.JLabel();
        LabelProduct = new javax.swing.JLabel();
        jToggleButton3 = new javax.swing.JToggleButton();
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
        jButton1 = new javax.swing.JButton();
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
        jLabel10 = new javax.swing.JLabel();
        overallInvestment = new javax.swing.JButton();
        jToggleButton2 = new javax.swing.JToggleButton();
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
        jToggleButton1 = new javax.swing.JToggleButton();
        CashTransfers = new javax.swing.JPanel();
        categorybtn3 = new javax.swing.JButton();
        transferamount = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        transferedCash = new javax.swing.JTable();
        jLabel53 = new javax.swing.JLabel();
        Emails = new javax.swing.JPanel();
        categorybtn4 = new javax.swing.JButton();
        emailMessage = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        EmailTable = new javax.swing.JTable();
        jLabel56 = new javax.swing.JLabel();
        deleteLabelIcon3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        AccountsLabel = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        Reports = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        NotificationMenu = new javax.swing.JMenu();
        jCheckBoxMenuItem4 = new javax.swing.JCheckBoxMenuItem();
        Staff = new javax.swing.JMenu();
        jCheckBoxMenuItem5 = new javax.swing.JCheckBoxMenuItem();
        Configurations = new javax.swing.JMenu();
        jCheckBoxMenuItem6 = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        MenuPanel.setBackground(new java.awt.Color(203, 196, 201));
        MenuPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuPanel.setMaximumSize(null);

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel1.setText(" NSR Microsystems");

        Brandsbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        Brandsbtn.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        Brandsbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-brands-64.png"))); // NOI18N
        Brandsbtn.setText("BRANDS");
        Brandsbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Brandsbtn.setContentAreaFilled(false);
        Brandsbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Brandsbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrandsbtnActionPerformed(evt);
            }
        });

        Orderbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        Orderbtn.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        Orderbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-shopping-cart-promotion-64.png"))); // NOI18N
        Orderbtn.setText("ORDERS");
        Orderbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Orderbtn.setContentAreaFilled(false);
        Orderbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Orderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderbtnActionPerformed(evt);
            }
        });

        prdbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        prdbtn.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        prdbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-products-64.png"))); // NOI18N
        prdbtn.setText("PRODUCTS");
        prdbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        prdbtn.setContentAreaFilled(false);
        prdbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        prdbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prdbtnActionPerformed(evt);
            }
        });

        stocksbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        stocksbtn.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        stocksbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-stocks-64.png"))); // NOI18N
        stocksbtn.setText("STOCKS");
        stocksbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        stocksbtn.setContentAreaFilled(false);
        stocksbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stocksbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stocksbtnActionPerformed(evt);
            }
        });

        CategoryBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        CategoryBtn.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        CategoryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-sorting-64.png"))); // NOI18N
        CategoryBtn.setText("CATEGORIES");
        CategoryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        CategoryBtn.setContentAreaFilled(false);
        CategoryBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CategoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoryBtnActionPerformed(evt);
            }
        });

        staffButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        staffButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        staffButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-sign-out-64.png"))); // NOI18N
        staffButton.setText("Log out");
        staffButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        staffButton.setContentAreaFilled(false);
        staffButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        staffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffButtonActionPerformed(evt);
            }
        });

        jButton7.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        jButton7.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-uninstalling-updates-64.png"))); // NOI18N
        jButton7.setText("Updates");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        jButton8.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-close-64.png"))); // NOI18N
        jButton8.setText("Exit");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        usernameLabel.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        usernameLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        EmailnotificationLabel.setForeground(new java.awt.Color(102, 102, 255));
        EmailnotificationLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-email-16.png"))); // NOI18N
        EmailnotificationLabel.setText("Message");
        EmailnotificationLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        EmailnotificationLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmailnotificationLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(Orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prdbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stocksbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Brandsbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CategoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(staffButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(EmailnotificationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(EmailnotificationLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prdbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stocksbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Brandsbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staffButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CategoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(MenuPanel, java.awt.BorderLayout.PAGE_START);

        ParentLayout.setLayout(new java.awt.CardLayout());

        OrderPanel.setBackground(java.awt.Color.lightGray);

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel8.setText("Search");

        ordersTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        ordersTable.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
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

        qntlabel.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
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

        oneBtn.setBackground(new java.awt.Color(46, 144, 205));
        oneBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        oneBtn.setForeground(new java.awt.Color(240, 240, 240));
        oneBtn.setText("1");
        oneBtn.setBorder(null);
        oneBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        oneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneBtnActionPerformed(evt);
            }
        });

        twoBtn.setBackground(new java.awt.Color(46, 144, 205));
        twoBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        twoBtn.setForeground(new java.awt.Color(240, 240, 240));
        twoBtn.setText("2");
        twoBtn.setBorder(null);
        twoBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        twoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twoBtnActionPerformed(evt);
            }
        });

        threeBtn.setBackground(new java.awt.Color(46, 144, 205));
        threeBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        threeBtn.setForeground(new java.awt.Color(240, 240, 240));
        threeBtn.setText("3");
        threeBtn.setBorder(null);
        threeBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        threeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threeBtnActionPerformed(evt);
            }
        });

        fourBtn.setBackground(new java.awt.Color(46, 144, 205));
        fourBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        fourBtn.setForeground(new java.awt.Color(240, 240, 240));
        fourBtn.setText("4");
        fourBtn.setBorder(null);
        fourBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        fourBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fourBtnActionPerformed(evt);
            }
        });

        fiveBtn.setBackground(new java.awt.Color(46, 144, 205));
        fiveBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        fiveBtn.setForeground(new java.awt.Color(240, 240, 240));
        fiveBtn.setText("5");
        fiveBtn.setBorder(null);
        fiveBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        fiveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveBtnActionPerformed(evt);
            }
        });

        sixBtn.setBackground(new java.awt.Color(46, 144, 205));
        sixBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        sixBtn.setForeground(new java.awt.Color(240, 240, 240));
        sixBtn.setText("6");
        sixBtn.setBorder(null);
        sixBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        sixBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sixBtnActionPerformed(evt);
            }
        });

        sevenBtn.setBackground(new java.awt.Color(46, 144, 205));
        sevenBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        sevenBtn.setForeground(new java.awt.Color(240, 240, 240));
        sevenBtn.setText("7");
        sevenBtn.setBorder(null);
        sevenBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        sevenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sevenBtnActionPerformed(evt);
            }
        });

        eightBtn.setBackground(new java.awt.Color(46, 144, 205));
        eightBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        eightBtn.setForeground(new java.awt.Color(240, 240, 240));
        eightBtn.setText("8");
        eightBtn.setBorder(null);
        eightBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        eightBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eightBtnActionPerformed(evt);
            }
        });

        zeroBtn1.setBackground(new java.awt.Color(46, 144, 205));
        zeroBtn1.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        zeroBtn1.setForeground(new java.awt.Color(240, 240, 240));
        zeroBtn1.setText("C");
        zeroBtn1.setBorder(null);
        zeroBtn1.setMaximumSize(new java.awt.Dimension(50, 49));
        zeroBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeroBtn1ActionPerformed(evt);
            }
        });

        zeroBtn.setBackground(new java.awt.Color(46, 144, 205));
        zeroBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        zeroBtn.setForeground(new java.awt.Color(240, 240, 240));
        zeroBtn.setText("0");
        zeroBtn.setBorder(null);
        zeroBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        zeroBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeroBtnActionPerformed(evt);
            }
        });

        nineBtn.setBackground(new java.awt.Color(46, 144, 205));
        nineBtn.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        nineBtn.setForeground(new java.awt.Color(240, 240, 240));
        nineBtn.setText("9");
        nineBtn.setBorder(null);
        nineBtn.setMaximumSize(new java.awt.Dimension(50, 49));
        nineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nineBtnActionPerformed(evt);
            }
        });

        saleproductbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        saleproductbtn.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
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
        SearchProductForm.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        SearchProductForm.setRows(5);
        jScrollPane7.setViewportView(SearchProductForm);

        todays.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        todays.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        datelabel.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        datelabel.setText("Date");

        backdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backdateMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backdateMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                backdateMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                backdateMouseReleased(evt);
            }
        });
        backdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                backdatePropertyChange(evt);
            }
        });

        searchLabelIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/searchicon.png"))); // NOI18N
        searchLabelIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchLabelIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchLabelIconMouseClicked(evt);
            }
        });

        deleteLabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteicon.png"))); // NOI18N
        deleteLabelIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteLabelIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteLabelIconMouseClicked(evt);
            }
        });

        eraserLabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/erasericon.png"))); // NOI18N
        eraserLabelIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eraserLabelIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraserLabelIconMouseClicked(evt);
            }
        });

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/calendaricon.png"))); // NOI18N
        jLabel39.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel39MouseClicked(evt);
            }
        });

        saleproductbtnUpdate.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        saleproductbtnUpdate.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        saleproductbtnUpdate.setText("Update");
        saleproductbtnUpdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        saleproductbtnUpdate.setContentAreaFilled(false);
        saleproductbtnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saleproductbtnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleproductbtnUpdateActionPerformed(evt);
            }
        });

        productid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        backbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-back-arrow-32.png"))); // NOI18N
        backbutton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backbuttonMouseClicked(evt);
            }
        });

        eightBtn1.setBackground(new java.awt.Color(46, 144, 205));
        eightBtn1.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        eightBtn1.setForeground(new java.awt.Color(240, 240, 240));
        eightBtn1.setText("½");
        eightBtn1.setBorder(null);
        eightBtn1.setMaximumSize(new java.awt.Dimension(50, 49));
        eightBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eightBtn1ActionPerformed(evt);
            }
        });

        eightBtn2.setBackground(new java.awt.Color(46, 144, 205));
        eightBtn2.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        eightBtn2.setForeground(new java.awt.Color(240, 240, 240));
        eightBtn2.setText("¾");
        eightBtn2.setBorder(null);
        eightBtn2.setMaximumSize(new java.awt.Dimension(50, 49));
        eightBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eightBtn2ActionPerformed(evt);
            }
        });

        eightBtn3.setBackground(new java.awt.Color(46, 144, 205));
        eightBtn3.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        eightBtn3.setForeground(new java.awt.Color(240, 240, 240));
        eightBtn3.setText("¼");
        eightBtn3.setBorder(null);
        eightBtn3.setMaximumSize(new java.awt.Dimension(50, 49));
        eightBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eightBtn3ActionPerformed(evt);
            }
        });

        jToggleButton9.setText("Download Excell");
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });

        jLabel23.setText("jLabel23");

        javax.swing.GroupLayout OrderPanelLayout = new javax.swing.GroupLayout(OrderPanel);
        OrderPanel.setLayout(OrderPanelLayout);
        OrderPanelLayout.setHorizontalGroup(
            OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrderPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jToggleButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(backbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)
                        .addComponent(jLabel23))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1066, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1074, Short.MAX_VALUE))
                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(quantityfield, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(OrderPanelLayout.createSequentialGroup()
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(oneBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                    .addComponent(fiveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(twoBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sixBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(threeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                                    .addComponent(sevenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fourBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(eightBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(OrderPanelLayout.createSequentialGroup()
                                .addComponent(backdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(productid, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(datelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(qntlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(OrderPanelLayout.createSequentialGroup()
                                .addComponent(saleproductbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saleproductbtnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(OrderPanelLayout.createSequentialGroup()
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nineBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(eightBtn3, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(OrderPanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(zeroBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OrderPanelLayout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(zeroBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eightBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eightBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(47, Short.MAX_VALUE))
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
                        .addComponent(backdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(qntlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(eightBtn1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nineBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(eightBtn2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(zeroBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(eightBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zeroBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saleproductbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saleproductbtnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(OrderPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(OrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(searchLabelIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(eraserLabelIcon)
                                    .addComponent(deleteLabelIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jLabel39))
                            .addComponent(backbutton)
                            .addComponent(jLabel23))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton9)
                .addContainerGap(80, Short.MAX_VALUE))
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

        jToggleButton8.setText("Download Excell");
        jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton8ActionPerformed(evt);
            }
        });

        searchLabelIcon3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLabelIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/searchicon.png"))); // NOI18N
        searchLabelIcon3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchLabelIcon3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchLabelIcon3MouseClicked(evt);
            }
        });

        deleteLabelIcon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteicon.png"))); // NOI18N
        deleteLabelIcon4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteLabelIcon4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteLabelIcon4MouseClicked(evt);
            }
        });

        eraserLabelIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/erasericon.png"))); // NOI18N
        eraserLabelIcon3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eraserLabelIcon3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraserLabelIcon3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout BrandPanelLayout = new javax.swing.GroupLayout(BrandPanel);
        BrandPanel.setLayout(BrandPanelLayout);
        BrandPanelLayout.setHorizontalGroup(
            BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BrandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchLabelIcon3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteLabelIcon4)
                        .addGap(18, 18, 18)
                        .addComponent(eraserLabelIcon3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(brandname, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(insertBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        BrandPanelLayout.setVerticalGroup(
            BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BrandPanelLayout.createSequentialGroup()
                .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BrandPanelLayout.createSequentialGroup()
                        .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(BrandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(searchLabelIcon3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(eraserLabelIcon3)
                                .addComponent(deleteLabelIcon4)))
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
                        .addComponent(insertBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton8)))
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

        jToggleButton7.setText("Download Excell");
        jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton7ActionPerformed(evt);
            }
        });

        searchLabelIcon4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLabelIcon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/searchicon.png"))); // NOI18N
        searchLabelIcon4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchLabelIcon4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchLabelIcon4MouseClicked(evt);
            }
        });

        deleteLabelIcon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteicon.png"))); // NOI18N
        deleteLabelIcon5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteLabelIcon5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteLabelIcon5MouseClicked(evt);
            }
        });

        eraserLabelIcon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/erasericon.png"))); // NOI18N
        eraserLabelIcon4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eraserLabelIcon4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraserLabelIcon4MouseClicked(evt);
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
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchLabelIcon4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteLabelIcon5)
                        .addGap(18, 18, 18)
                        .addComponent(eraserLabelIcon4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(categoryname, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categorybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        CategoryPanelLayout.setVerticalGroup(
            CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryPanelLayout.createSequentialGroup()
                .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryPanelLayout.createSequentialGroup()
                        .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(searchLabelIcon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(eraserLabelIcon4)
                                .addComponent(deleteLabelIcon5)))
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
                        .addComponent(categorybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(CategoryPanel, "card2");

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

        jToggleButton5.setText("Download Excell");
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

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
        productsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsTableMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                productsTableMouseEntered(evt);
            }
        });
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

        categoriesCombo.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        categoriesCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Category *");

        jLabel18.setText("Unit of measurements");

        jLabel19.setText("Expire date (Optional)");

        retailprice.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        retailprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retailpriceActionPerformed(evt);
            }
        });

        jLabel20.setText("Retail Price");

        listprice.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        jLabel21.setText("List Price");

        eraserLabelIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/erasericon.png"))); // NOI18N
        eraserLabelIcon1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eraserLabelIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraserLabelIcon1MouseClicked(evt);
            }
        });

        deleteLabelIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteicon.png"))); // NOI18N
        deleteLabelIcon1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteLabelIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteLabelIcon1MouseClicked(evt);
            }
        });

        searchLabelIcon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLabelIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/searchicon.png"))); // NOI18N
        searchLabelIcon1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchLabelIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchLabelIcon1MouseClicked(evt);
            }
        });

        producttype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        producttype.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                producttypeItemStateChanged(evt);
            }
        });

        updateProductbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        updateProductbtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        updateProductbtn.setText("Update");
        updateProductbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        updateProductbtn.setContentAreaFilled(false);
        updateProductbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        updateProductbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateProductbtnActionPerformed(evt);
            }
        });

        hiddenProductId.setText("jTextField1");

        jToggleButton6.setText("Download Excell");
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ProductsPanelLayout = new javax.swing.GroupLayout(ProductsPanel);
        ProductsPanel.setLayout(ProductsPanelLayout);
        ProductsPanelLayout.setHorizontalGroup(
            ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1074, Short.MAX_VALUE)
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchLabelIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteLabelIcon1)
                        .addGap(18, 18, 18)
                        .addComponent(eraserLabelIcon1))
                    .addComponent(jToggleButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addComponent(categorybtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(updateProductbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productname, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(brands, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categoriesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(producttype, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ProductsPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(expiredate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(ProductsPanelLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(hiddenProductId, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(listprice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                        .addComponent(retailprice, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ProductsPanelLayout.setVerticalGroup(
            ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expiredate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addComponent(productname, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoriesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brands, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hiddenProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(producttype, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(retailprice, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(listprice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categorybtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateProductbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ProductsPanelLayout.createSequentialGroup()
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductsPanelLayout.createSequentialGroup()
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(searchLabelIcon1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(eraserLabelIcon1)
                            .addComponent(deleteLabelIcon1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton6)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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
                .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(StaffPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ProductsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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
                        .addComponent(categorybtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(StaffPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ProductsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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

        jToggleButton4.setText("Download Excell");
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

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
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalreturns, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jToggleButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(ReportsPanelLayout.createSequentialGroup()
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(profit, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                                .addComponent(profit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton4)))
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
                .addContainerGap(308, Short.MAX_VALUE))
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
        stocksTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stocksTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(stocksTable);

        jLabel22.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel22.setText("Stock");

        addstocklbl.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        addstocklbl.setText("Add Stock");
        addstocklbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addstocklbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addstocklblMouseClicked(evt);
            }
        });

        prdlabel.setText("Product");

        StockAddBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        StockAddBtn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        StockAddBtn.setText("Add");
        StockAddBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        StockAddBtn.setContentAreaFilled(false);
        StockAddBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        StockAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockAddBtnActionPerformed(evt);
            }
        });

        qtylabel.setText("Quantity");

        ProductsOnly.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        searchLabelIcon2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLabelIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/searchicon.png"))); // NOI18N
        searchLabelIcon2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchLabelIcon2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchLabelIcon2MouseClicked(evt);
            }
        });

        deleteLabelIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteicon.png"))); // NOI18N
        deleteLabelIcon2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteLabelIcon2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteLabelIcon2MouseClicked(evt);
            }
        });

        eraserLabelIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/erasericon.png"))); // NOI18N
        eraserLabelIcon2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eraserLabelIcon2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraserLabelIcon2MouseClicked(evt);
            }
        });

        LabelProduct.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelProduct.setText("jLabel41");

        jToggleButton3.setText("Download Excell");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout StockPanelLayout = new javax.swing.GroupLayout(StockPanel);
        StockPanel.setLayout(StockPanelLayout);
        StockPanelLayout.setHorizontalGroup(
            StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StockPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchLabelIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteLabelIcon2)
                        .addGap(18, 18, 18)
                        .addComponent(eraserLabelIcon2)))
                .addGap(18, 18, 18)
                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addComponent(addstocklbl, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(ProductsOnly, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(prdlabel, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(StockQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, StockPanelLayout.createSequentialGroup()
                                    .addComponent(qtylabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(LabelProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(StockAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        StockPanelLayout.setVerticalGroup(
            StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StockPanelLayout.createSequentialGroup()
                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(StockPanelLayout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StockPanelLayout.createSequentialGroup()
                                .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(searchLabelIcon2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(eraserLabelIcon2)
                                    .addComponent(deleteLabelIcon2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(StockPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addstocklbl, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(prdlabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProductsOnly, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(StockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(qtylabel)
                            .addComponent(LabelProduct))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StockQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(StockAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton3)))
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

        jButton1.setText("Download Excell");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
                        .addComponent(totalreturns1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(profit1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
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

        jLabel10.setText("Investment overall :");

        overallInvestment.setText("jButton1");
        overallInvestment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overallInvestmentActionPerformed(evt);
            }
        });

        jToggleButton2.setText("Download Excell");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MonthlyReportLayout = new javax.swing.GroupLayout(MonthlyReport);
        MonthlyReport.setLayout(MonthlyReportLayout);
        MonthlyReportLayout.setHorizontalGroup(
            MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MonthlyReportLayout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1159, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MonthlyReportLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overallInvestment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(MonthlyReportLayout.createSequentialGroup()
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
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                                .addComponent(profit2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(55, 55, 55)
                        .addGroup(MonthlyReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(overallInvestment))))
                .addGap(18, 18, 18)
                .addComponent(jToggleButton2)
                .addContainerGap(139, Short.MAX_VALUE))
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

        jToggleButton1.setText("Download Excell");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DatedReportLayout = new javax.swing.GroupLayout(DatedReport);
        DatedReport.setLayout(DatedReportLayout);
        DatedReportLayout.setHorizontalGroup(
            DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DatedReportLayout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(DatedReportLayout.createSequentialGroup()
                        .addComponent(jButton12)
                        .addGap(32, 32, 32)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 945, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addGroup(DatedReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jToggleButton1))
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
                .addContainerGap(246, Short.MAX_VALUE))
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
                        .addGap(18, 18, 18)
                        .addComponent(transferamount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(categorybtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(264, Short.MAX_VALUE))
        );

        ParentLayout.add(CashTransfers, "card2");

        Emails.setBackground(new java.awt.Color(186, 164, 164));

        categorybtn4.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        categorybtn4.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        categorybtn4.setText("Delete");
        categorybtn4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        categorybtn4.setContentAreaFilled(false);
        categorybtn4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        categorybtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorybtn4ActionPerformed(evt);
            }
        });

        emailMessage.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        emailMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailMessageActionPerformed(evt);
            }
        });
        emailMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailMessageKeyReleased(evt);
            }
        });

        jLabel54.setText("Notification Message");

        EmailTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        EmailTable.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        EmailTable.setModel(new javax.swing.table.DefaultTableModel(
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
        EmailTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmailTableMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(EmailTable);

        jLabel56.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel56.setText("Notifications Panel");

        deleteLabelIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteicon.png"))); // NOI18N
        deleteLabelIcon3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteLabelIcon3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteLabelIcon3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteLabelIcon3MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout EmailsLayout = new javax.swing.GroupLayout(Emails);
        Emails.setLayout(EmailsLayout);
        EmailsLayout.setHorizontalGroup(
            EmailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EmailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(EmailsLayout.createSequentialGroup()
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteLabelIcon3)))
                .addGap(18, 18, 18)
                .addGroup(EmailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54)
                    .addComponent(categorybtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        EmailsLayout.setVerticalGroup(
            EmailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmailsLayout.createSequentialGroup()
                .addGroup(EmailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmailsLayout.createSequentialGroup()
                        .addGroup(EmailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteLabelIcon3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmailsLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(categorybtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ParentLayout.add(Emails, "card2");

        getContentPane().add(ParentLayout, java.awt.BorderLayout.CENTER);

        File.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153), null));
        File.setText("File");
        File.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        File.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N

        jCheckBoxMenuItem1.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Home");
        jCheckBoxMenuItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        File.add(jCheckBoxMenuItem1);

        jMenuItem1.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jMenuItem1.setText("Exit");
        jMenuItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        File.add(jMenuItem1);

        jMenuBar1.add(File);

        AccountsLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));
        AccountsLabel.setText("Accounts");
        AccountsLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AccountsLabel.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N

        jMenu6.setText("Cash Transfers");
        jMenu6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu6.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jMenuItem4.setText("Record Transfer");
        jMenuItem4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem4);

        AccountsLabel.add(jMenu6);

        jMenuBar1.add(AccountsLabel);

        Reports.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));
        Reports.setText("Reports");
        Reports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Reports.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N

        jMenu3.setText("Sales Reports");
        jMenu3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu3.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jMenuItem2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem2.setText("Daily Report");
        jMenuItem2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem3.setText("Weekly Report");
        jMenuItem3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem5.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem5.setText("Monthly Report");
        jMenuItem5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenuItem6.setText("Dated Report");
        jMenuItem6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        Reports.add(jMenu3);

        jMenu5.setText("Stock Reports");
        jMenu5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu5.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jMenu5.setHideActionText(true);
        Reports.add(jMenu5);

        jMenuBar1.add(Reports);

        NotificationMenu.setText("Notifications");
        NotificationMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NotificationMenu.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        NotificationMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NotificationMenuActionPerformed(evt);
            }
        });

        jCheckBoxMenuItem4.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jCheckBoxMenuItem4.setSelected(true);
        jCheckBoxMenuItem4.setText("Product Alerts");
        jCheckBoxMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem4ActionPerformed(evt);
            }
        });
        NotificationMenu.add(jCheckBoxMenuItem4);

        jMenuBar1.add(NotificationMenu);

        Staff.setText("Staff");
        Staff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Staff.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        Staff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StaffActionPerformed(evt);
            }
        });

        jCheckBoxMenuItem5.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jCheckBoxMenuItem5.setSelected(true);
        jCheckBoxMenuItem5.setText("Details");
        jCheckBoxMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem5ActionPerformed(evt);
            }
        });
        Staff.add(jCheckBoxMenuItem5);

        jMenuBar1.add(Staff);

        Configurations.setText("Settings");
        Configurations.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Configurations.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        Configurations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfigurationsActionPerformed(evt);
            }
        });

        jCheckBoxMenuItem6.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jCheckBoxMenuItem6.setSelected(true);
        jCheckBoxMenuItem6.setText("System settings");
        jCheckBoxMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem6ActionPerformed(evt);
            }
        });
        Configurations.add(jCheckBoxMenuItem6);

        jMenuBar1.add(Configurations);

        setJMenuBar(jMenuBar1);
    }// </editor-fold>//GEN-END:initComponents

    public final void loadJtableValues() throws SQLException, ClassNotFoundException, ParseException {
        ordersModel.setRowCount(0);
        orderlist = listOrders();
        for (int i = 0; i < orderlist.size(); i++) {
            Object[] obj = {
                orderlist.get(i).orderid,
                orderlist.get(i).product,
                orderlist.get(i).quantity,
                orderlist.get(i).listprice,
                orderlist.get(i).discount};
            ordersModel.addRow(obj);
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public final void loadBrandsJtableValues() throws SQLException, ClassNotFoundException {
        brandsModel.setRowCount(0);
        brandlist = listBrands();
        allBrands = new String[brandlist.size()];
        for (int i = 0; i < brandlist.size(); i++) {
            allBrands[i] = brandlist.get(i).brand_name;
            Object[] obj = {
                brandlist.get(i).brand_id,
                brandlist.get(i).brand_name};
            brandsModel.addRow(obj);
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public final void LoadCategories() throws SQLException, ClassNotFoundException {
        categoryModel.setRowCount(0);
        categorylist = listCategories();
        allCats = new String[categorylist.size()];
        for (int i = 0; i < categorylist.size(); i++) {
            allCats[i] = categorylist.get(i).category_name;
            Object[] obj = {
                categorylist.get(i).category_id,
                categorylist.get(i).category_name};
            categoryModel.addRow(obj);
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    private void LoadProducts() throws ClassNotFoundException {
        productModel.setRowCount(0);
        try {
            productlist = listProducts();
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        nonStock = new String[productlist.size()];
        for (int i = 0; i < productlist.size(); i++) {
            nonStock[i] = productlist.get(i).product_name + " - Tsh " + productlist.get(i).list_price + " : " + productlist.get(i).productid;
            Object[] obj = {
                productlist.get(i).productid,
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
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadStockProducts() throws ClassNotFoundException {
        try {
            stockproductlist = listStockProducts();
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        allProducts = new String[stockproductlist.size()];
        for (int i = 0; i < stockproductlist.size(); i++) {
            allProducts[i] = stockproductlist.get(i).product_name + " - Tsh "
                    + stockproductlist.get(i).list_price + " : "
                    + stockproductlist.get(i).productid;
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadProductsOnly() throws SQLException, ClassNotFoundException {
        productsonly = listProductOnly();
        productonly = new String[productsonly.size()];
        for (int i = 0; i < productsonly.size(); i++) {
            productonly[i] = productsonly.get(i).product_name + " - Tsh "
                    + productsonly.get(i).list_price + " : "
                    + productsonly.get(i).productid;
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadStocks() throws ClassNotFoundException {
        stockModel.setRowCount(0);
        stocklist = listStocks();
        for (int i = 0; i < stocklist.size(); i++) {
            Object[] obj = {
                stocklist.get(i).prdcode,
                stocklist.get(i).product_id,
                stocklist.get(i).quantity
            };
            stockModel.addRow(obj);
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadDailySalesReport() throws ParseException, ClassNotFoundException {
        dailySalesModel.setRowCount(0);
        dailyreportlist = listDailyReport();

        if (dailyreportlist.isEmpty()) {

        } else {
            tinvest.setText("Tsh " + dailyreportlist.get(dailyreportlist.size() - 1).totalinvestment);
            totalreturns.setText("Tsh " + dailyreportlist.get(dailyreportlist.size() - 1).totalreturns);
            profit.setText("Tsh " + dailyreportlist.get(dailyreportlist.size() - 1).superprofit);
            for (int i = 0; i < dailyreportlist.size(); i++) {
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
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadweeklyReport() throws ParseException, ClassNotFoundException {
        weeklyModel.setRowCount(0);
        weeklyreportlist = listWeeklyReport();
        tinvest1.setText("Tsh " + weeklyreportlist.get(weeklyreportlist.size() - 1).totalinvestment);
        totalreturns1.setText("Tsh " + weeklyreportlist.get(weeklyreportlist.size() - 1).totalreturns);
        profit1.setText("Tsh " + weeklyreportlist.get(weeklyreportlist.size() - 1).superprofit);
        for (int i = 0; i < weeklyreportlist.size(); i++) {
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
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadmonthlyReport() throws ParseException, ClassNotFoundException {
        monthlyModel.setRowCount(0);
        monthlyreportlist = listMonthlyReport();
        tinvest2.setText("Tsh " + monthlyreportlist.get(monthlyreportlist.size() - 1).totalinvestment);
        totalreturns2.setText("Tsh " + monthlyreportlist.get(monthlyreportlist.size() - 1).totalreturns);
        profit2.setText("Tsh " + monthlyreportlist.get(monthlyreportlist.size() - 1).superprofit);
        for (int i = 0; i < monthlyreportlist.size(); i++) {
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
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoaddatedReport(String dt) throws ParseException, ClassNotFoundException {
        datedModel.setRowCount(0);
        datedreportlist = DatedReport(dt);
         tinvest3.setText("Tsh " + datedreportlist.get(datedreportlist.size() - 1).totalinvestment);
        totalreturns3.setText("Tsh " + datedreportlist.get(datedreportlist.size() - 1).totalreturns);
        profit3.setText("Tsh " + datedreportlist.get(datedreportlist.size() - 1).superprofit);
        for (int i = 0; i < datedreportlist.size(); i++) {
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
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadSatffs() throws ParseException, ClassNotFoundException {
        StaffsModel.setRowCount(0);
        staffslist = LoadStaffs();
        if (staffslist.isEmpty()) {

        } else {
            for (int i = 0; i < staffslist.size(); i++) {
                Object[] obj = {
                    staffslist.get(i).staff_name,
                    staffslist.get(i).sur_name,
                    staffslist.get(i).staff_email,
                    staffslist.get(i).role
                };
                StaffsModel.addRow(obj);
            }
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    public void LoadTransfers() throws ParseException, ClassNotFoundException {
        TransferModel.setRowCount(0);
        transferedlist = getTransfers();

        for (int i = 0; i < transferedlist.size(); i++) {
            Object[] obj = {
                transferedlist.get(i).amount,
                transferedlist.get(i).date,
                transferedlist.get(i).collector,
                transferedlist.get(i).total
            };
            TransferModel.addRow(obj);
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }
    
    
     public void LoadNotificationsEmails() throws ClassNotFoundException {
        emailNotifications.setRowCount(0);
        emailList = listNotifications();
        
        for (int i = 0; i < emailList.size(); i++) {
            Object[] obj = {
                emailList.get(i).notice_id,
                emailList.get(i).date,
                emailList.get(i).title,
                emailList.get(i).message
            };
            emailNotifications.addRow(obj);
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

    private void destroyAll() {
        prdbtn.setBackground(javax.swing.UIManager
                .getDefaults().getColor("Button.darkShadow"));
        Orderbtn.setBackground(javax.swing.UIManager
                .getDefaults().getColor("Button.darkShadow"));
        stocksbtn.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.darkShadow"));
        Brandsbtn.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.darkShadow"));
        CategoryBtn.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.darkShadow"));
        staffButton.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.darkShadow"));
        jButton7.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.darkShadow"));
        jButton8.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.darkShadow"));
    }

    private void activeButton(String btn) {

        ParentLayout.removeAll();
        switch (btn) {
            case "orders": {
                //Undoing all others
                destroyAll();
                Orderbtn.setContentAreaFilled(false);
                Orderbtn.setOpaque(true);
                Orderbtn.setBackground(new Color(97, 160, 204));
                ParentLayout.repaint();
                ParentLayout.revalidate();
                break;
            }
            case "products": {
                //Undoing all others
                destroyAll();
                prdbtn.setContentAreaFilled(false);
                prdbtn.setOpaque(true);
                prdbtn.setBackground(new Color(97, 160, 204));
                ParentLayout.repaint();
                ParentLayout.revalidate();
                break;
            }

            case "stocksbtn": {
                destroyAll();
                stocksbtn.setContentAreaFilled(false);
                stocksbtn.setOpaque(true);
                stocksbtn.setBackground(new Color(97, 160, 204));
                ParentLayout.repaint();
                ParentLayout.revalidate();
                break;
            }
            case "Brandsbtn": {
                destroyAll();
                Brandsbtn.setContentAreaFilled(false);
                Brandsbtn.setOpaque(true);
                Brandsbtn.setBackground(new Color(97, 160, 204));
                ParentLayout.repaint();
                ParentLayout.revalidate();
                break;
            }

            case "CategoryBtn": {
                destroyAll();
                CategoryBtn.setContentAreaFilled(false);
                CategoryBtn.setOpaque(true);
                CategoryBtn.setBackground(new Color(97, 160, 204));
                ParentLayout.repaint();
                ParentLayout.revalidate();
                break;
            }

            case "staffButton": {
                destroyAll();
                staffButton.setContentAreaFilled(false);
                staffButton.setOpaque(true);
                staffButton.setBackground(new Color(97, 160, 204));
                ParentLayout.repaint();
                ParentLayout.revalidate();
                break;
            }

            case "Config": {
                destroyAll();
                jButton8.setContentAreaFilled(false);
                jButton8.setOpaque(true);
                jButton8.setBackground(new Color(97, 160, 204));
                break;
            }

            case "reportsbtn": {
                destroyAll();
                jButton7.setContentAreaFilled(false);
                jButton7.setOpaque(true);
                jButton7.setBackground(new Color(97, 160, 204));
                break;
            }
            default:
               JOptionPane.showMessageDialog(this, "Error in your selection");
        }
    }

    private void BrandsbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrandsbtnActionPerformed
        // TODO add your handling code here:
        activeButton("Brandsbtn");
        ParentLayout.removeAll();
        ParentLayout.add(BrandPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_BrandsbtnActionPerformed

    private void OrderbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderbtnActionPerformed
        // TODO add your handling code here:
        activeButton("orders");
        ParentLayout.removeAll();
        ParentLayout.add(OrderPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
        try {
            loadJtableValues();
            LoadStockProducts();
        }
        catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).
            log(Level.SEVERE, null, ex);
        }
        
        List list = Arrays.asList(allProducts);
        AutoCompleteDecorator.decorate(SearchProductForm, list, false);
    }//GEN-LAST:event_OrderbtnActionPerformed

    private void prdbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prdbtnActionPerformed
        activeButton("products");
        try {
            // TODO add your handling code here:

            loadBrandsJtableValues();
            LoadCategories();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(allBrands);
        brands.setModel(model);

        DefaultComboBoxModel catsmodel = new DefaultComboBoxModel(allCats);
        categoriesCombo.setModel(catsmodel);
        
        AutoCompleteDecorator.decorate(brands);
        AutoCompleteDecorator.decorate(categoriesCombo);

        ParentLayout.removeAll();
        ParentLayout.add(ProductsPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_prdbtnActionPerformed

    private void stocksbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stocksbtnActionPerformed
        try {
            // TODO add your handling code here:
            LoadProductsOnly();
            LoadStocks();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultComboBoxModel prodsmodel = new DefaultComboBoxModel(productonly);
        ProductsOnly.setModel(prodsmodel);
        AutoCompleteDecorator.decorate(ProductsOnly);
        activeButton("stocksbtn");
        ParentLayout.removeAll();
        ParentLayout.add(StockPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_stocksbtnActionPerformed

    private void staffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffButtonActionPerformed
       this.dispose();
        try {
            Login();
        } catch (URISyntaxException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_staffButtonActionPerformed

    private void CategoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategoryBtnActionPerformed
        // TODO add your handling code here:
        activeButton("CategoryBtn");
        ParentLayout.removeAll();
        ParentLayout.add(CategoryPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_CategoryBtnActionPerformed

    private void quantityfieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantityfieldKeyReleased
        // TODO add your handling code here:
        quantity = quantityfield.getText();

    }//GEN-LAST:event_quantityfieldKeyReleased

    private void oneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneBtnActionPerformed
        // TODO add your handling code here:
        int i = 1;
        String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_oneBtnActionPerformed

    private void twoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twoBtnActionPerformed
        // TODO add your handling code here:

        int i = 2;
         String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_twoBtnActionPerformed

    private void threeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threeBtnActionPerformed
        // TODO add your handling code here:

       int i = 3;
       String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_threeBtnActionPerformed

    private void fourBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fourBtnActionPerformed
        // TODO add your handling code here:
        int i = 4;
        String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_fourBtnActionPerformed

    private void fiveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveBtnActionPerformed
        // TODO add your handling code here:
        int c = 5;
        String k = quantity + String.valueOf(c);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_fiveBtnActionPerformed

    private void sixBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sixBtnActionPerformed
        // TODO add your handling code here:
        int i = 6;
        String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_sixBtnActionPerformed

    private void sevenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sevenBtnActionPerformed
        // TODO add your handling code here:

       int i = 7;
       String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_sevenBtnActionPerformed

    private void eightBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eightBtnActionPerformed
        // TODO add your handling code here:

        int i = 8;
         String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_eightBtnActionPerformed

    private void zeroBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroBtn1ActionPerformed
        // TODO add your handling code here:
        quantityfield.setText("");
        quantity="0.0";
        quantity = "";
    }//GEN-LAST:event_zeroBtn1ActionPerformed

    private void zeroBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroBtnActionPerformed
        // TODO add your handling code here:
        int i = 0;
        String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_zeroBtnActionPerformed

    private void nineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nineBtnActionPerformed
        // TODO add your handling code here:
        int i = 9;
        String k = quantity + String.valueOf(i);
        quantity = k;
        quantityfield.setText(k);
    }//GEN-LAST:event_nineBtnActionPerformed

    private void saleproductbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleproductbtnActionPerformed
//        saleproductbtn.setEnabled(false);
//        saleproductbtn.setText("Wait");
//        try {
//            quantity = quantityfield.getText();
//            String order_id = unique();
//            String item_id = unique();
//            String product_id = SearchProductForm.getText();
//            Double discount = 0.00;
//            SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
//            String backdated = dcn.format(backdate.getDate());
//            Double newquantity = Double.parseDouble(quantity);
//            try {
//                if (Orders.addOrder(order_id, item_id, product_id, newquantity, discount, backdated)) {
//                    boolean stockUpdate = Stocks.editStock(product_id, quantity);
//                    if(stockUpdate == false){
//                    JOptionPane.showMessageDialog(this, "Error in Updating stock");
//                    }
//                    JOptionPane.showMessageDialog(this, "Succesfully");
//                    try {
//                        loadJtableValues();
//                    } catch (SQLException ex) {
//                        Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        saleproductbtn.setEnabled(true);
//        saleproductbtn.setText("Sale");
    }//GEN-LAST:event_saleproductbtnActionPerformed

    private void brandnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_brandnameKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_brandnameKeyReleased

    private void insertBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertBrandActionPerformed
        try {
            // TODO add your handling code here:

//        String brand_id = unique();
            String brand_name = brandname.getText();
            if (Crudes.addBrand(brand_name)) {
                JOptionPane.showMessageDialog(this, "Succesfully");
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
        activeButton("reportsbtn");
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
            if (Categories.addCategory(category_name)) {
                JOptionPane.showMessageDialog(this, "Succesfully");
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
        int brand_id = brands.getSelectedIndex() + 1;
        int category_id = categoriesCombo.getSelectedIndex() + 1;
        int model = producttype.getSelectedIndex();
        SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
        String backdated = dcn.format(expiredate.getDate());
        Double list_price = Double.parseDouble(listprice.getText());
        Double retail_price = Double.parseDouble(retailprice.getText());
        try {
            try {
                if (Products.addProduct(product_name,
                        brand_id,
                        category_id,
                        model,
                        backdated,
                        list_price,
                        retail_price
                        )) {
                    JOptionPane.showMessageDialog(this, "Succesfully");
                    LoadProducts();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        clearFields();
    }//GEN-LAST:event_categorybtn1ActionPerformed

    public void clearFields() {
        productname.setText("");
        categoriesCombo.getEditor().setItem("");
        brands.getEditor().setItem("");
        producttype.setSelectedItem(null);
        Calendar expiredate = Calendar.getInstance();
        expiredate.clear(Calendar.MONTH);
        retailprice.setText("");
        listprice.setText("");
        
        
        datelabel.setVisible(false);
        backdate.setVisible(false);
        productid.setVisible(false);
        EmailnotificationLabel.setVisible(false);
        LabelProduct.setVisible(false);
        updateProductbtn.setVisible(false);
        hiddenProductId.setVisible(false);
    }

    private void productnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productnameKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_productnameKeyReleased

    private void productnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productnameActionPerformed

    private void retailpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retailpriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_retailpriceActionPerformed

    private void StockAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockAddBtnActionPerformed

        // TODO add your handling code here:
        if ("Update".equals(StockAddBtn.getText())) {
            if (this.productId != 0) {
                try {
                    int productquantity = Integer.parseInt(StockQuantity.getText());
                    if (Stocks.updateStock(this.productId, productquantity)) {
                        JOptionPane.showMessageDialog(this, "Succesfully");
                        LoadStocks();
                        LoadProductsOnly();
                        
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            String productname = ProductsOnly.getSelectedItem().toString();
            int productquantity = Integer.parseInt(StockQuantity.getText());

            try {
                if (Stocks.addStock(productname, productquantity)) {
                    JOptionPane.showMessageDialog(this, "Succesfully");
                    LoadStocks();
                    LoadProductsOnly();
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
            
    }//GEN-LAST:event_StockAddBtnActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
       System.exit(0);
    }//GEN-LAST:event_jButton8ActionPerformed


    private void ordersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersTableMouseClicked
        // codes to get the items out of orders clicked row:
        row = ordersTable.getSelectedRow();
        column = ordersTable.getColumnCount();
        
        quantityfield.setText(ordersModel.getValueAt(row, 2).toString());
        productid.setText(ordersModel.getValueAt(row, 0).toString());
        SearchProductForm.setText(ordersModel.getValueAt(row, 1).toString());
        SearchProductForm.setLineWrap(true);
        SearchProductForm.setWrapStyleWord(true);
    }//GEN-LAST:event_ordersTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            Orders.deleteAll();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(this, "Succesfully");
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

            if (Sales_Staffs.addStaff(
                    staff_name,
                    sur_name,
                    staff_email,
                    role)) {
                JOptionPane.showMessageDialog(this, "Succesfully");
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

    public void clearUserFields() {
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

    private void categorybtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorybtn3ActionPerformed
        try {
            // TODO add your handling code here:
            String transfer_amounts;
            String dateofTransfer;
            transfer_amounts = transferamount.getText();
            if (Accounts.addTransfer(transfer_amounts)) {
                try {
                    JOptionPane.showMessageDialog(this, "Succesfully");
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
        AudioPlayer Af = new AudioPlayer();
        String a = soundSetting.getText();
        UserSettings us = new UserSettings();
        try {
            Af.Playme("success");
            if ("Play Notification Sounds".equals(a)) {
                us.soundSettings(true);
            } else {
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

            try {
                row = ordersTable.getSelectedRow();
                column = ordersTable.getColumnCount();
                int orderId = IntegerConverter(ordersModel.getValueAt(row, 0).toString());
                int quantity = IntegerConverter(ordersModel.getValueAt(row, 2).toString());

                Orders.deleteRow(orderId, quantity);
                ordersModel.removeRow(row);
                orderlist.remove(row);
                ordersModel.setRowCount(0);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName())
                        .log(Level.SEVERE, null, ex);
            }

            for (int i = 0; i < orderlist.size(); i++) {
                Object[] obj = {
                    orderlist.get(i).orderid,
                    orderlist.get(i).product,
                    orderlist.get(i).quantity,
                    orderlist.get(i).listprice,
                    orderlist.get(i).discount
                };
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
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
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
        
    }//GEN-LAST:event_quantityfieldMouseExited

    private void saleproductbtnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleproductbtnUpdateActionPerformed
        // Updating the values of order
        // This is conditional to users (administrator's) configurations]]
     /*   saleproductbtnUpdate.setEnabled(false);
        saleproductbtnUpdate.setText("Wait");

        try {
            quantity = quantityfield.getText();
            int order_id = IntegerConverter(productid.getText());
            String item_id = unique();
            String product_id = SearchProductForm.getText();
            Double price = 1.0;
            Double discount = 0.00;
            Double newquantity = Double.parseDouble(quantity);
            SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
            String backdated = dcn.format(backdate.getDate());
            try {
                Stocks.editStockFromOrdersEd(order_id, product_id, quantity);
//                if (Orders.updateOrder(order_id, item_id, product_id, newquantity, price, discount, backdated)) {
                    try {
                        loadJtableValues();
                    } catch (SQLException ex) {
                        Logger.getLogger(UserInterface.class.getName())
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

    private void deleteLabelIcon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteLabelIcon1MouseClicked
        // Button to delete the specific product

        //Delete sales.
        int reply =
        JOptionPane.showConfirmDialog(this, "Are you sure?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            row = productsTable.getSelectedRow();
            column = productsTable.getColumnCount();
            int productId = IntegerConverter(productModel.getValueAt(row, 0).toString());
            try {
                Products.deleteProduct(productId);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            productModel.removeRow(row);
            productlist.remove(row);
            productModel.setRowCount(0);

            for (int i = 0; i < productlist.size(); i++) {
                Object[] obj = {
                    productlist.get(i).productid,
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
            ParentLayout.repaint();
            ParentLayout.revalidate();
        }
    }//GEN-LAST:event_deleteLabelIcon1MouseClicked

    private void searchLabelIcon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchLabelIcon1MouseClicked
        // Product search button:
        //Search engine.
        row = productsTable.getSelectedRow();
        column = productsTable.getColumnCount();
        //Row sorters
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(productModel);
        productsTable.setRowSorter(sorter);
        //End of row sorters
        String input = JOptionPane.showInputDialog(this, "Search row");
        if (input.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(input));
        }
    }//GEN-LAST:event_searchLabelIcon1MouseClicked

    private void producttypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_producttypeItemStateChanged
        //If the item is changed.
        int v = producttype.getSelectedIndex();
    }//GEN-LAST:event_producttypeItemStateChanged

    private void categorybtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorybtn4ActionPerformed
        try {
            // Delete emails
            Boolean deleteEmails = Notifications.deleteEmails();
            if(deleteEmails){
                 JOptionPane.showMessageDialog(this, "Succesfully");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_categorybtn4ActionPerformed

    private void emailMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailMessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailMessageActionPerformed

    private void emailMessageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailMessageKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_emailMessageKeyReleased

    private void EmailnotificationLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmailnotificationLabelMouseClicked
        ParentLayout.removeAll();
        ParentLayout.add(Emails);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_EmailnotificationLabelMouseClicked

    private void searchLabelIcon2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchLabelIcon2MouseClicked
        // TODO add your handling code here:
            //Search engine.
        row = stocksTable.getSelectedRow();
        column = stocksTable.getColumnCount();
        //Row sorters
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(stockModel);
        stocksTable.setRowSorter(sorter);
        //End of row sorters
        String input = JOptionPane.showInputDialog(this, "Search row");
        if (input.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(input));
        }
    }//GEN-LAST:event_searchLabelIcon2MouseClicked

    private void deleteLabelIcon2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteLabelIcon2MouseClicked
        // Stocks delete button
    }//GEN-LAST:event_deleteLabelIcon2MouseClicked

    private void eraserLabelIcon2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraserLabelIcon2MouseClicked
       
          try {
            // TODO add your handling code here:
            this.stocksTable.setAutoCreateRowSorter(false);
            this.stocksTable.setAutoCreateRowSorter(true);
            this.stocksTable.repaint();
            ProductsOnly.setSelectedItem("");
            StockQuantity.setText("");
            LoadProductsOnly();
        } catch (SQLException | ClassNotFoundException  ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }//GEN-LAST:event_eraserLabelIcon2MouseClicked

    private void stocksTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stocksTableMouseClicked
        // this is when you want to edit the values from the stocks table
        row = stocksTable.getSelectedRow();
        column = stocksTable.getColumnCount();
        ProductsOnly.setVisible(false);
        prdlabel.setVisible(false);
        addstocklbl.setText("Close");
        qtylabel.setText("Quantity for");
        StockAddBtn.setText("Update");
        String y = stockModel.getValueAt(row, 0).toString();
        int x = IntegerConverter(y);
        this.productId = x;
        LabelProduct.setVisible(true);
        LabelProduct.setText(stockModel.getValueAt(row, 1).toString());
        StockQuantity.setText(stockModel.getValueAt(row, 2).toString());
    }//GEN-LAST:event_stocksTableMouseClicked

    private void addstocklblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addstocklblMouseClicked
        // TODO add your handling code here:
        clearStockSelection();
    }//GEN-LAST:event_addstocklblMouseClicked

    private void backdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backdateMouseClicked
        //TODO add your handling code here:
        SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
        String backdated = dcn.format(backdate.getDate());
        datelabel.setText(backdated);
    }//GEN-LAST:event_backdateMouseClicked

    private void backdateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backdateMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_backdateMouseExited

    private void backdatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_backdatePropertyChange
              
    }//GEN-LAST:event_backdatePropertyChange

    private void backdateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backdateMouseReleased
        // TODO add your handling code here:
        SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
        String backdated = dcn.format(backdate.getDate());
        activeButton("orders");
        ParentLayout.removeAll();
        ParentLayout.add(OrderPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
        try {
            loadJtableValuesOutDated(backdated);
            LoadStockProducts();
        }
        catch (ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).
            log(Level.SEVERE, null, ex);
        }
        List list = Arrays.asList(allProducts);
        AutoCompleteDecorator.decorate(SearchProductForm, list, false);
    }//GEN-LAST:event_backdateMouseReleased

    private void backbuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backbuttonMouseClicked
        // Here users is displayed with prior orders
        
        SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
        String backdated = dcn.format(backdate.getDate());
        datelabel.setText(backdated);
        activeButton("orders");
        ParentLayout.removeAll();
        ParentLayout.add(OrderPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
        try {
            loadJtableValuesOutDated(backdated);
            LoadStockProducts();
        }
        catch (ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).
            log(Level.SEVERE, null, ex);
        }
        List list = Arrays.asList(allProducts);
        AutoCompleteDecorator.decorate(SearchProductForm, list, false);
    }//GEN-LAST:event_backbuttonMouseClicked

    private void backdateMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backdateMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_backdateMousePressed

    private void productsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTableMouseClicked
        // Products table mouse clicked:
        row = productsTable.getSelectedRow();
        column = productsTable.getColumnCount();
        updateProductbtn.setVisible(true);
        hiddenProductId.setText(productModel.getValueAt(row, 0).toString());
        productname.setText(productModel.getValueAt(row, 1).toString());
        
        brands.setSelectedItem(productModel.getValueAt(row,2).toString());
        brands.updateUI();
        categoriesCombo.setSelectedItem(productModel.getValueAt(row,3).toString());
        categoriesCombo.updateUI();
        producttype.setSelectedItem(productModel.getValueAt(row,4).toString());
        producttype.updateUI();
        this.repaint();
        this.revalidate();
        String j = productModel.getValueAt(row,5).toString();
        categoriesCombo.setSelectedItem("kiporo");
        categoriesCombo.updateUI();
        expiredate.updateUI();
        retailprice.setText(productModel.getValueAt(row,7).toString());
        listprice.setText(productModel.getValueAt(row,6).toString());
        categoriesCombo.setSelectedIndex(1);
        
    }//GEN-LAST:event_productsTableMouseClicked

    private void overallInvestmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overallInvestmentActionPerformed
        try {
            // TODO add your handling code here:
            overallInvestment.setText(""+Summaryeport());
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_overallInvestmentActionPerformed

    private void updateProductbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateProductbtnActionPerformed
        // TODO add your handling code here:
        //Update the product details
        String product_id = hiddenProductId.getText();
        String product_name = productname.getText();
        int brand_id = brands.getSelectedIndex() + 1;
        int category_id = categoriesCombo.getSelectedIndex() + 1;
        int model = producttype.getSelectedIndex();
        SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
        String backdated = dcn.format(expiredate.getDate());
        Double list_price = Double.parseDouble(listprice.getText());
        Double retail_price = Double.parseDouble(retailprice.getText());
        try {
            try {
                if (Products.editProduct(
                        product_id,
                        product_name,
                        brand_id,
                        category_id,
                        model,
                        backdated,
                        list_price,
                        retail_price
                        )) {
                    JOptionPane.showMessageDialog(this, "Succesfully");
                    LoadProducts();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearFields();
    }//GEN-LAST:event_updateProductbtnActionPerformed

    private void productsTableMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTableMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_productsTableMouseEntered

    private void eightBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eightBtn1ActionPerformed
        // TODO add your handling code here:
        double i = 0.5;
        if("".equals(quantity)){
        String k = "" + i;
        quantity = k;
        quantityfield.setText(k);
        }
        else {
        String r = "" + (Double.parseDouble(quantity) +i);
        quantity = r;
        quantityfield.setText(r);
        }
            
        
    }//GEN-LAST:event_eightBtn1ActionPerformed

    private void eightBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eightBtn2ActionPerformed
        // TODO add your handling code here:
           // TODO add your handling code here:
        double i = 0.75;
        if("".equals(quantity)){
        String k = "" + i;
        quantity = k;
        quantityfield.setText(k);
        }
        else {
        String r = "" + (Double.parseDouble(quantity) +i);
        quantity = r;
        quantityfield.setText(r);
        }
    }//GEN-LAST:event_eightBtn2ActionPerformed

    private void eightBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eightBtn3ActionPerformed
        // TODO add your handling code here:
        double i = 0.25;
           if("".equals(quantity)){
        String k = "" + i;
        quantity = k;
        quantityfield.setText(k);
        }
        else {
        String r = "" + (Double.parseDouble(quantity) +i);
        quantity = r;
        quantityfield.setText(r);
        }
    }//GEN-LAST:event_eightBtn3ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // Button that downloads the excell:
        ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(monthlyreporttable, new File(file));
           }
        
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Download Excell format :
          // Button that downloads the excell:
        ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(weeklyreporttable, new File(file));
           }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        //Download excell in stocks report:
          ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(stocksTable, new File(file));
           }
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // TODO add your handling code here:
          ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(dailySalesTable, new File(file));
           }
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        // TODO add your handling code here:
          ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(staffTable, new File(file));
           }
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
          // TODO add your handling code here:
          ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(productsTable, new File(file));
           }
        
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        //download categoryTable
         ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(categoryTable, new File(file));
           }
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed
        // Download excell :
           ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(brandsTable, new File(file));
           }
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        // Download :
          ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(ordersTable, new File(file));
           }
        
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // Dated report excell :
          ExcelFormat exft = new ExcelFormat();
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(UserInterface.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             exft.export(datedreporttable, new File(file));
           }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void EmailTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmailTableMouseClicked
        // Email table mouse click even:
        // codes to get the items out of orders clicked row:
        row = EmailTable.getSelectedRow();
        column = EmailTable.getColumnCount();
        emailMessage.setText(emailNotifications.getValueAt(row, 3).toString());
    }//GEN-LAST:event_EmailTableMouseClicked

    private void NotificationMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NotificationMenuActionPerformed

        ParentLayout.removeAll();
        ParentLayout.add(Emails);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_NotificationMenuActionPerformed

    private void jCheckBoxMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem4ActionPerformed
        // TODO add your handling code here:
        ParentLayout.removeAll();
        ParentLayout.add(Emails);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_jCheckBoxMenuItem4ActionPerformed

    private void deleteLabelIcon3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteLabelIcon3MouseClicked
        // Emails mouse click event
         //Delete sales.
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            row = EmailTable.getSelectedRow();
            column = EmailTable.getColumnCount();
            int notice_id = IntegerConverter(emailNotifications.getValueAt(row, 0).toString());
            int quantity = IntegerConverter(emailNotifications.getValueAt(row, 2).toString());
            try {
                if(Notifications.deleteRow(notice_id) == false){
                    JOptionPane.showMessageDialog(this, "Error in deleting");
                    }
                    JOptionPane.showMessageDialog(this, "Succesfully");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            emailNotifications.removeRow(row);
            emailList.remove(row);
            emailNotifications.setRowCount(0);

            for (int i = 0; i < emailList.size(); i++) {
                Object[] obj = {
                emailList.get(i).notice_id,
                emailList.get(i).date,
                emailList.get(i).title,
                emailList.get(i).message
                };
                emailNotifications.addRow(obj);
            }
        }
    }//GEN-LAST:event_deleteLabelIcon3MouseClicked

    private void deleteLabelIcon3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteLabelIcon3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteLabelIcon3MouseEntered

    private void searchLabelIcon3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchLabelIcon3MouseClicked
        // Brands search button
        
        
        //Search engine.
        row = brandsTable.getSelectedRow();
        column = brandsTable.getColumnCount();
        //Row sorters
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(brandsModel);
        brandsTable.setRowSorter(sorter);
        //End of row sorters
        String input = JOptionPane.showInputDialog(this, "Search row");
        if (input.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(input));
        }
    }//GEN-LAST:event_searchLabelIcon3MouseClicked

    private void deleteLabelIcon4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteLabelIcon4MouseClicked
        // TODO add your handling code here:
                //Delete sales.
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            row = brandsTable.getSelectedRow();
            column = brandsTable.getColumnCount();
            int brandId = IntegerConverter(brandsModel.getValueAt(row, 0).toString());
            try {
                Crudes.deleteRow(brandId);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            brandsModel.removeRow(row);
            brandlist.remove(row);
            brandsModel.setRowCount(0);

            for (int i = 0; i < brandlist.size(); i++) {
                Object[] obj = {
                    brandlist.get(i).brand_id,
                    brandlist.get(i).brand_name
                };
                brandsModel.addRow(obj);
            }
        }

    }//GEN-LAST:event_deleteLabelIcon4MouseClicked

    private void eraserLabelIcon3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraserLabelIcon3MouseClicked
        // TODO add your handling code here:
           try {
            // TODO add your handling code here:
            this.brandsTable.setAutoCreateRowSorter(false);
            this.brandsTable.setAutoCreateRowSorter(true);
            this.brandsTable.repaint();
            brandname.setText("");
            loadJtableValues();
            LoadStockProducts();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_eraserLabelIcon3MouseClicked

    private void searchLabelIcon4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchLabelIcon4MouseClicked
        // Search for category:
         
        //Search engine.
        row = categoryTable.getSelectedRow();
        column = categoryTable.getColumnCount();
        //Row sorters
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(categoryModel);
        categoryTable.setRowSorter(sorter);
        //End of row sorters
        String input = JOptionPane.showInputDialog(this, "Search row");
        if (input.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(input));
        }
    }//GEN-LAST:event_searchLabelIcon4MouseClicked

    private void deleteLabelIcon5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteLabelIcon5MouseClicked
        // TODO add your handling code here:
                 //Delete sales.
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            row = categoryTable.getSelectedRow();
            column = categoryTable.getColumnCount();
            int categoryId = IntegerConverter(categoryModel.getValueAt(row, 0).toString());
            try {
                Categories.deleteCategory(categoryId);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            categoryModel.removeRow(row);
            categorylist.remove(row);
            categoryModel.setRowCount(0);

            for (int i = 0; i < brandlist.size(); i++) {
                Object[] obj = {
                    categorylist.get(i).category_id,
                    categorylist.get(i).category_name
                };
                categoryModel.addRow(obj);
            }
        }
    }//GEN-LAST:event_deleteLabelIcon5MouseClicked

    private void eraserLabelIcon4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraserLabelIcon4MouseClicked
            // Clear fields:
            try {
            // TODO add your handling code here:
            this.categoryTable.setAutoCreateRowSorter(false);
            this.categoryTable.setAutoCreateRowSorter(true);
            this.categoryTable.repaint();
            categoryname.setText("");
            loadJtableValues();
            LoadStockProducts();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_eraserLabelIcon4MouseClicked

    private void jCheckBoxMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem5ActionPerformed
           activeButton("staffButton");
        try {
            // TODO add your handling code here:
            ParentLayout.removeAll();
            ParentLayout.add(StaffPanel);
            ParentLayout.repaint();
            ParentLayout.revalidate();
            LoadSatffs();
        } catch (ParseException | ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jCheckBoxMenuItem5ActionPerformed

    private void StaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StaffActionPerformed

    private void jCheckBoxMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem6ActionPerformed
        // Configurations label
              // TODO add your handling code here:
        activeButton("Config");
        ParentLayout.removeAll();
        ParentLayout.add(ConfigPanel);
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }//GEN-LAST:event_jCheckBoxMenuItem6ActionPerformed

    private void ConfigurationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfigurationsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConfigurationsActionPerformed

    private void eraserLabelIcon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraserLabelIcon1MouseClicked
        // Button that clears the search filter
        try {
            // TODO add your handling code here:
            this.productsTable.setAutoCreateRowSorter(false);
            this.productsTable.setAutoCreateRowSorter(true);
            this.productsTable.repaint();
            clearFields();
            LoadProducts();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        */
    }//GEN-LAST:event_eraserLabelIcon1MouseClicked

    public void clearStockSelection(){
        ProductsOnly.setVisible(true);
        prdlabel.setVisible(true);
        addstocklbl.setText("Add Stock");
        qtylabel.setText("Quantity");
        StockAddBtn.setText("Save");
        LabelProduct.setVisible(false);
        StockQuantity.setText("");
    }
    
    public static void PlayNotification(String type) {
        AudioPlayer Af = new AudioPlayer();
        try {
            Af.Playme(type);
        } catch (URISyntaxException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void setEmailNotification(String message) throws ParseException {
        EmailnotificationLabel.setVisible(true);
        EmailnotificationLabel.setText(message);
        //Refresh the page
        refresh();
    }
    
     public static void refresh() throws ParseException {
        if (Instance != null) {
            Instance.MenuPanel.repaint();
            Instance.MenuPanel.revalidate();
            try {
                Instance = new UserInterface();
            } catch (ClassNotFoundException | URISyntaxException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
     
      private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    
    private File getFileFromResource(String fileName) throws URISyntaxException{

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }

    }
    

    public static void UserIntfc() throws ClassNotFoundException, UnsupportedLookAndFeelException {
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
        
        
        java.awt.EventQueue.invokeLater(() -> {
            try {
                Instance  = new UserInterface();
                Instance.setVisible(true);
            } catch (URISyntaxException | ClassNotFoundException | ParseException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu AccountsLabel;
    private javax.swing.JPanel BrandPanel;
    private javax.swing.JButton Brandsbtn;
    private javax.swing.JPanel CashTransfers;
    private javax.swing.JButton CategoryBtn;
    private javax.swing.JPanel CategoryPanel;
    private javax.swing.JPanel ConfigPanel;
    private javax.swing.JMenu Configurations;
    private javax.swing.JPanel DatedReport;
    private javax.swing.JTable EmailTable;
    public static javax.swing.JLabel EmailnotificationLabel;
    private javax.swing.JPanel Emails;
    private javax.swing.JMenu File;
    private javax.swing.JLabel LabelProduct;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JPanel MonthlyReport;
    private javax.swing.JMenu NotificationMenu;
    private javax.swing.JPanel OrderPanel;
    private javax.swing.JButton Orderbtn;
    private javax.swing.JPanel ParentLayout;
    private javax.swing.JComboBox<String> ProductsOnly;
    private javax.swing.JPanel ProductsPanel;
    private javax.swing.JMenu Reports;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JTextArea SearchProductForm;
    private javax.swing.JMenu Staff;
    private javax.swing.JPanel StaffPanel;
    private javax.swing.JButton StockAddBtn;
    private javax.swing.JPanel StockPanel;
    private javax.swing.JTextField StockQuantity;
    private javax.swing.JPanel WeeklyReport;
    private javax.swing.JLabel addstocklbl;
    private javax.swing.JLabel backbutton;
    private com.toedter.calendar.JCalendar backdate;
    private javax.swing.JTextField brandname;
    private javax.swing.JComboBox<String> brands;
    private javax.swing.JTable brandsTable;
    private javax.swing.JComboBox<String> categoriesCombo;
    private javax.swing.JTable categoryTable;
    private javax.swing.JButton categorybtn;
    private javax.swing.JButton categorybtn1;
    private javax.swing.JButton categorybtn2;
    private javax.swing.JButton categorybtn3;
    private javax.swing.JButton categorybtn4;
    private javax.swing.JTextField categoryname;
    private javax.swing.JTable dailySalesTable;
    private com.toedter.calendar.JCalendar datedrep;
    private javax.swing.JTable datedreporttable;
    private javax.swing.JLabel datelabel;
    private javax.swing.JLabel deleteLabelIcon;
    private javax.swing.JLabel deleteLabelIcon1;
    private javax.swing.JLabel deleteLabelIcon2;
    private javax.swing.JLabel deleteLabelIcon3;
    private javax.swing.JLabel deleteLabelIcon4;
    private javax.swing.JLabel deleteLabelIcon5;
    private javax.swing.JButton eightBtn;
    private javax.swing.JButton eightBtn1;
    private javax.swing.JButton eightBtn2;
    private javax.swing.JButton eightBtn3;
    private javax.swing.JTextField email;
    private javax.swing.JTextField emailMessage;
    private javax.swing.JLabel eraserLabelIcon;
    private javax.swing.JLabel eraserLabelIcon1;
    private javax.swing.JLabel eraserLabelIcon2;
    private javax.swing.JLabel eraserLabelIcon3;
    private javax.swing.JLabel eraserLabelIcon4;
    private com.toedter.calendar.JCalendar expiredate;
    private javax.swing.JButton fiveBtn;
    private javax.swing.JButton fourBtn;
    private javax.swing.JTextField hiddenProductId;
    private javax.swing.JButton insertBrand;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem4;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem6;
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
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    private javax.swing.JTextField listprice;
    private javax.swing.JTable monthlyreporttable;
    private javax.swing.JButton nineBtn;
    private javax.swing.JButton oneBtn;
    private javax.swing.JTable ordersTable;
    private javax.swing.JButton overallInvestment;
    private javax.swing.JButton prdbtn;
    private javax.swing.JLabel prdlabel;
    private javax.swing.JTextField productid;
    private javax.swing.JTextField productname;
    private javax.swing.JTable productsTable;
    private javax.swing.JComboBox<String> producttype;
    private javax.swing.JLabel profit;
    private javax.swing.JLabel profit1;
    private javax.swing.JLabel profit2;
    private javax.swing.JLabel profit3;
    private javax.swing.JLabel qntlabel;
    private javax.swing.JLabel qtylabel;
    private javax.swing.JTextField quantityfield;
    private javax.swing.JTextField retailprice;
    private javax.swing.JButton saleproductbtn;
    private javax.swing.JButton saleproductbtnUpdate;
    private javax.swing.JLabel searchLabelIcon;
    private javax.swing.JLabel searchLabelIcon1;
    private javax.swing.JLabel searchLabelIcon2;
    private javax.swing.JLabel searchLabelIcon3;
    private javax.swing.JLabel searchLabelIcon4;
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
    private javax.swing.JButton updateProductbtn;
    public static javax.swing.JLabel usernameLabel;
    private javax.swing.JTable weeklyreporttable;
    private javax.swing.JButton zeroBtn;
    private javax.swing.JButton zeroBtn1;
    // End of variables declaration//GEN-END:variables

    private void loadJtableValuesOutDated(String backdated)
            throws ParseException, ClassNotFoundException {
        ordersModel.setRowCount(0);
        orderlist = listOrdersDated(backdated);
        for (int i = 0; i < orderlist.size(); i++) {
            Object[] obj = {
                orderlist.get(i).orderid,
                orderlist.get(i).product,
                orderlist.get(i).quantity,
                orderlist.get(i).listprice,
                orderlist.get(i).discount};
            ordersModel.addRow(obj);
        }
        ParentLayout.repaint();
        ParentLayout.revalidate();
    }

}
