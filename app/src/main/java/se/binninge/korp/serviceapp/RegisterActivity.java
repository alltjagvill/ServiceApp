package se.binninge.korp.serviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView emailText;
    TextView firstNameText;
    TextView lastNameText;
    TextView passwordText;
    TextView passwordConfirmText;

    FirebaseAuth auth;
    FirebaseFirestore db;
    CollectionReference usersCollection;
    String userID;
    DocumentReference userDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         auth = FirebaseAuth.getInstance();
         db = FirebaseFirestore.getInstance();
         usersCollection = db.collection("users");

         emailText = findViewById(R.id.registerEmail);
         firstNameText = findViewById(R.id.registerFirstName);
         lastNameText = findViewById(R.id.registerLastName);
         passwordText = findViewById(R.id.registerPassword);
         passwordConfirmText = findViewById(R.id.registerConfirmPassword);
    }

    public void registerSubmit(View view) {
        String email = emailText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = passwordConfirmText.getText().toString();


        if (!confirmPassword.equals(password) || firstName.equals("") || lastName.equals("") || email.equals("")) {

            if (!confirmPassword.equals(password) && !firstName.equals("") && !lastName.equals("") && !email.equals("")) {


                String passwordMissmatch = getString(R.string.password_missmatch);
                Toast.makeText(this, passwordMissmatch, Toast.LENGTH_SHORT).show();
            }



            else  {

                String emptyField = getString(R.string.field_empty);
                Toast.makeText(this, emptyField, Toast.LENGTH_SHORT).show();

            }
        }

        else {

            createAccount(email, password, firstName, lastName);

        }
    }
    private void createAccount(String email, String password, final String firstName, final String lastName) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {

                userID = auth.getUid();
                userDoc = usersCollection.document(userID);

                userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                if (documentSnapshot.exists()) {
                                    Log.d("!!!", getString(R.string.log_user_doc_exists));
                                    goToHomeActivity();
                                } else {

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("userID", userID);
                                    user.put("firstName", firstName);
                                    user.put("lastName", lastName);


                                    userDoc.set(user);

                                    goToHomeActivity();

                                }
                            }
                            catch (NullPointerException e) {
                                Log.d("!!!", e.toString());
                            }

                        }
                        else{

                                Toast.makeText(RegisterActivity.this, getString(R.string.failed_with) + task.getException(), Toast.LENGTH_SHORT).show();
                            }


                    }


                });

            }
            else {
                Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }


    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);

        /*intent.putExtra("FIRSTNAME", firstName);
        intent.putExtra("LASTNAME", lastName);
*/
        startActivity(intent);
    }

    public void cancelRegister(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

}
