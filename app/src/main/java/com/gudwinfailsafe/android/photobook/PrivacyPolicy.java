package com.gudwinfailsafe.android.photobook;


import androidx.fragment.app.Fragment;

/**
 * Created by shubhdeep on 11-01-2018.
 */
public class PrivacyPolicy extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment(){
        return new PrivacyPolicyFragment();
    }

}
