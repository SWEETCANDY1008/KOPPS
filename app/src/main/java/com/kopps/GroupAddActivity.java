package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupAddActivity extends AppCompatActivity {
    protected static final String TAG = "GroupAddActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadd);
    }

    public void onAddGroupClicked(View view) {
        EditText editText = (EditText)findViewById(R.id.groupname);

        List<String> filesNameList = new ArrayList<>();

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();




        if (editText.getText().toString().length() == 0 ) {
            //공백일 때 처리할 내용
            Toast.makeText(getApplicationContext(), "그룹 이름을 입력해 주세요", Toast.LENGTH_LONG).show();
        } else {
            //공백이 아닐 때 처리할 내용
            for (int i=0; i< files.length; i++) {
                Log.d(TAG, files[i].getName());
                if(files[i].getName().equals(editText.getText().toString())) {
                    // 같은 파일이 있는 경우
                    Toast.makeText(getApplicationContext(), "그룹이 이미 존재합니다. 그룹 이름 : " + files[i].getName(), Toast.LENGTH_LONG).show();
                } else {
                    // 같은 파일이 없는 경우
                    Toast.makeText(getApplicationContext(), "그룹이 추가되었습니다. 그룹 이름 : " + editText.getText().toString(), Toast.LENGTH_LONG).show();
                    // 그룹 파일 생성 코드 필요공간

                    String filename = editText.getText().toString();
                    String data = filename + "\n";

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
                finish();
            }
        }
    }
}
