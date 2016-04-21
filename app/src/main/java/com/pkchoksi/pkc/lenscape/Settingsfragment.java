package com.pkchoksi.pkc.lenscape;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseUser;

/**
 * Created by pkcho on 1/25/2016.
 */
public class Settingsfragment extends Fragment {
    private Button logout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        logout = (Button) v.findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(getActivity(), DispatchActivity.class));

            }
        });
        return v;
    }


    }

