package com.gaochlei.mulmusic.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.gaochlei.mulmusic.cusview.BaseLayout;
import com.gaochlei.mulmusic.entity.MediaAidlInterface;
import com.gaochlei.mulmusic.service.MusicPlayer;
import com.gaochlei.mulmusic.service.MusicService;

import java.lang.ref.WeakReference;

import static com.gaochlei.mulmusic.service.MusicPlayer.mService;

/**
 * Created by 高春雷 on 2017/1/12.
 */

public  class BaseActivity extends FragmentActivity implements View.OnClickListener,
        ServiceConnection {
    public BaseLayout baseLayout;
    private MusicPlayer.ServiceToken mToken;
    private PlaybackStatus mPlaybackStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = MusicPlayer.bindToService(this, this);
        mPlaybackStatus = new PlaybackStatus(this);
        IntentFilter f = new IntentFilter();
        f.addAction(MusicService.PROGRESS);
        registerReceiver(mPlaybackStatus, new IntentFilter(f));
    }

    public void setView(int contentViewId) {
        baseLayout = new BaseLayout(this, contentViewId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(baseLayout);

    }

    @Override
    public void onClick(View v) {
    }

    public void updateProgress() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken);
            mToken = null;
        }

        try {
            unregisterReceiver(mPlaybackStatus);
        } catch (final Throwable e) {
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = MediaAidlInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mService = null;
    }

    private final static class PlaybackStatus extends BroadcastReceiver {
        private final WeakReference<BaseActivity> mReference;

        public PlaybackStatus(final BaseActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
//            final String action = intent.getAction();
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                baseActivity.updateProgress();
            }
        }
    }
}

