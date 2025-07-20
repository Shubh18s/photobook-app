package com.gudwinfailsafe.android.photobook;


import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class PhotoActivity extends SingleFragmentActivity {

    private static final String EXTRA_PHOTO_ID="com.bignerdranch.android.photobook.photo_id";

    public static Intent newIntent(Context packageContext, UUID photoId){
        Intent intent=new Intent(packageContext, PhotoActivity.class);
        intent.putExtra(EXTRA_PHOTO_ID, photoId);
        return intent;
    }
    protected Fragment createFragment(){
        UUID photoId=(UUID)getIntent().getSerializableExtra(EXTRA_PHOTO_ID);
        return PhotoFragment.newInstance(photoId);
    }

}
