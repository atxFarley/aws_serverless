package fieldtracker;

import java.sql.Timestamp;
import java.io.Serializable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FieldUser implements Serializable, Cloneable{

    private static final long serialVersionUID = -2407033402694059862L;

    private Integer fieldUserId;
    private String lastName;
    private String firstName;
    private boolean appAdmin;
    private String appLogin;
    private String appPassword;
    private String email;
    private Timestamp createdDate;
    private Timestamp editDate;
    private String listDisplayName;


    public Integer getFieldUserId() {
        return fieldUserId;
    }

    public void setFieldUserId(Integer fieldUserId) {
        this.fieldUserId = fieldUserId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isAppAdmin() {
        return appAdmin;
    }

    public void setAppAdmin(boolean appAdmin) {
        this.appAdmin = appAdmin;
    }

    public String getAppLogin() {
        return appLogin;
    }

    public void setAppLogin(String appLogin) {
        this.appLogin = appLogin;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getEditDate() {
        return editDate;
    }

    public void setEditDate(Timestamp editDate) {
        this.editDate = editDate;
    }

    public String getListDisplayName() {
        return listDisplayName;
    }

    public void setListDisplayName(String listDisplayName) {
        this.listDisplayName = listDisplayName;
    }
}
