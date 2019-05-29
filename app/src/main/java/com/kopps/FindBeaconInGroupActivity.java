package com.kopps;

import android.os.Bundle;
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
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FindBeaconInGroupActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "FindBeaconInGroup";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private List<Beacon> beaconList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findbeaconingroup);

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(
                //new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
                new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        // 앱이 실행되고(액티비티가 전환되어 왔을 때) beaconManager 서비스를 실행한다.
        beaconManager.bind(this);

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
                Toast.makeText(getApplicationContext(), "끼야야야아아아아악.", Toast.LENGTH_LONG).show();
                onBeaconServiceConnect();
                logToDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 앱이 종료되었을때 beaconManager 서비스를 종료한다.
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                        // 여기에 비콘별로
                    }
                    logToDisplay();
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {

        }
    }



    private void logToDisplay() {
        runOnUiThread(new Runnable() {
            public void run() {
                String lists = "";
                TextView textview = (TextView) FindBeaconInGroupActivity.this.findViewById(R.id.beacondata);

                for (Beacon beacon : beaconList) {
                    lists = lists + "major : " + beacon.getId2() + " / minor : " + beacon.getId3() + " / 거리 : " + String.format("%.3f", beacon.getDistance()) + " / meters." + beacon.getRssi() + "\n";
                    Log.d(TAG, "major : " + beacon.getId2() + " Distance : " + String.format("%.3f", beacon.getDistance())+ " meters away." + beacon.getRssi() + "\n");
                    Log.d(TAG, beacon.toString());
                }
                textview.setText(lists + "\n" + "===================================================\n");
            }
        });
    }
}

