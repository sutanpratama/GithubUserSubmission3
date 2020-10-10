package com.purwoto.githubusersubmission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> user = new ArrayList<>();

    public UserAdapter(Context context){
        this.context = context;
    }

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }

    public ArrayList<User> getUser() {
        return user;
    }

    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public User getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        User user = (User) getItem(position);
        viewHolder.bind(user);
        return itemView;
    }

    public void updateUserList(ArrayList<User> list){
        this.user = list;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView tvNama,
                         tvUrl,
                         tvFollow;
        private ImageView imgUser;

        ViewHolder(View view) {
            tvNama = view.findViewById(R.id.tv_nama);
            tvUrl = view.findViewById(R.id.tv_url);
            imgUser = view.findViewById(R.id.img_user);
        }

        void bind(User user) {
            tvNama.setText(user.getLogin());
            tvUrl.setText(user.getUrl());
            Picasso.get().load(user.getAvatar()).into(imgUser);
        }
    }
}
