package com.kopps;

import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class BeaconAddActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "BeaconAddActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private ArrayList<Beacon> beaconList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(
                //new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
                new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        // 앱이 실행되고(액티비티가 전환되어 왔을 때) beaconManager 서비스를 실행한다.
        beaconManager.bind(this);
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
//                    addbeaconList.clear();
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                    logToDisplay();
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void logToDisplay() {
        final LinearLayout linearLayout = (LinearLayout) BeaconAddActivity.this.findViewById(R.id.linearlayout);
        runOnUiThread(new Runnable() {
            public void run() {
                for (final Beacon beacon : beaconList) {
                    // 비콘들의 정보가 들어가는 레이아웃
                    LinearLayout beaconlayout = new LinearLayout(BeaconAddActivity.this);

                    // 비콘의 이름이나 기타 텍스트를 작성하기 위한 뷰
                    TextView textviews = new TextView(BeaconAddActivity.this);

                    // 비콘의 태그를 정해주기 위함
                    textviews.setTag("beacon"+beacon.getId2());

                    // 각 비콘 별 추가 버튼 설정
                    Button button = new Button(BeaconAddActivity.this);
                    button.setTag("beaconAddButton"+beacon.getId2());
                    button.setText("추가");


                    // 화면에 들어가는 버튼 레이아웃 설정
                    LinearLayout.LayoutParams pm2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); //레이아웃파라미터 생성
                    pm2.setMargins(0, 10, 10, 10);
                    pm2.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

                    // 화면에 들어가는 레이아웃 설정
                    final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); //레이아웃파라미터 생성
                    pm.weight = 3;
                    pm.width = LinearLayout.LayoutParams.WRAP_CONTENT; //버튼의 너비를 설정(픽셀단위로도 지정가능)
                    pm.height = height; //버튼의 높이를 설정(픽셀단위로도 지정 가능)
                    pm.setMargins(10, 10, 0, 10);
                    pm.gravity = Gravity.CENTER_VERTICAL;



                    // 각 비콘별로 레이아웃을 생성할 때 기존에 존재하는지 확인하기 위함
                    if(linearLayout.findViewWithTag("beacon"+beacon.getId2()) == null || linearLayout.findViewWithTag("beacon"+beacon.getId2()).equals(null)) {
                        button.setLayoutParams(pm2);
                        textviews.setLayoutParams(pm);
                        textviews.setText("major : " + beacon.getId2() + " Distance : " + String.format("%.3f", beacon.getDistance()) + " meters." + beacon.getRssi() + "\n");
                        beaconlayout.addView(textviews);    // 각 비콘별 레이아웃에 텍스트뷰를 추가
                        // 버튼 추가란
                        beaconlayout.addView(button);
                        linearLayout.addView(beaconlayout);
                    } else {
                        TextView textView = linearLayout.findViewWithTag("beacon"+beacon.getId2());
                        textView.setText("major : " + beacon.getId2() + " Distance : " + String.format("%.3f", beacon.getDistance()) + " meters." + beacon.getRssi() + "\n");
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                    }

                    final Beacon beacon_test = beacon;

                    Button buttons = (Button) button.findViewWithTag("beaconAddButton" + beacon.getId2());
                    buttons.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(BeaconAddActivity.this, BeaconAddingActivity.class);
                            myIntent.putExtra("beacon_test", (Serializable) beacon_test);
                            BeaconAddActivity.this.startActivity(myIntent);
                      }
                    });
                }
            }
        });
    }
}

