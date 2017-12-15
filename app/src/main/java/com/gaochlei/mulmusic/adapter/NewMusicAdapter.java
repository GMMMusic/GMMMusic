package com.gaochlei.mulmusic.adapter;

import android.support.v7.widget.helper.ItemTouchHelper;

import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.service.MusicPlayer;

import java.util.List;

/**
 * Created by 高春雷 on 2017/9/24.
 */

public class NewMusicAdapter extends BaseMusicAdapter {
    public NewMusicAdapter(List<Music> list) {
        super(list);
    }

    @Override
    public Object AcceptMessage(Object[] data) {
        return null;
    }

}
