package com.takawo.fan.view;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;

import static android.graphics.drawable.GradientDrawable.Orientation;
import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;
import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;

/**
 * Created by 9004027600 on 2015/02/20.
 */
/**
 * Improved PeterAttardo/Shadow.java
 * @see{https://gist.github.com/PeterAttardo/cc722b7649d0e62274b2}
 */
public class Shadows {

    private static final int START_COLOR = Color.parseColor("#55000000");

    private static final int END_COLOR = Color.parseColor("#00000000");

    private static final int SHADOW_LENGTH = (int) (5 * Resources.getSystem()
            .getDisplayMetrics().density);

    private static final Orientation[] shadowOrientations =
            new Orientation[]{TOP_BOTTOM, LEFT_RIGHT};

    private static int[] colors = new int[]{START_COLOR, END_COLOR};

    private static SparseArray linearGradients = new SparseArray() {{
        for (Orientation orientation : shadowOrientations) {
            put(orientation.ordinal(), new GradientDrawable(orientation, colors));
        }
    }};

    private static GradientDrawable radialGradient = new GradientDrawable() {{
        setGradientType(RADIAL_GRADIENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setColors(colors);
        } else {
            setColor(END_COLOR);
        }
        setGradientRadius(SHADOW_LENGTH);
    }};


    static public void bindShadow(Canvas canvas, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        int height = view.getHeight();
        int width = view.getWidth();

        Rect bottomBounds = new Rect(SHADOW_LENGTH, height, width, height + SHADOW_LENGTH);
        drawShadow(canvas, TOP_BOTTOM, bottomBounds);

        Rect rightBounds = new Rect(width, SHADOW_LENGTH, width + SHADOW_LENGTH, height);
        drawShadow(canvas, LEFT_RIGHT, rightBounds);

        Rect cornerBLBounds = new Rect(0, height, SHADOW_LENGTH, height + SHADOW_LENGTH);
        radialGradient.setBounds(cornerBLBounds);
        radialGradient.setGradientCenter(1, 0);
        radialGradient.draw(canvas);

        Rect cornerBRBounds = new Rect(width, height, width + SHADOW_LENGTH,
                height + SHADOW_LENGTH);
        radialGradient.setBounds(cornerBRBounds);
        radialGradient.setGradientCenter(0, 0);
        radialGradient.draw(canvas);

        Rect cornerTRBounds = new Rect(width, 0, width + SHADOW_LENGTH, SHADOW_LENGTH);
        radialGradient.setBounds(cornerTRBounds);
        radialGradient.setGradientCenter(0, 1);
        radialGradient.draw(canvas);
    }

    static public void bindAboveShadow(Canvas canvas, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        int height = 0;
        int width = view.getWidth();

        Rect bottomBounds = new Rect(SHADOW_LENGTH, height, width, height + SHADOW_LENGTH);
        drawShadow(canvas, TOP_BOTTOM, bottomBounds);

        Rect rightBounds = new Rect(width, SHADOW_LENGTH, width + SHADOW_LENGTH, height);
        drawShadow(canvas, LEFT_RIGHT, rightBounds);

        Rect cornerBLBounds = new Rect(0, height, SHADOW_LENGTH, height + SHADOW_LENGTH);
        radialGradient.setBounds(cornerBLBounds);
        radialGradient.setGradientCenter(1, 0);
        radialGradient.draw(canvas);

        Rect cornerBRBounds = new Rect(width, height, width + SHADOW_LENGTH,
                height + SHADOW_LENGTH);
        radialGradient.setBounds(cornerBRBounds);
        radialGradient.setGradientCenter(0, 0);
        radialGradient.draw(canvas);

        Rect cornerTRBounds = new Rect(width, 0, width + SHADOW_LENGTH, SHADOW_LENGTH);
        radialGradient.setBounds(cornerTRBounds);
        radialGradient.setGradientCenter(0, 1);
        radialGradient.draw(canvas);
    }

    static private void drawShadow(Canvas canvas, Orientation orientation,
                                   Rect bounds) {
        GradientDrawable linearGradient = (GradientDrawable) linearGradients
                .get(orientation.ordinal());
        linearGradient.setBounds(bounds);
        linearGradient.draw(canvas);
    }
}