package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/5/2018
 */
public class Type {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int intercomTypeImage;
    private final String intercomType;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    Type(int intercomTypeImage, String intercomType) {
        this.intercomTypeImage = intercomTypeImage;
        this.intercomType = intercomType;
    }

    /* ------------------------------------------------------------- *
     * Public API (Getter & Setter)
     * ------------------------------------------------------------- */

    public int getIntercomTypeImage() {
        return intercomTypeImage;
    }

    public String getIntercomType() {
        return intercomType;
    }

}
