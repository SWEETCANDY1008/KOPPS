package com.kopps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
import android.util.Log;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
=======
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    protected static final String TAG = "MenuActivity";
>>>>>>> master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    // 버튼이 클릭되면 각 Activity로 화면을 넘겨줌
<<<<<<< HEAD
    public void onRegisterClicked(View view) {
        Intent myIntent = new Intent(this, RegisterActivity.class);
        this.startActivity(myIntent);
    }
=======
    public void onGroupAddClicked(View view) {
        Intent myIntent = new Intent(this, GroupAddActivity.class);
        this.startActivity(myIntent);
    }

    public void onGroupChangeClicked(View view) {
        Intent myIntent = new Intent(this, GroupChangeActivity.class);
        this.startActivity(myIntent);
    }

    public void onGroupDeleteClicked(View view) {
        Intent myIntent = new Intent(this, GroupDeleteActivity.class);
        this.startActivity(myIntent);
    }

    public void onBeaconAddClicked(View view) {
        Intent myIntent = new Intent(this, BeaconAddActivity.class);
        this.startActivity(myIntent);
    }






//    public void onRegisterClicked(View view) {
//        Intent myIntent = new Intent(this, RegisterActivity.class);
//        this.startActivity(myIntent);
//    }


>>>>>>> master
}
