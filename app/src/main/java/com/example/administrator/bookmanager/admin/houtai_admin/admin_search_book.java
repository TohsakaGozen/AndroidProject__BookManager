package com.example.administrator.bookmanager.admin.houtai_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.bookmanager.R;

//管理员搜索输入框界面

public class admin_search_book extends AppCompatActivity {
    private Button search;
    private EditText search_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_book);
        search = findViewById(R.id.search_btn);
        search_name = findViewById(R.id.search_name);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_search_book.this, admin_search_bookinfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", search_name.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}