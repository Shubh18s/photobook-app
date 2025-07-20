package com.gudwinfailsafe.android.photobook;

import androidx.fragment.app.Fragment;

/**
 * Created by shubhdeep on 14-07-2016.
 */
public class PhotoListActivity extends SingleFragmentActivity {
    protected Fragment createFragment(){
        return new PhotoListFragment();
    }

}
