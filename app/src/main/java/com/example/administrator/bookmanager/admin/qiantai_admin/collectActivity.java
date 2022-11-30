package com.example.administrator.bookmanager.admin.qiantai_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.MainActivity;
import com.example.administrator.bookmanager.admin.databaseHelp;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_select_bookinfo;
import com.example.administrator.bookmanager.admin.registerActivity;

import java.util.List;
import java.util.Map;

import static com.example.administrator.bookmanager.admin.utils.BitmapTool.byteToBitmap;

//侧边栏进入的收藏页面

public class collectActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private ListView listView;
    private databaseHelp help;
    private String username;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        listView = findViewById(R.id.show_collect);
        help = new databaseHelp(getApplicationContext());
        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);
        username = perf.getString("users", "");//获得当前用户名称
        //根据用户查询自己的收藏信息
        Cursor cursor=help.queryuser(username);
        String from[] = {"bookname", "bookauthor", "nowtime","type","rank","price","img"};
        int to[] = {R.id.collect_book_name, R.id.collect_book_author, R.id.collect_time,R.id.collect_book_type,R.id.collect_book_rank,R.id.collect_book_pice,R.id.collect_book_info_img};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.collect_item, cursor, from, to);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.collect_book_info_img) {
                    ImageView iconImageView = (ImageView) view;
                    //iconImageView.setImageURI(Uri.parse(cursor.getString(columnIndex)));
                    iconImageView.setImageBitmap(byteToBitmap(cursor.getBlob(columnIndex)));
                    return true;
                } else {
                    return false;
                }
            }
        });
        listView.setAdapter(adapter);

        back = findViewById(R.id.btn_person_collect_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(collectActivity.this,contentActivity.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });

        //取消收藏
/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View curr = adapterView.getChildAt((int) l);
                TextView name = curr.findViewById(R.id.Bbookname);
                help.delcollect(name.getText().toString());
                Toast.makeText(collectActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                listView.setAdapter(adapter);
                Intent intent = new Intent(collectActivity.this, collectActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        builder = new AlertDialog.Builder(collectActivity.this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, int i, long l) {
                final long temp = l;
                builder.setMessage("确定要取消收藏吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //View curr = adapterView.getChildAt((int) temp);

                        TextView v = view.findViewById(R.id.collect_book_name);
                        String name = v.getText().toString();
                        //TextView name = curr.findViewById(R.id.collect_book_name);
                        help.delcollect(name);
                        Toast.makeText(collectActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        //删除后重新显示
                        Cursor cursor=help.queryuser(username);
                        String from[] = {"bookname", "bookauthor", "nowtime","type","rank","price","img"};
                        int to[] = {R.id.collect_book_name, R.id.collect_book_author, R.id.collect_time,R.id.collect_book_type,R.id.collect_book_rank,R.id.collect_book_pice,R.id.collect_book_info_img};
                        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(collectActivity.this, R.layout.collect_item, cursor, from, to);
                        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                if (view.getId() == R.id.collect_book_info_img) {
                                    ImageView iconImageView = (ImageView) view;
                                    //iconImageView.setImageURI(Uri.parse(cursor.getString(columnIndex)));
                                    iconImageView.setImageBitmap(byteToBitmap(cursor.getBlob(columnIndex)));
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                        listView.setAdapter(adapter);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


    }
}
