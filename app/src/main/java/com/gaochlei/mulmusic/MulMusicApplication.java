package com.gaochlei.mulmusic;

import android.app.Application;

import com.gaochlei.mulmusic.utils.CrashHunter;

/**
 * Created by 高春雷 on 2017/1/8.
 */

public class MulMusicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHunter catchHandler = CrashHunter.getInstance();
		catchHandler.init(getApplicationContext());
    }
}
