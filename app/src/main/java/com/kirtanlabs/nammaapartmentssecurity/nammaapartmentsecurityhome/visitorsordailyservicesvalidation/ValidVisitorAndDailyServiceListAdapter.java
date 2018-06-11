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

public class ValidVisitorAndDailyServiceListAdapter extends
        RecyclerView.Adapter<ValidVisitorAndDailyServiceListAdapter.ValidVisitorAndDailyServiceHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private List<NammaApartmentVisitor> nammaApartmentValidVisitorList;
    private int validationStatusOf;
    private String fullName;
    private String flatNumber;
    private String inviterUid;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    ValidVisitorAndDailyServiceListAdapter(Context mCtx, int validationStatusOf, List<NammaApartmentVisitor> nammaApartmentValidVisitorList) {
        this.mCtx = mCtx;
        this.validationStatusOf = validationStatusOf;
        this.nammaApartmentValidVisitorList = nammaApartmentValidVisitorList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public ValidVisitorAndDailyServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitor_and_daily_service, parent, false);
        return new ValidVisitorAndDailyServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValidVisitorAndDailyServiceHolder holder, int position) {

        if (validationStatusOf == R.string.daily_service_validation_status) {
            changeViewsTitle(holder.textVisitorOrDailyServiceName, holder.buttonAllowVisitorAndDailyService);
            holder.textInvitedBy.setVisibility(View.GONE);
            holder.textInvitedByValue.setVisibility(View.GONE);
        }

        //Creating an instance of NammaApartmentVisitor class and retrieving the values from Firebase
        NammaApartmentVisitor nammaApartmentValidVisitor = nammaApartmentValidVisitorList.get(position);
        inviterUid = nammaApartmentValidVisitor.getInviterUID();
        holder.textVisitorOrDailyServiceNameValue.setText(nammaApartmentValidVisitor.getFullName());

        //To retrieve of inviter details from firebase
        getInviterDetailsFromFireBase(holder.textFlatToVisitValue, holder.textInvitedByValue);

        holder.buttonAllowVisitorAndDailyService.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentValidVisitorList.size();
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
     * We update the VisitorOrDailyServiceName Title and  Button AllowVisitorsAndEIntercom Text when in
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
     * This method is used to retrieve details of inviter
     *
     * @param textFlatToVisitValue - to display inviter flat number in this view
     * @param textInvitedByValue   - to display inviter name in this view
     */
    private void getInviterDetailsFromFireBase(final TextView textFlatToVisitValue, final TextView textInvitedByValue) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS)
                .child(Constants.FIREBASE_CHILD_PRIVATE).child(inviterUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fullName = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_FULL_NAME).getValue();
                flatNumber = (String) dataSnapshot.child(Constants.FIREBASE_CHILD_FLAT_NUMBER).getValue();
                textFlatToVisitValue.setText(flatNumber);
                textInvitedByValue.setText(fullName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Validation Status View Holder class
     * ------------------------------------------------------------- */

    class ValidVisitorAndDailyServiceHolder extends RecyclerView.ViewHolder {

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

        ValidVisitorAndDailyServiceHolder(View itemView) {
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
            textInvitedBy.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVisitorOrDailyServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitedByValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonAllowVisitorAndDailyService.setTypeface(Constants.setLatoLightFont(mCtx));
        }
    }
}
