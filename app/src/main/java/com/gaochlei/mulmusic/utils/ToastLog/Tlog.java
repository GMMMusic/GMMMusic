package com.gaochlei.mulmusic.utils.ToastLog;

import android.util.Log;

/**
 * Created by 高春雷 on 2017/8/18.
 */

public class Tlog {
    public static boolean isTest = true;

    public static void e(String tag,String msg){
        if(isTest){
            Log.e(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(isTest){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if(isTest){
            Log.i(tag,msg);
        }
    }
}
