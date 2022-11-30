package com.example.administrator.bookmanager.admin.qiantai_admin;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.databaseHelp;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.administrator.bookmanager.admin.utils.BitmapTool.byteToBitmap;

//用户从首页选择书目后进入的借书页面

public class borrowActivity extends AppCompatActivity {
    private ImageView borrow_bookimg;
    private TextView borrow_bookwriter,borrow_bookname,borrow_booktype,borrow_bookpublicer,borrow_bookprice,borrow_bookrank,borrow_bookcomment,borrow_bookid;
    private Button borrow_bt,collect_bt,remark_bt,back;
    private String str,book_id,book_name;
    private ListView listView;
    int id;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        /*
        获取当前系统时间
        用作借书日期time
         */
        SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy年MM月dd日/HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        str = formatter.format(curDate);
       /* SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("time",str);
        editor.apply();

/*        bid=(TextView)findViewById(R.id.bookbid);
        bname=(TextView)findViewById(R.id.bookbname);
        bauthor=(TextView)findViewById(R.id.bookbauthor);
        bprice=(TextView)findViewById(R.id.bookbprice);*/

        borrow_bookid=findViewById(R.id.borrow_bookid);
        borrow_bookname=findViewById(R.id.borrow_bookname);
        borrow_bookwriter=findViewById(R.id.borrow_bookwriter);
        borrow_bookprice=findViewById(R.id.borrow_bookprice);
        borrow_bookimg=findViewById(R.id.borrow_bookimg);
        borrow_booktype=findViewById(R.id.borrow_booktype);
        borrow_bookpublicer=findViewById(R.id.borrow_bookpublicer);
        borrow_bookrank=findViewById(R.id.borrow_bookrank);
        borrow_bookcomment=findViewById(R.id.borrow_bookcomment);

        //获取id
        Bundle bundle=this.getIntent().getExtras();
        id=bundle.getInt("id") + 1;
        final databaseHelp help=new databaseHelp(getApplicationContext());
        final Cursor cursor=help.querybookinfoid(id);
        Log.i("cursor", "onCreate: " +cursor.getCount());
        //信息显示
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            borrow_bookid.setText(cursor.getString(cursor.getColumnIndex("bookid")));
            book_id = cursor.getString(cursor.getColumnIndex("bookid"));
            borrow_bookname.setText(cursor.getString(cursor.getColumnIndex("name")));
            book_name = cursor.getString(cursor.getColumnIndex("name"));
            borrow_bookwriter.setText(cursor.getString(cursor.getColumnIndex("writer")));
            borrow_bookprice.setText(cursor.getString(cursor.getColumnIndex("price")));
            borrow_bookimg.setImageBitmap(byteToBitmap(cursor.getBlob(cursor.getColumnIndex("img"))));
            borrow_booktype.setText(cursor.getString(cursor.getColumnIndex("type")));
            borrow_bookpublicer.setText(cursor.getString(cursor.getColumnIndex("publicer")));
            borrow_bookrank.setText(cursor.getString(cursor.getColumnIndex("rank")));
            borrow_bookcomment.setText(cursor.getString(cursor.getColumnIndex("comment")));
        }

        //评论显示
        listView = (ListView) findViewById(R.id.rem_list);
        Cursor cursor1 = help.remarkInfo(book_id,book_name);
        Log.i("rem", "id是:"+book_id+"name是:"+book_name);
        String from[] = {"Rmkname", "rem", "nowtime"};
        int to[] = {R.id.remark_from, R.id.remark_content, R.id.remark_time};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.remark_item, cursor1, from, to);
        listView.setAdapter(adapter);

        borrow_bt=(Button)findViewById(R.id.borroe_bt);
        borrow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //借书日期
                SharedPreferences perf=getSharedPreferences("data",MODE_PRIVATE);
               // String datetime=perf.getString("time","");//获得当前系统时间
                String username=perf.getString("users","");//获得当前用户名称
                String strbname=borrow_bookname.getText().toString();//书名
                //查询是否已经借过
                Cursor cur = help.checkborrowinfo(strbname,username);
                if (cur.getCount()>0)
                {
                    Toast.makeText(borrowActivity.this,"您已经借阅了这本书!",Toast.LENGTH_SHORT).show();
                    return;
                }

                //将书籍信息插入借阅表中
                String strbid=borrow_bookid.getText().toString();
                String strbauthor=borrow_bookwriter.getText().toString();
                int intbid=Integer.parseInt(strbid);
                ContentValues values=new ContentValues();
                values.put("Bookid",intbid);
                values.put("bookname",strbname);
                values.put("bookauthor",strbauthor);
                values.put("Borname",username);
                values.put("nowtime",str);
                help.insertorrowo(values);
              //获取当前用户
                Toast.makeText(borrowActivity.this,"借书成功",Toast.LENGTH_SHORT).show();
            }
        });
        collect_bt=(Button)findViewById(R.id.collect_bt);
        collect_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将书籍信息插入收藏表中
                //收藏日期
                SharedPreferences perf=getSharedPreferences("data",MODE_PRIVATE);
                // String datetime=perf.getString("time","");//获得当前系统时间
                String username=perf.getString("users","");//获得当前用户名称
                String strbname=borrow_bookname.getText().toString();

                //查询是否收藏过
                Cursor cur = help.checkcollectinfo(strbname,username);
                if (cur.getCount()>0)
                {
                    Toast.makeText(borrowActivity.this,"您已经收藏了这本书!",Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values=new ContentValues();
                values.put("Bookid",Integer.parseInt(borrow_bookid.getText().toString()));
                values.put("bookname",strbname);
                values.put("bookauthor",borrow_bookwriter.getText().toString());
                values.put("Borname",username);
                values.put("nowtime",str);
                values.put("type",borrow_booktype.getText().toString());
                values.put("rank",borrow_bookrank.getText().toString());
                values.put("price",borrow_bookprice.getText().toString());
                values.put("img",cursor.getBlob(cursor.getColumnIndex("img")));
                help.insertocollect(values);
                //获取当前用户
                Toast.makeText(borrowActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
            }
        });
        //点击评论按钮
        remark_bt=(Button)findViewById(R.id.remark_bt);
        remark_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(borrowActivity.this, remarkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id-1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        back=findViewById(R.id.borrow_back_bt);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(borrowActivity.this, contentActivity.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });
    }

}
