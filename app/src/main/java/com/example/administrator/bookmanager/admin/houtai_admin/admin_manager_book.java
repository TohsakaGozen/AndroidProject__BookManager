package com.example.administrator.bookmanager.admin.houtai_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;

//管理员选择"图书管理"进入的界面

public class admin_manager_book extends AppCompatActivity {
    private ImageButton back_bt, addbook, editbook, searchbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_book);
        init();//界面初始化
    }

    private void init() {
        back_bt = (ImageButton) findViewById(R.id.manbook_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_book.this, admin_content.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });
        addbook = (ImageButton) findViewById(R.id.ad_add);
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_book.this, admin_add_book.class);
                startActivity(intent);
            }
        });
        searchbook = (ImageButton) findViewById(R.id.ad_search);
        searchbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_book.this, admin_search_book.class);
                startActivity(intent);
            }
        });
    }
}
