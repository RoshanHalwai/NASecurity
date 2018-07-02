package com.kirtanlabs.nammaapartmentssecurity;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {

    /* ------------------------------------------------------------- *
     * Intent Keys
     * ------------------------------------------------------------- */

    public static final String ARRIVAL_TYPE = "arrival_type";
    public static final String GIVEN_THINGS_TO = "given_things_to";
    public static final String SCREEN_TITLE = "screen_title";
    public static final String VALIDATION_STATUS = "validation_status";
    public static final String EXPECTED_ARRIVAL_UID = "expected_arrival_uid";
    public static final String SERVICE_TYPE = "service_type";

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
    public static final String FIREBASE_CHILD_APARTMENTS = "apartments";
    public static final String FIREBASE_CHILD_BANGALORE = "Bangalore";
    public static final String FIREBASE_CHILD_BRIGADEGATEWAY = "Brigade Gateway";
    public static final String FIREBASE_CHILD_SALARPURIA_CAMBRIDGE = "Salarpuria Cambridge";
    public static final String FIREBASE_CHILD_CABS = "cabs";
    private static final String FIREBASE_CHILD_CITIES = "cities";
    private static final String FIREBASE_CHILD_CLIENTS = "clients";
    public static final String FIREBASE_CHILD_DAILYSERVICES = "dailyServices";
    public static final String FIREBASE_CHILD_DAILYSERVICE_UID = "dailyServiceUID";
    public static final String FIREBASE_CHILD_DATE_AND_TIME_OF_ARRIVAL = "dateAndTimeOfArrival";
    public static final String FIREBASE_CHILD_DELIVERIES = "deliveries";
    public static final String FIREBASE_CHILD_FULL_NAME = "fullName";
    public static final String FIREBASE_CHILD_FLATS = "flats";
    public static final String FIREBASE_CHILD_FLAT_NUMBER = "flatNumber";
    public static final String FIREBASE_CHILD_HANDED_THINGS = "handedThings";
    public static final String FIREBASE_CHILD_OWNERS_UID = "ownersUID";
    public static final String FIREBASE_CHILD_FLATMEMBERS = "flatMembers";
    public static final String FIREBASE_CHILD_PERSONALDETAILS = "personalDetails";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORS = "preApprovedVisitors";
    public static final String FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER = "preApprovedVisitorsMobileNumber";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_PUBLIC = "public";
    public static final String FIREBASE_CHILD_DAILYSERVICETYPE = "dailyServiceType";
    public static final String FIREBASE_CHILD_SOCIETIES = "societies";
    public static final String FIREBASE_CHILD_STATUS = "status";
    public static final String FIREBASE_CHILD_TIMEOFVISIT = "timeOfVisit";
    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_USERDATA = "userData";
    public static final String FIREBASE_CHILD_VALIDFOR = "validFor";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_VISITOR_UID = "visitorUID";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    private static final DatabaseReference USER_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    private static final DatabaseReference VISITORS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VISITORS);
    private static final DatabaseReference DAILYSERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DAILYSERVICES);
    private static final DatabaseReference CABS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CABS);
    private static final DatabaseReference DELIVERIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DELIVERIES);
    private static final DatabaseReference FLATS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_FLATS);
    public static final DatabaseReference PRIVATE_FLATS_REFERENCE = FLATS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference PRIVATE_CLIENTS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CLIENTS).child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference CITIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_CITIES);
    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference PRIVATE_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_CABS_REFERENCE = PRIVATE_CABS_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PRIVATE_DELIVERY_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference ALL_DAILYSERVICES_REFERENCE = DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PUBLIC_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PUBLIC_DELIVERIES_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PRIVATE_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PUBLIC_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PREAPPROVED_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVEDVISITORS);
    public static final DatabaseReference PREAPPROVED_VISITORS_MOBILE_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER);
    private static final DatabaseReference USER_DATA_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERDATA);
    public static final DatabaseReference PRIVATE_USER_DATA_REFERENCE = USER_DATA_REFERENCE.child(FIREBASE_CHILD_PRIVATE);

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */

    public static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final int EDIT_TEXT_MIN_LENGTH = 0;
    public static final int CAB_NUMBER_FIELD_LENGTH = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;

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
