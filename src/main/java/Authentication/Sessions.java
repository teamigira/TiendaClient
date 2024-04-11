/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Authentication;

/**
 *
 * @author Nkanabo
 */
public class Sessions {
    public static String LoggedUser;
    public static String LoggedUserId;
    private static Sessions instance;
    public static String currentUserRole;

    public Sessions() {
        // Private constructor to prevent instantiation
    }

    public static synchronized Sessions getInstance() {
        if (instance == null) {
            instance = new Sessions();
        }
        return instance;
    }

    public void setLoggedUser(String user) {
        Sessions.LoggedUser = user;
    }

    public void setLoggedUserId(String userId) {
        Sessions.LoggedUserId = userId;
    }

    public String getLoggedUserId() {
        return LoggedUserId;
    }

    public String getLoggedUser() {
        return LoggedUser;
    }

    public void setCurrentUserRole(String role) {
        Sessions.currentUserRole = role;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }
}
