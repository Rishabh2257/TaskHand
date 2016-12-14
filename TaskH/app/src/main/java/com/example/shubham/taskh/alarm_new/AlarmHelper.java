package com.example.shubham.taskh.alarm_new;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.view.TaskHandleMain;

import java.io.File;
import java.util.Calendar;


public class AlarmHelper {

    static final String RECREATE_ONETIME_EXACT = "com.example.shubham.taskh.CreateOneTimeExact";
    static final String ALARM_INPUT = "com.example.shubham.taskh.OneTimeAlarmInput";
    static final String ALARM_ACTION = "com.example.shubham.taskh.alarm_new.action";
    static final String ALARM_CATEGORY = "com.example.shubham.taskh.alarm_new.category";
    static final int ILLEGAL_ALARM_ID = -1;

    private static Uri createFakeUri(int uniqueIdentifier) {
        return Uri.parse("content://com.example.shubham.taskh/Alarms/" + uniqueIdentifier);
    }

    private static String createFakeType() {
        return "com.example.shubham.taskh.dataType";
    }


    public static boolean setAnnualAlarm(Uri alarmUri, AlarmDetails alarmDetails) {
        boolean set = setOneTimeExactAlarm(alarmUri, alarmDetails, true);
        if(set){
            AlarmStore.addTheAlarm(alarmUri,alarmDetails);
        }
        return set;
    }

    public static boolean setMonthlyAlarm(Uri alarmUri, AlarmDetails alarmDetails) {
        boolean set = setOneTimeExactAlarm(alarmUri, alarmDetails, true);
        if (set) {
            AlarmStore.addTheAlarm(alarmUri, alarmDetails);
        }
        return set;
    }

    public static boolean setOneTimeExactAlarm(Uri alarmUri, AlarmDetails alarmDetails) {
        boolean set = setOneTimeExactAlarm(alarmUri, alarmDetails, false);
        if (set) {
            AlarmStore.addTheAlarm(alarmUri, alarmDetails);
        }
        return set;
    }

    private static int getLastSegmentNumber(Uri uri) {

        int number = ILLEGAL_ALARM_ID;
        String uriString = Uri.decode(uri.toString());
        if (uriString!=null) {
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


    public static boolean cancelAlarm(Uri alarmUri) {

        int senderRequestCode = getLastSegmentNumber(alarmUri);
        boolean alarmCancelled = false;

        if (senderRequestCode != ILLEGAL_ALARM_ID) {

            Context context = AppContext.getContext();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(ALARM_ACTION);
            intent.addCategory(ALARM_CATEGORY);
            intent.setDataAndType(alarmUri, createFakeType());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, senderRequestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        /*Uses Intent.filterEquals to matchIntents : Determine if two intents are the same for the purposes of intent resolution (filtering).
        That is, if their action, data, type, class, and categories are the same. This does not
        compare any extra data included in the intents.*/
            alarmManager.cancel(alarmIntent);
            alarmCancelled = true;
            /*remove alarm from the alarm store*/
            AlarmStore.removeAlarm(alarmUri);
        }
        return alarmCancelled;
    }


    public static boolean setExactRepeatingAlarm(Uri alarmUri, AlarmDetails alarmDetails) {

        int senderRequestCode = getLastSegmentNumber(alarmUri);
        boolean alarmSet = false;

        if (senderRequestCode != ILLEGAL_ALARM_ID) {
            Context context = AppContext.getContext();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(ALARM_ACTION);
            intent.addCategory(ALARM_CATEGORY);
            intent.setDataAndType(alarmUri, createFakeType());

            PendingIntent alarmIntent = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                intent.putExtra(ALARM_INPUT, alarmDetails);
                intent.putExtra(RECREATE_ONETIME_EXACT, true);
                alarmIntent = PendingIntent.getBroadcast(context, senderRequestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmDetails.getTriggerTime(), alarmIntent);
                alarmSet = true;

            } else {
                alarmIntent = PendingIntent.getBroadcast(context, senderRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (alarmDetails.getIntervalKind() == AlarmDetails.INTERVAL_KIND_MILLISECONDS) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmDetails.getTriggerTime(), alarmDetails.getInterval(), alarmIntent);
                    alarmSet = true;
                }
            }
        }
        return alarmSet;
    }


    static boolean setOneTimeExactAlarm(Uri alarmUri, AlarmDetails alarmDetails, boolean reCreateAfterTrigger) {

        int senderRequestCode = getLastSegmentNumber(alarmUri);
        boolean alarmSet = false;

        if (senderRequestCode != ILLEGAL_ALARM_ID) {

            Context context = AppContext.getContext();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(ALARM_ACTION);
            intent.addCategory(ALARM_CATEGORY);
            intent.setDataAndType(alarmUri, createFakeType());
            intent.putExtra(ALARM_INPUT, alarmDetails);

            long triggerTime = alarmDetails.getTriggerTime();

            if (reCreateAfterTrigger) {
                if (triggerTime < System.currentTimeMillis()) {
                    triggerTime = calculateNextTrigger(alarmDetails.getIntervalKind(), alarmDetails.getInterval(), triggerTime);
                    alarmDetails.setTriggerTime(triggerTime);
                    Log.i("TRigger","Value: "+triggerTime);
                }
                intent.putExtra(RECREATE_ONETIME_EXACT, true);
            }

            Intent intent1 = new Intent(context, AlarmReceiver.class);
            intent1.setAction(ALARM_ACTION);
            intent1.addCategory(ALARM_CATEGORY);
            intent1.setDataAndType(alarmUri, createFakeType());


            if (triggerTime > 0 && triggerTime > System.currentTimeMillis()) {
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, senderRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent);
                }
                alarmSet = true;
            }

        }
        return alarmSet;

    }

    private static long calculateNextTrigger(int intervalKind, long interval, long lastTrigger) {

        switch (intervalKind) {
            case AlarmDetails.INTERVAL_KIND_MONTHLY:
                return calculateNextMonthlyTrigger(lastTrigger, (int) interval);
            case AlarmDetails.INTERVAL_KIND_ANNUAL:
                return calculateNextAnnualTrigger(lastTrigger, (int) interval);
            case AlarmDetails.INTERVAL_KIND_MILLISECONDS:
                return (lastTrigger + interval);
            default:
                return 0;
        }
    }


    private static long calculateNextMonthlyTrigger(long lastTrigger, int monthInterval) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastTrigger);
        calendar.roll(Calendar.MONTH, monthInterval);
        return calendar.getTimeInMillis();

    }

    private static long calculateNextAnnualTrigger(long lastTrigger, int annualInterval) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastTrigger);
        calendar.roll(Calendar.YEAR, annualInterval);
        return calendar.getTimeInMillis();

    }


    public static void notifyUser(Uri alarmUri, AlarmDetails alarmInput) {

        if (alarmInput != null) {
            String notifTitle = alarmInput.getAlarmNotificationTitle();
            String notifContent = alarmInput.getAlarmNotificationMsg();
            int requestCode = getLastSegmentNumber(alarmUri);
            AlarmNotificationView.show(notifTitle, notifContent, requestCode,alarmUri, TaskHandleMain.class);
        }

    }

    public static Ringtone soundAlarm(AlarmDetails alarmInput) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(AppContext.getContext(), uri);
        ringtone.play();
        return ringtone;
    }

    public static void stopAlarm(AlarmDetails alarmInput, Ringtone ringtone) {
        ringtone.stop();
    }


}
