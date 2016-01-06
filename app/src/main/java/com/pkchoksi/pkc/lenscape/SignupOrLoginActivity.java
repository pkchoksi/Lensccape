package com.pkchoksi.pkc.lenscape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by pkcho on 1/2/2016.
 */
public class SignupOrLoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_signuporlogin);
        ((Button) findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent of the log in activity
                startActivity(new Intent(SignupOrLoginActivity.this, LoginActivity.class));
            }
        });
        ((Button) findViewById(R.id.signup)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent for the sign up activity
                startActivity(new Intent(SignupOrLoginActivity.this, SignupActivity.class));
            }
        });
    }
}