package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo;


public class UserFlatDetails {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String apartmentName;
    private String city;
    private String flatNumber;
    private String societyName;
    private String tenantType;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public UserFlatDetails() {}

    public UserFlatDetails(String apartmentName, String city, String flatNumber, String societyName, String tenantType) {
        this.apartmentName = apartmentName;
        this.city = city;
        this.flatNumber = flatNumber;
        this.societyName = societyName;
        this.tenantType = tenantType;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public String getCity() {
        return city;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public String getSocietyName() {
        return societyName;
    }

    public String getTenantType() {
        return tenantType;
    }

}