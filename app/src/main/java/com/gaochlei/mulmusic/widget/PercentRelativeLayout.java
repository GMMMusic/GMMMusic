package com.gaochlei.mulmusic.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gaochlei.mulmusic.R;

/**
 * Created by 高春雷 on 2016/12/25.
 */

public class PercentRelativeLayout extends RelativeLayout {
    public PercentRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentRelativeLayout(Context context) {
        super(context);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(),attrs);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取自身的宽和高
        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);
        for(int i = 0;i < this.getChildCount(); i ++){
            View child = this.getChildAt(i);
            //获取孩子view的布局属性
            ViewGroup.LayoutParams params = child.getLayoutParams();
            float widthPercent = 0;
            float heightPercent = 0;
            //含有自定义属性则获取百分比
            if(params instanceof PercentRelativeLayout.LayoutParams){
                widthPercent = ((PercentRelativeLayout.LayoutParams) params).getWidthPercent();
                heightPercent = ((PercentRelativeLayout.LayoutParams) params).getHeightPercent();
            }
            //如果宽和高的百分比为0，跳出循环
            if(widthPercent == 0 || heightPercent == 0){
                continue;
            }
            //真实的宽和高
            params.width = (int) (widthHint * widthPercent);
            params.height = (int) (heightHint * heightPercent);
        }
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams{
        private float widthPercent;
        private float heightPercent;

        public void setWidthPercent(float widthPercent){
            this.widthPercent = widthPercent;
        }

        public float getWidthPercent(){
            return widthPercent;
        }

        public void setHeightPercent(float heightPercent){
            this.heightPercent = heightPercent;
        }

        public float getHeightPercent(){
            return heightPercent;
        }



        //构造函数里获取自定义样式属性的值
        public LayoutParams(Context context, AttributeSet attrs){
            super(context,attrs);
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.percentRelativeLayout);
            widthPercent = array.getFloat(R.styleable.percentRelativeLayout_widthPercent,widthPercent);
            heightPercent = array.getFloat(R.styleable.percentRelativeLayout_heightPercent,heightPercent);
            array.recycle();
        }

        public LayoutParams(int w, int h, float widthPercent) {
            super(w, h);
            this.widthPercent = widthPercent;
        }

        /**
         * {@inheritDoc}
         *
         * @param source
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         *
         * @param source
         */
        public LayoutParams(MarginLayoutParams source, float widthPercent) {
            super(source);
            this.widthPercent = widthPercent;
        }
    }
}
