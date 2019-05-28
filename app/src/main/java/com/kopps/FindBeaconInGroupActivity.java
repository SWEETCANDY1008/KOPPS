package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindBeaconInGroupActivity extends AppCompatActivity {
    protected static final String TAG = "FindBeaconInGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupdelete);

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


}
