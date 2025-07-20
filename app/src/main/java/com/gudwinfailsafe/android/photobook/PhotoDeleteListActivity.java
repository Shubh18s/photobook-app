package com.gudwinfailsafe.android.photobook;

import androidx.fragment.app.Fragment;

/**
 * Created by shubhdeep on 18-07-2016.
 */
public class PhotoDeleteListActivity extends SingleFragmentActivity {
    protected Fragment createFragment(){
        return new PhotoDeleteFragment();
    }
}
