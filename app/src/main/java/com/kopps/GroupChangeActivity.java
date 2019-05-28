package com.kopps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupChangeActivity extends AppCompatActivity {
    protected static final String TAG = "GroupChangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchange);

        List<String> filesNameList = new ArrayList<>();

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i=0; i< files.length; i++) {
            filesNameList.add(files[i].getName());
            Log.d(TAG, files[i].getName());
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, filesNameList);
        spinner.setAdapter(arrayAdapter);


//        try {
//            String filename = "리스트에서 선택한 파일";

//            File cacheDir = getCacheDir();
//            File cacheFile = new File(cacheDir.getAbsolutePath(), this.filename);
//            FileInputStream inputStream = new FileInputStream(cacheFile.getAbsolutePath());
//
//            Scanner s = new Scanner(inputStream);
//            String text="";
//            while(s.hasNext()){
//                text+=s.nextLine();
//                Log.d(TAG, text);
//            }
//            inputStream.close();
////                            Log.d(TAG, text);
//            Log.d(TAG, String.valueOf(text.length()));
//        } catch(FileNotFoundException fnfe) {
//            fnfe.printStackTrace();
//        } catch(IOException ie) {
//            ie.printStackTrace();
//        }
//


    }

    public void onChangeGroupClicked(View view) {
        EditText edittext = (EditText) findViewById(R.id.changegroupname);
        String changename = String.valueOf(edittext.getText());

        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        String selectchangegroup = spinner.getSelectedItem().toString();

        String path = getCacheDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        File changefilename = new File(path, changename);

        int i;
        for (i = 0; i < files.length; i++) {
            String changegroupname = files[i].getName();
            if (changegroupname.equals(changename)) {
                Toast.makeText(getApplicationContext(), "바꾸려는 이름이 이미 존재합니다.", Toast.LENGTH_LONG).show();
                finish();
                break;
            }
        }
    }
}
//            else {
//                for (int j = i+1; j < files.length; j++) {
//                    changegroupname = files[j].getName();
//                    if (changegroupname.equals(changename)) {
//                        Toast.makeText(getApplicationContext(), "바꾸려는 이름이 이미 존재합니다.", Toast.LENGTH_LONG).show();
//                        finish();
//                        break;
//                    } else {
//                        files[j].renameTo(changefilename);
//                        Toast.makeText(getApplicationContext(), "그룹이 변경되었습니다. 변경된 그룹 이름 : " + changegroupname, Toast.LENGTH_LONG).show();
//                        finish();
//                        break;
//                    }
//                }




//                if (!selectchangegroup.equals(changegroupname)) {
//                    Toast.makeText(getApplicationContext(), "그룹이 변경되지 않았습니다.", Toast.LENGTH_LONG).show();
//                } else {
//                     else {
//                        Toast.makeText(getApplicationContext(), "그룹 변경에 실패하였습니다.", Toast.LENGTH_LONG).show();
//                    }
//                }
//        }

//
//        if (!changegroupname.equals(changename)) {
//            Toast.makeText(getApplicationContext(), "바꾸려는 이름이 이미 존재합니다.", Toast.LENGTH_LONG).show();
//        } else {}








