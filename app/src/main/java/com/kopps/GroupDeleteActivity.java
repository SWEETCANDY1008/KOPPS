package com.kopps;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupDeleteActivity extends AppCompatActivity {
    protected static final String TAG = "GroupDeleteActivity";

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
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, filesNameList);
        spinner.setAdapter(arrayAdapter);
    }

    public void onDeleteGroupClicked(View view) throws IOException {
        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        int items = spinner.getAdapter().getCount();

        if(items > 0) {
            String selectdeletegroup = spinner.getSelectedItem().toString();
            CachingBeacon cachingBeacon = new CachingBeacon(this);
            cachingBeacon.deletefile(this, selectdeletegroup);
            finish();
        }
    }
}

