package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

public class NammaApartmentVisitor {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String dateAndTimeOfVisit;
    private String fullName;
    private String inviterUID;
    private String mobileNumber;
    private String profilePhoto;
    private String status;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentVisitor() {
    }

    public NammaApartmentVisitor(String dateAndTimeOfVisit, String fullName, String inviterUID, String mobileNumber, String profilePhoto, String status, String uid) {
        this.dateAndTimeOfVisit = dateAndTimeOfVisit;
        this.fullName = fullName;
        this.inviterUID = inviterUID;
        this.mobileNumber = mobileNumber;
        this.profilePhoto = profilePhoto;
        this.status = status;
        this.uid = uid;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getDateAndTimeOfVisit() {
        return dateAndTimeOfVisit;
    }

    public String getFullName() {
        return fullName;
    }

    public String getInviterUID() {
        return inviterUID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }
}
