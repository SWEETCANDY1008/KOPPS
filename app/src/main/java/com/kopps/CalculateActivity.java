package com.kopps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class CalculateActivity extends AppCompatActivity {

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
        ArrayList<ArrayList> list = database.get_all_latis_longs(nick_name, group_name);

        if(list.size() > 1) {
            //  database.delete_CALCULATIED_LOCATION();

            ArrayList<Double> Last_located = list.get(0);
            ArrayList<Double> Previous_located1 = list.get(1);
            ArrayList<Double> Previous_located2 = list.get(2);

            // 최근 좌표와 바로 직전 좌표와의 비교를 위한 배열
            double[] located1 = {
                    Last_located.get(0), Last_located.get(1), Last_located.get(2),
                    Previous_located1.get(0), Previous_located1.get(1), Previous_located1.get(2)
            };

            // 최근 좌표와 바로 직전전 좌표와의 비교를 위한 배열
            double[] located2 = {
                    Last_located.get(0), Last_located.get(1), Last_located.get(2),
                    Previous_located2.get(0), Previous_located2.get(1), Previous_located2.get(2)
            };

            // 최근 좌표와 직전 좌표와의 교점 생성 여부확인
            // 생성되는 좌표(배열) 예) 교점여부, ix, iy, i`x, i`y
            double[] calculated_located1 = calculate.getIntersection(located1[0], located1[1], located1[2], located1[3], located1[4], located1[5]);

            // 최근 좌표와 직전전 좌표와의 교점 생성 여부확인
            // 생성되는 좌표(배열) 예) 교점여부, jx, jy, j`x, j`y
            double[] calculated_located2 = calculate.getIntersection(located2[0], located2[1], located2[2], located2[3], located2[4], located2[5]);



            // 만약 두 경우 모두 교점이 생긴다면
            if(calculated_located1[0] == 2.0 && calculated_located2[0] == 2.0) {
                // i계열과 j계열을 비교해야함
                // 두 점과의 거리
                // [ix, iy, jx, jy] [ix, iy, j`x, j`y]
                // [i`x, i`y, jx, jy] [i`x, i`y, j`x, j`y]
                // 이 4개 중 제일 짧은 거리가 나오는 i계열의 좌표가 비콘이 있는 위치가 된다.
                double[] L1 = {calculated_located1[1], calculated_located1[2], calculated_located2[1], calculated_located2[2]};
                double[] L2 = {calculated_located1[1], calculated_located1[2], calculated_located2[3], calculated_located2[4]};
                double[] L3 = {calculated_located1[3], calculated_located1[4], calculated_located2[1], calculated_located2[2]};
                double[] L4 = {calculated_located1[3], calculated_located1[4], calculated_located2[3], calculated_located2[4]};

                double[] distance = calculate.getDistance(L1, L2, L3, L4);

                System.out.println("distance결과");
                System.out.println(distance[0] + "/" + distance[1]);
            }

            //  database.insert(calculated[0], calculated[1], calculated[2], calculated[3], calculated[4]);

        }




//        setContentView(R.layout.activity_main);





        // 맵 화면으로 이동한다.
        Intent intent_map = new Intent(CalculateActivity.this, MapsActivity.class);
        // 계산된 좌표값을 전송해야함
        double gps[] = {37.550943, 126.990948};
        intent_map.putExtra("gps", gps);
        startActivity(intent_map);
        finish();
    }
}
