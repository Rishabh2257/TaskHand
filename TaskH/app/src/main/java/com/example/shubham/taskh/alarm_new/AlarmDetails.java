package com.example.shubham.taskh.alarm_new;

import java.io.Serializable;


public class AlarmDetails implements Serializable{

    static final int INTERVAL_KIND_MONTHLY = 1;
    static final int INTERVAL_KIND_ANNUAL = 2;
    static final int INTERVAL_KIND_MILLISECONDS = 3;
    static final int INTERVAL_KIND_NONE = 0;


    private long mTriggerTime;
    private long mInterval;
    private boolean mIsPeriodic;
    private String mAlarmNotificationMsg;
    private String mAlarmNotificationTitle;
    private int mIntervalKind = INTERVAL_KIND_NONE;

    public String getAlarmNotificationMsg() {
        return mAlarmNotificationMsg;
    }

    public String getAlarmNotificationTitle() {
        return mAlarmNotificationTitle;
    }

    public boolean isPeriodic(){
        return mIsPeriodic;
    }

    public void setPeriodic(boolean periodic){
        mIsPeriodic = periodic;
    }



    public void setAlarmNotificationMsg(String alarmNotificationMsg) {
        this.mAlarmNotificationMsg = alarmNotificationMsg;
    }

    public void setAlarmNotificationTitle(String alarmNotificationTitle) {
        this.mAlarmNotificationTitle = alarmNotificationTitle;
    }

    public long getTriggerTime() {
        return mTriggerTime;
    }

    public void setTriggerTime(long triggerTime) {
        this.mTriggerTime = triggerTime;
    }

    public long getInterval() {
        return mInterval;
    }

    public int getIntervalKind(){
        return mIntervalKind;
    }

    public void setIntervalInMonths(long interval) {
        mIntervalKind = INTERVAL_KIND_MONTHLY;
        this.mInterval = interval;
    }

    public void setIntervalInYears(long interval) {
        mIntervalKind = INTERVAL_KIND_ANNUAL;
        this.mInterval = interval;
    }

    public void setIntervalInMilliseconds(long interval) {
        mIntervalKind = INTERVAL_KIND_MILLISECONDS;
        this.mInterval = interval;
    }


}
