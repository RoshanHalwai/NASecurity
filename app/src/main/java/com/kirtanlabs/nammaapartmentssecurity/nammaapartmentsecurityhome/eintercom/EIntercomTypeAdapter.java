package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.R;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssecurity.Constants.CAB;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.EINTERCOM_TYPE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.GUEST;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.PACKAGE;
import static com.kirtanlabs.nammaapartmentssecurity.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/5/2018
 */
public class EIntercomTypeAdapter extends RecyclerView.Adapter<EIntercomTypeAdapter.VisitorsListHolder> {

    private final Context mCtx;
    private final List<Type> visitorsList;

    EIntercomTypeAdapter(Context mCtx, List<Type> visitorsList) {
        this.mCtx = mCtx;
        this.visitorsList = visitorsList;
    }

    @NonNull
    @Override
    public VisitorsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_eintercom_type, parent, false);
        return new VisitorsListHolder(view, mCtx);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorsListHolder holder, int position) {
        Type nammaApartmentService = visitorsList.get(position);
        holder.textNotification.setTypeface(setLatoRegularFont(mCtx));
        holder.textNotification.setText(nammaApartmentService.getIntercomType());
        holder.imageNotificationService.setImageResource(nammaApartmentService.getIntercomTypeImage());
    }

    @Override
    public int getItemCount() {
        return visitorsList.size();
    }

    public class VisitorsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textNotification;
        final ImageView imageNotificationService;
        private final Context mCtx;

        VisitorsListHolder(View itemView, Context mCtx) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mCtx = mCtx;
            textNotification = itemView.findViewById(R.id.textNotification);
            imageNotificationService = itemView.findViewById(R.id.imageNotificationService);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = null;
            switch (position) {
                case 0: {
                    intent = new Intent(mCtx, EIntercom.class);
                    intent.putExtra(EINTERCOM_TYPE, GUEST);
                    break;
                }
                case 1: {
                    intent = new Intent(mCtx, EIntercom.class);
                    intent.putExtra(EINTERCOM_TYPE, CAB);
                    break;
                }
                case 2: {
                    intent = new Intent(mCtx, EIntercom.class);
                    intent.putExtra(EINTERCOM_TYPE, PACKAGE);
                    break;
                }
            }
            mCtx.startActivity(intent);
        }
    }

}

