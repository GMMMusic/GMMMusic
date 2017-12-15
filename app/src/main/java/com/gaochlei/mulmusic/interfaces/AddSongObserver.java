package com.gaochlei.mulmusic.interfaces;

import com.gaochlei.mulmusic.entity.Music;

/**
 * Created by 高春雷 on 2017/9/10.
 */

public  interface AddSongObserver  {
    public void updateMusics(Music song);
}
