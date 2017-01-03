package hy.cityselectedtest;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by huangyue on 2016/12/23.
 */

public class DBManager {
    private static String dbName = "city_db.db";
    private static String dbPath;



    public static boolean copyDB(Context context){
        dbPath = context.getExternalFilesDir(null).getAbsolutePath()+dbName;
        AssetManager assetManager = context.getAssets();
        FileOutputStream os = null;
        InputStream is = null;
        try {
            is = assetManager.open("china_cities.db");
            os = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer,0,buffer.length))>0){
                os.write(buffer,0,length);
            }
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                is.close();
                os.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }

    public static List<City> getAllCity(){
        List<City> cities = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
        Cursor cursor = db.rawQuery("select * from city",null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setName(cursor.getString(cursor.getColumnIndex("name")));
            city.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
            cities.add(city);
        }
        Collections.sort(cities,comparator);
        db.close();
        cursor.close();
        return cities;
    }

    private static Comparator<City> comparator = new Comparator<City>() {
        @Override
        public int compare(City o1, City o2) {
//            if(o1.getPinyin().charAt(0) >=o2.getPinyin().charAt(0)){
//                return 1;
//            }
//            return -1;
            String a = o1.getPinyin().substring(0,1);
            String b = o2.getPinyin().substring(0,1);
            return a.compareTo(b);
        }
    };

    public static List<City> searchCity(String text){
        String sql = "select * from city where pinyin like \"%"+text+"%\" or name like \"%"+text+"%\"";
        List<City> cities = new ArrayList<>();
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setName(cursor.getString(cursor.getColumnIndex("name")));
            city.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
            cities.add(city);
        }
        Collections.sort(cities,comparator);
        database.close();
        cursor.close();
        return cities;
    }
}
