package com.purwoto.consumerapp;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private UserListAdapter adapter;
    ProgressDialog progressDialog;

    private static final String EXTRA_STATE = "EXTRA_STATE";
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<User> listUser = new ArrayList<>();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");

        recyclerView = findViewById(R.id.rv_user);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserListAdapter(listUser);
        recyclerView.setAdapter(adapter);

        Cursor dataCursor = getContentResolver().query(DatabaseContract.UserColumns.CONTENT_URI, null, null, null, null);
        listUser = MappingHelper.mapCursorToArrayList(dataCursor);

        adapter.updateUserList(listUser);

//        adapter.setOnItemClickCallback(new UserListAdapter.OnItemClickCallback(){
//            @Override
//            public void onItemClicked(User data, View view) {
//                Intent i = new Intent(MainActivity.this, DetailUserActivity.class);
//                TextView nama = (TextView) view.findViewById(R.id.tv_nama);
//                i.putExtra(DetailUserActivity.EXTRA_NAMA, nama.getText().toString());
//                startActivity(i);
//            }
//        });
    }
}
