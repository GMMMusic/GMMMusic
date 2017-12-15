package com.gaochlei.mulmusic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.gaochlei.mulmusic.activity.MainActivity;
import com.gaochlei.mulmusic.dbmanager.DbMusicManager;
import com.gaochlei.mulmusic.entity.LastPlayMusic;
import com.gaochlei.mulmusic.entity.MediaAidlInterface;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.entity.MusicUtils;
import com.gaochlei.mulmusic.msgline.MessageManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.gaochlei.mulmusic.dbmanager.DbMusicManager.initMusicManager;

/**
 * Created by 高春雷 on 2017/1/12.
 */

public class MusicService extends Service {
    private MediaPlayer mPlayer;
    private Music mMusic;
    private ServiceReceiver playerReceiver;
    private MediaSession mSession;
    public final static String PLAYERFILTER = "com.gaochlei.mulmusic.playerfilter";
    public final static String REPEATCOUNT = "com.gaochlei.mulmusic.repeatCountFilter";
    public final static String PROGRESS = "com.gaochlei.mulmusic.progress";
    /**
     * 当前要播放的音乐列表
     */
    private List<Music> playList;
    /**
     * 新建的播放列表
     */
    private List<Music> newList;
    private int playIndex;
    public final static String ISPLAYING_MUSIC = "isPlayMusic";
    private final IBinder mBinder = new ServiceStub(this);
    private Music mPlayingMusic;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private int playCount;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChanagerListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        newList = new ArrayList<>();
//        if (mMusic != null) {
//            mMusic = (Music) intent.getExtras().get("music");
//            loopPlay(2);
//        } else {
//        }
        Log.d("pause", "service onStartCommand ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("pause", "service onStart ");
    }

    private void initSession() {
        mSession = new MediaSession(this, "gao.mulmusic");
        mSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
            }

            @Override
            public void onPause() {
                super.onPause();
            }

            @Override
            public void onStop() {
                super.onStop();
            }

            @Override
            public void onSeekTo(long pos) {
                seek((int) pos);
            }
        });
        mSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    /**
     * 当前播放的歌曲循环播放
     *
     * @param musicIndex music index in music list
     */
    private void loopPlay(final int musicIndex) {
        if (playList == null) {
            playList = initMusicManager(getApplicationContext()).queryMusicListAll();
        }
        playIndex = musicIndex;
        mMusic = playList.get(musicIndex);
        playCount = mMusic.getPlayCount();
        if (playCount == 1) {
            loopPlayMusic(musicIndex);
        } else {
            playMusic(playIndex, false);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playCount--;
                    if (playCount < 1) {
                        playMusic(++playIndex, true);
                    } else {
                        playMusic(playIndex, false);
                    }
                }
            });
        }
    }

    /**
     * 监听音乐是否播放结束
     */
    private void loopPlayMusic(final int musicIndex) {
        playMusic(playIndex, true);
        // 监听音乐是否播放结束。
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playCount--;
                if (playCount < 1) {
                    playMusic(++playIndex, true);
                } else {
                    playMusic(playIndex, false);
                }
            }
        });
    }

    public int getAudioSessionId() {
        synchronized (this) {
            return mPlayer.getAudioSessionId();
        }
    }

    /**
     * 播放音乐并通知主界面做出界面上的调整。
     *
     * @param playIndex
     * @throws IOException
     */
    private void playMusic(int playIndex, boolean isRepeat) {
        if(requestFocus()) {
            final Intent intent1 = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
            intent1.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
            intent1.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
            sendBroadcast(intent1);
            if (playIndex < playList.size()) {
                try {
                    mPlayingMusic = playList.get(playIndex);
                    LastPlayMusic lastPlayMusic = new LastPlayMusic();
                    lastPlayMusic.setMusic(mPlayingMusic);
                    lastPlayMusic.setPosition(0);
                    lastPlayMusic.setSortId(System.currentTimeMillis());
                    initMusicManager(getApplicationContext()).addLastMusic(lastPlayMusic);
                    if (isRepeat) {
                        playCount = mPlayingMusic.getPlayCount();
                    }
                    mPlayer.reset();
                    mPlayer.setDataSource(mPlayingMusic.getUrl());
                    mPlayer.prepare();
                    mPlayer.start();
                    Intent intent = new Intent();
                    Bundle data = new Bundle();
                    data.putParcelable(ISPLAYING_MUSIC, mPlayingMusic);
                    intent.putExtras(data);
                    intent.setAction(MainActivity.PLAYING_MUSIC_FILTER);
                    sendBroadcast(intent);

                    final Intent intent2 = new Intent(MusicService.PROGRESS);
                    intent2.putExtra("position", position());
                    intent2.putExtra("position", 8l);
                    intent2.putExtra("duration", duration());
                    intent2.setAction(MusicService.PROGRESS);
                    sendBroadcast(intent2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                mPlayer.release();
            }
        }
    }

    private void notify(String what) {
        final Intent intent = new Intent(what);
        intent.setAction(what);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        mPlayingMusic = LastPlayMusic.getMusic(DbMusicManager.initMusicManager(getApplication()).queryLastMusic());
        mPlayer = new MediaPlayer();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 暂时先写成全部的音乐列表,默认的
        playList = initMusicManager(getApplicationContext()).queryMusicListAll();
        playerReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter(PLAYERFILTER);
        this.registerReceiver(playerReceiver, filter);
        initSession();
        mAudioFocusChanagerListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    try {
                        pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    try {
                        resume();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    mAudioManager.abandonAudioFocus(mAudioFocusChanagerListener);
                    try {
                        stop();
                        MessageManager.instance().notify(MainActivity.MSG_NAME, MessageManager
                                .PAUSE_FROM_SERVICE);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        super.onCreate();
    }

    private boolean requestFocus() {
        int result = mAudioManager.requestAudioFocus(mAudioFocusChanagerListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) mPlayer.release();
    }

    private void resume() throws RemoteException {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    /**
     * 播放按钮通知接收器
     */
    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean player_on_off = intent.getBooleanExtra(MainActivity.PLAYER_ON_OFF, false);
            if (player_on_off) {
                playMusic(playIndex, true);
            } else {
                mPlayer.stop();
            }
        }
    }

    private static final class ServiceStub extends MediaAidlInterface.Stub {
        private final WeakReference<MusicService> mService;

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
            try {
                super.onTransact(code, data, reply, flags);
            } catch (final RuntimeException e) {
//                L.E(D,TAG,"onTransact error");
                e.printStackTrace();
                File file = new File(mService.get().getCacheDir().getAbsolutePath() + "/err/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                try {
                    PrintWriter writer = new PrintWriter(mService.get().getCacheDir()
                            .getAbsolutePath() + "/err/" + System.currentTimeMillis() + "_aidl" +
                            ".log");
                    e.printStackTrace(writer);
                    writer.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                throw e;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return true;
        }

        private ServiceStub(final MusicService service) {
            mService = new WeakReference<MusicService>(service);
        }

        @Override
        public void play(int musicIndex) throws RemoteException {
            mService.get().play(musicIndex);
        }

        @Override
        public boolean playing() throws RemoteException {
            return mService.get().playing();
        }

        @Override
        public void seek(long position) throws RemoteException {
            mService.get().seek(position);
        }

        @Override
        public void stop() throws RemoteException {
            mService.get().stop();
        }

        @Override
        public void pause() throws RemoteException {
            mService.get().pause();
        }

        @Override
        public void start() throws RemoteException {
            mService.get().start();
        }

        @Override
        public long position() throws RemoteException {
            return mService.get().position();
        }

        @Override
        public long duration() throws RemoteException {
            return mService.get().duration();
        }

        @Override
        public int playingMusicId() throws RemoteException {
            return mService.get().plaingMusicId();
        }

        @Override
        public void setRepeatCount(int repeatCount) {
            mService.get().setRepeatCount(repeatCount);
        }

        @Override
        public void sendSong(long songId) {
            mService.get().sendSong(songId);
        }

        @Override
        public void sendMusic(Music music) {
            mService.get().sendMusic(music);
        }

        @Override
        public List<Music> loadNewPlayMusics() {
            return mService.get().loadMusicList();
        }

        @Override
        public void sendMusics(List<Music> musics) {
            mService.get().reloadCurMusicList(musics);
        }

        @Override
        public void playByMusicId(long musicId) {
            mService.get().playMusicById(musicId);
        }

        @Override
        public void resetPlayCount(long id, int index, int playCount) {
            mService.get().resetPlayCount(id, index, playCount);
        }
    }

    /**
     * 设置某个歌曲的播放次数
     *
     * @param musicId
     * @param playCount
     */
    private void resetPlayCount(long musicId, int index, int playCount) {
        if (mPlayingMusic != null && playList.get(index).getId() == mPlayingMusic.getId()) {
            this.playCount = playCount;
        }
        playList.get(index).setPlayCount(playCount);
    }

    private void reloadCurMusicList(List<Music> musics) {
        this.playList = musics;
    }

    private List<Music> loadMusicList() {
        return newList;
    }

    private void sendMusic(Music music) {
        newList.add(music);
    }

    private void setRepeatCount(int repeatCount) {
        this.playCount = repeatCount;
    }

    private void playMusicById(long musicId) {
        play(MusicUtils.getMusicIndex(playList, musicId));
    }

    /**
     * @return -1 当前还未播放过音乐，自上次退出之后。1当前有播放过音乐。
     * @throws RemoteException
     */
    private int plaingMusicId() throws RemoteException {
        if (mPlayingMusic == null) {
            return -1;
        } else {
            return 1;
        }
    }

    private void sendSong(long songId) {
        Music music = DbMusicManager.initMusicManager(getApplicationContext()).queryMusicById
                (songId);
        newList.add(music);
    }

    private boolean playing() throws RemoteException {
        return mPlayer.isPlaying();
    }

    private void start() throws RemoteException {
        if (mPlayer != null) mPlayer.start();
    }

    private void stop() throws RemoteException {
        if (mPlayer != null) mPlayer.stop();
    }

    private void pause() throws RemoteException {
        if (mPlayer != null) mPlayer.pause();
    }

    public void play(int musicIndex) {
//        playMusic(musicIndex,true);
        loopPlay(musicIndex);
        notify(MusicService.PROGRESS);
    }

    public void seek(long position) {
        mPlayer.seekTo((int) position);
    }

    public long position() {
        try {
            return mPlayer.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long duration() {
        return mPlayer.getDuration();
    }

}
