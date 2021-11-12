package com.example.farmbnb;

public class CustomerInfo {
    private String name;
    private String address;
    private String phone;

    public CustomerInfo(String custName, String custAddress, String custPhoneNo) {
        name = custName;
        address = custAddress;
        phone = custPhoneNo;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
