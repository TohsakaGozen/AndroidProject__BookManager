package com.example.administrator.bookmanager.admin.houtai_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.databaseHelp;

import static com.example.administrator.bookmanager.admin.utils.BitmapTool.byteToBitmap;

//管理员查询成功后显示的界面

public class admin_search_bookinfo extends AppCompatActivity {
    private ImageButton back_bt;
    private ListView listView;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_bookinfo);
        init();//界面初始化

    }

    private void init() {
        listView = (ListView) findViewById(R.id.select_book_list);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        back_bt = (ImageButton) findViewById(R.id.search_book_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_search_bookinfo.this, admin_manager_book.class);
                startActivity(intent);
            }
        });
        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        Cursor cursor = help.querybookinfoname(name);
        String from[] = {"name", "type", "writer","publicer","rank","img"};
        int to[] = {R.id.admin_book_name, R.id.admin_book_type, R.id.admin_book_author, R.id.admin_book_publish, R.id.admin_book_rank, R.id.admin_book_info_img};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.admin_book_item, cursor, from, to);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.admin_book_info_img) {
                    ImageView imageView = (ImageView) view;
                    //imageView.setImageURI(Uri.parse(cursor.getString(columnIndex)));
                    imageView.setImageBitmap(byteToBitmap(cursor.getBlob(columnIndex)));
                    return true;
                } else {
                    return false;
                }
            }
        });
        listView.setAdapter(adapter);
    }


}
