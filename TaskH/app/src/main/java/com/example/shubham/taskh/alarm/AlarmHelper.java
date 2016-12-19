package com.example.shubham.taskh.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.example.shubham.taskh.constants.IntegerConstants;
import com.example.shubham.taskh.constants.StringConstants;
import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.utility.Logger;

import java.io.File;

/*
Alarm Helper Class for helping in setting the alarm and cancelling of alarm
 */
public class AlarmHelper {

    /**
     * Method for creating a alarmType
     *
     * @return :FakeAlarmType for the matching in intent
     */
    private static String createFakeType() {
        return StringConstants.ALARM_TYPE;
    }

    /**
     * Method For setting the alarm for one time Exact and adding the alarm in AlarmStore Shared Preference
     *
     * @param alarmUri     :Uri which contains Action Uri of package of app and unique TaskId for setting alarm and notification
     * @param alarmDetails :Contain Detail regarding the Task reminder time
     * @return : Boolean value for checking that alarm is set or not
     */
    public static boolean setOneTimeExactAlarm(Uri alarmUri, AlarmDetails alarmDetails) {
        boolean outSet = setOneTimeExactAlarm(alarmUri, alarmDetails, false);
        if (outSet) {
            //Adding Alarm in AlarmStore
            outSet=AlarmStore.addTheAlarm(alarmUri, alarmDetails);
        }
        return outSet;
    }

    /**
     * Method for Getting The TaskId from Passed uri
     *
     * @param uri :Uri of AppPackage with Unique id of Task
     * @return : TaskId of task After fetching from Uri
     */
    private static int getLastSegmentNumber(Uri uri) {

        int number = IntegerConstants.ILLEGAL_ALARM_ID;
        String uriString = Uri.decode(uri.toString());
        if (uriString != null) {
            int pathSeparatorIndex = uriString.lastIndexOf(File.separator);
            if (pathSeparatorIndex != -1) {
                String numberStr = uriString.substring(pathSeparatorIndex + 1, uriString.length());
                if (TextUtils.isDigitsOnly(numberStr)) {
                    number = Integer.parseInt(numberStr);
                }
            }
        }
        return number;
    }

    /**
     * Method for Cancel a alarm
     * for a task which has been deleted or updated
     *
     * @param alarmUri :Contains Uri of Task With the TaskId
     * @return :returns boolean value for checking that alarm is cancelled or not
     */
    public static boolean cancelAlarm(Uri alarmUri) {
        //getting task Id From Uri
        int senderRequestCode = getLastSegmentNumber(alarmUri);
        boolean alarmCancelled = false;

        if (senderRequestCode != IntegerConstants.ILLEGAL_ALARM_ID) {

            Context context = AppContext.getContext();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(StringConstants.ALARM_ACTION);
            intent.addCategory(StringConstants.ALARM_CATEGORY);
            intent.setDataAndType(alarmUri, createFakeType());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, senderRequestCode,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

        /*AlarmManger.cancel(Intent) uses Intent.filterEquals to matchIntents :
        Determine if two intents are the same
        for the purposes of intent resolution (filtering).
        That is, if their action, data, type, class, and
        categories are the same. This does not
        compare any extra data included in the intents.*/
            alarmManager.cancel(alarmIntent);
            //alarmCancelled = true;
            /*remove alarm from the alarm store*/
           alarmCancelled= AlarmStore.removeAlarm(alarmUri);
            Logger.debug("alarm cancel"," "+alarmCancelled);
        }
        return alarmCancelled;
    }

    /***
     * Method For Setting ExactOneTime Alarm for the passed task
     *
     * @param alarmUri     :Uri which Contain Unique TaskID and
     * @param alarmDetails :Details of Alarm Regarding Time taskDetail,taskName,
     * @return :boolean value for checking that alarm is set or not
     */
    static boolean setOneTimeExactAlarm(Uri alarmUri, AlarmDetails alarmDetails, boolean reCreateAfterTrigger) {
        //getting the TaskId
        int senderRequestCode = getLastSegmentNumber(alarmUri);
        boolean outAlarmSet = false;

        if (senderRequestCode != IntegerConstants.ILLEGAL_ALARM_ID) {
            //getting appContext
            Context context = AppContext.getContext();
            //getting Alarm Manager
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(StringConstants.ALARM_ACTION);
            intent.addCategory(StringConstants.ALARM_CATEGORY);
            intent.setDataAndType(alarmUri, createFakeType());
            intent.putExtra(StringConstants.ALARM_INPUT, alarmDetails);

            long triggerTime = alarmDetails.getTriggerTime();

            if (triggerTime > 0 && triggerTime > System.currentTimeMillis()) {
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, senderRequestCode,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //setting exact alarm for SDK Versions greater than or equal to KitKat
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent);
                } else {
                    //setting alarm for SDK versions below KITKAT
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent);
                }
                outAlarmSet = true;
            }

        }
        return outAlarmSet;

    }

    /**
     * Method For Showing the Notifying user with Notification
     *
     * @param alarmUri   :Contains Uri of Alarm which has unique task id of task
     * @param alarmInput :Details of Alarm Like Name of task,Detail and Alarm Time
     */
    public static void notifyUser(Uri alarmUri, AlarmDetails alarmInput) {

        if (alarmInput != null) {
            String notificationTitle = alarmInput.getAlarmNotificationTitle();
            String notificationMsg = alarmInput.getAlarmNotificationMsg();
            int requestCode = getLastSegmentNumber(alarmUri);
            AlarmNotificationView.show(notificationTitle, notificationMsg, requestCode, alarmUri);
        }

    }

    /**
     * Method for playing ringtone
     *
     * @param alarmInput :contain details of task
     * @return : instance of ringtone
     */
    public static Ringtone soundAlarm(AlarmDetails alarmInput) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(AppContext.getContext(), uri);
        ringtone.play();
        return ringtone;
    }

    /**
     * Method for stopping the Alarm Ringtone
     *
     * @param alarmInput :Contain Detail of Task
     * @param ringtone   : instance of ringtone for stoping the ringtone
     */
    public static void stopAlarm(AlarmDetails alarmInput, Ringtone ringtone) {
        ringtone.stop();
    }
}