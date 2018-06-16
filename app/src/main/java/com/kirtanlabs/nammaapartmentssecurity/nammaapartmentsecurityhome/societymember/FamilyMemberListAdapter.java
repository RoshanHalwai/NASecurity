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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FamilyMemberListAdapter extends RecyclerView.Adapter<FamilyMemberListAdapter.FamilyMemberViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private List<NammaApartmentFamilyMember> nammaApartmentFamilyMemberList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    FamilyMemberListAdapter(Context mCtx, List<NammaApartmentFamilyMember> nammaApartmentFamilyMembersList) {
        this.mCtx = mCtx;
        this.nammaApartmentFamilyMemberList = nammaApartmentFamilyMembersList;
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
        NammaApartmentFamilyMember nammaApartmentFamilyMember = nammaApartmentFamilyMemberList.get(position);

        holder.textFamilyMemberNameValue.setText(nammaApartmentFamilyMember.getFullName());
    }

    @Override
    public int getItemCount() {
        return nammaApartmentFamilyMemberList.size();
    }

    /* ------------------------------------------------------------- *
     * Family Member View Holder class
     * ------------------------------------------------------------- */

    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private TextView textFamilyMemberNameValue;
        private CircleImageView familyMemberProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            /*Getting Id's for all the views*/
            textFamilyMemberNameValue = itemView.findViewById(R.id.textFamilyMemberNameValue);
            familyMemberProfilePic = itemView.findViewById(R.id.familyMemberProfilePic);

            /*Setting fonts to the views*/
            textFamilyMemberNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        }
    }
}
