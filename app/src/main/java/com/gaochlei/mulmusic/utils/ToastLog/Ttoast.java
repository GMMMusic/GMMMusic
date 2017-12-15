package com.gaochlei.mulmusic.utils.ToastLog;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 高春雷 on 2017/8/18.
 */

public class Ttoast {
    public static boolean isTest = true;

    public static void makeText(Context context,String msg,int flag){
        if(isTest) {
            Toast.makeText(context, msg, flag).show();
        }
    }
}
