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
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import Adapter.AdsAdapter;
import Model.Ads;

public class MyAdsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference myAdsRef;
    private CollectionReference userColRef;
    private DocumentReference userDocRef;
    private String userID;
    private String currentUser;
    private float x1, x2, y1, y2;
    private List<Ads> myAdsList;
    private List<Ads> tempList;
    RecyclerView myAdsRecyclerView;
    private RecyclerView.Adapter myAdsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        myAdsRef = db.collection("ads");
        userColRef = db.collection("users");



        toolbar = findViewById(R.id.toolBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_myads);
        setSupportActionBar(toolbar);

        userID = auth.getUid();
        try {
            userDocRef = userColRef.document(userID);

        //Set title
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String userName = firstName + " " + lastName;
                    setTitle(getString(R.string.your_ads) + " - " + userName);
                    Log.d("!!!!!", userName);

                } else {
                    Toast.makeText(MyAdsActivity.this, getString(R.string.document_not_exists), Toast.LENGTH_SHORT).show();
                }    }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        } catch(NullPointerException userIDExeption) {
            Log.d("USERIDEXEPTION", userIDExeption.toString());
        }
        //END Set title

        myAdsRecyclerView = findViewById(R.id.myAdsRecyclerViewID);
        myAdsRecyclerView.setHasFixedSize(true);
        myAdsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tempList = new ArrayList<>();
        myAdsList = new ArrayList<>();



        Query query = myAdsRef.whereEqualTo("userID", userID);
    try {

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                myAdsList.clear();


                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {


                    Ads ad = snapshot.toObject(Ads.class);
                    myAdsList.add(ad);


                }

                myAdsAdapter.notifyDataSetChanged();
            }
        });
    }catch (NullPointerException e){
    Log.d("NullPointExeptionMyAds", e.toString());
    }

        myAdsAdapter = new AdsAdapter(this, myAdsList);
        myAdsRecyclerView.setAdapter(myAdsAdapter);





        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_ads:
                        Intent ads = new Intent(MyAdsActivity.this, HomeActivity.class);
                        startActivity(ads);
                        break;

                    case R.id.action_maps:
                        Intent maps = new Intent(MyAdsActivity.this, MapsActivity.class);
                        startActivity(maps);
                        break;

                    case R.id.action_messages:
                        Intent messages = new Intent(MyAdsActivity.this, MessagesActivity.class);
                        startActivity(messages);
                        break;

                    case R.id.action_myads:
                        Intent myAds = new Intent(MyAdsActivity.this, MyAdsActivity.class);
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
                if (x1 < x2) {
                    Intent nearbyAds = new Intent(MyAdsActivity.this, MessagesActivity.class);
                    startActivity(nearbyAds);
                }
                break;
        }
        return false;
    }

    public void addAd(View view) {
        Intent intent = new Intent(this, AdAdvertismentActivity.class);

        startActivity(intent);


    }
}
