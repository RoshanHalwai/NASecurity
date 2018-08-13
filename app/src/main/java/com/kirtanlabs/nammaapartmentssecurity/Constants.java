package com.kirtanlabs.nammaapartmentssecurity;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DO NOT AUTO-FORMAT THIS FILE
 */
public class Constants {

    /* ------------------------------------------------------------- *
     * Intent Keys
     * ------------------------------------------------------------- */

    public static final String GIVEN_THINGS_TO = "given_things_to";
    public static final String SCREEN_TITLE = "screen_title";
    public static final String EXPECTED_ARRIVAL_UID = "expected_arrival_uid";
    public static final String SERVICE_TYPE = "service_type";
    public static final String FLAT_NUMBER = "flatNumber";
    public static final String EMERGENCY_TYPE = "emergencyType";
    public static final String OWNER_NAME = "ownerName";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String APARTMENT_NAME = "apartmentName";
    public static final String MESSAGE = "message";
    public static final String EINTERCOM_TYPE = "eIntercomType";

    /* ------------------------------------------------------------- *
     * Login/OTP Constants
     * ------------------------------------------------------------- */

    public static final int OTP_TIMER = 60;

    /* ------------------------------------------------------------- *
     * Dialog Types
     * ------------------------------------------------------------- */

    public static final String FAILED = "failed";
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";

    /* ------------------------------------------------------------- *
     * Firebase Keys
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_APARTMENTS = "apartments";
    public static final String FIREBASE_CHILD_BANGALURU = "Bengaluru";
    public static final String FIREBASE_CHILD_BRIGADEGATEWAY = "Brigade Gateway";
    public static final String FIREBASE_CHILD_SALARPURIA_CAMBRIDGE = "Salarpuria Cambridge";
    public static final String FIREBASE_CHILD_DAILYSERVICE_UID = "dailyServiceUID";
    public static final String FIREBASE_CHILD_DATE_AND_TIME_OF_ARRIVAL = "dateAndTimeOfArrival";
    public static final String FIREBASE_CHILD_DELIVERIES = "deliveries";
    public static final String FIREBASE_CHILD_DATA = "data";
    public static final String FIREBASE_CHILD_FLATS = "flats";
    public static final String FIREBASE_CHILD_FLAT_NUMBER = "flatNumber";
    public static final String FIREBASE_CHILD_FLAT_MEMBERS = "flatMembers";
    public static final String FIREBASE_CHILD_GATE_NOTIFICATIONS = "gateNotifications";
    public static final String FIREBASE_CHILD_HANDED_THINGS = "handedThings";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_PROFILE_PHOTO = "profilePhoto";
    public static final String FIREBASE_CHILD_DAILYSERVICE_TYPE = "dailyServiceType";
    public static final String FIREBASE_CHILD_SOCIETIES = "societies";
    public static final String FIREBASE_CHILD_STATUS = "status";
    public static final String FIREBASE_CHILD_TOKEN_ID = "tokenId";
    public static final String FIREBASE_CHILD_USERDATA = "userData";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_VISITOR_UID = "visitorUID";
    private static final String FIREBASE_CHILD_ALL = "all";
    private static final String FIREBASE_CHILD_CABS = "cabs";
    private static final String FIREBASE_CHILD_DAILYSERVICES = "dailyServices";
    private static final String FIREBASE_CHILD_EMERGENCIES = "emergencies";
    private static final String FIREBASE_CHILD_PUBLIC = "public";
    private static final String FIREBASE_CHILD_SECURITY_GUARD = "guard";
    private static final String FIREBASE_CHILD_SOCIETY_SERVICE = "societyServices";
    private static final String FIREBASE_CHILD_USERS = "users";
    private static final String FIREBASE_CHILD_CITIES = "cities";
    private static final String FIREBASE_CHILD_CLIENTS = "clients";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    private static final DatabaseReference USER_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference VISITORS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VISITORS);
    public static final DatabaseReference ALL_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PRIVATE_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference DAILYSERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DAILYSERVICES);
    private static final DatabaseReference CABS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CABS);
    public static final DatabaseReference PRIVATE_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference DELIVERIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DELIVERIES);
    public static final DatabaseReference PRIVATE_DELIVERIES_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference EMERGENCIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_EMERGENCIES);
    public static final DatabaseReference PUBLIC_EMERGENCIES_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    private static final DatabaseReference PRIVATE_CLIENTS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CLIENTS).child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference CITIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_CITIES);
    public static final DatabaseReference FLATS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_FLATS);
    public static final DatabaseReference APARTMENTS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_APARTMENTS);
    private static final DatabaseReference ALL_DAILYSERVICES_REFERENCE = DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PUBLIC_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    public static final DatabaseReference PRIVATE_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference USER_DATA_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERDATA);
    public static final DatabaseReference PRIVATE_USER_DATA_REFERENCE = USER_DATA_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference PRIVATE_EMERGENCIES_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_EMERGENCIES_REFERENCE = PRIVATE_EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference SOCIETY_SERVICE_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETY_SERVICE);
    public static final DatabaseReference ALL_SOCIETY_SERVICE_REFERENCE = SOCIETY_SERVICE_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference SECURITY_GUARD_REFERENCE = SOCIETY_SERVICE_REFERENCE.child(FIREBASE_CHILD_SECURITY_GUARD);

    /* ------------------------------------------------------------- *
     * Application Specific
     * ------------------------------------------------------------- */

    public static final String HYPHEN = "-";
    public static final String EMERGENCY_TYPE_MEDICAL = "Medical";
    public static final String EMERGENCY_TYPE_FIRE = "Fire";
    public static final String EMERGENCY_TYPE_THEFT = "Theft";
    public static final String GUEST = "guest";
    public static final String DAILY_SERVICE = "dailyService";
    public static final String CAB = "cab";
    public static final String PACKAGE = "package";
    public static final String FAMILY_MEMBER = "familyMember";

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */

    public static final int CAB_NUMBER_FIELD_LENGTH = 2;
    static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final int EDIT_TEXT_MIN_LENGTH = 0;
    public static final String COUNTRY_CODE_IN = "+91";

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */

    public static final int RECENT_EMERGENCY_REQUEST_CODE = 0;
    public static final int SEARCH_FLAT_NUMBER_REQUEST_CODE = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;

    /* ------------------------------------------------------------- *
     * E-Intercom Type Map
     * ------------------------------------------------------------- */

    public static final Map<String, String> EINTERCOM_TYPE_MAP = new LinkedHashMap<>();

    static {
        EINTERCOM_TYPE_MAP.put(GUEST, "guests");
        EINTERCOM_TYPE_MAP.put(DAILY_SERVICE, "dailyServices");
        EINTERCOM_TYPE_MAP.put(CAB, "cabs");
        EINTERCOM_TYPE_MAP.put(PACKAGE, "packages");
        EINTERCOM_TYPE_MAP.put(FAMILY_MEMBER, "familyMembers");
    }

    /* ------------------------------------------------------------- *
     * Shared Preference Keys
     * ------------------------------------------------------------- */

    public static final String NAMMA_APARTMENTS_SECURITY_PREFERENCE = "nammaApartmentsSecurityPreference";
    public static final String LOGGED_IN = "loggedIn";
    public static final String SECURITY_GUARD_UID = "securityGuardUid";

    /* ------------------------------------------------------------- *
     * Font Types
     * ------------------------------------------------------------- */

    public static Typeface setLatoBoldFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Bold.ttf");
    }

    public static Typeface setLatoItalicFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Italic.ttf");
    }

    public static Typeface setLatoLightFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");
    }

    public static Typeface setLatoRegularFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Regular.ttf");
    }

    public static String getGuestMessage(String guestName) {
        return "Your Guest " + guestName + " wants to enter your society. Please confirm.";
    }

    public static String getDailyServiceMessage(String dailyServiceName) {
        return "Your Daily Service " + dailyServiceName + " wants to enter your society. Please confirm.";
    }

    public static String getCabMessage(String cabNumber) {
        return "Your Cab Numbered " + cabNumber + " wants to enter your society. Please confirm.";
    }

    public static String getPackageMessage(String packageVendor) {
        return "Your Package vendor " + packageVendor + " wants to enter your society. Please confirm.";
    }

    public static String getFamilyMemberMessage(String familyMemberName) {
        return "Your Family Member " + familyMemberName + " wants to enter your society. Please confirm.";
    }

}
