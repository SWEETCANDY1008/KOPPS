package com.kopps;

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

    }

}