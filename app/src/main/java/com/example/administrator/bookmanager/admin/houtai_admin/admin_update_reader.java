package com.example.administrator.bookmanager.admin.houtai_admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.databaseHelp;


/**
 * 修改读者信息的页面
 */

public class admin_update_reader extends AppCompatActivity {
    private EditText user_ed, username ,pwd_ed,  birthday, phone, sex;
    private Button update_bt;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_reader);
        init();//初始化界面
    }

    private void init() {
        //将id返回的记录查询在edittext
        user_ed = (EditText) findViewById(R.id.r_name);
        pwd_ed = (EditText) findViewById(R.id.r_password);
        username = findViewById(R.id.user_name);
        birthday = findViewById(R.id.r_birthday);
        phone = findViewById(R.id.r_phone);
        sex = findViewById(R.id.r_sex);
        update_bt = (Button) findViewById(R.id.r_register);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.queryid(id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user_ed.setText(cursor.getString(cursor.getColumnIndex("user")));
            pwd_ed.setText(cursor.getString(cursor.getColumnIndex("password")));
            username.setText(cursor.getString(cursor.getColumnIndex("name")));
            birthday.setText(cursor.getString(cursor.getColumnIndex("birthday")));
            phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
            sex.setText(cursor.getString(cursor.getColumnIndex("sex")));

        }
        //修改按钮的事件监听
        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String struser = user_ed.getText().toString();
                String strpwd = pwd_ed.getText().toString();
                String uname = username.getText().toString();
                String birth = birthday.getText().toString();
                String phonenum = phone.getText().toString();
                String usersex = sex.getText().toString();
                SQLiteDatabase db = help.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user", struser);
                values.put("name", uname);
                values.put("password", strpwd);
                values.put("sex", usersex);
                values.put("phone", phonenum);
                values.put("birthday", birth);
                db.update("admin", values, "_id=?", new String[]{String.valueOf(id)});
                Intent intent = new Intent(admin_update_reader.this, admin_editer_reader.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });

    }
}
