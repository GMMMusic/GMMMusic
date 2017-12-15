package com.gaochlei.mulmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaochlei.mulmusic.adapter.BaseMusicAdapter;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.msgline.AbsMessage;

import java.util.List;

/**
 * Created by 高春雷 on 2017/8/30.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener, AbsMessage {
    public List<Music> musics;
    public BaseMusicAdapter adapter;
    public RecyclerView mRecyclerView;
    public final static String MSG_NAME = "MSG_BASEFRAGMENT";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        findView();
        super.onActivityCreated(savedInstanceState);
    }

    public List<Music> getMusics() {
        return musics;
    }

    public abstract void findView();

    @Override
    public void onClick(View v) {

    }

    public void setData(List<Music> musics) {
        this.musics = musics;
    }

    public void refresh() {

    }

    @Override
    public Object AcceptMessage(Object[] data) {
        return null;
    }

}
