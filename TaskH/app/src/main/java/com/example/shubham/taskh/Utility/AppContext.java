package com.example.shubham.taskh.Utility;

import android.app.Application;
import android.content.Context;

/**
 * Created by shubham on 30/11/16.
 */
public class AppContext extends Application {
    private static Context mContext;

    public void onCreate(){
        super.onCreate();
        mContext = mContext.getApplicationContext();
    }

    /***
     * method for returning the app context which can be accessed directly
     * @return app ontext
     */
    public static Context getAppContext(){
        return mContext;
    }
}
