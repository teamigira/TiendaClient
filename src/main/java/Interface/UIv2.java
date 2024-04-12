/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Authentication.Sessions;
import static Authentication.Sessions.LoggedUser;
import static Authentication.Sessions.currentUserRole;
import Classes.AbstractClasses.Brand;
import Classes.AbstractClasses.Category;
import Classes.AbstractClasses.DailyReport;
import Classes.AbstractClasses.Email;
import Classes.AbstractClasses.Order;
import Classes.AbstractClasses.Permissions;
import Classes.AbstractClasses.Product;
import Classes.AbstractClasses.RolePermissionMapping;
import Classes.AbstractClasses.Roles;
import Classes.AbstractClasses.SelectedStaff;
import Classes.AbstractClasses.Staff;
import Classes.AbstractClasses.Stock;
import Classes.AbstractClasses.Transfer;
import static Classes.Functions.Accounts.getTransfers;
import static Classes.Functions.Categories.listBrands;
import static Classes.Functions.Categories.listCategories;
import Classes.Functions.Constants;
import static Classes.Functions.Notifications.listNotifications;
import Classes.Functions.Orders;
import static Classes.Functions.Orders.listOrders;
import Classes.Functions.Permissions.PermissionFileManager;
import static Classes.Functions.Permissions.PermissionFileManager.addRole;
import static Classes.Functions.Permissions.PermissionFileManager.deletePermissionRow;
import static Classes.Functions.Permissions.PermissionFileManager.deleteRow;
import static Classes.Functions.Permissions.PermissionFileManager.loadPermissions;
import static Classes.Functions.Permissions.PermissionFileManager.loadRolePermissions;
import static Classes.Functions.Permissions.PermissionFileManager.loadRoles;
import static Classes.Functions.Permissions.PermissionFileManager.renameRole;
import Classes.Functions.Permissions.PermissionGroup;
import Classes.Functions.Permissions.RoleInputDialog;
import static Classes.Functions.Products.listProductOnly;
import static Classes.Functions.Products.listProducts;
import static Classes.Functions.Products.listStockProducts;
import static Classes.Functions.Reports.DatedReport;
import static Classes.Functions.Reports.listDailyReport;
import static Classes.Functions.Reports.listMonthlyReport;
import static Classes.Functions.Reports.listWeeklyReport;
import Classes.Functions.Sales_Staffs;
import static Classes.Functions.Sales_Staffs.LoadStaffs;
import static Classes.Functions.Sales_Staffs.addtestUsers;
import static Classes.Functions.Stocks.listStocks;
import Classes.Utilities.NotificationManager;
import Classes.Utilities.NotificationManager.NotificationType;

import static Classes.Utilities.NotificationManager.showConsoleNotification;
import static Classes.Utilities.NotificationManager.showPopupNotification;
import Classes.Utilities.Resources;
import Classes.Utilities.StockThread;
import Interface.Sales.SaveSale;
import Interface.Sales.ReturnProduct;

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
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import Interface.Products.*;
import Interface.Users.EditUser;
import Interface.Users.RegisterNewUser;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/*
 *
 * @author Nkanabo
 */
public final class UIv2 extends javax.swing.JFrame {

    public static UIv2 Instance;

    int productId;

    //Brands
    ArrayList<Brand> brandlist;
    String brandheaders[] = {"Brand Id", "Brand Name"};
    DefaultTableModel brandsModel;

    ArrayList<Order> PresentsalesList;
    Object[] columnNames = {"Select", "Order Id", "Product", "Quantity", "List price", "Discount"};
    DefaultTableModel SellingModel = new DefaultTableModel(0, 0) {
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

    ArrayList<Roles> rolelist;
    private DefaultListModel<String> listModel;
    private Map<String, List<String>> permissionsByCategory = new HashMap<>();

    //Products
    ArrayList<Stock> stocklist;
    String stockheader[] = {"Product Id", "Product", "Quantity"};
    DefaultTableModel stockModel;
    //End of products

    DefaultTableModel dailySalesModel;
    String dailyreportheader[] = {"Product Name", "Retail Price", "List Price", "Profit", "quantity", "date"};
    ArrayList<DailyReport> dailyreportlist;

    DefaultTableModel StaffsModel;
    String staffsheader[] = {"First Name", "Last Name", "Email", "phone_no", "store", "Status", "manager_id", "Role"};
    ArrayList<Staff> staffslist;
    private ArrayList<SelectedStaff> selectedStaff = new ArrayList<>();
    // Declare a separate data structure to store user IDs
    ArrayList<String> userIds = new ArrayList<String>();

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

    //For roles and permissions JTree
    String selectedNode, selectedValue;

    Component SelectedComponent = new JList();

    
    private Map<String, List<String>> rolePermissionsMap;

     // Permissions map: component name -> associated permission
     private Map<String, String> loadedpermissions;

    /**
     * Creates new form UIv2
     *
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
     */
    public UIv2() throws ClassNotFoundException, ParseException, SQLException {
        initComponents();
        //<editor-fold defaultstate="collapsed" desc="comment">

        //</editor-fold>
        //The below command makes the frame appear at the center of any screen
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setExtendedState(getExtendedState() | UIv2.MAXIMIZED_BOTH);
        StockThread th = new StockThread();

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
        if (Instance != null) {
            Instance = new UIv2();
        }

        //Orders list 
        PresentsalesList = new ArrayList<>();

        usernameLabel.setText(LoggedUser + " ["+currentUserRole+"]");
        String today = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        todays.setText(String.valueOf(Calendar.getInstance().getTime()));
        //Coloring the icons
        getActiveClass("");

        //Brands
        brandlist = new ArrayList<>();
        brandsModel = new DefaultTableModel(brandheaders, 0);
        brandsTable.setModel(brandsModel);

        //End of Brand
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

        //Roles
        rolelist = new ArrayList<>();
        listModel = new DefaultListModel<>();

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
        staffTable.setDefaultEditor(Object.class, null);

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
        col.setCellRenderer(new MyRenderer(new Color(255, 255, 255), new Color(0, 102, 51)));
        //col.setFont(col.getFont().deriveFont(Font.BOLD, 14f));
//        col.setCellRenderer(setFont(new Font("Arial", Font.BOLD, 10)));

        this.setLocationRelativeTo(null);

        

        loadJtableValues();
//            loadBrandsJtableValues();
//            LoadCategories();
//            LoadProducts();
//            LoadStockProducts();
//            LoadProductsOnly();
//            LoadStocks();
//            LoadNotificationsEmails();
//            Simulated permissions map (can be fetched from database)
//            Map to store permissions associated with each role
//            private Map<String, List<String>> rolePermissionsMap;
        initializeLoadedPermissions();
        initializeRolePermissionsMap();
        validatePermissions();

    }

    
    /*FUNCTIONS TO MANAE ROAL PERMISSIONS*/

    private void initializeLoadedPermissions() {
        // Load permissions from the database or other source
        // For demonstration purposes, let's assume some hardcoded values
        loadedpermissions = new HashMap<>();
        loadedpermissions.put("adminLabelMenu", "view_users");
        loadedpermissions.put("customers", "view_customers");
        loadedpermissions.put("stats", "view_reports");
        loadedpermissions.put("ProductsLabelMenu","view_products");
        loadedpermissions.put("saleslabel","view_sales");


        // Add other permissions...
    }

    
     // Initialize the rolePermissionsMap during initialization
    private void initializeRolePermissionsMap() {
        rolePermissionsMap = new HashMap<>();
        ArrayList<RolePermissionMapping> rolePermissions = loadRolePermissions();
        
        
        for (RolePermissionMapping mapping : rolePermissions) {
            // Access properties of each RolePermissionMapping object
            int roleId = mapping.getRoleId();
            int permissionId = mapping.getPermissionId();
            String roleName = mapping.getRoleName();
            String permissionName = mapping.getPermissionName();
            String category = mapping.getCategory();

            // Print or process the properties as needed
            System.out.println("Role ID: " + roleId);
            System.out.println("Permission ID: " + permissionId);
            System.out.println("Role Name: " + roleName);
            System.out.println("Permission Name: " + permissionName);
            System.out.println("Category: " + category);
        }

        // Populate the rolePermissionsMap
        for (RolePermissionMapping mapping : rolePermissions) {
            String roleName = mapping.getRoleName();
            String permissionName = mapping.getPermissionName();
                System.out.println("LINE 365 -->rOLE"+roleName+"Has ->"+permissionName);
            // Add the permission to the list associated with the role
            rolePermissionsMap.computeIfAbsent(roleName, k -> new ArrayList<>()).add(permissionName);
        }
    }
    
    private boolean userHasPermission(String role, String permission) {
        // Check if the rolePermissionsMap is initialized
        if (rolePermissionsMap == null) {
            initializeRolePermissionsMap();
            System.out.println("374 rolepermissionmao not null");
        }
        System.out.println("Your role is"+role+"and perm is "+permission);
        // Get the list of permissions associated with the role
        List<String> permissions = rolePermissionsMap.get(role);
          // Loop through the permissions list and print each permission
        for (String permit : permissions) {
            System.out.println("Permission: " + permit);
        }
                
        System.out.println(permissions.contains(permission));
        // Check if the permission exists in the list
        return permissions != null && permissions.contains(permission);
    }

    private void validatePermissions() {
        // Check if the current user has permission for each icon
   // Get the current user's role
    String currentUserRole = Sessions.getInstance().getCurrentUserRole();
    
    // Check if the current user has permission for each UI component
    for (Map.Entry<String, String> entry : loadedpermissions.entrySet()) {
        String componentName = entry.getKey();
        String associatedPermission = entry.getValue();

        // Check if the associated permission exists in the user's role
        if (userHasPermission(currentUserRole, associatedPermission)) {
            // Enable the UI component
            System.out.println("enabled");
            enableUIComponent(componentName);
        }
    }
    }

    // Method to enable the specified UI component
    private void enableUIComponent(String componentName) {
        // Enable the UI component based on its name
        switch (componentName) {
            case "adminLabelMenu":
                adminilabelmenu.setEnabled(true);
                break;
            case "customers":
            customers.setEnabled(true);
            break;
            case "stats":
            stats.setEnabled(true);
            break;
            case "ProductsLabelMenu":
            ProductsLabelMenu.setEnabled(true);
            break;
            case "saleslabel":
            saleslabel.setEnabled(true);
            break;
            // Add more cases for other UI components if needed
        }
    }
    /*END OF PERMISSIONS MAP*/
    
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

    public void LoadRoles() {
        rolelist = loadRoles();
        listModel.clear();
        int size = rolelist.size();
        for (Roles role : rolelist) {
            listModel.addElement(role.getRoleName());
        }
        rolesJlist.setModel(listModel);
        // Set tooltip text
        rolesJlist.setToolTipText("Select the role and then select permission"
                + "map permission to add permission in a new role");
        TablePanel.repaint();
        TablePanel.revalidate();
    }

    public void LoadPermissions() {
        ArrayList<Permissions> permissionList = loadPermissions();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Permissions");

        // Group permissions by category
        for (Permissions permission : permissionList) {
            String category = permission.getCategory();
            permissionsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(permission.getPermissionName());
        }

        // Add categories as parent nodes
        for (String category : permissionsByCategory.keySet()) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
            rootNode.add(categoryNode);

            // Add permissions as child nodes
            for (String permission : permissionsByCategory.get(category)) {
                DefaultMutableTreeNode permissionNode = new DefaultMutableTreeNode(permission);
                categoryNode.add(permissionNode);
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        PermissionsTree.setModel(treeModel);
//        PermissionsTree.setCellRenderer(new CustomTreeCellRenderer()); // Apply custom renderer
        TablePanel.repaint();
        TablePanel.revalidate();
    }

    public void LoadPermissionsRoles() {
        ArrayList<RolePermissionMapping> rolePermissionMappings = loadRolePermissions();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Roles");

        // Group permissions by role
        Map<String, Map<String, List<String>>> permissionsByRole = new HashMap<>();
        for (RolePermissionMapping mapping : rolePermissionMappings) {
            String roleName = mapping.getRoleName();
            String permissionName = mapping.getPermissionName();
            String category = mapping.getCategory();

            permissionsByRole
                    .computeIfAbsent(roleName, k -> new HashMap<>())
                    .computeIfAbsent(category, k -> new ArrayList<>())
                    .add(permissionName);
        }

        // Add roles as parent nodes
        for (String roleName : permissionsByRole.keySet()) {
            DefaultMutableTreeNode roleNode = new DefaultMutableTreeNode(roleName);
            rootNode.add(roleNode);

            // Add categories as child nodes
            Map<String, List<String>> categoryMap = permissionsByRole.get(roleName);
            for (String category : categoryMap.keySet()) {
                DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
                roleNode.add(categoryNode);

                // Add permissions as child nodes
                List<String> permissionList = categoryMap.get(category);
                for (String permission : permissionList) {
                    DefaultMutableTreeNode permissionNode = new DefaultMutableTreeNode(permission);
                    categoryNode.add(permissionNode);
                }
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        mappedTree.setModel(treeModel);
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

    //This function loads all the products that are only available in Stock.
    public void LoadStockProducts() throws ClassNotFoundException {

        try {
            stockproductlist = listStockProducts();
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public void LoadSatffs() throws ParseException, ClassNotFoundException, SQLException {
        StaffsModel.setRowCount(0);
        staffslist = LoadStaffs();
        if (staffslist.isEmpty()) {
        } else {
            for (int i = 0; i < staffslist.size(); i++) {
                userIds.add(staffslist.get(i).userid);
                int rolem = staffslist.get(i).role;
                String roleName = PermissionFileManager.getRoleName(rolem);
                Object[] obj = {
                    staffslist.get(i).staff_name,
                    staffslist.get(i).sur_name,
                    staffslist.get(i).staff_email,
                    staffslist.get(i).phone_no,
                    staffslist.get(i).store,
                    Constants.getStatusLabel(Integer.parseInt(staffslist.get(i).Status)),
                    staffslist.get(i).manager_id,
                    roleName,};
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
        // Declare variables to hold the original icons
        Icon originalSalesIcon = new ImageIcon(getClass().getResource("/resources/images/icons8-best-sales-64 Black.png"));
        Icon originalProductsIcon = new ImageIcon(getClass().getResource("/resources/images/icons8-box-64.png"));
        Icon originalStatsIcon = new ImageIcon(getClass().getResource("/resources/images/icons8-statistics-64.png"));
        Icon originalAdminIcon = new ImageIcon(getClass().getResource("/resources/images/icons8-administrator-male-64 (1).png"));
        Icon originalCustomersIcon = new ImageIcon(getClass().getResource("/resources/images/icons8-select-users-64.png"));

        switch (label) {
            case "saleslabel":
                saleslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-best-sales-64 (1).png")));
                ProductsLabelMenu.setIcon(originalProductsIcon);
                stats.setIcon(originalStatsIcon);
                adminilabelmenu.setIcon(originalAdminIcon);
                customers.setIcon(originalCustomersIcon);
                // Rest of your code
                break;
            case "box":
                ProductsLabelMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-box-64Blue.png")));
                saleslabel.setIcon(originalSalesIcon);
                stats.setIcon(originalStatsIcon);
                adminilabelmenu.setIcon(originalAdminIcon);
                customers.setIcon(originalCustomersIcon);
                // Rest of your code
                break;
            case "stats":
                stats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-statistics-64Blue.png")));
                saleslabel.setIcon(originalSalesIcon);
                ProductsLabelMenu.setIcon(originalProductsIcon);
                adminilabelmenu.setIcon(originalAdminIcon);
                customers.setIcon(originalCustomersIcon);
                // Rest of your code
                break;
            case "admin":
                adminilabelmenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-administrator-male-64.png")));
                saleslabel.setIcon(originalSalesIcon);
                ProductsLabelMenu.setIcon(originalProductsIcon);
                stats.setIcon(originalStatsIcon);
                customers.setIcon(originalCustomersIcon);
                // Rest of your code
                break;
            case "customers":
                customers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-select-users-64Blue.png")));
                saleslabel.setIcon(originalSalesIcon);
                ProductsLabelMenu.setIcon(originalProductsIcon);
                stats.setIcon(originalStatsIcon);
                adminilabelmenu.setIcon(originalAdminIcon);
                // Rest of your code
                break;
            default:
                saleslabel.setIcon(originalSalesIcon);
                ProductsLabelMenu.setIcon(originalProductsIcon);
                stats.setIcon(originalStatsIcon);
                adminilabelmenu.setIcon(originalAdminIcon);
                customers.setIcon(originalCustomersIcon);
        }
    }

    public void getActiveddClass(String label) {
        switch (label) {
            case "saleslabel":
                saleslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-best-sales-64 (1).png"))); // NOI18N
                //this.removeAll();
                this.add(SalesTablePanel);
                this.validate();
                break;
            case "box":
                ProductsLabelMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-box-64Blue.png")));
                this.add(ProductsTablePanel);
                this.validate();
                break;
            case "stats":
                stats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-statistics-64Blue.png")));
                break;
            case "admin":
                adminilabelmenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-administrator-male-64.png")));
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
        InventoryLabel = new javax.swing.JLabel();
        newproducy1 = new javax.swing.JLabel();
        UsersHeader = new javax.swing.JPanel();
        permissionnRolesLabel = new javax.swing.JLabel();
        newUserLabel = new javax.swing.JLabel();
        InfoPanel = new javax.swing.JPanel();
        SetEmailNotification = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Sidabar = new javax.swing.JPanel();
        stats = new javax.swing.JLabel();
        saleslabel = new javax.swing.JLabel();
        customerslabelmenu = new javax.swing.JLabel();
        userlabelmenu = new javax.swing.JLabel();
        customers = new javax.swing.JLabel();
        statisticslabelmenu = new javax.swing.JLabel();
        productslabelmenu = new javax.swing.JLabel();
        saleslabelmenu = new javax.swing.JLabel();
        adminilabelmenu = new javax.swing.JLabel();
        ProductsLabelMenu = new javax.swing.JLabel();
        Submenu1 = new javax.swing.JPanel();
        userssubmenu1 = new javax.swing.JPanel();
        todays = new javax.swing.JLabel();
        editlabeluserssumenu = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        salessubmenu1 = new javax.swing.JPanel();
        rolesnpermissionsubmenu1 = new javax.swing.JPanel();
        editpermrole = new javax.swing.JLabel();
        deleteRowPermissisonLabel = new javax.swing.JLabel();
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
        RolesPermissions = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        PermissionsTree = new javax.swing.JTree();
        jLabel3 = new javax.swing.JLabel();
        NewRole = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        rolesJlist = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        mappedTree = new javax.swing.JTree();
        description = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        TopMenu = new javax.swing.JMenu();
        QuitMenu = new javax.swing.JMenuItem();
        showMenu = new javax.swing.JMenu();
        CashMenu = new javax.swing.JMenu();
        BrandsMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

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
        EditLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-edit-32.png"))); // NOI18N
        EditLabel.setText("Edit");
        EditLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        EditLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EditLabelMouseClicked(evt);
            }
        });

        ExchangeLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ExchangeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-return-32.png"))); // NOI18N
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
        PrintReceipt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PrintReceiptMouseClicked(evt);
            }
        });

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
                .addContainerGap(26, Short.MAX_VALUE))
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
        ProductsHeader.setPreferredSize(new java.awt.Dimension(786, 60));

        InventoryLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        InventoryLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-inventory-32.png"))); // NOI18N
        InventoryLabel.setText("Inventory");
        InventoryLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        InventoryLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InventoryLabelMouseClicked(evt);
            }
        });

        newproducy1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newproducy1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-new-product-32.png"))); // NOI18N
        newproducy1.setText("New Product");
        newproducy1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newproducy1.setMaximumSize(new java.awt.Dimension(167, 32));
        newproducy1.setMinimumSize(new java.awt.Dimension(167, 32));
        newproducy1.setPreferredSize(new java.awt.Dimension(167, 32));
        newproducy1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newproducy1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ProductsHeaderLayout = new javax.swing.GroupLayout(ProductsHeader);
        ProductsHeader.setLayout(ProductsHeaderLayout);
        ProductsHeaderLayout.setHorizontalGroup(
            ProductsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newproducy1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InventoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(434, Short.MAX_VALUE))
        );
        ProductsHeaderLayout.setVerticalGroup(
            ProductsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductsHeaderLayout.createSequentialGroup()
                .addGroup(ProductsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(InventoryLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(newproducy1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Submenu.add(ProductsHeader, "card2");

        UsersHeader.setBackground(java.awt.Color.white);
        UsersHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        UsersHeader.setForeground(new java.awt.Color(153, 153, 153));
        UsersHeader.setPreferredSize(new java.awt.Dimension(786, 60));

        permissionnRolesLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        permissionnRolesLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-access-48.png"))); // NOI18N
        permissionnRolesLabel.setText("Roles & Permissions");
        permissionnRolesLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        permissionnRolesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                permissionnRolesLabelMouseClicked(evt);
            }
        });

        newUserLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newUserLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-add-user-male-48.png"))); // NOI18N
        newUserLabel.setText("New User");
        newUserLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newUserLabel.setMaximumSize(new java.awt.Dimension(167, 32));
        newUserLabel.setMinimumSize(new java.awt.Dimension(167, 32));
        newUserLabel.setPreferredSize(new java.awt.Dimension(167, 32));
        newUserLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newUserLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout UsersHeaderLayout = new javax.swing.GroupLayout(UsersHeader);
        UsersHeader.setLayout(UsersHeaderLayout);
        UsersHeaderLayout.setHorizontalGroup(
            UsersHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsersHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newUserLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(permissionnRolesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(395, Short.MAX_VALUE))
        );
        UsersHeaderLayout.setVerticalGroup(
            UsersHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsersHeaderLayout.createSequentialGroup()
                .addGroup(UsersHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(permissionnRolesLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(newUserLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Submenu.add(UsersHeader, "card2");

        InfoPanel.setBackground(new java.awt.Color(255, 255, 255));
        InfoPanel.setMaximumSize(new java.awt.Dimension(30000, 30000));

        SetEmailNotification.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        SetEmailNotification.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-email-16.png"))); // NOI18N
        SetEmailNotification.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernameLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-male-user-16.png"))); // NOI18N
        usernameLabel.setText("username");

        version.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        version.setText("Version 2.0.0");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Premium until 21-Mar-2025");

        javax.swing.GroupLayout InfoPanelLayout = new javax.swing.GroupLayout(InfoPanel);
        InfoPanel.setLayout(InfoPanelLayout);
        InfoPanelLayout.setHorizontalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(version)
                .addGap(18, 18, 18)
                .addComponent(SetEmailNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        InfoPanelLayout.setVerticalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InfoPanelLayout.createSequentialGroup()
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfoPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usernameLabel)
                            .addComponent(version)
                            .addComponent(jLabel1)))
                    .addGroup(InfoPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(SetEmailNotification)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        Sidabar.setBackground(new java.awt.Color(255, 255, 255));

        stats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-statistics-64.png"))); // NOI18N
        stats.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stats.setEnabled(false);
        stats.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statsMouseClicked(evt);
            }
        });

        saleslabel.setBackground(new java.awt.Color(204, 204, 255));
        saleslabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-best-sales-64 Black.png"))); // NOI18N
        saleslabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saleslabel.setEnabled(false);
        saleslabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleslabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saleslabelMouseEntered(evt);
            }
        });

        customerslabelmenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customerslabelmenu.setText("Customers");

        userlabelmenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        userlabelmenu.setText("Users");

        customers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-select-users-64.png"))); // NOI18N
        customers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customers.setEnabled(false);
        customers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customersMouseClicked(evt);
            }
        });

        statisticslabelmenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        statisticslabelmenu.setText("Statistics");

        productslabelmenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        productslabelmenu.setText("Products");

        saleslabelmenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saleslabelmenu.setLabelFor(saleslabel);
        saleslabelmenu.setText("Sales");

        adminilabelmenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-administrator-male-64 (1).png"))); // NOI18N
        adminilabelmenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        adminilabelmenu.setEnabled(false);
        adminilabelmenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adminilabelmenuMouseClicked(evt);
            }
        });

        ProductsLabelMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-box-64.png"))); // NOI18N
        ProductsLabelMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductsLabelMenu.setEnabled(false);
        ProductsLabelMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProductsLabelMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SidabarLayout = new javax.swing.GroupLayout(Sidabar);
        Sidabar.setLayout(SidabarLayout);
        SidabarLayout.setHorizontalGroup(
            SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidabarLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(SidabarLayout.createSequentialGroup()
                            .addGroup(SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(stats, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(statisticslabelmenu)
                                .addComponent(customerslabelmenu)
                                .addGroup(SidabarLayout.createSequentialGroup()
                                    .addGap(15, 15, 15)
                                    .addComponent(saleslabelmenu))
                                .addComponent(saleslabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ProductsLabelMenu)
                                .addComponent(productslabelmenu))
                            .addGap(203, 203, 203))
                        .addGroup(SidabarLayout.createSequentialGroup()
                            .addComponent(adminilabelmenu)
                            .addGap(224, 224, 224)))
                    .addGroup(SidabarLayout.createSequentialGroup()
                        .addGroup(SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customers, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SidabarLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(userlabelmenu)))
                        .addGap(115, 115, 115))))
        );
        SidabarLayout.setVerticalGroup(
            SidabarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidabarLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(saleslabel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(saleslabelmenu)
                .addGap(18, 18, 18)
                .addComponent(ProductsLabelMenu)
                .addGap(18, 18, 18)
                .addComponent(productslabelmenu)
                .addGap(18, 18, 18)
                .addComponent(stats)
                .addGap(18, 18, 18)
                .addComponent(statisticslabelmenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customers, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(customerslabelmenu)
                .addGap(18, 18, 18)
                .addComponent(adminilabelmenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userlabelmenu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Submenu1.setBackground(new java.awt.Color(246, 246, 246));
        Submenu1.setForeground(new java.awt.Color(228, 228, 228));
        Submenu1.setLayout(new java.awt.CardLayout());

        userssubmenu1.setBackground(new java.awt.Color(239, 235, 235));

        todays.setForeground(new java.awt.Color(0, 0, 128));
        todays.setText("today");

        editlabeluserssumenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-edit-32.png"))); // NOI18N
        editlabeluserssumenu.setText("Edit");
        editlabeluserssumenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editlabeluserssumenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editlabeluserssumenuMouseClicked(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-ban-32.png"))); // NOI18N
        jLabel2.setText("Deactivate");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-ok-32.png"))); // NOI18N
        jLabel4.setText("Activate");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout userssubmenu1Layout = new javax.swing.GroupLayout(userssubmenu1);
        userssubmenu1.setLayout(userssubmenu1Layout);
        userssubmenu1Layout.setHorizontalGroup(
            userssubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userssubmenu1Layout.createSequentialGroup()
                .addComponent(editlabeluserssumenu, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 420, Short.MAX_VALUE)
                .addComponent(todays)
                .addGap(64, 64, 64))
        );
        userssubmenu1Layout.setVerticalGroup(
            userssubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userssubmenu1Layout.createSequentialGroup()
                .addGap(0, 30, Short.MAX_VALUE)
                .addGroup(userssubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(userssubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4))
                    .addGroup(userssubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(todays)
                        .addComponent(editlabeluserssumenu)))
                .addContainerGap())
        );

        Submenu1.add(userssubmenu1, "card6");

        salessubmenu1.setBackground(new java.awt.Color(239, 235, 235));

        javax.swing.GroupLayout salessubmenu1Layout = new javax.swing.GroupLayout(salessubmenu1);
        salessubmenu1.setLayout(salessubmenu1Layout);
        salessubmenu1Layout.setHorizontalGroup(
            salessubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 776, Short.MAX_VALUE)
        );
        salessubmenu1Layout.setVerticalGroup(
            salessubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        Submenu1.add(salessubmenu1, "card3");

        rolesnpermissionsubmenu1.setBackground(new java.awt.Color(239, 235, 235));

        editpermrole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/v2icons8-edit-32.png"))); // NOI18N
        editpermrole.setText("Edit Role");
        editpermrole.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editpermrole.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editpermroleMouseClicked(evt);
            }
        });

        deleteRowPermissisonLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteicon.png"))); // NOI18N
        deleteRowPermissisonLabel.setText("Delete Role/Permission");
        deleteRowPermissisonLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deleteRowPermissisonLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteRowPermissisonLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout rolesnpermissionsubmenu1Layout = new javax.swing.GroupLayout(rolesnpermissionsubmenu1);
        rolesnpermissionsubmenu1.setLayout(rolesnpermissionsubmenu1Layout);
        rolesnpermissionsubmenu1Layout.setHorizontalGroup(
            rolesnpermissionsubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rolesnpermissionsubmenu1Layout.createSequentialGroup()
                .addComponent(editpermrole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteRowPermissisonLabel)
                .addContainerGap(515, Short.MAX_VALUE))
        );
        rolesnpermissionsubmenu1Layout.setVerticalGroup(
            rolesnpermissionsubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rolesnpermissionsubmenu1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(rolesnpermissionsubmenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editpermrole)
                    .addComponent(deleteRowPermissisonLabel))
                .addContainerGap())
        );

        Submenu1.add(rolesnpermissionsubmenu1, "card3");

        TablePanel.setLayout(new java.awt.CardLayout());

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                .addContainerGap())
        );
        SalesTablePanelLayout.setVerticalGroup(
            SalesTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesTablePanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(SalesTablePanel, "card2");

        BrandsTablePanel.setBackground(new java.awt.Color(51, 0, 255));
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        BrandsTablePanelLayout.setVerticalGroup(
            BrandsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BrandsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        categoryTablePanelLayout.setVerticalGroup(
            categoryTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoryTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        ProductsTablePanelLayout.setVerticalGroup(
            ProductsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        StocksTablePanelLayout.setVerticalGroup(
            StocksTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StocksTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DailyReportTablePanelLayout.setVerticalGroup(
            DailyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DailyReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        WeeklyReportTablePanelLayout.setVerticalGroup(
            WeeklyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WeeklyReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        MonthlyReportTablePanelLayout.setVerticalGroup(
            MonthlyReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MonthlyReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        DateReportTablePanelLayout.setVerticalGroup(
            DateReportTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DateReportTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
        UsersTablePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UsersTablePanelMouseClicked(evt);
            }
        });

        staffTable.setAutoCreateRowSorter(true);
        staffTable.setBackground(new java.awt.Color(242, 242, 242));
        staffTable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, new java.awt.Color(153, 153, 153)));
        staffTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
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
        staffTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        staffTable.setOpaque(false);
        staffTable.setRowHeight(40);
        staffTable.setShowGrid(true);
        staffTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                staffTableMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(staffTable);

        javax.swing.GroupLayout UsersTablePanelLayout = new javax.swing.GroupLayout(UsersTablePanel);
        UsersTablePanel.setLayout(UsersTablePanelLayout);
        UsersTablePanelLayout.setHorizontalGroup(
            UsersTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsersTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        UsersTablePanelLayout.setVerticalGroup(
            UsersTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsersTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        NotificationsTablePanelLayout.setVerticalGroup(
            NotificationsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NotificationsTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
        transferedCash.setFocusable(false);
    
        jScrollPane12.setViewportView(transferedCash);

        javax.swing.GroupLayout CashTablePanelLayout = new javax.swing.GroupLayout(CashTablePanel);
        CashTablePanel.setLayout(CashTablePanelLayout);
        CashTablePanelLayout.setHorizontalGroup(
            CashTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CashTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        CashTablePanelLayout.setVerticalGroup(
            CashTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CashTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addContainerGap())
        );

        TablePanel.add(CashTablePanel, "card2");

        PermissionsTree.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        PermissionsTree.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Permissions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 51, 255))); // NOI18N
        PermissionsTree.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        PermissionsTree.setForeground(new java.awt.Color(0, 0, 255));
        PermissionsTree.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PermissionsTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PermissionsTreeMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(PermissionsTree);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 0, 255));
        jLabel3.setText("Roles and Permission");

        NewRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewRoleActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Role Name");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-save-32.png"))); // NOI18N
        jLabel6.setText("Save");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        rolesJlist.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Roles", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 51, 255))); // NOI18N
        rolesJlist.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rolesJlist.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        rolesJlist.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rolesJlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rolesJlistMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(rolesJlist);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/icons8-save-32.png"))); // NOI18N
        jLabel8.setText("Map Permission");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        mappedTree.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        mappedTree.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mappedTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mappedTreeMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(mappedTree);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Description");

        javax.swing.GroupLayout RolesPermissionsLayout = new javax.swing.GroupLayout(RolesPermissions);
        RolesPermissions.setLayout(RolesPermissionsLayout);
        RolesPermissionsLayout.setHorizontalGroup(
            RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RolesPermissionsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RolesPermissionsLayout.createSequentialGroup()
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane15))
                    .addGroup(RolesPermissionsLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(NewRole, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        RolesPermissionsLayout.setVerticalGroup(
            RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RolesPermissionsLayout.createSequentialGroup()
                .addGroup(RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RolesPermissionsLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NewRole, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RolesPermissionsLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RolesPermissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RolesPermissionsLayout.createSequentialGroup()
                        .addComponent(jScrollPane14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(jScrollPane15)
                    .addComponent(jScrollPane13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablePanel.add(RolesPermissions, "card14");

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(InfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(menuLayout.createSequentialGroup()
                .addComponent(Sidabar, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Submenu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(Submenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(TablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
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
        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

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

        showMenu.setText("Show");
        showMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuBar1.add(showMenu);

        CashMenu.setText("Cash");
        jMenuBar1.add(CashMenu);

        BrandsMenu.setText("Brands");
        BrandsMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BrandsMenuMouseClicked(evt);
            }
        });

        jMenuItem1.setText("View");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        BrandsMenu.add(jMenuItem1);

        jMenuItem2.setText("Register");
        BrandsMenu.add(jMenuItem2);

        jMenuBar1.add(BrandsMenu);

        jMenu1.setText("Inventory");

        jMenuItem3.setText("Manage");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Test Data");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void QuitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitMenuActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_QuitMenuActionPerformed

    private void newsaleLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newsaleLabelMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        try {
            Sale sale = new Sale();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_newsaleLabelMouseClicked

    private void saleslabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleslabelMouseClicked
        TablePanel.removeAll();
        TablePanel.add(SalesTablePanel);
        TablePanel.repaint();
        TablePanel.revalidate();

        Submenu.removeAll();
        Submenu.add(SalesHeader);
        Submenu.repaint();
        Submenu.revalidate();

        Submenu1.removeAll();
        Submenu1.add(salessubmenu1);
        Submenu1.repaint();
        Submenu1.revalidate();

        try {
            // TODO add your handling code here:
            getActiveClass("saleslabel");
            loadJtableValues();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saleslabelMouseClicked

    private void statsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsMouseClicked
        // TODO add your handling code here:
        TablePanel.removeAll();
        TablePanel.add(DailyReportTablePanel);
        TablePanel.repaint();
        TablePanel.revalidate();
        getActiveClass("stats");
    }//GEN-LAST:event_statsMouseClicked

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
        Boolean selected = ((Boolean) SellingModel.getValueAt(row, 0));
        if (selected == true) {
            try {
                SaveSale editSale;
                SaveSale.setMeUp(getProductDetails());
            } catch (ClassNotFoundException | ParseException ex) {
                Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_EditLabelMouseClicked

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

    private void saleslabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleslabelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_saleslabelMouseEntered

    private void adminilabelmenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminilabelmenuMouseClicked
        try {
            adminIconClicked();
        } catch (SQLException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_adminilabelmenuMouseClicked

    private void ProductsLabelMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProductsLabelMenuMouseClicked
        TablePanel.removeAll();
        TablePanel.add(ProductsTablePanel);
        TablePanel.repaint();
        TablePanel.revalidate();

        Submenu.removeAll();
        Submenu.add(ProductsHeader);
        Submenu.repaint();
        Submenu.revalidate();
        getActiveClass("box");

    }//GEN-LAST:event_ProductsLabelMenuMouseClicked

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
        // TODO add your handling code here:
        //See all the brands.
    }//GEN-LAST:event_jMenuItem1MouseClicked

    private void BrandsMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BrandsMenuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BrandsMenuMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        this.add(SalesTablePanel);
        this.validate();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:

        try {
            LoadStocks();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
        TablePanel.removeAll();
        TablePanel.add(StocksTablePanel);
        TablePanel.repaint();
        TablePanel.revalidate();

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void InventoryLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventoryLabelMouseClicked
        // TODO add your handling code here:

        try {
            LoadStocks();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
        TablePanel.removeAll();
        TablePanel.add(StocksTablePanel);
        TablePanel.repaint();
        TablePanel.revalidate();
    }//GEN-LAST:event_InventoryLabelMouseClicked

    private void newproducy1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newproducy1MouseClicked
        // TODO add your handling code here:
        RegisterProduct.main(allCats);
    }//GEN-LAST:event_newproducy1MouseClicked

    private void permissionnRolesLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_permissionnRolesLabelMouseClicked

        java.util.List<PermissionGroup> permissionGroups = null;
        //Panels showing:
        TablePanel.removeAll();
        TablePanel.add(RolesPermissions);
        TablePanel.repaint();
        TablePanel.revalidate();

        Submenu1.removeAll();
        Submenu1.add(rolesnpermissionsubmenu1);
        Submenu1.repaint();
        Submenu1.revalidate();

        LoadRoles();
        LoadPermissions();
        LoadPermissionsRoles();

    }//GEN-LAST:event_permissionnRolesLabelMouseClicked

    private void newUserLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newUserLabelMouseClicked
        try {
            RegisterNewUser.main();
        } catch (ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_newUserLabelMouseClicked

    private void UsersTablePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsersTablePanelMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_UsersTablePanelMouseClicked

    private void editlabeluserssumenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editlabeluserssumenuMouseClicked

        // TODO add your handling code here:
        SelectedStaff selectedstaff = selectedStaff.get(selectedStaff.size() - 1);
        System.out.println("2537 line UIv2 "+selectedstaff.role);
        try {
            EditUser.main(selectedstaff);
        } catch (ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_editlabeluserssumenuMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        int selectedRow = staffTable.getSelectedRow();
        String staffName = (String) StaffsModel.getValueAt(selectedRow, 0);
        String userId = userIds.get(selectedRow);
        String message = "<html><div style='font-size:14px;'>Are you sure you want to disable" + staffName + "?</div></html>";
        int confirm = JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Call the function to update user status
            boolean updated = false;
            try {
                updated = updateStaffStatus(userId, 0);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (updated) {
                JOptionPane.showMessageDialog(null,
                        "User status updated successfully.");

                try {
                    LoadSatffs();
                } catch (ParseException | ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
                }

                TablePanel.removeAll();
                TablePanel.add(UsersTablePanel);
                TablePanel.repaint();
                TablePanel.revalidate();

            } else {
                JOptionPane.showMessageDialog(null, "Failed to update user status.");
            }
        }
        //Sales_Staffs.addtestUsers();
    }//GEN-LAST:event_jLabel2MouseClicked

    public static boolean updateStaffStatus(String staffId, int type) throws SQLException, ClassNotFoundException {
        // Call the editStaff method with the staff ID
        try {
            return Sales_Staffs.StaffStatus(staffId, type);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
            // Log the exception
            return false; // Return false in case of exception
        }

    }

    private void staffTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_staffTableMouseClicked
        row = staffTable.getSelectedRow();
        column = staffTable.getColumnCount();
        int selectedRow = staffTable.getSelectedRow();
        // Check if a row is selected
        if (selectedRow != -1) {
            // Get the values from the selected row
            String staffName = StaffsModel.getValueAt(selectedRow, 0).toString();
            String surName = StaffsModel.getValueAt(selectedRow, 1).toString();
            String staffEmail = StaffsModel.getValueAt(selectedRow, 2).toString();
            String phoneNo = StaffsModel.getValueAt(selectedRow, 3).toString();
            String store = StaffsModel.getValueAt(selectedRow, 4).toString();
            String status = StaffsModel.getValueAt(selectedRow, 5).toString();
            String managerId = StaffsModel.getValueAt(selectedRow, 6).toString();
            String userId = userIds.get(selectedRow);
            Object roleObj = StaffsModel.getValueAt(selectedRow, 7);
            String role = null;
            if (roleObj instanceof Integer) {
                role = (String) roleObj;
            } else if (roleObj instanceof String) {
                try {
                    role = (String) roleObj;
                    
                } catch (NumberFormatException e) {
                    // Handle the case where the value cannot be parsed to an integer
                    e.printStackTrace();
                    // Provide a default value or handle the error as appropriate
                }
            } else {
                // Handle the case where the value is neither an integer nor a string
            }

            // Create an object with the retrieved values
            // Create a new Staff object and add it to the userDetails ArrayList
            selectedStaff.add(new SelectedStaff(staffName, surName,
                    staffEmail, phoneNo, store, status,
                    managerId, role, userId));
            // Pass the object to the editUser me       
        } else {
            // No row is selected
            JOptionPane.showMessageDialog(null, "No row is selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_staffTableMouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        int selectedRow = staffTable.getSelectedRow();
        String staffName = (String) StaffsModel.getValueAt(selectedRow, 0);
        String userId = userIds.get(selectedRow);
        String message = "<html><div style='font-size:14px;'>Are you sure you want to enable " + staffName + "?</div></html>";
        int confirm = JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Call the function to update user status
            boolean updated = false;
            try {
                updated = updateStaffStatus(userId, 1);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (updated) {
                JOptionPane.showMessageDialog(null,
                        "User status updated successfully.");
                try {
                    LoadSatffs();
                } catch (ParseException | ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
                }

                TablePanel.removeAll();
                TablePanel.add(UsersTablePanel);
                TablePanel.repaint();
                TablePanel.revalidate();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update user status.");
            }
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        try {
            // TODO add your handling code here:
            addtestUsers();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        String roleName = NewRole.getText();
        String desc = description.getText();
        try {
            addRole(roleName, desc);
        } catch (SQLException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
        String message = "Role created successfully";
        showPopupNotification(message, NotificationManager.NotificationType.INFO);
        showConsoleNotification(message, NotificationManager.NotificationType.INFO);
        LoadRoles();
        TablePanel.repaint();
        TablePanel.revalidate();
        LoadPermissions();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void NewRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewRoleActionPerformed

    private void PermissionsTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PermissionsTreeMouseClicked
// For PermissionsTree (JTree)
        TreePath selectedPath = PermissionsTree.getSelectionPath();
        if (selectedPath != null) {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
            selectedNode = selected.toString(); // Convert selected node to String
            // Do something with the selected node
            //  renameRoleOrDeleteRow(PermissionsTree);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_PermissionsTreeMouseClicked

    private void rolesJlistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rolesJlistMouseClicked
// For rolesJlist (JList)
        Object Value = rolesJlist.getSelectedValue();
        if (Value != null) {
            selectedValue = (String) Value;
            SelectedComponent = new JList();
            //   renameRoleOrDeleteRow(rolesJlist);
            // Do something with the selected value
        }        // TODO add your handling code here:
    }//GEN-LAST:event_rolesJlistMouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        if (PermissionsTree.getSelectionCount() == 0 || rolesJlist.getSelectedIndex() == -1) {
            String message = "";
            if (PermissionsTree.getSelectionCount() == 0) {
                message += "Please select a permission from the Permissions Tree.\n";

            }
            if (rolesJlist.getSelectedIndex() == -1) {
                message += "Please select a role from the Roles List.";
            }
            showPopupNotification(message, NotificationManager.NotificationType.ERROR);
            showConsoleNotification(message, NotificationManager.NotificationType.ERROR);
            return;
        } else {
            PermissionFileManager pm = new PermissionFileManager();
            pm.addPermissionsRole(selectedValue, selectedNode);
            LoadPermissionsRoles();
        }

    }//GEN-LAST:event_jLabel8MouseClicked

    private void editpermroleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editpermroleMouseClicked
        //Incase the user selects the delete we will want to see
        //what did she clicked last between JLIST OR JTREE.:
        // Rename the role in the database using selectedValue
        // Call RoleInputDialog class and pass the role name value

        renameRoleOrDeleteRow(SelectedComponent, 1);
        LoadRoles();
    }//GEN-LAST:event_editpermroleMouseClicked

    // Method to rename role or delete row based on selected component
    public void renameRoleOrDeleteRow(Component selectedComponent, int type) {
        if (selectedComponent instanceof JList) {
            // If JList is clicked
            if (type == 0) {
                deleteRow(selectedValue);
            } else {
                RoleInputDialog.showInputDialog(selectedValue);
            }

        } else if (selectedComponent instanceof JTree) {
            // If JTree is clicked
            // Delete the row completely from the database using selectedNode
            deletePermissionRow(selectedNode);
        }
    }


    private void mappedTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mappedTreeMouseClicked
        // TODO add your handling code here:
        SelectedComponent = new JTree();
        // For PermissionsTree (JTree)
        TreePath selectedPath = mappedTree.getSelectionPath();
        if (selectedPath != null) {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
            selectedNode = selected.toString(); // Convert selected node to String
            System.out.println("this is the selected tree value" + selectedNode + " and " + selected);
        }
    }//GEN-LAST:event_mappedTreeMouseClicked

    private void deleteRowPermissisonLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteRowPermissisonLabelMouseClicked
        // TODO add your handling code here:
        //0 for delete
        //1 for edit
        renameRoleOrDeleteRow(SelectedComponent, 0);
        LoadRoles();
        LoadPermissions();
        LoadPermissionsRoles();
    }//GEN-LAST:event_deleteRowPermissisonLabelMouseClicked

    private void PrintReceiptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PrintReceiptMouseClicked

    }//GEN-LAST:event_PrintReceiptMouseClicked

    // Method to get the instance of UIv2
    public static UIv2 getInstance() throws SQLException {
        if (Instance == null) {
            try {
                Instance = new UIv2();
            } catch (ClassNotFoundException | ParseException ex) {
                Logger.getLogger(EditUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Instance;
    }

    public static void callAdminIconClicked() throws SQLException {
        // Get the existing instance of UIv2
        UIv2 uiv = UIv2.getInstance();

        // Call the adminIconClicked() method
        uiv.adminIconClicked();
    }

    public void adminIconClicked() throws SQLException {
        try {
            LoadSatffs();
        } catch (ParseException | ClassNotFoundException ex) {
            Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
        }

        JTableHeader Sheader = staffTable.getTableHeader();
        Sheader.setBackground(new Color(194, 218, 245));
        Sheader.setForeground(new Color(0, 0, 128));
        Sheader.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        // Align the header text to the left
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) staffTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        getActiveClass("admin");

        TablePanel.removeAll();
        TablePanel.add(UsersTablePanel);
        TablePanel.repaint();
        TablePanel.revalidate();

        Submenu.removeAll();
        Submenu.add(UsersHeader);
        Submenu.repaint();
        Submenu.revalidate();

        Submenu1.removeAll();
        Submenu1.add(userssubmenu1);
        Submenu1.repaint();
        Submenu1.revalidate();
    }

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

    public static void main(String args[]) {

            System.setProperty("sun.java2d.dpiaware", "false");
            // Retrieve the current user role
            FlatGitHubIJTheme.setup();

        java.awt.EventQueue.invokeLater(() -> {
            try {
                new UIv2().setVisible(true);    
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | ParseException | SQLException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(UIv2.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu BrandsMenu;
    private javax.swing.JPanel BrandsTablePanel;
    private javax.swing.JLabel CancelLabel;
    private javax.swing.JMenu CashMenu;
    private javax.swing.JPanel CashTablePanel;
    private javax.swing.JPanel DailyReportTablePanel;
    private javax.swing.JPanel DateReportTablePanel;
    private javax.swing.JLabel EditLabel;
    private javax.swing.JTable EmailTable;
    private javax.swing.JLabel ExchangeLabel;
    private javax.swing.JPanel InfoPanel;
    private javax.swing.JLabel InventoryLabel;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JPanel MonthlyReportTablePanel;
    private javax.swing.JTextField NewRole;
    private javax.swing.JPanel NotificationsTablePanel;
    private javax.swing.JLabel OrderLabel;
    private javax.swing.JTree PermissionsTree;
    private javax.swing.JLabel PrintReceipt;
    private javax.swing.JPanel ProductsHeader;
    private javax.swing.JLabel ProductsLabelMenu;
    private javax.swing.JPanel ProductsTablePanel;
    private javax.swing.JMenuItem QuitMenu;
    private javax.swing.JPanel RolesPermissions;
    private javax.swing.JPanel SalesHeader;
    private javax.swing.JTable SalesTable;
    private javax.swing.JPanel SalesTablePanel;
    private static javax.swing.JLabel SetEmailNotification;
    private javax.swing.JPanel Sidabar;
    private javax.swing.JPanel StocksTablePanel;
    private javax.swing.JPanel Submenu;
    private javax.swing.JPanel Submenu1;
    public javax.swing.JPanel TablePanel;
    private javax.swing.JMenu TopMenu;
    private javax.swing.JPanel UsersHeader;
    public javax.swing.JPanel UsersTablePanel;
    private javax.swing.JPanel WeeklyReportTablePanel;
    public static javax.swing.JLabel adminilabelmenu;
    private javax.swing.JTable brandsTable;
    private javax.swing.JTable categoryTable;
    private javax.swing.JPanel categoryTablePanel;
    private javax.swing.JLabel customers;
    private javax.swing.JLabel customerslabelmenu;
    private javax.swing.JTable dailySalesTable;
    private javax.swing.JTable datedreporttable;
    private javax.swing.JLabel deleteRowPermissisonLabel;
    private javax.swing.JTextField description;
    private javax.swing.JLabel editlabeluserssumenu;
    private javax.swing.JLabel editpermrole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTree mappedTree;
    private javax.swing.JPanel menu;
    private javax.swing.JTable monthlyreporttable;
    private javax.swing.JLabel newUserLabel;
    private javax.swing.JLabel newproducy1;
    private javax.swing.JLabel newsaleLabel;
    private javax.swing.JLabel permissionnRolesLabel;
    private javax.swing.JTable productsTable;
    private javax.swing.JLabel productslabelmenu;
    private javax.swing.JLabel profit;
    private javax.swing.JLabel profit1;
    private javax.swing.JLabel profit2;
    private javax.swing.JLabel profit3;
    private javax.swing.JList<String> rolesJlist;
    private javax.swing.JPanel rolesnpermissionsubmenu1;
    private javax.swing.JLabel saleslabel;
    private javax.swing.JLabel saleslabelmenu;
    private javax.swing.JPanel salessubmenu1;
    private javax.swing.JMenu showMenu;
    private javax.swing.JTable staffTable;
    private javax.swing.JLabel statisticslabelmenu;
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
    private javax.swing.JLabel userlabelmenu;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JPanel userssubmenu1;
    private javax.swing.JLabel version;
    private javax.swing.JTable weeklyreporttable;
    // End of variables declaration//GEN-END:variables

}
// Customize the code to set the color for each column in JTable

class MyRenderer extends DefaultTableCellRenderer {

    Color bg, fg;

    public MyRenderer(Color bg, Color fg) {
        super();
        this.bg = bg;
        this.fg = fg;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        cell.setBackground(bg);
        cell.setForeground(fg);
        return cell;
    }
}
