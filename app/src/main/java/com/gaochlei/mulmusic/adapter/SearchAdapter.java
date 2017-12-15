package com.gaochlei.mulmusic.adapter;

import com.gaochlei.mulmusic.entity.Music;

import java.util.List;

/**
 * Created by 高春雷 on 2017/11/27.
 */

public class SearchAdapter extends BaseMusicAdapter {
    public SearchAdapter(List<Music> list) {
        super(list);
    }

    @Override
    public Object AcceptMessage(Object[] data) {
        return null;
    }
}
