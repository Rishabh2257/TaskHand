package com.example.shubham.taskh.utility;

import android.os.Bundle;
import android.widget.Toast;

import com.example.shubham.taskh.constants.StringConstants;
import com.example.shubham.taskh.database.TaskHandDBHelper;
import com.example.shubham.taskh.database.TaskHandDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by shubham on 16/12/16.
 */
public class TaskHandHelper {

    /**
     * Display a toast with supplied message for {@code SHORT} period of time.
     *
     * @param msg The message to be shown. Can be formatted text.
     */
    public static void toastShort(CharSequence msg) {
        if (msg != null) {
            Toast.makeText(AppContext.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Display a toast with supplied message for {@code LONG} period of time.
     *
     * @param msg The message to be shown. Can be formatted text.
     */
    public static void toastLong(CharSequence msg) {
        if (msg != null) {
            Toast.makeText(AppContext.getContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method for setting data into Bundle after taking data from ArrayList which is passed here
     *
     * @param iTaskHandArrayList : list which contain data
     * @return : return the bundle which contain data which has to be passed
     */
    public static Bundle setData(ArrayList<TaskHandDataModel> iTaskHandArrayList, int iPosition) {

        int id = iTaskHandArrayList.get(iPosition).getTask_Id();
        String name = iTaskHandArrayList.get(iPosition).getTaskName();
        String detail = iTaskHandArrayList.get(iPosition).getTaskDetail();
        String priority = iTaskHandArrayList.get(iPosition).getTaskPriority();
        long reminderTime = iTaskHandArrayList.get(iPosition).getTaskReminderTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Logger.debug("task  data :",
                "" + iPosition + " " + name + " " + detail + " " + priority + " "
                        + simpleDateFormat.format(reminderTime));
        Bundle outBundle = new Bundle();
        outBundle.putInt(StringConstants.TASK_ID, id);
        outBundle.putString(StringConstants.TASK_NAME, name);
        outBundle.putString(StringConstants.TASK_DETAIL, detail);
        outBundle.putString(StringConstants.TASK_PRIORITY, priority);
        outBundle.putLong(StringConstants.TASK_REMINDER_TIME, reminderTime);
        return outBundle;
    }

    /***
     * Utility Method for returning list of values taken from TASKHAND.DB of app
     *
     * @return :ArrayList with values
     */
    public static ArrayList<TaskHandDataModel> getTaskHandArrayList() {
        //getting ArrayList of Values from helper Class Method: getTaskListData
        return TaskHandDBHelper.getTaskListData();
    }

}
