package com.purwoto.githubusersubmission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.purwoto.githubusersubmission.db.DatabaseHelper;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private UserListAdapter adapter;
    DatabaseHelper myDb;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        myDb = new DatabaseHelper(this);

        final ArrayList<User> listUser = new ArrayList<>();

        Cursor res = myDb.getAllData();

        while(res.moveToNext()){
            User user = new User();
            user.setLogin(res.getString(1));
            user.setName("");
            user.setBlog("");
            user.setLocation("");
            user.setEmail("");
            user.setAvatar(res.getString(2));
            user.setUrl(res.getString(3));
            user.setFollowers(0);
            user.setFollowing(0);
            user.setFavorit(true);
            listUser.add(user);
        }

        progressDialog = new ProgressDialog(FavoriteActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        final RecyclerView recyclerView = findViewById(R.id.rv_favorit);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserListAdapter(listUser);
        recyclerView.setAdapter(adapter);

        progressDialog.dismiss();

        adapter.setOnItemClickCallback(new UserListAdapter.OnItemClickCallback(){
            @Override
            public void onItemClicked(User data, View view) {
                Intent i = new Intent(FavoriteActivity.this, DetailUserActivity.class);
                TextView nama = (TextView) view.findViewById(R.id.tv_nama);
                i.putExtra(DetailUserActivity.EXTRA_NAMA, nama.getText().toString());
                startActivity(i);
            }
        });
    }
}