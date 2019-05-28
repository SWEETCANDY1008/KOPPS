package com.kopps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import org.altbeacon.beacon.Beacon;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeaconAddingActivity extends AppCompatActivity {
    protected static final String TAG = "BeaconAddingActivity";
    private Beacon beacon_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beaconadding);

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        List<String> filesNameList = new ArrayList<>();

        for (int i=0; i< files.length; i++) {
            filesNameList.add(files[i].getName());
            Log.d(TAG, files[i].getName());
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, filesNameList);
        spinner.setAdapter(arrayAdapter);
    }

    public void onBeaconAddGroupClicked(View view) throws IOException {
        Intent intent = getIntent();
        beacon_test = (Beacon) intent.getSerializableExtra("beacon_test");

        Log.d(TAG, "BZ" + beacon_test.getId2());
        Log.d(TAG, "BZ" + beacon_test.getDistance());

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        String selectdeletegroup = spinner.getSelectedItem().toString();

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i=0; i< files.length; i++) {
            String addinggroupname = files[i].getName();

            if(!selectdeletegroup.equals(addinggroupname)) {
                Toast.makeText(getApplicationContext(), "비콘이 추가되지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {
                String slice = " | ";
                String id1 = "id1 : " + beacon_test.getId1() + slice;
                String id2 = "id2 : " + beacon_test.getId2() + slice;
                String id3 = "id3 : " + beacon_test.getId3();
                String data = id1 + id2 + id3;
//                          data : id1 | id2 | id3

                String filename = addinggroupname;
                File cacheDir = getCacheDir();
                File cacheFile = new File(cacheDir.getAbsolutePath(), filename);
                FileOutputStream fos = new FileOutputStream(cacheFile.getAbsolutePath());
                fos.write(data.getBytes());
                fos.close();

                Toast.makeText(getApplicationContext(), "비콘이 추가되었습니다. 그룹 이름 : " + addinggroupname, Toast.LENGTH_LONG).show();
                finish();
            }
        }



    }
}
