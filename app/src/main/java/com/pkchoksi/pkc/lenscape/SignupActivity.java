package com.pkchoksi.pkc.lenscape;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);




               // Set up the signup form.
               usernameView = (EditText) findViewById(R.id.username_edit_text);
               passwordView = (EditText) findViewById(R.id.password_edit_text);
               passwordAgainView = (EditText) findViewById(R.id.password_again_edit_text);
               emailView = (EditText) findViewById(R.id.email_edit_text);


               // Set up the submit button click handler
               findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
                   public void onClick(View view) {

                       // Validate the sign up data
                       boolean validationError = false;
                       StringBuilder validationErrorMessage =
                               new StringBuilder(getResources().getString(R.string.error_intro));
                       if (isEmpty(usernameView)) {
                           validationError = true;
                           validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                       }
                       if(isEmpty(emailView)){
                           validationError = true;
                           validationErrorMessage.append("enter an email");
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
               if (etText.getText().toString().trim().length() > 0) {
                   return false;
               } else {
                   return true;
               }
           }

           private boolean isMatching(EditText etText1, EditText etText2) {
               if (etText1.getText().toString().equals(etText2.getText().toString())) {
                   return true;
               } else {
                   return false;
               }
           }
}
