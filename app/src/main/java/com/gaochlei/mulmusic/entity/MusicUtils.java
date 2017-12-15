package com.gaochlei.mulmusic.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 高春雷 on 2017/11/1.
 */

public class MusicUtils {
    /**
     * 根据音乐文件的ID查找该音乐文件的下标
     * @param musicId
     * @return
     */
    public static int getMusicIndex(List<Music> list,long musicId){
        int index = 0;
        for(int i = 0; i < list.size(); i ++){
            if(list.get(i).getId() == musicId){
                index = i;
                break;
            }
        }
        return index;
    }
    public static String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date now = new Date();
        return sdf.format(now);
    }
}
