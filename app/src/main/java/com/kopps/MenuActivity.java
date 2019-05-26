package com.kopps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    // 버튼이 클릭되면 각 Activity로 화면을 넘겨줌
    public void onRegisterClicked(View view) {
        Intent myIntent = new Intent(this, RegisterActivity.class);
        this.startActivity(myIntent);
    }
}
