package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pkcho on 3/14/2016.
 */
public class CustomSearchResultsAdaptor extends ParseQueryAdapter<Follow> {
    public CustomSearchResultsAdaptor(Context context, Class<? extends Follow> clazz, final ParseUser sName) {

        super(context, new ParseQueryAdapter.QueryFactory<Follow>() {

            @Override
            public ParseQuery<Follow> create() {
                ParseQuery query = ParseQuery.getQuery(Follow.class);
                ParseQuery userquery = ParseUser.getQuery();
                userquery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                query.whereMatchesQuery("from", userquery);
                query.whereEqualTo("to",sName);
                return query;
            }
        });
    }
    public View getItemView(final Follow object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.search_results, null);
        }
        super.getItemView(object, v, parent);
        if (object !=null){
            TextView followtext = (TextView)v.findViewById(R.id.searchResultsText);
            CircleImageView followImage = (CircleImageView)v.findViewById(R.id.profileviewSearch);
            ImageButton addbutton = (ImageButton)v.findViewById(R.id.addbutton);

            ParseUser name = object.getto();
            try {
                String setname =  name.fetchIfNeeded().getUsername();
                Log.d("Name: ", setname);
                followtext.setText(setname);
               // followImage.setImageBitmap(BitmapFactory.de);

            } catch (ParseException e) {
                Log.e("this: ", "Something has gone terribly wrong with Parse", e);
            }

        }




        return v;
    }
}

