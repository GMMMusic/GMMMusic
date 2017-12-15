package com.gaochlei.mulmusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaochlei.mulmusic.R;
import com.gaochlei.mulmusic.activity.MainActivity;
import com.gaochlei.mulmusic.activity.SyllabusActivity;
import com.gaochlei.mulmusic.adapter.BaseMusicAdapter;
import com.gaochlei.mulmusic.adapter.NewMusicAdapter;
import com.gaochlei.mulmusic.adapter.helper.RecyItemTouchCallback;
import com.gaochlei.mulmusic.dbmanager.DbMusicManager;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.entity.MusicHistory;
import com.gaochlei.mulmusic.entity.MusicUtils;
import com.gaochlei.mulmusic.msgline.MessageManager;
import com.gaochlei.mulmusic.service.CurrentPlayList;
import com.gaochlei.mulmusic.service.MusicPlayer;
import com.gaochlei.mulmusic.utils.ToastLog.Tlog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 高春雷 on 2017/8/30.
 */

public class NewPlayListFragment extends BaseFragment {

    public final static String MSG_NAME = "MSG_NEWPLAYLISTFRAGMENT";
    private ItemTouchHelper itemTouchHelper;
    private RelativeLayout rl_calendar;
    private TextView tv_date;
    private TextView tv_songCount;
    private int songCount;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MessageManager.instance().registerObserver(NewPlayListFragment.MSG_NAME,this);
        initAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper.Callback callback = new RecyItemTouchCallback(adapter,false,true);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tv_date.setText(MusicUtils.getTodayDate());
        if(songCount == 0){
            tv_songCount.setText("无歌曲");
        }else {
            tv_songCount.setText(songCount + "首歌曲");
        }
    }

    public void findView() {
        View view = getView();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyc_new);
        rl_calendar = (RelativeLayout)view.findViewById(R.id.id_new_list_rl);
        tv_date = (TextView)view.findViewById(R.id.id_new_list_date);
        tv_songCount = (TextView)view.findViewById(R.id.id_new_list_songcount);
        rl_calendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.id_new_list_rl){
            Intent intent = new Intent(getActivity(), SyllabusActivity.class);
            startActivity(intent);
        }
    }

    public void setData(LinkedList<Music> musics) {
        this.musics = musics;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void refresh() {
        adapter.setData(getDataFDb());
        adapter.notifyDataSetChanged();
    }

    public List<Music> getDataFDb(){
        String key = MusicUtils.getTodayDate();
        List<Music> musics = new ArrayList<>();
        List<MusicHistory> mhList = DbMusicManager.initMusicManager(getActivity()).selectMusicHistory(key);
        if(mhList != null && mhList.size() > 0){
            for(MusicHistory mh : mhList){
                musics.add(mh.getMusic());
            }
        }
        songCount = musics.size();
        return musics;
    }

    public void initAdapter() {
        musics = getDataFDb();
        adapter = new NewMusicAdapter(musics);
        adapter.setContext(getContext());
        adapter.setItemOnClickListener(new BaseMusicAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClickListener(long musicIndex) {
                MessageManager.instance().notify(MainActivity.MSG_NAME, "改变底部播放状态");
                CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.newList,musics);
                MusicPlayer.playMusicById(musicIndex);
            }
        });
        adapter.setRepeatCountListener(new BaseMusicAdapter.RepeatCountListener() {
            @Override
            public void repeatCountListener(int index,int repeatCount) {
                CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.newList,musics);
                MusicPlayer.resetPlayCount(musics.get(index).getId(),index,repeatCount);
            }
        });
    }

    public Object AcceptMessage(Object[] data) {
        if (data[0] instanceof Music) {
            if (musics == null) {
                musics = new LinkedList<>();
            }
            musics.add((Music) data[0]);
            Tlog.i("accept","accepted");
            insertHistoryMusic((Music)data[0]);
        }
        return null;
    }
    private void insertHistoryMusic(Music music){
        MusicHistory mh = new MusicHistory();
        mh.setId(System.currentTimeMillis()+"");
        mh.setDate(MusicUtils.getTodayDate());
        mh.setPlayCount(music.getPlayCount());
        mh.setMusicId(music.getId());
        mh.setMusic(music);
        DbMusicManager.initMusicManager(getActivity()).insertMusicHistory(mh);
    }
}
