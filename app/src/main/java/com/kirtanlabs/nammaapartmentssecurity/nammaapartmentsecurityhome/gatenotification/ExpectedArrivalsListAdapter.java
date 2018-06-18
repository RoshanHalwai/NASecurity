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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;

import java.util.List;

public class ExpectedArrivalsListAdapter extends RecyclerView.Adapter<ExpectedArrivalsListAdapter.ExpectedArrivalsHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;
    private NammaApartmentExpectedArrivals nammaApartmentExpectedArrivals;
    private List<NammaApartmentExpectedArrivals> nammaApartmentExpectedArrivalsList;
    private String bookedBy;
    private String flatToVisit;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    ExpectedArrivalsListAdapter(Context mCtx, List<NammaApartmentExpectedArrivals> nammaApartmentExpectedArrivalsList) {
        this.mCtx = mCtx;
        this.nammaApartmentExpectedArrivalsList = nammaApartmentExpectedArrivalsList;
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
        //Creating an instance of NammaApartmentExpectedArrivals class and retrieving the values from Firebase
        nammaApartmentExpectedArrivals = nammaApartmentExpectedArrivalsList.get(position);

        //To retrieve Owners details from firebase
        getOwnerDetailsFromFireBase(holder.textBookedByValue, holder.textFlatNumberValue);

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
        Intent intent = new Intent(mCtx, NammaApartmentSecurityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mCtx.startActivity(intent);
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    private void getOwnerDetailsFromFireBase(final TextView textBookedByValue, final TextView textFlatNumberValue) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS)
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(nammaApartmentExpectedArrivals.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        bookedBy = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_FULL_NAME).getValue();
                        flatToVisit = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_FLAT_NUMBER).getValue();
                        textBookedByValue.setText(bookedBy);
                        textFlatNumberValue.setText(flatToVisit);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /* ------------------------------------------------------------- *
     * Expected Cab Arrivals View Holder class
     * ------------------------------------------------------------- */

    class ExpectedArrivalsHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        TextView textBookedBy;
        TextView textFlatNumber;
        TextView textDateToVisit;
        TextView textTimeToVisit;
        TextView textBookedByValue;
        TextView textFlatNumberValue;
        TextView textDateToVisitValue;
        TextView textTimeToVisitValue;
        Button buttonAllowExpectedArrivals;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ExpectedArrivalsHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textBookedBy = itemView.findViewById(R.id.textBookedBy);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textDateToVisit = itemView.findViewById(R.id.textDateToVisit);
            textTimeToVisit = itemView.findViewById(R.id.textTimeToVisit);
            textBookedByValue = itemView.findViewById(R.id.textBookedByValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textDateToVisitValue = itemView.findViewById(R.id.textDateToVisitValue);
            textTimeToVisitValue = itemView.findViewById(R.id.textTimeToVisitValue);
            buttonAllowExpectedArrivals = itemView.findViewById(R.id.buttonAllowArrivals);

            /*Setting fonts to the views*/
            textBookedBy.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textDateToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textTimeToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textBookedByValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textDateToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textTimeToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonAllowExpectedArrivals.setTypeface(Constants.setLatoLightFont(mCtx));
        }
    }
}
