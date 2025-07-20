package com.gudwinfailsafe.android.photobook;

import android.net.Uri;
import android.preference.MultiSelectListPreference;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.nio.channels.Selector;
import java.util.List;

/**
 * Created by shubhdeep on 18-07-2016.
 */
public class PhotoDeleteFragment extends Fragment {

    private PhotoAdapter mPhotoAdapter;

    private RecyclerView mPhotoRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_delete_photo_list, container, false);

        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.delete_photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contextual_menu, menu);
    }

    private void updateUI() {
        PhotoLab photoLab = PhotoLab.get(getActivity());
        List<Photo> photos = photoLab.getPhotos();


            mPhotoAdapter = new PhotoAdapter(photos);
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);

    }
    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Photo mPhoto;
        private File mPhotoFile;
        private ImageView mImageView;
        private TextView mTitle;
        private TextView mDate;
        private CheckBox mSelectedCheckBox;

        public PhotoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.delete_list_item_photo_Image);
            mTitle = (TextView) itemView.findViewById(R.id.delete_title_text_view);
            mDate = (TextView) itemView.findViewById(R.id.delete_date_text_view);
            mSelectedCheckBox = (CheckBox) itemView.findViewById(R.id.selected_check_box);
        }

            @Override
            public void onClick(View v){
                if(mSelectedCheckBox.isChecked())
                {
                    mSelectedCheckBox.setChecked(false);
                    v.setActivated(true);
                }
                else
                {
                    mSelectedCheckBox.setChecked(true);
                    v.setActivated(false);
                }

            }



        private String dateString;

        public void bindPhoto(Photo photo){
            mPhoto=photo;

            mPhotoFile=PhotoLab.get(getActivity()).getPhotoFile(mPhoto);
            Uri uri=Uri.fromFile(mPhotoFile);

            if(mPhotoFile==null || !mPhotoFile.exists()){
                int imgdrawable=R.drawable.ic_action_name3;
                mImageView.setImageDrawable(getResources().getDrawable(imgdrawable));
            } else {
                Picasso.get().load(uri).fit().into(mImageView);
            }
            mTitle.setText(mPhoto.getTitle());
            String dateFormat = "EEE, MMM dd";
            dateString = DateFormat.format(dateFormat, mPhoto.getDate()).toString();
            mDate.setText(dateString);

        }

        private SparseBooleanArray mSelectedPositions=new SparseBooleanArray();
        private boolean mIsSelectable=false;

        private void setItemChecked(int position, boolean isChecked){
            mSelectedPositions.put(position,isChecked);
        }
        private boolean isItemChecked(int position){
            return mSelectedPositions.get(position);
        }
        private void setSelectable(boolean selectable){
            mIsSelectable=selectable;
        }
        private boolean isSelectable() {
            return mIsSelectable;
        }




    }
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{
        private List<Photo> mPhotos;
        public PhotoAdapter(List<Photo> photos){ mPhotos=photos;}

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.delete_list_item_photo, parent, false);

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
