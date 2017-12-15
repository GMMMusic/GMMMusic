// MediaAidlInterface.aidl
package com.gaochlei.mulmusic.entity;
import com.gaochlei.mulmusic.entity.Music;
// Declare any non-default types here with import statements

interface MediaAidlInterface {
     /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
       void play(int musicIndex);
       void playByMusicId(long musicId);
       boolean playing();
       void seek(long position);
       void stop();
       void pause();
       void start();
       long position();
       long duration();
       int playingMusicId();
       void setRepeatCount(int repeatCount);
       void sendSong(long songId);
       void sendMusic(in Music music);
       void sendMusics(in List<Music> musics);
       List<Music> loadNewPlayMusics();
       void resetPlayCount(long id,int index,int repeatCount);
}
