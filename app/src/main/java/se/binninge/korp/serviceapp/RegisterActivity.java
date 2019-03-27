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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView emailText;
    TextView passwordText;
    TextView passwordConfirmText;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         auth = FirebaseAuth.getInstance();
         emailText = findViewById(R.id.registerEmail);
         passwordText = findViewById(R.id.registerPassword);
         passwordConfirmText = findViewById(R.id.registerConfirmPassword);
    }

    public void registerSubmit(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = passwordConfirmText.getText().toString();


        if (!confirmPassword.equals(password)) {

            String passwordMissmatch = getString(R.string.password_missmatch);
            Toast.makeText(this, passwordMissmatch, Toast.LENGTH_SHORT).show();
        }
        else {

            createAccount(email, password);

        }
    }
    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                goToHomeActivity();
            }
            else {
                Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }


    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
    }

    public void cancelRegister(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

}
