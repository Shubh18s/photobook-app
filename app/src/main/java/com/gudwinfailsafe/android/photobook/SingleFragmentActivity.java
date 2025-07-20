package com.gudwinfailsafe.android.photobook;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by shubhdeep on 14-07-2016.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher4);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);
        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();

        }
    }
}
