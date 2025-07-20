package com.gudwinfailsafe.android.photobook.database;

/**
 * Created by shubhdeep on 15-07-2016.
 */
public class PhotoDbSchema {
    public static final class PhotoTable{
        public static final String NAME="photos";

        public static final class Cols{
            public static final String UUID="uuid";
            public static final String TITLE="title";
            public static final String DATE="date";
            public static final String DESCRIPTION="description";
        }
    }
}
