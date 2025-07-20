package com.gudwinfailsafe.android.photobook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.io.File;
import java.text.Format;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoFragment extends Fragment {

    private Photo mPhoto;
    private File mPhotoFile;

    private static final String ARG_CRIME_ID="crime_id";
    private static final String DIALOG_DATE="DialogDate";
    private static final String DIALOG_PHOTO="DialogPhoto";
    private static final String DIALOG_CONFIRM_DELETE="DialogConfirmDelete";
    private static final String DIALOG_IMAGE_SOURCE="DialogImageSource";

    private static final int REQUEST_DATE=0;
    private static final int REQUEST_PHOTO=1;
    private static final int PICK_IMAGE_REQUEST=2;


    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private TextView mPhotoDateField;
    private EditText mTitleField;
    private ImageButton mDateButton;
    private EditText mDescriptionField;
    private String dateString;


    public static PhotoFragment newInstance(UUID crimeId){
        Bundle args=new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        PhotoFragment fragment=new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String formatDate(Date date){
        String dateFormat="EEE, MMM dd yyyy";
        dateString= DateFormat.format(dateFormat, date).toString();
        return dateString;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID photoId=(UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mPhoto=PhotoLab.get(getActivity()).getPhoto(photoId);
        mPhotoFile=PhotoLab.get(getActivity()).getPhotoFile(mPhoto);

        //Toast.makeText(getActivity(), "WRITE/EDIT, It will be saved automatically!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause(){
        super.onPause();

        PhotoLab.get(getActivity()).updatePhoto(mPhoto);
        updatePhotoView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_photo, container, false);

        mPhotoView=(ImageView) v.findViewById(R.id.photo);
        updatePhotoView();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mPhotoFile.exists()) {
                    FragmentManager manager = getFragmentManager();
                    PhotoShowFragment dialog2 = PhotoShowFragment.newInstance(mPhoto.getId());
                    dialog2.show(manager, DIALOG_PHOTO);
                } else {
                    Intent intent = FullPhotoShowActivity.newIntent(getActivity(), mPhoto.getId());
                    startActivity(intent);
                }


            }
        });


        mPhotoButton=(ImageButton)v.findViewById(R.id.photo_button);
        final Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager=getActivity().getPackageManager();
        boolean canTakePhoto=mPhotoFile!=null &&
                captureImage.resolveActivity(packageManager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto){
            Uri uri=Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder ImageSourceDialog= new AlertDialog.Builder(getActivity());
                ImageSourceDialog.setTitle("Pick Image From:");
                ImageSourceDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //Trying intent to pick image from gallery
                        pickIntent.setType("image/*");
                        Uri uriImagePath = Uri.fromFile(mPhotoFile);
                        pickIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
                        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST);
                    }
                }).setIcon(R.drawable.ic_gallery);

                ImageSourceDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(captureImage, REQUEST_PHOTO);  //only this line for camera
                    }
                }).setIcon(R.drawable.ic_camera);

                ImageSourceDialog.show();
            }

        });

        mTitleField=(EditText)v.findViewById(R.id.photo_title);
        mTitleField.setText(mPhoto.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //does nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhoto.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //does nothing
            }
        });

        mPhotoDateField=(TextView)v.findViewById(R.id.photo_date_text);
        mPhotoDateField.setText(formatDate(mPhoto.getDate()));

        mDateButton=(ImageButton) v.findViewById(R.id.photo_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mPhoto.getDate());
                dialog.setTargetFragment(PhotoFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mDescriptionField=(EditText) v.findViewById(R.id.photo_description);
        mDescriptionField.setText(mPhoto.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //does nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhoto.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //does nothing
            }
        });

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_DATE){
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mPhoto.setDate(date);
            mPhotoDateField.setText(formatDate(date));
        } else if(requestCode==REQUEST_PHOTO){
            updatePhotoView();
        } else if(requestCode==PICK_IMAGE_REQUEST){
            Glide.with(getActivity()).load(data.getData()).into(mPhotoView);
            //updatePhotoView();
            File file=getBitmapFile(data);

            //Toast.makeText(getActivity(),file.toString(),Toast.LENGTH_SHORT).show();
            try {
               copyFile(file, mPhotoFile);
            }catch(IOException e){
              Toast.makeText(getActivity(),"Turn Onn Storage permissions in SETTINGS>APPS>PHOTOBOOK>PERMISSIONS",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }


    public File getBitmapFile(Intent data){
        Uri selectedImage=data.getData();
        Cursor cursor=getContext().getContentResolver().query(selectedImage, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
        cursor.moveToFirst();

        int idx=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String selectedImagePath=cursor.getString(idx);
        cursor.close();

        return new File(selectedImagePath);

    }

    private  void copyFile(File sourceFile, File destFile)throws IOException{
        if(!sourceFile.exists()){
            return;
        }

        FileChannel source=null;
        FileChannel destination=null;
        source=new FileInputStream(sourceFile).getChannel();
        destination=new FileOutputStream(destFile).getChannel();
        if(destination!=null && source!=null){
            destination.transferFrom(source, 0, source.size());
        }
        if(source!=null){
            source.close();
        }
        if(destination!=null){
            destination.close();
        }
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_photo, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId(); // Get the item ID

        if (id==R.id.menu_item_delete_photo) {
            //PhotoLab.get(getActivity()).deletePhoto(mPhoto);   //Don't need to delete directly. using delete fragment
            //getActivity().finish();

            //new AlertDialog.Builder(getActivity()).setMessage("PhotoNote will be deleted.").setPositiveButton(R.string.delete_note, new DialogInterface.OnClickListener() {
            //  @Override
            //public void onClick(DialogInterface dialog, int which) {
            //  PhotoLab.get(getActivity()).deletePhoto(mPhoto);
            //}
            // }).setNegativeButton(R.string.cancel_delete, null).create();

            FragmentManager manager = getFragmentManager();
            ConfirmDeleteFragment dialog = ConfirmDeleteFragment.newInstance(mPhoto.getId());
            dialog.show(manager, DIALOG_CONFIRM_DELETE);

            return true;
        }
        else if (id==R.id.menu_item_share_photo) {
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, getPhotoNote());
            i.putExtra(Intent.EXTRA_SUBJECT, getString((R.string.photo_title_label)));
            startActivity(i);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }


    private void updatePhotoView(){
        if(mPhotoFile==null || !mPhotoFile.exists()){
            int imgdrawable=R.drawable.ic_action_name3;
            mPhotoView.setImageDrawable(getResources().getDrawable(imgdrawable));
        } else {
            Glide.with(getActivity()).load(Uri.fromFile(mPhotoFile)).into(mPhotoView);
            //Bitmap bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            //mPhotoView.setImageBitmap(bitmap);// Now we're not using the PictureUtils.java class
        }
    }

    private String getPhotoNote(){ //Making string to share note
        String dateFormat="EEE, MMM dd";
        String dateString= DateFormat.format(dateFormat, mPhoto.getDate()).toString();
        String title=mPhoto.getTitle();
        String note=mPhoto.getDescription();
        //String uri=mPhoto.getPhotoFilename(); //Not used yet, since not sending the image

        String report=getString(R.string.photo_note, title, dateString, note);
        return report;
    }

}
