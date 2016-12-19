package com.example.shubham.taskh.constants;

/**
 * Created by shubham on 16/12/16.
 */
public class StringConstants {

    public static final String PRIORITY_LOWEST = "Lowest";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_HIGHEST = "Highest";

    //key for putting data and retrieving data
    public static final String TASK_ID = "taskId";
    public static final String TASK_NAME = "taskName";
    public static final String TASK_DETAIL = "taskDetail";
    public static final String TASK_PRIORITY = "taskPriority";
    public static final String TASK_REMINDER_TIME = "taskReminder";

    //alarm keys and filters
    public static final String ALARM_ID = "content://com.example.shubham/Alarms/";
    public static final String ALARM_NOTIFICATION = "com.example.shubham.taskh.alarm.notification";
    public static final String ALARM_ACTION = "com.example.shubham.taskh.alarm.action";
    public static final String ALARM_CATEGORY = "com.example.shubham.taskh.alarm.category";
    public static final String ALARM_INPUT = "com.example.shubham.taskh.OneTimeAlarmInput";
    public static final String ALARM_TYPE="com.example.shubham.taskh.dataType";
    public static final String ALARM_STORE_FILE_NAME ="AlarmStore ";

}
