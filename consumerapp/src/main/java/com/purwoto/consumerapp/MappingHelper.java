package com.purwoto.consumerapp;

import android.database.Cursor;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<User> mapCursorToArrayList(Cursor userCursor) {
        ArrayList<User> userList = new ArrayList<>();

        while (userCursor.moveToNext()) {
            int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
            String nama = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAMA));
            String avatar_url = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL));
            String url = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.URL));
            User user = new User();
            user.setLogin(nama);
            user.setName("");
            user.setBlog("");
            user.setLocation("");
            user.setEmail("");
            user.setAvatar(avatar_url);
            user.setUrl(url);
            user.setFollowers(0);
            user.setFollowing(0);
            user.setFavorit(true);
            userList.add(user);
        }

        return userList;
    }

    public static User mapCursorToObject(Cursor userCursor) {
        userCursor.moveToFirst();
        int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
        String nama = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAMA));
        String avatar_url = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL));
        String url = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.URL));
        User user = new User();
        user.setLogin(nama);
        user.setName("");
        user.setBlog("");
        user.setLocation("");
        user.setEmail("");
        user.setAvatar(avatar_url);
        user.setUrl(url);
        user.setFollowers(0);
        user.setFollowing(0);
        user.setFavorit(true);
        return user;
    }
}
