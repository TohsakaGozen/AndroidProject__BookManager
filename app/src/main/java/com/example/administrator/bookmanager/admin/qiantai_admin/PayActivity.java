package com.example.administrator.bookmanager.admin.qiantai_admin;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.databaseHelp;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.administrator.bookmanager.admin.utils.BitmapTool.byteToBitmap;

public class PayActivity extends AppCompatActivity {
    private TextView pay_bookid, pay_bookname, pay_bookwriter, pay_bookprice, pay_booktype, pay_bookpublicer, pay_bookrank, pay_bookcomment, borrow_time;
    private ImageView pay_bookimg;
    private Button paybook_bt,pay_back_bt;
    private databaseHelp help;
    private int bookid,borrowid;
    private String time_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        help = new databaseHelp(getApplicationContext());

        pay_bookid = findViewById(R.id.pay_bookid);
        pay_bookname = findViewById(R.id.pay_bookname);
        pay_bookwriter = findViewById(R.id.pay_bookwriter);
        pay_bookprice = findViewById(R.id.pay_bookprice);
        pay_bookimg = findViewById(R.id.pay_bookimg);
        pay_booktype = findViewById(R.id.pay_booktype);
        pay_bookpublicer = findViewById(R.id.pay_bookpublicer);
        pay_bookrank = findViewById(R.id.pay_bookrank);
        pay_bookcomment = findViewById(R.id.pay_bookcomment);
        borrow_time = findViewById(R.id.pay_time);

        Bundle bundle = this.getIntent().getExtras();
        pay_bookid.setText(bundle.getInt("bookid")+"");
        pay_bookname.setText(bundle.getString("bookname"));
        pay_bookwriter.setText(bundle.getString("bookauthor"));
        borrow_time.setText(bundle.getString("booktime"));
        borrowid=bundle.getInt("borrowid");

        bookid = bundle.getInt("bookid");
        Cursor cursor = help.querybookinfobookid(bookid);
        Log.i("count123", String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            pay_bookprice.setText(cursor.getString(cursor.getColumnIndex("price")));
            pay_bookimg.setImageBitmap(byteToBitmap(cursor.getBlob(cursor.getColumnIndex("img"))));
            pay_booktype.setText(cursor.getString(cursor.getColumnIndex("type")));
            pay_bookpublicer.setText(cursor.getString(cursor.getColumnIndex("publicer")));
            pay_bookrank.setText(cursor.getString(cursor.getColumnIndex("rank")));
            pay_bookcomment.setText(cursor.getString(cursor.getColumnIndex("comment")));
        }

        //还书按钮的事件监听
        paybook_bt = findViewById(R.id.pay_bt);
        paybook_bt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                /*
                将信息插入还书表
                 */
                //还书日期
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                time_str = formatter.format(curDate);
                SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);
                // String datetime=perf.getString("time","");//获得当前系统时间
                String username = perf.getString("users", "");//获得当前用户名称

                String strbname = pay_bookname.getText().toString();//书籍名称
                String strbauthor = pay_bookwriter.getText().toString();//书籍作者
                ContentValues values = new ContentValues();
                values.put("Borname", username);
                values.put("Bookid", bookid);
                values.put("bookname", strbname);
                values.put("bookauthor", strbauthor);
                values.put("nowtime", time_str);
                values.put("borrowid",borrowid);
                help.insertpay(values);
                Toast.makeText(PayActivity.this, "还书成功", Toast.LENGTH_SHORT).show();
                /*
                删除相应的借书信息
                */
                help.delborrow(bookid);

                Intent intent = new Intent(PayActivity.this, person_borrow.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });
        pay_back_bt = findViewById(R.id.pay_back_bt);
        pay_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, person_borrow.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });
    }
}
