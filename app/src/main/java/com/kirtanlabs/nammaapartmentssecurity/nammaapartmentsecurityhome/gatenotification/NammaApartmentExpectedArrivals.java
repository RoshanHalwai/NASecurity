package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

public class NammaApartmentExpectedArrivals {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String dateAndTimeOfArrival;
    private String reference;
    private String inviterUID;
    private String validFor;
    private String status;
    private String expectedArrivalUid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentExpectedArrivals() {
    }

    public NammaApartmentExpectedArrivals(String dateAndTimeOfArrival, String reference, String status, String inviterUID, String validFor) {
        this.dateAndTimeOfArrival = dateAndTimeOfArrival;
        this.reference = reference;
        this.status = status;
        this.inviterUID = inviterUID;
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

    public String getInviterUID() {
        return inviterUID;
    }

    public String getValidFor() {
        return validFor;
    }

    public String getStatus() {
        return status;
    }

    public String getExpectedArrivalUid() {
        return expectedArrivalUid;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setExpectedArrivalUid(String expectedArrivalUid) {
        this.expectedArrivalUid = expectedArrivalUid;
    }
}
