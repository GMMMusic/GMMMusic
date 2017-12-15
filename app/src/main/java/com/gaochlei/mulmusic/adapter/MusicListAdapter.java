package com.gaochlei.mulmusic.adapter;

import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.utils.ToastLog.Tlog;

import java.util.List;

/**
 * Created by 高春雷 on 2017/9/24.
 */

public class MusicListAdapter extends BaseMusicAdapter {

    public MusicListAdapter(List<Music> list) {
        super(list);
    }
    @Override
    public Object AcceptMessage(Object[] data) {
        return null;
    }
}
