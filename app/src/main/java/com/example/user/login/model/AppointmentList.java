package com.example.user.login.model;

public class AppointmentList {

    private String firstname;
    private String middlename;
    private String lastname;
    private String diag;
    private String App_Id;
    private String id;


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDiag() {
        return diag;
    }

    public void setDiag(String diag) {
        this.diag = diag;
    }

    public String getApp_Id() {
        return App_Id;
    }

    public void setApp_Id(String app_Id) {
        App_Id = app_Id;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}
}
