package com.kirtanlabs.nammaapartmentssecurity;

import android.content.Context;
import android.graphics.Typeface;

public class Constants {

    /* ------------------------------------------------------------- *
     * Intent Keys
     * ------------------------------------------------------------- */

    public static final String ARRIVAL_TYPE = "arrival_type";
    public static final String GIVEN_THINGS_TO = "given_things_to";
    public static final String SCREEN_TITLE = "screen_title";
    public static final String VALIDATION_STATUS = "validation_status";

    /* ------------------------------------------------------------- *
     * Dialog Types
     * ------------------------------------------------------------- */

    public static final String FAILED = "failed";
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";

    /* ------------------------------------------------------------- *
     * Firebase objects
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_DAILYSERVICES = "dailyServices";
    public static final String FIREBASE_CHILD_DAILYSERVICE_UID = "dailyServiceUID";
    public static final String FIREBASE_CHILD_FULL_NAME = "fullName";
    public static final String FIREBASE_CHILD_FLAT_NUMBER = "flatNumber";
    public static final String FIREBASE_CHILD_OWNERS_UID = "ownersUID";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORS = "preApprovedVisitors";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER = "preApprovedVisitorsMobileNumber";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_PUBLIC = "public";
    public static final String FIREBASE_CHILD_SERVICETYPE = "serviceType";
    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_VISITOR_UID = "visitorUID";

    /* ------------------------------------------------------------- *
     * Font Types
     * ------------------------------------------------------------- */

    public static Typeface setLatoBlackFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Black.ttf");
    }

    public static Typeface setLatoBlackItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-BlackItalic.ttf");
    }

    public static Typeface setLatoBoldFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Bold.ttf");
    }

    public static Typeface setLatoBoldItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-BoldItalic.ttf");
    }

    public static Typeface setLatoHairlineFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Hairline.ttf");
    }

    public static Typeface setLatoHairlineItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-HairlineItalic.ttf");
    }

    public static Typeface setLatoItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Italic.ttf");
    }

    public static Typeface setLatoLightFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");
    }

    public static Typeface setLatoLightItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-LightItalic.ttf");
    }

    public static Typeface setLatoRegularFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Regular.ttf");
    }

}
