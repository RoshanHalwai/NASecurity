package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo;


public class UserPersonalDetails {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String email;
    private String fullName;
    private String phoneNumber;
    private String profilePhoto;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public UserPersonalDetails() {}

    public UserPersonalDetails(String email, String fullName, String phoneNumber, String profilePhoto) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
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

    public String getEmail() {
        return email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

}
