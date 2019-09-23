package com.kopps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    protected static final String TAG = "SettingActivity";
    private ArrayList<Beacon> findbeaconList = new ArrayList<>();
    private Intent intent;

    private BeaconServices mService;
    private boolean mBound = false;

    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BeaconServices.LocalBinder binder = (BeaconServices.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        intent = new Intent(SettingActivity.this, BeaconServices.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        final Switch sw = (Switch) findViewById(R.id.switchs);

//        sw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if(sw.isChecked()) {
//                        if(mBound) {
//                            findbeaconList = mService.getBeaconList();
//                            Log.d("H", "ON");
//                        }
//                        Toast.makeText(getApplicationContext(), "비콘찾기를 시작합니다.",Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        if(mBound) {
//                            unbindService(mConnection);
//                            mBound = false;
//                            Log.d("H", "OFF");
//                        }
//                        Toast.makeText(getApplicationContext(), "비콘찾기를 종료합니다.",Toast.LENGTH_SHORT).show();
//
//                    }
//                }catch(SecurityException ex){}
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }







}
