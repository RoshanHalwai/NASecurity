package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;

import java.util.Calendar;
import java.util.List;

public class ExpectedArrivalsListAdapter extends RecyclerView.Adapter<ExpectedArrivalsListAdapter.ExpectedArrivalsHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals;
    private List<NammaApartmentExpectedArrivals> nammaApartmentExpectedArrivalsList;
    private DatabaseReference expectedArrivalsReference;
    private int validationStatusOf;
    private String status;
    private String notificationMessage;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    ExpectedArrivalsListAdapter(Context mCtx, List<NammaApartmentExpectedArrivals> nammaApartmentExpectedArrivalsList, int validationStatusOf) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentExpectedArrivalsList = nammaApartmentExpectedArrivalsList;
        this.validationStatusOf = validationStatusOf;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public ExpectedArrivalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_expected_cab_and_package_arrivals, parent, false);
        return new ExpectedArrivalsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpectedArrivalsHolder holder, int position) {
        String apartmentTitle = mCtx.getString(R.string.apartment) + ":";
        holder.textApartment.setText(apartmentTitle);
        String flatNumberTitle = mCtx.getString(R.string.flat_number) + ":";
        holder.textFlatNumber.setText(flatNumberTitle);

        //Creating an instance of NammaApartmentExpectedArrivals class and retrieving the values from Firebase
        nammaApartmentExpectedArrivals = nammaApartmentExpectedArrivalsList.get(position);

        /*Since we are using same layout for Cab Driver and Package Vendor Validation we need to
         *change some Views Text and make some views Visible and Invisible in Package Vendor Validation*/
        String expectedArrivalLeft;
        if (validationStatusOf == R.string.package_vendor_validation_status) {
            changeViewsText(holder.textBookedBy, holder.buttonAllowExpectedArrivals);
            holder.textPackageVendor.setVisibility(View.VISIBLE);
            holder.textPackageVendorValue.setVisibility(View.VISIBLE);
            holder.textApartment.setVisibility(View.GONE);
            holder.textApartmentValue.setVisibility(View.GONE);
            holder.textFlatNumber.setVisibility(View.GONE);
            holder.textFlatNumberValue.setVisibility(View.GONE);
            holder.textPackageVendorValue.setText(nammaApartmentExpectedArrivals.getReference());
            expectedArrivalLeft = mCtx.getString(R.string.package_vendor_left);
        } else {
            expectedArrivalLeft = mCtx.getString(R.string.cab_driver_left);
        }

        //To retrieve Owners details from firebase
        getOwnerDetailsFromFireBase(holder.textBookedByValue, holder.textFlatNumberValue, holder.textApartmentValue);

        String dateAndTime = nammaApartmentExpectedArrivals.getDateAndTimeOfArrival();
        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
        holder.textDateToVisitValue.setText(separatedDateAndTime[0]);
        holder.textTimeToVisitValue.setText(separatedDateAndTime[1]);

        //If status of Expected Arrival is Entered that we have to change button text.
        status = nammaApartmentExpectedArrivals.getStatus();
        if (status.equals(mCtx.getString(R.string.entered))) {
            holder.buttonAllowExpectedArrivals.setText(expectedArrivalLeft);
            notificationMessage = mCtx.getString(R.string.expected_arrival_left_notification_message);
        } else {
            notificationMessage = mCtx.getString(R.string.expected_arrival_notification_message);
        }
    }

    @Override
    public int getItemCount() {
        return nammaApartmentExpectedArrivalsList.size();
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * We update the TextView textBookedBy and Button buttonAllowCabDriver Text in Package Vendor Validation
     *
     * @param textBookedBy                - to update text in Package Vendor Validation
     * @param buttonAllowExpectedArrivals - to update text in Package Vendor Validation
     */
    private void changeViewsText(final TextView textBookedBy, final Button buttonAllowExpectedArrivals) {
        String orderedByTitle = mCtx.getString(R.string.ordered_by);
        textBookedBy.setText(orderedByTitle);

        String allowTo = mCtx.getString(R.string.allow_package_vendor);
        buttonAllowExpectedArrivals.setText(allowTo);
    }

    private void getOwnerDetailsFromFireBase(final TextView textBookedByValue, final TextView textFlatNumberValue, TextView textApartmentValue) {
        DatabaseReference ownerReference = Constants.PRIVATE_USERS_REFERENCE
                .child(nammaApartmentExpectedArrivals.getInviterUID());
        ownerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                assert nammaApartmentUser != null;
                textBookedByValue.setText(nammaApartmentUser.getPersonalDetails().getFullName());
                textApartmentValue.setText(nammaApartmentUser.getFlatDetails().getApartmentName());
                textFlatNumberValue.setText(nammaApartmentUser.getFlatDetails().getFlatNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Expected Cab Arrivals View Holder class
     * ------------------------------------------------------------- */

    class ExpectedArrivalsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textPackageVendor;
        private TextView textBookedBy;
        private TextView textApartment;
        private TextView textFlatNumber;
        private TextView textDateToVisit;
        private TextView textTimeToVisit;
        private TextView textPackageVendorValue;
        private TextView textBookedByValue;
        private TextView textApartmentValue;
        private TextView textFlatNumberValue;
        private TextView textDateToVisitValue;
        private TextView textTimeToVisitValue;
        private Button buttonAllowExpectedArrivals;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ExpectedArrivalsHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textPackageVendor = itemView.findViewById(R.id.textPackageVendor);
            textBookedBy = itemView.findViewById(R.id.textBookedBy);
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textDateToVisit = itemView.findViewById(R.id.textDateToVisit);
            textTimeToVisit = itemView.findViewById(R.id.textTimeToVisit);
            textPackageVendorValue = itemView.findViewById(R.id.textPackageVendorValue);
            textBookedByValue = itemView.findViewById(R.id.textBookedByValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textDateToVisitValue = itemView.findViewById(R.id.textDateToVisitValue);
            textTimeToVisitValue = itemView.findViewById(R.id.textTimeToVisitValue);
            buttonAllowExpectedArrivals = itemView.findViewById(R.id.buttonAllowArrivals);

            /*Setting fonts to the views*/
            textPackageVendor.setTypeface(Constants.setLatoRegularFont(mCtx));
            textBookedBy.setTypeface(Constants.setLatoRegularFont(mCtx));
            textApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textDateToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textTimeToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textPackageVendorValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textBookedByValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textDateToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textTimeToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonAllowExpectedArrivals.setTypeface(Constants.setLatoLightFont(mCtx));

            /*Setting onClickListener for view*/
            buttonAllowExpectedArrivals.setOnClickListener(this);
        }

        /* ------------------------------------------------------------- *
         * Overriding OnClick Listeners
         * ------------------------------------------------------------- */

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            if (isExpectedArrivalReachedOnTime(position)) {
                changeExpectedArrivalStatusInFirebase(position);
                baseActivity.showNotificationSentDialog(mCtx.getString(R.string.expected_arrival_notification_title), notificationMessage);
            } else {
                baseActivity.openValidationStatusDialog(Constants.FAILED, mCtx.getString(R.string.expected_time_of_arrival_is_finished));
            }
        }

        /* ------------------------------------------------------------- *
         * Private Method
         * ------------------------------------------------------------- */

        /**
         * This method is invoked to change status of Expected Arrivals
         *
         * @param position of card view for which status has been changed
         */
        private void changeExpectedArrivalStatusInFirebase(int position) {
            NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals = nammaApartmentExpectedArrivalsList.get(position);
            status = nammaApartmentExpectedArrivals.getStatus();
            if (validationStatusOf == R.string.cab_driver_validation_status) {
                expectedArrivalsReference = Constants.PUBLIC_CABS_REFERENCE
                        .child(nammaApartmentExpectedArrivals.getExpectedArrivalUid());
            } else {
                expectedArrivalsReference = Constants.PUBLIC_DELIVERIES_REFERENCE
                        .child(nammaApartmentExpectedArrivals.getExpectedArrivalUid());
            }
            baseActivity.changeStatus(status, expectedArrivalsReference.child(Constants.FIREBASE_CHILD_STATUS));

            if (status.equals(mCtx.getString(R.string.not_entered))) {
                changeDateAndTime();
            }
        }

        /**
         * This method is used to change in time of Expected Arrival
         */
        private void changeDateAndTime() {
            String currentTime = baseActivity.getCurrentTime();
            String currentDate = baseActivity.getCurrentDate();
            String concatenatedDateAndTime = currentDate + "\t\t" + " " + currentTime;
            expectedArrivalsReference.child(Constants.FIREBASE_CHILD_DATE_AND_TIME_OF_ARRIVAL).setValue(concatenatedDateAndTime);
        }

        /**
         * This method is invoked to checked whether Expected Arrival reached into Society on expected time or not.
         *
         * @param position of card view for which expected Time is to be checked.
         */
        private boolean isExpectedArrivalReachedOnTime(int position) {
            NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals = nammaApartmentExpectedArrivalsList.get(position);
            status = nammaApartmentExpectedArrivals.getStatus();

            if (status.equals(mCtx.getString(R.string.not_entered))) {
                String expectedDateAndTime = nammaApartmentExpectedArrivals.getDateAndTimeOfArrival();
                String[] separatedDateAndTime = TextUtils.split(expectedDateAndTime, "\t\t ");
                String expectedDate = separatedDateAndTime[0];
                String expectedTime = separatedDateAndTime[1];

                String currentDate = baseActivity.getCurrentDate();
                String validFor = nammaApartmentExpectedArrivals.getValidFor();

                String[] validHours = TextUtils.split(validFor, " ");
                int hoursValidFor = Integer.parseInt(validHours[0]);
                String[] expectedHoursAndMinutes = TextUtils.split(expectedTime, ":");
                int expectedHour = Integer.parseInt(expectedHoursAndMinutes[0]);
                int expectedMinutes = Integer.parseInt(expectedHoursAndMinutes[1]);
                int totalValidHours = expectedHour + hoursValidFor;

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                return expectedDate.equals(currentDate) && (currentHour < totalValidHours || (currentHour == totalValidHours && currentMinute <= expectedMinutes));
            } else {
                return true;
            }
        }
    }
}
