package com.kopps;

import android.location.Location;

public class getDistences {

    public static String mergeLocation(double save_data, double low_data){
        double upper = save_data;
        double lower = low_data;
        String str_lower;
        if(lower < 0){
            upper = ((int)(upper*1000)-1) * 0.001;
            lower = 1000.0 + lower;  // EX) -6.0 -> 994.0
        }
        // 99 -> 099
        if(lower < 100 && lower > 10){
            str_lower = Double.toString(upper) + "0" + Integer.toString((int)lower);
        }
        else if(lower < 10){
            str_lower =  Double.toString(upper) + "00" + Integer.toString((int)lower);
        }
        else{
            str_lower =  Double.toString(upper) + Integer.toString((int)lower);
        }
        return str_lower;
    }

    public double[] getIntersection(double xa, double ya, double ra, double xb, double yb, double rb) {
        // 입력값 : 원 a 의 x좌표, y좌표, 반지름, 원 b의 x좌표, y좌표, 반지름
        // 단위 0.1m
        // 반지름 *10 , 좌표 소수3자리까지 save 4~6 자리에서 계산
        // 소수 6번째 -> 0.1m 단위

        double xi, yi, xi_prime, yi_prime;    // 교점1(xi, yi) 교점2(xi_prime, yi_prime)
        double a, dx, dy, d, h, rx, ry, x2, y2;
        double[] result = {0.0, 0.0, 0.0, 0.0, 0.0};

        double x_save, y_save;
        double x0, y0, x1, y1, r0, r1;

        x_save = (int)(xa*1000) * 0.001;
        y_save = (int)(ya*1000) * 0.001;

        x0 = (int)((xa - x_save) * 1000000);
        y0 = (int)((ya - y_save) * 1000000);
        x1 = (int)((xb - x_save) * 1000000);
        y1 = (int)((yb - y_save) * 1000000);
        r0 = (int)(ra * 10);
        r1 = (int)(rb * 10);

        dx = x1 - x0;
        dy = y1 - y0;

        d = (int)(Math.sqrt(dy*dy + dx*dx) * 10) * 0.1;

        if( d > (r0 + r1) ){
            // 외부에 존재 교점 없음
            result[0] = 0.0;
        }
        else if( d < Math.abs(r0 - r1) ){
            // 내부에 존재 교점 없음
            result[0] = 0.0;
        }
        else if((d == 0) && (r0 == r1)){
            // 일치
            result[0] = 3.0;
        }
        else if(d == r0 + r1){
            // 외접
            result[0] = 1.0;
        }
        else if(d == Math.abs(r0 - r1)){
            // 내접
            result[0] = 1.0;
        }
        else{
            a = ((r0*r0) - (r1*r1) + (d*d) )/ (2.0*d);

            x2 = x0 + (dx * a/d);
            y2 = y0 + (dy * a/d);

            h = Math.sqrt((r0*r0) - (a*a));

            rx = -dy * (h/d);
            ry = dx * (h/d);

            xi = x2 + rx;
            xi_prime = x2 - rx;
            yi = y2 + ry;
            yi_prime = y2 - ry;

            result[0] = 2.0;
            result[1] = Double.parseDouble(mergeLocation(x_save,xi));
            result[2] = Double.parseDouble(mergeLocation(y_save,yi));
            result[3] = Double.parseDouble(mergeLocation(x_save,xi_prime));
            result[4] = Double.parseDouble(mergeLocation(y_save,yi_prime));
        }

        // result[0] : 교점 수 (0, 1, 2, 3)
        // 교점1 (result[1],result[2])
        // 교점2 (result[3],result[4])
        return result;
    }





    public double[] getDistance(double[] L1, double[] L2, double[] L3, double[] L4) {
        double min = 10000.0;
        int save_i = 0;
        double[] save_d = {};

        double d1, d2, d3, d4;
        //  double d = Math.sqrt(Math.pow(x1-x, 2) + Math.pow(y1-y, 2));

        // L1 [ix, iy, jx, jy]
        d1 = Math.sqrt(Math.pow(L1[0]-L1[2], 2) + Math.pow(L1[1]-L1[3], 2));

        // L2 [ix, iy, j`x, j`y]
        d2 = Math.sqrt(Math.pow(L2[0]-L2[2], 2) + Math.pow(L2[1]-L2[3], 2));

        // L3 [i`x, i`y, jx, jy]
        d3 = Math.sqrt(Math.pow(L3[0]-L3[2], 2) + Math.pow(L3[1]-L3[3], 2));

        // L4 [i`x, i`y, j`x, j`y]
        d4 = Math.sqrt(Math.pow(L4[0]-L4[2], 2) + Math.pow(L4[1]-L4[3], 2));

        double[] distance = {d1, d2, d3, d4};

        for(int i=0;i<distance.length;i++) {
            if(distance[i] < min) {
                min = distance[i];
                save_i = i;
            }
        }

        switch(save_i) {
            case 0: save_d = L1; break;
            case 1: save_d = L2; break;
            case 2: save_d = L3; break;
            case 3: save_d = L4; break;
        }

        return save_d;
    }
}

