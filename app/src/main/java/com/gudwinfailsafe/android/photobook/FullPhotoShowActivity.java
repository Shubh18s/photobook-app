package com.gudwinfailsafe.android.photobook;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import java.util.UUID;

/**
 * Created by shubhdeep on 29-12-2017.
 */
public class FullPhotoShowActivity extends SingleFragmentActivity {

    public static final String EXTRA_PHOTO_ID="com.failsafe.android.photobook.photo_id";

    public static Intent newIntent(Context packageContext, UUID photoId){
        Intent intent=new Intent(packageContext, FullPhotoShowActivity.class);
        intent.putExtra(EXTRA_PHOTO_ID, photoId);
        return intent;
    }
    @Override
    protected Fragment createFragment(){
        return new FullPhotoShowFragment();
    }

}
