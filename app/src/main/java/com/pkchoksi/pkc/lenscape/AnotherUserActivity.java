package com.pkchoksi.pkc.lenscape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class AnotherUserActivity extends AppCompatActivity {

    private TextView userText;
    private GridView picGrid;
    private CustomPicProfileAdapter customAdapter;
    private String userid;
    private String username;
    private ImageButton followB;
    private Follow follow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        follow = new Follow();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userText = (TextView) findViewById(R.id.userName_text_another1);
        followB = (ImageButton) findViewById(R.id.followbutton2);
        Intent intent;
        intent = getIntent();
        picGrid = (GridView) findViewById(R.id.picgridView_another1);
        userid = intent.getStringExtra("username");
        final ParseQuery query = ParseUser.getQuery().whereEqualTo("objectId", userid);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, ParseException e) {
                if (e == null) {
                    username = user.get(0).getUsername();
                    userText.setText(username);
                } else {
                    // error
                }
            }
        });



        //UserId = query.get("objectId");
        customAdapter = new CustomPicProfileAdapter(this, Pictures.class, userid);
        picGrid.setAdapter(customAdapter);
        customAdapter.loadObjects();

        followB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final ParseQuery query = ParseUser.getQuery().whereEqualTo("objectId", userid);
               query.findInBackground(new FindCallback<ParseUser>() {
                   public void done(List<ParseUser> user, ParseException e) {
                       if(e == null && user != null){
                           final ParseUser name = user.get(0);

                           ParseQuery queryFollow = ParseQuery.getQuery("follow");
                           queryFollow.whereMatchesQuery("to",query);
                           queryFollow.findInBackground(new FindCallback<ParseObject>() {
                               @Override
                               public void done(List<ParseObject> objects, ParseException e) {
                                   if (objects.size() != 0 && e == null) {
                                       Log.d(objects.toString(),"This is the object");
                                       if ((objects.get(0).get("to") == name) && (objects.get(0).get("from") == ParseUser.getCurrentUser())) {

                                           Toast.makeText(getApplicationContext(), "You are already following: " + name.getUsername(), Toast.LENGTH_LONG).show();
                                       }
                                   }
                                   else {
                                       follow.setfrom(ParseUser.getCurrentUser());
                                       follow.setto(name);
                                       follow.saveInBackground();
                                       Toast.makeText(getApplicationContext(), "You are now following "+name.getUsername() , Toast.LENGTH_LONG).show();
                                   }
                               }
                           });

                           //ParseUser.getCurrentUser().put("friends", Arrays.asList(friends));

                       }
                       else {
                           Toast.makeText(getApplicationContext(),"Error saving the user",Toast.LENGTH_LONG).show();
                       }
                   }

               });


            }
        });


    }

    private void updatePicture() {
        customAdapter.loadObjects();
        picGrid.setAdapter(customAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // If a new post has been added, update
            // the list of posts
            updatePicture();
        }

    }





}
