package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo;

import java.io.Serializable;

public class NammaApartmentUser implements Serializable {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private UserFlatDetails flatDetails;
    private UserPersonalDetails personalDetails;
    private UserPrivileges privileges;
    private String uid;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public NammaApartmentUser() {
    }

    public NammaApartmentUser(String uid, UserPersonalDetails personalDetails, UserFlatDetails flatDetails, UserPrivileges privileges) {
        this.uid = uid;
        this.personalDetails = personalDetails;
        this.flatDetails = flatDetails;
        this.privileges = privileges;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public UserFlatDetails getFlatDetails() {
        return flatDetails;
    }

    public UserPersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public UserPrivileges getPrivileges() {
        return privileges;
    }

    public String getUID() {
        return uid;
    }
}
