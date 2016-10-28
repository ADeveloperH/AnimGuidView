package com.example.myguideview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class MyChildView extends ViewGroup {
    private int[] imgesIds = new int[]{R.mipmap.onboarding_page3_app_bg,
            R.mipmap.onboarding_page1_qipao
            ,R.mipmap.onboarding_page2_qipao2};
    private GestureDetector gestureDetector;
    public MyChildView(Context context) {
        super(context);
        init(context);
    }

    public MyChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public MyChildView(Context context,int[] imgesIds) {
        this(context);

        init(context);
    }

    private void init(Context context) {
        for (int i = 0; i < imgesIds.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(imgesIds[i]);
            //将图片添加至自定义的view中去
            addView(imageView);
        }
        //这里我们主要为了实现触摸滑动时View跟着滑动的效果。切换页面的动作没有在这里实现，直接写在onTouchEvent中
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    /**
                     *当手指在屏幕上移动时调用这个方法
                     * @param e1 : down事件的MotionEvent
                     * @param e2 : move事件的MotionEvent
                     * @param distanceX : X轴方向移动的距离
                     * @param distanceY : Y轴方向移动的距离
                     *
                     */
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        System.out.println("onScroll");
                        /**
                         *  移动View，是移动了，表示每次移动某个距离。不是移动到某处
                         * @param distanceX:x轴移动的距离。为正时表示向左移动，为负时向右移动
                         * @param distanceY:y轴移动的距离
                         *
                         */
                        scrollBy((int)distanceX,0);

                        return true;//这个返回值没有什么用,是在onTouchEvent方法中调用时的返回值。我们没有接收这个返回值使用
                    }
                });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
//            getChildAt(i).layout(0 + i* getWidth(),0,getWidth() + i* getWidth(),getHeight());
            getChildAt(i).layout(0 + i* getWidth(),
                    -i * getWidth()/2,
                    getWidth() + i* getWidth(),
                    getHeight() - i * getWidth()/2);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            //测量子控件的大小
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
    }


    @Override
    /**
     * 处理触摸事件
     *
     * 这里我们直接由自己处理，直接返回true
     */
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("onTouchEvent");

        /**
         * 这个方法里面主要处理触摸的滑动事件。
         *
         */
        gestureDetector.onTouchEvent(event);
        return true;
    }
}
