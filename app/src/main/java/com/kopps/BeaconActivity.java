package com.kopps;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.altbeacon.beacon.Beacon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class BeaconActivity extends AppCompatActivity {
    protected static final String TAG = "BeaconActivity";
    ArrayList<String> ID1 = new ArrayList<>();
    ArrayList<String> ID2 = new ArrayList<>();
    ArrayList<String> ID3 = new ArrayList<>();
    ArrayList<String> beaconlist = new ArrayList<>();
    ArrayList<String> lists = new ArrayList<>();

    ArrayList<Double> beacon_data = new ArrayList<>();

    private ArrayAdapter<String> adapter;
    private ArrayList<Beacon> findbeaconList = new ArrayList<>();
    private BeaconServices mService;
    private boolean mBound = false;
    private boolean isStart = true;

    private double longs = 0.0;
    private double latis = 0.0;

    Runnable r = new thread();
    Thread thread;
    Intent intent_beacon;

    String test;
    String group_name;

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
    protected void onResume() {
        super.onResume();
        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        beaconlist.clear();
        lists.clear();
        beaconlist = database.getBeacon(group_name);
        for(String a : beaconlist) {
            test = a + "\n거리 : 15.7m";
            lists.add(test);
        }
        adapter = new ArrayAdapter<String>(BeaconActivity.this, android.R.layout.simple_list_item_1, lists);
        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);

        intent_beacon = new Intent(BeaconActivity.this, BeaconServices.class);
        bindService(intent_beacon, mConnection, Context.BIND_AUTO_CREATE);

        Intent intent = getIntent();
        group_name = intent.getExtras().getString("group");
        beaconlist = database.getBeacon(group_name);
        for(String a : beaconlist) {
            test = a + "\n거리 : 15.7m";
            lists.add(test);
        }

        Button add = (Button) findViewById(R.id.add);
        Button modify = (Button) findViewById(R.id.modify);
        Button delete = (Button) findViewById(R.id.delete);

        final ToggleButton button = (ToggleButton) findViewById(R.id.beaconfind);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(button.isChecked()){
                        thread = new Thread(r);
                        if(mBound) {
                            findbeaconList = mService.getBeaconList();
                        }

                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);

                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);

                        thread.start();
                        isStart = false;
                        Toast.makeText(getApplicationContext(), "비콘찾기를 시작합니다.",Toast.LENGTH_SHORT).show();

                    } else {
                        if(mBound) {
                            unbindService(mConnection);
                            mBound = false;
                        }
                        lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                        thread.interrupt();
                        isStart = true;
                        Toast.makeText(getApplicationContext(), "비콘찾기를 종료합니다.",Toast.LENGTH_SHORT).show();

                    }
                } catch(SecurityException ex){}
            }
        });

        if(beaconlist != null) {
            adapter = new ArrayAdapter<String>(BeaconActivity.this, android.R.layout.simple_list_item_2, lists);

            // Adapter 생성

            ListView listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // 맵 화면으로 이동한다.
                    Intent intent_map = new Intent(BeaconActivity.this, MapsActivity.class);
                    // 계산된 좌표값을 전송해야함
                    double gps[] = {latis, longs};
                    intent_map.putExtra("gps", gps);
                    startActivity(intent_map);
                }
            });
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비콘을 찾는 액티비티로 이동한다. 이동한 액티비티에서는 현재 인식되는 비콘들과 거리가 나온다.
                Intent intent = new Intent(BeaconActivity.this, FindBeaconActivity.class);
                intent.putExtra("group_name", group_name);
                startActivity(intent);
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(BeaconActivity.this);
                dialog.setTitle("비콘 닉네임 수정");
                LinearLayout linearLayout = new LinearLayout(BeaconActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final TextView textView_groupname = new TextView(BeaconActivity.this);
                final EditText edittexts = new EditText(BeaconActivity.this);

                final TextView textView_groupname_change = new TextView(BeaconActivity.this);
                final EditText edittexts_change = new EditText(BeaconActivity.this);

                textView_groupname.setText("기존 비콘 닉네임을 입력하세요");
                textView_groupname_change.setText("변경할 비콘 닉네임을 입력하세요");

                linearLayout.addView(textView_groupname);
                linearLayout.addView(edittexts);

                linearLayout.addView(textView_groupname_change);
                linearLayout.addView(edittexts_change);

                dialog.setView(linearLayout);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String beaconname = edittexts.getText().toString();
                        String beaconname_change = edittexts_change.getText().toString();

                        boolean exists_change = beaconlist.contains(beaconname_change);

                        if (beaconname.length() == 0) {
                            // 공백일때 처리
                            Toast.makeText(getApplicationContext(), "기존 비콘닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
                        } else if(beaconname_change.length() == 0) {
                            Toast.makeText(getApplicationContext(), "바꿀 비콘닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
                        } else if(exists_change) {
                            Toast.makeText(getApplicationContext(), "바꿀 비콘닉네임이 이미 존재합니다..", Toast.LENGTH_LONG).show();
                        } else {
                            database.update(group_name, beaconname, beaconname_change);
                            beaconlist.clear();
                            lists.clear();
                            beaconlist = database.getBeacon(group_name);
                            for(String a : beaconlist) {
                                test = a + "\n거리 : 15.7m";
                                lists.add(test);
                            }
                            adapter = new ArrayAdapter<String>(BeaconActivity.this, android.R.layout.simple_list_item_1, lists);
                            ListView listview = (ListView) findViewById(R.id.listview1);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),  "기존 비콘닉네임 : " + beaconname + " 수정된 비콘닉네임 : " + beaconname_change, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
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


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비콘을 삭제한다.
                AlertDialog.Builder dialog = new AlertDialog.Builder(BeaconActivity.this);
                dialog.setTitle("그룹 삭제");
                dialog.setMessage("삭제할 비콘 닉네임을 입력해 주세요");

                final EditText edittexts = new EditText(BeaconActivity.this);
                dialog.setView(edittexts);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = edittexts.getText().toString();
                        boolean exists = beaconlist.contains(value);

                        if (value.length() == 0 ) {
                            // 공백일 때 처리할 내용
                            Toast.makeText(getApplicationContext(), "비콘 닉네임을 입력해 주세요", Toast.LENGTH_LONG).show();
                        } else if(!exists) {
                            Toast.makeText(getApplicationContext(), "비콘 닉네임이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                        } else if(exists){
                            database.delete(group_name, value);
                            beaconlist.clear();
                            lists.clear();
                            beaconlist = database.getBeacon(group_name);
                            for(String a : beaconlist) {
                                test = a + "\n거리 : 15.7m";
                                lists.add(test);
                            }
                            adapter = new ArrayAdapter<String>(BeaconActivity.this, android.R.layout.simple_list_item_1, lists);
                            ListView listview = (ListView) findViewById(R.id.listview1);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "삭제된 비콘 닉네임 : " + value, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                        }
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

    class thread implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    logToDisplay();
                    Log.d(TAG, "find beacon!!!!");
                    Log.d(TAG, "위도 : " + latis + " 경도 : " + longs);
                } catch (InterruptedException e) {
                    thread.interrupt();
                    Log.d(TAG, "interrupt!!!!");
                }
            }
        }
    }

    private void logToDisplay() {
        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        runOnUiThread(new Runnable() {
            public void run() {
                // 감지되는 비콘들
                findbeaconList = mService.getBeaconList();

                for(final Beacon beacon : findbeaconList) {
                    // 비콘이 감지될때 마다 현재 위치 저장
                    beaconlist.clear();
                    beacon_data.clear();
                    lists.clear();
                    beaconlist = database.getBeacon(group_name);
                    String distance = String.valueOf(beacon.getDistance());

                    // DB에 저장된 비콘들
                    for(String a : beaconlist) {
                        ID1 = database.getBeaconID1(a, group_name);
                        ID2 = database.getBeaconID2(a, group_name);
                        ID3 = database.getBeaconID3(a, group_name);

                        String beacon_id1 = String.valueOf(beacon.getId1());
                        String beacon_id2 = String.valueOf(beacon.getId2());
                        String beacon_id3 = String.valueOf(beacon.getId3());

                        test = a + "\n거리 : " + distance + "m";

//                        계속 측정되고있는 현재 위치
//                        latis;
//                        longs;
//                        바로 직전위치 정보가 필요하다. 직전위치랑 비교해서 동일한 경우 insert 하지 않음
                        double latied;
                        double longed;

                        beacon_data = database.getthreenearbeacongps(a, group_name);

                        if(beacon_data.size() == 0) {
                            database.insert(a, group_name, latis, longs, beacon.getDistance());
                        } else {
                            latied = beacon_data.get(0);
                            longed = beacon_data.get(1);

                            if((latied != latis) && (longed != longs)) {
                                database.insert(a, group_name, latis, longs, beacon.getDistance());
                                Log.d("test", "add gps");
                            } else if((latied == latis) && (longed != longs)) {
                                database.insert(a, group_name, latis, longs, beacon.getDistance());
                                Log.d("test", "add gps");
                            } else if((latied != latis) && (longed == longs)) {
                                database.insert(a, group_name, latis, longs, beacon.getDistance());
                                Log.d("test", "add gps");
                            } else if((latied == latis) && (longed == longs)){
                                Log.d("test", "no add gps");
                            }
                        }

                        Log.d(TAG, "database insert!!!!");
                        lists.add(test);

                        if(!ID1.contains(beacon_id1)
                                && !ID2.contains(beacon_id2)
                                && !ID3.contains(beacon_id3)) {
                            alarmNotification(a, 0);
                            Log.d(TAG, "알람이 떠야 합니다.");
                        }

                    }
                    adapter = new ArrayAdapter<String>(BeaconActivity.this, android.R.layout.simple_list_item_1, lists);
                    ListView listview = (ListView) findViewById(R.id.listview1);
                    listview.setAdapter(BeaconActivity.this.adapter);
                    adapter.notifyDataSetChanged();
                }

                if(findbeaconList.size() == 0) {
                    for(String a : beaconlist) {
                        alarmNotification(a, 0);
                    }
                }




            }
        });
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
//            double altitude = location.getAltitude();   //고도
//            float accuracy = location.getAccuracy();    //정확도
//            String provider = location.getProvider();   //위치제공자

            longs = location.getLongitude(); //경도
            latis = location.getLatitude();   //위도
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    // 노티 관련  실행은 alarmNotification(닉네임, 구분할 id)
    @TargetApi(Build.VERSION_CODES.O)
    public void alarmNotification(String nick, int noti_id) {
        PendingIntent intents_alarm = PendingIntent.getActivity(BeaconActivity.this, 0, new Intent(this, BeaconActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(BeaconActivity.this, notificationChannel.getId());
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.alram_smallcon)
                .setContentTitle("신호 끊김")
                .setContentText(nick + "의 신호가 끊겼습니다.")
                .setTicker("신호 끊김")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(intents_alarm);

        notificationManager.notify(noti_id, notificationBuilder.build());
    }



}