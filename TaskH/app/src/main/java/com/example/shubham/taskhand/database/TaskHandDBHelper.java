package com.example.shubham.taskhand.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shubham.taskhand.utility.AppContext;
import com.example.shubham.taskhand.utility.Logger;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * DB Helper Class for Database operations in App
 * <p>
 * Created by shubham on 2/12/16.
 */
public class TaskHandDBHelper extends SQLiteOpenHelper {
    private static final String TAG = TaskHandDBHelper.class.getSimpleName();
    //database info and version
    private static final String DATABASE_NAME = "TASKHAND.DB";
    private static final int DB_VERSION = 1;
    //insert table query
    private static final String TABLE_QUERY = "CREATE TABLE " + DatabaseContract.TABLE_NAME + "("
            + DatabaseContract.TaskDetail.TASK_ID + " INTEGER PRIMARY KEY,"
            + DatabaseContract.TaskDetail.TASK_NAME + " TEXT UNIQUE,"
            + DatabaseContract.TaskDetail.TASK_DETAIL + " TEXT,"
            + DatabaseContract.TaskDetail.TASK_PRIORITY + " TEXT,"
            + DatabaseContract.TaskDetail.TASK_TIME_CREATION + " INTEGER,"
            + DatabaseContract.TaskDetail.TASK_TIME_REMINDER + " INTEGER" + ")";

    public TaskHandDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
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
    public static long addTask(String iTaskName, String iTaskDetail, String iTaskPriority, long iTaskReminder) {
        long outChecking = 0;
        SQLiteDatabase db = new TaskHandDBHelper(AppContext.getContext()).getWritableDatabase();
        ContentValues iValues = new ContentValues();
        iValues.put(DatabaseContract.TaskDetail.TASK_NAME, iTaskName);
        iValues.put(DatabaseContract.TaskDetail.TASK_DETAIL, iTaskDetail);
        iValues.put(DatabaseContract.TaskDetail.TASK_PRIORITY, iTaskPriority);
        iValues.put(DatabaseContract.TaskDetail.TASK_TIME_REMINDER, iTaskReminder);
        iValues.put(DatabaseContract.TaskDetail.TASK_TIME_CREATION, getDateTime());//getDateTime() for getting current timestamp
        //inserting in table
        try {
            outChecking = db.insertOrThrow(DatabaseContract.TABLE_NAME, null, iValues);
        } catch (SQLiteException e) {
            Logger.error(TAG + "DB operation :AddTask :", " insertion unsuccessful due to" + e.getMessage(), e);
        }
        iValues.clear();
        db.close();
        return outChecking;
    }

    /***
     * method for adding data in list view
     *
     * @return :ArrayList<TaskHandDataModel> which contains arraylist of data from database
     */
    public static ArrayList<TaskHandDataModel> getTaskListData() {

        SQLiteDatabase db = new TaskHandDBHelper(AppContext.getContext()).getWritableDatabase();
        ArrayList<TaskHandDataModel> outTaskList;
        outTaskList = new ArrayList<TaskHandDataModel>();
        Cursor cursor = null;
        String[] iProjection = {
                DatabaseContract.TaskDetail.TASK_ID,
                DatabaseContract.TaskDetail.TASK_NAME,
                DatabaseContract.TaskDetail.TASK_DETAIL,
                DatabaseContract.TaskDetail.TASK_PRIORITY,
                DatabaseContract.TaskDetail.TASK_TIME_REMINDER,
                DatabaseContract.TaskDetail.TASK_TIME_CREATION
        };
        try {
            cursor = db.query(DatabaseContract.TABLE_NAME, iProjection, null, null, null, null, DatabaseContract.TaskDetail.TASK_NAME + " ASC");
            Logger.debug(TAG + "DB operation", "implementing getting result from database");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    TaskHandDataModel taskHandDataModel = new TaskHandDataModel(cursor.getInt(0), cursor.getString(1), cursor.getLong(4),
                            cursor.getString(3), cursor.getString(2), cursor.getLong(5));
                    outTaskList.add(taskHandDataModel);
                }
            }
        } catch (CursorIndexOutOfBoundsException | SQLiteException | NullPointerException e) {
            e.printStackTrace();
            Logger.error(TAG, " Exception occurred in getting Data", e);
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return outTaskList;
    }

    /***
     * method for getting current date and ic_time fro storing in dB
     *
     * @return :the current date and ic_time(Long data type)
     */
    private static long getDateTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /***
     * Method of updating a already created task
     *
     * @param id            :id of task (Integer)
     * @param iTaskName     :name of Task(String)
     * @param iTaskDetail   : details of task(String)
     * @param iTaskPriority :Priority of task(String)
     * @param iTaskReminder :Reminder ic_time of task(Long)
     * @return :true or false whether task is updated or not
     */
    public static boolean updateTask(int id, String iTaskName, String iTaskDetail,
                                     String iTaskPriority, long iTaskReminder) {
        SQLiteDatabase db = new TaskHandDBHelper(AppContext.getContext()).getWritableDatabase();
        ContentValues iValues = new ContentValues();
        iValues.put(DatabaseContract.TaskDetail.TASK_NAME, iTaskName);
        iValues.put(DatabaseContract.TaskDetail.TASK_DETAIL, iTaskDetail);
        iValues.put(DatabaseContract.TaskDetail.TASK_PRIORITY, iTaskPriority);
        iValues.put(DatabaseContract.TaskDetail.TASK_TIME_REMINDER, iTaskReminder);
        iValues.put(DatabaseContract.TaskDetail.TASK_TIME_CREATION, getDateTime());
        int outRowUpdated = db.update(DatabaseContract.TABLE_NAME, iValues, DatabaseContract.TaskDetail.TASK_ID
                + "= ? ", new String[]{Integer.toString(id)});
        db.close();
        return outRowUpdated >= 1;

    }

    /**
     * Method for Deletion of a particular row whose id is passed
     *
     * @param id : id of row which has to be deleted
     * @return : number of row deleted
     */

    public static int deleteNotes(int id) {
        Logger.debug("DELETE", "" + id);
        SQLiteDatabase db = new TaskHandDBHelper(AppContext.getContext()).getWritableDatabase();
        int outRowDeleted = 0;
        try {
            outRowDeleted = db.delete(DatabaseContract.TABLE_NAME, DatabaseContract.TaskDetail.TASK_ID + "=?",
                    new String[]{String.valueOf(id)});
        } catch (SQLiteException e) {
            e.printStackTrace();
            Logger.error(TAG, "Deletion Error", e);
        }

        db.close();
        return outRowDeleted;

    }

    /***
     * Method for searching of task in db with help of Task Name
     *
     * @param name :name or hint of Task Name which may be present or not in db
     * @return : List of all the tasks which contain that hint
     */
    public static ArrayList<TaskHandDataModel> getTaskHandSearchList(String name) {
        TaskHandDataModel mTaskHandSearchList;
        ArrayList<TaskHandDataModel> outList = new ArrayList<TaskHandDataModel>();
        SQLiteDatabase db = new TaskHandDBHelper(AppContext.getContext()).getWritableDatabase();
        Cursor cursor = null;
        String[] iProjection = {
                DatabaseContract.TaskDetail.TASK_ID,
                DatabaseContract.TaskDetail.TASK_NAME,
                DatabaseContract.TaskDetail.TASK_DETAIL,
                DatabaseContract.TaskDetail.TASK_PRIORITY,
                DatabaseContract.TaskDetail.TASK_TIME_REMINDER,
                DatabaseContract.TaskDetail.TASK_TIME_CREATION
        };

        try {
            cursor = db.query(DatabaseContract.TABLE_NAME, iProjection,
                    DatabaseContract.TaskDetail.TASK_NAME + " LIKE ?", new String[]{"%" + name + "%"},
                    null, null, null);
            Logger.debug(TAG + "DB operation dataSearch", "implementing getting result from database");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    mTaskHandSearchList = new TaskHandDataModel(cursor.getInt(0), cursor.getString(1), cursor.getLong(4),
                            cursor.getString(3), cursor.getString(2), cursor.getLong(5));

                    outList.add(mTaskHandSearchList);
                }
            }
        } catch (CursorIndexOutOfBoundsException | SQLiteException | NullPointerException e) {
            e.printStackTrace();
            Logger.error(TAG + "DB SEARCH", "" + e.getMessage() + "\n", e);
        } finally {
            if (cursor != null || db != null) {
                cursor.close();
                db.close();

            }
        }
        return outList;
    }

    @Override
    public void onCreate(SQLiteDatabase taskHandDb) {
        //creating table TaskHandTable
        try {
            taskHandDb.execSQL(TABLE_QUERY);
            Logger.debug(TAG, "table creation successful ");
        } catch (SQLiteException e) {
            e.printStackTrace();
            Logger.error(TAG, e.getMessage() + "\n", e.getCause());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // sqLiteDatabase.execSQL(TABLE_QUERY);
    }

}