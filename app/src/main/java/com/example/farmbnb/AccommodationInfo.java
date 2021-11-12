package com.example.farmbnb;

public class AccommodationInfo {
    private String accommName;
    private String arrivalDate;
    private String departureDate;

    public AccommodationInfo(String accomm, String arrDate, String depDate) {
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
