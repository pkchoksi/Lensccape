package com.pkchoksi.pkc.lenscape;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class NewPictureActivity extends Activity {

    private Pictures pictures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pictures = new Pictures();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_picture);


        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new NewPictureFragment();
            manager.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }
    public Pictures getCurrentpicture() {
        return pictures;
    }
}
