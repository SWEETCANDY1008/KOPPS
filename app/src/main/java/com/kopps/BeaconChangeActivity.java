package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class BeaconChangeActivity extends AppCompatActivity {
    protected static final String TAG = "BeaconChangeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beaconchange);

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        ArrayList<String> groupNameList = database.getResult("GROUPTABLE");


        final Spinner groupspinner = (Spinner) findViewById(R.id.groupspinner);
        final Spinner beaconspinner = (Spinner) findViewById(R.id.beaconspinner);
        final Spinner groupchangespinner = (Spinner) findViewById(R.id.changegroupspinner);

        groupspinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, groupNameList));
        groupchangespinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, groupNameList));

        groupspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String groupname = groupspinner.getItemAtPosition(position).toString();
                ArrayList<String> beaconList;
                beaconList = database.getBeacon(groupname);

                beaconspinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, beaconList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 버튼 클릭시 그룹을 바꾸는 SQL을 시행해야 함
        Button button = (Button) findViewById(R.id.groupchangebutton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupname = groupspinner.getSelectedItem().toString();
                String nickname = beaconspinner.getSelectedItem().toString();
                String changegroupname = groupchangespinner.getSelectedItem().toString();

                database.update(groupname, nickname, changegroupname);
                Toast.makeText(getApplicationContext(), groupname + "에서 " + nickname +"(이)가 " + changegroupname + "으로 수정됐습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
