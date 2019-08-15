package com.jeremy.work.wordCountLimitMemory;

import com.jeremy.work.wordCountLimitMemory.domain.WordCount;

import java.io.*;
import java.util.*;

/**
 * 有一个1G大小的一个文件，里面每一行是一个词，词的大小不超过16字节，内存限制大小是1M。返回频数最高的50个词.
 */

/**
 * 为了方便运行，将题目调整为：
 * 有一个1M大小的一个文件，里面每一行是一个词，词的大小不超过16字节，内存限制大小是100KB。返回频数最高的50个词.
 */
public class WordCountMain {
    public static void main(String[] args) throws IOException {
        //0.生成每行一个单词的文件
        //FileUtils.generateWordTxt("E:\\wordCount\\The Poems of Goethe.txt","E:\\wordCount\\wordFile.txt");

        //1.根据实际内存大小进行文件分片
        HashSet<File> splitFile = FileUtils.getSplitFile(new File("E:\\wordCount\\wordFile.txt"), 100L);

        System.out.println("-----------------切分文件完成--------------------");

        //2.通过hashmap统计每个文件中出现的词，以及相应的频率

        //3.维护一个含有50个结点的最小堆，并一次将文件中的词频存入堆；
        //如果第一个文件中词的数目小于50，那么可以继续遍历第二个文件，直到构建好有50个节点的小顶堆为止
        //如果遍历到的词的出现次数大于堆顶上词的出现次数，那么可以用新遍历到的词替换堆顶的词，然后重新调整这个堆为小顶堆

        int topN = 50;//返回频数最高的50个词
        int i = 1;//统计循环次数

        List<WordCount> wordCountList = new ArrayList<>(topN);//最小堆

        for (File file : splitFile) {
            //读取文件中的单词，通过HashMap计算词频
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));
            Map<String, Integer> wordCountHashMap = new HashMap<>();
            String line;

            int num = 1;
            while ((line = br.readLine()) != null) {
                int count = 1;//频次的初始值
                if (wordCountHashMap.containsKey(line)) {
                    count = wordCountHashMap.get(line) + 1;
                }
                //System.out.println(num++ + " "+line + " "+count);
                wordCountHashMap.put(line, count);
            }

            //System.out.println(wordCountHashMap.toString());
            br.close();

            //将hashMap中的结果存入最小堆
            for (Map.Entry<String, Integer> entry : wordCountHashMap.entrySet()) {
                if (i <= topN) {
                    wordCountList.add(new WordCount(entry.getKey(), entry.getValue()));
                    if (i == topN)//等于最小堆维护节点数时，建树
                        MinHeapList.heapBuild(wordCountList, topN);
                    i++;
                } else {
                    if (entry.getValue() > wordCountList.get(0).getCount()) {//entry中的频次比堆顶频次大
                        wordCountList.set(0, new WordCount(entry.getKey(), entry.getValue()));
                        MinHeapList.Fix(wordCountList, 0, topN);
                    }
                }
            }
        }
        Collections.sort(wordCountList);
        System.out.println(wordCountList.toString());
    }
}
