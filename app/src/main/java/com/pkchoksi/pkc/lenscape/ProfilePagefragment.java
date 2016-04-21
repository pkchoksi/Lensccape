package com.pkchoksi.pkc.lenscape;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePagefragment extends Fragment {

    private TextView userText;
    private TextView followers;
    private TextView following;
    private ListView picGrid;
    private CircleImageView imagepro;
    private CustomProPicListAdapter customAdapter;
    private String Name;
    private ImageView logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.fragment_profile);
        //setTitle(ParseUser.getCurrentUser().getUsername().toUpperCase());




    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        Toolbar newtoolbar = (Toolbar) v.findViewById(R.id.toolbarFrag);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbarBottom);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        //FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.buttonCamera);

        Name = ParseUser.getCurrentUser().getObjectId();
        userText = (TextView) v.findViewById(R.id.userName_text);
        userText.setText(ParseUser.getCurrentUser().getUsername());
        picGrid = (ListView) v.findViewById(R.id.picgridView);
        customAdapter = new CustomProPicListAdapter(getActivity(),Pictures.class,Name);
        followers = (TextView) v.findViewById(R.id.followercon);
        following = (TextView) v.findViewById(R.id.followincon);
        ParseQuery query = ParseQuery.getQuery(Follow.class);
        imagepro = (CircleImageView)v.findViewById(R.id.proimageView);
        logo = (ImageView) newtoolbar.findViewById(R.id.logo_app);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        logo.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.logo_lescape, 100, 100));




        query.whereEqualTo("from", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.isEmpty() && e != null) {
                    Toast.makeText(getActivity(), "You need to follow more people", Toast.LENGTH_SHORT).show();
                } else {
                    int followingsize = objects.size();
                    StringBuilder sb = new StringBuilder();
                    sb.append(followingsize);

                    following.setText(sb.toString());
                }

            }


        });
        ParseQuery query2 = new ParseQuery(Follow.class);
        query2.whereEqualTo("to",ParseUser.getCurrentUser());
        query2.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.isEmpty() && e != null){
                    Toast.makeText(getActivity(),"You need to follow people",Toast.LENGTH_SHORT).show();
                }
                else {
                    int followersize = objects.size();
                    StringBuilder sb = new StringBuilder();
                    sb.append(followersize);
                    followers.setText(sb.toString());
                }
            }

        });


       ParseUser user = ParseUser.getCurrentUser();
        final ParseFile proimge = user.getParseFile("Images");
        if(proimge != null) {
            proimge.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null && data != null) {
                        Bitmap probitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                        imagepro.setImageBitmap(probitmap);
                    } else {
                        Toast.makeText(getActivity(), "Please set a profile Pic", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        //mainAdapter = new ParseQueryAdapter<Pictures>(this,Pictures.class);
        //mainAdapter = new ParseQueryAdapter<Pictures>(this, Pictures.class,R.layout.image_grid);
        //mainAdapter = new ParseQueryAdapter<Pictures>(getApplicationContext(),Pictures.class,R.layout.image_grid);
        // mainAdapter.setImageKey("photo");
        // mainAdapter.setTextKey("title");

        picGrid.setAdapter(customAdapter);
        setListViewHeightBasedOnChildren(picGrid);



        customAdapter.loadObjects();

        final RadioButton buttonprofile = (RadioButton) toolbar.findViewById(R.id.profilebutton);
        buttonprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonprofile.setBackgroundColor(getResources().getColor(R.color.off_white));
                ProfilePagefragment fragment6 = new ProfilePagefragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment6);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        final RadioButton buttonHome = (RadioButton) toolbar.findViewById(R.id.settingsButton);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHome.setBackgroundColor(getResources().getColor(R.color.off_white));
                HomePageFragment fragment6 = new HomePageFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment6);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        final RadioButton buttonCamera = (RadioButton) toolbar.findViewById(R.id.cameraButton);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCamera.setBackgroundColor(getResources().getColor(R.color.off_white));
                startActivityForResult(new Intent(getActivity(), NewPictureActivity.class), 0);
            }
        });
        final RadioButton buttonUsers = (RadioButton) toolbar.findViewById(R.id.contactsButton);
        buttonUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUsers.setBackgroundColor(getResources().getColor(R.color.off_white));
                ListUsersfragment fragment6 = new ListUsersfragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment6);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        final RadioButton groupsButton = (RadioButton) toolbar.findViewById(R.id.usersButton);
        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupsFragment fragment = new GroupsFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });





        //fab.setOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View v) {
                //startActivityForResult(new Intent(getActivity(), NewPictureActivity.class), 0);
           // }
       // });

        return v;
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
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromParse(byte[] data,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data,0,data.length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data,0,data.length, options);
    }
        public void getListViewSize(ListView myListView) {
            ListAdapter myListAdapter = myListView.getAdapter();
            if (myListAdapter == null) {
                //do nothing return null
                return;
            }
            //set listAdapter in loop for getting final size
            int totalHeight = 0;
            for (int size = 0; size < myListAdapter.getCount(); size++) {
                View listItem = myListAdapter.getView(size, null, myListView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            //setting listview item in adapter
            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
            myListView.setLayoutParams(params);
            // print height of adapter on log
            Log.i("height of listItem:", String.valueOf(totalHeight));
        }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


}
