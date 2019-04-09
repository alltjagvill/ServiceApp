package se.binninge.korp.serviceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import Model.Ads;

public class LookAtAdActivity extends AppCompatActivity {

    private String title;
    private String description;
    private String firstName;
    private String lastName;
    private String userID;
    private Double price;
    private String userName;

    private TextView titleTV;
    private TextView descriptionTV;
    private TextView userNameTV;
    private TextView priceTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_at_ad);

        Ads ad = (Ads) getIntent().getSerializableExtra("ADOBJECT");

        title = ad.getTitle();
        description = ad.getDescription();
        firstName = ad.getFirstName();
        lastName = ad.getLastName();
        userID = ad.getUserID();
        price = ad.getPrice();
        userName = firstName + " " + lastName;

        titleTV = findViewById(R.id.lookAtAdTitle);
        descriptionTV = findViewById(R.id.lookAtAdDescription);
        userNameTV = findViewById(R.id.lookAtAdName);
        priceTV = findViewById(R.id.lookAtAdPrice);

        titleTV.setText(title);
        descriptionTV.setText(description);
        userNameTV.setText(userName);
        priceTV.setText(price.toString());


    }
}
