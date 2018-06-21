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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;

import java.util.List;


public class VisitorListAdapter extends
        RecyclerView.Adapter<VisitorListAdapter.VisitorHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private NammaApartmentVisitor nammaApartmentVisitor;
    private String fullName;
    private String flatNumber;
    private String inviterUid;
    private String apartmentName;

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
        String titleApartment = mCtx.getString(R.string.apartment) + ":";
        holder.textApartment.setText(titleApartment);

        //Creating an instance of NammaApartmentVisitor class and retrieving the values from Firebase
        nammaApartmentVisitor = nammaApartmentVisitorList.get(position);
        inviterUid = nammaApartmentVisitor.getInviterUID();
        holder.textVisitorOrDailyServiceNameValue.setText(nammaApartmentVisitor.getFullName());
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentVisitor.getProfilePhoto())
                .into(holder.VisitorAndDailyServiceProfilePic);

        //To retrieve of inviter details from firebase
        getInviterDetailsFromFireBase(holder.textFlatToVisitValue, holder.textInvitedByValue, holder.textApartmentValue);

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
        changeVisitorStatus();
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
     * @param textApartmentValue   - to display inviter apartment name in this view
     */
    private void getInviterDetailsFromFireBase(final TextView textFlatToVisitValue, final TextView textInvitedByValue, TextView textApartmentValue) {
        Constants.PRIVATE_USERS_REFERENCE
                .child(inviterUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                assert nammaApartmentUser != null;
                fullName = nammaApartmentUser.getPersonalDetails().getFullName();
                flatNumber = nammaApartmentUser.getFlatDetails().getFlatNumber();
                apartmentName = nammaApartmentUser.getFlatDetails().getApartmentName();
                textFlatToVisitValue.setText(flatNumber);
                textInvitedByValue.setText(fullName);
                textApartmentValue.setText(apartmentName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to change status of visitor
     */
    private void changeVisitorStatus() {
        DatabaseReference visitorStatusReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                .child(nammaApartmentVisitor.getUid())
                .child(Constants.FIREBASE_CHILD_STATUS);
        String visitorStatus = nammaApartmentVisitor.getStatus();
        if (visitorStatus.equals(mCtx.getString(R.string.not_entered))) {
            visitorStatusReference.setValue(mCtx.getString(R.string.entered));
        } else if (visitorStatus.equals(mCtx.getString(R.string.entered))) {
            visitorStatusReference.setValue(mCtx.getString(R.string.left));
        }
    }

    /* ------------------------------------------------------------- *
     * Validation Status View Holder class
     * ------------------------------------------------------------- */

    class VisitorHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textVisitorOrDailyServiceName;
        private TextView textApartment;
        private TextView textFlatToVisit;
        private TextView textInvitedBy;
        private TextView textVisitorOrDailyServiceNameValue;
        private TextView textApartmentValue;
        private TextView textFlatToVisitValue;
        private TextView textInvitedByValue;
        private Button buttonAllowVisitorAndDailyService;
        private final de.hdodenhof.circleimageview.CircleImageView VisitorAndDailyServiceProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        VisitorHolder(View itemView) {
            super(itemView);
            VisitorAndDailyServiceProfilePic = itemView.findViewById(R.id.VisitorAndDailyServiceProfilePic);
            textVisitorOrDailyServiceName = itemView.findViewById(R.id.textVisitorOrDailyServiceName);
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlatToVisit = itemView.findViewById(R.id.textFlatToVisit);
            textInvitedBy = itemView.findViewById(R.id.textInvitedBy);
            textVisitorOrDailyServiceNameValue = itemView.findViewById(R.id.textVisitorOrDailyServiceNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatToVisitValue = itemView.findViewById(R.id.textFlatToVisitValue);
            textInvitedByValue = itemView.findViewById(R.id.textInvitedByValue);
            buttonAllowVisitorAndDailyService = itemView.findViewById(R.id.buttonAllowVisitorAndDailyService);

            /*Setting fonts to the views*/
            textVisitorOrDailyServiceName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitedBy.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVisitorOrDailyServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitedByValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonAllowVisitorAndDailyService.setTypeface(Constants.setLatoLightFont(mCtx));
        }
    }
}
