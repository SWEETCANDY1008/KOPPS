package com.kopps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {
    protected static final String TAG = "GroupActivity";
    static ArrayList<String> lists = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        final DataBase database = new DataBase(getApplicationContext(), "Test.db", null, 1);


        lists = database.getGroup();

        Button add = (Button) findViewById(R.id.add);
        Button modify = (Button) findViewById(R.id.modify);
        Button delete = (Button) findViewById(R.id.delete);

        if(lists != null) {
            adapter = new ArrayAdapter<String>(GroupActivity.this, android.R.layout.simple_list_item_1, lists);

            ListView listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent intent = new Intent(GroupActivity.this, BeaconActivity.class);
                    String get_group = lists.get(position);
                    intent.putExtra("group", get_group);
                    startActivity(intent);
                }
            });
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(GroupActivity.this);
                dialog.setTitle("그룹 생성");
                dialog.setMessage("생성할 그룹이름을 입력해 주세요");

                final EditText edittexts = new EditText(GroupActivity.this);
                dialog.setView(edittexts);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = edittexts.getText().toString();
                        boolean exists;
                        if(lists == null) {
                            exists = false;
                        } else if(lists.contains(value)) {
                            exists = true;
                        } else {
                            exists = false;
                        }

                        if (value.length() == 0 ) {
                            // 공백일 때 처리할 내용
                            Toast.makeText(getApplicationContext(), "그룹 이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                        } else if(!exists){
                            database.insert(value);
                            if(lists != null) {
                                lists.clear();
                            }
                            lists = database.getGroup();
                            adapter = new ArrayAdapter<String>(GroupActivity.this, android.R.layout.simple_list_item_1, lists);
                            ListView listview = (ListView) findViewById(R.id.listview1);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "추가된 그룹명 : " + value, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else if(exists) {
                            Toast.makeText(getApplicationContext(), "그룹이 이미 존재합니다.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(GroupActivity.this);
                dialog.setTitle("그룹 수정");
                LinearLayout linearLayout = new LinearLayout(GroupActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final TextView textView_groupname = new TextView(GroupActivity.this);
                final EditText edittexts = new EditText(GroupActivity.this);

                final TextView textView_groupname_change = new TextView(GroupActivity.this);
                final EditText edittexts_change = new EditText(GroupActivity.this);

                textView_groupname.setText("기존 그룹이름을 입력하세요");
                textView_groupname_change.setText("변경할 그룹이름을 입력하세요");

                linearLayout.addView(textView_groupname);
                linearLayout.addView(edittexts);

                linearLayout.addView(textView_groupname_change);
                linearLayout.addView(edittexts_change);

                dialog.setView(linearLayout);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String groupname = edittexts.getText().toString();
                        String groupname_change = edittexts_change.getText().toString();

                        boolean exists_change = lists.contains(groupname_change);

                        if (groupname.length() == 0) {
                            // 공백일때 처리
                            Toast.makeText(getApplicationContext(), "기존 그룹 이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                        } else if(groupname_change.length() == 0) {
                            Toast.makeText(getApplicationContext(), "바꿀 그룹 이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                        } else if(exists_change) {
                            Toast.makeText(getApplicationContext(), "바꿀 그룹이 이미 존재합니다..", Toast.LENGTH_LONG).show();
                        } else {
                            database.update(groupname, groupname_change);
                            lists.clear();
                            lists = database.getGroup();
                            adapter = new ArrayAdapter<String>(GroupActivity.this, android.R.layout.simple_list_item_1, lists);
                            ListView listview = (ListView) findViewById(R.id.listview1);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),  "기존 그룹명 : " + groupname + " 수정된 그룹명 : " + groupname_change, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(GroupActivity.this);
                dialog.setTitle("그룹 삭제");
                dialog.setMessage("삭제할 그룹이름을 입력해 주세요");

                final EditText edittexts = new EditText(GroupActivity.this);
                dialog.setView(edittexts);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = edittexts.getText().toString();
                        boolean exists = lists.contains(value);

                        if (value.length() == 0 ) {
                            // 공백일 때 처리할 내용
                            Toast.makeText(getApplicationContext(), "그룹 이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                        } else if(!exists) {
                            Toast.makeText(getApplicationContext(), "그룹이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                        } else if(exists){
                            database.delete(value);
                            lists.clear();
                            lists = database.getGroup();
                            adapter = new ArrayAdapter<String>(GroupActivity.this, android.R.layout.simple_list_item_1, lists);
                            ListView listview = (ListView) findViewById(R.id.listview1);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "삭제된 그룹명 : " + value, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }


}
