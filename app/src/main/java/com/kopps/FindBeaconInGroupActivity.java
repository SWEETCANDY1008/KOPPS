package com.kopps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FindBeaconInGroupActivity extends AppCompatActivity {
    protected static final String TAG = "FindBeaconInGroup";
    private List<Beacon> findbeaconList = new ArrayList<>();

    private BeaconServices mService;
    private boolean mBound = false;

        private ServiceConnection mConnection = new ServiceConnection() {
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
        setContentView(R.layout.activity_findbeaconingroup);

        Intent intent = new Intent(FindBeaconInGroupActivity.this, BeaconServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        List<String> filesNameList = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            filesNameList.add(files[i].getName());
            Log.d(TAG, files[i].getName());
        }

        final Spinner spinner = (Spinner) findViewById(R.id.spinners);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, filesNameList);
        spinner.setAdapter(arrayAdapter);

        final TextView textView = (TextView) findViewById(R.id.beacondata);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "끼야야야아아아아악." + mBound, Toast.LENGTH_LONG).show();

                if(mBound) {
                    findbeaconList = mService.getBeaconList();
                    Log.d(TAG, String.valueOf(findbeaconList));

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                            while (!Thread.interrupted()) {
                                try {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (Beacon beacon : findbeaconList) {
                                                String beacondistance = String.valueOf(beacon.getDistance());
                                                textView.setText("ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
                                                Log.d(TAG, "ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) { }
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}



