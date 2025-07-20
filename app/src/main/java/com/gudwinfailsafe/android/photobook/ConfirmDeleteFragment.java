package com.gudwinfailsafe.android.photobook;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.print.PrintJob;
import androidx.fragment.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shubhdeep on 29-12-2017.
 */
public class ConfirmDeleteFragment extends DialogFragment {

    private Photo mPhoto;
    private static final String EXTRA_ID="com.gudwinfailsafe.android.photobook.id";

    public static ConfirmDeleteFragment newInstance(UUID id){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_ID, id);

        ConfirmDeleteFragment fragment=new ConfirmDeleteFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete,null);
        return new AlertDialog.Builder(getActivity()).setView(v).setPositiveButton(R.string.delete_note, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UUID photoId=(UUID)getArguments().getSerializable(EXTRA_ID);
                mPhoto=PhotoLab.get(getActivity()).getPhoto(photoId);
                PhotoLab.get(getActivity()).deletePhoto(mPhoto);
                getActivity().finish();
            }
        }).setNegativeButton(R.string.cancel_delete, null).create();
    }
}
