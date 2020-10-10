package com.purwoto.consumerapp;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final String AUTHORITY = "com.purwoto.githubusersubmission";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class UserColumns implements BaseColumns {
        public static final String TABLE_NAME = "favorit";
        public static final String NAMA = "nama";
        public static final String AVATAR_URL = "avatar_url";
        public static final String URL = "url";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
