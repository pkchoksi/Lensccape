package com.pkchoksi.pkc.lenscape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AnotherUserActivity extends AppCompatActivity {

    private TextView userText;
    private GridView picGrid;
    private CustomPicProfileAdapter customAdapter;
    private String userid;
    private String username;
    private ImageButton followB;
    private ArrayList<ParseObject> friends;

    public static ParseObject follow = new ParseObject("follow");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userText = (TextView) findViewById(R.id.userName_text_another1);
        followB = (ImageButton) findViewById(R.id.followbutton);
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
        friends = new ArrayList<ParseObject>();


        //UserId = query.get("objectId");
        customAdapter = new CustomPicProfileAdapter(this, Pictures.class, userid);
        picGrid.setAdapter(customAdapter);
        customAdapter.loadObjects();

        followB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final ParseQuery<Follow> query2 = ParseQuery.getQuery(Follow.class);

                final ParseQuery query2 = ParseUser.getQuery();
                //final Follow follow = new Follow();
                //follow.setfrom(ParseUser.getCurrentUser());
                //ParseQuery queryuser = ParseUser.getQuery();
                //queryuser.whereEqualTo("objectId", userid);
                //queryuser.findInBackground(new FindCallback<ParseUser>() {
                //@Override
                // public void done(List<ParseUser> user, ParseException e) {
                //    follow.setto(user.get(0));


                follow.put("from", ParseUser.getCurrentUser());
                query2.whereEqualTo("objectId",userid);
                query2.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        follow.put("to", objects.get(0));
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



    public void isFreinds(ParseUser user1, ParseUser user2, Boolean isTrue){

    }
}
