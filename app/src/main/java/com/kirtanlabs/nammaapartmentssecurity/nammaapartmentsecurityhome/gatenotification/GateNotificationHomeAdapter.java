package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.gatenotification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.R;

public class GateNotificationHomeAdapter extends BaseAdapter {
    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private final Context context;
    private final int[] icons;
    private final String[] stringGateNotificationServices;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */
    GateNotificationHomeAdapter(Context context, int[] icons, String[] stringGateNotificationServices) {
        this.context = context;
        this.icons = icons;
        this.stringGateNotificationServices = stringGateNotificationServices;
    }

    /* ------------------------------------------------------------- *
     * Overriding BaseAdapter Objects
     * ------------------------------------------------------------- */
    @Override
    public int getCount() {
        return stringGateNotificationServices.length;
    }

    @Override
    public Object getItem(int position) {
        return stringGateNotificationServices[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                listView = layoutInflater.inflate(R.layout.layout_gate_notification_services, parent, false);
            }
            /*Getting Id's for all the views*/
            ImageView imageGateNotificationService = listView.findViewById(R.id.imageGateNotificationService);
            TextView textGateNotification = listView.findViewById(R.id.textGateNotification);

            /*Setting values for all the views*/
            imageGateNotificationService.setImageResource(icons[position]);
            textGateNotification.setText(stringGateNotificationServices[position]);
        }
        return listView;
    }
}
