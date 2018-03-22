package com.wz.tnews;

/**
 * Created by v_wangzhan on 2017/10/25.
 */

public class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        return this.x == ((Point) obj).x && this.y == ((Point) obj).y;
    }
}
