package com.pkchoksi.pkc.lenscape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView userText;
    private GridView picGrid;
    private ParseQueryAdapter<Pictures> mainAdapter;
    private CustomPicProfileAdapter customAdapter;
    private String Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(ParseUser.getCurrentUser().getUsername().toUpperCase());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.buttonCamera);

        Name = ParseUser.getCurrentUser().getObjectId();
        userText = (TextView) findViewById(R.id.userName_text);
        userText.setText(ParseUser.getCurrentUser().getUsername());
        picGrid = (GridView) findViewById(R.id.picgridView);
        customAdapter = new CustomPicProfileAdapter(this,Pictures.class,Name);

        //mainAdapter = new ParseQueryAdapter<Pictures>(this,Pictures.class);
        //mainAdapter = new ParseQueryAdapter<Pictures>(this, Pictures.class,R.layout.image_grid);
        //mainAdapter = new ParseQueryAdapter<Pictures>(getApplicationContext(),Pictures.class,R.layout.image_grid);
       // mainAdapter.setImageKey("photo");
       // mainAdapter.setTextKey("title");
        picGrid.setAdapter(customAdapter);
        customAdapter.loadObjects();





     fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             startActivityForResult(new Intent(ProfileActivity.this, NewPictureActivity.class), 0);
         }
     });


    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_refresh: {
                updatePicture();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
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
