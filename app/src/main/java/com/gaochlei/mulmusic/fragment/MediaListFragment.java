package com.gaochlei.mulmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaochlei.mulmusic.R;
import com.gaochlei.mulmusic.activity.MainActivity;
import com.gaochlei.mulmusic.adapter.BaseMusicAdapter;
import com.gaochlei.mulmusic.adapter.MusicListAdapter;
import com.gaochlei.mulmusic.adapter.helper.RecyItemTouchCallback;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.msgline.MessageManager;
import com.gaochlei.mulmusic.service.CurrentPlayList;
import com.gaochlei.mulmusic.service.MusicPlayer;

import java.util.LinkedList;

/**
 * Created by 高春雷 on 2017/8/30.
 */

public class MediaListFragment extends BaseFragment {
    /**
     * 记录删除的位置数组
     */
    private View view;
    public final static String ACCEPT_MSG = "ACCEPT_MSG_MEDIALISTFRAGMENT";
    private ItemTouchHelper itemTouchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        MessageManager.instance().registerObserver(MediaListFragment.ACCEPT_MSG, this);
        initAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper.Callback callback = new RecyItemTouchCallback(adapter,false,true);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void findView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setData(LinkedList<Music> musics) {
        this.musics = musics;
    }

    public void refresh() {
        setData(musics);
        adapter.notifyDataSetChanged();
    }

    public void initAdapter() {
        adapter = new MusicListAdapter(musics);
        adapter.setContext(getContext());
        adapter.setItemOnClickListener(new BaseMusicAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClickListener(long musicIndex) {
                MessageManager.instance().notify(MainActivity.MSG_NAME, "改变底部播放状态");
                CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.allList,musics);
                MusicPlayer.playMusicById(musicIndex);
            }
        });
        adapter.setRepeatCountListener(new BaseMusicAdapter.RepeatCountListener() {
            @Override
            public void repeatCountListener(int index,int repeatCount) {
                CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.allList,musics);
                MusicPlayer.resetPlayCount(musics.get(index).getId(),index,repeatCount);
            }
        });
    }

    public Object AcceptMessage(Object[] data) {
        CurrentPlayList.instance().curList = CurrentPlayList.PlayingList.otherList;
        return null;
    }
}
