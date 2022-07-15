    package com.example.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    public DataBase( Context context) {
        super(context, "tasks.dp", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tasks(id INT PRIMARY KEY , name VARCHAR(15) NOT NULL, date VARCHAR(30) NOT NULL , done_or_no VARCHAR(6) NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
    }
    public String insert(Task a){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues c_v= new ContentValues();
        c_v.put("id",a.getId());
        c_v.put("name",a.getName());
        c_v.put("date",a.getDate().getDate());
        c_v.put("done_or_no",String.valueOf(a.isDone()));
        long h = database.insert("tasks",null,c_v);
        if (h != -1){
            return "Successful operation";
        }else {
            return "FAILED";
        }
    }
    public ArrayList<Task> getData(){
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM tasks",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            tasks.add(new Task(
                c.getInt(0),
                c.getString(1),
                getDate(c.getString(2)),
                Boolean.valueOf(c.getString(3))
            ));
            c.moveToNext();
        }
        return tasks;
    }

    public ArrayList<Integer> getIDS(){
        ArrayList<Integer> integers = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM tasks",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            integers.add(c.getInt(0));
        }
        return integers;
    }
    public void delete(int id){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("tasks","id=?",new String[]{String.valueOf(id)});
    }
    public void update(int id,Task a){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("name",a.getName());
        values.put("date",a.getDate().getDate());
        values.put("done_or_no",String.valueOf(a.isDone()));
        database.update("tasks",values,"id=?",new String[]{String.valueOf(id)});
    }
    public void updateStatues(int id , Task a){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("name",a.getName());
        values.put("date",a.getDate().getDate());
        values.put("done_or_no",String.valueOf(!a.isDone()));
        database.update("tasks",values,"id=?",new String[]{String.valueOf(id)});
    }
    private Date getDate(String a){
        String day="",month="",year="",S_counter="";
        int counter=0;
        for (int i =0 ; i<a.length();i++){
            if (a.charAt(i) != '/'){
                S_counter=S_counter+a.charAt(i);
            }
            if (a.charAt(i) == '/'){
                counter++;
                if (counter == 1){
                    day=S_counter;
                    S_counter="";
                }else if(counter ==2){
                    month=S_counter;
                    S_counter="";
                    for (int y =i+1 ; y<a.length();y++){
                        S_counter=S_counter+a.charAt(y);
                    }
                    year=S_counter;
                    break;
                }
            }

        }
        return new Date(Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(year));
    }
}
