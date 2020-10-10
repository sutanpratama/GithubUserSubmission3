package com.purwoto.githubusersubmission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    Switch switch1;
    AlarmReceiver alarmReceiver;
    Button btncheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        alarmReceiver = new AlarmReceiver();

        switch1 = findViewById(R.id.switch1);
        btncheck = findViewById(R.id.btncek);

        if(alarmReceiver.checkAlarm(SettingActivity.this)){
            switch1.setChecked(true);
        }else{
            switch1.setChecked(false);
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alarmReceiver.setAlarm(SettingActivity.this);
                    Toast.makeText(getApplicationContext(), "Reminder Diaktifkan pukul 09:00", Toast.LENGTH_SHORT).show();
                }else{
                    alarmReceiver.stopAlarm(SettingActivity.this);
                    Toast.makeText(getApplicationContext(), "Reminder Dimatikan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmReceiver.checkAlarm(SettingActivity.this);
            }
        });
    }
}