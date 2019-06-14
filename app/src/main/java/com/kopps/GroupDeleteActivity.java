package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;

public class GroupDeleteActivity extends AppCompatActivity {
    protected static final String TAG = "GroupDeleteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupdelete);

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        ArrayList<String> groupNameList = database.getResult("GROUPTABLE");

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, groupNameList);
        spinner.setAdapter(arrayAdapter);

        Button button = (Button) findViewById(R.id.deletebutton);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = (Spinner) findViewById(R.id.spinners);
                int items = spinner.getAdapter().getCount();

                if(items > 0) {
                    String selectdeletegroup = spinner.getSelectedItem().toString();
                    database.delete(selectdeletegroup);

                    finish();
                }
            }
        });
    }
}

