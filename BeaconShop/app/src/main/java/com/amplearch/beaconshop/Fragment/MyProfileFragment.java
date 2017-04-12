package com.amplearch.beaconshop.Fragment;

import android.os.Bundle;
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
    TrojanText tvGender, tvDateBirth, tvProfileName, tvProfileId, tvOfferNumbers, tvVoucherNumbers, tvBadgeNumbers ;
    TrojanButton btnGender, btnDateBirth ;
    LinearLayout llProfileShare , llProfileInvite ;
    CircleImageView profileImage ;

    public MyProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rView = inflater.inflate(R.layout.profile_account, container, false);

        btnClaimOffers = (Button)rView.findViewById(R.id.btnProfileClaimOffer);
        profileImage = (CircleImageView)rView.findViewById(R.id.profile_image);
        llProfileInvite = (LinearLayout)rView.findViewById(R.id.llProfileInvite);
        llProfileShare = (LinearLayout)rView.findViewById(R.id.llProfileShare);
        btnGender = (TrojanButton)rView.findViewById(R.id.ProfileGenderButton);
        btnDateBirth = (TrojanButton)rView.findViewById(R.id.ProfileDateBirthButton);
        tvGender = (TrojanText)rView.findViewById(R.id.ProfileGenderText);
        tvDateBirth = (TrojanText)rView.findViewById(R.id.ProfileDateBirthText);
        tvProfileName = (TrojanText)rView.findViewById(R.id.tvProfileName);
        tvProfileId = (TrojanText)rView.findViewById(R.id.tvProfileId);
        tvOfferNumbers = (TrojanText)rView.findViewById(R.id.tvProfileOfferNumbers);
        tvVoucherNumbers = (TrojanText)rView.findViewById(R.id.tvProfileVoucherNumbers);
        tvBadgeNumbers = (TrojanText)rView.findViewById(R.id.tvProfileBadgeNumbers);

        return rView;
    }

}
