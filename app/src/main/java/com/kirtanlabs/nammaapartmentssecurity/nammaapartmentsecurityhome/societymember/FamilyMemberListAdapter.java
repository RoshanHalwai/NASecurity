package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;

public class FamilyMemberListAdapter extends RecyclerView.Adapter<FamilyMemberListAdapter.FamilyMemberViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    FamilyMemberListAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public FamilyMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_family_member_list, parent, false);
        return new FamilyMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyMemberViewHolder holder, int position) {

        holder.textFamilyMemberNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
    }

    @Override
    public int getItemCount() {
        //TODO: To change the get item count here
        return 3;
    }

    /* ------------------------------------------------------------- *
     * Family Member View Holder class
     * ------------------------------------------------------------- */

    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textFamilyMemberNameValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            textFamilyMemberNameValue = itemView.findViewById(R.id.textFamilyMemberNameValue);
        }
    }
}
