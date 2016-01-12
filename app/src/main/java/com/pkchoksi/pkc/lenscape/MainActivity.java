package com.pkchoksi.pkc.lenscape;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CustomPicturesAdapter mainAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        setTitle("LensCape");

      listView = (ListView) findViewById(R.id.list);
       mainAdapter = new CustomPicturesAdapter(this, Pictures.class);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notes) {
            Intent intent1 = new Intent(this,NotesActivity.class);
            this.startActivity(intent1);
            return true;
        }

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Not Created Yet", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id == R.id.action_users){
            Intent intent2 = new Intent(this, ListUsersActivity.class);
            this.startActivity(intent2);
            return true;
        }
        if(id == R.id.action_logout){
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(MainActivity.this, DispatchActivity.class));
            return true;
        }
        if(id == R.id.action_profile){
            Intent intent3 = new Intent(this, ProfileActivity.class);
            startActivity(intent3);
        }

        return super.onOptionsItemSelected(item);
    }
    /*public void clickhandler(View view) {
        Intent notesintent = new Intent(this, NotesActivity.class);
        startActivity(notesintent);
    }
*/
   @Override
    public void onStart() {
        super.onStart();
        updatePicture();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pkchoksi.pkc.lenscape/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        updatePicture();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pkchoksi.pkc.lenscape/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    private void updatePicture() {
        mainAdapter.loadObjects();
        listView.setAdapter(mainAdapter);
    }


}
