package com.takawo.fan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by 9004027600 on 2015/02/20.
 */
public class ShadowablePagerSlidingTabStrip extends PagerSlidingTabStrip{

    public ShadowablePagerSlidingTabStrip(Context context) {
        super(context);
    }

    public ShadowablePagerSlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowablePagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // getChildAt(0) でタブ本体の View に設定をおこなう
        Shadows.bindAboveShadow(canvas, getChildAt(0));
    }
}
