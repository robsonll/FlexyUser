package com.example.flexyuser.ModelClasses;

public class Address
{
    private String addressTitle;
    private String aptNumber;
    private String postalCode;
    private String city;
    private String province;

    public Address(){}

    public Address(String addressTitle, String aptNumber, String postalCode, String city, String province) {
        this.addressTitle = addressTitle;
        this.aptNumber = aptNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.province = province;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAptNumber() {
        return aptNumber;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

}
