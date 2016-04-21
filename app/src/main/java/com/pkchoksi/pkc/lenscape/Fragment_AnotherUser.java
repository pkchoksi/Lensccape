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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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

public class Fragment_AnotherUser extends Fragment {

    private TextView userText;
    private GridView picGrid;
    private CustomPicProfileAdapter customAdapter;
    private String userid;
    private String username;
    private Button followB;
    private Follow follow;
    private CircleImageView imagepro;
    private ImageView logo;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_anotheruser, container, false);
        follow = new Follow();
        Toolbar toptoolbar = (Toolbar) v.findViewById(R.id.toolbarFrag);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbarBottom);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        logo = (ImageView) toptoolbar.findViewById(R.id.logo_app);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        logo.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.logo_lescape, 100, 100));

        imagepro = (CircleImageView)v.findViewById(R.id.imageviewAnother1);

        final RadioButton buttonprofile = (RadioButton) toolbar.findViewById(R.id.profilebutton);
        buttonprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Eat this", Toast.LENGTH_SHORT).show();
                buttonprofile.setBackgroundColor(getResources().getColor(R.color.off_white));
                ProfilePagefragment fragment6 = new ProfilePagefragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment6);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        final RadioButton buttonSettings = (RadioButton) toolbar.findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSettings.setBackgroundColor(getResources().getColor(R.color.off_white));
                Settingsfragment fragment6 = new Settingsfragment();
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
        buttonUsers.setBackgroundColor(getResources().getColor(R.color.off_white));
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
        final RadioButton homebutton = (RadioButton) toolbar.findViewById(R.id.usersButton);
        homebutton.setOnClickListener(new View.OnClickListener() {
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


        userText = (TextView) v.findViewById(R.id.userName_text_another1);
        followB = (Button) v.findViewById(R.id.followbutton2);

        picGrid = (GridView) v.findViewById(R.id.picgridView_another1);
        userid  = getArguments().getString("username");
        final ParseQuery query = ParseUser.getQuery().whereEqualTo("objectId", userid);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, ParseException e) {
                if (e == null) {
                    username = user.get(0).getUsername();
                    userText.setText(username);
                    getActivity().setTitle(user.get(0).getUsername());
                    ParseQuery query1 = ParseQuery.getQuery(Follow.class);
                    query1.whereEqualTo("from", ParseUser.getCurrentUser());
                    query1.whereMatchesQuery("to", query);
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null && objects.size() != 0) {
                                if (objects.get(0).getBoolean("isFriends") == true) {
                                    followB.setText("Following");
                                } else {
                                    Log.d(follow.getisFriends().toString(), "this is the value getting");
                                }
                            }
                        }
                    });
                } else {
                    //nothing needs to go here
                }
            }
        });


        //UserId = query.get("objectId");
        customAdapter = new CustomPicProfileAdapter(getActivity(), Pictures.class, userid);
        picGrid.setAdapter(customAdapter);
        customAdapter.loadObjects();
        ParseQuery user = ParseUser.getQuery();
        user.whereEqualTo("objectId", userid);
        user.findInBackground(new FindCallback<ParseUser>() {

            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size()!= 0){
                final ParseFile proimge = objects.get(0).getParseFile("Images");
                if(proimge != null) {
                    proimge.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data != null) {
                                Bitmap probitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                imagepro.setImageBitmap(probitmap);
                            } else {
                                Toast.makeText(getActivity(),"this person needs a profile picture",Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }}

        });


        followB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final ParseQuery query = ParseUser.getQuery().whereEqualTo("objectId", userid);
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> user, ParseException e) {
                        if (e == null && user != null) {
                            final ParseUser name = user.get(0);

                            //ParseQuery queryFollow = ParseQuery.getQuery("follow");
                            ParseQuery qeryfollow2 = ParseQuery.getQuery(Follow.class);

                            qeryfollow2.whereMatchesQuery("to", query);
                            qeryfollow2.whereEqualTo("from", ParseUser.getCurrentUser());
                            qeryfollow2.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (objects.size() != 0 && e == null) {
                                        Log.d(objects.toString(), "This is the object");
                                        if (objects.get(0).getBoolean("isFriends")) {

                                            Log.d(objects.get(0).toString(), "done: ");
                                            followB.setText("Follow");
                                            Toast.makeText(getActivity(), "You are now unfollowing: " + name.getUsername(), Toast.LENGTH_SHORT).show();
                                            objects.get(0).deleteInBackground();
                                        } else {

                                            follow.setfrom(ParseUser.getCurrentUser());
                                            follow.setto(name);
                                            follow.setisFriends(true);
                                            followB.setText("following");
                                            follow.saveEventually();
                                            Toast.makeText(getActivity(), "You are now following " + name.getUsername(), Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Log.d(follow.getisFriends().toString(), "truth if friendds: ");
                                        follow.setfrom(ParseUser.getCurrentUser());
                                        follow.setto(name);
                                        followB.setText("Following");
                                        follow.setisFriends(true);
                                        follow.saveEventually();
                                        Toast.makeText(getActivity(), "You are now following " + name.getUsername(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            //ParseUser.getCurrentUser().put("friends", Arrays.asList(friends));

                        } else {
                            Toast.makeText(getActivity(), "Error saving the user", Toast.LENGTH_LONG).show();
                        }
                    }

                });


            }
        });
        return v;
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