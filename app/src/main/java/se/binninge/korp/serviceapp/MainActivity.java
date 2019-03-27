package se.binninge.korp.serviceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView emailView;
    TextView passwordView;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailView = findViewById(R.id.loginEmail);
        passwordView = findViewById(R.id.loginPassword);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
//auth.signOut();
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);

            startActivity(intent);
        }




    }



    public void submit(View view) {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (email.matches("") || password.matches("")) {
            String emptyField = getString(R.string.field_empty);
            Toast.makeText(this, emptyField, Toast.LENGTH_SHORT).show();
        }

        else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startHome();
                    } else {
                        Toast.makeText(MainActivity.this, "Login unsuccessful!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    void startHome() {

        Intent intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
    }



    public void startRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }



}
