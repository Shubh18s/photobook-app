package com.gudwinfailsafe.android.photobook;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

/**
 * Created by shubhdeep on 29-12-2017.
 */
public class FullPhotoShowFragment extends Fragment {
    //Nothing YEt@Override
    private File mPhotoFile;
    private Photo mPhoto;
    private ImageView mFullPhotoView;
    private Matrix imageMatrix;     //new Addition

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID photoId=(UUID)getActivity().getIntent().getSerializableExtra(FullPhotoShowActivity.EXTRA_PHOTO_ID);
        mPhoto=PhotoLab.get(getActivity()).getPhoto(photoId);
        mPhotoFile=PhotoLab.get(getActivity()).getPhotoFile(mPhoto);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_full_photo, container, false);
        mFullPhotoView=(ImageView)v.findViewById(R.id.full_photo);
        Glide.with(getActivity()).load(Uri.fromFile(mPhotoFile)).into(mFullPhotoView);
        return v;
    }

        @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.full_photo_menu, menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_item_share_photo) {
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setType("image/jpg");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mPhotoFile));
            startActivity(i);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

    }


}
