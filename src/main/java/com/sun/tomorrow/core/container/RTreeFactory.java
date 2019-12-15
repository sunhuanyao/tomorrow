package com.sun.tomorrow.core.container;

import com.sun.tomorrow.core.base.Leaf;
import com.sun.tomorrow.core.base.Point;
import com.sun.tomorrow.core.base.RTreeNode;
import com.sun.tomorrow.core.base.Rectangle;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author roger sun
 * @Date 2019/12/15 20:00
 */
public class RTreeFactory {

    private Map<Integer, RTreeNode> RTreeNodeMap = new HashMap<>();

    public int maxRTreeNodeEntries;
    public int minRTreeNodeEntries;

    private byte[] entryStatus = null;
    private byte[] initialEntryStatus = null;

    private LinkedList<Integer> parents = new LinkedList<>();
    private LinkedList<Integer> parentsEntry = new LinkedList<Integer>();
    private final static int DEFAULT_MAX_RTreeNode_ENTRIES = 5;
    private final static int DEFAULT_MIN_RTreeNode_ENTRIES = 0;

    private Stack<Integer> deletedRTreeNodeIds = new Stack<Integer>();

    //用来标记节点分割期间，entries的状态
    private final static int ENTRY_STATUS_ASSIGNED = 0;
    private final static int ENTRY_STATUS_UNASSIGNED = 1;


    private int treeHeight = 1;
    private int rootRTreeNodeId = 0;

    private int highestUsedRTreeNodeId = rootRTreeNodeId;
    private int size = 0;

    public RTreeFactory() {
        return;
    }

    public void init() {
        maxRTreeNodeEntries = DEFAULT_MAX_RTreeNode_ENTRIES;
        minRTreeNodeEntries = maxRTreeNodeEntries / 2;
//        minRTreeNodeEntries = 1;
        entryStatus = new byte[maxRTreeNodeEntries];
        initialEntryStatus = new byte[maxRTreeNodeEntries];

        for (int i = 0; i < maxRTreeNodeEntries; i++) {
            initialEntryStatus[i] = ENTRY_STATUS_UNASSIGNED;
        }

        RTreeNode root = new RTreeNode(rootRTreeNodeId, 1, maxRTreeNodeEntries);
        RTreeNodeMap.put(rootRTreeNodeId, root);

    }

    public RTreeNode getRTreeNode(int id) {
        return RTreeNodeMap.get(id);
    }

    public int getRootRTreeNodeId() {
        return rootRTreeNodeId;
    }

    public void add(Rectangle r, int id) {

        add(r.minX, r.minY, r.maxX, r.maxY, id, 1);

        size++;


    }

    private RTreeNode chooseRTreeNode(double minX, double minY, double maxX, double maxY, int level) {
        RTreeNode n = getRTreeNode(rootRTreeNodeId);

        parents.clear();
        parentsEntry.clear();

        while (true) {
            if (n.islevel(level)) {
                return n;
            }

            double leastEnlargement = Rectangle.enlargement(
                    n.entriesMinX[0],
                    n.entriesMinY[0],
                    n.entriesMaxX[0],
                    n.entriesMaxY[0],
                    minX, minY, maxX, maxY
            );

            int index = 0;
            for (int i = 1; i < n.entryCount; i++) {
                double tempMinX = n.entriesMinX[i];
                double tempMinY = n.entriesMinY[i];
                double tempMaxX = n.entriesMaxX[i];
                double tempMaxY = n.entriesMaxY[i];
                double tempEnlargement = Rectangle.enlargement(
                        tempMinX, tempMinY, tempMaxX, tempMaxY,
                        minX, minY, maxX, maxY
                );

                if ((tempEnlargement < leastEnlargement) ||
                        (tempEnlargement == leastEnlargement) &&
                                (Rectangle.area(tempMinX, tempMinY, tempMaxX, tempMaxY) <
                                        Rectangle.area(n.entriesMinX[index], n.entriesMinY[index], n.entriesMaxX[index], n.entriesMaxY[index]))
                ) {
                    index = i;
                    leastEnlargement = tempEnlargement;
                }
            }
            parents.push(n.nodeId);
            parentsEntry.push(index);

            n = getRTreeNode(n.ids[index]);
        }

    }

    private int getNextRTreeNodeId() {
        int nextRTreeNodeId = 0;
        if (deletedRTreeNodeIds.size() > 0) {
            nextRTreeNodeId = deletedRTreeNodeIds.pop();

        } else {
            nextRTreeNodeId = 1 + highestUsedRTreeNodeId++;
        }
        return nextRTreeNodeId;
    }

    //找到 X, Y差距最大的两个 矩形。。分别组成新的节点。
    private void pickSeeds(RTreeNode n, double newRectMinX, double newRectMinY, double newRectMaxX, double newRectMaxY, int newId, RTreeNode newRTreeNode) {
        double maxNormalizedSeparation = -1;
        int highestLowIndex = -1;
        int lowestHighIndex = -1;

        //更新n节点的矩形信息

        if (newRectMinX < n.mbrMinX) n.mbrMinX = newRectMinX;
        if (newRectMinY < n.mbrMinY) n.mbrMinY = newRectMinY;
        if (newRectMaxX > n.mbrMaxX) n.mbrMaxX = newRectMaxX;
        if (newRectMaxY > n.mbrMaxY) n.mbrMaxY = newRectMaxY;


        //记录n节点的矩形长度和宽度
        double mbrLenX = n.mbrMaxX - n.mbrMinX;
        double mbrLenY = n.mbrMaxY - n.mbrMinY;

        double tempHighestLow = newRectMinX;
        int tempHighestLowIndex = -1;

        double tempLowestHigh = newRectMaxX;
        int tempLowestHighIndex = -1;


        for (int i = 0; i < n.entryCount; i++) {
            double tempLow = n.entriesMinX[i];
            double tempHigh = n.entriesMaxX[i];
            if (tempLow >= tempHighestLow) {
                tempHighestLow = tempLow;
                tempHighestLowIndex = i;
            } else {
                if (tempHigh <= tempLowestHigh) {
                    tempLowestHigh = tempHigh;
                    tempLowestHighIndex = i;
                }
            }
            double normalizedSeparation = mbrLenX == 0 ? 1 : (tempHighestLow - tempLowestHigh) / mbrLenX;


            if (normalizedSeparation >= maxNormalizedSeparation) {
                highestLowIndex = tempHighestLowIndex;
                lowestHighIndex = tempLowestHighIndex;
                maxNormalizedSeparation = normalizedSeparation;
            }
        }

        tempHighestLow = newRectMinY;
        tempHighestLowIndex = -1;

        tempLowestHigh = newRectMaxY;
        tempLowestHighIndex = -1;

        for (int i = 0; i < n.entryCount; i++) {
            double tempLow = n.entriesMinY[i];
            if (tempLow >= tempHighestLow) {
                tempHighestLow = tempLow;
                tempHighestLowIndex = i;
            } else {
                double tempHigh = n.entriesMaxY[i];
                if (tempHigh <= tempLowestHigh) {
                    tempLowestHigh = tempHigh;
                    tempLowestHighIndex = i;
                }
            }
            double normalizedSeparation = mbrLenY == 0 ? 1 : (tempHighestLow - tempLowestHigh) / mbrLenY;
            if (normalizedSeparation >= maxNormalizedSeparation) {
                highestLowIndex = tempHighestLowIndex;
                lowestHighIndex = tempLowestHighIndex;
                maxNormalizedSeparation = normalizedSeparation;
            }
        }

        if (highestLowIndex == lowestHighIndex) {
            highestLowIndex = -1;
            double tempMinY = newRectMinY;

            lowestHighIndex = 0;
            double tempMaxX = n.entriesMaxX[0];

            for (int i = 1; i < n.entryCount; i++) {
                if (n.entriesMinY[i] < tempMinY) {
                    tempMinY = n.entriesMinY[i];
                    highestLowIndex = i;
                } else if (n.entriesMaxX[i] > tempMaxX) {
                    tempMaxX = n.entriesMaxX[i];
                    lowestHighIndex = i;
                }
            }
        }

        if (highestLowIndex == -1) {
            newRTreeNode.addEntry(newRectMinX, newRectMinY, newRectMaxX, newRectMaxY, newId);
        } else {
            newRTreeNode.addEntry(
                    n.entriesMinX[highestLowIndex], n.entriesMinY[highestLowIndex],
                    n.entriesMaxX[highestLowIndex], n.entriesMaxY[highestLowIndex],
                    n.ids[highestLowIndex]);
            n.ids[highestLowIndex] = -1;
            n.entriesMinX[highestLowIndex] = newRectMinX;
            n.entriesMinY[highestLowIndex] = newRectMinY;
            n.entriesMaxX[highestLowIndex] = newRectMaxX;
            n.entriesMaxY[highestLowIndex] = newRectMaxY;

            n.ids[highestLowIndex] = newId;
        }

        if (lowestHighIndex == -1) {
            lowestHighIndex = highestLowIndex;
        }

        entryStatus[lowestHighIndex] = ENTRY_STATUS_ASSIGNED;
        n.entryCount = 1;
        n.mbrMinX = n.entriesMinX[lowestHighIndex];
        n.mbrMinY = n.entriesMinY[lowestHighIndex];
        n.mbrMaxX = n.entriesMaxX[lowestHighIndex];
        n.mbrMaxY = n.entriesMaxY[lowestHighIndex];

        //
    }

    /**
     * 分裂节点，当增加一个子节点大于最大节点数目，则对节点进行分裂， 找到距离最远的两个矩阵节点，形成新的两个新节点
     * 然后根据最小扩展原则分别将剩余的节点加入到两个新节点中。
     * @param n
     * @param newRectMinX
     * @param newRectMinY
     * @param newRectMaxX
     * @param newRectMaxY
     * @param newId
     * @return
     */
    private RTreeNode splitRTreeNode(RTreeNode n, double newRectMinX, double newRectMinY, double newRectMaxX, double newRectMaxY, int newId) {


        double initialArea = 0;

        System.arraycopy(initialEntryStatus, 0, entryStatus, 0, maxRTreeNodeEntries);

        RTreeNode newRTreeNode = new RTreeNode(getNextRTreeNodeId(), n.level, maxRTreeNodeEntries);

        RTreeNodeMap.put(newRTreeNode.nodeId, newRTreeNode);

        pickSeeds(n, newRectMinX, newRectMinY, newRectMaxX, newRectMaxY, newId, newRTreeNode);

        while (n.entryCount + newRTreeNode.entryCount < maxRTreeNodeEntries + 1) {
            if (maxRTreeNodeEntries + 1 - newRTreeNode.entryCount == minRTreeNodeEntries) {
                for (int i = 0; i < maxRTreeNodeEntries; i++) {
                    if (entryStatus[i] == ENTRY_STATUS_UNASSIGNED) {
                        entryStatus[i] = ENTRY_STATUS_ASSIGNED;
                        if (n.entriesMinX[i] < n.mbrMinX) n.mbrMinX = n.entriesMinX[i];
                        if (n.entriesMinY[i] < n.mbrMinY) n.mbrMinY = n.entriesMinY[i];
                        if (n.entriesMaxX[i] > n.mbrMaxX) n.mbrMaxX = n.entriesMaxX[i];
                        if (n.entriesMaxY[i] > n.mbrMaxY) n.mbrMaxY = n.entriesMaxY[i];
                        n.entryCount++;
                    }
                }
                break;
            }
            if (maxRTreeNodeEntries + 1 - n.entryCount == minRTreeNodeEntries) {

                for (int i = 0; i < maxRTreeNodeEntries; i++) {
                    if (entryStatus[i] == ENTRY_STATUS_UNASSIGNED) {
                        entryStatus[i] = ENTRY_STATUS_ASSIGNED;
                        newRTreeNode.addEntry(n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i], n.ids[i]);
                        n.ids[i] = -1;
                    }
                }
                break;
            }
            pickNext(n, newRTreeNode);
        }
        n.reorganize(this);




        return newRTreeNode;
    }

    /**
     * 增加叶子矩阵信息
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     * @param id
     * @param level
     */

    private void add(double minX, double minY, double maxX, double maxY, int id, int level) {
        RTreeNode n = chooseRTreeNode(minX, minY, maxX, maxY, level);

        RTreeNode newLeaf = null;

        if(n.entryCount < maxRTreeNodeEntries){
            n.addEntry(minX, minY, maxX, maxY, id);
        } else{
            newLeaf = splitRTreeNode(n, minX, minY, maxX, maxY, id);
        }

        RTreeNode newRTreeNode = adjustTree(n, newLeaf);

        if(newRTreeNode != null){
            int oldRootRTreeNodeId = rootRTreeNodeId;
            RTreeNode oldRoot = getRTreeNode(oldRootRTreeNodeId);

            rootRTreeNodeId = getNextRTreeNodeId();
            treeHeight ++;
            RTreeNode root = new RTreeNode(rootRTreeNodeId, treeHeight, maxRTreeNodeEntries);
            root.addEntry(newRTreeNode.mbrMinX, newRTreeNode.mbrMinY, newRTreeNode.mbrMaxX, newRTreeNode.mbrMaxY, newRTreeNode.nodeId);
            root.addEntry(oldRoot.mbrMinX, oldRoot.mbrMinY, oldRoot.mbrMaxX, oldRoot.mbrMaxY, oldRoot.nodeId);
            RTreeNodeMap.put(rootRTreeNodeId, root);
        }


    }

    /**
     * 调整树结构，子节点的变化会影响父节点的属性，因此需要对父节点进行更新。
     * @param n
     * @param nn
     * @return
     */
    private RTreeNode adjustTree(RTreeNode n, RTreeNode nn){
        while(n.level != treeHeight){

            RTreeNode parent = getRTreeNode(parents.pop());

            int entry = parentsEntry.pop();

            if (parent.entriesMinX[entry] != n.mbrMinX ||
                    parent.entriesMinY[entry] != n.mbrMinY ||
                    parent.entriesMaxX[entry] != n.mbrMaxX ||
                    parent.entriesMaxY[entry] != n.mbrMaxY) {

                parent.entriesMinX[entry] = n.mbrMinX;
                parent.entriesMinY[entry] = n.mbrMinY;
                parent.entriesMaxX[entry] = n.mbrMaxX;
                parent.entriesMaxY[entry] = n.mbrMaxY;

                parent.recalculateMBR();
            }
            RTreeNode newRTreeNode = null;

            if (nn != null) {

                if(parent.entryCount < maxRTreeNodeEntries){
                    parent.addEntry(nn.mbrMinX, nn.mbrMinY, nn.mbrMaxX, nn.mbrMaxY, nn.nodeId);
                } else {
                    newRTreeNode = splitRTreeNode(parent, nn.mbrMinX, nn.mbrMinY, nn.mbrMaxX, nn.mbrMaxY, nn.nodeId);
                }
            }

            n = parent;

            nn = newRTreeNode;
            parent = null;
            newRTreeNode = null;

        }

        return nn;

    }

    /**
     * 分裂后，对未分配的节点选择一个更合适的新节点。
     * @param n
     * @param newRTreeNode
     * @return
     */
    public int pickNext(RTreeNode n, RTreeNode newRTreeNode){
        double maxDifference = Double.NEGATIVE_INFINITY;
        int next = 0;
        int nextGroup = 0;

        maxDifference = Double.NEGATIVE_INFINITY;

        for(int i = 0; i < maxRTreeNodeEntries;i ++){
            if(entryStatus[i] == ENTRY_STATUS_UNASSIGNED){
                double nIncrease = Rectangle.enlargement(n.mbrMinX, n.mbrMinY, n.mbrMaxX, n.mbrMaxY,
                        n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i]);

                double newRTreeNodeIncrease = Rectangle.enlargement(newRTreeNode.mbrMinX, newRTreeNode.mbrMinY, newRTreeNode.mbrMaxX, newRTreeNode.mbrMaxY,
                        n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i]);

                double difference = Math.abs(nIncrease - newRTreeNodeIncrease);

                if(difference > maxDifference){
                    next = i;

                    if(nIncrease < newRTreeNodeIncrease){
                        nextGroup = 0;
                    } else if(newRTreeNodeIncrease < nIncrease){
                        nextGroup = 1;
                    } else if(Rectangle.area(n.mbrMinX, n.mbrMinY, n.mbrMaxX, n.mbrMaxY) < Rectangle.area(newRTreeNode.mbrMinX, newRTreeNode.mbrMinY, newRTreeNode.mbrMaxX, newRTreeNode.mbrMaxY)){
                        nextGroup = 0;
                    } else if(Rectangle.area(newRTreeNode.mbrMinX, newRTreeNode.mbrMinY, newRTreeNode.mbrMaxX, newRTreeNode.mbrMaxY) < Rectangle.area(n.mbrMinX, n.mbrMinY, n.mbrMaxX, n.mbrMaxY)){
                        nextGroup = 1;
                    } else if (newRTreeNode.entryCount < maxRTreeNodeEntries / 2) {
                        nextGroup = 0;
                    } else{
                        nextGroup = 1;
                    }

                    maxDifference = difference;

                }
            }
        }
        entryStatus[next] = ENTRY_STATUS_ASSIGNED;

        if(nextGroup == 0){
            if (n.entriesMaxX[next] < n.mbrMinX) n.mbrMinX = n.entriesMinX[next];
            if (n.entriesMinY[next] < n.mbrMinY) n.mbrMinY = n.entriesMinY[next];
            if (n.entriesMaxX[next] > n.mbrMaxX) n.mbrMaxX = n.entriesMaxX[next];
            if (n.entriesMaxY[next] > n.mbrMaxY) n.mbrMaxY = n.entriesMaxY[next];
            n.entryCount ++;
        }else{
            newRTreeNode.addEntry(n.entriesMinX[next], n.entriesMinY[next], n.entriesMaxX[next], n.entriesMaxY[next], n.ids[next]);
            n.ids[next] = -1;
        }
        return next;
    }

    /**
     *  bfs(宽度优先搜索) 返回 查询包含该坐标的矩阵
     * @param p
     * @param level
     * @return
     */
    public List<RTreeNode> queryRelation(Point p, int level){

        RTreeNode n = getRTreeNode(rootRTreeNodeId);
        RTreeNode tmp = null;
//    RTreeNode new_tp = null;

        List<RTreeNode> result = new ArrayList<RTreeNode>();

        Queue<RTreeNode> queue = new ArrayBlockingQueue<RTreeNode>(DEFAULT_MAX_RTreeNode_ENTRIES * 100 );

        queue.add(n);

        while(true){

            tmp = queue.poll();

            if(tmp == null) break;

            if(tmp.level == level){
//        return
                result.add(tmp);
                continue;
            }

            int index = 0;


            for(int i = 0;i < tmp.entryCount; i++){
                double tempMinX = tmp.entriesMinX[i];
                double tempMinY = tmp.entriesMinY[i];
                double tempMaxX = tmp.entriesMaxX[i];
                double tempMaxY = tmp.entriesMaxY[i];
                if(containsPoint(tempMaxX, tempMaxY, tempMinX, tempMinY, p)){
                    queue.add(getRTreeNode(tmp.ids[i]));
                }
            }

        }

//        for(RTreeNode rn: result){
//            if(containsPoint(rn.mbrMinX, rn.mbrMinY, rn.mbrMaxX, rn.mbrMaxY, p)){
//                return rn;
//            }
//        }
        return result;
    }

    public List<Leaf> queryLevel(Point p){

        List<RTreeNode> result = queryRelation(p, 1);

        List<Leaf> resLeaf = new ArrayList<Leaf>();
        for(RTreeNode rn: result){
            for(int i = 0; i < rn.entryCount; i++) {
                if (containsPoint(rn.entriesMaxX[i], rn.entriesMaxY[i], rn.entriesMinX[i], rn.entriesMinY[i], p)) {
//                    return new Leaf(rn.entriesMinX[i], rn.entriesMinY[i], rn.entriesMaxX[i], rn.entriesMaxY[i]);
                    resLeaf.add(new Leaf(rn.entriesMinX[i], rn.entriesMinY[i], rn.entriesMaxX[i], rn.entriesMaxY[i]));

                }
            }
        }
        return resLeaf;
    }

    public boolean containsPoint(double maxX, double maxY, double minX, double minY, Point p){

        if(maxX >= p.x && maxY >= p.y && minX <= p.x && minY <= p.y){
            return true;
        }
        return false;

    }



}
