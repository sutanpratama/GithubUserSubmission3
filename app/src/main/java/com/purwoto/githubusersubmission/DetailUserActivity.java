package com.purwoto.githubusersubmission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.purwoto.githubusersubmission.db.DatabaseHelper;
import com.purwoto.githubusersubmission.share.Share;
import com.squareup.picasso.Picasso;

public class DetailUserActivity extends AppCompatActivity {

    public static final String EXTRA_NAMA = "extra_nama";
    public static final String EXTRA_AVATAR_URL = "extra_avatar_url";
    public static final String EXTRA_URL = "extra_url";

    private TextView tvDetailNama,
                     tvDetailUsername,
                     tvDetailBlog,
                     tvDetailLocation,
                     tvDetailRepository,
                     tvDetailFollower,
                     tvDetailFollowing,
                     tvFavorit;

    private ImageView ivDetailAvatar,
                      imgLineIcon,
                      imgWhatsappIcon;

    ProgressDialog progressDialog;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        myDb = new DatabaseHelper(this);

        LinearLayout followBox = findViewById(R.id.follow_box);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();

        tvDetailNama = findViewById(R.id.tv_nama_detail);
        tvDetailUsername = findViewById(R.id.tv_username_detail);
        tvDetailBlog = findViewById(R.id.tv_detail_blog);
        tvDetailLocation = findViewById(R.id.tv_detail_location);
        tvDetailRepository = findViewById(R.id.tv_detail_repository);
        tvDetailFollower = findViewById(R.id.tv_detail_follower);
        tvDetailFollowing = findViewById(R.id.tv_detail_following);
        tvFavorit = findViewById(R.id.tv_favorit);

        ivDetailAvatar = findViewById(R.id.img_detail);
        imgLineIcon = findViewById(R.id.img_line_icon);
        imgWhatsappIcon = findViewById(R.id.img_line_whatsapp);

        final String nama = getIntent().getStringExtra(EXTRA_NAMA);
        final String avatar_url = getIntent().getStringExtra(EXTRA_AVATAR_URL);
        final String url = getIntent().getStringExtra(EXTRA_URL);

        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.get("https://api.github.com/users/" + nama)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(User.class, new ParsedRequestListener<User>() {
                    @Override
                    public void onResponse(User user) {
                        progressDialog.dismiss();
                        tvDetailNama.setText(user.getName());
                        tvDetailUsername.setText(user.getLogin());
                        tvDetailBlog.setText(user.getBlog());
                        tvDetailLocation.setText(user.getLocation());
                        tvDetailRepository.setText(Integer.toString(user.getPublic_repos()));
                        tvDetailFollower.setText(Integer.toString(user.getFollowers()));
                        tvDetailFollowing.setText(Integer.toString(user.getFollowing()));

                        Picasso.get().load(user.getAvatar()).into(ivDetailAvatar);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });

        followBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailUserActivity.this, FollowUserActivity.class);
                i.putExtra(DetailUserActivity.EXTRA_NAMA, nama);
                startActivity(i);
            }
        });

        imgLineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String appName = "jp.naver.line.android";
                    final boolean isAppInstalled = Share.isAppAvailable(getApplicationContext(), appName);
                    if (isAppInstalled) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://line.me/R/msg/text/?ayo%21%20kunjungi%20github.com%2F" + nama));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("ERROR LINE", e.toString());
                    Toast.makeText(getApplicationContext(), "Line not installed in your device", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgWhatsappIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String appName = "com.whatsapp";
                    final boolean isAppInstalled = Share.isAppAvailable(getApplicationContext(), appName);
                    if (isAppInstalled) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("whatsapp://send?text=ayo%21%20kunjungi%20github.com%2F" + nama));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("ERROR LINE", e.toString());
                    Toast.makeText(getApplicationContext(), "Whatsapp not installed in your device", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(myDb.checkFavorit(nama)){
            tvFavorit.setText("Telah Ditambahkan Ke Favorit!");
            tvFavorit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDb.deleteData(nama);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }else{
            tvFavorit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDb.insertData(nama, avatar_url, url);

                    TextView tv = findViewById(R.id.tv_favorit);
                    tv.setText("Telah Ditambahkan Ke Favorit!");

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DetailUserActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
