package com.example.shubham.taskh.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by shubham on 5/12/16.
 */
public class TaskHandDataProvider {

    private String mTaskName;
    private String mTaskDetail;
    private String mTaskPriority;
    private long mTaskReminderTime;
    private long mTaskCreationTime;
    private int mTask_Id;

    public int getmPriority() {
        return mPriority;
    }

    public void setmPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    private int mPriority;

    public int getmTask_Id() {
        return mTask_Id;
    }

    public void setmTask_Id(int mTask_Id) {
        this.mTask_Id = mTask_Id;
    }

    public TaskHandDataProvider(int mTask_Id, String mTaskName, long mTaskReminderTime, String mTaskPriority, String mTaskDetail, long mTaskCreationTime) {
        this.mTask_Id=mTask_Id;

        this.mTaskName = mTaskName;
        this.mTaskReminderTime = mTaskReminderTime;
        setmTaskPriority(mTaskPriority);
        //mTaskPriority = mTaskPriority;
        this.mTaskDetail = mTaskDetail;
        this.mTaskCreationTime = mTaskCreationTime;
    }

    public String getmTaskName() {
        return mTaskName;
    }

    public void setmTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    public String getmTaskDetail() {
        return mTaskDetail;
    }

    public void setmTaskDetail(String mTaskDetail) {
        this.mTaskDetail = mTaskDetail;
    }

    public String getmTaskPriority() {
        return mTaskPriority;
    }

    public void setmTaskPriority(String mTaskPriority) {

        switch (mTaskPriority)
        {
            case "Lowest":
                this.mPriority=1;
                break;
            case "Medium":
                this.mPriority=2;
                break;
            case "Highest":
                this.mPriority=3;
                break;
        }

      this.mTaskPriority = mTaskPriority;
    }

    public long getmTaskReminderTime() {
        return mTaskReminderTime;
    }

    public void setmTaskReminderTime(long mTaskReminderTime) {
        this.mTaskReminderTime = mTaskReminderTime;
    }

    public long getmTaskCreationTime() {
        return mTaskCreationTime;
    }

    public void setmTaskCreationTime(long mTaskCreationTime) {
        this.mTaskCreationTime = mTaskCreationTime;
    }

    public static void sortArrayListWithPriority(ArrayList<TaskHandDataProvider> list )
    {
        Collections.sort(list, new Comparator<TaskHandDataProvider>() {
            @Override
            public int compare(TaskHandDataProvider taskHandDataProvider1, TaskHandDataProvider taskHandDataProvider2) {

                Integer priority1 = taskHandDataProvider1.getmPriority();
                Integer priority2 = taskHandDataProvider2.getmPriority();
                Log.d("Priority",""+priority1+" "+priority2);
                return priority1.compareTo(priority2);
            }
        });
    }

    public static void sortArrayListWithCreationTime(ArrayList<TaskHandDataProvider> list )
    {
        Collections.sort(list, new Comparator<TaskHandDataProvider>() {
            @Override
            public int compare(TaskHandDataProvider taskHandDataProvider1, TaskHandDataProvider taskHandDataProvider2) {

                Long creationTime1= taskHandDataProvider1.getmTaskCreationTime();
                Long creationTime2= taskHandDataProvider2.getmTaskCreationTime();

                return creationTime1.compareTo(creationTime2);
            }
        });
    }

}
