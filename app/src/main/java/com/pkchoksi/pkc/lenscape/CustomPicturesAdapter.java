package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.io.ByteArrayInputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pkcho on 1/10/2016.
 */
public class CustomPicturesAdapter extends ParseQueryAdapter<Pictures> {

    public CustomPicturesAdapter(Context context, Class<? extends Pictures> clazz) {
        super(context, new ParseQueryAdapter.QueryFactory<Pictures>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery(Pictures.class);
                ParseQuery query2 = ParseQuery.getQuery("follow");
                query2.whereEqualTo("from", ParseUser.getCurrentUser());
                query.whereMatchesKeyInQuery("author", "to", query2);

                return query;
            }
        });
    }

    public View getItemView(final Pictures object, View v, ViewGroup parent){
        if (v == null) {
            v = View.inflate(getContext(), R.layout.cutom_adapter, null);
        }

        super.getItemView(object, v, parent);
        Toolbar newtoolbar = (Toolbar) v.findViewById(R.id.toolbar_customeadapter);

        final CircleImageView imagepro = (CircleImageView) newtoolbar.findViewById(R.id.profileview);
        ParseQuery query3 = ParseUser.getQuery();
        try {
            String queryname = object.getAuthor().fetchIfNeeded().getObjectId();
            query3.whereEqualTo("objectId",queryname);
            query3.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size() != 0){
                   ParseFile imgfile = objects.get(0).getParseFile("Images");
                    if(imgfile != null){
                        try{
                            byte[] data = imgfile.getData();
                            Bitmap probitmap = decodeSampledBitmapFromParse(data, 50, 50);
                            imagepro.setImageBitmap(probitmap);
                        }catch (ParseException error){
                            error.printStackTrace();
                        }

                    }
                }
                }


            });
        }
        catch(Exception e) {

        }






        // Add and download the image
        final ParseImageView mainFeedImage = (ParseImageView) v.findViewById(R.id.icon2);

        final ParseFile imageFile = object.getPhotoFile();





        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {

                    final Bitmap imgprofile = BitmapFactory.decodeByteArray(data,0,data.length);
                   // if (imgprofile != null) {
                        //int nh = (int) (imgprofile.getHeight() * (512.0 / imgprofile.getWidth()));
                        //Bitmap scaled = Bitmap.createScaledBitmap(imgprofile, 512, nh, true);
                        //mainFeedImage.setVisibility(View.GONE);
                         //mainFeedImage.setVisibility(View.VISIBLE);
                        mainFeedImage.setImageBitmap(imgprofile);
                       mainFeedImage.loadInBackground();
                       mainFeedImage.destroyDrawingCache();

                    //}


                }
                else {
                    Toast.makeText(getContext(), "Not getting the image", Toast.LENGTH_SHORT).show();
                }
            }
        });


/*

        if (imageFile != null) {
          // mainFeedImage.setVisibility(View.GONE);
            //  mainFeedImage.setVisibility(View.VISIBLE);
            mainFeedImage.setImageBitmap(scaled);
            mainFeedImage.loadInBackground();

        }
*/
        // Add the title view
        TextView titleTextView = (TextView) newtoolbar.findViewById(R.id.text1);
        titleTextView.setText(object.getTitle().toString());

        ParseQuery query = ParseUser.getQuery();
        query.whereExists("username");
        ParseQuery query2 = ParseQuery.getQuery(Pictures.class);
        query2.whereMatchesQuery("author", query);

        TextView authorText = (TextView) newtoolbar.findViewById(R.id.Author);
        try {
           String name = object.getAuthor().fetchIfNeeded().getUsername();
            authorText.setText(name);

        } catch (ParseException e) {
            Log.v("Some tag", e.toString());
            e.printStackTrace();
        }



        // Add a reminder of how long this item has been outstanding
        TextView timestampView = (TextView) newtoolbar.findViewById(R.id.timestamp);
        timestampView.setText(object.getCreatedAt().toString());
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
    public static Bitmap decodeSampledBitmapFromParse(byte[] data,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
       BitmapFactory.decodeByteArray(data,0,data.length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data,0,data.length, options);
    }
    public void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId);
    }
    private Bitmap decodeFile(ParseFile f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new ByteArrayInputStream(f.getDataInBackground().getResult()), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new ByteArrayInputStream(f.getDataInBackground().getResult()), null, o2);
        } catch (NullPointerException e) {}
        return null;
    }
}
