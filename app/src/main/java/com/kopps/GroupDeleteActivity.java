package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void onDeleteGroupClicked(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        String selectdeletegroup = spinner.getSelectedItem().toString();

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i=0; i< files.length; i++) {
            String deletegroupname = files[i].getName();

            if(!selectdeletegroup.equals(deletegroupname)) {
                Toast.makeText(getApplicationContext(), "그룹이 삭제되지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {
                files[i].delete();
                Toast.makeText(getApplicationContext(), "그룹이 삭제되었습니다. 그룹 이름 : " + deletegroupname, Toast.LENGTH_LONG).show();
                finish();
            }
        }



    }

}

