package com.wz.tnews;


/**
 * Created by v_wangzhan on 2017/10/25.
 */

public class ColorPoint extends Point {
    int color;

    public ColorPoint(int x, int y) {
        super(x, y);
    }

    public ColorPoint(int x, int y, int color) {
        super(x, y);
        this.color = color;
    }



}
