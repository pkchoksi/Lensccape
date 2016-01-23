package com.pkchoksi.pkc.lenscape;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by pkcho on 1/2/2016.
 */
public class initialApplication extends Application {
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Pictures.class);
        ParseObject.registerSubclass(Follow.class);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_app_key));

    }
}
