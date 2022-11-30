package com.example.administrator.bookmanager.admin.qiantai_admin;
/*
查询我的还书
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.bookmanager.R;
import com.example.administrator.bookmanager.admin.ActivityCollector;
import com.example.administrator.bookmanager.admin.databaseHelp;

import java.util.List;
import java.util.Map;

public class person_pay extends AppCompatActivity {
    private ListView pay_view;
    private Button back;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_person_pay);
            pay_view=(ListView)findViewById(R.id.show_pay);
            final databaseHelp help = new databaseHelp(getApplicationContext());
            SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);

            String username = perf.getString("users", "");//获得当前用户名称
            //根据用户查询自己的还书信息
            List<Map<String, Object>> data = help.querypayuser(username);
            SimpleAdapter adapter = new SimpleAdapter(
                    person_pay.this, data, R.layout.pay_item,
                    new String[]{"Borname", "Bookid", "bookname",
                            "bookauthor", "nowtime"},
                    new int[]{R.id.Porname, R.id.Pbookid,
                            R.id.Pbookname, R.id.Pbookauthor,
                            R.id.Pnowtimae});
            pay_view.setAdapter(adapter);

            back = findViewById(R.id.btn_person_pay_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(person_pay.this,contentActivity.class);
                    startActivity(intent);
                    ActivityCollector.finishAll();
                }
            });
        }

}
