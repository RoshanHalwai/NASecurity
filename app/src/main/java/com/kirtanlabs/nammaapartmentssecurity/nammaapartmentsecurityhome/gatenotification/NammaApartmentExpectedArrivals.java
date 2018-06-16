package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

public class NammaApartmentExpectedArrivals {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String dateAndTimeOfArrival;
    private String reference;
    private String uid;
    private String validFor;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentExpectedArrivals() {
    }

    public NammaApartmentExpectedArrivals(String dateAndTimeOfArrival, String reference, String uid, String validFor) {
        this.dateAndTimeOfArrival = dateAndTimeOfArrival;
        this.reference = reference;
        this.uid = uid;
        this.validFor = validFor;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getDateAndTimeOfArrival() {
        return dateAndTimeOfArrival;
    }

    public String getReference() {
        return reference;
    }

    public String getUid() {
        return uid;
    }

    public String getValidFor() {
        return validFor;
    }
}
