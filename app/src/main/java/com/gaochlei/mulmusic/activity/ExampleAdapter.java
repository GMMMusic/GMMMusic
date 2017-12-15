package com.gaochlei.mulmusic.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaochlei.mulmusic.R;

/**
 * Created by ldf on 17/6/14.
 */

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private String[] titles;

    public ExampleAdapter(Context context) {
//        titles = context.getResources().getStringArray(R.array.titles);
        titles = new String[]{"京华烟云","大宅门","倩女幽魂","午夜凶铃","蝙蝠侠","蜘蛛侠","超人大战蝙蝠侠"
        ,"神奇女侠哦","鸟”","蓝色星球","地球脉动","三傻大闹宝莱坞"};
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ExampleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(titles[position]);
    }

    @Override
    public int getItemCount() {
        return titles == null ? 0 : titles.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text_view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}
