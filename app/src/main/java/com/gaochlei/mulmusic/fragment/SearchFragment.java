package com.gaochlei.mulmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gaochlei.mulmusic.R;
import com.gaochlei.mulmusic.activity.MainActivity;
import com.gaochlei.mulmusic.adapter.BaseMusicAdapter;
import com.gaochlei.mulmusic.adapter.SearchAdapter;
import com.gaochlei.mulmusic.adapter.helper.RecyItemTouchCallback;
import com.gaochlei.mulmusic.dbmanager.DbMusicManager;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.msgline.MessageManager;
import com.gaochlei.mulmusic.service.CurrentPlayList;
import com.gaochlei.mulmusic.service.MusicPlayer;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by 高春雷 on 2017/11/27.
 */

public class SearchFragment extends BaseFragment {
    private EditText et_search;
    private ItemTouchHelper itemTouchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper.Callback callback = new RecyItemTouchCallback(adapter, false, true);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        et_search = (EditText) getView().findViewById(R.id.id_et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    musics = DbMusicManager.initMusicManager(getActivity()).queryMusicByName(s
                            .toString());
                    if (musics != null && musics.size() > 0) {
                        adapter.setData(musics);
                        adapter.notifyDataSetChanged();
                        MusicPlayer.reloadMusicList(musics);
                    }else{
                        musics.clear();
                        adapter.setData(musics);
                        adapter.notifyDataSetChanged();
                        MusicPlayer.reloadMusicList(musics);
                    }
                } else {
                    if (musics != null && musics.size() > 0) {
                        musics.clear();
                        adapter.setData(musics);
                        adapter.notifyDataSetChanged();
                        MusicPlayer.reloadMusicList(musics);
                    }
                }

            }
        });
    }

    @Override
    public void findView() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.id_recyl_search);
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
        musics = new ArrayList<>();
        adapter = new SearchAdapter(musics);
        adapter.setContext(getContext());
        adapter.setItemOnClickListener(new BaseMusicAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClickListener(long musicIndex) {
                MessageManager.instance().notify(MainActivity.MSG_NAME, "改变底部播放状态");
                CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.searchList,musics);
                MusicPlayer.playMusicById(musicIndex);
            }
        });
        adapter.setRepeatCountListener(new BaseMusicAdapter.RepeatCountListener() {
            @Override
            public void repeatCountListener(int index, int repeatCount) {
                CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.searchList,musics);
                MusicPlayer.resetPlayCount(musics.get(index).getId(), index, repeatCount);
            }
        });
    }

    public Object AcceptMessage(Object[] data) {
        return null;
    }
}
