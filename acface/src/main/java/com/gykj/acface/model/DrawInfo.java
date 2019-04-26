package com.gykj.acface.model;

import android.graphics.Rect;

public class DrawInfo {
    private Rect rect;

    public DrawInfo(Rect rect) {
        this.rect = rect;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

}
