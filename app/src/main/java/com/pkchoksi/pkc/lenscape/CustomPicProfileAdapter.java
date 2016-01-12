package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by pkcho on 1/10/2016.
 */
public class CustomPicProfileAdapter extends ParseQueryAdapter<Pictures> {

    public CustomPicProfileAdapter(Context context, Class<? extends ParseObject> clazz, final String name) {
        super(context, new ParseQueryAdapter.QueryFactory<Pictures>() {
            public ParseQuery create() {
                ParseQuery query = ParseQuery.getQuery("pictures");
                query.whereEqualTo("author", name);
                return query;
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
