package com.example.shubham.taskhand.constants;

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
    public static final String ALARM_NOTIFICATION = "com.example.shubham.taskhand.alarm.notification";
    public static final String ALARM_ACTION = "com.example.shubham.taskhand.alarm.action";
    public static final String ALARM_CATEGORY = "com.example.shubham.taskhand.alarm.category";
    public static final String ALARM_INPUT = "com.example.shubham.taskhand.OneTimeAlarmInput";
    public static final String ALARM_TYPE="com.example.shubham.taskhand.dataType";
    public static final String ALARM_STORE_FILE_NAME ="AlarmStore ";

}
