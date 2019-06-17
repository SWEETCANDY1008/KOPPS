package com.kopps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    ArrayList<String> beaconList;

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

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        ArrayList<String> groupNameList = database.getResult("GROUPTABLE");

        final Spinner spinner = (Spinner) findViewById(R.id.group);
        spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, groupNameList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int items = spinner.getAdapter().getCount();

                if(items > 0) {
                    String selectgroup = spinner.getSelectedItem().toString();
                    beaconList = database.getBeaconID1(selectgroup);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button button = (Button) findViewById(R.id.findbeacon);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound) {
                    findbeaconList = mService.getBeaconList();
                    Log.d(TAG, String.valueOf(findbeaconList));
                    thread.start();
                }
            }
        });
    }
// 노티 관련  실행은 alramNotification(닉네임, 구분할 id)
    public void alramNotification(String nick, int noti_id) {
        // 알람을 클릭했을 때 특정 액티비티를 활성화시킬 인텐트 객체 준비
        Intent resultIntent = new Intent(this, FindBeaconInGroupActivity.class);
        // 플래그로 액티비티 실행
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 인텐트 전달자 pendingIntent 에 Intent 전달
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        // Notification - 알림메시지 객체 생성
        Notification arlam = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                // 아이콘 모양 설정
                .setSmallIcon(R.drawable.noti_alram)
                .setContentTitle("신호 끊김")
                .setContentText(nick+" 의 신호가 끊겼습니다.")
                .setTicker("신호 끊김")
                .setAutoCancel(true)
                .build();

        NotificationManager notiManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notiManager.notify(noti_id, arlam);
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

    private void logToDisplay() {
        runOnUiThread(new Runnable() {
            TextView textView = (TextView) findViewById(R.id.beacondata);
            @Override
            public void run() {
                for (Beacon beacon : findbeaconList) {
                    for(String beaconid1 : beaconList) {

                        Log.d(TAG, beacon.getId1().toString() + " | " + beaconid1);

                        if(beacon.getId1().toString().equals(beaconid1)) {
                            String beacondistance = String.valueOf(beacon.getDistance());
                            textView.setText("ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
                            Log.d(TAG, "ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
                        }
                    }
                }
            }
        });
    }

    class thread implements Runnable {
        @Override
        public void run() {
            // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    logToDisplay();

//                    runOnUiThread(new Runnable() {
//                        TextView textView = (TextView) findViewById(R.id.beacondata);
//                        @Override
//                        public void run() {
//                            for (Beacon beacon : findbeaconList) {
//                                for(String beaconid1 : beaconList) {
//                                    if(beacon.getId1().equals(beaconid1)) {
//                                        String beacondistance = String.valueOf(beacon.getDistance());
//                                        textView.setText("ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
//                                        Log.d(TAG, "ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacondistance);
//                                    }
//                                }
//                            }
//                        }
//                    });


                } catch (InterruptedException e) {
                    e.printStackTrace();
                    thread.interrupt();
                    Log.d(TAG, "interrupt!!!!");
                }
            }
        }
    }

}



