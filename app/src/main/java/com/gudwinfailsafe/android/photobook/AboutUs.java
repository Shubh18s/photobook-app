package com.gudwinfailsafe.android.photobook;

import androidx.fragment.app.Fragment;

/**
 * Created by shubhdeep on 12-01-2018.
 */
public class AboutUs extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new AboutUsFragment();
    }
}
