package com.gaochlei.mulmusic.service;

import com.gaochlei.mulmusic.entity.Music;

import java.util.List;

/**
 * 记录当前播放的列表，避免重复加载
 * Created by 高春雷 on 2017/9/25.
 */

public class CurrentPlayList {
    public enum PlayingList{
        allList,newList,likeList,historyList,otherList,searchList;
    }
    private CurrentPlayList(){}
    private static CurrentPlayList currentPlayList;
    public static CurrentPlayList instance(){
        if(currentPlayList == null){
            currentPlayList = new CurrentPlayList();
        }
        return currentPlayList;
    }
    public PlayingList curList = PlayingList.allList;

    /**
     * service去重加载musics
     * @param curList
     * @param musics
     */
    public static void reloadMusics(PlayingList curList, List<Music> musics) {
        if (CurrentPlayList.instance().curList != curList) {
            MusicPlayer.reloadMusicList(musics);
        }
        CurrentPlayList.instance().curList = curList;
    }
}
