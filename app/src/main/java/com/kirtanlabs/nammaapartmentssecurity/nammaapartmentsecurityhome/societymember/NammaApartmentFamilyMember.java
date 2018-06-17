package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

public class NammaApartmentFamilyMember {

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

    public NammaApartmentFamilyMember() {
    }

    public NammaApartmentFamilyMember(String email, String fullName, String phoneNumber, String profilePhoto) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }
}
