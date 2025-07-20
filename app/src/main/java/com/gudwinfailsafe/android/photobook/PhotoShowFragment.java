package com.gudwinfailsafe.android.photobook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.Layout;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

/**
 * Created by shubhdeep on 06-09-2016.
 */
public class PhotoShowFragment extends DialogFragment {

    private static final String ARG_PHOTO="photo";
    private Photo mPhoto;
    private ImageView mImageView;
    private File mPhotoFile;

    public static PhotoShowFragment newInstance(UUID photoId){
        Bundle args=new Bundle();
        args.putSerializable(ARG_PHOTO, photoId);

        PhotoShowFragment fragment=new PhotoShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        UUID photoId=(UUID)getArguments().getSerializable(ARG_PHOTO);
        mPhoto=PhotoLab.get(getActivity()).getPhoto(photoId);
        mPhotoFile=PhotoLab.get(getActivity()).getPhotoFile(mPhoto);

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);

        if(!mPhotoFile.exists()){
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.image_not_available)
                    .setPositiveButton(android.R.string.ok, null).create();
        } else {
            mImageView=(ImageView)v.findViewById(R.id.photo_view);
            //Picasso.with(getActivity()).load(Uri.fromFile(mPhotoFile)).into(mImageView);
            mImageView.setImageURI(Uri.fromFile(mPhotoFile));
        }

        return new AlertDialog.Builder(getActivity()).setView(v).create();

    }
}
