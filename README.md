# TiendaClient

Tienda Client is a Java-based application designed for field operations. It serves as an environment for various functionalities within a business setting.

## Entry Point

The project's entry point is at `Tienda.com.nkanabo.main`. Here, there is a thorough check to verify if the product is licensed. Following successful verification, normal authentication processes ensue.

## Modules

Currently, the application consists of at least four main modules:

1. Sales Management
2. Product Management
3. Stock Management
4. Reporting
5. User Management

## Dependencies

The system dependencies are outlined in the POM file, with a critical dependency being the H2 database.

## Adding a Role

To add a role, follow these steps:

1. Set the UI component's enable option to `false`.
2. Assign the UI component its associated permission.
   For example, in the `loadedpermissions` function, the component `adminlabel` is associated with the permission `view_users`:
   ```
   loadedpermissions.put("adminLabelMenu", "view_users");
   ```
3. The component should then be added to a switch case that re-enables it as needed.
4. Ensure that the component is set to enabled and is included in the switch case.
5. To disable the onclick function, add the following line at the top of the function:
   ```java
   if (!ProductsLabelMenu.isEnabled()) {
       return; // Do nothing if the button is disabled
   }
   ```
   With this line added, when a disabled button is clicked, no code will be executed.

## Backend Management

For backend management, permissions need to be registered to reside in the database. Follow these steps:

1. Check any category or create a new category that adequately represents the permissions.
   For example:
   ```java
   String[] salesPermissions = {"view_sales","sale"};
   ```
2. Call the function that will insert the category and the permission into the database:
   ```java
   insertPermissions(stmt, salesPermissions, "sales");
   ```
3. If the category you created does not exist, be sure to register it into administrators. Admins should have access to all roles and accounts.
   ```java
   addPermissionsForRole(roleName, reportsPermissions);
   ```
   If you just add the permission, it will be automatically added.

## Resources

The application's UI utilizes FlatLaf. You can find the source at [FlatLaf GitHub Repository](https://github.com/JFormDesigner/FlatLaf/tree/main/flatlaf-intellij-themes).