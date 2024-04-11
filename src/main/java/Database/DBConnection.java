/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.CleanResult;
import org.flywaydb.core.api.output.MigrateResult;
import org.flywaydb.core.api.output.RepairResult;

/**
 *
 * @author Nkanabo
 */
public class DBConnection {

    /**
     * H2 Database Connection
     */
    private static DBConnection dbConnection = null;

    /**
     * Database username
     */
    private Optional<String> username = Optional.of("root");

    /**
     * Database password
     */
    private Optional<String> password = Optional.of("tazamaroad");

    /**
     * Database Location
     */
    private Optional<String> url = Optional.of("~/Tienda");

    private Connection connection = null;

    /**
     * Creating an Instance of Database Connection, which we will be accessible
     * in entire applications
     *
     * @return
     */
    public static DBConnection getConnectionInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    /**
     * Return Connection and create it when is null
     *
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
//    public Connection getConnection() throws SQLException,
//            ClassNotFoundException {
//        if (connection == null) {
//            try {
//                Class.forName("org.h2.Driver");
//            } catch (ClassNotFoundException ex) {
//                System.out.println(ex.getMessage());
//            }
//
//            connection = DriverManager.getConnection(
//                    "jdbc:h2:" + url.get() + "/Tienda;" + "DATABASE_TO_UPPER=false;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE",
//                    username.get(),
//                    password.get()
//            );
//        }
//        return connection;
//    }
    
    public Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            // Handle the exception appropriately (e.g., log it)
            throw new SQLException("H2 Driver not found", ex);
        }

        String jdbcUrl = String.format("jdbc:h2:%s/Tienda;"+"DATABASE_TO_UPPER=false",
                url.get());

        try {
            connection = DriverManager.getConnection(jdbcUrl, username.get(), password.get());
        } catch (SQLException ex) {
            // Handle the exception appropriately (e.g., log it)
            throw new SQLException("Failed to obtain H2 connection", ex);
        }
    }
    return connection;
}


    /**
     * Setting Database username
     *
     * @param username
     */
    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    /**
     * Setting Database password
     *
     * @param password
     */
    public void setPassword(Optional<String> password) {
        this.password = password;
    }

    /**
     * Setting Database url
     *
     * @param url
     */
    public void setUrl(Optional<String> url) {
        this.url = url;
    }

    /**
     * Close Connection only if Connection is available otherwise do nothing
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Checking if Connection is Available
     *
     * @return
     */
    public boolean isAvailable() {
        return connection != null;
    }

    public void repair() {
        String uri = "jdbc:h2:" + url.get() + "/Tienda";
        String user = username.get();
        String pass = password.get();
        String urlc = "jdbc:h2:" + url.get() + "/Tienda;DATABASE_TO_UPPER=false";
        CleanResult flyway
                = Flyway.configure()
                        .dataSource(urlc, user, pass)
                        .defaultSchema("Tienda")
                        .schemas("Tienda")
                        .baselineOnMigrate(true)
                        .cleanDisabled(false)
                        .load()
                        .clean();
    }
    
    public void migrate() {
        String uri = "jdbc:h2:" + url.get() + "/Tienda;IFEXISTS=FALSE;DATABASE_TO_UPPER=false";
        String user = username.get();
        String pass = password.get();
        MigrateResult flyway;
        flyway = Flyway
                .configure()
                .dataSource(uri, user, pass)
                .schemas("Tienda")
                .defaultSchema("Tienda")
                .baselineOnMigrate(true)
                .load()
                .migrate();
    }
}
