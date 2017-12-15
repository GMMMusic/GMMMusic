package com.gaochlei.mulmusic.adapter.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import com.gaochlei.mulmusic.adapter.BaseMusicAdapter;
import com.gaochlei.mulmusic.adapter.MusicListAdapter;
import com.gaochlei.mulmusic.adapter.NewMusicAdapter;
import com.gaochlei.mulmusic.adapter.SearchAdapter;
import com.gaochlei.mulmusic.dbmanager.DbMusicManager;
import com.gaochlei.mulmusic.entity.Music;
import com.gaochlei.mulmusic.fragment.NewPlayListFragment;
import com.gaochlei.mulmusic.msgline.MessageManager;
import com.gaochlei.mulmusic.service.CurrentPlayList;
import com.gaochlei.mulmusic.service.MusicPlayer;

import java.util.Collections;

/**
 * Created by 高春雷 on 2017/11/1.
 */

public class RecyItemTouchCallback extends Callback {
    private RecyclerView.Adapter mAdapter;
    private boolean isFirstDragUnable;
    private boolean isSwipeEnable;

    public RecyItemTouchCallback(RecyclerView.Adapter mAdapter,boolean isFirstDragUnable,boolean isSwipeEnable){
        this.mAdapter = mAdapter;
        this.isFirstDragUnable = isFirstDragUnable;
        this.isSwipeEnable = isSwipeEnable;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(((BaseMusicAdapter) mAdapter).getDataList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(((BaseMusicAdapter) mAdapter).getDataList(), i, i - 1);
            }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int adapterPosition = viewHolder.getAdapterPosition();
        if(mAdapter instanceof MusicListAdapter){
            CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.allList,((BaseMusicAdapter) mAdapter).getDataList());
            if(direction == 32) {
                MessageManager.instance().notify(NewPlayListFragment.MSG_NAME, ((BaseMusicAdapter) mAdapter).getDataList().get(adapterPosition));
            }
        }else if(mAdapter instanceof NewMusicAdapter){

        }else if(mAdapter instanceof SearchAdapter){
            CurrentPlayList.reloadMusics(CurrentPlayList.PlayingList.allList,((BaseMusicAdapter) mAdapter).getDataList());
            MessageManager.instance().notify(NewPlayListFragment.MSG_NAME, ((BaseMusicAdapter) mAdapter).getDataList().get(adapterPosition));
        }
        if(direction ==32){
            mAdapter.notifyItemRemoved(adapterPosition);
            ((BaseMusicAdapter) mAdapter).getDataList().remove(adapterPosition);
        }
        else{
            Music music = ((BaseMusicAdapter) mAdapter).getDataList().get(adapterPosition);
            music.setPlayCount(1);
            DbMusicManager dbManger = DbMusicManager.initMusicManager(viewHolder.itemView.getContext());
            dbManger.update(music);
            MusicPlayer.resetPlayCount(music.getId(),adapterPosition,1);
            mAdapter.notifyItemChanged(adapterPosition);
        }

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return !isFirstDragUnable;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnable;
    }
}
