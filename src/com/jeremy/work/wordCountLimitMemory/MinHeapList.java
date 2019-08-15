package com.jeremy.work.wordCountLimitMemory;

import com.jeremy.work.wordCountLimitMemory.domain.WordCount;
import java.util.*;

//基于list的最小堆
public class MinHeapList {
    /**
     * 构建堆原理：
     * 父节点坐标：index
     * 左孩子节点：index*2
     * 右孩子节点：index*2+1
     * 若孩子节点比父节点的值小，则进行值交换
     *
     * @param wordCountList 保存单词和频次
     * @param len           topN的值，即维护最小堆的节点数
     */
    public static void heapBuild(List<WordCount> wordCountList, int len) {
        for (int i = 0; i < len / 2; i++) {
            Fix(wordCountList, i, len);
        }
    }

    //修正最小堆
    public static void Fix(List<WordCount> wordCountList, int i, int len) {
        int left = i * 2;
        int right = i * 2 + 1;
        int min = i;
        if (left < len && wordCountList.get(left).getCount() < wordCountList.get(i).getCount()) {
            min = left;
        }
        if (right < len && wordCountList.get(right).getCount() < wordCountList.get(min).getCount()) {
            min = right;
        }
        if(i != min){
            Collections.swap(wordCountList,min,i);//交换ArrayList中两元素位置
            Fix(wordCountList,min,len);
        }
    }

}
