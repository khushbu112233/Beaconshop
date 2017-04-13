package com.amplearch.beaconshop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanButton;
import com.amplearch.beaconshop.Utils.TrojanText;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 4/12/2017.
 */

public class MyProfileFragment extends Fragment
{
    Button btnClaimOffers;
    TrojanText tvGender, btnDatePicker, txtName, tvProfileId, tvOfferNumbers, tvVoucherNumbers, tvBadgeNumbers ;
    TrojanButton btnGender, txtDate ;
    LinearLayout llProfileShare , llProfileInvite ;
    CircleImageView ivImage ;
    TrojanButton btnLogout, btnSignOut, btnRevokeAccess ;

    public MyProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.profile_account, container, false);

        btnClaimOffers = (Button)rootView.findViewById(R.id.btnProfileClaimOffer);
        ivImage = (CircleImageView)rootView.findViewById(R.id.profile_picture);
        llProfileInvite = (LinearLayout)rootView.findViewById(R.id.llProfileInvite);
        llProfileShare = (LinearLayout)rootView.findViewById(R.id.llProfileShare);
        btnGender = (TrojanButton)rootView.findViewById(R.id.ProfileGenderButton);
        txtDate = (TrojanButton)rootView.findViewById(R.id.ProfileDateBirthButton);
        tvGender = (TrojanText)rootView.findViewById(R.id.ProfileGenderText);
        btnDatePicker = (TrojanText)rootView.findViewById(R.id.ProfileDateBirthText);
        txtName = (TrojanText)rootView.findViewById(R.id.tvProfileName);
        tvProfileId = (TrojanText)rootView.findViewById(R.id.tvProfileId);
        tvOfferNumbers = (TrojanText)rootView.findViewById(R.id.tvProfileOfferNumbers);
        tvVoucherNumbers = (TrojanText)rootView.findViewById(R.id.tvProfileVoucherNumbers);
        tvBadgeNumbers = (TrojanText)rootView.findViewById(R.id.tvProfileBadgeNumbers);
        btnLogout = (TrojanButton)rootView.findViewById(R.id.btnLogOut);
        btnSignOut = (TrojanButton)rootView.findViewById(R.id.btnSignOut);
        btnRevokeAccess = (TrojanButton)rootView.findViewById(R.id.btnRevokeAccess);



        return rootView;
    }

}
