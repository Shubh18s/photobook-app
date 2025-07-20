package com.gudwinfailsafe.android.photobook;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.gudwinfailsafe.android.photobook.database.PhotoDbSchema.PhotoTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shubhdeep on 15-07-2016.
 */
public class PhotoCursorWrapper extends CursorWrapper {
    public PhotoCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Photo getPhoto(){
        String uuidString=getString(getColumnIndex(PhotoTable.Cols.UUID));
        String title=getString(getColumnIndex(PhotoTable.Cols.TITLE));
        long date=getLong(getColumnIndex(PhotoTable.Cols.DATE));
        String description=getString(getColumnIndex(PhotoTable.Cols.DESCRIPTION));

        Photo photo=new Photo(UUID.fromString(uuidString));
        photo.setTitle(title);
        photo.setDate(new Date(date));
        photo.setDescription(description);

        return photo;
    }
}
