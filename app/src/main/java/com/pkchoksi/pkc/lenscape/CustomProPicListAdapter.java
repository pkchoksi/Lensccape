package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by pkcho on 1/10/2016.
 */
public class CustomProPicListAdapter extends ParseQueryAdapter<Pictures> {

    public CustomProPicListAdapter(Context context, Class<? extends ParseObject> clazz, final String name) {

        super(context, new ParseQueryAdapter.QueryFactory<Pictures>() {
            public ParseQuery create() {
                ParseQuery query2 = ParseQuery.getQuery("pictures");
                ParseQuery query1 = ParseUser.getQuery();
                query1.whereEqualTo("objectId",name);
                query2.whereMatchesQuery("author",query1);
                return query2;
            }
        });
    }

    public View getItemView(Pictures object, View v, ViewGroup parent){
        if (v == null) {
            v = View.inflate(getContext(), R.layout.custompropic_adapter, null);
        }

        super.getItemView(object, v, parent);

        // Add and download the image
        ParseImageView mainFeedImage = (ParseImageView) v.findViewById(R.id.image_grid);
        ParseFile imageFile = object.getPhotoFile();

        if (imageFile != null) {

            mainFeedImage.setParseFile(imageFile);
            mainFeedImage.loadInBackground();
            mainFeedImage.destroyDrawingCache();
        }

        // Add the title view
        TextView caption = (TextView)v.findViewById(R.id.captiontext);
        caption.setText(object.getTitle());


        // Add a reminder of how long this item has been outstanding
        return v;

    }
}
