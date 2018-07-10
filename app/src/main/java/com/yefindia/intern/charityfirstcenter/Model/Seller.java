package com.yefindia.intern.charityfirstcenter.Model;

public class Seller {

    private String name, organization, address, phone;

    public Seller(){ };

    public Seller(String name, String organization, String address, String phone) {
        this.name = name;
        this.organization = organization;
        this.address = address;
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
