package com.example.shubham.taskh.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;

import com.example.shubham.taskh.constants.StringConstants;
import com.example.shubham.taskh.utility.Logger;
import com.example.shubham.taskh.utility.TaskHandHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
Receiver class for receiving the broadcast from different pending intent
and setting alarm and reconfiguring alarm when phone get switched off
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static Ringtone mRingtone;

    /**
     * Method for handling alarm action
     *
     * @param intent  : intent on which we have to notify the user and play alarm ringtone
     * @param context :context of app
     */
    private void handleAlarmAction(Intent intent, Context context) {
        //getting details of task for which alarm has to set
        AlarmDetails alarmInput = (AlarmDetails) intent.getSerializableExtra(StringConstants.ALARM_INPUT);
        //if ringtone is playing then stop the ringtone
        if (mRingtone != null && mRingtone.isPlaying()) {
            mRingtone.stop();
        }
        mRingtone = AlarmHelper.soundAlarm(alarmInput);
        //getting Uri
        Uri alarmUri = intent.getData();
        //Notifying User with AlarmHelper.notifyUser() passing the alarm uri for a particular
        //task and details of task
        AlarmHelper.notifyUser(alarmUri, alarmInput);
    }

    /**
     * Method for Handling notification
     *
     * @param intent  : intent from AlarmNotificationView class
     * @param context : app context
     */
    private void handleNotificationAction(Intent intent, Context context) {
        //getting details of task for which alarm has to set
        AlarmDetails alarmInput = (AlarmDetails) intent.getSerializableExtra(StringConstants.ALARM_INPUT);
        //if ringtone is playing then stop the ringtone
        if (mRingtone != null) {
            AlarmHelper.stopAlarm(alarmInput, mRingtone);
        } else {
            TaskHandHelper.toastShort("No Ringtone Playing");
        }

    }

    /**
     * Broadcast Receiver's override method for handling the Broadcast from different places of app
     *
     * @param context : context of app
     * @param intent  : intent which has to be called for that broadcast
     */

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            switch (intent.getAction()) {
                case StringConstants.ALARM_ACTION:
                    handleAlarmAction(intent, context);
                    break;
                case StringConstants.ALARM_NOTIFICATION:
                    handleNotificationAction(intent, context);
                    break;
                case Intent.ACTION_BOOT_COMPLETED:
                    reconfigureAllAlarms(context);
                    break;
            }
        }
    }

    /**
     * Method For reconfiguring or resetting alarms after reboot of phone
     *
     * @param context : context of app
     */
    private void reconfigureAllAlarms(Context context) {
        Logger.debug("ALARM", "YES ");
        //Getting all alarms from AlarmStore
        HashMap<Uri, AlarmDetails> details = AlarmStore.retrieveAllAlarms(context);
        Set<Map.Entry<Uri, AlarmDetails>> entries = details.entrySet();
        Iterator<Map.Entry<Uri, AlarmDetails>> alarms = entries.iterator();

        while (alarms.hasNext()) {
            Map.Entry<Uri, AlarmDetails> entry = alarms.next();
            AlarmDetails alarm = entry.getValue();
            Uri alarmUri = entry.getKey();
            AlarmHelper.setOneTimeExactAlarm(alarmUri, alarm);
        }
    }

}
