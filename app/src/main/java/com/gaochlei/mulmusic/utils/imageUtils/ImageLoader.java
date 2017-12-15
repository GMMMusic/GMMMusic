package com.gaochlei.mulmusic.utils.imageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.gaochlei.mulmusic.entity.Music;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by 高春雷 on 2017/1/15.
 */

public class ImageLoader {
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 线程池线程的数量
     */
    private int mThreadCount;

    /**
     * 轮询的线程
     */
    private Thread mPoolThread;
    /**
     * 轮询的线程
     */
    private Handler mPoolHandler;
    /**
     * 更新UI的线程
     */
    private Handler mUIHandler;
    /**
     * 线程队列
     */
    private LinkedList<Runnable> mTasks;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.FIFO;
    /**
     * 引入一个值为1的信号量，防止mThreadHandler未初始化完成
     */
    private volatile Semaphore mSemaphore = new Semaphore(0);
    /**
     * 引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，使LIFO效果不明显
     */
    private volatile Semaphore mPoolSemaphore;

    public enum Type {
        FIFO, LIFO
    }

    private LruCache<String, Bitmap> mLruCache;

    private static ImageLoader mInstance;

    private ImageLoader(int mThreadCount, Type type) {
        init(mThreadCount, type);
    }

    private void init(int mThreadCount, Type type) {
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                //通知轮询的线程获取任务
                mPoolHandler = new Handler() {
                    //轮询的线程要进行任务
                    @Override
                    public void handleMessage(Message msg) {
                        mThreadPool.execute(getTask());
                        try {
                            mPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                mSemaphore.release();
                Looper.loop();
            }
        };
        mPoolThread.start();
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mThreadPool = Executors.newFixedThreadPool(mThreadCount);
        mTasks = new LinkedList<Runnable>();
        mPoolSemaphore = new Semaphore(mThreadCount);
        mType = type == null ? Type.LIFO : type;
    }

    private synchronized Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTasks.removeFirst();
        } else {
            return mTasks.removeLast();
        }
    }

    public void loadImage4Id(final Context context,final ImageView imageView, final Music music) {
        imageView.setTag(music.getId());
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    ImageHolder holder = (ImageHolder)msg.obj;
                    ImageView imageView = holder.imageView;
                    Bitmap bitmap = holder.bitmap;
                    long albumId = holder.musicId;
                    if((long)imageView.getTag() == albumId){
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
        }
        Bitmap bitmap = getBitmapFromLruCache(music.getAlbumId());
        if(bitmap != null){
            ImageHolder holder = new ImageHolder();
            holder.musicId = music.getId();
            holder.bitmap = bitmap;
            holder.imageView = imageView;
            Message msg = Message.obtain();
            msg.obj = holder;
            mUIHandler.sendMessage(msg);
        }else{
            addTask(new Runnable() {
                @Override
                public void run() {
                    ImageHolder holder = new ImageHolder();
                    Bitmap bitmap = ImageUtils.getArtwork(context,music.getId(),music.getAlbumId(),true,true);
                    addBitmapToLruCache(music.getAlbumId(),bitmap);
                    holder.bitmap = getBitmapFromLruCache(music.getAlbumId());
                    holder.musicId = music.getId();
                    holder.imageView = imageView;
                    Message msg = Message.obtain();
                    msg.obj = holder;
                    mUIHandler.sendMessage(msg);
                    mPoolSemaphore.release();
                }
            });
        }
    }


    private Bitmap getBitmapFromLruCache(long albumId) {
        return mLruCache.get(albumId + "");
    }

    private void addBitmapToLruCache(long albumId, Bitmap bitmap) {
        if (getBitmapFromLruCache(albumId) == null) {
            if (bitmap != null) {
                mLruCache.put(albumId + "", bitmap);
            }
        }
    }

    private synchronized void addTask(Runnable runnable) {
        try {
            // 请求信号量，防止mThreadHander为null
            if (mPoolHandler == null)
                mSemaphore.acquire();
        } catch (InterruptedException e) {
        }
        mTasks.add(runnable);
        mPoolHandler.sendEmptyMessage(0x110);
    }

    public static ImageLoader getmInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(3, Type.FIFO);
                }
            }
        }
        return mInstance;
    }

    private static class ImageHolder{
        public long musicId;
        public ImageView imageView;
        public Bitmap bitmap;
    }
}
