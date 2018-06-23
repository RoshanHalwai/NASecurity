package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

public class NammaApartmentDailyService {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String fullName;
    private String phoneNumber;
    private String profilePhoto;
    private boolean providedThings;
    private int rating;
    private String status;
    private String timeOfVisit;
    private String uid;
    private String dailyServiceType;
    private String ownerUid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDailyService() {
    }

    public NammaApartmentDailyService(String fullName, String phoneNumber, String profilePhoto, boolean providedThings, int rating, String status, String timeOfVisit, String uid) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.providedThings = providedThings;
        this.rating = rating;
        this.status = status;
        this.timeOfVisit = timeOfVisit;
        this.uid = uid;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public boolean isProvidedThings() {
        return providedThings;
    }

    public int getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
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

    /* ------------------------------------------------------------- *
     * Setters
     * ------------------------------------------------------------- */

    public void setDailyServiceType(String dailyServiceType) {
        this.dailyServiceType = dailyServiceType;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }
}
