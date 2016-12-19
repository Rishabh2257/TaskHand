package com.example.shubham.taskh.utility;

import android.text.TextUtils;
import android.util.Log;

import com.example.shubham.taskh.BuildConfig;

/**
 * Logger Class for logging important information according to need
 * <p/>
 * Created by shubham on 16/12/16.
 */
public final class Logger {

    /**
     * Logger Class for showing logs when mode is DEBug
     *
     * @param tag     TAG under which INFO message will be shown.
     * @param message Message to be shown under INFO tag.
     */
    public static void debug(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(tag)) tag = AppContext.class.getSimpleName();
            if (message == null) message = "";
            Log.d(tag, message);
        }

    }

    /**
     * Logger Class for showing logs when mode is DEBug
     *
     * @param tag       TAG under which INFO message will be shown.
     * @param message   Message to be shown under INFO tag.
     * @param throwable Error which is thrown
     */
    public static void error(String tag, String message, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(tag)) tag = AppContext.class.getSimpleName();
            if (message == null) message = "";
            Log.e(tag, message, throwable);
        }

    }

}