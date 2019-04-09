package se.binninge.korp.serviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class AdAdvertismentActivity extends AppCompatActivity {

    private TextView adAddTitleTextView;
    private TextView adAddDescriptionTextView;
    private TextView adAddPriceTextView;

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

        adButton = findViewById(R.id.createAd);

        adAddTitleTextView = findViewById(R.id.addAdTitle);
        adAddDescriptionTextView = findViewById(R.id.adAddDescription);
        adAddPriceTextView = findViewById(R.id.adAddPrice);
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
}
