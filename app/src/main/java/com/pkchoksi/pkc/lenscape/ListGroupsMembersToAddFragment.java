package com.pkchoksi.pkc.lenscape;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by pkcho on 3/22/2016.
 */
public class ListGroupsMembersToAddFragment extends Fragment {
    private ListView userslistView;
    private CustomFollowersAdaptor mainAdapter;
    private Toolbar toolbar;
    private  int count=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_listusers);




    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_list_group_users_toadd, container, false);
        userslistView = (ListView)v.findViewById(R.id.groupUsersListView);
        toolbar = (Toolbar) v.findViewById(R.id.toolbarFrag);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        final ArrayList names = new ArrayList<String>();
        mainAdapter = new CustomFollowersAdaptor(getActivity(),Follow.class);
        userslistView.setAdapter(mainAdapter);
        userslistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        userslistView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                names.add(mainAdapter.getItem(position).getto().getUsername());
                count = count + 1;
                mode.setTitle(count + " Item Selected");

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_addgroupusers, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_all:
                        userslistView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
                        NewGroupFragment fragment6 = new NewGroupFragment();
                        Bundle args = new Bundle();
                        args.putStringArrayList("userslist", names);
                        fragment6.setArguments(args);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.container, fragment6);
                        ft.addToBackStack(null);
                        ft.commit();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                count = 0;
            }

        });

        return v;
    }

}
