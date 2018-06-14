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

public class VisitorListAdapter extends
        RecyclerView.Adapter<VisitorListAdapter.VisitorHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private String fullName;
    private String flatNumber;
    private String inviterUid;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    VisitorListAdapter(Context mCtx, List<NammaApartmentVisitor> nammaApartmentVisitorList) {
        this.mCtx = mCtx;
        this.nammaApartmentVisitorList = nammaApartmentVisitorList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public VisitorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitor_and_daily_service, parent, false);
        return new VisitorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorHolder holder, int position) {
        //Creating an instance of NammaApartmentVisitor class and retrieving the values from Firebase
        NammaApartmentVisitor nammaApartmentValidVisitor = nammaApartmentVisitorList.get(position);
        inviterUid = nammaApartmentValidVisitor.getInviterUID();
        holder.textVisitorOrDailyServiceNameValue.setText(nammaApartmentValidVisitor.getFullName());

        //To retrieve of inviter details from firebase
        getInviterDetailsFromFireBase(holder.textFlatToVisitValue, holder.textInvitedByValue);

        holder.buttonAllowVisitorAndDailyService.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentVisitorList.size();
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

    class VisitorHolder extends RecyclerView.ViewHolder {

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

        VisitorHolder(View itemView) {
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
