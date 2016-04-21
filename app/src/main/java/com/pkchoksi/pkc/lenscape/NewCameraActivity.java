package com.pkchoksi.pkc.lenscape;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by pkcho on 3/29/2016.
 */
public class NewCameraActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Point mSize;
    private Pictures pictures;
    private EditText titleText;
    private Button Done;
    private Intent newdata;
    private Button SaveProfile;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcamera);
        Toolbar toolbar = (Toolbar)findViewById(R.id.include);
        titleText = (EditText)findViewById(R.id.pictureTitle);


        Done = (Button)findViewById(R.id.Done);
        SaveProfile = (Button)findViewById(R.id.buttonProfile);
        logo = (ImageView) toolbar.findViewById(R.id.logo_app);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        logo.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.logo_lescape, 100, 100));







        Display display = getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pictures = new Pictures();
        titleText = (EditText)findViewById(R.id.pictureTitle);
        Done = (Button)findViewById(R.id.Done);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            final Uri photoUri = data.getData();
            ParseUser user = ParseUser.getCurrentUser();
            // Get the bitmap in according to the width of the device
            final Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
            ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
            SaveProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(photoUri != null){
                            final ProgressDialog dlg = new ProgressDialog(NewCameraActivity.this);
                            dlg.setTitle("Please wait.");
                            dlg.setMessage("Saving Picture.  Please wait.");
                            dlg.show();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            ParseFile photofile = new ParseFile("Picture.jpg", byteArray);
                            ParseUser curruser = ParseUser.getCurrentUser();
                            curruser.put("Images",photofile);
                            curruser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null){
                                        dlg.dismiss();
                                        Toast.makeText(NewCameraActivity.this, "Profile Picture set", Toast.LENGTH_SHORT).show();
                                        startActivityForResult(new Intent(NewCameraActivity.this, MainActivity.class), 0);
                                    }
                                    else {
                                        Toast.makeText(NewCameraActivity.this, "Error..please check your connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(NewCameraActivity.this, "Please take a picture first", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception error){
                        error.printStackTrace();
                    }
                }
            });
            Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Done just got called", "onClick: ");
                    try {

                        if (photoUri != null) {

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            ParseFile photofile = new ParseFile("Picture.jpg", byteArray);
                            pictures.setPhotoFile(photofile);
                            if (titleText.getText() != null) {
                                final ProgressDialog dlg = new ProgressDialog(NewCameraActivity.this);
                                dlg.setTitle("Please wait.");
                                dlg.setMessage("Saving Picture.  Please wait.");
                                dlg.show();
                                pictures.setTitle(titleText.getText().toString());
                                pictures.setAuthor(ParseUser.getCurrentUser());
                                pictures.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            dlg.dismiss();
                                            Toast.makeText(NewCameraActivity.this, "photo is saved", Toast.LENGTH_SHORT).show();
                                            startActivityForResult(new Intent(NewCameraActivity.this, MainActivity.class), 0);
                                        } else {
                                            Toast.makeText(NewCameraActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(NewCameraActivity.this, "Please take a picture first", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestForCameraPermission(View view) {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(NewCameraActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewCameraActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(NewCameraActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewCameraActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(NewCameraActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public  byte[] scaledData(byte[] data){
        // Resize photo from camera byte array
        Bitmap imageProfile = BitmapFactory.decodeByteArray(data, 0, data.length);
        final Bitmap imageProfileScaled = Bitmap.createScaledBitmap(imageProfile, 500, 500
                * imageProfile.getHeight() / imageProfile.getWidth(), false);



        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageProfileScaled.compress(Bitmap.CompressFormat.JPEG, 100, bos);


        byte[] scaledData = bos.toByteArray();
        return scaledData;
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
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
