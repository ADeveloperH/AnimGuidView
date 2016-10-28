package com.example.myguideview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class MyGuideView extends ViewGroup {
    private final String TAG = "MyGuideView";

    private GestureDetector gestureDetector;
    private MyGuideScroller scroller;

    private long childScrollX = 0;//子viewx轴方向移动的距离
    private long childScrollY = 0;//子viewx轴方向移动的距离
    private boolean startScroll = false;

    private double moveStepX = 2/3.0;
    private double moveStepY = -1/3.0;

    public MyGuideView(Context context) {
        super(context);
        init(context);
    }

    public MyGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if ((currentIndex == 0 && distanceX <= 0 && childScrollX >= 0)
                        || (currentIndex == 2 && distanceX >= 0 && childScrollX <= 0 )) {
                    return true;
                }
                childScrollX += -(int) distanceX;
                childScrollY += (int) distanceX/2;
                for (int i = 0; i < 3; i++) {
                    getChildAt(i).offsetTopAndBottom((int) distanceX/2);
                    getChildAt(i).offsetLeftAndRight(-(int) distanceX);
                }

                for (int i = 3; i < 6; i++) {
                    getChildAt(i).offsetLeftAndRight(-(int) distanceX);
                }
                getChildAt(10).offsetLeftAndRight(-(int) distanceX);

                for (int i = 6; i < 10; i++) {
                    int temp = i;
                    if (i > 7) {
                        temp = i -1;
                    }
                    int left = (int) ((temp-currentIndex-6)* getWidth()- childScrollY*2);
                    int top ;
                    if (i == 8) {
                        top = (int) (-(temp - 6- currentIndex) * getWidth() / 2
                                + Math.abs(childScrollX));
                    } else {
                        top = (int) (-(temp - 6- currentIndex) * getWidth() / 2
                                - Math.abs(childScrollX));
                    }
                    getChildAt(i).layout( left,
                            top,
                            getWidth() + left,
                            getHeight() + top);
                    getChildAt(i).setAlpha((float) ((255-Math.abs(childScrollX))/255.0));
                }

                System.out.println("childScrollX:::" + childScrollX + "   childScrollY::" + childScrollY);

                startScroll = false;
                postInvalidate();
                return super.onScroll(e1, e2, distanceX, distanceY);
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                System.out.println("onFling");
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        scroller = new MyGuideScroller();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            //测量子控件的大小
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
    }


    /**
     * i:0---2 3个方块
     *   3---5 3行顶部的介绍文字
     *   6---9 4个需要动画的ImageView
     *   10    第一页底部描述文字
     *
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < 3; i++) {
            int left = (int) (i* getWidth()* moveStepX);
            int top = (int) (i * getWidth() * moveStepY);
            getChildAt(i).layout(left,
                    top,
                    getWidth() + left,
                    getHeight() + top);
        }
        for (int i = 3; i < 6; i++) {
            int left = (i-3)* getWidth();
            getChildAt(i).layout(left,
                    50,
                    getWidth() + left,
                    250);
        }
        for (int i = 6; i < 10; i++) {
            int temp = i;
            if (i > 7) {
                temp = i -1;
            }
            int left = (temp - 6) * getWidth();
            int top = -(temp - 6) * getWidth();
            getChildAt(i).layout(left,
                    top,
                    getWidth() + left,
                    getHeight() + top);
        }

        getChildAt(10).layout(0,getHeight() - 100,getWidth(),getHeight() - 50);

    }



    private int downX;//按下时x轴的坐标
    private  int currentIndex = 0;//当前屏幕显示的页面下标
    private  int lastIndex = 0;//上一个显示的页面下标

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"MotionEvent.ACTION_DOWN");
                downX = (int) event.getX();
                break;
            case  MotionEvent.ACTION_MOVE:
                Log.i(TAG,"MotionEvent.ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"MotionEvent.ACTION_UP");
                if(downX - (int)event.getX() > getWidth()/3){
                    //移动到下一页
                    currentIndex ++;
                }else if((int)event.getX() - downX > getWidth()/3){
                    //移动到上一页
                    currentIndex --;
                }
                moveToDestination(currentIndex);
                break;
        }
        return true;
    }

    /**
     *
     * 移动View到指定的页面
     * @param currentIndex 移动到的页面下标 从0开始
     */
    public void moveToDestination(int currentIndex) {
        if(currentIndex < 0){
            currentIndex = 0;
        }
        if(currentIndex > 2){
            currentIndex = 2;
        }
        this.currentIndex = currentIndex;
        /**
         *
         * 当要改变页面是调用回调方法。触发页面改变的监听
         */
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageChanged(currentIndex);
        }

        System.out.println("lastIndex:::" + lastIndex);
        System.out.println("currentIndex:::" + currentIndex);

        System.out.println("childScrollX::" + childScrollX);
        System.out.println("childScrollY::" + childScrollY);

        if ((lastIndex ==0 && currentIndex == 0 && childScrollX >= 0)
                || (lastIndex == 2 && currentIndex == 2 && childScrollX <= 0 )) {
            //当前页面已经是第一页还要向右滑动。当前页面已经是最后一页还要向左滑动。
            return;
        }

        //当前位置
//        int i = 0;
//        int curX = (int) ((i-lastIndex)* getWidth() + childScrollX);
//        int curY = (int) (-(i-lastIndex) * getWidth()/2 + childScrollY);

        /**
         * 当前位置 = 起始位置 + 已经移动的距离
         * 需要移动的距离 = 目标位置 - 当前位置
         */



        //色块需要移动的x/y轴距离
        int distanceX = (int) ((lastIndex - currentIndex) * getWidth() * moveStepX - childScrollX);
        int distanceY = (int) ((lastIndex - currentIndex) * getWidth() * moveStepY - childScrollY);

        //顶部文字需要移动的距离
        int topTxtDistanceX = (int) ((lastIndex - currentIndex) * getWidth()  - childScrollX);

        //动画元素需要移动的x/y轴距离
        int animDistanceX = (int) ((lastIndex - currentIndex) * getWidth() + childScrollY * 2);
        //i==8的子view需要移动的y轴距离
        int animDiatanceY1 = (int) ((currentIndex - lastIndex) * getWidth() / 2 - Math.abs(childScrollX));
        //i!=8的子view需要移动的y轴距离
        int animDiatanceY2 = (int) ((currentIndex - lastIndex) * getWidth() / 2 + Math.abs(childScrollX));

        System.out.println("需要移动的x；" + distanceX);
        System.out.println("需要移动的y；" + distanceY);

        //当前的alpha：getChildAt(6).getAlpha()。目标是1.0
        float curAlpha = getChildAt(6).getAlpha();
        System.out.println("当前的curAlpha::" + curAlpha);
        if (curAlpha < 0) {
            curAlpha = 0;
        } else if (curAlpha > 1) {
            curAlpha = 1;
        }
        float differAlpha = 1 - curAlpha;

        System.out.println("differAlpha::" + differAlpha);
        scroller.startScroller(distanceX,distanceY,topTxtDistanceX,
                animDistanceX,animDiatanceY1,animDiatanceY2,
                differAlpha,500);

        startScroll = true;
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (startScroll) {
            System.out.println("computeScroll");
            //查询MyScroller当前的位置，刷新页面
            if (scroller.computeScrollOffeset()) {
                long needScrollX = scroller.getNeedScrollX();
                long needScrollY = scroller.getNeedScrollY();
                System.out.println("needScrollX::" + needScrollX + " needScrollY::" + needScrollY);
                for (int i = 0; i < 3; i++) {
                    int left = (int) ((i-lastIndex)* getWidth() * moveStepX + childScrollX + needScrollX);
                    int top = (int) ((i-lastIndex) * getWidth() * moveStepY + childScrollY + needScrollY);
                    getChildAt(i).layout( left,
                            top,
                            getWidth() + left,
                            getHeight() + top);
                }

                long topTxtNeedScrollX = scroller.getTopTxtNeedScrollX();
                for (int i = 3; i < 6; i++) {
                    int left = (int) ((i-3-lastIndex)* getWidth()+ childScrollX + topTxtNeedScrollX);
                    getChildAt(i).layout(left,
                            50,
                            getWidth() + left,
                            250);
                }
                getChildAt(10).layout((int) (-lastIndex*getWidth()+ childScrollX + topTxtNeedScrollX),
                        getHeight() - 100,
                        (int) ((1-lastIndex)*getWidth()+ childScrollX + topTxtNeedScrollX),
                        getHeight() - 50);


                long animNeedScrollX = scroller.getAnimNeedScrollX();
                long animNeedScrollY1 = scroller.getAnimNeedScrollY1();
                long animNeedScrollY2 = scroller.getAnimNeedScrollY2();

                for (int i = 6; i < 10; i++) {
                    int temp = i;
                    if (i > 7) {
                        temp = i -1;
                    }
                    int left = (int) ((temp-lastIndex-6)* getWidth()- childScrollY*2 + animNeedScrollX);
                    int top ;
                    if (i == 8) {
                        top = (int) (-(temp - 6- lastIndex) * getWidth() / 2
                                + Math.abs(childScrollX) + animNeedScrollY1);
                    } else {
                        top = (int) (-(temp - 6- lastIndex) * getWidth() / 2
                                - Math.abs(childScrollX) + animNeedScrollY2);
                    }
                    getChildAt(i).layout( left,
                            top,
                            getWidth() + left,
                            getHeight() + top);
                    getChildAt(i).setAlpha(scroller.getAlpha());
                }
//            invalidate();
                postInvalidate();
            } else {
                System.out.println("移动完成");
                //移动完成
                lastIndex = currentIndex;
                childScrollX = 0;
                childScrollY = 0;
                //终点位置
                for (int i = 0; i < 3; i++) {
                    int left = (int) ((i-currentIndex)* getWidth() * moveStepX);
                    int top = (int) ((i-currentIndex) * getWidth() * moveStepY);
                    getChildAt(i).layout( left,
                            top,
                            getWidth() + left,
                            getHeight() + top);
                }
                for (int i = 3; i < 6; i++) {
                    int left = (i-3-currentIndex)* getWidth();
                    getChildAt(i).layout(left,
                            50,
                            getWidth() + left,
                            250);
                }
                getChildAt(10).layout(-currentIndex*getWidth(),
                        getHeight() - 100,
                        (1-currentIndex)*getWidth(),
                        getHeight() - 50);

                for (int i = 6; i < 10; i++) {
                    int temp = i;
                    if (i > 7) {
                        temp = i -1;
                    }
                    int left = (temp-currentIndex-6)* getWidth();
                    int top = -(temp-currentIndex-6) * getWidth();
                    getChildAt(i).layout( left,
                            top,
                            getWidth() + left,
                            getHeight() + top);
                    getChildAt(i).setAlpha(1);
                }
            }
            super.computeScroll();
        }

    }


    private IOnPageChangeListener onPageChangeListener;
    /**
     *
     * 用来监听页面改变的接口
     */
    public interface IOnPageChangeListener{
        /**
         *
         * 当页面改变时回调该方法。
         * @param index ： 要移动到的页面下标
         */
        void onPageChanged(int index);
    }

    public void setOnPageChangeListener(IOnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
