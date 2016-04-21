package com.pkchoksi.pkc.lenscape;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;
/**
 * Created by pkcho on 1/8/2016.
 */
public class NewPictureFragment extends android.app.Fragment {

    private ImageButton photoButton;
    private Button saveButton;
    private Button cancelButton;
    private EditText picName;
    private ParseImageView picPreview;
    private Button saveasprofile;

    //ParseGeoPoint userLocation = new ParseGeoPoint();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater,  final ViewGroup parent,
                             Bundle SavedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_picture, parent, false);

        picName = ((EditText) v.findViewById(R.id.pic_name));

        // The mealRating spinner lets people assign favorites of meals they've
        // eaten.
        // Meals with 4 or 5 ratings will appear in the Favorites view.


       // location = (TextView) v.findViewById(R.id.location);

        photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(picName.getWindowToken(), 0);
                startCamera();
            }
        });

        saveasprofile = (Button)v.findViewById(R.id.saveasprofile);
        saveasprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pictures propic = ((NewPictureActivity) getActivity()).getCurrentpicture();
                ParseUser user = ParseUser.getCurrentUser();
                user.put("Images", propic);
                user.saveInBackground();
                HomePageFragment fragment = new HomePageFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        saveButton = ((Button) v.findViewById(R.id.save_button));
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Pictures picture = ((NewPictureActivity) getActivity()).getCurrentpicture();

                // When the user clicks "Save," upload the meal to Parse
                // Add data to the meal object:
                picture.setTitle(picName.getText().toString());
                //picture.setLocation(userLocation.toString());
                // Associate the meal with the current user
                picture.setAuthor(ParseUser.getCurrentUser());

                // Add the rating


                // If the user added a photo, that data will be
                // added in the CameraFragment

                // Save the picture
                picture.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });

        cancelButton = ((Button) v.findViewById(R.id.cancel_button));
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        // Until the user has taken a photo, hide the preview
        picPreview = (ParseImageView) v.findViewById(R.id.preview_image);
        picPreview.setVisibility(View.INVISIBLE);

        return v;
    }

    /*
     * All data entry about a Meal object is managed from the NewMealActivity.
     * When the user wants to add a photo, we'll start up a custom
     * CameraFragment that will let them take the photo and save it to the Meal
     * object owned by the NewMealActivity. Create a new CameraFragment, swap
     * the contents of the fragmentContainer (see activity_new_meal.xml), then
     * add the NewMealFragment to the back stack so we can return to it when the
     * camera is finished.
     */
    public void startCamera() {
        Fragment cameraFragment = new CameraFragment();
        FragmentTransaction transaction = getActivity().getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentContainer, cameraFragment);
        transaction.addToBackStack("NewPictureFragment");
        transaction.commit();
    }

    /*
     * On resume, check and see if a meal photo has been set from the
     * CameraFragment. If it has, load the image in this fragment and make the
     * preview image visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        ParseFile photoFile = ((NewPictureActivity) getActivity()).getCurrentpicture().getPhotoFile();
        if (photoFile != null) {
            picPreview.setParseFile(photoFile);
            picPreview.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    picPreview.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
