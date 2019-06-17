package com.kopps;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {


    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB생성 및 테이블 생성시 호출되는 메소드
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS GROUPTABLE (groupname text PRIMARY KEY);");
        db.execSQL("CREATE TABLE IF NOT EXISTS BEACONTABLE (nickname TEXT, groupname TEXT, id1 TEXT, id2 TEXT, id3 TEXT, PRIMARY KEY(nickname, groupname));");
        db.execSQL("CREATE TABLE IF NOT EXISTS BEACONREFERENCETABLE (nickname_groupname TEXT PRIMARY KEY, id1 TEXT, id2 TEXT, id3 TEXT, latitude REAL, longitude REAL, rssi INTEGER, time NUMERIC);");
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

    public void update(String groupname, String nickname, String changegroupname) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE BEACONTABLE SET groupname='" + changegroupname + "' WHERE groupname='" + groupname + "' AND nickname='" + nickname + "';");
        db.execSQL("UPDATE BEACONREFERENCETABLE SET nickname_groupname='" + nickname+"_"+changegroupname + "' WHERE nickname_groupname='" + nickname+"_"+groupname + "';");

        db.close();
    }

    public void delete(String groupname, String nickname) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM BEACONTABLE WHERE groupname='" + groupname + "' AND nickname='" + nickname + "' ;");
        db.execSQL("DELETE FROM BEACONREFERENCETABLE WHERE nickname_groupname='" + nickname+"_"+groupname + "';");
        db.close();
    }






    // <---------------------------------------------------->

    // BEACONREFERENCETABLE
    // insert BEACONREFERENCETABLE
    // BEACONREFERENCETABLE에 각 정보를 추가한다.
    public void insert(String nickname, String groupname, String id1, String id2, String id3, double latitude, double longitude, int rssi) {
        String nickname_groupname = nickname + "_" + groupname;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO BEACONREFERENCETABLE(nickname_groupname, id1, id2, id3, latitude, longitude, rssi, time) VALUES('" + nickname_groupname + "', '" + id1 + "', '" + id2 + "', '" + id3 + "', '" + latitude + "', '" + longitude + "', '" + rssi + "', datetime('now','localtime'));");
        db.close();
    }


//    public void update(String nickname, String changenickname) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("UPDATE BEACONTABLE SET nickname=" + changenickname + " WHERE nickname='" + nickname + "';");
////        db.execSQL("INSERT INTO BEACONTABLE VALUES('" + nickname + "', '" + groupname + "', '" + id1 + "', '" + id2 + "', '" + id3 + "');");
//        db.close();
//    }

//    public void update(String nickname_groupname, String id1, String id2, String id3, double latitude, double longitude, int rssi) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
////        db.execSQL("INSERT INTO BEACONTABLE VALUES('" + nickname_groupname + "', '" + id1 + "', '" + id2 + "', '" + id3 + "', '" + latitude + "', '" + longitude + "', '" + rssi + "', datetime('now','localtime'));");
//        db.close();
//    }

//    public void update(String item, int price) {
//        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행의 가격 정보 수정
//        db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
//        db.close();
//    }

//    public void delete(String groupname) {
//        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM GROUPTABLE WHERE groupname='" + groupname + "';");
//        db.execSQL("DELETE FROM BEACONTABLE WHERE groupname='" + groupname + "';");
////        db.execSQL("DELETE FROM MONEYBOOK WHERE item='" + item + "';");
//        db.close();
//    }








    // 원하는 테이블의 모든 정보를 읽어옴
    public ArrayList getResult(String table) {
        ArrayList<String> datalist = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalist.clear();
        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM '"+ table+ "'", null);
        while (cursor.moveToNext()) {
            datalist.add(cursor.getString(0));
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

    public ArrayList getBeaconID1(String groupname) {
        ArrayList<String> datalist = new ArrayList<>();
        // 읽기가 가능하게 DB 열기
        datalist.clear();
        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BEACONTABLE WHERE groupname='" + groupname + "'", null);
        while (cursor.moveToNext()) {
            datalist.add(cursor.getString(2));
        }
        return datalist;
    }

}