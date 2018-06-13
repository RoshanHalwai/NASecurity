package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.visitorsordailyservicesvalidation;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;

import java.util.List;

public class DailyServiceListAdapter extends RecyclerView.Adapter<DailyServiceListAdapter.DailyServiceHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private NammaApartmentDailyService nammaApartmentDailyService;
    private String ownerUid;
    private String flatNumber;
    private String serviceType;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    DailyServiceListAdapter(Context mCtx, List<NammaApartmentDailyService> nammaApartmentDailyServiceList, String serviceType) {
        this.mCtx = mCtx;
        this.nammaApartmentDailyServiceList = nammaApartmentDailyServiceList;
        this.serviceType = serviceType;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public DailyServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitor_and_daily_service, parent, false);
        return new DailyServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyServiceHolder holder, int position) {
        changeViewsTitle(holder.textVisitorOrDailyServiceName, holder.buttonAllowVisitorAndDailyService);
        holder.textInvitedBy.setVisibility(View.GONE);
        holder.textInvitedByValue.setVisibility(View.GONE);

        nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        holder.textVisitorOrDailyServiceNameValue.setText(nammaApartmentDailyService.getFullName());

        //To retrieve of owner details from firebase
        getOwnerDetailsFromFireBase(holder.textFlatToVisitValue);

        holder.buttonAllowVisitorAndDailyService.setOnClickListener(this);
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
        Intent intent = new Intent(mCtx, NammaApartmentSecurityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mCtx.startActivity(intent);
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * We update the VisitorOrDailyServiceName Title and  Button AllowVisitorsAndEIntercom Text in
     * Daily Services Validation Status screen
     *
     * @param textVisitorOrDailyServiceName     - to update title in Daily Services Validation Status Screen
     * @param buttonAllowVisitorAndDailyService - to update text in Daily Services Validation Status Screen
     */
    private void changeViewsTitle(TextView textVisitorOrDailyServiceName, Button buttonAllowVisitorAndDailyService) {
        String nameTitle = mCtx.getResources().getString(R.string.visitor_name);
        nameTitle = nameTitle.substring(8);
        textVisitorOrDailyServiceName.setText(nameTitle);

        String allowTo = mCtx.getString(R.string.allow_visitor);
        allowTo = allowTo.replace("Visitor", "Daily Service");
        buttonAllowVisitorAndDailyService.setText(allowTo);
    }

    /**
     * This method is used to retrieve details of owner
     *
     * @param textFlatToVisitValue - to display owner flat number in this view
     */
    private void getOwnerDetailsFromFireBase(final TextView textFlatToVisitValue) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_DAILYSERVICES)
                .child(Constants.FIREBASE_CHILD_ALL)
                .child(Constants.FIREBASE_CHILD_PUBLIC)
                .child(serviceType)
                .child(nammaApartmentDailyService.getUid())
                .child(Constants.FIREBASE_CHILD_OWNERS_UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ownerUidDataSnapshot : dataSnapshot.getChildren()) {
                            ownerUid = ownerUidDataSnapshot.getKey();
                        }

                        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS)
                                .child(Constants.FIREBASE_CHILD_PRIVATE)
                                .child(ownerUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                flatNumber = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_FLAT_NUMBER).getValue();
                                textFlatToVisitValue.setText(flatNumber);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /* ------------------------------------------------------------- *
     * Daily Service View Holder class
     * ------------------------------------------------------------- */

    class DailyServiceHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        TextView textVisitorOrDailyServiceName;
        TextView textFlatToVisit;
        TextView textInvitedBy;
        TextView textVisitorOrDailyServiceNameValue;
        TextView textFlatToVisitValue;
        TextView textInvitedByValue;
        Button buttonAllowVisitorAndDailyService;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        DailyServiceHolder(View itemView) {
            super(itemView);

            textVisitorOrDailyServiceName = itemView.findViewById(R.id.textVisitorOrDailyServiceName);
            textFlatToVisit = itemView.findViewById(R.id.textFlatToVisit);
            textInvitedBy = itemView.findViewById(R.id.textInvitedBy);
            textVisitorOrDailyServiceNameValue = itemView.findViewById(R.id.textVisitorOrDailyServiceNameValue);
            textFlatToVisitValue = itemView.findViewById(R.id.textFlatToVisitValue);
            textInvitedByValue = itemView.findViewById(R.id.textInvitedByValue);
            buttonAllowVisitorAndDailyService = itemView.findViewById(R.id.buttonAllowVisitorAndDailyService);

            /*Setting fonts to the views*/
            textVisitorOrDailyServiceName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVisitorOrDailyServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonAllowVisitorAndDailyService.setTypeface(Constants.setLatoLightFont(mCtx));
        }
    }
}
