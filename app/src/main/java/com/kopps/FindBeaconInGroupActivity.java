package com.kopps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.altbeacon.beacon.Beacon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindBeaconInGroupActivity extends AppCompatActivity {
    protected static final String TAG = "FindBeaconInGroup";
    private List<Beacon> findbeaconList = new ArrayList<>();

    private BeaconServices mService;
    private boolean mBound = false;

    Runnable r = new thread();
    Thread thread = new Thread(r);
    Intent intent;

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
        setContentView(R.layout.activity_findbeaconingroup);

        intent = new Intent(FindBeaconInGroupActivity.this, BeaconServices.class);
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "끼야야야아아아아악." + mBound, Toast.LENGTH_LONG).show();

                if(mBound) {
                    findbeaconList = mService.getBeaconList();
                    Log.d(TAG, String.valueOf(findbeaconList));
                    thread.start();
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
        if(mBound) {
            mBound = false;
            unbindService(mConnection);
        }
        thread.interrupt();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mBound) {
            mBound = false;
            unbindService(mConnection);
        }
        super.onDestroy();
    }

    class thread implements Runnable {

        @Override
        public void run() {
            // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        TextView textView = (TextView) findViewById(R.id.beacondata);
                        @Override
                        public void run() {
                            for (Beacon beacon : findbeaconList) {
                                String beacondistance = String.valueOf(beacon.getDistance());
                                textView.setText("ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
                                Log.d(TAG, "ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    thread.interrupt();
                    Log.d(TAG, "interrupt!!!!");
                }
            }
        }
    }

}



