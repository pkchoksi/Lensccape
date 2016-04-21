package com.pkchoksi.pkc.lenscape;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ListUsersfragment extends Fragment {

    private ListView userslistView;
    private Toolbar toolbar;
    private Toolbar newtoolbar;
    private ImageView logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_listusers);




    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_listusers, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbarBottom);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        newtoolbar = (Toolbar) v.findViewById(R.id.toolbarFrag);
        logo = (ImageView) newtoolbar.findViewById(R.id.logo_app);

        logo.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.logo_lescape, 100, 100));





        //toolbar.setLogo(R.drawable.logo_lescape);

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




        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        final ArrayList names = new ArrayList<String>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", currentUserId);

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {
                        names.add(userList.get(i).getUsername());
                    }
                    userslistView = (ListView) v.findViewById(R.id.usersListView);
                    ArrayAdapter namesArrayAdapter =
                            new ArrayAdapter<String>(getActivity(),
                                    R.layout.user_list, names);
                    userslistView.setAdapter(namesArrayAdapter);
                    userslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                            openAnotherProfile(names, i);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    public void openAnotherProfile(ArrayList<String> names, int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, ParseException e) {
                if (e == null) {
                    //start the profile activity

                    Fragment_AnotherUser fragment6 = new Fragment_AnotherUser();
                    Bundle args = new Bundle();
                    args.putString("username",user.get(0).getObjectId());
                    fragment6.setArguments(args);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, fragment6);
                    ft.addToBackStack(null);
                    ft.commit();

                } else {
                    Toast.makeText(getActivity(),
                            "Error finding that user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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

}
