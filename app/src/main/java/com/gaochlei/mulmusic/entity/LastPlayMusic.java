package com.gaochlei.mulmusic.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * Created by 高春雷 on 2017/5/8.
 */
@Entity
public class LastPlayMusic implements Serializable {
    public static final long serialVersionUID = -34523L;
    /**音乐文件的id*/
    @Id
    private long id;
    /**播放的次数*/
    @Property(nameInDb = "PLAYCOUNT")
    private int playCount = 1;
    /**音乐文件的大小*/
    @Property(nameInDb = "SIZE")
    private long size;
    /**是否喜欢*/
    @Property(nameInDb = "LIKE")
    private int like;
    /**用户设置的播放顺序*/
    @Property(nameInDb = "PLAYORDER")
    private int playOrder;
    /**音频文件的时长*/
    @Property(nameInDb = "DURATION")
    private int duration;
    /**文件的本地url*/
    @Property(nameInDb = "URL")
    private String url;
    /**歌曲所属专辑*/
    @Property(nameInDb = "ARTIC")
    private String artic;
    /**歌曲图片专辑id*/
    @Property(nameInDb = "ALBUMID")
    private long albumId;
    /**歌曲名称*/
    @Property(nameInDb = "TITLE")
    private String title;
    /**歌曲专辑封面图片*/
    @Property(nameInDb = "ALBUM")
    private String album;
    @Property(nameInDb = "NAME")
    private String name;
    @Property(nameInDb = "OTHER")
    private String other;
    @Property(nameInDb = "LASTPLAYEDID")
    private long lastPlayedId;
    @Property(nameInDb = "PLAYEDRATE")
    private double playedrate;
    /**
     * 所属list列表的id
     */
    @Property(nameInDb = "LISTID")
    private long listId;
    @Property(nameInDb = "POSITION")
    private int position;
    /**
     * 用于排序
     */
    @Property(nameInDb = "SORTID")
    private long sortId = 0l;
    @Generated(hash = 307518912)
    public LastPlayMusic(long id, int playCount, long size, int like, int playOrder,
            int duration, String url, String artic, long albumId, String title,
            String album, String name, String other, long lastPlayedId,
            double playedrate, long listId, int position, long sortId) {
        this.id = id;
        this.playCount = playCount;
        this.size = size;
        this.like = like;
        this.playOrder = playOrder;
        this.duration = duration;
        this.url = url;
        this.artic = artic;
        this.albumId = albumId;
        this.title = title;
        this.album = album;
        this.name = name;
        this.other = other;
        this.lastPlayedId = lastPlayedId;
        this.playedrate = playedrate;
        this.listId = listId;
        this.position = position;
        this.sortId = sortId;
    }
    @Generated(hash = 642410513)
    public LastPlayMusic() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getPlayCount() {
        return this.playCount;
    }
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public int getLike() {
        return this.like;
    }
    public void setLike(int like) {
        this.like = like;
    }
    public int getPlayOrder() {
        return this.playOrder;
    }
    public void setPlayOrder(int playOrder) {
        this.playOrder = playOrder;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getArtic() {
        return this.artic;
    }
    public void setArtic(String artic) {
        this.artic = artic;
    }
    public long getAlbumId() {
        return this.albumId;
    }
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAlbum() {
        return this.album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOther() {
        return this.other;
    }
    public void setOther(String other) {
        this.other = other;
    }
    public long getLastPlayedId() {
        return this.lastPlayedId;
    }
    public void setLastPlayedId(long lastPlayedId) {
        this.lastPlayedId = lastPlayedId;
    }
    public double getPlayedrate() {
        return this.playedrate;
    }
    public void setPlayedrate(double playedrate) {
        this.playedrate = playedrate;
    }
    public long getListId() {
        return this.listId;
    }
    public void setListId(long listId) {
        this.listId = listId;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public void setMusic(Music music){
        this.setPlayCount(music.getPlayCount());
        this.setTitle(music.getTitle());
        this.setAlbum(music.getAlbum());
        this.setAlbumId(music.getAlbumId());
        this.setArtic(music.getArtic());
        this.setId(music.getId());
        this.setLike(music.getLike());
        this.setName(music.getName());
        this.setPlayOrder(music.getPlayOrder());
        this.setSize(music.getSize());
        this.setUrl(music.getUrl());
        this.setOther(music.getOther());
    }
    public static Music getMusic(LastPlayMusic lastPlayMusic){
        Music music = new Music();
        music.setPlayCount(lastPlayMusic.getPlayCount());
        music.setTitle(lastPlayMusic.getTitle());
        music.setAlbumId(lastPlayMusic.getAlbumId());
        music.setAlbum(lastPlayMusic.getAlbum());
        music.setArtic(lastPlayMusic.getArtic());
        music.setId(lastPlayMusic.getId());
        music.setLike(lastPlayMusic.getLike());
        music.setName(lastPlayMusic.getName());
        music.setPlayOrder(lastPlayMusic.getPlayOrder());
        music.setSize(lastPlayMusic.getSize());
        music.setUrl(lastPlayMusic.getUrl());
        music.setOther(lastPlayMusic.getOther());
        return music;
    }
    public long getSortId() {
        return this.sortId;
    }
    public void setSortId(long sortId) {
        this.sortId = sortId;
    }
}
