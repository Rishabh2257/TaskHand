package com.example.shubham.taskh.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.shubham.taskh.constants.StringConstants;
import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.utility.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AlarmStore class for storing the alarm in shared preference
 * for the setting the alarms when phone gets rebooted
 */
public class AlarmStore {
    /**
     * Method for adding alarm in shared preference
     * @param uri : Uri of task which is unique for a task
     * @param details:Details of task for which alarm has been set
     * @return :boolean value for checking that alarm is stored or not
     */
    public static boolean addTheAlarm(Uri uri, AlarmDetails details){

        boolean added = false;
        SharedPreferences sharedPreferences = AppContext.getContext().
                getSharedPreferences(StringConstants.ALARM_STORE_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            // in key value pair we store task in shared preference
            editor.putString(uri.toString(), new Gson().toJson(details));
            //saving data in preference by commit
            added = editor.commit();
        }catch (NullPointerException|JsonIOException exception){
            exception.printStackTrace();
        }
        return added;
    }

    /**
     * Method For removing alarm and task from the shared preference
     * @param uri :uri which contain taskId of the removing task
     * @return : boolean value for checking that alarm is removed from shared reference or not
     */
    public static boolean removeAlarm(Uri uri){

        boolean removed = false;
        SharedPreferences sharedPreferences = AppContext.getContext().
                getSharedPreferences(StringConstants.ALARM_STORE_FILE_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.remove(uri.toString());
            removed = editor.commit();
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }
        return removed;

    }

    /**
     * Method for retrieving all tasks and alarms which are stored in Shared preferences
     * @param context :app context
     * @return :HashMap of all Alarms which has key :Uri of task and value: Detail of task
     */

    public static HashMap<Uri,AlarmDetails> retrieveAllAlarms(Context context){

        AlarmDetails alarmDetails = null;
        //get shared preference file name ALARM_STORE_FILE_NAME
        // using the file name which should be unique
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(StringConstants.ALARM_STORE_FILE_NAME,
                        Context.MODE_PRIVATE);

        //Map for creating a set of values from shared preferences file
        Map<String, ?> alarms = sharedPreferences.getAll();
        //creating a HashMap of Uri and AlarmDetail for storing the set
        // which is created from the set by the shared preference file

        HashMap<Uri,AlarmDetails> outStoredAlarms = new HashMap<>();

        Set<String>keySet = alarms.keySet();

        for (String keyUri: keySet) {
            try{
                //parsing uri from Uri for Key generation
                Uri uri = Uri.parse(keyUri);
                //details in string for value
                String details = (String)alarms.get(keyUri);

                Logger.debug("ALARM","ALARM: "+uri+","+details);

                alarmDetails = new Gson().fromJson(details, AlarmDetails.class);
                outStoredAlarms.put(uri,alarmDetails);
            }catch (ClassCastException|NullPointerException|JsonIOException exception){
                exception.printStackTrace();
                continue;
            }
        }
        return outStoredAlarms;

    }
}