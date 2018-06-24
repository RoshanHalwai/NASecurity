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
import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.NammaApartmentSecurityHome;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DailyServiceListAdapter extends RecyclerView.Adapter<DailyServiceListAdapter.DailyServiceHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private NammaApartmentDailyService nammaApartmentDailyService;
    private DatabaseReference dailyServiceReference;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    DailyServiceListAdapter(Context mCtx, List<NammaApartmentDailyService> nammaApartmentDailyServiceList) {
        this.mCtx = mCtx;
        this.baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentDailyServiceList = nammaApartmentDailyServiceList;
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
        String titleApartment = mCtx.getString(R.string.apartment) + ":";
        holder.textApartment.setText(titleApartment);

        /*We use a common layout for Visitor and Daily Service List screen,so
         *we change titles of some views in Daily Service List*/
        changeViewsTitle(holder.textVisitorOrDailyServiceName, holder.buttonAllowVisitorAndDailyService);
        holder.textInvitedBy.setVisibility(View.GONE);
        holder.textInvitedByValue.setVisibility(View.GONE);
        holder.textServiceType.setVisibility(View.VISIBLE);
        holder.textServiceTypeValue.setVisibility(View.VISIBLE);

        //Creating an instance of NammaApartmentDailyService class and retrieving the values from Firebase
        nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        holder.textVisitorOrDailyServiceNameValue.setText(nammaApartmentDailyService.getFullName());
        holder.textServiceTypeValue.setText(nammaApartmentDailyService.getDailyServiceType());
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentDailyService.getProfilePhoto()).into(holder.VisitorAndDailyServiceProfilePic);

        //To retrieve of owner details from firebase
        getOwnerDetailsFromFireBase(holder.textFlatToVisitValue, holder.textApartmentValue);

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
        changeDailyServiceStatus();
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
    private void changeViewsTitle(final TextView textVisitorOrDailyServiceName, final Button buttonAllowVisitorAndDailyService) {
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
     * @param textApartmentValue   - to display owner apartment name in this view
     */
    private void getOwnerDetailsFromFireBase(final TextView textFlatToVisitValue, final TextView textApartmentValue) {
        Constants.PRIVATE_USERS_REFERENCE
                .child(nammaApartmentDailyService.getOwnerUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                        assert nammaApartmentUser != null;
                        textFlatToVisitValue.setText(nammaApartmentUser.getFlatDetails().getFlatNumber());
                        textApartmentValue.setText(nammaApartmentUser.getFlatDetails().getApartmentName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * This method is invoked to change status of daily service
     */
    private void changeDailyServiceStatus() {
        dailyServiceReference = Constants.PUBLIC_DAILYSERVICES_REFERENCE
                .child(nammaApartmentDailyService.getDailyServiceType())
                .child(nammaApartmentDailyService.getUid());
        String dailyServiceStatus = nammaApartmentDailyService.getStatus();
        baseActivity.changeStatus(dailyServiceStatus, dailyServiceReference.child(Constants.FIREBASE_CHILD_STATUS));

        if (dailyServiceStatus.equals(mCtx.getString(R.string.not_entered))) {
            updateDailyServiceTimeInFirebase();
        }
    }

    /**
     * This method is invoked to change timeOfVisit of daily service in Firebase.
     */
    private void updateDailyServiceTimeInFirebase() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        String currentTime = String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute);
        dailyServiceReference.child(Constants.FIREBASE_CHILD_TIMEOFVISIT).setValue(currentTime);
    }

    /* ------------------------------------------------------------- *
     * Daily Service View Holder class
     * ------------------------------------------------------------- */

    class DailyServiceHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textVisitorOrDailyServiceName;
        private TextView textServiceType;
        private TextView textApartment;
        private TextView textFlatToVisit;
        private TextView textInvitedBy;
        private TextView textVisitorOrDailyServiceNameValue;
        private TextView textServiceTypeValue;
        private TextView textApartmentValue;
        private TextView textFlatToVisitValue;
        private TextView textInvitedByValue;
        private Button buttonAllowVisitorAndDailyService;
        private final de.hdodenhof.circleimageview.CircleImageView VisitorAndDailyServiceProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        DailyServiceHolder(View itemView) {
            super(itemView);

            VisitorAndDailyServiceProfilePic = itemView.findViewById(R.id.VisitorAndDailyServiceProfilePic);
            textServiceType = itemView.findViewById(R.id.textServiceType);
            textApartment = itemView.findViewById(R.id.textApartment);
            textVisitorOrDailyServiceName = itemView.findViewById(R.id.textVisitorOrDailyServiceName);
            textFlatToVisit = itemView.findViewById(R.id.textFlatToVisit);
            textInvitedBy = itemView.findViewById(R.id.textInvitedBy);
            textVisitorOrDailyServiceNameValue = itemView.findViewById(R.id.textVisitorOrDailyServiceNameValue);
            textServiceTypeValue = itemView.findViewById(R.id.textServiceTypeValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatToVisitValue = itemView.findViewById(R.id.textFlatToVisitValue);
            textInvitedByValue = itemView.findViewById(R.id.textInvitedByValue);
            buttonAllowVisitorAndDailyService = itemView.findViewById(R.id.buttonAllowVisitorAndDailyService);

            /*Setting fonts to the views*/
            textVisitorOrDailyServiceName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textServiceType.setTypeface(Constants.setLatoRegularFont(mCtx));
            textApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textFlatToVisit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVisitorOrDailyServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textServiceTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textFlatToVisitValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            buttonAllowVisitorAndDailyService.setTypeface(Constants.setLatoLightFont(mCtx));
        }
    }
}