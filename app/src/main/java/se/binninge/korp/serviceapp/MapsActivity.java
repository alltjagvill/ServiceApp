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
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import Model.Ads;
import Model.User;
import Utilites.CalcGeoPoints;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference adsRef;
    private List<Ads> adsList;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Double mapUserLat;
    private Double mapUserLon;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        adsRef = db.collection("ads");

        toolbar = findViewById(R.id.toolBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_maps);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.nearby_ads));



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_ads:
                        Intent ads = new Intent(MapsActivity.this, HomeActivity.class);
                        startActivity(ads);
                        break;

                    case R.id.action_maps:
                        Intent maps = new Intent(MapsActivity.this, MapsActivity.class);
                        startActivity(maps);
                        break;

                    case R.id.action_messages:
                        Intent messages = new Intent(MapsActivity.this, MessagesActivity.class);
                        startActivity(messages);
                        break;

                    case R.id.action_myads:
                        Intent myAds = new Intent(MapsActivity.this, MyAdsActivity.class);
                        startActivity(myAds);
                        break;
                }
                return true;
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.clear();
                // Add a marker for user location
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLatLng).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(userLatLng.latitude, userLatLng.longitude)));

                mapUserLat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                mapUserLon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                Log.d("CURRENTLOCATION", userLatLng.toString());
               // mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                Log.d("CURRENTLOCATION", userLatLng.toString());

                adsList = new ArrayList<>();
                adsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        adsList.clear();

                        int adPosition = 0;
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


                                    LatLng adLatLng = new LatLng(ad.getUserLat(), ad.getUserLon());
                                    mMap.addMarker(new MarkerOptions().position(adLatLng).title(ad.getTitle()).snippet(Integer.toString(adPosition)));
                                    //MarkerOptions marker = new MarkerOptions().position(ad.getUserLat(), ad.getUserLon())
                                    adPosition = adPosition +1;

                                }
                            } catch (NullPointerException e1) {
                                Log.d("NULLPOINT", e1.toString());
                            }
                        }


                    }
                });

            }

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


                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mapUserLat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                mapUserLon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

                mMap.clear();
                // Add a marker for user location
                LatLng userLatLng1 = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLatLng1).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLatLng1.latitude, userLatLng1.longitude), 10));
                Log.d("Lastknown", userLatLng1.toString());
             //  mMap.animateCamera(CameraUpdateFactory.zoomIn());
                Log.d("Lastknown", userLatLng1.toString());

            }
        }
        //Creating user
        user = new User(mapUserLat, mapUserLon);




        /*for (int i = 0; i < adsList.size(); i++) {

            Ads newAd = adsList[i];


        }
*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.app_bar_menu, menu);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }
            }
        }

    }


}
