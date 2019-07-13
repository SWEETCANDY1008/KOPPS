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

public class BeaconDeleteActivity extends AppCompatActivity {
    protected static final String TAG = "BeaconDeleteActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacondelete);

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        ArrayList<String> groupNameList = database.getResult("GROUPTABLE");

        final Spinner groupspinner = (Spinner) findViewById(R.id.groupspinner);
        final Spinner beaconspinner = (Spinner) findViewById(R.id.beaconspinner);

        groupspinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, groupNameList));

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

        Button button = (Button) findViewById(R.id.beacondeletebutton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시 DB에 해당 그룹의 비콘 삭제
                String groupname = groupspinner.getSelectedItem().toString();
                String nickname = beaconspinner.getSelectedItem().toString();

                database.delete(groupname, nickname);
                Toast.makeText(getApplicationContext(), groupname + "에서 " + nickname +"(이)가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish();


//                int items = spinner.getAdapter().getCount();
//
//                if(items > 0) {
//                    String selectdeletegroup = spinner.getSelectedItem().toString();
//                    database.delete(selectdeletegroup);
//                    Toast.makeText(getApplicationContext(), selectdeletegroup + "가 삭제됐습니다.", Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), "삭제할 그룹이 없습니다.", Toast.LENGTH_LONG).show();
//                }




            }
        });
    }
}
