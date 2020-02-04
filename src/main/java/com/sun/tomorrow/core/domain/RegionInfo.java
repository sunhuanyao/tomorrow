package com.sun.tomorrow.core.domain;

import com.sun.tomorrow.core.base.Point;
import com.sun.tomorrow.core.base.Rectangle;

public class RegionInfo extends Rectangle{

    private String name;

    private Point[] points;

    public RegionInfo(String name, Point[] points) {
        super(points);
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }


}
