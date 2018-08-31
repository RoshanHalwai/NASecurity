package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo;


public class UserPrivileges {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private boolean admin;
    private boolean grantedAccess;
    private int verified;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public UserPrivileges() {
    }

    public UserPrivileges(boolean admin, boolean grantedAccess, int verified) {
        this.admin = admin;
        this.grantedAccess = grantedAccess;
        this.verified = verified;
    }

    /* ------------------------------------------------------------- *
     * Getters
     * ------------------------------------------------------------- */

    public boolean isAdmin() {
        return admin;
    }

    public boolean isGrantedAccess() {
        return grantedAccess;
    }

    public int getVerified() {
        return verified;
    }

}
