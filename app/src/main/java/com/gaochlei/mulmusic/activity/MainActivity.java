package com.gaochlei.mulmusic.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gaochlei.mulmusic.R;
import com.gaochlei.mulmusic.dbmanager.DbMusicManager;
import com.gaochlei.mulmusic.entity.LastPlayMusic;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.fragment.BaseFragment;
import com.gaochlei.mulmusic.fragment.LikeFragment;
import com.gaochlei.mulmusic.fragment.MediaListFragment;
import com.gaochlei.mulmusic.fragment.NewPlayListFragment;
import com.gaochlei.mulmusic.fragment.RecentPlayListFragment;
import com.gaochlei.mulmusic.fragment.SearchFragment;
import com.gaochlei.mulmusic.msgline.AbsMessage;
import com.gaochlei.mulmusic.msgline.MessageManager;
import com.gaochlei.mulmusic.service.MusicPlayer;
import com.gaochlei.mulmusic.service.MusicService;
import com.gaochlei.mulmusic.utils.imageUtils.ImageLoader;
import com.gaochlei.mulmusic.utils.imageUtils.ImageUtils;
import com.gaochlei.mulmusic.utils.musicUtils.MusicLoader;
import com.gaochlei.mulmusic.widget.PlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity implements AbsMessage {
    public SeekBar mProgress;
    private ScanMusicHandler scanMusicHandler;
    private List<Music> musicList;
    private ImageLoader mImageLoader;
    private int lastPlayingSongId;
    private static final File MUSIC_PATH = Environment.getExternalStorageDirectory();
    private static final int MY_PREMISSIONS_REQUEST_READ = 1;
    private static final int MY_PREMISSIONS_REQUEST_WRITE = 2;
    /**
     * service 向 Activity传递数据的过滤器
     */
    public static final String PLAYING_MUSIC_FILTER = "com.gaochlei.mulmusic.playingmusic";
    public final static String PLAYER_ON_OFF = "player_on_off";
    public final static String REPEAT_COUNT = "repeat_count";
    public final static String MSG_NAME = "MSG_MAINACTIVITY";
    private MyReceiver receiver;
    private PlayerView playerView;
    private boolean isPlaying;
    private LastPlayMusic lastMusic;
    private long time;

    private Fragment[] mFragments;
    private BaseFragment mMediaListFragment;
    private BaseFragment mLikeFragment;
    private BaseFragment mNewPlayListFragment;
    private BaseFragment mRecentFragment;
    private BaseFragment mSearchFragment;
    private RelativeLayout rlFragment;
    private TextView scanTv;
    private LinearLayout ll_content;
    private TextView tv_musics;
    private TextView tv_hestory;
    private TextView tv_like;
    private TextView tv_recent;
    private TextView tv_search;
    private TextView tv_more;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        mImageLoader = ImageLoader.getmInstance();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(MainActivity.PLAYING_MUSIC_FILTER);
        this.registerReceiver(receiver, filter);
        setView(R.layout.activity_main);
        findView();
        scanTv.setOnClickListener(this);
        requestPermissions();

        initView();
        seekBarSetChangeListener();
        MessageManager.instance().registerObserver(MSG_NAME,this);
        mLikeFragment = new LikeFragment();
        mMediaListFragment = new MediaListFragment();
        mNewPlayListFragment = new NewPlayListFragment();
        mRecentFragment = new RecentPlayListFragment();
        mSearchFragment = new SearchFragment();
        mFragments = new Fragment[]{mSearchFragment,mLikeFragment, mMediaListFragment, mNewPlayListFragment, mRecentFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content,
                mMediaListFragment)
                .add(R.id.fragment_content, mNewPlayListFragment)
                .add(R.id.fragment_content, mLikeFragment)
                .add(R.id.fragment_content, mRecentFragment)
                .add(R.id.fragment_content,mSearchFragment)
                .hide(mNewPlayListFragment).hide(mLikeFragment)
                .hide(mRecentFragment)
                .hide(mSearchFragment)
                .show(mMediaListFragment).commit();
        musicList = new LinkedList<>(DbMusicManager.initMusicManager(this).queryMusicListAll());
        if (musicList != null && musicList.size() > 0) {
            ll_content.setVisibility(View.VISIBLE);
            scanTv.setVisibility(View.GONE);
            mMediaListFragment.setData(musicList);
        }

    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PREMISSIONS_REQUEST_READ);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PREMISSIONS_REQUEST_READ);
        }
    }

    private void findView() {
        tv_hestory = (TextView)this.findViewById(R.id.id_menu_hestory);
        tv_musics = (TextView)this.findViewById(R.id.id_base_tv_all_music);
        tv_like = (TextView)this.findViewById(R.id.id_base_tv_like);
        tv_recent = (TextView)this.findViewById(R.id.id_base_tv_recent);
        tv_search = (TextView)this.findViewById(R.id.id_base_tv_search);
        rlFragment = (RelativeLayout) this.findViewById(R.id.fragment_content);
        scanTv = (TextView) this.findViewById(R.id.tv_scan_music);
        mProgress = (SeekBar) this.findViewById(R.id.id_progressbar);
        mProgress.setIndeterminate(false);
        mProgress.setProgress(1);
        mProgress.setMax(1000);
    }

    private void initView() {
        playerView = (PlayerView) baseLayout.tv_bottom_player;
        ll_content = (LinearLayout)this.findViewById(R.id.id_main_ll);
        initBottomView();

        playerView.setOnClickListener(this);
        tv_hestory.setOnClickListener(this);
        tv_recent.setOnClickListener(this);
        tv_like.setOnClickListener(this);
        tv_musics.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    private void initBottomView() {
        lastMusic = DbMusicManager.initMusicManager(getApplicationContext()).queryLastMusic();
        Music music = new Music();
        if (lastMusic == null) {
            if (musicList == null || musicList.size() == 0) {
                return;
            }
            music = musicList.get(0);
            baseLayout.tv_bottom_title.setText(music.getTitle());
        } else {
            baseLayout.tv_bottom_title.setText(lastMusic.getTitle());
            music.setId(lastMusic.getId());
            music.setAlbumId(lastMusic.getAlbumId());
        }
        mImageLoader.loadImage4Id(this, baseLayout.iv_bottom_album, music);
    }

    private void clickChangeTextColor(int id){
        tv_like.setTextColor(Color.BLACK);
        tv_recent.setTextColor(Color.BLACK);
        tv_hestory.setTextColor(Color.BLACK);
        tv_musics.setTextColor(Color.BLACK);
        tv_search.setTextColor(Color.BLACK);
        if(id == R.id.id_menu_hestory){
            tv_hestory.setTextColor(Color.parseColor("#ffb701"));
        }else if(id == R.id.id_base_tv_recent){
            tv_recent.setTextColor(Color.parseColor("#ffb701"));
        }else if(id == R.id.id_base_tv_all_music){
            tv_musics.setTextColor(Color.parseColor("#ffb701"));
        }else if(id == R.id.id_base_tv_like){
            tv_like.setTextColor(Color.parseColor("#ffb701"));
        }else if(id == R.id.id_base_tv_search){
            tv_search.setTextColor(Color.parseColor("#ffb701"));
        }
    }

    public void updateProgress() {
        mProgress.postDelayed(mUpdateProgress, 200);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_scan_music) {
            if (scanMusicHandler == null) {
                Toast.makeText(getApplication(), "开始音乐扫描", Toast.LENGTH_LONG).show();
                scanMusicHandler = new ScanMusicHandler();
                Message msg = Message.obtain();
                List<Music> musics = MusicLoader.instance(getApplication().getContentResolver()).getMusicList();
                msg.obj = musics;
                scanMusicHandler.sendMessage(msg);
            }
        } else if (v.getId() == R.id.id_pv_player) {
            isPlaying = MusicPlayer.playing();
            if (isPlaying) {
                playerView.pause();
                MusicPlayer.pause();
            } else {
                playerView.start();
                MusicPlayer.start();
                if (MusicPlayer.playingMusicId() < 0) {
                    MusicPlayer.play(getMusicListIndex(lastMusic.getId()));
                }
            }
        } else if (v.getId() == R.id.id_menu_hestory) {
            mNewPlayListFragment.refresh();
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            fragTransaction.hide(mMediaListFragment)
                    .hide(mLikeFragment)
                    .hide(mRecentFragment)
                    .hide(mSearchFragment);
            fragTransaction.show(mNewPlayListFragment).commit();
            clickChangeTextColor(R.id.id_menu_hestory);
        }else if(v.getId() == R.id.id_base_tv_all_music){
            getSupportFragmentManager().beginTransaction()
                    .hide(mNewPlayListFragment)
                    .hide(mSearchFragment)
                    .hide(mLikeFragment)
                    .hide(mRecentFragment)
                    .show(mMediaListFragment)
                    .commit();
            clickChangeTextColor(R.id.id_base_tv_all_music);
        }else if(v.getId() == R.id.id_base_tv_search){
            getSupportFragmentManager().beginTransaction()
                    .hide(mNewPlayListFragment)
                    .hide(mMediaListFragment)
                    .hide(mRecentFragment)
                    .hide(mLikeFragment)
                    .show(mSearchFragment)
                    .commit();
            clickChangeTextColor(R.id.id_base_tv_search);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("songId", MusicPlayer.playingMusicId());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastPlayingSongId = savedInstanceState.getInt("songId");
    }


    public int getMusicListIndex(long musicId) {
        int index = 0;
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getId() == musicId) {
                index = i;
            } else {
                continue;
            }
        }
        return index;
    }

    private void seekBarSetChangeListener() {
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayer.seek(progress * MusicPlayer.duration() / 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override

    public Object AcceptMessage(Object[] data) {
        Log.d("pause","activity_pause");
        if(data.length < 0){
            return null;
        }else {
            if (((String) data[0]).equals(MessageManager.PAUSE_FROM_SERVICE)){
                playerView.pause();
            }else {
                playerView.start();
            }
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


     class ScanMusicHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
                LinkedList<Music> musicList = null;
                if (msg.obj != null) {
                    Toast.makeText(getApplication(), "扫描音乐成功", Toast.LENGTH_LONG).show();
                    musicList = new LinkedList<>((ArrayList<Music>) msg.obj);
                    DbMusicManager.initMusicManager(MainActivity.this).insertMusicList(musicList);
                    rlFragment.setVisibility(View.VISIBLE);
                    scanTv.setVisibility(View.GONE);
                    mMediaListFragment.setData(musicList);
                    mMediaListFragment.refresh();
                }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("pause","activity onresume");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
        mProgress.removeCallbacks(mUpdateProgress);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Music music = (Music) bundle.getParcelable(MusicService.ISPLAYING_MUSIC);
            baseLayout.iv_bottom_album.setImageBitmap(ImageUtils.getArtwork(MainActivity.this, music.getId(), music.getAlbumId(), true, true));
            baseLayout.tv_bottom_title.setText(music.getTitle());
        }
    }

    private Runnable mUpdateProgress = new Runnable() {
        @Override
        public void run() {
            if (mProgress != null) {
                long position = MusicPlayer.position();
                long duration = MusicPlayer.duration();
                if (duration > 0 && duration < 627080716) {
                    mProgress.setProgress((int) (1000 * position / duration));
                }
            }
            mProgress.postDelayed(mUpdateProgress, 200);
        }
    };
}
