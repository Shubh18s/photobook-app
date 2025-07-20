package com.gudwinfailsafe.android.photobook;

import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.UUID;

/**
 * Created by shubhdeep on 03-01-2018.
 */


public class ChooseImageSourceDialogFragment extends DialogFragment {


    private static final String EXTRA_ID="com.gudwinfailsafe.android.photobook.id";

    public static ChooseImageSourceDialogFragment newInstance(UUID id){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_ID, id);

        ChooseImageSourceDialogFragment fragment=new ChooseImageSourceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }



    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_image,null);
        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }
}
