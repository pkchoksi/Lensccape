package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by pkcho on 1/10/2016.
 */
public class CustomPicProfileAdapter extends ParseQueryAdapter<Pictures> {

    public CustomPicProfileAdapter(Context context, Class<? extends ParseObject> clazz, final String name) {

        super(context, new ParseQueryAdapter.QueryFactory<Pictures>() {
            public ParseQuery create() {
                ParseQuery query2 = ParseQuery.getQuery("pictures");
                ParseQuery query1 = ParseUser.getQuery();
                query1.whereEqualTo("objectId",name);

                Log.d(name, "this is the name");
                Log.d(ParseUser.getCurrentUser().toString(), "This is the name getting");

                query2.whereMatchesQuery("author",query1);
                return query2;
            }
        });
    }

    public View getItemView(Pictures object, View v, ViewGroup parent){
        if (v == null) {
            v = View.inflate(getContext(), R.layout.image_grid, null);
        }

        super.getItemView(object, v, parent);

        // Add and download the image
        ParseImageView mainFeedImage = (ParseImageView) v.findViewById(R.id.icon2);
        ParseFile imageFile = object.getPhotoFile();
        if (imageFile != null) {
            mainFeedImage.setParseFile(imageFile);
            mainFeedImage.loadInBackground();
        }

        // Add the title view


        // Add a reminder of how long this item has been outstanding
        return v;

    }
}
