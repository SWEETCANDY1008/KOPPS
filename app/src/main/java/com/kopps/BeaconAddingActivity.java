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

        CachingBeacon cachingBeacon = new CachingBeacon(this);

        Log.d(TAG, "BZ" + beacon_test.getId2());
        Log.d(TAG, "BZ" + beacon_test.getDistance());

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        String selectgroup = spinner.getSelectedItem().toString();

        cachingBeacon.writefile(this, selectgroup, beacon_test);
        finish();
    }
}
