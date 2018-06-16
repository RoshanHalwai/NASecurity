package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;

public class NammaApartmentFamilyMember {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String apartmentName;
    private String emailId;
    private String flatNumber;
    private String fullName;
    private String phoneNumber;
    private String societyName;
    private String tenantType;
    private String uid;
    private boolean verified;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentFamilyMember() {
    }

    public NammaApartmentFamilyMember(String apartmentName, String emailId, String flatNumber, String fullName, String phoneNumber, String societyName, String tenantType, String uid, boolean verified) {
        this.apartmentName = apartmentName;
        this.emailId = emailId;
        this.flatNumber = flatNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.societyName = societyName;
        this.tenantType = tenantType;
        this.uid = uid;
        this.verified = verified;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getApartmentName() {
        return apartmentName;
    }

    public String getEmailId() {
        return emailId;
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

    public String getSocietyName() {
        return societyName;
    }

    public String getTenantType() {
        return tenantType;
    }

    public String getUid() {
        return uid;
    }

    public boolean getVerified() {
        return verified;
    }
}
