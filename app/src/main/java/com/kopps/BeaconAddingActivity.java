package com.kopps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        ArrayList<String> groupNameList = database.getResult("GROUPTABLE");

//        String path = getCacheDir().toString();
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        List<String> filesNameList = new ArrayList<>();
//
//        for (int i=0; i< files.length; i++) {
//            filesNameList.add(files[i].getName());
//            Log.d(TAG, files[i].getName());
//        }

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, groupNameList);
        spinner.setAdapter(arrayAdapter);

        final EditText editText = (EditText) findViewById(R.id.beaconnickname);

        Button button = (Button) findViewById(R.id.addbeacon);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                beacon_test = (Beacon) intent.getSerializableExtra("beacon_test");

                Log.d(TAG, "BZ" + beacon_test.getId2());
                Log.d(TAG, "BZ" + beacon_test.getDistance());

                String nickname = editText.getText().toString();

                if(nickname.replace(" ", "").equals("")) {
                    Spinner spinner = (Spinner) findViewById(R.id.spinners);
                    int items = spinner.getAdapter().getCount();

                    if (items > 0) {
                        String selectgroup = spinner.getSelectedItem().toString();
                        String id1 = beacon_test.getId1().toString();
                        String id2 = beacon_test.getId2().toString();
                        String id3 = beacon_test.getId3().toString();

                        database.insert(nickname, selectgroup, id1, id2, id3);

                        finish();
                    }
                }
            }
        });
    }

//    public void onBeaconAddGroupClicked(View view) throws IOException {
//        Intent intent = getIntent();
//        beacon_test = (Beacon) intent.getSerializableExtra("beacon_test");
//
//        CachingBeacon cachingBeacon = new CachingBeacon(this);
//
//        Log.d(TAG, "BZ" + beacon_test.getId2());
//        Log.d(TAG, "BZ" + beacon_test.getDistance());
//
//        Spinner spinner = (Spinner) findViewById(R.id.spinners);
//        String selectgroup = spinner.getSelectedItem().toString();
//
//        cachingBeacon.writefile(this, selectgroup, beacon_test);
//        finish();
//    }
}
