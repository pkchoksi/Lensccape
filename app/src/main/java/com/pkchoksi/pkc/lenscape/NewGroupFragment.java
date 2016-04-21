package com.pkchoksi.pkc.lenscape;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pkcho on 2/26/2016.
 */
public class NewGroupFragment extends Fragment {
    private SearchView searchfriends;
    private ListView userview;
    private EditText titleText;
    private CustomFollowersAdaptor mainAdaptor;
    private CustomSearchResultsAdaptor primaryAdaptor;
    private ArrayList<String> searchresults;
    private ArrayList<String> filteredsearchresults;
    private ArrayList<String> UserstobeAdded;
    private ArrayList<ParseUser> finalListUsers;
    private Button addUsers;
    private Button RefreshList;
    private Button SaveGroups;
    private Toolbar toolbar;
    private ArrayAdapter<String> stringAdapter;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_groups, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbarFrag);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        SaveGroups = (Button) v.findViewById(R.id.saveButtonGroups);
        searchresults = new ArrayList<String>();
        titleText = (EditText) v.findViewById(R.id.editText);
        filteredsearchresults = new ArrayList<String>();
        UserstobeAdded = new ArrayList<String>();
        userview = (ListView) v.findViewById(R.id.listView);
        RefreshList = (Button)v.findViewById(R.id.refreshList);
        RefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    UserstobeAdded = getArguments().getStringArrayList("userslist");
                }catch (Exception e){
                    Toast.makeText(getActivity(),"you must add users to groups first",Toast.LENGTH_SHORT).show();
                }

                stringAdapter = new ArrayAdapter<String>(getActivity(), R.layout.user_list, R.id.userListItem, UserstobeAdded);
                userview.setAdapter(stringAdapter);
            }
        });

        SaveGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(getArguments().getStringArrayList("userslist") != null && titleText.getText() != null){

                        ParseQuery query = ParseUser.getQuery();
                        UserstobeAdded = getArguments().getStringArrayList("userslist");


                        query.whereContainedIn("username", UserstobeAdded);
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(final List<ParseUser> objects, ParseException e) {
                                if (e == null && objects.size() != 0) {
                                    final ProgressDialog processDialog = new ProgressDialog(getActivity());
                                    processDialog.setTitle("Creating Group");
                                    processDialog.setMessage("Saving...");
                                    processDialog.show();
                                    ParseACL acl = new ParseACL();
                                    acl.setWriteAccess(ParseUser.getCurrentUser(), true);
                                    acl.setPublicReadAccess(true);
                                    ParseRole role = new ParseRole(titleText.getText().toString(), acl);
                                    role.put("Admin", ParseUser.getCurrentUser());
                                    role.put("GroupName", titleText.getText().toString());
                                    for (int i = 0; i < objects.size(); i++) {
                                        role.getUsers().add(objects.get(i));
                                    }

                                    role.saveInBackground(new SaveCallback() {

                                        @Override
                                        public void done(ParseException e) {
                                            processDialog.dismiss();
                                            if (e == null) {
                                                GroupsFragment fragment = new GroupsFragment();
                                                FragmentManager fm = getFragmentManager();
                                                FragmentTransaction ft = fm.beginTransaction();
                                                ft.replace(R.id.container, fragment);
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            } else {
                                                Toast.makeText(getActivity(), "Seems like internet connection is weak", Toast.LENGTH_SHORT).show();
                                                Log.d("This is the Message", e.getMessage());
                                            }
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(getActivity(), "You must add people to bee added to group",Toast.LENGTH_SHORT).show();
                                }
                            }

                        });


                    }
                } catch (Exception e){
                    Toast.makeText(getActivity(),"you must add users to groups and set a title",Toast.LENGTH_SHORT).show();
                }
            }
        });


        addUsers = (Button)v.findViewById(R.id.addUsers);
        addUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListGroupsMembersToAddFragment fragment = new ListGroupsMembersToAddFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //#####################################################
/*
        searchfriends.setQueryHint("Start searching to add user");

        searchfriends.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //gets called everytime user clicks on the search View
            }
        });
        searchfriends.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // primaryAdaptor.clear();

                return false;
            }
        });

        searchfriends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 1){
                    ParseQuery queryUsername = ParseUser.getQuery();
                    queryUsername.whereEqualTo("username", newText);
                    queryUsername.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null && objects.size() != 0) {
                                for (int i = 0; i < objects.size(); i ++){
                                    //primaryAdaptor = new CustomSearchResultsAdaptor(getActivity(),Follow.class,objects.get(i));
                                    //userview.setAdapter(primaryAdaptor);


                                }
                                //primaryAdaptor.loadObjects();
                            }
                        }
                    });
                    searchresults.add(newText);

                }
                else {

                }
                return false;
            }
        });
*/

        mainAdaptor = new CustomFollowersAdaptor(getActivity(),Follow.class);
        //userview.setAdapter(mainAdaptor);
        //mainAdaptor.loadObjects();

        return v;
    }
    public void filteredsearchresults(String newText){
        String sResults;
        filteredsearchresults.clear();
        for(int i = 0; i<searchresults.size(); i++){
            sResults = searchresults.get(i).toString();
            if (sResults.contains(newText)){
                filteredsearchresults.add(searchresults.get(i));
            }
        }
    }
    public ArrayList<String> FilteredArray(ArrayList<String> userobjectIds){
        final ArrayList<String> filtered = new ArrayList<String>();
        ParseQuery userq = ParseUser.getQuery();
       for(int i = 0; i< userobjectIds.size();i++){
           userq.whereContainsAll("objecId", userobjectIds);
           userq.findInBackground(new FindCallback<ParseUser>() {
               @Override
               public void done(List<ParseUser> objects, ParseException e) {
                   for(int i = 0; i <objects.size();i++){
                       filtered.add(objects.get(i).getUsername());
                   }
               }

           });
       }
        return filtered;
    }

}

