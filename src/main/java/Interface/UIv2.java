/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import static Authentication.Sessions.LoggedUser;
import Classes.AbstractClasses.Brands;
import Classes.AbstractClasses.Category;
import Classes.AbstractClasses.DailyReport;
import Classes.AbstractClasses.Email;
import Classes.AbstractClasses.Order;
import Classes.AbstractClasses.Product;
import Classes.AbstractClasses.Staff;
import Classes.AbstractClasses.Stock;
import Classes.AbstractClasses.Transfer;
import static Classes.Functions.Accounts.getTransfers;
import static Classes.Functions.Categories.listBrands;
import static Classes.Functions.Categories.listCategories;
import static Classes.Functions.Notifications.listNotifications;
import Classes.Functions.Orders;
import static Classes.Functions.Orders.listOrders;
import static Classes.Functions.Products.listProductOnly;
import static Classes.Functions.Products.listProducts;
import static Classes.Functions.Products.listStockProducts;
import static Classes.Functions.Reports.DatedReport;
import static Classes.Functions.Reports.listDailyReport;
import static Classes.Functions.Reports.listMonthlyReport;
import static Classes.Functions.Reports.listWeeklyReport;
import static Classes.Functions.Sales_Staffs.LoadStaffs;
import static Classes.Functions.Stocks.listStocks;
import Classes.Utilities.ExcelFormat;
import Classes.Utilities.Resources;
import Classes.Utilities.StockThread;
import Interface.Sales.EditSale;
import Interface.Sales.ReturnProduct;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import static com.nkanabo.Tienda.Utilities.DoubleConverter;
import static com.nkanabo.Tienda.Utilities.IntegerConverter;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import Interface.Products.*;

/*
 *
 * @author Nkanabo
 */
public final class UIv2 extends javax.swing.JFrame {

    static UIv2 Instance;

    int productId;

    //Brands
    ArrayList<Brands> brandlist;
    String brandheaders[] = {"Brand Id", "Brand Name"};
    DefaultTableModel brandsModel;

    ArrayList<Order> PresentsalesList;
    Object[] columnNames  = {"Select","Order Id", "Product", "Quantity", "List price", "Discount"};
    DefaultTableModel SellingModel = new DefaultTableModel(0, 0){
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
    // add header of the table
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
    String stockheader[] = {"Product Id", "Product", "Quantity"};
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
    String emailHeader[] = {"Count", "Sent Date", "Subject", "Message"};
    ArrayList<Email> emailList;

    public String quantity = "";
    int row, column;
    String[] allBrands;
    String[] allCats;
    String[] allProducts;
    String[] nonStock;
    String[] productonly;
    public ArrayList<String> prd_details = new ArrayList<String>();
     private TableColumn col;

    /**
     * Creates new form UIv2
     *
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
     */
    public UIv2() throws ClassNotFoundException, ParseException {
        FlatGitHubIJTheme.setup();
        initComponents();
        //<editor-fold defaultstate="collapsed" desc="comment">

        //</editor-fold>
        //The below command makes the frame appear at the center of any screen
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setExtendedState(getExtendedState() | UIv2.MAXIMIZED_BOTH);
        StockThread th = new StockThread();
        if (Instance != null) {
            try {
                Instance = new UIv2();
                String url = "resources/images/icons8.jpg";
                Resources rs = new Resources();
                File is = rs.getFileFromResource(url);

                String filepath = Paths.get(is.toURI()).toFile().getAbsolutePath();
                ImageIcon icon = new ImageIcon(filepath);
                setIconImage(icon.getImage());

            } catch (URISyntaxException ex) {
                Logger.getLogger(launcher.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Orders list 
            PresentsalesList = new ArrayList<>();
        }

        usernameLabel.setText(LoggedUser);
        String today = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        todays.setText(String.valueOf(Calendar.getInstance().getTime()));

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

        // add header in table model     
        SellingModel.setColumnIdentifiers(columnNames);
        SalesTable.setModel(SellingModel);
        
          //get the 2nd column
        col = SalesTable.getColumnModel().getColumn(3);
        //define the renderer
        //The preferred blue 102,102,102
        col.setCellRenderer(new MyRenderer(new Color (255,255,255), new Color (0,102,51)));
        //col.setFont(col.getFont().deriveFont(Font.BOLD, 14f));
//        col.setCellRenderer(setFont(new Font("Arial", Font.BOLD, 10)));
      
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
        } catch (SQLException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
        

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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
    }

    public final void LoadProducts() throws ClassNotFoundException {
        productModel.setRowCount(0);
        try {
            productlist = listProducts();
        } catch (SQLException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
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
        TablePanel.repaint();
        TablePanel.revalidate();
    }

    public void LoadStockProducts() throws ClassNotFoundException {
        try {
            stockproductlist = listStockProducts();
        } catch (SQLException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        allProducts = new String[stockproductlist.size()];
        for (int i = 0; i < stockproductlist.size(); i++) {
            allProducts[i] = stockproductlist.get(i).product_name + " - Tsh "
                    + stockproductlist.get(i).list_price + " : "
                    + stockproductlist.get(i).productid;
        }
        TablePanel.repaint();
        TablePanel.revalidate();
    }

    public void LoadProductsOnly() throws SQLException, ClassNotFoundException {
        productsonly = listProductOnly();
        productonly = new String[productsonly.size()];
        for (int i = 0; i < productsonly.size(); i++) {
            productonly[i] = productsonly.get(i).product_name + " - Tsh "
                    + productsonly.get(i).list_price + " : "
                    + productsonly.get(i).productid;
        }
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
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
        TablePanel.repaint();
        TablePanel.revalidate();
    }

    public static void setEmailNotification(String message) throws ParseException {
        SetEmailNotification.setVisible(true);
        SetEmailNotification.setText(message);
    }

    public void getActiveClass(String label) {
        switch (label) {
            case "saleslabel":
                saleslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-best-sales-64 (1).png"))); // NOI18N
                break;
            case "box":
                box.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-box-64Blue.png")));
                break;
            case "stats":
                stats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-statistics-64Blue.png")));
                break;
            case "admin":
                admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-administrator-male-64.png")));
                break;
            case "customers":
                customers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-select-users-64Blue.png"))); // NOI18N
                break;

            default:
                saleslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-best-sales-64 (1).png"))); // NOI18N
        }
    }

    /**
     * End of loading functions
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LeftPanel = new javax.swing.JPanel();
        menu = new javax.swing.JPanel();
        Submenu = new javax.swing.JPanel();
        SalesHeader = new javax.swing.JPanel();
        newsaleLabel = new javax.swing.JLabel();
        OrderLabel = new javax.swing.JLabel();
        EditLabel = new javax.swing.JLabel();
        ExchangeLabel = new javax.swing.JLabel();
        CancelLabel = new javax.swing.JLabel();
        PrintReceipt = new javax.swing.JLabel();
        ProductsHeader = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        InfoPanel = new javax.swing.JPanel();
        SetEmailNotification = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        Sidabar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        stats = new javax.swing.JLabel();
        saleslabel = new javax.swing.JLabel();
        box = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        customers = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        admin = new javax.swing.JLabel();
        Submenu1 = new javax.swing.JPanel();
        todays = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        TablePanel = new javax.swing.JPanel();
        SalesTablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SalesTable = new javax.swing.JTable();
        BrandsTablePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        brandsTable = new javax.swing.JTable();
        categoryTablePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        categoryTable = new javax.swing.JTable();
        ProductsTablePanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        StocksTablePanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        stocksTable = new javax.swing.JTable();
        DailyReportTablePanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        dailySalesTable = new javax.swing.JTable();
        tinvest = new javax.swing.JLabel();
        totalreturns = new javax.swing.JLabel();
        profit = new javax.swing.JLabel();
        WeeklyReportTablePanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        weeklyreporttable = new javax.swing.JTable();
        tinvest1 = new javax.swing.JLabel();
        totalreturns1 = new javax.swing.JLabel();
        profit1 = new javax.swing.JLabel();
        MonthlyReportTablePanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        monthlyreporttable = new javax.swing.JTable();
        tinvest2 = new javax.swing.JLabel();
        totalreturns2 = new javax.swing.JLabel();
        profit2 = new javax.swing.JLabel();
        DateReportTablePanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        datedreporttable = new javax.swing.JTable();
        tinvest3 = new javax.swing.JLabel();
        totalreturns3 = new javax.swing.JLabel();
        profit3 = new javax.swing.JLabel();
        UsersTablePanel = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        staffTable = new javax.swing.JTable();
        NotificationsTablePanel = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        EmailTable = new javax.swing.JTable();
        CashTablePanel = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        transferedCash = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        TopMenu = new javax.swing.JMenu();
        QuitMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        LeftPanel.setLayout(new java.awt.CardLayout());

        menu.setBackground(new java.awt.Color(204, 204, 204));
        menu.setForeground(new java.awt.Color(153, 255, 255));

        Submenu.setBackground(java.awt.Color.white);
        Submenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        Submenu.setForeground(new java.awt.Color(153, 153, 153));
        Submenu.setLayout(new java.awt.CardLayout());

        SalesHeader.setBackground(java.awt.Color.white);
        SalesHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        SalesHeader.setForeground(new java.awt.Color(153, 153, 153));

        newsaleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newsaleLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-cash-register-32.png"))); // NOI18N
        newsaleLabel.setText("New Sale - POS (F3)");
        newsaleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newsaleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newsaleLabelMouseClicked(evt);
            }
        });

        OrderLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        OrderLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-purchase-order-32.png"))); // NOI18N
        OrderLabel.setText("Order (F4)");
        OrderLabel.setEnabled(false);

        EditLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EditLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-edit-32.png"))); // NOI18N
        EditLabel.setText("Edit");
        EditLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        EditLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EditLabelMouseClicked(evt);
            }
        });

        ExchangeLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ExchangeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-buying-32.png"))); // NOI18N
        ExchangeLabel.setText("Exchange/Return");
        ExchangeLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ExchangeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExchangeLabelMouseClicked(evt);
            }
        });

        CancelLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CancelLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-ban-32.png"))); // NOI18N
        CancelLabel.setText("Cancel");
        CancelLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CancelLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelLabelMouseClicked(evt);
            }
        });

        PrintReceipt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PrintReceipt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-print-32.png"))); // NOI18N
        PrintReceipt.setText("Print Receipt");
        PrintReceipt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout SalesHeaderLayout = new javax.swing.GroupLayout(SalesHeader);
        SalesHeader.setLayout(SalesHeaderLayout);
        SalesHeaderLayout.setHorizontalGroup(
            SalesHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newsaleLabel)
                .addGap(18, 18, 18)
                .addComponent(OrderLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EditLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ExchangeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CancelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PrintReceipt)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        SalesHeaderLayout.setVerticalGroup(
            SalesHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SalesHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newsaleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(OrderLabel)
                    .addComponent(EditLabel)
                    .addComponent(ExchangeLabel)
                    .addComponent(CancelLabel)
                    .addComponent(PrintReceipt))
                .addContainerGap())
        );

        Submenu.add(SalesHeader, "card2");

        ProductsHeader.setBackground(java.awt.Color.white);
        ProductsHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        ProductsHeader.setForeground(new java.awt.Color(153, 153, 153));

        jLabel2.setText("New Product");

        jLabel4.setText("New Brand");
        jLabel4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel4KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout ProductsHeaderLayout = new javax.swing.GroupLayout(ProductsHeader);
        ProductsHeader.setLayout(ProductsHeaderLayout);
        ProductsHeaderLayout.setHorizontalGroup(
            ProductsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsHeaderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addGap(54, 54, 54)
                .addComponent(jLabel4)
                .addContainerGap(585, Short.MAX_VALUE))
        );
        ProductsHeaderLayout.setVerticalGroup(
            ProductsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductsHeaderLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(ProductsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(17, 17, 17))
        );

        Submenu.add(ProductsHeader, "card2");

        InfoPanel.setBackground(new java.awt.Color(255, 255, 255));
        InfoPanel.setMaximumSize(new java.awt.Dimension(30000, 30000));

        SetEmailNotification.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        SetEmailNotification.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-email-16.png"))); // NOI18N
        SetEmailNotification.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernameLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-male-user-16.png"))); // NOI18N
        usernameLabel.setText("username");

        javax.swing.GroupLayout InfoPanelLayout = new javax.swing.GroupLayout(InfoPanel);
        InfoPanel.setLayout(InfoPanelLayout);
        InfoPanelLayout.setHorizontalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SetEmailNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        InfoPanelLayout.setVerticalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InfoPanelLayout.createSequentialGroup()
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfoPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(usernameLabel))
                    .addGroup(InfoPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(SetEmailNotification)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        Sidabar.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("jLabel3");

        stats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-statistics-64.png"))); // NOI18N
        stats.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statsMouseClicked(evt);
            }
        });

        saleslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-best-sales-64 Black.png"))); // NOI18N
        saleslabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleslabelMouseClicked(evt);
            }
        });

        box.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-box-64.png"))); // NOI18N
        box.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                boxMouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Customers");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Users");

        customers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-select-users-64.png"))); // NOI18N
        customers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customersMouseClicked(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Statistics");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Products");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Sales");

        admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-administrator-male-64 (1).png"))); // NOI18N
        admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adminMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SidabarLayout = new javax.swing.GroupLayout(Sidabar);
        Sidabar.setLayout(SidabarLayout);
        SidabarLayout.setHorizontalGroup(
            SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidabarLayout.createSequentialGroup()
                .addGap(228, 228, 228)
                .addComponent(jLabel3))
            .addGroup(SidabarLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(box)
                    .addComponent(customers)
                    .addComponent(stats)
                    .addComponent(saleslabel)
                    .addGroup(SidabarLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(SidabarLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel14))
                            .addComponent(admin)))
                    .addGroup(SidabarLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel15))))
            .addGroup(SidabarLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel16))
            .addGroup(SidabarLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel12))
        );
        SidabarLayout.setVerticalGroup(
            SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidabarLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(saleslabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addGap(26, 26, 26)
                .addComponent(box)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addGap(32, 32, 32)
                .addComponent(stats)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(customers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addGap(31, 31, 31)
                .addComponent(admin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addGap(140, 140, 140)
                .addComponent(jLabel3)
                .addGap(273, 273, 273))
        );

        Submenu1.setBackground(new java.awt.Color(246, 246, 246));
        Submenu1.setForeground(new java.awt.Color(228, 228, 228));

        todays.setText("jLabel4");

        jToggleButton1.setText("Open Orders");

        jToggleButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton2.setText("Sales");

        jToggleButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-downloads-16.png"))); // NOI18N
        jToggleButton3.setText("Download Excell");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Submenu1Layout = new javax.swing.GroupLayout(Submenu1);
        Submenu1.setLayout(Submenu1Layout);
        Submenu1Layout.setHorizontalGroup(
            Submenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Submenu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(todays, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );
        Submenu1Layout.setVerticalGroup(
            Submenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Submenu1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(Submenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(todays)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton2)
                    .addComponent(jToggleButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablePanel.setLayout(new java.awt.CardLayout());

        SalesTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        SalesTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        SalesTable.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        SalesTable.setForeground(new java.awt.Color(102, 102, 102));
        SalesTable.setModel(new javax.swing.table.DefaultTableModel(
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
        SalesTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SalesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SalesTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(SalesTable);

        javax.swing.GroupLayout SalesTablePanelLayout = new javax.swing.GroupLayout(SalesTablePanel);
        SalesTablePanel.setLayout(SalesTablePanelLayout);
        SalesTablePanelLayout.setHorizontalGroup(
            SalesTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesTablePanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
        );
        SalesTablePanelLayout.setVerticalGroup(
            SalesTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesTablePanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(SalesTablePanel, "card2");

        BrandsTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        BrandsTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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

        javax.swing.GroupLayout BrandsTablePanelLayout = new javax.swing.GroupLayout(BrandsTablePanel);
        BrandsTablePanel.setLayout(BrandsTablePanelLayout);
        BrandsTablePanelLayout.setHorizontalGroup(
            BrandsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BrandsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        BrandsTablePanelLayout.setVerticalGroup(
            BrandsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BrandsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(BrandsTablePanel, "card2");

        categoryTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        categoryTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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

        javax.swing.GroupLayout categoryTablePanelLayout = new javax.swing.GroupLayout(categoryTablePanel);
        categoryTablePanel.setLayout(categoryTablePanelLayout);
        categoryTablePanelLayout.setHorizontalGroup(
            categoryTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        categoryTablePanelLayout.setVerticalGroup(
            categoryTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoryTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(categoryTablePanel, "card2");

        ProductsTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        ProductsTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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

        javax.swing.GroupLayout ProductsTablePanelLayout = new javax.swing.GroupLayout(ProductsTablePanel);
        ProductsTablePanel.setLayout(ProductsTablePanelLayout);
        ProductsTablePanelLayout.setHorizontalGroup(
            ProductsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        ProductsTablePanelLayout.setVerticalGroup(
            ProductsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(ProductsTablePanel, "card2");

        StocksTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        StocksTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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

        javax.swing.GroupLayout StocksTablePanelLayout = new javax.swing.GroupLayout(StocksTablePanel);
        StocksTablePanel.setLayout(StocksTablePanelLayout);
        StocksTablePanelLayout.setHorizontalGroup(
            StocksTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StocksTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        StocksTablePanelLayout.setVerticalGroup(
            StocksTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StocksTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(StocksTablePanel, "card2");

        DailyReportTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        DailyReportTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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

        tinvest.setText("jLabel5");

        totalreturns.setText("jLabel6");

        profit.setText("jLabel7");

        javax.swing.GroupLayout DailyReportTablePanelLayout = new javax.swing.GroupLayout(DailyReportTablePanel);
        DailyReportTablePanel.setLayout(DailyReportTablePanelLayout);
        DailyReportTablePanelLayout.setHorizontalGroup(
            DailyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DailyReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DailyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tinvest)
                    .addComponent(totalreturns)
                    .addComponent(profit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DailyReportTablePanelLayout.setVerticalGroup(
            DailyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DailyReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(DailyReportTablePanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(tinvest)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalreturns)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(profit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablePanel.add(DailyReportTablePanel, "card2");

        WeeklyReportTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        WeeklyReportTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane7.setViewportView(weeklyreporttable);

        tinvest1.setText("Investment");

        totalreturns1.setText("Total returns");

        profit1.setText("Profit");

        javax.swing.GroupLayout WeeklyReportTablePanelLayout = new javax.swing.GroupLayout(WeeklyReportTablePanel);
        WeeklyReportTablePanel.setLayout(WeeklyReportTablePanelLayout);
        WeeklyReportTablePanelLayout.setHorizontalGroup(
            WeeklyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WeeklyReportTablePanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(WeeklyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tinvest1)
                    .addComponent(totalreturns1)
                    .addComponent(profit1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        WeeklyReportTablePanelLayout.setVerticalGroup(
            WeeklyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WeeklyReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(WeeklyReportTablePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(tinvest1)
                .addGap(18, 18, 18)
                .addComponent(totalreturns1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(profit1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablePanel.add(WeeklyReportTablePanel, "card2");

        MonthlyReportTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        MonthlyReportTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane8.setViewportView(monthlyreporttable);

        tinvest2.setText("Total Investment");

        totalreturns2.setText("Total returns");

        profit2.setText("Profit");

        javax.swing.GroupLayout MonthlyReportTablePanelLayout = new javax.swing.GroupLayout(MonthlyReportTablePanel);
        MonthlyReportTablePanel.setLayout(MonthlyReportTablePanelLayout);
        MonthlyReportTablePanelLayout.setHorizontalGroup(
            MonthlyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MonthlyReportTablePanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(MonthlyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tinvest2)
                    .addComponent(totalreturns2)
                    .addComponent(profit2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        MonthlyReportTablePanelLayout.setVerticalGroup(
            MonthlyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MonthlyReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(MonthlyReportTablePanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(tinvest2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalreturns2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profit2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablePanel.add(MonthlyReportTablePanel, "card2");

        DateReportTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        DateReportTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane9.setViewportView(datedreporttable);

        tinvest3.setText("jLabel5");

        totalreturns3.setText("jLabel6");

        profit3.setText("jLabel7");

        javax.swing.GroupLayout DateReportTablePanelLayout = new javax.swing.GroupLayout(DateReportTablePanel);
        DateReportTablePanel.setLayout(DateReportTablePanelLayout);
        DateReportTablePanelLayout.setHorizontalGroup(
            DateReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DateReportTablePanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(DateReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tinvest3)
                    .addComponent(totalreturns3)
                    .addComponent(profit3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DateReportTablePanelLayout.setVerticalGroup(
            DateReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DateReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(DateReportTablePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(tinvest3)
                .addGap(18, 18, 18)
                .addComponent(totalreturns3)
                .addGap(18, 18, 18)
                .addComponent(profit3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablePanel.add(DateReportTablePanel, "card2");

        UsersTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        UsersTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane10.setViewportView(staffTable);

        javax.swing.GroupLayout UsersTablePanelLayout = new javax.swing.GroupLayout(UsersTablePanel);
        UsersTablePanel.setLayout(UsersTablePanelLayout);
        UsersTablePanelLayout.setHorizontalGroup(
            UsersTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsersTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        UsersTablePanelLayout.setVerticalGroup(
            UsersTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsersTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(UsersTablePanel, "card2");

        NotificationsTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        NotificationsTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane11.setViewportView(EmailTable);

        javax.swing.GroupLayout NotificationsTablePanelLayout = new javax.swing.GroupLayout(NotificationsTablePanel);
        NotificationsTablePanel.setLayout(NotificationsTablePanelLayout);
        NotificationsTablePanelLayout.setHorizontalGroup(
            NotificationsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NotificationsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        NotificationsTablePanelLayout.setVerticalGroup(
            NotificationsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NotificationsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(NotificationsTablePanel, "card2");

        CashTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        CashTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane12.setViewportView(transferedCash);

        javax.swing.GroupLayout CashTablePanelLayout = new javax.swing.GroupLayout(CashTablePanel);
        CashTablePanel.setLayout(CashTablePanelLayout);
        CashTablePanelLayout.setHorizontalGroup(
            CashTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CashTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        CashTablePanelLayout.setVerticalGroup(
            CashTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CashTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(CashTablePanel, "card2");

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(InfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(menuLayout.createSequentialGroup()
                .addComponent(Sidabar, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Submenu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(Submenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuLayout.createSequentialGroup()
                .addComponent(InfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuLayout.createSequentialGroup()
                        .addComponent(Submenu, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Submenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(TablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(Sidabar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        LeftPanel.add(menu, "card2");

        getContentPane().add(LeftPanel, java.awt.BorderLayout.CENTER);

        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N

        TopMenu.setText("File");
        TopMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        QuitMenu.setText("Quit");
        QuitMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        QuitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitMenuActionPerformed(evt);
            }
        });
        TopMenu.add(QuitMenu);

        jMenuBar1.add(TopMenu);

        jMenu2.setText("Show");
        jMenu2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuBar1.add(jMenu2);

        jMenu1.setText("Cash");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void QuitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitMenuActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_QuitMenuActionPerformed

    private void jLabel4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel4KeyPressed
        // TODO add your handling code here:
        CreateBrands cb = new CreateBrands();
    }//GEN-LAST:event_jLabel4KeyPressed

    private void newsaleLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newsaleLabelMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        try {
            Sale sale = new Sale();
        } catch (SQLException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_newsaleLabelMouseClicked

    private void saleslabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleslabelMouseClicked
        // TODO add your handling code here:
        getActiveClass("saleslabel");
    }//GEN-LAST:event_saleslabelMouseClicked

    private void boxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boxMouseClicked
        // TODO add your handling code here:
        getActiveClass("box");
        RegisterProduct.main(null);
    }//GEN-LAST:event_boxMouseClicked

    private void statsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsMouseClicked
        // TODO add your handling code here:
        getActiveClass("stats");
    }//GEN-LAST:event_statsMouseClicked

    private void adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminMouseClicked
        // TODO add your handling code here:
        getActiveClass("admin");
    }//GEN-LAST:event_adminMouseClicked

    private void customersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customersMouseClicked
        // TODO add your handling code here:
        getActiveClass("customers");
    }//GEN-LAST:event_customersMouseClicked

    private void CancelLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelLabelMouseClicked
        // Deleting or Cancelling the sale:
        int reply
                = JOptionPane.showConfirmDialog(this, "Are you sure?,"
                + " Cancelling will delete this sale permanently",
           "Confirm", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            try {
                row = SalesTable.getSelectedRow();
                column = SalesTable.getColumnCount();
                int orderId = IntegerConverter(SellingModel.getValueAt(row, 0).toString());
                String preqty = SellingModel.getValueAt(row, 2).toString();
                Double q = DoubleConverter(preqty);
                Orders.deleteRow(orderId, q);
                SellingModel.removeRow(row);
                PresentsalesList.remove(row);
                SellingModel.setRowCount(0);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            for (int i = 0; i < PresentsalesList.size(); i++) {
                Object[] obj = {
                    PresentsalesList.get(i).orderid,
                    PresentsalesList.get(i).product,
                    PresentsalesList.get(i).quantity,
                    PresentsalesList.get(i).listprice,
                    PresentsalesList.get(i).discount
                };
                SellingModel.addRow(obj);
            }
        }
    }//GEN-LAST:event_CancelLabelMouseClicked

    private void EditLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditLabelMouseClicked
        Boolean selected = ((Boolean)SellingModel.getValueAt(row, 0));
        if(selected == true){
            try {
                EditSale editSale;
                EditSale.setMeUp(getProductDetails());
            } catch (ClassNotFoundException | ParseException ex) {
                Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_EditLabelMouseClicked

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // Fetch the Excell file
        ExcelFormat exft = new ExcelFormat();
        JFileChooser fchoose = new JFileChooser();
        int option = fchoose.showSaveDialog(UIv2.this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String name = fchoose.getSelectedFile().getName();
            String path = fchoose.getSelectedFile().getParentFile().getPath();
            String file = path + "\\" + name + ".xls";
            exft.export(SalesTable, new File(file));
        }

    }//GEN-LAST:event_jToggleButton3ActionPerformed

    public ArrayList<String> getProductDetails() {
        return prd_details;
    }

    private void SalesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalesTableMouseClicked
        row = SalesTable.getSelectedRow();
        column = SalesTable.getColumnCount();
        String prd_name = SellingModel.getValueAt(row, 2).toString();
        String id = SellingModel.getValueAt(row, 1).toString();
        String sellingquantity = SellingModel.getValueAt(row, 3).toString();
        String price = SellingModel.getValueAt(row, 4).toString();
        String discount = SellingModel.getValueAt(row, 5).toString();
        this.prd_details.add(id);
        this.prd_details.add(prd_name);
        this.prd_details.add(price);
        this.prd_details.add(sellingquantity);
        this.prd_details.add(discount);
    }//GEN-LAST:event_SalesTableMouseClicked

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
     
    }//GEN-LAST:event_formFocusGained

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
           try {
            loadJtableValues();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void ExchangeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExchangeLabelMouseClicked
        // Calling the exchange JFrame
        ReturnProduct.main(null);
    }//GEN-LAST:event_ExchangeLabelMouseClicked

    /**
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
     */
    public final void loadJtableValues() throws SQLException,
            ClassNotFoundException, ParseException {
        if (SellingModel != null) {
            SellingModel.setRowCount(0);
        }
        PresentsalesList = listOrders();
        for (int i = 0; i < PresentsalesList.size(); i++) {
            Object[] obj = {
                PresentsalesList.get(i).selected,
                (PresentsalesList.get(i).orderid).toUpperCase(),
                (PresentsalesList.get(i).product).toUpperCase(),
                (PresentsalesList.get(i).quantity).toUpperCase(),
                (PresentsalesList.get(i).listprice),
                (PresentsalesList.get(i).discount)
            };
            SellingModel.addRow(obj);
        }
        SalesTable.repaint();
        SalesTable.revalidate();
    }

    public static void main(String args[])
            throws UnsupportedLookAndFeelException {
        SplashJava.main(null);
        //https://github.com/JFormDesigner/FlatLaf/tree/main/flatlaf-intellij-themes#how-to-use
    
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new UIv2().setVisible(true);
            } catch (ClassNotFoundException | ParseException ex) {
                Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BrandsTablePanel;
    private javax.swing.JLabel CancelLabel;
    private javax.swing.JPanel CashTablePanel;
    private javax.swing.JPanel DailyReportTablePanel;
    private javax.swing.JPanel DateReportTablePanel;
    private javax.swing.JLabel EditLabel;
    private javax.swing.JTable EmailTable;
    private javax.swing.JLabel ExchangeLabel;
    private javax.swing.JPanel InfoPanel;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JPanel MonthlyReportTablePanel;
    private javax.swing.JPanel NotificationsTablePanel;
    private javax.swing.JLabel OrderLabel;
    private javax.swing.JLabel PrintReceipt;
    private javax.swing.JPanel ProductsHeader;
    private javax.swing.JPanel ProductsTablePanel;
    private javax.swing.JMenuItem QuitMenu;
    private javax.swing.JPanel SalesHeader;
    private javax.swing.JTable SalesTable;
    private javax.swing.JPanel SalesTablePanel;
    private static javax.swing.JLabel SetEmailNotification;
    private javax.swing.JPanel Sidabar;
    private javax.swing.JPanel StocksTablePanel;
    private javax.swing.JPanel Submenu;
    private javax.swing.JPanel Submenu1;
    private javax.swing.JPanel TablePanel;
    private javax.swing.JMenu TopMenu;
    private javax.swing.JPanel UsersTablePanel;
    private javax.swing.JPanel WeeklyReportTablePanel;
    private javax.swing.JLabel admin;
    private javax.swing.JLabel box;
    private javax.swing.JTable brandsTable;
    private javax.swing.JTable categoryTable;
    private javax.swing.JPanel categoryTablePanel;
    private javax.swing.JLabel customers;
    private javax.swing.JTable dailySalesTable;
    private javax.swing.JTable datedreporttable;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JPanel menu;
    private javax.swing.JTable monthlyreporttable;
    private javax.swing.JLabel newsaleLabel;
    private javax.swing.JTable productsTable;
    private javax.swing.JLabel profit;
    private javax.swing.JLabel profit1;
    private javax.swing.JLabel profit2;
    private javax.swing.JLabel profit3;
    private javax.swing.JLabel saleslabel;
    private javax.swing.JTable staffTable;
    private javax.swing.JLabel stats;
    private javax.swing.JTable stocksTable;
    private javax.swing.JLabel tinvest;
    private javax.swing.JLabel tinvest1;
    private javax.swing.JLabel tinvest2;
    private javax.swing.JLabel tinvest3;
    private javax.swing.JLabel todays;
    private javax.swing.JLabel totalreturns;
    private javax.swing.JLabel totalreturns1;
    private javax.swing.JLabel totalreturns2;
    private javax.swing.JLabel totalreturns3;
    private javax.swing.JTable transferedCash;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTable weeklyreporttable;
    // End of variables declaration//GEN-END:variables
}


// Customize the code to set the color for each column in JTable
class MyRenderer extends DefaultTableCellRenderer 
{
   Color bg, fg;
   public MyRenderer(Color bg, Color fg) {
      super();
      this.bg = bg;
      this.fg = fg;
   }
   @Override
   public Component getTableCellRendererComponent(JTable table, Object 
   value, boolean isSelected, boolean hasFocus, int row, int column) 
   {
      Component cell = super.getTableCellRendererComponent(table, value, 
      isSelected, hasFocus, row, column);
      cell.setBackground(bg);
      cell.setForeground(fg);
      return cell;
   }
}