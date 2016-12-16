package com.example.shubham.taskh.database;

import android.text.TextUtils;
import android.util.Log;

import com.example.shubham.taskh.constants.IntegerConstants;
import com.example.shubham.taskh.constants.StringConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by shubham on 5/12/16.
 */
public class TaskHandDataModel {

    private String mTaskName;
    private String mTaskDetail;
    private String mTaskPriority;
    private long mTaskReminderTime;
    private long mTaskCreationTime;
    private int mTask_Id;
    private int mPriority;

    /***
     * Constructor of Model Class
     *
     * @param mTask_Id           : Id of Task(Integer)
     * @param mTaskName          : Name of Task(String)
     * @param mTaskReminderTime  :Reminder Time of Task(Store in Long)
     * @param mTaskPriority      : Priority of Task(String)
     * @param mTaskDetail:       Detail of Task(String)
     * @param mTaskCreationTime: Creation time of Task(Store in Long)
     */
    public TaskHandDataModel(int mTask_Id, String mTaskName, long mTaskReminderTime,
                             String mTaskPriority, String mTaskDetail, long mTaskCreationTime) {
        this.mTask_Id = mTask_Id;
        this.mTaskName = mTaskName;
        this.mTaskReminderTime = mTaskReminderTime;
        setTaskPriority(mTaskPriority);
        this.mTaskDetail = mTaskDetail;
        this.mTaskCreationTime = mTaskCreationTime;
    }

    /***
     * Method for sorting list which is passed to it
     * on the basis of priority
     *
     * @param iList : ArrayList which has to be sorted
     */
    public static void sortArrayListWithPriority(ArrayList<TaskHandDataModel> iList) {
        if (iList.size() != 0) {
            Collections.sort(iList, new Comparator<TaskHandDataModel>() {
                @Override
                public int compare(TaskHandDataModel taskHandDataModel1, TaskHandDataModel taskHandDataModel2) {

                    Integer priority1 = taskHandDataModel1.getPriority();
                    Integer priority2 = taskHandDataModel2.getPriority();
                    Log.d("Priority", "" + priority1 + " " + priority2);
                    return priority1.compareTo(priority2);
                }
            });
        } else
            Log.e("error in List", "List is empty");
    }

    /**
     * Method for sorting list which is passed to it
     * on the basis of creation time or update time
     *
     * @param iList : ArrayList which has to be sorted
     */
    public static void sortArrayListWithCreationTime(ArrayList<TaskHandDataModel> iList) {
        if (iList.size() != 0) {
            Collections.sort(iList, new Comparator<TaskHandDataModel>() {
                @Override
                public int compare(TaskHandDataModel taskHandDataModel1, TaskHandDataModel taskHandDataModel2) {

                    Long creationTime1 = taskHandDataModel1.getTaskCreationTime();
                    Long creationTime2 = taskHandDataModel2.getTaskCreationTime();
                    return creationTime1.compareTo(creationTime2);
                }
            });
        } else
            Log.e("error in List", "List is empty");
    }

    /***
     * Method for Returning Priorities
     *
     * @return :mPriority (Integer)
     */
    public int getPriority() {
        return mPriority;
    }

    /***
     * Method for setting Priority
     *
     * @param mPriority : take Integer type priority value(Integer)
     */
    public void setPriority(int mPriority) {
        if (mPriority != 0) {
            this.mPriority = mPriority;
        }
    }

    /***
     * Method for getting the TaskId
     *
     * @return :mTaskId(Integer)
     */
    public int getTask_Id() {
        return mTask_Id;
    }

    /***
     * Method for setting TaskId
     *
     * @param mTask_Id: take TaskId(Integer)
     */
    public void setTask_Id(int mTask_Id) {
        if (mTask_Id != 0) {
            this.mTask_Id = mTask_Id;
        }
    }

    /**
     * Method for getting the Name of task
     *
     * @return : mTaskName (String)
     */
    public String getTaskName() {
        return mTaskName;
    }

    /***
     * Method for setting name of Task
     *
     * @param mTaskName :take name of task (String)
     */
    public void setTaskName(String mTaskName) {
        if (mTaskName != null) {
            this.mTaskName = mTaskName;
        } else {
            this.mTaskName = "Task";
        }
    }

    /***
     * Method for getting Task Details
     *
     * @return :mTaskDetail(String)
     */
    public String getTaskDetail() {
        return mTaskDetail;
    }

    /***
     * Method for setting Task Details
     *
     * @param mTaskDetail :Contain Detail of Task
     */
    public void setTaskDetail(String mTaskDetail) {
        if (mTaskDetail != null)
            this.mTaskDetail = mTaskDetail;
        else
            this.mTaskDetail = "Task Detail";

    }

    /***
     * Method for getting Priorities of Task
     *
     * @return : mTaskPriority(String)
     */
    public String getTaskPriority() {
        return mTaskPriority;
    }

    /***
     * Method for setting the priority of task
     * also set the priority in integer for sorting
     *
     * @param mTaskPriority : Priority of Task in String Format
     */
    public void setTaskPriority(String mTaskPriority) {
        if (!TextUtils.isEmpty(mTaskPriority)) {
            this.mTaskPriority = mTaskPriority;

        } else {
            this.mTaskPriority = StringConstants.PRIORITY_LOWEST;
        }

        //switching for sorting purpose in sortArrayListWithPriority Method
        switch (mTaskPriority) {
            case StringConstants.PRIORITY_LOWEST:
                this.mPriority = IntegerConstants.PRIORITY_LOWEST;
                break;
            case StringConstants.PRIORITY_MEDIUM:
                this.mPriority = IntegerConstants.PRIORITY_MEDIUM;
                break;
            case StringConstants.PRIORITY_HIGHEST:
                this.mPriority = IntegerConstants.PRIORITY_HIGHEST;
                break;
        }
    }

    /***
     * Method for getting Reminder time and Date of Task in Long type which is stored in DB
     *
     * @return :MTaskReminderTime(Long)
     */
    public long getTaskReminderTime() {
        return mTaskReminderTime;
    }

    /***
     * Method for setting Reminder Time and Date
     *
     * @param mTaskReminderTime : Reminder Time in Long Type for storing in DB
     */
    public void setTaskReminderTime(long mTaskReminderTime) {
        if (mTaskReminderTime != 0) {
            this.mTaskReminderTime = mTaskReminderTime;
        } else {
            this.mTaskReminderTime = 0;
        }
    }

    /***
     * Method for Getting Creation and Update Time and Date of Task
     *
     * @return : mTaskCreationTime(Long)
     */
    public long getTaskCreationTime() {
        return mTaskCreationTime;
    }

    /***
     * Method for setting Creation and update time and Date of Task
     *
     * @param mTaskCreationTime :Time when the task is updated or created which is stored in DB
     */
    public void setTaskCreationTime(long mTaskCreationTime) {
        this.mTaskCreationTime = mTaskCreationTime;
    }
}