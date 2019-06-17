package com.kopps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.altbeacon.beacon.Beacon;
import java.io.Serializable;
import java.util.ArrayList;

public class BeaconAddActivity extends AppCompatActivity {
    protected static final String TAG = "BeaconAddActivity";

    private ArrayList<Beacon> findbeaconList = new ArrayList<>();
    private BeaconServices mService;
    private boolean mBound = false;

    Runnable r = new thread();
    Thread thread = new Thread(r);
    Intent intent;

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
        setContentView(R.layout.activity_beaconadd);

        intent = new Intent(BeaconAddActivity.this, BeaconServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        Button button = (Button) findViewById(R.id.startbutton);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBound) {
                    findbeaconList = mService.getBeaconList();
                    Log.d(TAG, String.valueOf(findbeaconList));

//                    thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
//                            while (!Thread.interrupted()) {
//                                try {
//                                    Thread.sleep(1000);
//                                    logToDisplay();
//                                } catch (InterruptedException e) { }
//                            }
//                        }
//                    });
                    thread.start();
                }
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
            unbindService(mConnection);
            mBound = false;
        }
        thread.interrupt();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();
    }

    private void logToDisplay() {
        final LinearLayout linearLayout = (LinearLayout) BeaconAddActivity.this.findViewById(R.id.linearlayout);
        runOnUiThread(new Runnable() {
            public void run() {
                for (final Beacon beacon : findbeaconList) {
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
                        Log.d(TAG, "ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacon.getDistance());

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


    class thread implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    logToDisplay();

                } catch (InterruptedException e) {
                    thread.interrupt();
                    Log.d(TAG, "interrupt!!!!");
                }
            }
        }
    }





}

