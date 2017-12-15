package com.gaochlei.mulmusic.service;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by 高春雷 on 2017/11/25.
 */

public class AudioPlayManager {

    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mChangeListener;

    public int setAudioPlayChangerListener(Context context,final AudioListener changerListener){
        if(Build.VERSION.SDK_INT < 8){
            return 0;
        }
        if(mAudioManager == null){
            mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        }
        if(mChangeListener == null){
            mChangeListener = new AudioManager.OnAudioFocusChangeListener(){

                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                            //播放操作
                            changerListener.startListener();
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            //暂停操作
                            changerListener.pauseListener();
                            Log.d("pause","AudioManagerPause");
                            break;
                        default:
                            break;
                    }
                }
            };
        }
        int requestFocusResult = mAudioManager.requestAudioFocus(mChangeListener,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        return requestFocusResult;
    }

    public interface AudioListener{
        void startListener();
        void pauseListener();
    }
}
