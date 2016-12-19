package com.example.shubham.taskh.alarm;

import java.io.Serializable;

/**
 * AlarmDetails Class for setting and getting alarm details for a task
 */
public class AlarmDetails implements Serializable {

    private long mTriggerTime;
    private String mAlarmNotificationMsg;
    private String mAlarmNotificationTitle;

    /**
     * method For getting Notification message displayed in Notification sub context
     *
     * @return :notification message(String)
     */
    public String getAlarmNotificationMsg() {
        return mAlarmNotificationMsg;
    }

    /**
     * Method for setting AlarmNotificationMessage
     *
     * @param alarmNotificationMsg : Message which has to be displayed
     */
    public void setAlarmNotificationMsg(String alarmNotificationMsg) {
        if (alarmNotificationMsg != null)
            this.mAlarmNotificationMsg = alarmNotificationMsg;
        else
            this.mAlarmNotificationMsg = " ";
    }

    /**
     * Method for getting title of task
     *
     * @return :title of task (String)
     */
    public String getAlarmNotificationTitle() {
        return mAlarmNotificationTitle;
    }

    /**
     * Method for Setting alarm Title
     *
     * @param alarmNotificationTitle : title of task
     */
    public void setAlarmNotificationTitle(String alarmNotificationTitle) {
        this.mAlarmNotificationTitle = alarmNotificationTitle;
    }

    /**
     * Method of getting of Trigger Time of Alarm
     *
     * @return :datetime in long
     */
    public long getTriggerTime() {
        return mTriggerTime;
    }

    /***
     * Method for setting trigger time of alarm
     *
     * @param triggerTime :time in milli-seconds in long data type
     */
    public void setTriggerTime(long triggerTime) {
        this.mTriggerTime = triggerTime;
    }
}