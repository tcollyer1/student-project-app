package com.example.farmbnb;

// Class used to temporarily store booking-related info on the first booking page
public class BookingInfo {
    private String accommName;
    private String arrivalDate;
    private String departureDate;

    public BookingInfo(String accomm, String arrDate, String depDate) {
        accommName = accomm;
        arrivalDate = arrDate;
        departureDate = depDate;
    }

    public String getAccommName() {
        return accommName;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }
}
