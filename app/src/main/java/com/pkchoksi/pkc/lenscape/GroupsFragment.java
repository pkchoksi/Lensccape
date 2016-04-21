package com.pkchoksi.pkc.lenscape;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by pkcho on 2/13/2016.
 */
public class GroupsFragment extends Fragment {
private Button addgroups;
private CustomGroupsAdapter mainAdapter;
private Toolbar newtoolbar;
private ImageView logo;
private ArrayList<String> userarray = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbarBottom);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        newtoolbar = (Toolbar) v.findViewById(R.id.toolbarFrag);
        logo = (ImageView) newtoolbar.findViewById(R.id.logo_app);
        logo.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.logo_lescape,100,100));

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
        final RadioButton buttonSettings = (RadioButton) toolbar.findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSettings.setBackgroundColor(getResources().getColor(R.color.off_white));
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
                startActivityForResult(new Intent(getActivity(), NewCameraActivity.class), 0);
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
        final RadioButton homebutton = (RadioButton) toolbar.findViewById(R.id.usersButton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homebutton.setBackgroundColor(Color.WHITE);
                GroupsFragment fragment = new GroupsFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });



        addgroups = (Button) v.findViewById(R.id.addgroupbutton);
        addgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseObject obj= new ParseObject("Groups");

                ParseQuery query = ParseUser.getQuery();
                query.whereExists("objectId");
                query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
               /*
                query.findInBackground(new FindCallback<ParseUser>() {

                    public void done(final List<ParseUser> objects, ParseException e) {
                        if (e == null && objects.size() != 0) {
                            ParseQuery querynew = ParseRole.getQuery();
                            querynew.whereEqualTo("name", "modertor");
                            querynew.findInBackground(new FindCallback<ParseRole>() {
                                @Override
                                public void done(List<ParseRole> roleobjects, ParseException e) {

                                        ParseRole newrole = roleobjects.get(0);
                                        ParseACL role = new ParseACL();
                                        role.setWriteAccess(ParseUser.getCurrentUser(),true);
                                        ParseRole adminrole = new ParseRole("Admin",role);
                                        newrole.getRoles().add(adminrole);

                                        //newrole.getUsers().add(objects.get(3));
                                        newrole.saveInBackground();


                                    Toast.makeText(getActivity(), "This is the role " + roleobjects.get(0).getName(), Toast.LENGTH_SHORT).show();
                                }


                            });
                            //obj.put("Admin", ParseUser.getCurrentUser());
                            //obj.addAllUnique("Members", userarray);
                            //obj.saveEventually();
                        } else {
                            Toast.makeText(getActivity(), "Did not get results", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
                */
                Toast.makeText(getActivity(),"This is the size of the array "+userarray.size(),Toast.LENGTH_SHORT ).show();

                ParseACL roleACL = new ParseACL();
                roleACL.setPublicReadAccess(true);

                userarray.clear();

                NewGroupFragment fragment = new NewGroupFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        ListView groupsList = (ListView) v.findViewById(R.id.groupslist);
        mainAdapter = new CustomGroupsAdapter(getActivity(),ParseUser.getCurrentUser());
        groupsList.setAdapter(mainAdapter);
        mainAdapter.loadObjects();
        groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"This will take you to the groups",Toast.LENGTH_SHORT).show();
            }
        });
        return v;
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
