package com.yefindia.intern.charityfirstcenter.Model;

public class UserData {

    private String email;
    private String userId;
    private String sellerStatus;

    public UserData(){}


    public UserData(String userId,String email) {
        this.email = email;
        this.userId = userId;

        //Initial Seller status null

        this.sellerStatus = "None";
    }

    public String getEmail() {
        return email;
    }

    public String getuserId() {
        return userId;
    }

    public String getSellerStatus() {
        return sellerStatus;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }
}
