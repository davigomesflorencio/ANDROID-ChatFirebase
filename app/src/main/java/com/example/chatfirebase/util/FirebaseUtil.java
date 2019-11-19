package com.example.chatfirebase.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {

    private static DatabaseReference firebase;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;

    public FirebaseUtil() {
        firebase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public DatabaseReference getFirebase() {
        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return (firebase);
    }

    public FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth;
    }

    public FirebaseAuth.AuthStateListener getAuthStateListener() {
        return authStateListener;
    }

    public void setAuthStateListener(FirebaseAuth.AuthStateListener authStateListener) {
        this.authStateListener = authStateListener;
    }

    private String getUid() {
        return firebaseAuth.getUid();
    }

}
