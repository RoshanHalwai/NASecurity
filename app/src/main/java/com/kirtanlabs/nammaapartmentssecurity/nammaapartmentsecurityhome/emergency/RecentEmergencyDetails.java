package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.BaseActivity;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class RecentEmergencyDetails extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textResidentApartmentValue;
    private TextView textResidentFlatNumberValue;
    private TextView textEmergencyTypeValue;
    private TextView textResidentNameValue;
    private TextView textResidentMobileNumberValue;
    private ImageView imageEmergencyType;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_recent_emergency_details;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.recent_emergency;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textResidentApartment = findViewById(R.id.textResidentApartment);
        TextView textResidentFlatNumber = findViewById(R.id.textResidentFlatNumber);
        TextView textEmergencyType = findViewById(R.id.textEmergencyType);
        TextView textResidentName = findViewById(R.id.textResidentName);
        TextView textResidentMobileNumber = findViewById(R.id.textResidentMobileNumber);
        textResidentApartmentValue = findViewById(R.id.textResidentApartmentValue);
        textResidentFlatNumberValue = findViewById(R.id.textResidentFlatNumberValue);
        textEmergencyTypeValue = findViewById(R.id.textEmergencyTypeValue);
        textResidentNameValue = findViewById(R.id.textResidentNameValue);
        textResidentMobileNumberValue = findViewById(R.id.textResidentMobileNumberValue);
        imageEmergencyType = findViewById(R.id.imageEmergencyType);

        /*Setting fonts to the views*/
        textResidentApartment.setTypeface(Constants.setLatoRegularFont(this));
        textResidentFlatNumber.setTypeface(Constants.setLatoRegularFont(this));
        textEmergencyType.setTypeface(Constants.setLatoRegularFont(this));
        textResidentName.setTypeface(Constants.setLatoRegularFont(this));
        textResidentMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        textResidentApartmentValue.setTypeface(Constants.setLatoBoldFont(this));
        textResidentFlatNumberValue.setTypeface(Constants.setLatoBoldFont(this));
        textEmergencyTypeValue.setTypeface(Constants.setLatoBoldFont(this));
        textResidentNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textResidentMobileNumberValue.setTypeface(Constants.setLatoBoldFont(this));

        /*Setting titles for views*/
        String apartmentTitle = getString(R.string.apartment) + ":";
        textResidentApartment.setText(apartmentTitle);
        String flatNumberTitle = getString(R.string.flat_number) + ":";
        textResidentFlatNumber.setText(flatNumberTitle);
        String mobileNumberTitle = getString(R.string.phone_number) + ":";
        textResidentMobileNumber.setText(mobileNumberTitle);

        // To display of details of recent occurred emergency
        showRecentEmergencyDetails();

    }

    /*-------------------------------------------------------------------------------
     * Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method is invoked to display all details of Latest Emergency raised by the user
     */
    private void showRecentEmergencyDetails() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        String emergencyType = bundle.getString(Constants.EMERGENCY_TYPE);
        String ownerName = bundle.getString(Constants.OWNER_NAME);
        String mobileNumber = bundle.getString(Constants.MOBILE_NUMBER);
        String apartmentName = bundle.getString(Constants.APARTMENT_NAME);
        String flatNumber = bundle.getString(Constants.FLAT_NUMBER);

        textEmergencyTypeValue.setText(emergencyType);
        textResidentNameValue.setText(ownerName);
        textResidentMobileNumberValue.setText(mobileNumber);
        textResidentApartmentValue.setText(apartmentName);
        textResidentFlatNumberValue.setText(flatNumber);

        // Here we are setting image in cardView according to the emergency type
        switch (emergencyType) {
            case Constants.EMERGENCY_TYPE_MEDICAL:
                imageEmergencyType.setImageResource(R.drawable.medical_emergency);
                break;
            case Constants.EMERGENCY_TYPE_FIRE:
                imageEmergencyType.setImageResource(R.drawable.fire_alarm);
                break;
            case Constants.EMERGENCY_TYPE_THEFT:
                imageEmergencyType.setImageResource(R.drawable.theft_alarm);
                break;
        }
    }
}
