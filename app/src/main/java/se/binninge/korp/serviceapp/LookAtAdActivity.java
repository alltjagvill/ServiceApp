package se.binninge.korp.serviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.Ads;

public class LookAtAdActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private Toolbar toolbar;
    private MenuItem searchItem;
    private BottomNavigationView bottomNavigationView;


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

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = auth.getUid();

        toolbar = findViewById(R.id.toolBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_ads);
        setSupportActionBar(toolbar);





        Ads ad = (Ads) getIntent().getSerializableExtra("ADOBJECT");

        title = ad.getTitle();
        description = ad.getDescription();
        firstName = ad.getFirstName();
        lastName = ad.getLastName();
        userID = ad.getUserID();
        price = ad.getPrice();
        userName = firstName + " " + lastName;

        setTitle(getString(R.string.ads) + " - " + userName);


        titleTV = findViewById(R.id.lookAtAdTitle);
        descriptionTV = findViewById(R.id.lookAtAdDescription);
        userNameTV = findViewById(R.id.lookAtAdName);
        priceTV = findViewById(R.id.lookAtAdPrice);

        titleTV.setText(title);
        descriptionTV.setText(description);
        userNameTV.setText(userName);
        priceTV.setText(price.toString());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_ads:
                        Intent ads = new Intent(LookAtAdActivity.this, HomeActivity.class);
                        startActivity(ads);
                        break;

                    case R.id.action_maps:
                        Intent maps = new Intent(LookAtAdActivity.this, MapsActivity.class);
                        startActivity(maps);
                        break;

                    case R.id.action_messages:
                        Intent messages = new Intent(LookAtAdActivity.this, MessagesActivity.class);
                        startActivity(messages);
                        break;

                    case R.id.action_myads:
                        Intent myAds = new Intent(LookAtAdActivity.this, MyAdsActivity.class);
                        startActivity(myAds);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();



        menuInflater.inflate(R.menu.app_bar_menu_no_searh, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {


            case R.id.action_settings:
                Toast.makeText(this, "Settings button selected", Toast.LENGTH_SHORT).show();
                return true;



            case R.id.action_logout:


                auth.signOut();
                Intent logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                return true;




            default: return super.onOptionsItemSelected(item);
        }


    }
}
