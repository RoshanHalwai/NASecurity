package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation.NammaApartmentDailyService;

import java.util.List;

public class ThingsGivenToDailyServiceAdapter extends RecyclerView.Adapter<ThingsGivenToDailyServiceAdapter.ThingsGivenHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private NammaApartmentDailyService nammaApartmentDailyService;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    ThingsGivenToDailyServiceAdapter(Context mCtx, List<NammaApartmentDailyService> nammaApartmentDailyServiceList) {
        this.mCtx = mCtx;
        this.nammaApartmentDailyServiceList = nammaApartmentDailyServiceList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public ThingsGivenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_things_given, parent, false);
        return new ThingsGivenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThingsGivenHolder holder, int position) {
        String titleApartment = mCtx.getString(R.string.apartment) + ":";
        holder.textApartment.setText(titleApartment);
        String titleFlat = mCtx.getString(R.string.flat) + ":";
        holder.textFlat.setText(titleFlat);

        //Creating an instance of NammaApartmentDailyService class and retrieving the values from Firebase
        nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);

        DatabaseReference ownersReference = Constants.PRIVATE_USERS_REFERENCE
                .child(nammaApartmentDailyService.getOwnerUid());
        // Retrieving details of owner from (Users->Private->ownersUID) in firebase.
        ownersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                assert nammaApartmentUser != null;
                holder.textApartmentValue.setText(nammaApartmentUser.getFlatDetails().getApartmentName());
                holder.textFlatValue.setText(nammaApartmentUser.getFlatDetails().getFlatNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        holder.buttonOk.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentDailyServiceList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        updateStatus();
        Intent intentThingsGiven = new Intent(mCtx, NammaApartmentSecurityHome.class);
        intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentThingsGiven.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mCtx.startActivity(intentThingsGiven);
    }

    /**
     * This method is invoked to change status of Daily Service.
     */
    private void updateStatus() {
        DatabaseReference dailyServiceReference = Constants.PUBLIC_DAILYSERVICES_REFERENCE
                .child(nammaApartmentDailyService.getDailyServiceType())
                .child(nammaApartmentDailyService.getUid())
                .child(Constants.FIREBASE_CHILD_STATUS);
        dailyServiceReference.setValue(mCtx.getString(R.string.left));
    }

    /* ------------------------------------------------------------- *
     * Things Given View Holder class
     * ------------------------------------------------------------- */

    class ThingsGivenHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textApartment;
        private final TextView textFlat;
        private final TextView textApartmentValue;
        private final TextView textFlatValue;
        private final TextView textHasGivenThings;
        private final Button buttonOk;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ThingsGivenHolder(View itemView) {
            super(itemView);
            /*Getting Id's for all the views*/
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlat = itemView.findViewById(R.id.textFlat);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatValue = itemView.findViewById(R.id.textFlatValue);
            textHasGivenThings = itemView.findViewById(R.id.textHasGivenThings);
            buttonOk = itemView.findViewById(R.id.buttonOk);

            /*Setting fonts to the views*/
            textApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlat.setTypeface(Constants.setLatoRegularFont(mCtx));
            textApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textHasGivenThings.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonOk.setTypeface(Constants.setLatoLightFont(mCtx));
        }
    }
}
