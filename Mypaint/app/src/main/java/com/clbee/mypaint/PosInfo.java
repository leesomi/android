package com.clbee.mypaint;

import android.graphics.Paint;

/**
 * Created by somi on 2018. 1. 7..
 */

public class PosInfo {
    private float x, y, r;
    private Paint paint;

    PosInfo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}
