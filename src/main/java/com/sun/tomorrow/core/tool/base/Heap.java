package com.sun.tomorrow.core.tool.base;

import com.sun.tomorrow.core.util.SafeGson;
import org.omg.CORBA.OBJ_ADAPTER;

import java.util.ArrayList;
import java.util.List;

/**
 * 定长堆 --
 * @param <T>
 */
public class Heap<T extends BaseEntity> implements TArray<T> {

    private static int defaultSize = 100;

    private final int capity;

    public Object[] array;

    // true : 最大堆   false : 最小堆
    private final boolean isMax;

    private int nowIndex;

    public Heap(){
        this(defaultSize, false);
        this.nowIndex = 0;
    }

    public Heap(int size, boolean isMax){
        this.capity = size;
        this.isMax = isMax;
        this.array = new Object[this.capity];
    }

    public Heap(int size){
        this(size, false);
    }


    @SuppressWarnings("unchecked")
    public void add(T tmp){

        if(this.nowIndex >= this.capity) throw new ArrayIndexOutOfBoundsException();
//        T[] arrayEntity = (T[]) array;

        int tmpIndex = this.nowIndex;
        this.array[this.nowIndex] = tmp;
        T nowTmp = tmp;
        while(tmpIndex > 0){
            int paIndex = (tmpIndex - 1) / 2;
            //如果最大堆
                // 是右子树
            if(tmpIndex % 2 == 0){
                // 左子树大于当前节点
                if(compare(tmpIndex - 1, tmpIndex)){
//                if(((BaseEntity)this.array[tmpIndex - 1]).compareTo((BaseEntity)this.array[tmpIndex]) > 0){
                    this.array[tmpIndex] = nowTmp;
                    tmpIndex -= 1;
                    nowTmp = (T)this.array[tmpIndex];
                }
            }else{
                // 是左子树
                // 右子树大于当前节点
                if(tmpIndex + 1 < this.capity &&this.array[tmpIndex + 1] != null && compare(tmpIndex + 1, tmpIndex)){
                    this.array[tmpIndex] = nowTmp;
                    tmpIndex += 1;
                    nowTmp = (T) this.array[tmpIndex];
                }
            }

            // 当前节点 > 其父亲节点 ， 则
            if (compare(tmpIndex, paIndex)) {
                this.array[tmpIndex] = this.array[paIndex];
            }else{
                break;
            }
            tmpIndex = paIndex;
        }
        this.array[tmpIndex] = nowTmp;
//        System.out.println(this.toString());

        this.nowIndex ++;

    }

    @SuppressWarnings("unchecked")
    public T getFirst(){
        if(this.nowIndex == 0) return null;
        else {
            return (T)this.array[0];
        }
    }

    @SuppressWarnings("unchecked")
    public T pop(){
        T ret = (T)this.array[0];
        this.array[0] = this.array[this.nowIndex - 1];
        this.array[this.nowIndex - 1] = null;
        this.nowIndex -- ;

        recoverDown();
        return ret;


    }

    public void remove(){

    }

    public int judge(){
        return this.isMax? -1:1;
    }

    //true 在上面  false 在下面
    private boolean compare(int fromIndex, int toIndex){
        return ((BaseEntity)this.array[fromIndex]).compareTo((BaseEntity)this.array[toIndex]) * judge() < 0;
    }

    private boolean compareWithVal(T now, int index){
        return now.compareTo((BaseEntity)this.array[index]) * judge() < 0;
    }

    @SuppressWarnings("unchecked")
    private void recoverDown(){
        int nowIdx = 0;
        T nowTmp = (T)this.array[nowIdx];
        while(nowIdx < this.nowIndex){
            int leftIdx = (nowIdx << 1) + 1;
            int rightIdx = leftIdx + 1;
            if(leftIdx >= this.nowIndex) break;

            if(rightIdx >= this.nowIndex || compare(leftIdx, rightIdx)){
                if(!compareWithVal(nowTmp, leftIdx)){
//                    nowTmp = (T)this.array[nowIdx];
                    this.array[nowIdx] = this.array[leftIdx];
                    nowIdx = leftIdx;
                }else{
                    break;
                }
            }else{
                if(!compareWithVal(nowTmp, rightIdx)){
//                    nowTmp = (T)this.array[nowIdx];
                    this.array[nowIdx] = this.array[rightIdx];
                    nowIdx = rightIdx;
                }else{
                    break;
                }
            }
        }
        this.array[nowIdx] = nowTmp;
        System.out.println(this.toString());
    }


    public String toString(){
        return SafeGson.toJson(this.array) + "\t" + "index:" + nowIndex;
    }



}
