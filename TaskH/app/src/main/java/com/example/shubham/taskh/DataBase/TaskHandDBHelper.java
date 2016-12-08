package com.example.shubham.taskh.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shubham on 2/12/16.
 */
public class TaskHandDBHelper extends SQLiteOpenHelper {
    private static final String TAG = TaskHandDBHelper.class.getSimpleName();
    //database info and version
    private static final String DATABASE_NAME = "TASKHAND.DB";
    private static final int DB_VERSION = 1;
    //insert table query
    private static final String TABLE_QUERY = "CREATE TABLE " + TaskHandDatabaseSchema.TABLE_NAME + "("
            + TaskHandDatabaseSchema.TaskDetail.TASK_ID + " INTEGER PRIMARY KEY,"
            + TaskHandDatabaseSchema.TaskDetail.TASK_NAME + " TEXT NOT NULL,"
            + TaskHandDatabaseSchema.TaskDetail.TASK_DETAIL + " TEXT,"
            + TaskHandDatabaseSchema.TaskDetail.TASK_PRIORITY + " TEXT,"
            + TaskHandDatabaseSchema.TaskDetail.TASK_TIME_CREATION + " INTEGER,"
            + TaskHandDatabaseSchema.TaskDetail.TASK_TIME_REMINDER + " INTEGER" + ")";
    ArrayList<TaskHandDataListProvider> mTaskHandDataListProvidersList;
    private TaskHandDataListProvider mTaskHandDataListProvider;

    public TaskHandDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase taskHandDb) {
        //creating table TaskHandTable
        try {
            taskHandDb.execSQL(TABLE_QUERY);
            Log.e(TAG, "table creation successful ");
        } catch (SQLiteException e) {

            Log.getStackTraceString(e);
        }
    }

    /***
     * addTask method for storing task in DB
     *
     * @param iTaskName     :Task Name
     * @param iTaskDetail:  Detail About task
     * @param iTaskPriority :Priority of task
     * @param iTaskReminder : Time of Reminder
     * @return : long value for checking insertion is does or not
     */
    public long addTask(String iTaskName, String iTaskDetail, String iTaskPriority, long iTaskReminder, SQLiteDatabase db) {
        long oChecking = 0;
        ContentValues iValues = new ContentValues();
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_NAME, iTaskName);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_DETAIL, iTaskDetail);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_PRIORITY, iTaskPriority);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_TIME_REMINDER, iTaskReminder);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_TIME_CREATION, getDateTime());//getDateTime() for getting current timestamp
        //inserting in table
        try {
            oChecking = db.insertOrThrow(TaskHandDatabaseSchema.TABLE_NAME, null, iValues);
            Log.e(TAG + "DB operation: AddTask :", "insertion successful of one row ");
        } catch (SQLiteException e) {
            Log.e(TAG + "DB operation :AddTask :", " insertion unsuccessful", e);
        }
        db.close();
        return oChecking;
    }

    /***
     * method for adding data in list view
     *
     * @param iSqLiteDatabase : instance of database
     * @return :ArrayList<TaskHandDataListProvider> which contains arraylist of data from database
     */

    public ArrayList<TaskHandDataListProvider> getTaskListData(SQLiteDatabase iSqLiteDatabase) {
        //mTaskHandDataListProvider=null;
        mTaskHandDataListProvidersList = new ArrayList<TaskHandDataListProvider>();
        Cursor cursor = null;
        String[] iProjection = {
                TaskHandDatabaseSchema.TaskDetail.TASK_ID,
                TaskHandDatabaseSchema.TaskDetail.TASK_NAME,
                TaskHandDatabaseSchema.TaskDetail.TASK_DETAIL,
                TaskHandDatabaseSchema.TaskDetail.TASK_PRIORITY,
                TaskHandDatabaseSchema.TaskDetail.TASK_TIME_REMINDER,
                TaskHandDatabaseSchema.TaskDetail.TASK_TIME_CREATION
        };
        try {
            cursor = iSqLiteDatabase.query(TaskHandDatabaseSchema.TABLE_NAME, iProjection, null, null, null, null, null);
            Log.d(TAG + "DB operation", "implementing getting result from database");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    mTaskHandDataListProvider = new TaskHandDataListProvider(cursor.getInt(0), cursor.getString(1), cursor.getLong(4),
                            cursor.getString(3), cursor.getString(2), cursor.getLong(5));

                    Log.e("data", "" + cursor.getString(0));
                    Log.e("data", "" + cursor.getString(1));
                    Log.e("data", "" + cursor.getString(2));
                    Log.e("data", "" + cursor.getString(3));
                    Log.e("data", "" + cursor.getString(4));
                    Log.e("data", "" + cursor.getString(5));

                    mTaskHandDataListProvidersList.add(mTaskHandDataListProvider);
                }
            }
        } catch (CursorIndexOutOfBoundsException | SQLiteException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return mTaskHandDataListProvidersList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TABLE_QUERY);
    }

    /***
     * method for getting current date and time fro storing in dB
     *
     * @return :in string form the current date and time
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean updateTask(int id, String iTaskName, String iTaskDetail,
                              String iTaskPriority, long iTaskReminder, SQLiteDatabase db) {
        ContentValues iValues = new ContentValues();
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_NAME, iTaskName);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_DETAIL, iTaskDetail);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_PRIORITY, iTaskPriority);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_TIME_REMINDER, iTaskReminder);
        iValues.put(TaskHandDatabaseSchema.TaskDetail.TASK_TIME_CREATION, getDateTime());
        db.update(TaskHandDatabaseSchema.TABLE_NAME, iValues, TaskHandDatabaseSchema.TaskDetail.TASK_ID
                + "= ? ",new String[]{Integer.toString(id)});
        return true;
    }

    public void deleteNotes(int id) {

    }
}