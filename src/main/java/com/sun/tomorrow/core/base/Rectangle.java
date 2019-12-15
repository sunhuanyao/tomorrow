package com.sun.tomorrow.core.base;

/**
 * @Author roger sun
 * @Date 2019/12/15 20:26
 */
public class Rectangle {
    public double minX, minY, maxX, maxY;

    public Rectangle(double MinX, double MinY, double MaxX, double MaxY){
        this.minX = MinX;
        this.minY = MinY;
        this.maxX = MaxX;
        this.maxY = MaxY;
    }

    public static double enlargement(double r1MinX, double r1MinY, double r1MaxX, double r1MaxY,
                                     double r2MinX, double r2MinY, double r2MaxX, double r2MaxY){
        double r1Area = (r1MaxX - r1MinX) * (r1MaxY - r1MinY);

        if(r1Area == Double.POSITIVE_INFINITY){
            return 0;
        }

        if (r2MinX < r1MinX) r1MinX = r2MinX;
        if (r2MinY < r1MinY) r1MinY = r2MinY;
        if (r2MaxX > r1MaxX) r1MaxX = r2MaxX;
        if (r2MaxY > r1MaxY) r1MaxY = r2MaxY;

        double r1r2UnionArea = (r1MaxX - r1MinX) * (r1MaxY - r1MinY);

        if(r1r2UnionArea == Double.POSITIVE_INFINITY){
            return Double.POSITIVE_INFINITY;
        }

        return r1r2UnionArea - r1Area;

    }

    public static double area(double MinX, double MinY, double MaxX, double MaxY){
        return (MaxX - MinX) * (MaxY - MinY);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + String.valueOf(minX).hashCode();
        result = prime * result + String.valueOf(minY).hashCode();
        result = prime * result + String.valueOf(maxX).hashCode();
        result = prime * result + String.valueOf(maxY).hashCode();
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Rectangle other = (Rectangle) obj;
        if(other.minX == minX && other.minY == minY && other.maxX == maxX && other.maxY == maxY){
            return true;
        }
        return false;
    }
}
