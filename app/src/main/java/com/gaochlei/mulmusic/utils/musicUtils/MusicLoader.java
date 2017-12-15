package com.gaochlei.mulmusic.utils.musicUtils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.gaochlei.mulmusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高春雷 on 2016/12/31.
 */

public class MusicLoader {
    private static final String TAG = "com.gaochlei.mulmusic";
    private static List<Music> musicList = new ArrayList<Music>();
    private static MusicLoader musicLoader;
    private static ContentResolver contentResolver;
    private Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    /**projection 选择的列*/
    private String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };
    private String where = "meme_type in ('audio/x-ms-wma') and bucket_diaplay_name <> 'audio' and is_music > 0";
    private String sortOrder = MediaStore.Audio.Media.DATA;

    public static MusicLoader instance(ContentResolver pContentResolver){
        if(musicLoader == null){
            contentResolver = pContentResolver;
            musicLoader = new MusicLoader();
        }
        return musicLoader;
    }

    private MusicLoader(){
//        Cursor cursor = contentResolver.query(contentUri,projection,where,null,sortOrder);
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        if(cursor == null){
            Log.v(TAG,"MusicLoader cursor == null");
        }else {
            if (!cursor.moveToFirst()) {
                Log.v(TAG, "MusicLoader cursor moveToFirst returns false");
            } else {
                int audioName = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int albumCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int audioId = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int audioDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int audioSize = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
                int audioArt = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int audioUrl = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int audioAlbumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                do {
                    String musicName = cursor.getString(audioName);
                    String musicAlbum = cursor.getString(albumCol);
                    long musicId = cursor.getLong(audioId);
                    int musicDuration = cursor.getInt(audioDuration);
                    long musicSize = cursor.getLong(audioSize);
                    String musicArtist = cursor.getString(audioArt);
                    String musicUrl = cursor.getString(audioUrl);
                    String musicTitle = cursor.getString(title);
                    long musicAlbumId = cursor.getLong(audioAlbumId);

                    Music music = new Music();
                    music.setArtic(musicArtist);
                    music.setUrl(musicUrl);
                    music.setSize(musicSize);
                    music.setDuration(musicDuration);
                    music.setId(musicId);
                    music.setAlbum(musicAlbum);
                    music.setName(musicName);
                    music.setAlbumId(musicAlbumId);
                    music.setTitle(musicTitle);
                    musicList.add(music);
                } while (cursor.moveToNext());
            }
        }

    }
    public List<Music> getMusicList(){
        return musicList;
    }

    public Uri getContentUriById(long id){
        Uri uri = ContentUris.withAppendedId(contentUri,id);
        return uri;
    }

}
