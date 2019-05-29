package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class GroupAddActivity extends AppCompatActivity {
    protected static final String TAG = "GroupAddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadd);
    }

    public void onAddGroupClicked(View view) throws IOException {
        // 중복 검사가 필요함
        CachingBeacon cachingBeacon = new CachingBeacon(this);
        EditText editText = (EditText) findViewById(R.id.groupname);

        String filename = editText.getText().toString();

        cachingBeacon.makefile(this, filename);
        finish();
    }
}
