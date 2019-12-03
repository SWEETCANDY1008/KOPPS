package com.kopps;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB생성 및 테이블 생성시 호출되는 메소드
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS GROUPTABLE (groupname TEXT PRIMARY KEY);");
        db.execSQL("CREATE TABLE IF NOT EXISTS BEACONTABLE (nickname TEXT, groupname TEXT, id1 TEXT, id2 TEXT, id3 TEXT, PRIMARY KEY(nickname, groupname));");
        db.execSQL("CREATE TABLE IF NOT EXISTS LOCATION (ID INTEGER PRIMARY KEY AUTOINCREMENT, nickname TEXT, groupname TEXT, latitude REAL, longitude REAL, distance REAL, time NUMERIC);");
    }

    // DB 업그레이드를 위한 메소드
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    // GROUPTABLE
    // insert GROUPTABLE
    // grouptable에 groupname을 추가한다.
    public void insert(String groupname) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO GROUPTABLE(groupname) VALUES('" + groupname + "');");
        db.close();
    }

    // update GROUPTABLE
    public void update(String groupname, String changegroupname) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE GROUPTABLE SET groupname='" + changegroupname + "' WHERE groupname='" + groupname + "';");
//        db.execSQL("UPDATE BEACONTABLE SET groupname='" + changegroupname + "' WHERE groupname='" + groupname + "';");
        db.close();
    }

    // delete GROUPTABLE
    // 입력한 항목과 일치하는 행 삭제
    public void delete(String groupname) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM GROUPTABLE WHERE groupname='" + groupname + "';");
    //        db.execSQL("DELETE FROM MONEYBOOK WHERE item='" + item + "';");
        db.close();
    }


    // <---------------------------------------------------->

    // BEACONTABLE
    // insert BEACONTABLE
    // BEACONTABLE에 각 정보를 추가한다.

    public void insert(String nickname, String groupname, String id1, String id2, String id3) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO BEACONTABLE(nickname, groupname, id1, id2, id3) VALUES('" + nickname + "', '" + groupname + "', '" + id1 + "', '" + id2 + "', '" + id3 + "');");
        db.close();
    }

    public void update(String groupname, String nickname, String changenickname) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE BEACONTABLE SET nickname='" + changenickname + "' WHERE groupname='" + groupname + "' AND nickname='" + nickname + "';");
        db.close();
    }

    public void delete(String groupname, String nickname) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM BEACONTABLE WHERE groupname='" + groupname + "' AND nickname='" + nickname + "' ;");
        db.close();
    }

    // LOCATION (ID INTEGER PRIMARY KEY AUTOINCREMENT, nickname TEXT, groupname TEXT, latitude REAL, longitude REAL, distance INTEGER, time NUMERIC)
    // LOCATION TABLE

    public void insert(String nickname, String groupname, double latitude, double longitude, double distance) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO LOCATION(nickname, groupname, latitude, longitude, distance, time) VALUES('" + nickname + "', '" + groupname + "','" + latitude + "', '" + longitude + "', '" + distance + "', datetime('now','localtime'));");
        db.close();
    // nickname TEXT, groupname TEXT, latitude REAL, longitude REAL, distance REAL, time NUMERIC
    }

    public ArrayList getGroup() {
        ArrayList<String> datalist = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalist.clear();
        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM GROUPTABLE", null);
        if(cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                datalist.add(cursor.getString(0));
            }
        } else {
            datalist = null;
        }
        return datalist;
    }


    public ArrayList getBeacon(String groupname) {
        ArrayList<String> datalist = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalist.clear();
        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BEACONTABLE WHERE groupname='" + groupname + "'", null);
        while (cursor.moveToNext()) {
            datalist.add(cursor.getString(0));
        }

        return datalist;
    }

    public ArrayList getBeaconID1(String nickname, String groupname) {
        ArrayList<String> datalist = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalist.clear();
        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BEACONTABLE WHERE nickname='" + nickname + "' AND groupname='" + groupname + "'", null);
        while (cursor.moveToNext()) {
            datalist.add(cursor.getString(2));
        }
        return datalist;
    }

    public ArrayList getBeaconID2(String nickname, String groupname) {
        ArrayList<String> datalist = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalist.clear();
        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BEACONTABLE WHERE nickname='" + nickname + "' AND groupname='" + groupname + "'", null);
        while (cursor.moveToNext()) {
            datalist.add(cursor.getString(3));
        }
        return datalist;
    }

    public ArrayList getBeaconID3(String nickname, String groupname) {
        ArrayList<String> datalist = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalist.clear();
        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BEACONTABLE WHERE nickname='" + nickname + "' AND groupname='" + groupname + "'", null);
        while (cursor.moveToNext()) {
            datalist.add(cursor.getString(4));
        }
        return datalist;
    }
//    insert(String nickname, double latitude, double longitude, double distance)
    public ArrayList getthreenearbeacongps(String nickname, String groupname) {
        ArrayList<Double> datalists = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalists.clear();

        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT latitude, longitude, distance FROM LOCATION WHERE nickname='" + nickname + "' AND groupname='" + groupname + "' ORDER BY time DESC ", null);

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            Double lati = cursor.getDouble(0);
            Double longs = cursor.getDouble(1);
            Double distance = cursor.getDouble(2);

            datalists.add(lati);
            datalists.add(longs);
            datalists.add(distance);

            Log.d("test", datalists.toString());
        } else if (cursor == null && cursor.getCount() == 0) {
            datalists = null;
        }
        return datalists;
    }


    public ArrayList get_all_latis_longs(String nickname, String groupname) {
        ArrayList<ArrayList> datalists = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalists.clear();

        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT latitude, longitude, distance FROM LOCATION WHERE nickname='" + nickname + "' AND groupname='" + groupname + "' ORDER BY time DESC ", null);

        while (cursor.moveToNext()) {
            ArrayList<Double> latis_longs = new ArrayList<>();

            Double lati = cursor.getDouble(0);
            Double longs = cursor.getDouble(1);
            Double distance = cursor.getDouble(2);

            latis_longs.add(lati);
            latis_longs.add(longs);
            latis_longs.add(distance);

            datalists.add(latis_longs);
        }
        return datalists;
    }
}

