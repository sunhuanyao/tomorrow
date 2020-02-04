package com.sun.tomorrow.core.util;

import com.sun.tomorrow.core.base.Point;
import com.sun.tomorrow.core.domain.RegionInfo;

public class PolygonUtil {



    /**
     * 判断点是否在多边形内
     * @param point 检测点
     * @param pts   多边形的顶点
     * @return      点在多边形内返回true,否则返回false
     */
    public static boolean IsPtInPoly(Point point, Point[] pts){

        int N = pts.length;
        boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0;//cross points count of x
        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
        Point p1, p2;//neighbour bound vertices
        Point p = point; //当前点

        p1 = pts[0];//left vertex
        for(int i = 1; i <= N; ++i){//check all rays
            if(p.equals(p1)){
                return boundOrVertex;//p is an vertex
            }

            p2 = pts[i % N];//right vertex
            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests
                p1 = p2;
                continue;//next ray left point
            }

            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//横坐标 内  ray is crossing over by the algorithm (common part of)
                if(p.y <= Math.max(p1.y, p2.y)){//y下  x is before of ray
                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray  垂线
                        return boundOrVertex;
                    }

                    if(p1.y == p2.y){//水平线 ray is vertical
                        if(p1.y == p.y){//水平线内 overlies on a vertical ray
                            return boundOrVertex;
                        }else{//before ray
                            ++intersectCount;  //交点在上方
                        }
                    }else{//cross point on the left side
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//两点式化简，交点y坐标 cross point of y
                        if(Math.abs(p.y - xinters) < precision){//== 0  在线上  overlies on a ray
                            return boundOrVertex;
                        }

                        if(p.y < xinters){//before ray
                            ++intersectCount;  //交点在上方
                        }
                    }
                }
            }else{//special case when ray is crossing through the vertex
                if(p.x == p2.x && p.y <= p2.y){//p crossing over p2
                    Point p3 = pts[(i+1) % N]; //next vertex
                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
                        ++intersectCount;
                    }else{
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;//next ray left point
        }

        if(intersectCount % 2 == 0){//偶数在多边形外
            return false;
        } else { //奇数在多边形内
            return true;
        }

    }


}
