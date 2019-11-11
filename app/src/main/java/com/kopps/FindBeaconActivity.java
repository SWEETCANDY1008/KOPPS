package com.kopps;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.altbeacon.beacon.Beacon;

import java.io.Serializable;
import java.util.ArrayList;

public class FindBeaconActivity extends AppCompatActivity {
    protected static final String TAG = "FindBeaconActivity";
    String group_name = null;

    private ArrayList<Beacon> findbeaconList = new ArrayList<>();
    private BeaconServices mService;
    private boolean mBound = false;
    boolean isStart = true;

    Runnable r = new thread();
    Thread thread;
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
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_beacon);

        Intent intented = getIntent();
        group_name = intented.getExtras().getString("group_name");

        intent = new Intent(FindBeaconActivity.this, BeaconServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        final ToggleButton button = (ToggleButton) findViewById(R.id.startbutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (button.isChecked()) {
                        thread = new Thread(r);
                        if (mBound) {
                            findbeaconList = mService.getBeaconList();
                        }
                        thread.start();
                        isStart = false;
                        Toast.makeText(getApplicationContext(), "비콘찾기를 시작합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mBound) {
                            unbindService(mConnection);
                            mBound = false;
                        }
                        thread.interrupt();
                        isStart = true;
                        Toast.makeText(getApplicationContext(), "비콘찾기를 종료합니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (SecurityException ex) {
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
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
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
        final LinearLayout linearLayout = (LinearLayout) FindBeaconActivity.this.findViewById(R.id.linearlayout);
            runOnUiThread(new Runnable() {
            public void run() {
                for (final Beacon beacon : findbeaconList) {
                    // 비콘들의 정보가 들어가는 레이아웃
                    LinearLayout beaconlayout = new LinearLayout(FindBeaconActivity.this);

                    // 비콘의 이름이나 기타 텍스트를 작성하기 위한 뷰
                    TextView textviews = new TextView(FindBeaconActivity.this);

                    // 비콘의 태그를 정해주기 위함
                    textviews.setTag("beacon"+beacon.getId2());

                    // 각 비콘 별 추가 버튼 설정
                    Button button = new Button(FindBeaconActivity.this);
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
                        textviews.setText("major : " + beacon.getId2() + " Distance : " + String.format("%.3f", beacon.getDistance()) + " meters." + beacon.getRssi() + "\n");
                        textviews.setLayoutParams(pm);
                        button.setLayoutParams(pm2);
                        beaconlayout.addView(textviews);    // 각 비콘별 레이아웃에 텍스트뷰를 추가
                        // 버튼 추가
                        beaconlayout.addView(button);
                        linearLayout.addView(beaconlayout);
                    } else {
                        TextView textView = linearLayout.findViewWithTag("beacon"+beacon.getId2());
                        textView.setText("major : " + beacon.getId2() + " Distance : " + String.format("%.3f", beacon.getDistance()) + " meters." + beacon.getRssi() + "\n");
                        textviews.setLayoutParams(pm);
                        Log.d(TAG, "ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacon.getDistance());
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                    }

                    Button buttons = (Button) button.findViewWithTag("beaconAddButton" + beacon.getId2());
                    buttons.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final DataBase database = new DataBase(getApplicationContext(), "kopps.db", null, 1);

                            AlertDialog.Builder dialog = new AlertDialog.Builder(FindBeaconActivity.this);
                            dialog.setTitle("비콘 별명 저장");
                            dialog.setMessage("비콘의 별명을 입력해 주세요");

                            final EditText edittexts = new EditText(FindBeaconActivity.this);
                            dialog.setView(edittexts);

                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String value = edittexts.getText().toString();

                                    // 별명이 이미 존재하는지 확인해야 함


                                    Toast.makeText(getApplicationContext(), "별명 : " + value, Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    // 비콘 별명을 받으면 그 비콘의 정보와 별명을 데이터베이스에 저장하는 코드가 들어가야 함
                                    database.insert(value, group_name, beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString());
                                    finish();
                                }
                            });

                            dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });

                    // 찾아지는 모든 비콘들에 대하여 rssi(거리값)과 각종정보, 그리고 현재 디바이스의 위치 정보가 저장되어야 함 디바이스 위치는 중복이 되어선 안된다.




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

