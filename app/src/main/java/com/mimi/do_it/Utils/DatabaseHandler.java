package com.mimi.do_it.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mimi.do_it.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";

    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TODO_TABLE + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK + " TEXT, " +
                    STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_TABLE); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, task.getStatus());
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = db.query(TODO_TABLE, null, null, null, null, null, null);

        if (cur != null && cur.moveToFirst()) {
            do {
                ToDoModel t = new ToDoModel();
                t.setId(cur.getInt(cur.getColumnIndex(ID)));
                t.setTask(cur.getString(cur.getColumnIndex(TASK)));
                t.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                taskList.add(t);
            } while (cur.moveToNext());
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }
}
