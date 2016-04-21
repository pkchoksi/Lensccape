package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by pkcho on 3/14/2016.
 */
public class CustomFollowersAdaptor extends ParseQueryAdapter<Follow> {
    public CustomFollowersAdaptor(Context context,Class<? extends Follow> clazz) {

            super(context, new ParseQueryAdapter.QueryFactory<Follow>() {

                @Override
                public ParseQuery<Follow> create() {
                    ParseQuery query = ParseQuery.getQuery(Follow.class);
                    ParseQuery userquery = ParseUser.getQuery();
                    userquery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                    query.whereMatchesQuery("from", userquery);
                    return query;
                }
            });
        }
    public View getItemView(final Follow object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.user_list, null);
        }
        super.getItemView(object, v, parent);
        if (object !=null){
            TextView followtext = (TextView)v.findViewById(R.id.userListItem);
            ParseUser name = object.getto();
            try {
               String setname =  name.fetchIfNeeded().getUsername();
                followtext.setText(setname);
            } catch (ParseException e) {
                Log.e("this: ", "Something has gone terribly wrong with Parse", e);
            }

        }



        return v;
    }
    }

