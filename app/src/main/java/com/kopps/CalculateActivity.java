package com.kopps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class CalculateActivity extends AppCompatActivity {
    ArrayList<ArrayList> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);
        getDistences calculate = new getDistences();

        Intent intent = getIntent();
        String[] group_nickname = intent.getStringArrayExtra("group_nickname");
        int idx = group_nickname[1].indexOf("\n");

        String group_name = group_nickname[0];
        String nick_name = group_nickname[1].substring(0, idx);

        // db에서 해당 그룹 닉네임의 모든 위치정보를 가져옴
        // 형태 : ArrayList 내부에 ArrayList
        list = database.get_all_latis_longs(nick_name, group_name);

        if(list.size() > 1) {
            for(int i=0;i<list.size();i++) {
//            database.delete_CALCULATIED_LOCATION();

                ArrayList<Double> located1 = list.get(i);
                ArrayList<Double> located2 = list.get(i+1);

                double xa = located1.get(0);
                double ya = located1.get(1);
                double ra = located1.get(2);

                double xb = located2.get(0);
                double yb = located2.get(1);
                double rb = located2.get(2);

                double[] calculated = calculate.getIntersection(xa, ya, ra, xb, yb, rb);

                database.insert(calculated[0], calculated[1], calculated[2], calculated[3], calculated[4]);
            }
        }




//        setContentView(R.layout.activity_main);





        // 맵 화면으로 이동한다.
//        Intent intent_map = new Intent(CalculateActivity.this, MapsActivity.class);
        // 계산된 좌표값을 전송해야함
//        double gps[] = {latis, longs};
//        intent_map.putExtra("gps", gps);
//        startActivity(intent_map);

    }
}
