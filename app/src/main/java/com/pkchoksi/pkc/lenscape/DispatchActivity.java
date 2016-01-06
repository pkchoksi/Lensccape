package com.pkchoksi.pkc.lenscape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

public class DispatchActivity extends AppCompatActivity {




    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

// Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, SignupOrLoginActivity.class));
        }



    }




}
