package com.gaochlei.mulmusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by 高春雷 on 2016/12/25.
 */
@Entity
public class Music implements Parcelable {
//    private static final long serialVersionUID = -13342323l;
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
    @Generated(hash = 384071637)
    public Music(long id, int playCount, long size, int like, int playOrder,
            int duration, String url, String artic, long albumId, String title,
            String album, String name, String other, long lastPlayedId,
            double playedrate, long listId, int position) {
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
    }


    @Generated(hash = 1263212761)
    public Music() {
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getOther(){
        return other;
    }

    public void setOther(String other){
        this.other = other;
    }

    public String getAlbum(){
        return this.album;
    }

    public void setAlbum(String album){
        this.album = album;
    }

    public int getDuration(){
        return duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(int playOrder) {
        this.playOrder = playOrder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtic() {
        return artic;
    }

    public void setArtic(String artic) {
        this.artic = artic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", playCount=" + playCount +
                ", size=" + size +
                ", like=" + like +
                ", playOrder=" + playOrder +
                ", url='" + url + '\'' +
                ", artic='" + artic + '\'' +
                ", title='" + title + '\'' +
                '}';
    }


    public long getAlbumId() {
        return this.albumId;
    }


    public void setAlbumId(long albumId) {
        this.albumId = albumId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(title);
        dest.writeLong(id);
        dest.writeString(album);
        dest.writeLong(albumId);
        dest.writeString(artic);
        dest.writeInt(playCount);
        dest.writeInt(position);
        dest.writeInt(duration);
        dest.writeString(url);
        dest.writeLong(size);
        dest.writeInt(like);
        dest.writeDouble(playedrate);
        dest.writeString(other);
        dest.writeLong(listId);
        dest.writeInt(playOrder);
        dest.writeLong(lastPlayedId);
    }

    public Music(Parcel in){
        name = in.readString();
        title = in.readString();
        id = in.readLong();
        album = in.readString();
        albumId = in.readLong();
        artic = in.readString();
        playCount = in.readInt();
        position = in.readInt();
        duration = in.readInt();
        url = in.readString();
        size = in.readLong();
        like = in.readInt();
        playedrate = in.readDouble();
        other = in.readString();
        listId = in.readLong();
        playOrder = in.readInt();
        lastPlayedId = in.readLong();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };


}
