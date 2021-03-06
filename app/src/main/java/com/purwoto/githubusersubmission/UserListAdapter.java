package com.purwoto.githubusersubmission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListViewHolder> {

    private Context context;
    private ArrayList<User> listUser = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    public UserListAdapter(ArrayList<User> user){
        this.listUser = user;
    }

    public void setUser(ArrayList<User> user) {
        this.listUser = user;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public UserListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListAdapter.ListViewHolder holder, int position) {
        final User user = listUser.get(position);

        holder.tvNama.setText(user.getLogin());
        holder.tvUrl.setText(user.getUrl());
        holder.tvAvatarUrl.setText(user.getAvatar());
        holder.tvAvatarUrl.setVisibility(View.INVISIBLE);
        Picasso.get().load(user.getAvatar()).into(holder.imgUser);

        if(!user.isFavorit()){
            holder.ivFavorit.setVisibility(View.INVISIBLE);
        }else{
            holder.ivFavorit.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listUser.get(holder.getAdapterPosition()), holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void updateUserList(ArrayList<User> list){
        this.listUser = list;
        notifyDataSetChanged();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNama,
                         tvUrl,
                         tvAvatarUrl;
        private ImageView imgUser,
                          ivFavorit;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvUrl = itemView.findViewById(R.id.tv_url);
            tvAvatarUrl = itemView.findViewById(R.id.tv_avatar_url);
            imgUser = itemView.findViewById(R.id.img_user);
            ivFavorit = itemView.findViewById(R.id.iv_heart);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data, View view);
    }
}
