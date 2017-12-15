package com.gaochlei.mulmusic.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.gaochlei.mulmusic.entity.MediaAidlInterface;
import com.gaochlei.mulmusic.entity.Music;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by 高春雷 on 2017/8/8.
 * MusicService 的代理类
 */

public class MusicPlayer {
    public static MediaAidlInterface mService = null;
    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap;
//    private static ContentValues[] mContentValuesCache = null;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
    }

    public static final class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }

    public static final ServiceToken bindToService(final Context context,
                                                   final ServiceConnection callback) {

        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MusicService.class));
        final ServiceBinder binder = new ServiceBinder(callback,
                contextWrapper.getApplicationContext());
        if (contextWrapper.bindService(
                new Intent().setClass(contextWrapper, MusicService.class), binder, 0)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }

    public static void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            mService = null;
        }
    }


    public static final class ServiceBinder implements ServiceConnection{

        private final ServiceConnection mCallback;
        private final Context mContext;


        public ServiceBinder(final ServiceConnection callback, final Context context) {
            mCallback = callback;
            mContext = context;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = MediaAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public static void reloadMusicList(List<Music> musics){
        try{
            mService.sendMusics(musics);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public static void playMusicById(long musicId){
        try {
            mService.playByMusicId(musicId);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }


    public static List<Music> loadNewPlayMusics(){
        if(mService == null){
            return null;
        }
        try {
            return mService.loadNewPlayMusics();
        }catch (RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void sendMusic(long songId){
        try{
            mService.sendSong(songId);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public static void resetPlayCount(long musicId,int index,int playCount){
        try {
            mService.resetPlayCount(musicId,index,playCount);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }


    public static void setRepeatCount(int repeatCount){
        try {
            mService.setRepeatCount(repeatCount);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public static void seek(long position){
        try{
            mService.seek(position);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public static void sendMusic(Music music){
        try{
            mService.sendMusic(music);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    public static void play(int musicIndex){
        try {
            mService.play(musicIndex);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public static int playingMusicId(){
        try {
            return mService.playingMusicId();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean playing() {
        try{
            return mService.playing();
        }catch(RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void stop(){
        try {
            mService.stop();
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public static void pause(){
        try{
            mService.pause();
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public static void start(){
        try{
            mService.start();
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public static long position(){
        long position = 0L;
        try {
            position = mService.position();
        }catch (RemoteException e){
            e.printStackTrace();
        }finally {
            return position;
        }
    }

    public static long duration(){
        long duration = 0L;
        try {
            duration = mService.duration();
        }catch (RemoteException e){
            e.printStackTrace();
        }finally {
            return duration;
        }
    }
}
