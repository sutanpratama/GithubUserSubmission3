package com.purwoto.consumerapp;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    public static ArrayList<User> listUser = new ArrayList<>();

    private String login,
                   name,
                   blog,
                   url,
                   email,
                   avatar_url,
                   location;

    private int followers,
                following,
                total_count,
                public_repos;

    private boolean favorit;

    private boolean incomplete_results;

    private List<User> items;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected User(Parcel in) {
        login = in.readString();
        name = in.readString();
        blog = in.readString();
        url = in.readString();
        email = in.readString();
        avatar_url = in.readString();
        followers = in.readInt();
        following = in.readInt();
        favorit = in.readBoolean();
    }

    public User(){
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlog() {
        return blog;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar_url;
    }

    public void setAvatar(String avatar) {
        this.avatar_url = avatar;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFavorit() {
        return favorit;
    }

    public void setFavorit(boolean favorit) {
        this.favorit = favorit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(blog);
        dest.writeString(email);
        dest.writeString(avatar_url);
        dest.writeInt(followers);
        dest.writeInt(following);
        dest.writeBoolean(favorit);
    }
}
