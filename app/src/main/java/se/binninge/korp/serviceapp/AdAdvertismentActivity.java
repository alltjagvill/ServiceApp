package se.binninge.korp.serviceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class AdAdvertismentActivity extends AppCompatActivity {

    private TextView adAddTitleTextView;
    private TextView adAddDescriptionTextView;
    private TextView adAddPriceTextView;
    private TextView adAddUsernameTextview;

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private Button adButton;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference ads;
    private CollectionReference user;
    private DocumentReference userDoc;

    private String title;
    private String description;
    private String userID;
    private String firstName;
    private String lastName;
    private Double price;
    private String priceText;
    private String userName;

    private LocationManager locationManager;

    private Double userLocationGeoLat;
    private Double getUserLocationGeoLon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_advertisment);

        setTitle(getString(R.string.your_ads ) + " - " + getString(R.string.create_ad));
        toolbar = findViewById(R.id.toolBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_myads);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = auth.getUid();

        ads = db.collection("ads");
        user = db.collection("users");
        userDoc = user.document(userID);

        adButton = findViewById(R.id.createAdButton);

        adAddTitleTextView = findViewById(R.id.addAdTitle);
        adAddDescriptionTextView = findViewById(R.id.adAddDescription);
        adAddPriceTextView = findViewById(R.id.adAddPrice);
        adAddUsernameTextview = findViewById(R.id.adAddUsername);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        userLocationGeoLat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        getUserLocationGeoLon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();


        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {



                    firstName = documentSnapshot.getString("firstName");
                    lastName = documentSnapshot.getString("lastName");

                    userName = firstName + " " + lastName;
                    adAddUsernameTextview.setText(userName);
                    Log.d("!!!!!", userName);



                } else {
                    Toast.makeText(AdAdvertismentActivity.this, getString(R.string.document_not_exists), Toast.LENGTH_SHORT).show();
                }

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


       // Log.d("!!!!!!!!", userName);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_ads:
                        Intent ads = new Intent(AdAdvertismentActivity.this, HomeActivity.class);
                        startActivity(ads);
                        break;

                    case R.id.action_maps:
                        Intent maps = new Intent(AdAdvertismentActivity.this, MapsActivity.class);
                        startActivity(maps);
                        break;

                    case R.id.action_messages:
                        Intent messages = new Intent(AdAdvertismentActivity.this, MessagesActivity.class);
                        startActivity(messages);
                        break;

                    case R.id.action_myads:
                        Intent myAds = new Intent(AdAdvertismentActivity.this, MyAdsActivity.class);
                        startActivity(myAds);
                        break;
                }
                return true;
            }
        });




    }


    public void createAd(View view) {

        title = adAddTitleTextView.getText().toString();
        description = adAddDescriptionTextView.getText().toString();
        priceText = adAddPriceTextView.getText().toString();

        if (title.equals("") || description.equals("") || priceText.equals("")) {

            Toast.makeText(this, getString(R.string.field_empty), Toast.LENGTH_SHORT).show();

        } else {

            userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.exists()) {

                        price = Double.parseDouble(adAddPriceTextView.getText().toString());
                        adButton.setEnabled(false);

                        firstName = documentSnapshot.getString("firstName");
                        lastName = documentSnapshot.getString("lastName");

                        Map<String, Object> createAd = new HashMap<>();
                        createAd.put("title", title);
                        createAd.put("description", description);
                        createAd.put("firstName", firstName);
                        createAd.put("lastName", lastName);
                        createAd.put("price", price);
                        createAd.put("userID", userID);
                        createAd.put("userLat", userLocationGeoLat);
                        createAd.put("userLon", getUserLocationGeoLon);
                        //createAd.put("userGeo", userLocationGeo);

                        ads.add(createAd);


                    } else {
                        Toast.makeText(AdAdvertismentActivity.this, getString(R.string.document_not_exists), Toast.LENGTH_SHORT).show();
                    }

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });




                Intent intent = new Intent(this, MyAdsActivity.class);


                startActivity(intent);


        }
    }


    public void cancelCreateAdd(View view) {

        Intent intent = new Intent(this, MyAdsActivity.class);
        startActivity(intent);
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
            case R.id.action_search:
                Toast.makeText(this, "Search button selected", Toast.LENGTH_SHORT).show();

                return true;

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
