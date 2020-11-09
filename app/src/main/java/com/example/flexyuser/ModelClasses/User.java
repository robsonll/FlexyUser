package com.example.flexyuser.ModelClasses;

import java.util.ArrayList;

public class User {

    private String id;
    private String name;
    private String email;
    private ArrayList<Address> addressList = new ArrayList<Address>();
    private int loginType;

    public  User(){}

    public User(String id, String name, String email, ArrayList<Address> addressList, int loginType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.addressList = addressList;
        this.loginType = loginType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

}
