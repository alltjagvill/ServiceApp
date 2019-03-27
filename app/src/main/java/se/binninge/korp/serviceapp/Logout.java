package se.binninge.korp.serviceapp;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Logout {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public void logOut(View view) {
       auth.signOut();
    }

}
