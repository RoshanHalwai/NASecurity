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
    private String timeOfVisit;
    private String uid;
    private String dailyServiceType;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentDailyService() {
    }

    public NammaApartmentDailyService(String fullName, String phoneNumber, String profilePhoto, boolean providedThings, int rating, String timeOfVisit, String uid) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.providedThings = providedThings;
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

    public String getTimeOfVisit() {
        return timeOfVisit;
    }

    public String getUid() {
        return uid;
    }

    public String getDailyServiceType() {
        return dailyServiceType;
    }

    public void setDailyServiceType(String dailyServiceType) {
        this.dailyServiceType = dailyServiceType;
    }
}
