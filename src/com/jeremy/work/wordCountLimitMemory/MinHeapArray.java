package com.jeremy.work.wordCountLimitMemory;

import java.util.Arrays;
//基于数组的最小堆
public class MinHeapArray {
    //测试
    public static void main(String[] args)  {
        int[] arr={62,5,81,63,13,43,34,5,8,9,6,44};
        int k=6;

        int[] newArr = Arrays.copyOfRange(arr, 0, k);

        heapBuild(newArr,k);  //建树

        System.out.println(Arrays.toString(newArr));

        for (int i = k; i < arr.length; i++) {
            if(arr[i]>newArr[0]){
                newArr[0] = arr[i];
                Fix(newArr,0,newArr.length);
            }
        }

        System.out.println(Arrays.toString(newArr));

    }

    /**
     *
     * 父节点坐标：index
     * 左孩子节点：index*2
     * 右孩子节点：index*2+1
     * 若孩子节点比父节点的值小，则进行值交换
     *
     * @param arr   单词频率的数组
     * @param len   topN的值，即维护最小堆的节点数
     */
    public static void heapBuild(int[] arr,int len) {
        for(int i=len/2;i>=0;i--){//从后遍历孩子节点大于等于一的节点
            Fix(arr,i,len);     //修正该子树
        }
    }

    /**
     * 修正最小堆
     * @param arr
     * @param i
     * @param len
     */
    public static void Fix(int[] arr, int i,int len) {
        int left=i*2;
        int right=i*2+1;
        int min=i;
        if (left < len && arr[left] < arr[i]) {
            min = left;
        }
        if (right < len && arr[right] < arr[min]) {
            min = right;
        }
        if(i!=min){
            swap(arr,min,i);
            Fix(arr,min,len);
        }
    }

    //交换赋值
    private static void swap(int[] arr, int a, int b) {
        int temp=arr[a];
        arr[a]=arr[b];
        arr[b]=temp;
    }

}
