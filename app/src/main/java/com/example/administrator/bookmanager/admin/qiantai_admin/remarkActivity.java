package com.example.administrator.bookmanager.admin.qiantai_admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.databaseHelp;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_add_book;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_manager_book;

import java.text.SimpleDateFormat;
import java.util.Date;

public class remarkActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_bt,commit_btn;
    private databaseHelp helper;
    private EditText editText;
    private String user_name,time,book_id,book_name;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        init();//界面初始化
        helper = new databaseHelp(getApplicationContext());
        /*
        获取当前系统时间
        用作评论日期time
         */
        SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy年MM月dd日/HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        time = formatter.format(curDate);

        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);
        user_name = perf.getString("users", "");//获得当前用户名称

        Bundle bundle=this.getIntent().getExtras();//获取id
        id=bundle.getInt("id")+1;
        Cursor cursor=helper.querybookinfoid(id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            book_id = cursor.getString(cursor.getColumnIndex("bookid"));
            book_name = cursor.getString(cursor.getColumnIndex("name"));
        }
    }

    private void init() {
        editText = findViewById(R.id.remark);
        back_bt = findViewById(R.id.btn_remarkback);
        commit_btn = findViewById(R.id.btn_remarkcommit);
        commit_btn.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_remarkcommit:
                if(editText.getText().length()==0){
                    Toast.makeText(remarkActivity.this,"评论不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }
                helper.insertRemark(
                        user_name,
                        book_id,
                        book_name,
                        time,
                        editText.getText().toString()
                );//调用databaseHelp中的方法将评论插入数据库中
                Toast.makeText(remarkActivity.this,"评论成功！",Toast.LENGTH_SHORT).show();
            case R.id.btn_remarkback:
                Intent intent = new Intent(remarkActivity.this, borrowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id-1);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityCollector.finishAll();



        }
    }

}
