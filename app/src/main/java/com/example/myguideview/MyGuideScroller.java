package com.example.myguideview;

import android.os.SystemClock;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class MyGuideScroller {

    private int distanceY;
    private int distanceX;
    private int topTxtDistanceX;
    private int animDistanceX;
    private int animDistanceY1;
    private int animDistanceY2;
    private float differAlpha;
    private int total_time;//移动过程的总的时间
    private long start_time;//开始移动的时间
    private  boolean isFinish = false;//判断当前的运动是否完成，默认是没有完成


    private long needScrollX = 0;
    private long needScrollY = 0;
    private long topTxtNeedScrollX = 0;
    private long animNeedScrollX = 0;
    private long animNeedScrollY1 = 0;
    private long animNeedScrollY2 = 0;
    private float alpha = 1;

    /**
     *
     * @param distanceX 色块x轴方向移动的距离
     * @param distanceY 色块y轴方向移动的距离
     * @param topTxtDistanceX 顶部文字x轴方向移动的距离
     * @param animDistanceX 动画的viewx轴方向移动的距离
     * @param animDistanceY1 i==8动画的view，y轴方向移动的距离
     * @param animDistanceY2 i!=8动画的view，y轴方向移动的距离
     * @param differAlpha 动画过程中view的
     * @param total_time 动画需要的总时间
     */
    public void startScroller(int distanceX, int distanceY,int topTxtDistanceX,
                              int animDistanceX,int animDistanceY1,
                              int animDistanceY2, float differAlpha, int total_time) {
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.topTxtDistanceX = topTxtDistanceX;
        this.animDistanceX = animDistanceX;
        this.animDistanceY1 = animDistanceY1;
        this.animDistanceY2 = animDistanceY2;
        this.differAlpha = differAlpha;
        this.total_time = total_time;

        this.start_time = SystemClock.uptimeMillis();//获取当前的时间
        this.needScrollX = this.needScrollY =
                this.animNeedScrollX = this.animNeedScrollY1
                        = this.animNeedScrollY2= this.topTxtNeedScrollX = 0;
        this.alpha = 1;

        isFinish = false;
    }


    /**
     *计算当前的位移量，并返回结果
     * @return true表示还在运动 false表示动画已经结束
     */
    public boolean computeScrollOffeset(){
        if (isFinish) {
//            needScrollX = needScrollY = 0;
//            animNeedScrollX = animNeedScrollY1 = animDistanceY2 = 0;
//            alpha = 1;
//            topTxtNeedScrollX = topTxtNeedScrollX = 0;
            return false;
        }

        long passTime = SystemClock.uptimeMillis() - start_time;//已经消耗的时间
        System.out.println("passTime::" + passTime);
        System.out.println("total_time::" + total_time);
        System.out.println("distanceX::" + distanceX);
        if(passTime < total_time){
            System.out.println("passTime < total_time");
            //还在运动
            // s = v*t; 位移 = 速度* 时间
            needScrollX = (distanceX * passTime / total_time);
            needScrollY = (distanceY * passTime / total_time);
            topTxtNeedScrollX = (topTxtDistanceX * passTime / total_time);
            animNeedScrollX = (animDistanceX * passTime / total_time);
            animNeedScrollY1 = (animDistanceY1 * passTime / total_time);
            animNeedScrollY2 = (animDistanceY2 * passTime / total_time);
            alpha = (differAlpha * passTime / total_time);
            System.out.println("needScrollX::" + needScrollX);
            System.out.println("needScrollY::" + needScrollY);
        }else{
            System.out.println("完成动画");
            //时间到了，运动应该结束了。当前的坐标为终点坐标
            needScrollX = distanceX;
            needScrollY = distanceY;
            topTxtNeedScrollX = topTxtDistanceX;
            animNeedScrollX = animDistanceX;
            animNeedScrollY1 = animDistanceY1;
            animNeedScrollY2 = animDistanceY2;
            alpha = differAlpha = 1;
            isFinish = true;
        }
        return  true;
    }

    public long getNeedScrollX() {
        return needScrollX;
    }

    public long getNeedScrollY() {
        return needScrollY;
    }

    public long getTopTxtNeedScrollX() {
        return topTxtNeedScrollX;
    }

    public long getAnimNeedScrollX() {
        return animNeedScrollX;
    }

    public long getAnimNeedScrollY1() {
        return animNeedScrollY1;
    }

    public long getAnimNeedScrollY2() {
        return animNeedScrollY2;
    }

    public float getAlpha() {
        System.out.println("getAlpha：：" + alpha);
        return alpha;
    }
}
