package com.example.farmbnb;

// Class used to store customer info
public class Customer {
    private int id;
    private String name;
    private String address;
    private String phone;

    public Customer(int customerID, String custName, String custAddress, String custPhoneNo) {
        id = customerID;
        name = custName;
        address = custAddress;
        phone = custPhoneNo;
    }

    public int getId() {
        return id;
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
