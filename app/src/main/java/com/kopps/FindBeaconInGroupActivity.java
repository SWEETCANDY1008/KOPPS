package com.kopps;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.altbeacon.beacon.Beacon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindBeaconInGroupActivity extends AppCompatActivity {
    protected static final String TAG = "FindBeaconInGroup";
    private ArrayList<Beacon> findbeaconList = new ArrayList<>();
    private ArrayList<Double[]> gps_test = new ArrayList<>();

    private double longs = 0.0;
    private double latis = 0.0;
    private double latied = 0.0;
    private double longed = 0.0;

    ToggleButton button;

    ArrayList<String> beaconList;
    ArrayList<String> findnicknamelists;
//    ArrayList<ArrayList> gps;
    ArrayList<Double[]> gps;

    private BeaconServices mService;
    private boolean mBound = false;

    Runnable r = new thread();
    Thread thread;
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

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findbeaconingroup);

        intent = new Intent(FindBeaconInGroupActivity.this, BeaconServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        ArrayList<String> groupNameList = database.getResult("GROUPTABLE");

        final LinearLayout linearLayout = (LinearLayout) FindBeaconInGroupActivity.this.findViewById(R.id.beacondatalayout);

        final Spinner spinner = (Spinner) findViewById(R.id.group);
        spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, groupNameList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int items = spinner.getAdapter().getCount();

                if(items > 0) {
                    String selectgroup = spinner.getSelectedItem().toString();
                    beaconList = database.getBeaconID1("", selectgroup);
                    for(String id1 : beaconList) {
                        findnicknamelists = database.findBeaconNICKNAME(id1);
                    }
                }
                linearLayout.removeAllViewsInLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        button = (ToggleButton) findViewById(R.id.findbeacon);
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
                    Toast.makeText(getApplicationContext(), "비콘찾기를 시작합니다.",Toast.LENGTH_SHORT).show();

                } else {
                    if(mBound) {
                        unbindService(mConnection);
                        mBound = false;
                    }
                    lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                    thread.interrupt();
                    Toast.makeText(getApplicationContext(), "비콘찾기를 종료합니다.",Toast.LENGTH_SHORT).show();

                }
            }catch(SecurityException ex){}
        }
    });
}

//        TextView tv = (TextView) findViewById(R.id.textgps);
//        tv.setText("위치정보 미수신중");
//        final ToggleButton tb = (ToggleButton)findViewById(R.id.gpsbutton);
//        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        tb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if(tb.isChecked()){
//                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
//                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
//                                100, // 통지사이의 최소 시간간격 (miliSecond)
//                                1, // 통지사이의 최소 변경거리 (m)
//                                mLocationListener);
//                    }else{
//                        lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
//                    }
//                }catch(SecurityException ex){
//                }

//
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
//            double longitude = location.getLongitude(); //경도
//            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

            longs = location.getLongitude(); //경도
            latis = location.getLatitude();   //위도

//            longs = longitude;
//            latis = latitude;


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
        PendingIntent intents = PendingIntent.getActivity(FindBeaconInGroupActivity.this, 0, new Intent(getApplicationContext(), FindBeaconInGroupActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(FindBeaconInGroupActivity.this, notificationChannel.getId());
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.alram_smallcon)
                .setContentTitle("신호 끊김")
                .setContentText(nick + "의 신호가 끊겼습니다.")
                .setTicker("신호 끊김")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(intents);

        notificationManager.notify(noti_id, notificationBuilder.build());
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
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
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
        final LinearLayout linearLayout = (LinearLayout) FindBeaconInGroupActivity.this.findViewById(R.id.beacondatalayout);
        runOnUiThread(new Runnable() {
            public void run() {
                ArrayList<String> findbeaconid1 = new ArrayList<>();
                ArrayList<Double[]> gps_lalo = new ArrayList<>();

                final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);

                gps_lalo.clear();
                gps_test.clear();

                findbeaconid1.clear();

                for (String beaconid1 : beaconList) {
                    for (Beacon beacon : findbeaconList) {

                        Log.d("test4","gps | " + gps);
                        // 123444544112344454411234445441123444544112344454411234445441
                        // 이전 DB 좌표랑 비교해서 같으면 저장하지 않고 다르면 DB저장

                        Log.d("test", "lati1 | " + latis);
                        Log.d("test", "long1 | " + longs);

                        if((latied != latis) && (longed != longs)) {
//                            database.insert(닉네임을 넣어야 함, latis, longs, (int) beacon.getDistance());
                            Log.d("test", "add gps");
                        } else if((latied == latis) && (longed != longs)) {
//                            database.insert(닉네임을 넣어야 함, latis, longs, (int) beacon.getDistance());
                            Log.d("test", "add gps");
                        } else if((latied != latis) && (longed == longs)) {
//                            database.insert(닉네임을 넣어야 함, latis, longs, (int) beacon.getDistance());
                            Log.d("test", "add gps");
                        } else if((latied == latis) && (longed == longs)){
                            Log.d("test", "no add gps");
                        }

//                        if((gps = database.getthreenearbeacongps(닉네임을 넣어야 함)) != null) {
//                            gps_test = gps;
//                        }


                        Button button = new Button(FindBeaconInGroupActivity.this);
                        button.setTag("beaconAddButton"+beacon.getId2());
                        button.setText("확인");

                        // 탐지되는 비콘들의 id1 값이 arraylist에 담김
                        findbeaconid1.add(beacon.getId1().toString());

                        Log.d(TAG, beacon.getId1().toString() + " | " + beaconid1);
                        if (beacon.getId1().toString().equals(beaconid1)) {
                            // 비콘들의 정보가 들어가는 레이아웃
                            LinearLayout beaconlayout = new LinearLayout(FindBeaconInGroupActivity.this);

                            // 비콘의 이름이나 기타 텍스트를 작성하기 위한 뷰
                            TextView textviews = new TextView(FindBeaconInGroupActivity.this);

                            // 비콘의 태그를 정해주기 위함
                            textviews.setTag("beacon" + beacon.getId2());

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
                            if (linearLayout.findViewWithTag("beacon" + beacon.getId2()) == null || linearLayout.findViewWithTag("beacon" + beacon.getId2()).equals(null)) {
                                textviews.setText("major : " + beacon.getId2() + " Distance : " + String.format("%.3f", beacon.getDistance()) + " meters." + beacon.getRssi() + "\n");
                                textviews.setLayoutParams(pm);
                                beaconlayout.addView(textviews);    // 각 비콘별 레이아웃에 텍스트뷰를 추가

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent myIntent = new Intent(FindBeaconInGroupActivity.this, MapsActivity.class);
                                        myIntent.putExtra("gps_test", (Serializable) gps_test);
                                        // 데이터 베이스 내용을 받아야 함
                                        FindBeaconInGroupActivity.this.startActivity(myIntent);
                                    }
                                });

                                beaconlayout.addView(button);
                                linearLayout.addView(beaconlayout);
                            } else {
                                TextView textView = linearLayout.findViewWithTag("beacon" + beacon.getId2());
                                textView.setText("major : " + beacon.getId2() + " Distance : " + String.format("%.3f", beacon.getDistance()) + " meters." + beacon.getRssi() + "\n");
                                textviews.setLayoutParams(pm);
                                Log.d(TAG, "ID1 : " + beacon.getId1() + " ID2 : " + beacon.getId2() + " ID3 : " + beacon.getId3() + " DISTANCE : " + beacon.getDistance());

                                textView.setGravity(Gravity.CENTER_VERTICAL);
                            }
                        }

//                        findbeaconid1 와 DB상의 id1을 비교해서 없으면 알람
//                        비콘이 아무것도 없으면 아에 동작을 안함. 1개라도 있어야 함
//                        무슨 말이냐면 비콘이 0개이면 없다고 알람이 떠야 하지만 뜨지를 않는다.

                        for (String id : findbeaconid1) {
                            if (!beaconList.contains(id)) {
                                for (String nickname : findnicknamelists) {
                                    alarmNotification(nickname, 1);
                                }
                            }
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    thread.interrupt();
                    Log.d(TAG, "interrupt!!!!");
                }
            }
        }

    }
}



