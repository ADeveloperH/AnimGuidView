package com.example.myguideview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int[] imgesBgIds = new int[]{R.mipmap.onboarding_page3_app_bg,
            R.mipmap.onboarding_page1_qipao
            ,R.mipmap.onboarding_page2_qipao2};
    private int[] imagesAnimIds = new int[]{R.mipmap.anim_one,
            R.mipmap.anim_two_first,
            R.mipmap.anim_two_twice
            ,R.mipmap.anim_three};
    private MyGuideView mgv;
    private LinearLayout llPointContiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initView();
        initData();
        setListener();
    }



    private void initView() {
        mgv = (MyGuideView) findViewById(R.id.mgv);
        llPointContiner = (LinearLayout) findViewById(R.id.ll_point_continer);
    }


    private void initData() {
        for (int i = 0; i < imgesBgIds.length; i++) {
            ImageView imageView = new ImageView(this);
//            imageView.setBackgroundResource(imgesBgIds[i]);
            imageView.setImageBitmap(Utils.getBitmapForImgResourse(this,imgesBgIds[i]));
            //将图片添加至自定义的view中去
            mgv.addView(imageView);
        }

        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setText("这里是Page" + i);
            mgv.addView(textView);
        }

        for (int i = 0; i < imagesAnimIds.length; i++) {
            ImageView imageView = new ImageView(this);
//            imageView.setBackgroundResource(imagesAnimIds[i]);
            imageView.setImageBitmap(Utils.getBitmapForImgResourse(this,imagesAnimIds[i]));
            //将图片添加至自定义的view中去
            mgv.addView(imageView);
        }

        TextView textView = new TextView(this);
        textView.setTextSize(10);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setText("中国移动10086客服APP");
        mgv.addView(textView);



        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
        lp.setMargins(10,0,0,0);
        //添加圆点
        for (int i = 0; i < 3; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.guide_point_selector);

            if (i == 0) {
                view.setEnabled(true);
            } else {
                view.setEnabled(false);
            }
            llPointContiner.addView(view,lp);
        }
    }

    private void setListener() {
        mgv.setOnPageChangeListener(new MyGuideView.IOnPageChangeListener() {
            @Override
            public void onPageChanged(int index) {
                for (int i = 0; i < llPointContiner.getChildCount(); i++) {
                    if (i == index) {
                        llPointContiner.getChildAt(i).setEnabled(true);
                    } else {
                        llPointContiner.getChildAt(i).setEnabled(false);
                    }

                }
            }
        });
    }
}
