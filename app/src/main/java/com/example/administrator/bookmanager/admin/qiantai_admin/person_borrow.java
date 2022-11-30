package com.example.administrator.bookmanager.admin.qiantai_admin;
/*
个人借书表
 */

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.databaseHelp;

import java.util.List;
import java.util.Map;

public class person_borrow extends AppCompatActivity {
    private ListView listView;
    private String bookname, bookauthor, book_bor_time,username;
    private int bookid,borrowid;
    private List<Map<String, Object>> data;
    private Map map;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_borrow);
        listView = (ListView) findViewById(R.id.show_borrow);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);

        username = perf.getString("users", "");//获得当前用户名称
        //根据用户查询自己的借阅信息
        data = help.queryborrow(username);
        SimpleAdapter adapter = new SimpleAdapter(
                person_borrow.this, data, R.layout.borrow_item,
                new String[]{"Borname", "Bookid", "bookname",
                        "bookauthor", "bortime"},
                new int[]{R.id.Borname, R.id.Bbookid,
                        R.id.Bbookname, R.id.Bbookauthor,
                        R.id.Bnowtimae});
        listView.setAdapter(adapter);
        //通过id查询图书表里的所有信息，用bundle进行数据交互

        //点击item跳转到还书页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(person_borrow.this, PayActivity.class);
                map = solveData(position);
                borrowid = (int)map.get("_Bid");
                bookid = (int)map.get("Bookid");
                bookname = map.get("bookname").toString();
                bookauthor = map.get("bookauthor").toString();
                book_bor_time = map.get("bortime").toString();
                Bundle bundle = new Bundle();
                bundle.putInt("bookid",bookid);
                bundle.putInt("borrowid",borrowid);
                bundle.putString("bookname",bookname);
                bundle.putString("bookauthor",bookauthor);
                bundle.putString("booktime",book_bor_time);
                intent.putExtras(bundle);
                startActivity(intent);
/*                View curr = adapterView.getChildAt((int)l);
                TextView bookname = curr.findViewById(R.id.Bbookname);
                TextView bookID = curr.findViewById(R.id.Bbookid);
                TextView author = curr.findViewById(R.id.Bbookauthor);
                TextView time = curr.findViewById(R.id.Bnowtimae);
                help.delborrowbyname(bookname.getText().toString());
                Toast.makeText(person_borrow.this, "还书成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(person_borrow.this, person_borrow.class);
//                startActivity(intent);
                ContentValues values=new ContentValues();
                values.put("Bookid",bookID.getText().toString());
                values.put("bookname",bookname.getText().toString());
                values.put("bookauthor",author.getText().toString());
                values.put("Borname",username);
                values.put("nowtime",time.getText().toString());
                help.insertpay(values);
                finish();*/
            }
        });
        back = findViewById(R.id.btn_person_borrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(person_borrow.this,contentActivity.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });




    }
    public Map solveData(int i){
        map = data.get(i);
        return map;
    }
}
