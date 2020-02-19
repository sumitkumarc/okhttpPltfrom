package com.newiplquizgame.myipl.extra;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ZoomOutTransformation implements ViewPager.PageTransformer {

//    private static final float MIN_SCALE = 0.65f;
//    private static final float MIN_ALPHA = 0.3f;

    final float MIN_SCALE = 0.8f;
    final int paddingPx = 100;
    final float MAX_SCALE = 1f;

    @Override
    public void transformPage(View page, float position) {
        float pagerWidthPx = ((ViewPager) page.getParent()).getWidth();
        float pageWidthPx = pagerWidthPx - 2 * paddingPx;

        float maxVisiblePages = pagerWidthPx / pageWidthPx;
        float center = maxVisiblePages / 2f;

        float scale;
        if (position + 0.5f < center - 0.5f || position > center) {
            scale = MIN_SCALE;
        } else {
            float coef;
            if (position + 0.5f < center) {
                coef = (position + 1 - center) / 0.5f;
            } else {
                coef = (center - position) / 0.5f;
            }
            scale = coef * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
        }
        page.setScaleX(scale);
        page.setScaleY(scale);
//
//        if (position <-1){  // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            page.setAlpha(0);
//
//        }
//        else if (position <=1){ // [-1,1]
//
//            page.setScaleX(Math.max(MIN_SCALE,1-Math.abs(position)));
//            page.setScaleY(Math.max(MIN_SCALE,1-Math.abs(position)));
//            page.setAlpha(Math.max(MIN_ALPHA,1-Math.abs(position)));
//
//        }
//        else {  // (1,+Infinity]
//            // This page is way off-screen to the right.
//            page.setAlpha(0);
//
//        }


    }
}