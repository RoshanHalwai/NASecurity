package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

public class NammaApartmentEmergency {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String apartmentName;
    private String emergencyType;
    private String flatNumber;
    private String fullName;
    private String phoneNumber;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentEmergency() {
    }

    public NammaApartmentEmergency(String apartmentName, String emergencyType, String flatNumber, String fullName, String phoneNumber) {
        this.apartmentName = apartmentName;
        this.emergencyType = emergencyType;
        this.flatNumber = flatNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getApartmentName() {
        return apartmentName;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
