package com.example.farmbnb;

// Class used to store a full booking's information
public class FinalBooking {
    private int customerID;
    private String accommName;
    private String arrDate;
    private String depDate;
    private int bookingID;

    public FinalBooking(int id, String accommodation, String arrivalDate, String departureDate) {
        customerID = id;
        accommName = accommodation;
        arrDate = arrivalDate;
        depDate = departureDate;
        bookingID = assignBookingID();
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getAccommName() {
        return accommName;
    }

    public String getArrDate() {
        return arrDate;
    }

    public String getDepDate() {
        return depDate;
    }

    public int getBookingID() {
        return bookingID;
    }

    // In the final product would be assigned its own unique value for storage
    private int assignBookingID() {
        int id = 0;

        return id;
    }
}
