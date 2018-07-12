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

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private List<NammaApartmentEmergency> nammaApartmentEmergencyList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    EmergencyAdapter(Context mCtx, List<NammaApartmentEmergency> nammaApartmentEmergencyList) {
        this.mCtx = mCtx;
        this.nammaApartmentEmergencyList = nammaApartmentEmergencyList;
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
        //Creating an instance of NammaApartmentEmergency class and retrieving the values from Firebase
        NammaApartmentEmergency nammaApartmentEmergency = nammaApartmentEmergencyList.get(position);
        String emergencyType = nammaApartmentEmergency.getEmergencyType();
        holder.textEmergencyTypeValue.setText(emergencyType);
        holder.textResidentNameValue.setText(nammaApartmentEmergency.getFullName());
        holder.textResidentMobileNumberValue.setText(nammaApartmentEmergency.getPhoneNumber());
        holder.textResidentApartmentValue.setText(nammaApartmentEmergency.getApartmentName());
        holder.textResidentFlatNumberValue.setText(nammaApartmentEmergency.getFlatNumber());

        // Here we are setting image in cardView according to the emergency type
        switch (emergencyType) {
            case Constants.EMERGENCY_TYPE_MEDICAL:
                holder.imageEmergencyType.setImageResource(R.drawable.medical_emergency);
                break;
            case Constants.EMERGENCY_TYPE_FIRE:
                holder.imageEmergencyType.setImageResource(R.drawable.fire_alarm);
                break;
            case Constants.EMERGENCY_TYPE_THEFT:
                holder.imageEmergencyType.setImageResource(R.drawable.theft_alarm);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return nammaApartmentEmergencyList.size();
    }

    /* ------------------------------------------------------------- *
     * Family Member View Holder class
     * ------------------------------------------------------------- */

    class EmergencyHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textResidentApartment;
        private TextView textResidentFlatNumber;
        private TextView textEmergencyType;
        private TextView textResidentName;
        private TextView textResidentMobileNumber;
        private TextView textResidentApartmentValue;
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
            /*Getting Id's for all the views*/
            textResidentApartment = itemView.findViewById(R.id.textResidentApartment);
            textResidentFlatNumber = itemView.findViewById(R.id.textResidentFlatNumber);
            textEmergencyType = itemView.findViewById(R.id.textEmergencyType);
            textResidentName = itemView.findViewById(R.id.textResidentName);
            textResidentMobileNumber = itemView.findViewById(R.id.textResidentMobileNumber);
            textResidentApartmentValue = itemView.findViewById(R.id.textResidentApartmentValue);
            textResidentFlatNumberValue = itemView.findViewById(R.id.textResidentFlatNumberValue);
            textEmergencyTypeValue = itemView.findViewById(R.id.textEmergencyTypeValue);
            textResidentNameValue = itemView.findViewById(R.id.textResidentNameValue);
            textResidentMobileNumberValue = itemView.findViewById(R.id.textResidentMobileNumberValue);
            imageEmergencyType = itemView.findViewById(R.id.imageEmergencyType);

            /*Setting fonts to the views*/
            textResidentApartment.setTypeface(Constants.setLatoRegularFont(mCtx));
            textResidentFlatNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textEmergencyType.setTypeface(Constants.setLatoRegularFont(mCtx));
            textResidentName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textResidentMobileNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textResidentApartmentValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textResidentFlatNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textEmergencyTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textResidentNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textResidentMobileNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));

            /*Setting titles for views*/
            String apartmentTitle = mCtx.getResources().getString(R.string.apartment) + ":";
            textResidentApartment.setText(apartmentTitle);
            String flatNumberTitle = mCtx.getResources().getString(R.string.flat_number) + ":";
            textResidentFlatNumber.setText(flatNumberTitle);
            String mobileNumberTitle = mCtx.getResources().getString(R.string.phone_number) + ":";
            textResidentMobileNumber.setText(mobileNumberTitle);
        }
    }
}