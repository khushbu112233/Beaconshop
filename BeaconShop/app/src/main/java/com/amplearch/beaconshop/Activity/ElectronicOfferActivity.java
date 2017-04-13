package com.amplearch.beaconshop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.amplearch.beaconshop.Adapter.ElectOfferAdapter;
import com.amplearch.beaconshop.R;

import java.util.ArrayList;

public class ElectronicOfferActivity extends AppCompatActivity
{
    ListView listView_Elect ;
    ElectOfferAdapter  electOfferAdapter ;
    ArrayList<Integer> elect_Image = new ArrayList<Integer>();
    ArrayList<String> elect_Text = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic_offer);

        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("20% Discount on Samsung Mobile.");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("10% Discount on L.G. Mobile.");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("50% Discount on iPhone 7+ .");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("10% Discount on Asus Mobile.");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("20% Discount on Moto G5+ Mobile.");

        electOfferAdapter = new ElectOfferAdapter(getApplicationContext(), elect_Image, elect_Text);

        listView_Elect = (ListView)findViewById(R.id.listView_Elect);
        listView_Elect.setAdapter(electOfferAdapter);

    }
}
