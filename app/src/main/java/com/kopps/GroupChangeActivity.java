package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupChangeActivity extends AppCompatActivity {
    protected static final String TAG = "GroupChangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchange);

        List<String> filesNameList = new ArrayList<>();

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            filesNameList.add(files[i].getName());
            Log.d(TAG, files[i].getName());
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, filesNameList);
        spinner.setAdapter(arrayAdapter);
    }

    public void onChangeGroupClicked(View view) throws IOException {
        CachingBeacon cachingBeacon = new CachingBeacon(this);

        EditText edittext = (EditText) findViewById(R.id.changegroupname);
        String changename = edittext.getText().toString();

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        int items = spinner.getAdapter().getCount();

        if(items > 0) {
            String selectchangegroup = spinner.getSelectedItem().toString();
            cachingBeacon.changefile(this, changename, selectchangegroup);
            finish();
        }
    }
}

