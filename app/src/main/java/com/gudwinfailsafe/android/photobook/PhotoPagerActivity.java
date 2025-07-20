package com.gudwinfailsafe.android.photobook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by shubhdeep on 15-07-2016.
 */
public class PhotoPagerActivity extends AppCompatActivity {

    private static final String EXTRA_PHOTO_ID="com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Photo> mPhotos;

    public static Intent newIntent(Context packageContext, UUID photoId){
        Intent intent=new Intent(packageContext, PhotoPagerActivity.class);
        intent.putExtra(EXTRA_PHOTO_ID, photoId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);
        UUID photoId=(UUID)getIntent().getSerializableExtra(EXTRA_PHOTO_ID);

        mViewPager=(ViewPager)findViewById(R.id.activity_photo_pager_view_pager);

        mPhotos=PhotoLab.get(this).getPhotos();
        Toast.makeText(this,"Write/Edit, PhotoNote will be saved automatically!",Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager){


            @Override
            public Fragment getItem(int position) {
                Photo photo=mPhotos.get(position);
                return PhotoFragment.newInstance(photo.getId());
            }

            @Override
            public int getCount() {
                return mPhotos.size();
            }
        });

        for(int i=0; i<mPhotos.size(); i++){
            if(mPhotos.get(i).getId().equals(photoId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
