package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.R;

class NammaApartmentSecurityHomeAdapter extends BaseAdapter {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] icons;
    private final String[] stringGuardServices;
    private final Context context;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    NammaApartmentSecurityHomeAdapter(Context context, int[] icons, String[] stringGuardServices) {
        this.context = context;
        this.icons = icons;
        this.stringGuardServices = stringGuardServices;
    }

    /* ------------------------------------------------------------- *
     * Overriding BaseAdapter Objects
     * ------------------------------------------------------------- */

    @Override
    public int getCount() {
        return stringGuardServices.length;
    }

    @Override
    public Object getItem(int position) {
        return stringGuardServices[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                gridView = layoutInflater.inflate(R.layout.grid_layout_nammaapatrment_security_home, parent, false);
            }

            /*Getting Id's for all the views*/
            ImageView imageNammaApartmentSecurityServices = gridView.findViewById(R.id.imageNammaApartmentSecurityServices);
            TextView textNammaApartmentSecurityServices = gridView.findViewById(R.id.textNammaApartmentSecurityServices);

            /*Setting values for all the views*/
            imageNammaApartmentSecurityServices.setImageResource(icons[position]);
            textNammaApartmentSecurityServices.setText(stringGuardServices[position]);
        }
        return gridView;
    }
}
