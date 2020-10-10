package com.purwoto.githubusersubmission;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.purwoto.githubusersubmission.db.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private UserListAdapter adapter;
    ProgressDialog progressDialog;
    boolean isSearch = false;
    DatabaseHelper myDb;
    String savedQuery;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        final ArrayList<User> listUser = new ArrayList<>();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        final RecyclerView recyclerView = findViewById(R.id.rv_user);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserListAdapter(listUser);
        recyclerView.setAdapter(adapter);

        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.get("https://api.github.com/users")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();
                        try {
                            for(int i=0;i<response.length();i++){
                                User user = new User();
                                user.setLogin(response.getJSONObject(i).getString("login"));
                                user.setName("");
                                user.setBlog("");
                                user.setLocation("");
                                user.setEmail("");
                                user.setAvatar(response.getJSONObject(i).getString("avatar_url"));
                                user.setUrl(response.getJSONObject(i).getString("url"));
                                user.setFollowers(0);
                                user.setFollowing(0);

                                if(myDb.checkFavorit(response.getJSONObject(i).getString("login"))){
                                    user.setFavorit(true);
                                }else{
                                    user.setFavorit(false);
                                }

                                listUser.add(user);
                            }
                            adapter.updateUserList(listUser);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });

        adapter.setOnItemClickCallback(new UserListAdapter.OnItemClickCallback(){
            @Override
            public void onItemClicked(User data, View view) {
                Intent i = new Intent(MainActivity.this, DetailUserActivity.class);
                TextView nama = (TextView) view.findViewById(R.id.tv_nama);
                TextView url = (TextView) view.findViewById(R.id.tv_url);
                TextView avatar_url = (TextView) view.findViewById(R.id.tv_avatar_url);
                i.putExtra(DetailUserActivity.EXTRA_NAMA, nama.getText().toString());
                i.putExtra(DetailUserActivity.EXTRA_URL, url.getText().toString());
                i.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, avatar_url.getText().toString());
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.e("asd", "INI HABIS ORIENTATSI " + savedQuery);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && isSearch){
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();

            AndroidNetworking.get("https://api.github.com/search/users?q=" + savedQuery)
                    .addQueryParameter("limit", "10")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(User.class, new ParsedRequestListener<User>() {
                        @Override
                        public void onResponse(User user) {
                            progressDialog.dismiss();
                            ArrayList<User> newList = new ArrayList<>();
                            for(User temp : user.getItems()){
                                newList.add(temp);
                            }

                            adapter.updateUserList(newList);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && isSearch){
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();

            AndroidNetworking.get("https://api.github.com/search/users?q=" + savedQuery)
                    .addQueryParameter("limit", "10")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(User.class, new ParsedRequestListener<User>() {
                        @Override
                        public void onResponse(User user) {
                            progressDialog.dismiss();
                            ArrayList<User> newList = new ArrayList<>();
                            for(User temp : user.getItems()){
                                newList.add(temp);
                            }

                            adapter.updateUserList(newList);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.search:
                return true;
            case R.id.action_change_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return true;
            case R.id.favorite_icon:
                Cursor res = myDb.getAllData();
                String message = "";
                if(res.getCount() == 0){

                }else{
                    Intent i = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivity(i);
                }
                return true;
            case R.id.setting_icon:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        this.menu = menu;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Cari User ..");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    savedQuery = query;
                    isSearch = true;
                    AndroidNetworking.get("https://api.github.com/search/users?q=" + query)
                            .addQueryParameter("limit", "10")
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsObject(User.class, new ParsedRequestListener<User>() {
                                @Override
                                public void onResponse(User user) {
                                    ArrayList<User> newList = new ArrayList<>();
                                    for(User temp : user.getItems()){
                                        if(myDb.checkFavorit(temp.getLogin())){
                                            temp.setFavorit(true);
                                        }else{
                                            temp.setFavorit(false);
                                        }

                                        newList.add(temp);
                                    }

                                    adapter.updateUserList(newList);
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });

                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.equals("")){
                        AndroidNetworking.get("https://api.github.com/users")
                                .setPriority(Priority.LOW)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        try {

                                            ArrayList<User> listUser = new ArrayList<>();

                                            for(int i=0;i<response.length();i++){
                                                User user = new User();
                                                user.setLogin(response.getJSONObject(i).getString("login"));
                                                user.setName("");
                                                user.setBlog("");
                                                user.setLocation("");
                                                user.setEmail("");
                                                user.setAvatar(response.getJSONObject(i).getString("avatar_url"));
                                                user.setUrl(response.getJSONObject(i).getString("url"));
                                                user.setFollowers(0);
                                                user.setFollowing(0);
                                                if(myDb.checkFavorit(response.getJSONObject(i).getString("login"))){
                                                    user.setFavorit(true);
                                                }else{
                                                    user.setFavorit(false);
                                                }
                                                listUser.add(user);
                                            }
                                            adapter.updateUserList(listUser);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                    }
                                });
                    }
                    return true;
                }
            });
        }
        return true;
    }
}
