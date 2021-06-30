package com.example.foodlabel6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;

public class DBHelper  extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "FoodItems", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE FoodItems(id serial primary key, upc text, name text, calories integer, sugar integer, sodium integer, protein integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop TABLE if exists FoodItems");
    }

    boolean insertFoodData(String upc, String name, int calories, int sugar, int sodium, int protein){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("upc", upc);
        row.put("name", name);
        row.put("calories", calories);
        row.put("sugar", sugar);
        row.put("sodium", sodium);
        row.put("protein", protein);

        if(DB.insert("FoodItems", null, row) < 0){
            return false;
        }else{
            return true;
        }
    }

    public JSONObject getData() throws JSONException {
        SQLiteDatabase DB = this.getWritableDatabase();
        JSONObject ret = new JSONObject();

        Cursor res = DB.rawQuery("Select " +
                "SUM(calories) as calories, " +
                "SUM(sugar) as sugar, " +
                "SUM(sodium) as sodium," +
                "SUM(protein) as protein from FoodItems", null);

        if (res.moveToFirst()) {
            ret.put("calories", res.getInt(res.getColumnIndex("calories")));
            ret.put("sugar", res.getInt(res.getColumnIndex("sugar")));
            ret.put("sodium", res.getInt(res.getColumnIndex("sodium")));
            ret.put("protein", res.getInt(res.getColumnIndex("protein")));
        }else{
            System.out.println("Wasnt there");
        }
        return ret;
    }

    /*public void getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor res = DB.rawQuery("Select * from FoodItems", null);

//        if(res.getCount() == 0){
//
//        }

        while(res.moveToNext()){
            System.out.println(res.getString(1));
            System.out.println("One More");
        }
    }*/
}
