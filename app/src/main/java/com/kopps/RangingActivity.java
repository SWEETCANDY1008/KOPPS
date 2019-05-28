package com.kopps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class RangingActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private List<Beacon> beaconList = new ArrayList<>();
    private List<String[]> stringbeacon = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(
                //new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
                new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        // 앱이 실행되고(액티비티가 전환되어 왔을 때) beaconManager 서비스를 실행한다.
        beaconManager.bind(this);

        String filename = "internal_cache_data";        // cache에 저장될 파일 이름

        try {
            File cacheDir = getCacheDir();
            File cacheFile = new File(cacheDir.getAbsolutePath(), filename);
            FileInputStream inputStream = new FileInputStream(cacheFile.getAbsolutePath());

            Scanner s = new Scanner(inputStream);
            String text="";
            while(s.hasNext()){
                text+=s.nextLine();

                Log.d(TAG, text);
            }

            inputStream.close();
//                            Log.d(TAG, text);
            Log.d(TAG, String.valueOf(text.length()));
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        }






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

    // beaconManager 서비스가 실행되었을 때 자동적으로 실행된다.
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
        } catch (RemoteException e) {   }
    }



//     오버로딩(BeaconReferenceApplication에서 동일한 이름의 logToDisplay가 사용됨)
            private void logToDisplay() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        TextView textview = (TextView) RangingActivity.this.findViewById(R.id.rangingText);
                        String lists = "";
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

