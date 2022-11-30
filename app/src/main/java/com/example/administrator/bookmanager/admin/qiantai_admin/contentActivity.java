package com.example.administrator.bookmanager.admin.qiantai_admin;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bookmanager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.MainActivity;
import com.example.administrator.bookmanager.admin.databaseHelp;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_content;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_search_book;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_search_bookinfo;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_select_bookinfo;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_update_book;
import com.example.administrator.bookmanager.admin.houtai_admin.admin_update_reader;
import com.example.administrator.bookmanager.admin.registerActivity;

import static com.example.administrator.bookmanager.admin.utils.BitmapTool.bitmapToByte;
import static com.example.administrator.bookmanager.admin.utils.BitmapTool.byteToBitmap;

//用户登录成功后的首页界面

public class contentActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private long mExitTime;
    private EditText searchStr;
    private Button searchBtn;
    private TextView personName;
    private databaseHelp help;
    private static boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        help = new databaseHelp(getApplicationContext());

/*        //更新图片
        if (flag){
            initImg();
            flag=false;
        }*/


        searchStr = findViewById(R.id.user_search_str);
        searchBtn = findViewById(R.id.user_search);
        searchBtn.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                int i = position + 1;
                Intent intent = new Intent(contentActivity.this, borrowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //侧滑菜单栏的选项
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shoucang:
                        //跳转至个人收藏页面
                        Intent intent = new Intent(contentActivity.this, collectActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.jieyue:
                        //跳转到个人借书的页面
                        Intent intent2 = new Intent(contentActivity.this, person_borrow.class);
                        startActivity(intent2);
                        break;
                    case R.id.updateInfo:
                        //跳转到修改个人信息页面
                        Intent intent3 = new Intent(contentActivity.this, ReaderUpdateInfo.class);
                        startActivity(intent3);
                        break;
                    case R.id.tuijian:
                        //跳转到推荐阅读信息页面
                        Intent intent4 = new Intent(contentActivity.this, recommendInfo.class);
                        startActivity(intent4);
                        break;
                    case R.id.exit:
                        Intent intent5 = new Intent(contentActivity.this, MainActivity.class);
                        startActivity(intent5);
                        ActivityCollector.finishAll();
                        break;
                    case R.id.yihuan:
                        //跳转到个人借书的页面
                        Intent intent6 = new Intent(contentActivity.this, person_pay.class);
                        startActivity(intent6);
                        break;
                    default:

                }
                drawerLayout.closeDrawers();//将滑动菜单关闭
                return true;
            }
        });

        listView = findViewById(R.id.list_view);
        Cursor cursor = help.querybookinfo();
        /*String from[] = {"img","name", "writer"};
        int to[] = {R.id.book_image,R.id.book_name, R.id.book_author};*/
        String from[] = {"name", "type", "writer", "publicer", "rank", "img","price"};
        int to[] = {R.id.user_book_name, R.id.user_book_type, R.id.user_book_author, R.id.user_book_publish, R.id.user_book_rank, R.id.user_book_info_img,R.id.user_book_pice};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.book_item, cursor, from, to);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.user_book_info_img) {
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

        View headView = navigationView.getHeaderView(0);
        personName = headView.findViewById(R.id.person_name);
        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);
        String name = perf.getString("users", "");//获得当前用户名称
        //直接用findViewbyid(R.id.person_name);返回的值是null。所以personName.setText(name);会报错空指针异常!
        //要先获取到 Navigation ,然后通过  navigation.getHeaderView(0)  来获取  headView，
        //接着通过headview.findViewbyid(R.id.person_name);便可获取。
        personName.setText(name);





    }

    private void initImg() {
        Bitmap bm1  = BitmapFactory.decodeResource(getResources(),R.mipmap.book0 ,null);
        Bitmap bm2  = BitmapFactory.decodeResource(getResources(),R.mipmap.book1 ,null);
        Bitmap bm3  = BitmapFactory.decodeResource(getResources(),R.mipmap.book2 ,null);
        Bitmap bm4  = BitmapFactory.decodeResource(getResources(),R.mipmap.book3 ,null);
        Bitmap bm5  = BitmapFactory.decodeResource(getResources(),R.mipmap.book4 ,null);
        Bitmap bm6  = BitmapFactory.decodeResource(getResources(),R.mipmap.book5 ,null);
        Bitmap bm7  = BitmapFactory.decodeResource(getResources(),R.mipmap.book6 ,null);
        Bitmap bm8  = BitmapFactory.decodeResource(getResources(),R.mipmap.book7 ,null);
        Bitmap bm9  = BitmapFactory.decodeResource(getResources(),R.mipmap.book8 ,null);
        Bitmap bm10 = BitmapFactory.decodeResource(getResources(),R.mipmap.book9 ,null);
        Bitmap bm11 = BitmapFactory.decodeResource(getResources(),R.mipmap.book10,null);
        Bitmap bm12 = BitmapFactory.decodeResource(getResources(),R.mipmap.book11,null);
        Bitmap bm13 = BitmapFactory.decodeResource(getResources(),R.mipmap.book12,null);
        ContentValues values = new ContentValues();
        values.put("img",bitmapToByte(bm1 ));
        help.updateImg(values,1);
        values.put("img",bitmapToByte(bm2 ));
        help.updateImg(values,2);
        values.put("img",bitmapToByte(bm3 ));
        help.updateImg(values,3);
        values.put("img",bitmapToByte(bm4 ));
        help.updateImg(values,4);
        values.put("img",bitmapToByte(bm5 ));
        help.updateImg(values,5);
        values.put("img",bitmapToByte(bm6 ));
        help.updateImg(values,6);
        values.put("img",bitmapToByte(bm7 ));
        help.updateImg(values,7);
        values.put("img",bitmapToByte(bm8 ));
        help.updateImg(values,8);
        values.put("img",bitmapToByte(bm9 ));
        help.updateImg(values,9);
        values.put("img",bitmapToByte(bm10));
        help.updateImg(values,10);
        values.put("img",bitmapToByte(bm11));
        help.updateImg(values,11);
        values.put("img",bitmapToByte(bm12));
        help.updateImg(values,12);
        values.put("img",bitmapToByte(bm13));
        help.updateImg(values,13);
    }

    //toolbar的菜单栏的选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                Toast.makeText(contentActivity.this, "search", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_search:
                Intent intent = new Intent(contentActivity.this, user_search_bookinfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", searchStr.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(contentActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
