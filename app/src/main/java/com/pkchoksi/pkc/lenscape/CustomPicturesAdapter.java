package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by pkcho on 1/10/2016.
 */
public class CustomPicturesAdapter extends ParseQueryAdapter<Pictures> {

    public CustomPicturesAdapter(Context context, Class<? extends Pictures> clazz) {
        super(context, new ParseQueryAdapter.QueryFactory<Pictures>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery(Pictures.class);
                return query;
            }
        });
    }

    public View getItemView(Pictures object, View v, ViewGroup parent){
        if (v == null) {
            v = View.inflate(getContext(), R.layout.cutom_adapter, null);
        }

        super.getItemView(object, v, parent);

        // Add and download the image
        ParseImageView mainFeedImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile imageFile = object.getPhotoFile();
        if (imageFile != null) {
            mainFeedImage.setParseFile(imageFile);
            mainFeedImage.loadInBackground();
        }

        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.text1);
        titleTextView.setText(object.getTitle().toString());

        // Add a reminder of how long this item has been outstanding
        TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
        timestampView.setText(object.getCreatedAt().toString());
        return v;

    }
}
