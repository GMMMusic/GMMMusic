package com.gaochlei.mulmusic.dbmanager;

import android.content.Context;

import com.gaochlei.mulmusic.entity.DaoMaster;
import com.gaochlei.mulmusic.entity.DaoSession;
import com.gaochlei.mulmusic.entity.LastPlayMusic;
import com.gaochlei.mulmusic.entity.LastPlayMusicDao;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.entity.MusicDao;
import com.gaochlei.mulmusic.entity.MusicHistory;
import com.gaochlei.mulmusic.entity.MusicHistoryDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;


/**
 * Created by 高春雷 on 2017/1/13.
 */

public class DbMusicManager {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static DbMusicManager initMusicManager(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = null;
        devOpenHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "mvp.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        return new DbMusicManager();
    }

    public MusicDao getMusicDao(){
        return daoSession.getMusicDao();
    }

    public MusicHistoryDao getMusicHistoryDao(){
        return daoSession.getMusicHistoryDao();
    }

    public List<MusicHistory> selectMusicHistory(String date){
        Query<MusicHistory> query = getMusicHistoryDao().queryBuilder()
                .where(MusicHistoryDao.Properties.Date.eq(date))
                .build();
        return query.list();
    }

    public LastPlayMusicDao getLastMusicDao(){
        return daoSession.getLastPlayMusicDao();
    }

    public void addLastMusic(LastPlayMusic lastPlayMusic){
        List<LastPlayMusic> list = queryAllLastMusic();
        if(list != null && list.size() >= 50){
           deleteLastMusic(list.get(list.size() - 1));
        }
        Query<LastPlayMusic> query = getLastMusicDao().queryBuilder().where(LastPlayMusicDao
                .Properties.Id.eq(lastPlayMusic.getId())).build();
        if(query.list() != null && query.list().size() > 0){
            getLastMusicDao().deleteByKey(lastPlayMusic.getId());
        }
        getLastMusicDao().insert(lastPlayMusic);
    }

    public Music queryMusicById(long musicId){
        Query<Music> query = getMusicDao().queryBuilder()
                .where(MusicDao.Properties.Id.eq(musicId))
                .build();
        return query.list().get(0);
    }

    public LastPlayMusic queryLastMusic(){
        Query<LastPlayMusic> query = getLastMusicDao().queryBuilder().where(LastPlayMusicDao.Properties.SortId.notEq(0l)).orderDesc(LastPlayMusicDao.Properties.SortId).build();
        List<LastPlayMusic> list = query.list();
//        List<LastPlayMusic> list = getLastMusicDao().loadAll();
        if(getLastMusicDao() != null && list != null && list.size() > 0  ) {
            return list.get(0);
        }else
        {
            return null;
        }
    }


    public List<LastPlayMusic> queryAllLastMusic(){
        return getLastMusicDao().loadAll();
    }


    public void deleteLastMusic(LastPlayMusic lastMusic){
         getLastMusicDao().delete(lastMusic);
    }

    public void addMusic(Music music){
        getMusicDao().insert(music);
    }

    public void insertMusicList(List<Music> musicList){
        getMusicDao().insertOrReplaceInTx(musicList,true);
    }
    public void insertMusicHistory(MusicHistory musicHistory){
        getMusicHistoryDao().insert(musicHistory);
    }

    public long queryRecord(){
        return getMusicDao().queryBuilder().count();
    }

    public List<Music> queryMusicListAll(){
        List<Music> userList = getMusicDao().loadAll();
        return userList;
    }

    public List<Music> queryMusicByName(String name){
        List<Music> musics = getMusicDao().queryBuilder()
                .where(MusicDao.Properties.Title.like("%" + name + "%"))
                .build()
                .list();
        return musics;
    }

    public void deleteMusic(Music music){
        getMusicDao().delete(music);
    }

    public void deleteMusicById(int id){
        Music music = getMusicDao().queryBuilder().
                where(MusicDao.Properties.Id.eq(id)).build().unique();
        if (music == null) {
            //music not exist;
        }else{
            getMusicDao().deleteByKey(music.getId());
        }
    }

    public List<Music> queryUserList(){
        List<Music> list = getMusicDao().queryBuilder()
                .where(MusicDao.Properties.Id.between(2, 13)).limit(5).build().list();
        return  list;
    }

    /**
     * @param music 音乐某个属性修改，使用此方法
     */
    public void update(Music music){
        getMusicDao().update(music);
    }
}
