package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Context;
import android.content.Intent;
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
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;

import java.util.List;

public class ExpectedArrivalsListAdapter extends RecyclerView.Adapter<ExpectedArrivalsListAdapter.ExpectedArrivalsHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals;
    private List<NammaApartmentExpectedArrivals> nammaApartmentExpectedArrivalsList;
    private DatabaseReference expectedArrivalsReference;
    private int validationStatusOf;

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

        /*Since we are using same layout for Cab Driver and Package Vendor Validation we need to
         *change some Views Text in Package Vendor Validation*/
        if (validationStatusOf == R.string.package_vendor_validation_status) {
            changeViewsText(holder.textBookedBy, holder.buttonAllowExpectedArrivals);
        }

        //Creating an instance of NammaApartmentExpectedArrivals class and retrieving the values from Firebase
        nammaApartmentExpectedArrivals = nammaApartmentExpectedArrivalsList.get(position);

        //To retrieve Owners details from firebase
        getOwnerDetailsFromFireBase(holder.textBookedByValue, holder.textFlatNumberValue, holder.textApartmentValue);

        String dateAndTime = nammaApartmentExpectedArrivals.getDateAndTimeOfArrival();
        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
        holder.textDateToVisitValue.setText(separatedDateAndTime[0]);
        holder.textTimeToVisitValue.setText(separatedDateAndTime[1]);

        /*Setting onClickListener for view*/
        holder.buttonAllowExpectedArrivals.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentExpectedArrivalsList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        changeExpectedArrivalStatusInFirebase();
        Intent intent = new Intent(mCtx, NammaApartmentSecurityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mCtx.startActivity(intent);
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
                .child(nammaApartmentExpectedArrivals.getUid());
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

    /**
     * This method is invoked to change status of Expected Arrivals
     */
    private void changeExpectedArrivalStatusInFirebase() {
        String status = nammaApartmentExpectedArrivals.getStatus();
        expectedArrivalsReference = Constants.PUBLIC_CABS_REFERENCE
                .child(nammaApartmentExpectedArrivals.getExpectedArrivalUid());
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

    /* ------------------------------------------------------------- *
     * Expected Cab Arrivals View Holder class
     * ------------------------------------------------------------- */

    class ExpectedArrivalsHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textBookedBy;
        private TextView textApartment;
        private TextView textFlatNumber;
        private TextView textDateToVisit;
        private TextView textTimeToVisit;
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
            textBookedBy = itemView.findViewById(R.id.textBookedBy);
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textDateToVisit = itemView.findViewById(R.id.textDateToVisit);
            textTimeToVisit = itemView.findViewById(R.id.textTimeToVisit);
            textBookedByValue = itemView.findViewById(R.id.textBookedByValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textDateToVisitValue = itemView.findViewById(R.id.textDateToVisitValue);
            textTimeToVisitValue = itemView.findViewById(R.id.textTimeToVisitValue);
            buttonAllowExpectedArrivals = itemView.findViewById(R.id.buttonAllowArrivals);

            /*Setting fonts to the views*/
            textBookedBy.setTypeface(Constants.setLatoRegularFont(mCtx));
            textApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textDateToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textTimeToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textBookedByValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textDateToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textTimeToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonAllowExpectedArrivals.setTypeface(Constants.setLatoLightFont(mCtx));
        }
    }
}
