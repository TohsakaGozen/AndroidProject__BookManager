package com.example.administrator.bookmanager.admin.houtai_admin;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.databaseHelp;

import static com.example.administrator.bookmanager.admin.utils.BitmapTool.bitmapToByte;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员添加图书的界面
 */

public class admin_add_book extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_bt;
    Bitmap bmp = null;
    private Spinner spinner;
    private databaseHelp helper;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<String>();
    Uri uri;
    private String pub;
    private ImageView bookimg;
    private EditText et_bookid, et_bookname, et_booktype, et_bookwriter, et_bookpublicer, et_bookprice, et_bookrank, et_bookcomment;
    private Button btn_bookcommit, btn_bookback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        list.add("小明出版社");
        list.add("小红出版社");
        list.add("小张出版社");
        list.add("张三出版社");
        list.add("李四出版社");
        list.add("王五出版社");
        spinner = (Spinner) findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pub = adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initdata();//界面初始化
        helper = new databaseHelp(getApplicationContext());
    }

    private void initdata() {
        bookimg = findViewById(R.id.add_bookimg);

        Resources r = admin_add_book.this.getResources();
        bmp = BitmapFactory.decodeResource(r, R.drawable.head);

        et_bookid = findViewById(R.id.et_bookid);
        et_bookname = findViewById(R.id.et_bookname);
        et_booktype = findViewById(R.id.et_booktype);
        et_bookwriter = findViewById(R.id.et_bookwriter);
        et_bookprice = findViewById(R.id.et_bookprice);
        et_bookrank = findViewById(R.id.et_bookrank);
        et_bookcomment = findViewById(R.id.et_bookcomment);


        btn_bookcommit = findViewById(R.id.btn_bookcommit);
        btn_bookback = findViewById(R.id.btn_bookback);

        btn_bookcommit.setOnClickListener(this);
        bookimg.setOnClickListener(this);
        btn_bookback.setOnClickListener(this);

    }

    //对管理员输入的图书信息进行验证，全部符合要求才能通过
    boolean testid = true, testother = true;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bookcommit:
                if (et_bookid.getText() == null) {
                    Toast.makeText(admin_add_book.this, "请输入图书id", Toast.LENGTH_SHORT).show();
                    testid = false;
                    break;
                }

                if (et_bookname.getText().length() == 0) {
                    Toast.makeText(admin_add_book.this, "请输入完整图书信息", Toast.LENGTH_SHORT).show();
                    testother = false;
                    break;
                }
                if (testid == true && testother == true) {
                    helper.inserbooktdata(
                            et_bookid.getText().toString(),
                            et_bookname.getText().toString(),
                            et_booktype.getText().toString(),
                            et_bookwriter.getText().toString(),
                            pub,
                            et_bookprice.getText().toString(),
                            et_bookrank.getText().toString(),
                            et_bookcomment.getText().toString(),
                            bitmapToByte(bmp)
                    );
                    Toast.makeText(admin_add_book.this, "添加图书成功！", Toast.LENGTH_SHORT).show();
                    break;
                }


            case R.id.add_bookimg:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);  // 第二个参数是请求码
                break;

            case R.id.btn_bookback:
                Intent intentback = new Intent();
                intentback.setClass(admin_add_book.this, admin_manager_book.class);
                startActivity(intentback);
                ActivityCollector.finishAll();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:  // 请求码
                parseUri(data);
                break;
            default:
        }
    }

    public void parseUri(Intent data) {
        uri = data.getData();
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                Log.i("111", uri.getAuthority());
                is = admin_add_book.this.getContentResolver().openInputStream(uri);
                bmp = BitmapFactory.decodeStream(is);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        bookimg.setImageBitmap(bmp);
    }

}
