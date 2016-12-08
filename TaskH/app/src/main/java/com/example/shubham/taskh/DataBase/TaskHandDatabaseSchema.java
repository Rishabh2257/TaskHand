package com.example.shubham.taskh.DataBase;

/**
 * Created by shubham on 2/12/16.
 */
public class TaskHandDatabaseSchema {

    public static final String TABLE_NAME = "TaskHandTable";

    public static abstract class TaskDetail {
        public static final String TASK_ID = "_id";
        public static final String TASK_NAME = "task_name";
        public static final String TASK_DETAIL = "task_detail";
        public static final String TASK_PRIORITY = "task_priority";
        public static final String TASK_TIME_CREATION = "task_time_creation";
        public static final String TASK_TIME_REMINDER = "task_time_reminder";

    }
}
