package com.sun.tomorrow.core.base;

/**
 * @Author roger sun
 * @Date 2019/12/15 20:30
 */
public class Point {

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
