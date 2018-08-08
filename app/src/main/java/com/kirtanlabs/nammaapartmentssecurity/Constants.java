package com.kirtanlabs.nammaapartmentssecurity;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;
import java.util.Map;

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
     * Dialog Types
     * ------------------------------------------------------------- */

    public static final String FAILED = "failed";
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";

    /* ------------------------------------------------------------- *
     * Firebase objects
     * ------------------------------------------------------------- */

    static final int PHONE_NUMBER_MAX_LENGTH = 10;
    public static final String FIREBASE_CHILD_APARTMENTS = "apartments";
    public static final String FIREBASE_CHILD_BANGALURU = "Bengaluru";
    public static final String FIREBASE_CHILD_BRIGADEGATEWAY = "Brigade Gateway";
    public static final String FIREBASE_CHILD_SALARPURIA_CAMBRIDGE = "Salarpuria Cambridge";
    private static final String FIREBASE_CHILD_ALL = "all";
    private static final String FIREBASE_CHILD_CABS = "cabs";
    public static final String FIREBASE_CHILD_DAILYSERVICE_UID = "dailyServiceUID";
    public static final String FIREBASE_CHILD_DATE_AND_TIME_OF_ARRIVAL = "dateAndTimeOfArrival";
    public static final String FIREBASE_CHILD_DELIVERIES = "deliveries";
    private static final String FIREBASE_CHILD_DAILYSERVICES = "dailyServices";
    public static final String FIREBASE_CHILD_FLATS = "flats";
    public static final String FIREBASE_CHILD_FLAT_NUMBER = "flatNumber";
    public static final String FIREBASE_CHILD_FLAT_MEMBERS = "flatMembers";
    public static final String FIREBASE_CHILD_GATE_NOTIFICATIONS = "gateNotifications";
    public static final String FIREBASE_CHILD_HANDED_THINGS = "handedThings";
    private static final String FIREBASE_CHILD_EMERGENCIES = "emergencies";
    private static final String FIREBASE_CHILD_PREAPPROVEDVISITORS = "preApprovedVisitors";
    public static final String FIREBASE_CHILD_POSTAPPROVEDVISITORS = "postApprovedVisitors";
    public static final String FIREBASE_CHILD_PRIVATE = "private";
    public static final String FIREBASE_CHILD_PROFILE_PHOTO = "profilePhoto";
    private static final String FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER = "preApprovedVisitorsMobileNumber";
    public static final String FIREBASE_CHILD_DAILYSERVICETYPE = "dailyServiceType";
    private static final String FIREBASE_CHILD_PUBLIC = "public";
    public static final String FIREBASE_CHILD_SOCIETIES = "societies";
    public static final String FIREBASE_CHILD_STATUS = "status";
    public static final String FIREBASE_CHILD_TOKEN_ID = "tokenId";
    private static final String FIREBASE_CHILD_SECURITY_GUARD = "securityGuard";
    public static final String FIREBASE_CHILD_USERDATA = "userData";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_VISITOR_UID = "visitorUID";
    private static final String FIREBASE_CHILD_USERS = "users";
    public static final int EDIT_TEXT_MIN_LENGTH = 0;

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */
    public static final int CAB_NUMBER_FIELD_LENGTH = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;
    public static final String HYPHEN = "-";
    public static final int RECENT_EMERGENCY_REQUEST_CODE = 0;
    public static final int SEARCH_FLAT_NUMBER_REQUEST_CODE = 1;
    public static final String EMERGENCY_TYPE_MEDICAL = "Medical";
    public static final String EMERGENCY_TYPE_FIRE = "Fire";
    public static final String EMERGENCY_TYPE_THEFT = "Theft";
    private static final String FIREBASE_CHILD_CITIES = "cities";
    private static final String FIREBASE_CHILD_CLIENTS = "clients";
    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final DatabaseReference SECURITY_GUARD_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SECURITY_GUARD);
    private static final DatabaseReference USER_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERS);
    public static final DatabaseReference PRIVATE_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_USERS_REFERENCE = USER_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference VISITORS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VISITORS);
    public static final DatabaseReference PREAPPROVED_VISITORS_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVEDVISITORS);
    public static final DatabaseReference PREAPPROVED_VISITORS_MOBILE_REFERENCE = VISITORS_REFERENCE.child(FIREBASE_CHILD_PREAPPROVEDVISITORSMOBILENUMBER);
    private static final DatabaseReference DAILYSERVICES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DAILYSERVICES);
    private static final DatabaseReference CABS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CABS);
    public static final DatabaseReference PUBLIC_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    private static final DatabaseReference DELIVERIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_DELIVERIES);
    public static final DatabaseReference PUBLIC_DELIVERIES_REFERENCE = DELIVERIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    private static final DatabaseReference EMERGENCIES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_EMERGENCIES);
    public static final DatabaseReference PUBLIC_EMERGENCIES_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);
    private static final DatabaseReference PRIVATE_CLIENTS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_CLIENTS).child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference CITIES_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_CITIES);
    public static final DatabaseReference FLATS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_FLATS);

    /* ------------------------------------------------------------- *
     * Validation Keys
     * ------------------------------------------------------------- */
    public static final DatabaseReference APARTMENTS_REFERENCE = PRIVATE_CLIENTS_REFERENCE.child(FIREBASE_CHILD_APARTMENTS);
    private static final DatabaseReference PRIVATE_CABS_REFERENCE = CABS_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_CABS_REFERENCE = PRIVATE_CABS_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference ALL_DAILYSERVICES_REFERENCE = DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PUBLIC_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PUBLIC);

    /* ------------------------------------------------------------- *
     * Request Code
     * ------------------------------------------------------------- */
    public static final DatabaseReference PRIVATE_DAILYSERVICES_REFERENCE = ALL_DAILYSERVICES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference USER_DATA_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_USERDATA);

    /* ------------------------------------------------------------- *
     * Emergency Type
     * ------------------------------------------------------------- */
    public static final DatabaseReference PRIVATE_USER_DATA_REFERENCE = USER_DATA_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    private static final DatabaseReference PRIVATE_EMERGENCIES_REFERENCE = EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference ALL_EMERGENCIES_REFERENCE = PRIVATE_EMERGENCIES_REFERENCE.child(FIREBASE_CHILD_ALL);

    public static final String GUEST = "guest";
    public static final String DAILY_SERVICE = "dailyService";
    public static final String CAB = "cab";
    public static final String PACKAGE = "package";
    public static final String FAMILY_MEMBER = "familyMember";

    public static final Map<String, String> EINTERCOM_TYPE_MAP = new LinkedHashMap<>();
    static {
        EINTERCOM_TYPE_MAP.put(GUEST, "guests");
        EINTERCOM_TYPE_MAP.put(DAILY_SERVICE, "dailyServices");
        EINTERCOM_TYPE_MAP.put(CAB, "cabs");
        EINTERCOM_TYPE_MAP.put(PACKAGE, "packages");
        EINTERCOM_TYPE_MAP.put(FAMILY_MEMBER, "familyMembers");
    }

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
