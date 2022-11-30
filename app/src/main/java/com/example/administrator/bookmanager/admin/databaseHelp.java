package com.example.administrator.bookmanager.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.administrator.bookmanager.admin.utils.BitmapTool.bitmapToByte;

public class databaseHelp extends SQLiteOpenHelper {
    private static final String DB_NAME = "CMP.db";
    private static final String Table_Name1 = "admin";//读者表
    private static final String Table_Name2 = "bookinfo";//图书表
    private static final String Table_Name3 = "borrow";//借书表
    private static final String Table_Name4 = "pay";//还书表
    private static final String Table_Name5 = "collect";//收藏表

    public static final String id = "_id";
    public static final String Table_Book = "bookinfo";
    public static final String Book_Id = "bookid";//书号（图书的ISBN）
    public static final String Book_Name = "name";
    public static final String Book_Type = "type";
    public static final String Book_Writer = "writer";
    public static final String Book_Publicer = "publicer";//出版社
    public static final String Book_Price = "price";
    public static final String Book_Rank = "rank";//等级或者评分
    public static final String Book_Comment = "comment";//简介
    public static final String Book_Img = "img";

    private static final String Creat_table = "create table admin(_id integer primary key autoincrement,user text , name text, password text,sex text, phone text, birthday text)";

    public static final String Creat_table1 = "create table bookinfo1 ("
            + id + " integer primary key autoincrement," + Book_Id + "," + Book_Name + "," + Book_Type + " text,"
            + Book_Writer + "," + Book_Publicer + "," + Book_Price + "," + Book_Rank + "," + Book_Comment + "," + Book_Img + " BLOB DEFAULT NULL)";

    public static final String Creat_table2 = "create table " + Table_Book + "("
            + id + " integer primary key autoincrement," + Book_Id + "," + Book_Name + "," + Book_Type + " text,"
            + Book_Writer + "," + Book_Publicer + "," + Book_Price + "," + Book_Rank + "," + Book_Comment + "," + Book_Img + " BLOB DEFAULT NULL)";

    private static final String Creat_table3 = "create table borrow(_Bid integer primary key autoincrement,Borname text,Bookid integer,bookname text,bookauthor text,nowtime text)";
    //借书表，Bid是借阅记录编号，Borname是借阅者名称，bookname 是书名，Bookid是书籍编号，bookauthor是作者名称，nowtime是当前系统时间
    private static final String Creat_table5 = "create table collect(_id integer primary key autoincrement,Borname text,Bookid integer,bookname text,bookauthor text,nowtime text,type text,rank text,price text,img blob)";

    private static final String Creat_table6 = "create table remark(_id integer primary key autoincrement,Rmkname text,Bookid integer,bookname text,nowtime text,rem text)";

    private static final String Creat_table4 = "create table pay(_Rid integer primary key autoincrement,Borname text,Bookid integer,bookname text,bookauthor text,nowtime text,borrowid integer)";
    //还书表，Rid是归还记录编号，Borname是借阅者名称，bookname 是书名，Bookid是书籍编号，bookauthor是作者名称，nowtime是当前系统时间
    private static final String Creat_table7 = "create table admin1(_id integer primary key autoincrement,user text , name text, password text,sex text, phone text, birthday text)";
    SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        db.execSQL(Creat_table);
        db.execSQL(Creat_table2);
        db.execSQL(Creat_table3);
        db.execSQL(Creat_table4);
        db.execSQL(Creat_table5);
        db.execSQL(Creat_table6);
        //初始化书单
        db.execSQL("insert into bookinfo (bookid,name,type,writer,publicer,price,rank,comment) values " +
                "(0,'书0','叙事','作者0','张三出版社0',20,9.0,'讲述了一段关于英子童年时的故事0')," +
                "(1,'书1','科幻','作者1','张三出版社1',21,9.1,'讲述了一段关于英子童年时的故事1')," +
                "(2,'书2','爱情','作者2','张三出版社2',22,9.2,'讲述了一段关于英子童年时的故事2')," +
                "(3,'书3','武侠','作者3','张三出版社3',23,9.3,'讲述了一段关于英子童年时的故事3')," +
                "(4,'书4','叙事','作者4','张三出版社4',24,9.4,'讲述了一段关于英子童年时的故事4')," +
                "(5,'书5','叙事','作者5','张三出版社5',25,9.5,'讲述了一段关于英子童年时的故事5')," +
                "(6,'书6','叙事','作者6','张三出版社6',26,9.6,'讲述了一段关于英子童年时的故事6')," +
                "(7,'书7','叙事','作者7','张三出版社7',27,9.7,'讲述了一段关于英子童年时的故事7')," +
                "(8,'书8','叙事','作者8','张三出版社8',28,9.8,'讲述了一段关于英子童年时的故事8')," +
                "(9,'书9','叙事','作者9','张三出版社9',29,9.9,'讲述了一段关于英子童年时的故事9')," +
                "(10,'书10','叙事','作者10','张三出版社10',210,9.10,'讲述了一段关于英子童年时的故事10')," +
                "(11,'书11','叙事','作者11','张三出版社11',211,9.11,'讲述了一段关于英子童年时的故事11')," +
                "(12,'书12','叙事','作者12','张三出版社12',212,9.12,'讲述了一段关于英子童年时的故事12')");
        //初始化用户
        db.execSQL("insert into admin (user,name,password,sex,phone,birthday) values " +
                "('1','用户1','1','男','12345678901','2022.11.20')," +
                "('2','用户2','2','男','12345678901','2022.11.20')," +
                "('3','用户3','3','男','12345678901','2022.11.20')," +
                "('4','用户4','4','女','12345678901','2022.11.20')," +
                "('5','用户5','5','女','12345678901','2022.11.20')," +
                "('6','用户6','6','女','12345678901','2022.11.20');");
    }

    public databaseHelp(Context context) {
        super(context, DB_NAME, null, 2);
    }

    //插入图片
    public void updateImg(ContentValues values, int id) {
        db = getWritableDatabase();
        db.update("bookinfo", values, "_id=?", new String[]{String.valueOf(id)});
        //db.execSQL("update bookinfo set img = "+img+" where _id ="+id);
    }

    //根据bookid和bookname查找评论
    public Cursor remarkInfo(String bookid, String bookname) {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM remark WHERE bookname = '" + bookname + "' AND Bookid = '" + bookid + "' order by nowtime desc;", null);
        return cursor;
    }

    //插入评论
    public boolean insertRemark(String Rmkname, String bookid, String bookname, String time, String remark) {
        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Rmkname", Rmkname);
        values.put("Bookid", bookid);
        values.put("bookname", bookname);
        values.put("nowtime", time);
        values.put("rem", remark);
        Log.i("vale", "insertRemark: " + values + "112312");
        long line = db.insert("remark", null, values);
        db.close();
        if (line != -1) {
            return true;
        } else {
            return false;
        }
    }

    //推荐
    public Cursor recommend() {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM bookinfo order by rank DESC", null);
        return cursor;
    }

    //bookinfoid重新排序
    public void sortbookinfoid() {
        db = getReadableDatabase();
        db.execSQL(Creat_table1);
        db.execSQL("insert into bookinfo1 (bookid,name,type,writer,publicer,price,rank,comment,img) select bookid,name,type,writer,publicer,price,rank,comment,img from bookinfo");
        db.execSQL("drop table bookinfo");
        db.execSQL("alter table bookinfo1 rename to bookinfo");
        db.close();
    }

    //userid重新排序
    public void sortuserid() {
        db = getReadableDatabase();
        db.execSQL(Creat_table7);
        db.execSQL("insert into admin1 (user,name,password,sex,phone,birthday) select user,name,password,sex,phone,birthday from admin");
        db.execSQL("drop table admin");
        db.execSQL("alter table admin1 rename to admin");
        db.close();
    }

    //根据_id和用户名查询boroow表中的数据
    public Cursor checkborrowinfo(String bookname, String name) {
        db = getReadableDatabase();
        //Cursor cursor = db.query(Table_Name3, null, "bookname = \'" +bookname+"\'Borname = \'"+name+"\'" , null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT bookname,Borname FROM borrow WHERE bookname = \'" + bookname + "\' AND Borname = \'" + name + "\';", null);
        //Cursor cursor = db.execSQL("SELECT bookname,Borname FROM borrow WHERE bookname = \'"+bookname+"\' AND Borname = \'"+name+"\';");
        return cursor;

    }

    //根据_id和用户名查询collect表中的数据
    public Cursor checkcollectinfo(String bookname, String name) {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT bookname,Borname FROM collect WHERE bookname = \'" + bookname + "\' AND Borname = \'" + name + "\';", null);
        return cursor;

    }


    //往admin表中插入信息
    public void insert(ContentValues values) {
        db = getReadableDatabase();
        db.insert(Table_Name1, null, values);
        db.close();
    }

    //把新添加的图书数据插入到book表中
    public boolean inserbooktdata(String bookid, String name, String type, String writer,
                                  String publicer, String price, String rank, String comment, byte[] img) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Book_Id, bookid);
        values.put(Book_Name, name);
        values.put(Book_Type, type);
        values.put(Book_Writer, writer);
        values.put(Book_Publicer, publicer);
        values.put(Book_Price, price);
        values.put(Book_Rank, rank);
        values.put(Book_Comment, comment);
        values.put(Book_Img, img);
        long line = db.insert("bookinfo", null, values);
        db.close();
        if (line != -1) {
            return true;
        } else {
            return false;
        }
    }

    //查询所有读者信息
    public Cursor query() {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name1, null, null, null, null, null, null);
        return cursor;

    }

    //删除读者的信息
    public void del(int id) {
        db = getReadableDatabase();
        db.delete(Table_Name1, "_id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //通过id查询读者信息
    public Cursor queryid(int id) {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name1, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
        return cursor;
    }

    //通过name查询读者信息
    public Cursor queryname(String name) {
        Log.i("inut:", "queryname: " + name);
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name1, null, "user=?", new String[]{name}, null, null, null);
        return cursor;
    }

    //往bookinfo表中插入数据
    public void insertbookinfo(ContentValues values) {
        db = getReadableDatabase();
        db.insert(Table_Name2, null, values);
        db.close();

    }

    //往bookinfo中查询所有数据
    public Cursor querybookinfo() {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name2, null, null, null, null, null, null);
        return cursor;
    }

    //往bookinfo表中通过_id查找
    public Cursor querybookinfoid(int id) {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name2, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
        return cursor;
    }

    //往bookinfo表中通过bookid查找
    public Cursor querybookinfobookid(int id) {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name2, null, "bookid=?", new String[]{String.valueOf(id)}, null, null, null);
        //mtCursor cursor = db.rawQuery("select * from bookinfo where bookid = "+id,null);
        return cursor;
    }

    //往bookinfo表中通过name查找
    public Cursor querybookinfoname(String name) {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name2, null, "name like ?", new String[]{"%" + name + "%"}, null, null, null, null);
        return cursor;
    }

    //删除图书信息
    public void delbookinfo(int id) {
        db = getReadableDatabase();
        db.delete(Table_Name2, "_id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //往borrow表中添加数据
    public void insertorrowo(ContentValues values) {
        db = getReadableDatabase();
        db.insert(Table_Name3, null, values);
        db.close();

    }

    //往collect表中添加数据
    public void insertocollect(ContentValues values) {
        db = getReadableDatabase();
        db.insert(Table_Name5, null, values);
        db.close();
    }

    //查询boroow表中的所有数据
    public Cursor queryborrowinfo() {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name3, null, null, null, null, null, null);
        return cursor;

    }

    //查询borrow表中的所有数据
    public List<Map<String, Object>> queryborrow() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from borrow order by Borname asc", null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("_Bid", cursor.getString(cursor.getColumnIndex("_Bid")));
            map.put("Borname", cursor.getString(cursor.getColumnIndex("Borname")));
            map.put("Bookid", cursor.getString(cursor.getColumnIndex("Bookid")));
            map.put("bookname", cursor.getString(cursor.getColumnIndex("bookname")));
            map.put("bookauthor", cursor.getString(cursor.getColumnIndex("bookauthor")));
            map.put("nowtime", cursor.getString(cursor.getColumnIndex("nowtime")));
            data.add(map);
        }
        return data;
    }
    //在collector表中根据用户查询

    public Cursor queryuser(String str) {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name5, null, "Borname=?", new String[]{str}, null, null, null);
        return cursor;
    }

    //查询borrow表中的所有数据
    public List<Map<String, Object>> queryborrow(String str) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name3, null, "Borname=?", new String[]{str}, null, null, null, null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("_Bid", cursor.getInt(cursor.getColumnIndex("_Bid")));
            map.put("Borname", cursor.getString(cursor.getColumnIndex("Borname")));
            map.put("Bookid", cursor.getInt(cursor.getColumnIndex("Bookid")));
            map.put("bookname", cursor.getString(cursor.getColumnIndex("bookname")));
            map.put("bookauthor", cursor.getString(cursor.getColumnIndex("bookauthor")));
            map.put("bortime", cursor.getString(cursor.getColumnIndex("nowtime")));
            data.add(map);
        }
        return data;
    }

    //通过删除borrow表的信息
    public void delborrow(int id) {
        db = getReadableDatabase();
        db.delete(Table_Name3, "Bookid=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //通过删除collect表的信息
    public void delcollect(String name) {
        db = getReadableDatabase();
        db.delete(Table_Name5, "bookname=?", new String[]{String.valueOf(name)});
        db.close();
    }

    public void delborrowbyname(String name) {
        if (db == null) {
            db = getReadableDatabase();
            db.delete(Table_Name3, "bookname=?", new String[]{String.valueOf(name)});
        }
        db.close();
    }

    //通过id查询borrow表里的信息
    public Cursor queryborrowinforname(String name) {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name3, null, "bookname=" + name, null, null, null, null);
        return cursor;
    }

    //通过id查询collect表里的信息
    public Cursor querycollectinfoid(int id) {
        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name5, null, "_Bid=" + id, null, null, null, null);
        return cursor;
    }


    //往pay表中添加数据
    public void insertpay(ContentValues values) {
        db = getReadableDatabase();
        db.insert(Table_Name4, null, values);
        db.close();

    }

    //查询pay表中的所有数据
    public List<Map<String, Object>> querypay() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name4, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("_Rid", cursor.getString(cursor.getColumnIndex("_Rid")));
            map.put("Borname", cursor.getString(cursor.getColumnIndex("Borname")));
            map.put("Bookid", cursor.getString(cursor.getColumnIndex("Bookid")));
            map.put("bookname", cursor.getString(cursor.getColumnIndex("bookname")));
            map.put("bookauthor", cursor.getString(cursor.getColumnIndex("bookauthor")));
            map.put("nowtime", cursor.getString(cursor.getColumnIndex("nowtime")));
            map.put("borrowid", cursor.getInt(cursor.getColumnIndex("borrowid")));
            data.add(map);
        }
        return data;
    }
    //在pay表中根据用户查询

    public List<Map<String, Object>> querypayuser(String str) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        db = getReadableDatabase();
        Cursor cursor = db.query(Table_Name4, null, "Borname=?", new String[]{str}, null, null, null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("_Rid", cursor.getString(cursor.getColumnIndex("_Rid")));
            map.put("Borname", cursor.getString(cursor.getColumnIndex("Borname")));
            map.put("Bookid", cursor.getString(cursor.getColumnIndex("Bookid")));
            map.put("bookname", cursor.getString(cursor.getColumnIndex("bookname")));
            map.put("bookauthor", cursor.getString(cursor.getColumnIndex("bookauthor")));
            map.put("nowtime", cursor.getString(cursor.getColumnIndex("nowtime")));
            map.put("borrowid", cursor.getInt(cursor.getColumnIndex("borrowid")));
            data.add(map);
        }
        return data;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(Creat_table);
                break;
            case 2:
                db.execSQL(Creat_table2);
                break;
            case 3:
                db.execSQL(Creat_table3);
                break;
            case 4:
                db.execSQL(Creat_table4);
                break;
            case 5:
                db.execSQL(Creat_table5);
                break;
            case 6:
                db.execSQL(Creat_table6);
                break;
            default:

        }

    }

    //打开外键
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

}
