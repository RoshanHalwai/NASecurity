package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.pojo;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 9/9/2018
 */
public class Notification {

    private String message;
    private String mobileNumber;
    private String profilePhoto;
    private String uid;

    public Notification() {
    }

    public Notification(String message, String mobileNumber, String profilePhoto, String uid) {
        this.message = message;
        this.mobileNumber = mobileNumber;
        this.profilePhoto = profilePhoto;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getUid() {
        return uid;
    }
}
