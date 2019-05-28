package com.kopps;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class CachingBeacon extends AppCompatActivity {
    protected static final String TAG = "CachingBeacon";
    String filename;
    String data;

    CachingBeacon(String filename, String data) {
        this.filename = filename;
        this.data = data;
    }

    public void writes() {
        try {
            File cacheDir = getCacheDir();
            File cacheFile = new File(cacheDir.getAbsolutePath(), this.filename);
            FileOutputStream fos = new FileOutputStream(cacheFile.getAbsolutePath());
            fos.write(this.data.getBytes());
            fos.close();

        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }

    public void read() {
        try {
            File cacheDir = getCacheDir();
            File cacheFile = new File(cacheDir.getAbsolutePath(), this.filename);
            FileInputStream inputStream = new FileInputStream(cacheFile.getAbsolutePath());

            Scanner s = new Scanner(inputStream);
            String text="";
            while(s.hasNext()){
                text+=s.nextLine();
                Log.d(TAG, text);
            }
            inputStream.close();
//                            Log.d(TAG, text);
            Log.d(TAG, String.valueOf(text.length()));


        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        }

    }

}