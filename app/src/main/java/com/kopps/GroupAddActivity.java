package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class GroupAddActivity extends AppCompatActivity {
    protected static final String TAG = "GroupAddActivity";
    ArrayList<String> grouplists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadd);

        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        final EditText editText = (EditText) findViewById(R.id.groupname);


        Button button = (Button) findViewById(R.id.addbutton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupname = editText.getText().toString();

                grouplists = database.getResult("GROUPTABLE");

                boolean exists = grouplists.contains(groupname);

                if (groupname.length() == 0 ) {
                    // 공백일 때 처리할 내용
                    Toast.makeText(getApplicationContext(), "그룹 이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                } else if(exists) {
                    Toast.makeText(getApplicationContext(), "그룹이 이미 존재합니다.", Toast.LENGTH_LONG).show();
                    finish();
                } else if(!exists){
                    database.insert(groupname);
                    Toast.makeText(getApplicationContext(), "그룹이 추가되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                Log.d(TAG, String.valueOf(database.getResult("GROUPTABLE")));
            }
        });
    }
}
