package se.binninge.korp.serviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import Model.Ads;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db;
    private String userId;
    CollectionReference adsRef;
    private List<Ads> adsList;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

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

        //Setting up Recycler view
        adsRecyclerView = findViewById(R.id.adsRecyclerViewID);
        adsRecyclerView.setHasFixedSize(true);
        adsRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        adsList = new ArrayList<>();
       adsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
          adsList.clear();

          for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
              Ads ad = snapshot.toObject(Ads.class);
              adsList.add(ad);
          }
               Log.d("!!!","size:" + adsList.size());

               for(Ads ad : adsList) {
                   Log.d("!!!", "User: " + ad.getFirstName() + " " + ad.getLastName());
                   Log.d("!!!", "Title: " + ad.getTitle());
                   Log.d("!!!","Description" + ad.getDescription() );
               }
               adapter.notifyDataSetChanged();
           }
       });

        adapter = new AdsAdapter(this, adsList);
        adsRecyclerView.setAdapter(adapter);

        /* adsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    adsList = new ArrayList<>();
                    Ads ad = documentSnapshot.toObject(Ads.class);

                    adsList.add(ad);
                }
            }
        });*/



        /*adsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (queryDocumentSnapshots doc)
                adsList = new ArrayList<>();


                adapter.notifyDataSetChanged();


            }
        });*/

        /*Ads ad1 = new Ads("Haircutting", "I'm cutting hair. Cheap.", "Korp", 300);

        adsList = new ArrayList<>();

        adsList.add(ad1);*/




        /*for (int i = 0; i < 10; i++) {
            Ads ad3 = new Ads("Test" + (i+1), "Testing this out", "Korp", 100) {

            };

            adsList.add(ad3);
        }*/


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
