package com.example.administrator.bookmanager.admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;

public class registerActivity extends AppCompatActivity {
    private EditText user_ed, username ,pwd_ed,  birthday, phone, sex;
    private Button back_bt, register_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();//界面初始化
    }

    private void init() {
        user_ed = (EditText) findViewById(R.id.r_name);
        pwd_ed = (EditText) findViewById(R.id.r_password);
        username = findViewById(R.id.user_name);
        birthday = findViewById(R.id.r_birthday);
        phone = findViewById(R.id.r_phone);
        sex = findViewById(R.id.r_sex);
        //返回按钮的事件监听
        back_bt = (Button) findViewById(R.id.r_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerActivity.this, MainActivity.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });
        //注册按钮de事件监听
        register_bt = (Button) findViewById(R.id.r_register);
        register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String struser = user_ed.getText().toString();
                String strpwd = pwd_ed.getText().toString();
                String uname = username.getText().toString();
                String birth = birthday.getText().toString();
                String phonenum = phone.getText().toString();
                String usersex = sex.getText().toString();

                databaseHelp help = new databaseHelp(getApplicationContext());
                ContentValues values = new ContentValues();
                values.put("user", struser);
                values.put("name", uname);
                values.put("password", strpwd);
                values.put("sex", usersex);
                values.put("phone", phonenum);
                values.put("birthday", birth);
                SQLiteDatabase db = help.getWritableDatabase();
                //查询用户是否已经存在
                Cursor cusror = db.query("admin", null, null, null, null, null, null);
                if (cusror.moveToFirst()) {
                    do {
                        String username = cusror.getString(cusror.getColumnIndex("user"));
                        if (username.equals(user_ed.getText().toString())) {
                            Toast.makeText(registerActivity.this, "用户名已存在", Toast.LENGTH_LONG).show();
                            ((EditText) findViewById(R.id.r_name)).setText("");
                            return;
                        }

                    } while (cusror.moveToNext());

                }
                cusror.close();
                //对用户注册输入的信息进行验证，全部符合要求才能通过
                boolean testid=true,testnum=true;
                if (user_ed.getText().length()==0) {
                    Toast.makeText(registerActivity.this,"请输入帐号",Toast.LENGTH_SHORT).show();
                    testid=false;

                }

                if(phone.getText().length()!=11){
                    Toast.makeText(registerActivity.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
                    testnum=false;
                }
                if(testid && testnum){
                    help.insert(values);
                    Toast.makeText(registerActivity.this, "用户注册成功", Toast.LENGTH_LONG).show();
                    //用户注册成功，就跳转到登录页面
                    Intent intent = new Intent(registerActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(registerActivity.this, "请重新注册", Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(registerActivity.this, registerActivity.class);
                    startActivity(intent);*/

                }
            }
        });


    }

}
