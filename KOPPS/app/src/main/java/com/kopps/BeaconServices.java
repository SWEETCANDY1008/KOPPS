package com.kopps;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;


public class BeaconServices extends Service implements BeaconConsumer {
    protected static final String TAG = "BeaconServices";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private ArrayList<Beacon> beaconList = new ArrayList<>();

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        BeaconServices getService() {
            return BeaconServices.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(
                //new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
                new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        // 앱이 실행되고(액티비티가 전환되어 왔을 때) beaconManager 서비스를 실행한다.
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 앱이 종료되었을때 beaconManager 서비스를 종료한다.
        beaconManager.unbind(this);
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
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    // 비콘 리스트를 반환해 줄 메소드
    public ArrayList<Beacon> getBeaconList() {
        return beaconList;
    }
}
