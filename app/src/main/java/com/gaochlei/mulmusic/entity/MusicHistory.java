package com.gaochlei.mulmusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by 高春雷 on 2017/11/4.
 */
@Entity
public class MusicHistory implements Parcelable{

    @Id
    private String id;
    private long musicId;
    private int playCount;
    private String date;
    @ToOne(joinProperty = "musicId")
    private Music music;

    protected MusicHistory(Parcel in) {
        id = in.readString();
        musicId = in.readLong();
        date = in.readString();
    }


    @Generated(hash = 360845837)
    public MusicHistory() {
    }


    @Generated(hash = 536889865)
    public MusicHistory(String id, long musicId, int playCount, String date) {
        this.id = id;
        this.musicId = musicId;
        this.playCount = playCount;
        this.date = date;
    }




    public static final Creator<MusicHistory> CREATOR = new Creator<MusicHistory>() {
        @Override
        public MusicHistory createFromParcel(Parcel in) {
            return new MusicHistory(in);
        }

        @Override
        public MusicHistory[] newArray(int size) {
            return new MusicHistory[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 797906319)
    private transient MusicHistoryDao myDao;
    @Generated(hash = 993622651)
    private transient Long music__resolvedKey;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.musicId);
        dest.writeString(this.date);
    }


    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public long getMusicId() {
        return this.musicId;
    }


    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }


    public int getPlayCount() {
        return this.playCount;
    }


    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }


    public String getDate() {
        return this.date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1479079417)
    public Music getMusic() {
        long __key = this.musicId;
        if (music__resolvedKey == null || !music__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MusicDao targetDao = daoSession.getMusicDao();
            Music musicNew = targetDao.load(__key);
            synchronized (this) {
                music = musicNew;
                music__resolvedKey = __key;
            }
        }
        return music;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 746099822)
    public void setMusic(@NotNull Music music) {
        if (music == null) {
            throw new DaoException(
                    "To-one property 'musicId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.music = music;
            musicId = music.getId();
            music__resolvedKey = musicId;
        }
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2119916795)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMusicHistoryDao() : null;
    }



}
