package com.gudwinfailsafe.android.photobook;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

//import com.google.ads.AdRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by shubhdeep on 14-07-2016.
 */
public class PhotoListFragment extends Fragment {
    public static final int PICK_IMAGE=1;
    //private EditText mSearchString;
    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;

    private AdView mAdView;
    private SearchView mSearchView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        MobileAds.initialize(getActivity(), "ca-app-pub-4783680534092225~3471384531");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);

        mAdView= (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = new Photo();
                PhotoLab.get(getActivity()).addPhoto(photo);
                Intent intent = PhotoPagerActivity.newIntent(getActivity(), photo.getId());
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        //mSearchString=(EditText) view.findViewById(R.id.search_string);
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        updateUI();
        return view;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_list, menu);

        mSearchView=(SearchView)menu.findItem(R.id.menu_item_search_button).getActionView();

         mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()<1){
                    updateUI();
                }else {
                    PhotoLab photoLab = PhotoLab.get(getActivity());
                    List<Photo> photos = photoLab.getPhotos(newText);
                    if (mAdapter == null) {
                        mAdapter = new PhotoAdapter(photos);
                        mPhotoRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setPhotos(photos);
                        mAdapter.notifyDataSetChanged();//notifyItemChanged(int pos);
                    }
                }
                //updateUI();
                //Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // Get the item ID

        if (id==R.id.menu_item_search_button) {
            return true;
        }
        else if (id==R.id.menu_item_new_photo) {
            Photo photo = new Photo();
            PhotoLab.get(getActivity()).addPhoto(photo);
            Intent intent = PhotoPagerActivity.newIntent(getActivity(), photo.getId());
            startActivity(intent);
        }
        else if (id==R.id.menu_item_delete_photo) {
            //photo=new Photo();
            //Intent deleteintent=new Intent(getActivity(), PhotoDeleteListActivity.class);
            //startActivity(deleteintent);
        }
        else if (id==R.id.menu_item_privacy_policy) {
            Intent privacyPolicyIntent= new Intent(getActivity(), PrivacyPolicy.class);
            startActivity(privacyPolicyIntent);
            return true;
        }
        else if (id==R.id.menu_item_tnc) {
            Intent tncIntent= new Intent(getActivity(), TnC.class);
            startActivity(tncIntent);
            return true;
        }
        else if (id==R.id.menu_item_aboutus) {
            Intent aboutUsIntent= new Intent(getActivity(), AboutUs.class);
            startActivity(aboutUsIntent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

        return false;
    }

    private void updateUI() {
        PhotoLab photoLab = PhotoLab.get(getActivity());
        List<Photo> photos = photoLab.getPhotos();

        if (mAdapter == null) {
            mAdapter = new PhotoAdapter(photos);
            mPhotoRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPhotos(photos);
            mAdapter.notifyDataSetChanged();//notifyItemChanged(int pos);
        }
    }


    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private Photo mPhoto;

        private File mPhotoFile;

        private ImageView mThumbnail;
        private TextView mTitleTextView;
        private TextView mDateTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);

            mThumbnail = (ImageView) itemView.findViewById(R.id.list_item_photo_Image);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
        }


        @Override
        public void onClick(View v) {
            Intent intent = PhotoPagerActivity.newIntent(getActivity(), mPhoto.getId());
            startActivity(intent);
        }
        @Override
        public boolean onLongClick(View v){  //New Addition WORKING NOW
            Intent deleteintent=new Intent(getActivity(), PhotoDeleteListActivity.class);
            startActivity(deleteintent);
            //Toast.makeText(getActivity(),mPhoto.getTitle()+" longPressed!", Toast.LENGTH_SHORT).show();
            return true;
        }


        private String dateString;

        public void bindPhoto(Photo photo) {
            mPhoto = photo;
            mPhotoFile=PhotoLab.get(getActivity()).getPhotoFile(mPhoto);
            Uri uri=Uri.fromFile(mPhotoFile);

            if(mPhotoFile==null || !mPhotoFile.exists()){
                int imgdrawable=R.drawable.ic_action_name3;
                mThumbnail.setImageDrawable(getResources().getDrawable(imgdrawable));
            } else {
                Glide.with(getActivity()).load(uri).into(mThumbnail);
            }


            mTitleTextView.setText(mPhoto.getTitle());
            String dateFormat = "MMM dd yyyy";
            dateString = DateFormat.format(dateFormat, mPhoto.getDate()).toString();
            mDateTextView.setText(dateString);
        }

    }


    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<Photo> mPhotos;

        public PhotoAdapter(List<Photo> photos) {
            mPhotos = photos;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_photo, parent, false);

            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            Photo photo = mPhotos.get(position);
            holder.bindPhoto(photo);

        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }

        public void setPhotos(List<Photo> photos) {
            mPhotos = photos;
        }

    }


}

