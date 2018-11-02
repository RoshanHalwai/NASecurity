package com.kirtanlabs.nammaapartmentssecurity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.kirtanlabs.nammaapartmentssecurity.NammaApartmentsSecurity.BUILD_VARIANT;

/**
 * DO NOT AUTO-FORMAT THIS FILE
 */
public class Constants {

    /* ------------------------------------------------------------- *
     * Environment
     * ------------------------------------------------------------- */

    static final String BETA_ENV = "beta_env";
    static final String DEV_ENV = "dev_env";

    /* ------------------------------------------------------------- *
     * Application Specific
     * ------------------------------------------------------------- */

    //TODO:Change the Guard's City and Society name when we deploy to new society.
    public static final String GUARD_CITY_NAME = "Chennai";
    public static final String GUARD_SOCIETY_NAME = "Air Force Colony";

    static final String PACKAGE_NAME = "com.kirtanlabs.nammaapartmentssecurity";
    public static final String HYPHEN = "-";
    public static final String EMERGENCY_TYPE_MEDICAL = "Medical";
    public static final String EMERGENCY_TYPE_FIRE = "Fire";
    public static final String EMERGENCY_TYPE_THEFT = "Theft";
    public static final String EMERGENCY_TYPE_WATER = "Water";
    public static final String GUEST = "guest";
    private static final String DAILY_SERVICE = "dailyService";
    public static final String CAB = "cab";
    public static final String PACKAGE = "package";
    private static final String FAMILY_MEMBER = "familyMember";
    public static final String ACCEPTED = "Accepted";
    public static final String REJECTED = "Rejected";
    public static final String IGNORED = "Ignored";
    public static final String NOT_ENTERED = "Not Entered";
    public static final String ENTERED = "Entered";
    public static final String LEFT = "Left";

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
    public static final String VEHICLE_UID = "vehicleUID";
    public static final String NOTIFICATION_UID = "notificationUID";
    public static final String REFERENCE = "reference";
    public static final String VISITOR_IMAGE_FILE_PATH = "visitorImageFilePath";
    public static final String SENT_USER_UID = "sentUserUID";
    public static final String USER_MOBILE_NUMBER = "userMobileNumber";
    public static final String VISITOR_MOBILE_NUMBER = "visitorMobileNumber";
    public static final String VISITOR_IMAGE_URL = "visitorImageURL";

    /* ------------------------------------------------------------- *
     * Login/OTP Constants
     * ------------------------------------------------------------- */

    public static final int OTP_TIMER = 60;

    /* ------------------------------------------------------------- *
     * Dialog Types
     * ------------------------------------------------------------- */

    public static final String FAILED = "failed";
    public static final String SUCCESS = "success";

    /* ------------------------------------------------------------- *
     * Firebase Keys
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_APARTMENTS = "apartments";
    public static final String FIREBASE_CHILD_SALARPURIA_CAMBRIDGE = "Salarpuria Cambridge";
    public static final String FIREBASE_CHILD_DAILYSERVICE_UID = "dailyServiceUID";
    public static final String FIREBASE_CHILD_DATE_AND_TIME_OF_ARRIVAL = "dateAndTimeOfArrival";
    public static final String FIREBASE_CHILD_DELIVERIES = "deliveries";
    private static final String FIREBASE_CHILD_DATA = "data";
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
    private static final String FIREBASE_CHILD_USERDATA = "userData";
    public static final String FIREBASE_CHILD_VISITORS = "visitors";
    public static final String FIREBASE_CHILD_VISITOR_UID = "visitorUID";
    private static final String FIREBASE_CHILD_ALL = "all";
    public static final String FIREBASE_CHILD_CABS = "cabs";
    private static final String FIREBASE_CHILD_DAILYSERVICES = "dailyServices";
    private static final String FIREBASE_CHILD_EMERGENCIES = "emergencies";
    private static final String FIREBASE_CHILD_PUBLIC = "public";
    private static final String FIREBASE_CHILD_SECURITY_GUARDS = "guards";
    private static final String FIREBASE_CHILD_USERS = "users";
    private static final String FIREBASE_CHILD_CITIES = "cities";
    private static final String FIREBASE_CHILD_CLIENTS = "clients";
    public static final String FIREBASE_CHILD_MESSAGE = "message";
    public static final String FIREBASE_CHILD_UID = "uid";
    private static final String FIREBASE_CHILD_VEHICLES = "vehicles";
    public static final String FIREBASE_CHILD_VEHICLE_NUMBER = "vehicleNumber";
    public static final String FIREBASE_CHILD_OWNER_NAME = "ownerName";
    public static final String FIREBASE_CHILD_VEHICLE_TYPE = "vehicleType";
    public static final String FIREBASE_CHILD_DATE_AND_TIME_OF_VISIT = "dateAndTimeOfVisit";
    private static final String FIREBASE_CHILD_SOCIETY_DETAILS = "societyDetails";

    /* ------------------------------------------------------------- *
     * Firebase Database References
     * ------------------------------------------------------------- */

    @SuppressLint("StaticFieldLeak")
    private static final FirebaseApp FIREBASE_APP = FirebaseApp.getInstance(BUILD_VARIANT);
    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance(FIREBASE_APP);
    public static final FirebaseStorage FIREBASE_STORAGE = FirebaseStorage.getInstance(FIREBASE_APP);
    public static final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance(FIREBASE_APP);
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
    private static final DatabaseReference SECURITY_GUARDS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SECURITY_GUARDS);
    public static final DatabaseReference SECURITY_GUARDS_PRIVATE_DATA_REFERENCE = SECURITY_GUARDS_REFERENCE.child(FIREBASE_CHILD_PRIVATE).child(FIREBASE_CHILD_DATA);
    public static final DatabaseReference ALL_SECURITY_GUARDS_REFERENCE = SECURITY_GUARDS_REFERENCE.child(FIREBASE_CHILD_ALL);
    private static final DatabaseReference VEHICLES_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_VEHICLES);
    public static final DatabaseReference ALL_VEHICLES_REFERENCE = VEHICLES_REFERENCE.child(FIREBASE_CHILD_ALL);
    public static final DatabaseReference PRIVATE_VEHICLES_REFERENCE = VEHICLES_REFERENCE.child(FIREBASE_CHILD_PRIVATE);
    public static final DatabaseReference PRIVATE_SOCIETY_DETAILS_REFERENCE = FIREBASE_DATABASE.getReference(FIREBASE_CHILD_SOCIETY_DETAILS).child(FIREBASE_CHILD_PRIVATE);

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
    public static final int PLACE_CALL_PERMISSION_REQUEST_CODE = 2;

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
    public static final String GUEST_APPROVAL_NOTIFICATION_UID = "guestApprovalNotificationUid";
    public static final String CAB_APPROVAL_NOTIFICATION_UID = "cabApprovalNotificationUid";
    public static final String PACKAGE_APPROVAL_NOTIFICATION_UID = "packageApprovalNotificationUid";
    public static final String GUEST_APPROVAL_USER_UID = "guestApprovalUserUid";
    public static final String CAB_APPROVAL_USER_UID = "cabApprovalUserUid";
    public static final String PACKAGE_APPROVAL_USER_UID = "packageApprovalUserUid";
    public static final String GUEST_APPROVAL_REFERENCE = "guestApprovalReference";
    public static final String CAB_APPROVAL_REFERENCE = "cabApprovalReference";
    public static final String PACKAGE_APPROVAL_REFERENCE = "packageApprovalReference";
    public static final String GUEST_APPROVAL_USER_MOBILE_NUMBER = "guestApprovalUserMobileNumber";
    public static final String CAB_APPROVAL_USER_MOBILE_NUMBER = "cabApprovalUserMobileNumber";
    public static final String PACKAGE_APPROVAL_USER_MOBILE_NUMBER = "packageApprovalUserMobileNumber";
    public static final String GUEST_APPROVAL_USER_APARTMENT_NAME = "guestApprovalUserApartmentName";
    public static final String CAB_APPROVAL_USER_APARTMENT_NAME = "cabApprovalUserApartmentName";
    public static final String PACKAGE_APPROVAL_USER_APARTMENT_NAME = "packageApprovalUserApartmentName";
    public static final String GUEST_APPROVAL_USER_FLAT_NUMBER = "guestApprovalUserFlatNumber";
    public static final String CAB_APPROVAL_USER_FLAT_NUMBER = "cabApprovalUserFlatNumber";
    public static final String PACKAGE_APPROVAL_USER_FLAT_NUMBER = "packageApprovalUserFlatNumber";
    public static final String GUEST_APPROVAL_VISITOR_IMAGE_PATH = "guestApprovalVisitorImagePath";

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

    public static String getCabMessage(String cabNumber) {
        return "Your Cab Numbered " + cabNumber + " wants to enter your society. Please confirm.";
    }

    public static String getPackageMessage(String packageVendor) {
        return "Your Package vendor " + packageVendor + " wants to enter your society. Please confirm.";
    }

}
