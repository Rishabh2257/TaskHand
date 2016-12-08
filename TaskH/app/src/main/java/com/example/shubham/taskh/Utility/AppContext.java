package com.example.shubham.taskh.Utility;

import android.app.Application;
import android.content.Context;

/**
 * Created by shubham on 30/11/16.
 */
public class AppContext extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        AppContext.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setmContext(this);
    }

}
