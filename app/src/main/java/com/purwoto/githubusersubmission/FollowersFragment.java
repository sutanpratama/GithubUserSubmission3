package com.purwoto.githubusersubmission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class FollowersFragment extends Fragment {

    private UserAdapter adapter;
    ProgressDialog progressDialog;

    public FollowersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ArrayList<User> listFollowers = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        ListView lvFollowers = view.findViewById(R.id.lv_user_followers);

        adapter = new UserAdapter(getContext());
        lvFollowers.setAdapter(adapter);

        adapter.setUser(listFollowers);

        FollowUserActivity activity = (FollowUserActivity) getActivity();
        String name = activity.getNama();

        Log.e("FollowUserActivity", "INI NAMA DI FRAGMENT  " + name);

        AndroidNetworking.initialize(getActivity());

        AndroidNetworking.get("https://api.github.com/users/" + name + "/followers")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("FollowingFragment", "JALAN INI FAN _ NYA");
                        progressDialog.dismiss();
                        try {
                            for(int i=0;i<response.length();i++){
                                Log.e("asd", "sisnya INI NAMANYA WOI " + response.getJSONObject(i).getString("login"));
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
                                listFollowers.add(user);
                            }
                            adapter.updateUserList(listFollowers);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });

        lvFollowers.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailUserActivity.class);
                TextView nama = (TextView) view.findViewById(R.id.tv_nama);
                i.putExtra(DetailUserActivity.EXTRA_NAMA, nama.getText().toString());
                startActivity(i);
            }
        });
    }
}
