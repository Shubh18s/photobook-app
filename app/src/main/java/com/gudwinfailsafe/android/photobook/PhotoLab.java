package com.gudwinfailsafe.android.photobook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.gudwinfailsafe.android.photobook.database.PhotoDbSchema.PhotoTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by shubhdeep on 14-07-2016.
 */
public class PhotoLab {
    private static PhotoLab sPhotoLab;


    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static PhotoLab get(Context context){
        if(sPhotoLab==null){
            sPhotoLab=new PhotoLab(context);
        }
        return sPhotoLab;
    }

    private PhotoLab(Context context){
        mContext=context.getApplicationContext();
        mDatabase=new PhotoBaseHelper(mContext).getWritableDatabase();

    }

    public void addPhoto(Photo p){
    ContentValues values=getContentValues(p);
        mDatabase.insert(PhotoTable.NAME, null, values);
    }

    public void deletePhoto(Photo p){     //Trying Out for the first time Fingers Crossed- Done perfectly Voila
        String photoToDel=p.getPhotoFilename();

        File fileToDel=new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), photoToDel);
        try{
        if (fileToDel.exists()){
            fileToDel.delete();
        }}
        catch(Exception e){
            Log.e("App","Exception while Deleting image from pictures folder"+e.getMessage());
        }
        //File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //externalFilesDir.
        String uuidString=p.getId().toString();
        mDatabase.delete(PhotoTable.NAME, PhotoTable.Cols.UUID + "= ?", new String[]{uuidString});

    }


    public List<Photo> getPhotos(String searchword){
        List<Photo> photos=new ArrayList<>();

        PhotoCursorWrapper cursor=queryPhotos(PhotoTable.Cols.TITLE + " LIKE ?",new String[]{'%'+searchword+'%'});
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                photos.add(cursor.getPhoto());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return photos;
    }

    public List<Photo> getPhotos(){
        List<Photo> photos=new ArrayList<>();


        PhotoCursorWrapper cursor=queryPhotos(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                photos.add(cursor.getPhoto());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return photos;
    }

    public Photo getPhoto(UUID id){
        PhotoCursorWrapper cursor=queryPhotos(
                PhotoTable.Cols.UUID + " =? ",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPhoto();
        }finally{
            cursor.close();
        }
    }

    public File getPhotoFile(Photo photo){
        File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);//getExternalStorageDirectory().toString());

        if(externalFilesDir==null){
            return null;
        }
        return  new File(externalFilesDir, photo.getPhotoFilename());
    }

    public void updatePhoto(Photo photo){
        String uuidString=photo.getId().toString();
        ContentValues values=getContentValues(photo);

        mDatabase.update(PhotoTable.NAME, values, PhotoTable.Cols.UUID + "= ?", new String[]{uuidString});
    }

    private static ContentValues getContentValues(Photo photo){
        ContentValues values=new ContentValues();
        values.put(PhotoTable.Cols.UUID, photo.getId().toString());
        values.put(PhotoTable.Cols.TITLE, photo.getTitle());
        values.put(PhotoTable.Cols.DATE, photo.getDate().getTime());
        values.put(PhotoTable.Cols.DESCRIPTION, photo.getDescription());

        return values;
    }

    private PhotoCursorWrapper queryPhotos(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(
                PhotoTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new PhotoCursorWrapper(cursor);
    }

}
