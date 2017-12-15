package com.gaochlei.mulmusic.cusview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaochlei.mulmusic.R;

/**
 * Created by 高春雷 on 2017/1/17.
 */

public class BaseLayout extends RelativeLayout {
//    public View base_top;
    public ImageView iv_bottom_album;
    public TextView tv_bottom_title;
    public View tv_bottom_player;
    public View bottom_baseView;
//    public TextView tv_bottom_menu_all_music;
//    public TextView tv_bottom_like;
//    public TextView tv_bottom_recent;
//    public TextView tv_bottom_new;
    public BaseLayout(Context context,int layoutId){
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bottom_baseView = inflater.inflate(R.layout.base_layout,null);
//        base_top = inflater.inflate(base_top,null);
//        base_top.setId(R.id.rl_base_top);
        iv_bottom_album = (ImageView)bottom_baseView.findViewById(R.id.id_image_base_bottom);
        tv_bottom_player = bottom_baseView.findViewById(R.id.id_pv_player);
        tv_bottom_title = (TextView)bottom_baseView.findViewById(R.id.id_tv_title_bottom);
//        tv_bottom_menu_all_music = (TextView)base_top.findViewById(R.id.id_base_tv_all_music);
//        tv_bottom_like = (TextView)base_top.findViewById(R.id.id_base_tv_like);
//        tv_bottom_recent = (TextView)base_top.findViewById(R.id.id_base_tv_recent);
//        tv_bottom_new = (TextView)base_top.findViewById(R.id.id_menu_hestory);

        View contentView = inflater.inflate(layoutId,null);
//        base_top = inflater.inflate(base_top,null);
        RelativeLayout.LayoutParams  params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP,R.id.rl_base_top);
//        addView(base_top,params);
        params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(contentView,params);
        params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(bottom_baseView,params);
    }

//    public void clickChangeTextColor(int id){
//        tv_bottom_like.setTextColor(Color.BLACK);
//        tv_bottom_recent.setTextColor(Color.BLACK);
//        tv_bottom_new.setTextColor(Color.BLACK);
//        tv_bottom_menu_all_music.setTextColor(Color.BLACK);
//        if(id == R.id.id_menu_hestory){
//            tv_bottom_new.setTextColor(Color.parseColor("#ffb701"));
//        }else if(id == R.id.id_base_tv_recent){
//            tv_bottom_recent.setTextColor(Color.parseColor("#ffb701"));
//        }else if(id == R.id.id_base_tv_all_music){
//            tv_bottom_menu_all_music.setTextColor(Color.parseColor("#ffb701"));
//        }else if(id == R.id.id_base_tv_like){
//            tv_bottom_like.setTextColor(Color.parseColor("#ffb701"));
//        }
//    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
