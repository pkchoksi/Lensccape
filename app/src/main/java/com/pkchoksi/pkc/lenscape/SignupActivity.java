package com.pkchoksi.pkc.lenscape;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by pkcho on 1/2/2016.
 */
public class SignupActivity extends AppCompatActivity{

    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordAgainView;
    private EditText emailView;
    private EditText phnumberView;
    private ImageView Welcome;
    private ImageButton Begin;
    private TextView usertext;
    private TextView passtext;
    private TextView repasstext;
    private TextView phonetext;
    private TextView emailtext;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       Toolbar toolbar = (Toolbar)findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (Exception e){
            e.printStackTrace();
        }


        ImageView logo = (ImageView) toolbar.findViewById(R.id.logo_app);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Lenscape.ttf");



        logo.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.logo_lescape, 100, 100));





        // Set up the signup form.
                usertext = (TextView) findViewById(R.id.textusername);
                usertext.setTypeface(typeface);
                passtext = (TextView) findViewById(R.id.textpass);
                passtext.setTypeface(typeface);
                repasstext = (TextView) findViewById(R.id.textretypepass);
                repasstext.setTypeface(typeface);
                emailtext = (TextView) findViewById(R.id.textemail);
                emailtext.setTypeface(typeface);
                phonetext = (TextView) findViewById(R.id.textphone);
                phonetext.setTypeface(typeface);
               usernameView = (EditText) findViewById(R.id.username_edit_text);
               passwordView = (EditText) findViewById(R.id.password_edit_text);
               passwordAgainView = (EditText) findViewById(R.id.password_again_edit_text);
               emailView = (EditText) findViewById(R.id.email_edit_text);
               phnumberView = (EditText) findViewById(R.id.phonenumber_edit_text);
                Welcome = (ImageView)findViewById(R.id.Welcome);
                Welcome.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.welcome, 500, 500));
                Begin = (ImageButton)findViewById(R.id.action_button);
                Begin.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.button,75,50));

               // Set up the submit button click handler
               Begin.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View view) {

                       // Validate the sign up data
                       boolean validationError = false;
                       StringBuilder validationErrorMessage =
                               new StringBuilder(getResources().getString(R.string.error_intro));
                       if (isEmpty(usernameView)) {
                           validationError = true;
                           validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                       }
                       if (isEmpty(emailView)) {
                           validationError = true;
                           validationErrorMessage.append("enter an email");
                       }
                       if (isEmpty(phnumberView)) {
                           validationError = true;
                           validationErrorMessage.append("enter your phone number");
                       }
                       if (isEmpty(passwordView)) {
                           if (validationError) {
                               validationErrorMessage.append(getResources().getString(R.string.error_join));
                           }
                           validationError = true;
                           validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
                       }
                       if (!isMatching(passwordView, passwordAgainView)) {
                           if (validationError) {
                               validationErrorMessage.append(getResources().getString(R.string.error_join));
                           }
                           validationError = true;
                           validationErrorMessage.append(getResources().getString(
                                   R.string.error_mismatched_passwords));
                       }
                       validationErrorMessage.append(getResources().getString(R.string.error_end));

                       // If there is a validation error, display the error
                       if (validationError) {
                           Toast.makeText(SignupActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                                   .show();
                           return;
                       }

                       // Set up a progress dialog
                       final ProgressDialog dlg = new ProgressDialog(SignupActivity.this);
                       dlg.setTitle("Please wait.");
                       dlg.setMessage("Signing up.  Please wait.");
                       dlg.show();

                       // Set up a new Parse user
                       ParseUser user = new ParseUser();
                       user.setUsername(usernameView.getText().toString());
                       user.setPassword(passwordView.getText().toString());
                       user.setEmail(emailView.getText().toString());
                       user.put("phone", phnumberView.getText().toString());
                       // Call the Parse signup method
                       user.signUpInBackground(new SignUpCallback() {

                           @Override
                           public void done(ParseException e) {
                               dlg.dismiss();
                               if (e != null) {
                                   // Show the error message
                                   Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                               } else {
                                   // Start an intent for the dispatch activity
                                   Intent intent = new Intent(SignupActivity.this, DispatchActivity.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(intent);
                               }
                           }
                       });
                   }
               });
    }



    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
           }

           private boolean isMatching(EditText etText1, EditText etText2) {
               return etText1.getText().toString().equals(etText2.getText().toString());
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
