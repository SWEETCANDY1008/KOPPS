package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupChangeActivity extends AppCompatActivity {
    protected static final String TAG = "GroupChangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchange);

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        ArrayList<String> groupNameList = database.getResult("GROUPTABLE");

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, groupNameList);
        spinner.setAdapter(arrayAdapter);

        Button button = (Button) findViewById(R.id.changebutton);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edittext = (EditText) findViewById(R.id.changegroupname);
                String changename = edittext.getText().toString();

                if(!changename.replace(" ", "").equals("")) {
                    Spinner spinner = (Spinner) findViewById(R.id.spinners);
                    int items = spinner.getAdapter().getCount();

                    if(items > 0) {
                        String selectchangegroup = spinner.getSelectedItem().toString();
                        database.update(selectchangegroup, changename);
                        Toast.makeText(getApplicationContext(), selectchangegroup + "이 " + changename +"으로 수정됐습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "바꿀 그룹이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "그룹 이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

