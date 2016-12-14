package com.example.shubham.taskh.alarm_new;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.shubham.taskh.utility.AppContext;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AlarmStore {

    public static boolean addTheAlarm(Uri uri, AlarmDetails details){

        boolean added = false;
        SharedPreferences sharedPreferences = AppContext.getContext().getSharedPreferences("AlarmStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(uri.toString(), new Gson().toJson(details));
            added = editor.commit();
        }catch (NullPointerException|JsonIOException exception){
            exception.printStackTrace();
        }
        return added;

    }

    public static boolean removeAlarm(Uri uri){

        boolean removed = false;
        SharedPreferences sharedPreferences = AppContext.getContext().getSharedPreferences("AlarmStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.remove(uri.toString());
            removed = editor.commit();
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }
        return removed;

    }


    public static AlarmDetails retrieveTheAlarm(Uri uri){

        AlarmDetails alarmDetails = null;
        SharedPreferences sharedPreferences = AppContext.getContext().getSharedPreferences("AlarmStore", Context.MODE_PRIVATE);
        String detailsStr = sharedPreferences.getString(uri.toString(), "");

        try{
            alarmDetails = new Gson().fromJson(detailsStr, AlarmDetails.class);
        }catch (JsonIOException exception){
            exception.printStackTrace();
        }
        return alarmDetails;

    }

    public static HashMap<Uri,AlarmDetails> retrieveAllAlarms(Context context){

        AlarmDetails alarmDetails = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("AlarmStore", Context.MODE_PRIVATE);
        Map<String, ?> alarms = sharedPreferences.getAll();

        HashMap<Uri,AlarmDetails> storedAlarms = new HashMap<>();

        Set<String>keySet = alarms.keySet();

        for (String keyUri: keySet) {
            try{
                Uri uri = Uri.parse(keyUri);

                String details = (String)alarms.get(keyUri);

                Log.i("ALARM","ALARM: "+uri+","+details);

                alarmDetails = new Gson().fromJson(details, AlarmDetails.class);
                storedAlarms.put(uri,alarmDetails);
            }catch (ClassCastException|NullPointerException|JsonIOException exception){
                exception.printStackTrace();
                continue;
            }
        }

        return storedAlarms;

    }
}
