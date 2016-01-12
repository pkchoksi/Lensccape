package com.pkchoksi.pkc.lenscape;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ListUsersActivity extends AppCompatActivity {

    private ListView userslistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        final ArrayList names = new ArrayList<String>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId",currentUserId);

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {
                        names.add(userList.get(i).getUsername().toString());
                    }
                    userslistView = (ListView) findViewById(R.id.usersListView);
                   ArrayAdapter namesArrayAdapter =
                            new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.user_list, names);
                    userslistView.setAdapter(namesArrayAdapter);
                    userslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                          openAnotherProfile(names,i);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openAnotherProfile(ArrayList<String> names, int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, ParseException e) {
                if (e == null) {
                    //start the messaging activity


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error finding that user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
