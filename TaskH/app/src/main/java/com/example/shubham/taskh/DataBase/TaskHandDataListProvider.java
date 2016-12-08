package com.example.shubham.taskh.DataBase;

/**
 * Created by shubham on 5/12/16.
 */
public class TaskHandDataListProvider {

    private String mTaskName;
    private String mTaskDetail;
    private String mTaskPriority;
    private long mTaskReminderTime;
    private long mTaskCreationTime;
    private int mTask_Id;

    public int getmTask_Id() {
        return mTask_Id;
    }

    public void setmTask_Id(int mTask_Id) {
        this.mTask_Id = mTask_Id;
    }

    public TaskHandDataListProvider(int mTask_Id, String mTaskName, long mTaskReminderTime, String mTaskPriority, String mTaskDetail, long mTaskCreationTime) {
        this.mTask_Id=mTask_Id;

        this.mTaskName = mTaskName;
        this.mTaskReminderTime = mTaskReminderTime;
        this.mTaskPriority = mTaskPriority;
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
}
