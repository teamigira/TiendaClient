package Classes.AbstractClasses;

public class UserData {
    public SelectedStaff selectedStaff;
    public boolean changePassword;

    public UserData(SelectedStaff selectedStaff, boolean changePassword) {
        this.selectedStaff = selectedStaff;
        this.changePassword = changePassword;
    }

    public SelectedStaff getStaff() {
        return selectedStaff;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

}
