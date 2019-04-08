package se.binninge.korp.serviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdAdvertismentActivity extends AppCompatActivity {

    private TextView adAddTitleTextView;
    private TextView adAddDescriptionTextView;
    private TextView adAddPriceTextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_advertisment);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = auth.getUid();

        ads = db.collection("ads");
        user = db.collection("users");
        userDoc = user.document(userID);

        Log.d("!!!", userID);
        Log.d("!!!", user.toString());
        Log.d("!!!", userDoc.toString());




        adAddTitleTextView = findViewById(R.id.addAdTitle);
        adAddDescriptionTextView = findViewById(R.id.adAddDescription);
        adAddPriceTextView = findViewById(R.id.adAddPrice);



    }


    public void createAd(View view) {

        title = adAddTitleTextView.getText().toString();
        description = adAddDescriptionTextView.getText().toString();
        //String priceText = adAddPriceTextView.getText().toString();
        price = Double.parseDouble(adAddPriceTextView.getText().toString());

        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    firstName = documentSnapshot.getString("firstname");
                    lastName = documentSnapshot.getString("lastname");
                    Log.d("!!!", firstName);
                    Log.d("!!!", lastName);
                } else {
                    Toast.makeText(AdAdvertismentActivity.this, getString(R.string.document_not_exists), Toast.LENGTH_SHORT).show();
                }
            }




        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        Map<String,  Object> createAd = new HashMap<>();
        createAd.put("title", title);
        createAd.put("description", description);
        createAd.put("firstName", firstName);
        createAd.put("lastName", lastName);
        createAd.put("price", price);

        


    }


    public void cancelCreateAdd(View view) {

        Intent intent = new Intent(this, MyAdsActivity.class);
        startActivity(intent);
    }
}
