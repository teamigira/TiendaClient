package Classes.AbstractClasses;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Nkanabo
 */
public class EditedUserData {

    public EditedUser editedUser;
    public boolean changePassword;

    public EditedUserData(EditedUser editedUser, boolean changePassword) {
        this.editedUser = editedUser;
        this.changePassword = changePassword;
    }

    public EditedUser getStaff() {
        return editedUser;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

}
