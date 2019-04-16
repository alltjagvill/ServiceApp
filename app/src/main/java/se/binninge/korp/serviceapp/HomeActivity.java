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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.Toast;
import Adapter.AdsAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import Model.Ads;
import Model.User;
import Utilites.CalcGeoPoints;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userId;
    private CollectionReference adsRef;
    private List<Ads> adsList;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;


    private LocationManager locationManager;
    private LocationListener locationListener;
    private Double userLat;
    private Double userLong;
    private LatLng userLatLng;
    private Double createUserLat;
    private Double createUserLon;

    private User user;



    float x1, x2, y1, y2;

   // private List<Ads> adsList;
    private RecyclerView adsRecyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Setting up firestore connection
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getUid();
        adsRef = db.collection("ads");




        //Setting up toolbars
        toolbar = findViewById(R.id.toolBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_ads);
        setSupportActionBar(toolbar);

       //Setting title
        setTitle(getString(R.string.ads));




        //Setting up Recycler view
        adsRecyclerView = findViewById(R.id.adsRecyclerViewID);
        adsRecyclerView.setHasFixedSize(true);
        adsRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Setting up location listener and updating Geo location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT < 23)  {

            createUserLat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            createUserLon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();


        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Ask for it
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //we have permission
                createUserLat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                createUserLon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

            }
        }


        //Creating user
        user = new User(createUserLat, createUserLon);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("Location", location.toString());
                final GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                userLat = location.getLatitude();
                userLong = location.getLongitude();
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                user.setLat(userLat);
                user.setLon(userLong);

                Log.d("UserLocation lat: ", Double.toString(userLat));
                Log.d("UserLocation long: ", Double.toString(userLong));
                //String userLocationString = userLocation.toString();
                Log.d("Userlocation", userLocation.toString());

               // Query queryGpsUpdate = adsRef.whereEqualTo("userID", userId);

                adsRef.whereEqualTo("userID", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                 Map<Object, Double> userGeoLat = new HashMap<>();
                                userGeoLat.put("userLat", userLat);
                                adsRef.document(document.getId()).set(userGeoLat, SetOptions.merge());

                                Map<Object, Double> userGeoLon = new HashMap<>();
                                userGeoLon.put("userLon", userLong);
                                adsRef.document(document.getId()).set(userGeoLon, SetOptions.merge());
                                Log.d("!!!", userId);
                            }
                            } else {
                            Log.d("!!!", "No response");
                        }
                        }
                });
            }
            // Setting up location listener and updating Geo location


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };



        adsList = new ArrayList<>();
        adsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                adsList.clear();

                CalcGeoPoints calcGeoPoints = new CalcGeoPoints();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                    try {
                        Ads ad = snapshot.toObject(Ads.class);

                        Double adLat = ad.getUserLat();
                        Double adLong = ad.getUserLon();
                        Double userLat = user.getLat();
                        Double userLon = user.getLon();

                        Log.d("!!!!!TheAd", ad.getTitle());
                        Log.d("!!!!!AdName", ad.getFirstName() + " " + ad.getLastName());
                        Log.d("!!!!!adLocation lat", Double.toString(adLat));
                        Log.d("!!!!!adLocation long", Double.toString(adLong));
                        Log.d("!!!!!userLocation lat", user.getLat().toString());
                        Log.d("!!!!!userLocation long", user.getLon().toString());



                    float geoCalc = calcGeoPoints.withinDistance(userLat, userLon, adLat, adLong);
                    Log.d("!!!!!GEOCALC", Float.toString(geoCalc));


                        //Log.d("geoLocation", Double.toString(ad.getUserGeo().getLatitude()));

                         if ( geoCalc < 100000) {
                        adsList.add(ad);

                        }
                    } catch (NullPointerException e1) {
                        Log.d("NULLPOINT", e1.toString());
                    }
                    }

                adapter.notifyDataSetChanged();
            }
        });

        adapter = new AdsAdapter(this, adsList);
        adsRecyclerView.setAdapter(adapter);








        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_ads:
                        Intent ads = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(ads);
                        break;

                    case R.id.action_maps:
                        Intent maps = new Intent(HomeActivity.this, MapsActivity.class);
                        startActivity(maps);
                        break;

                    case R.id.action_messages:
                        Intent messages = new Intent(HomeActivity.this, MessagesActivity.class);
                        startActivity(messages);
                        break;

                    case R.id.action_myads:
                        Intent myAds = new Intent(HomeActivity.this, MyAdsActivity.class);
                        startActivity(myAds);
                        break;
                }
                return true;
            }
        });


        //Checking location permission

        if (Build.VERSION.SDK_INT < 23)  {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Ask for it
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //we have permission
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            }
        }

    }





    //Methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.app_bar_menu, menu);

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
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

    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getX();
                break;

            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if (x1 > x2) {
                    Intent nearbyAds = new Intent(HomeActivity.this, MapsActivity.class);
                    startActivity(nearbyAds);
                }
                break;
        }
        return false;
    }
}
