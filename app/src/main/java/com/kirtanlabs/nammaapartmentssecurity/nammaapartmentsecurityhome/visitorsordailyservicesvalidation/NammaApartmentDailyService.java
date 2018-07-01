package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

public class NammaApartmentDailyService {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private int numberOfFlats;
    private String phoneNumber;
    private String profilePhoto;
    private int rating;
    private String timeOfVisit;
    private String uid;
    private String dailyServiceType;
    private String ownerUid;
    private String status;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDailyService() {
    }

    public NammaApartmentDailyService(String fullName, int numberOfFlats, String phoneNumber, String profilePhoto, int rating, String timeOfVisit, String uid) {
        this.fullName = fullName;
        this.numberOfFlats = numberOfFlats;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.rating = rating;
        this.timeOfVisit = timeOfVisit;
        this.uid = uid;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFullName() {
        return fullName;
    }

    public int getNumberOfFlats() {
        return numberOfFlats;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public int getRating() {
        return rating;
    }

    public String getTimeOfVisit() {
        return timeOfVisit;
    }

    public String getUid() {
        return uid;
    }

    public String getDailyServiceType() {
        return dailyServiceType;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public String getStatus() {
        return status;
    }

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDailyServiceType(String dailyServiceType) {
        this.dailyServiceType = dailyServiceType;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }
}
