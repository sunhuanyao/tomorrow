package com.sun.tomorrow.core.base;

import com.sun.tomorrow.core.container.RTreeFactory;

/**
 * @Author roger sun
 * @Date 2019/12/15 19:58
 */
public class RTreeNode {

    public int nodeId = 0;
    public double mbrMinX = Double.MAX_VALUE;
    public double mbrMinY = Double.MAX_VALUE;

    public double mbrMaxX = Double.MIN_VALUE;
    public double mbrMaxY = Double.MIN_VALUE;

    public double[] entriesMinX = null;
    public double[] entriesMinY = null;

    public double[] entriesMaxX = null;
    public double[] entriesMaxY = null;

    public int[] ids = null;
    public int level;
    public int entryCount;

    public RTreeNode(int nodeId, int level, int maxNodeEntries) {
        this.nodeId = nodeId;
        this.level = level;
        entriesMinX = new double[maxNodeEntries];
        entriesMinY = new double[maxNodeEntries];
        entriesMaxX = new double[maxNodeEntries];
        entriesMaxY = new double[maxNodeEntries];
        ids = new int[maxNodeEntries];
    }

    public void addEntry(double minX, double minY, double maxX, double maxY, int id) {
        ids[entryCount] = id;
        entriesMinX[entryCount] = minX;
        entriesMinY[entryCount] = minY;
        entriesMaxX[entryCount] = maxX;
        entriesMaxY[entryCount] = maxY;

        if (minX < mbrMinX) mbrMinX = minX;
        if (minY < mbrMinY) mbrMinY = minY;
        if (maxX > mbrMaxX) mbrMaxX = maxX;
        if (maxY > mbrMaxY) mbrMaxY = maxY;

        entryCount++;
    }
    //重组，去掉那些为空的节点
    public void reorganize(RTreeFactory rTree){
        int countdownIndex = rTree.maxRTreeNodeEntries - 1;
        for(int index = 0; index < entryCount; index ++){
            if(ids[index] == -1){
                while(ids[countdownIndex] == -1 && countdownIndex > index){
                    countdownIndex -- ;
                }
                entriesMinX[index] = entriesMinX[countdownIndex];
                entriesMinY[index] = entriesMinY[countdownIndex];
                entriesMaxX[index] = entriesMaxX[countdownIndex];
                entriesMaxY[index] = entriesMaxY[countdownIndex];
                ids[index] = ids[countdownIndex];
                ids[countdownIndex] = -1;
            }
        }
    }

    public void recalculateMBR(){
        mbrMinX = entriesMinX[0];
        mbrMinY = entriesMinY[0];
        mbrMaxX = entriesMaxX[0];
        mbrMaxY = entriesMaxY[0];

        for(int i = 1;i < entryCount; i++){
            if (entriesMinX[i] < mbrMinX) mbrMinX = entriesMinX[i];
            if (entriesMinY[i] < mbrMinY) mbrMinY = entriesMinY[i];
            if (entriesMaxX[i] > mbrMaxX) mbrMaxX = entriesMaxX[i];
            if (entriesMaxY[i] > mbrMaxY) mbrMaxY = entriesMaxY[i];

        }
    }



    public boolean isleaf(){return (level == 1);}

    public boolean islevel(int level){return (this.level == level);}

    public void checkinfo(){

        System.out.println("mbr-> minx:" + mbrMinX + " miny:" + mbrMinY + " maxx:" + mbrMaxX + " maxy:" + mbrMaxY);

        System.out.println("entryInfo:");
        for(int i=0;i<entryCount;i++){
            System.out.println(entriesMinX[i] +
                    " " + entriesMinY[i] +
                    " " + entriesMaxX[i] +
                    " " + entriesMaxY[i]
            );
        }

        System.out.println("----------------------------end");

    }
}
