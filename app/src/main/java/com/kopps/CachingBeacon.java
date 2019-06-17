package com.kopps;

<<<<<<< HEAD
import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

class CachingBeacon extends Activity {
    protected static final String TAG = "RangingActivity";
    String filename = "internal_cache_data";        // cache에 저장될 파일 이름
    String data = "Go ahead";                       // internal_cache_data파일에 저장될 내용

    void write() {
        try {
            File cacheDir = getCacheDir();
            File cacheFile = new File(cacheDir.getAbsolutePath(), filename);
            FileOutputStream fos = new FileOutputStream(cacheFile.getAbsolutePath());
            fos.write(data.getBytes());
            fos.close();
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }

    void read() {
        try {
            File cacheDir = getCacheDir();
            File cacheFile = new File(cacheDir.getAbsolutePath(), filename);
            FileInputStream inputStream = new FileInputStream(cacheFile.getAbsolutePath());

            Scanner s = new Scanner(inputStream);
            String text="";
            while(s.hasNext()){
                text+=s.nextLine();
            }
            inputStream.close();
            Log.d(TAG, text);


        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        }

=======
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class CachingBeacon extends AppCompatActivity {
    protected static final String TAG = "CachingBeacon";
    Context context;

    public CachingBeacon(Context co) {
        context = co;
    }

    public void makefile(Context context, String filename) throws IOException {
        File cacheDir = context.getCacheDir();
        File cacheFile = new File(cacheDir.getAbsolutePath(), filename);

        if (filename.length() == 0 ) {
            // 공백일 때 처리할 내용
            Toast.makeText(context, "그룹 이름을 입력해 주세요", Toast.LENGTH_LONG).show();
        } else if(cacheFile.exists()) {
            Toast.makeText(context, "그룹이 이미 존재합니다. 그룹 이름 : " + cacheFile.getName(), Toast.LENGTH_LONG).show();
        } else if(!cacheFile.exists()){
            cacheFile.createNewFile();
            Toast.makeText(context, "그룹이 추가되었습니다. 그룹 이름 : " + filename, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void changefile(Context context, String changename, String selectchangegroup) throws IOException {
        File cachedir = context.getCacheDir();
        File cachefile = new File(cachedir, selectchangegroup);
        File renamecachefile = new File(cachedir, changename);

        if (changename.length() == 0) {
            // 공백일 때 처리할 내용
            Toast.makeText(context, "변경할 이름을 입력해 주세요", Toast.LENGTH_LONG).show();
        } else if (renamecachefile.exists()) {
            Toast.makeText(context, "그룹이 이미 존재합니다. 그룹 이름 : " + cachefile.getName(), Toast.LENGTH_LONG).show();
        } else if (!renamecachefile.exists()) {
            cachefile.renameTo(renamecachefile);
            Toast.makeText(context, "그룹이 변경 되었습니다. 그룹 이름 : " + changename, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void deletefile(Context context, String deletefilename) throws IOException {
        File cacheDir = context.getCacheDir();
        File deletecacheFile = new File(cacheDir.getAbsolutePath(), deletefilename);

        if (deletecacheFile.exists()) {
            deletecacheFile.delete();
            Toast.makeText(context, "그룹이 삭제되었습니다. 그룹 이름 : " + deletefilename, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "그룹이 삭제되지 않았습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void writefile(Context context, String filename, Beacon beacon) throws IOException {
            File cacheDir = context.getCacheDir();
            File cacheFile = new File(cacheDir.getAbsolutePath(), filename);
            FileInputStream inputStream = new FileInputStream(cacheFile.getAbsolutePath());

            if(cacheFile.exists()) {
                Scanner s = new Scanner(inputStream);
                String slice = " | ";
                String id1 = "id1 : " + beacon.getId1() + slice;
                String id2 = "id2 : " + beacon.getId2() + slice;
                String id3 = "id3 : " + beacon.getId3();
                String data = id1 + id2 + id3;

                while(s.hasNext()){
                    data+=s.nextLine();
                    Log.d(TAG, data);
                }

                inputStream.close();
                Toast.makeText(context, "비콘이 추가되었습니다. 그룹 이름 : " + filename, Toast.LENGTH_LONG).show();
            }
>>>>>>> master
    }

}