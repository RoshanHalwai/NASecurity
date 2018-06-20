package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.societymember;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartmentssecurity.Constants;
import com.kirtanlabs.nammaapartmentssecurity.R;
import com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.userpojo.NammaApartmentUser;

import java.util.List;


public class FamilyMemberListAdapter extends RecyclerView.Adapter<FamilyMemberListAdapter.FamilyMemberViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private List<NammaApartmentUser> nammaApartmentFamilyMemberList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    FamilyMemberListAdapter(Context mCtx, List<NammaApartmentUser> nammaApartmentFamilyMembersList) {
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
        NammaApartmentUser nammaApartmentFamilyMember = nammaApartmentFamilyMemberList.get(position);

        holder.textFamilyMemberNameValue.setText(nammaApartmentFamilyMember.getPersonalDetails().getFullName());
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentFamilyMember.getPersonalDetails().getProfilePhoto()).into(holder.familyMemberProfilePic);
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
        private final de.hdodenhof.circleimageview.CircleImageView familyMemberProfilePic;

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
