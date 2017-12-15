package com.gaochlei.mulmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaochlei.mulmusic.R;
import com.gaochlei.mulmusic.dbmanager.DbMusicManager;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.msgline.AbsMessage;
import com.gaochlei.mulmusic.utils.Setting;
import com.gaochlei.mulmusic.utils.imageUtils.ImageLoader;

import java.util.List;

/**
 * Created by 高春雷 on 2016/12/25.
 */

public abstract class BaseMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        AbsMessage{
    protected ItemOnClickListener itemOnClickListener;
    protected RepeatCountListener repeatCountListener;
    protected List<Music> list;
    protected Context context;
    protected ImageLoader imageLoader;

    public void setContext(Context context) {
        this.context = context;
    }

    public BaseMusicAdapter(List<Music> list) {
        this.list = list;
        imageLoader = ImageLoader.getmInstance();
    }

    public void setData(List<Music> musics){
        this.list = musics;
    }
    public List<Music> getDataList(){
        return list;
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    /**
     * @param repeatCountListener
     * 设置监听重复次数的接口
     */
    public void setRepeatCountListener(RepeatCountListener repeatCountListener){
        this.repeatCountListener = repeatCountListener;
    }

    @Override
    public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_music_recyclerview, parent, false);
        return new MusicHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MusicHolder musicHolder = (MusicHolder)holder;
        final Music music = list.get(position);
        music.setPosition(position);
        holder.itemView.setTag(music);
        imageLoader.loadImage4Id(context, musicHolder.imageView, music);
        musicHolder.musicId = music.getId();
        musicHolder.num_tv.setText(music.getPlayCount() + "");
        if (!TextUtils.isEmpty(music.getTitle())) {
            musicHolder.title.setText(music.getTitle());
        } else if (!TextUtils.isEmpty(music.getName())) {
            musicHolder.title.setText(music.getName());
        } else{
            musicHolder.title.setText("未命名");
        }
        musicHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null) {
                    itemOnClickListener.itemOnClickListener(musicHolder.musicId);
                }
            }
        });
        musicHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repeat_num = musicHolder.num_tv.getText().toString().trim();
                int num = Integer.parseInt(repeat_num);
                if (num >= Setting.repeatCount) {
                    num = 1;
                } else {
                    num++;
                }
                repeatCountListener.repeatCountListener(position,num);
                music.setPlayCount(num);
                musicHolder.num_tv.setText(num + "");
                if (dbManger == null) {
                    dbManger = DbMusicManager.initMusicManager(v.getContext());
                }
                dbManger.update(music);
            }
        });
    }

    private DbMusicManager dbManger;

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }else{
            return 0;
        }
    }


    public class MusicHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;
        public TextView num_tv;
        public long musicId;
        public MusicHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.id_image_music);
            title = (TextView) itemView.findViewById(R.id.id_title_music);
            num_tv = (TextView) itemView.findViewById(R.id.id_tv_repeat_num);
            musicId = 0;
        }
    }

    public interface ItemOnClickListener {
        void itemOnClickListener(long musicIndex);
    }

    public interface RepeatCountListener {
        void repeatCountListener(int index,int repeatCount);
    }
}
