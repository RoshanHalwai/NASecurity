package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    EmergencyAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public EmergencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_emergency_list, parent, false);
        return new EmergencyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyHolder holder, int position) {
        holder.textResidentFlatNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textEmergencyType.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textResidentName.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textResidentMobileNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textResidentFlatNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textEmergencyTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textResidentNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textResidentMobileNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));

        String flatNumberTitle = mCtx.getResources().getString(R.string.flat_number);
        flatNumberTitle = flatNumberTitle + ":";
        holder.textResidentFlatNumber.setText(flatNumberTitle);

        String mobileNumberTitle = mCtx.getResources().getString(R.string.phone_number);
        mobileNumberTitle = mobileNumberTitle + ":";
        holder.textResidentMobileNumber.setText(mobileNumberTitle);

        // TODO : To Remove this switch case statement from here
        switch (position) {
            case 0:
                holder.imageEmergencyType.setImageResource(R.drawable.medical_emergency);
                holder.textEmergencyTypeValue.setText("Medical");
                break;
            case 1:
                holder.imageEmergencyType.setImageResource(R.drawable.fire_alarm);
                holder.textEmergencyTypeValue.setText("Fire");
                break;
            case 2:
                holder.imageEmergencyType.setImageResource(R.drawable.theft_alarm);
                holder.textEmergencyTypeValue.setText("Theft");
                break;
        }
    }

    @Override
    public int getItemCount() {
        // TODO : To change item count here
        return 3;
    }

    /* ------------------------------------------------------------- *
     * Family Member View Holder class
     * ------------------------------------------------------------- */

    class EmergencyHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textResidentFlatNumber;
        private TextView textEmergencyType;
        private TextView textResidentName;
        private TextView textResidentMobileNumber;
        private TextView textResidentFlatNumberValue;
        private TextView textEmergencyTypeValue;
        private TextView textResidentNameValue;
        private TextView textResidentMobileNumberValue;
        private ImageView imageEmergencyType;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        EmergencyHolder(View itemView) {
            super(itemView);
            textResidentFlatNumber = itemView.findViewById(R.id.textResidentFlatNumber);
            textEmergencyType = itemView.findViewById(R.id.textEmergencyType);
            textResidentName = itemView.findViewById(R.id.textResidentName);
            textResidentMobileNumber = itemView.findViewById(R.id.textResidentMobileNumber);
            textResidentFlatNumberValue = itemView.findViewById(R.id.textResidentFlatNumberValue);
            textEmergencyTypeValue = itemView.findViewById(R.id.textEmergencyTypeValue);
            textResidentNameValue = itemView.findViewById(R.id.textResidentNameValue);
            textResidentMobileNumberValue = itemView.findViewById(R.id.textResidentMobileNumberValue);
            imageEmergencyType = itemView.findViewById(R.id.imageEmergencyType);
        }
    }
}