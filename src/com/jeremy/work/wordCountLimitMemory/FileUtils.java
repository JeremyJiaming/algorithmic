package com.jeremy.work.wordCountLimitMemory;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;


public class FileUtils {
    /**
     * 分割文件
     * 文件需满足：每一行是一个词
     *
     * @param pFile 文件全路径
     * @param size  实际内存大小（单位：KB）
     * @return 分割后子文件集合
     * @throws IOException
     */
    public static HashSet<File> getSplitFile(File pFile, long size) throws IOException {
        //根据实际内存大小，返回文件分割分数
        int childCount = autoGetCountSplit(pFile, size);
        //childFileSet存放所有子文件
        HashSet<File> childFileSet = new HashSet();

        //文件中每行存一个单词，∴采用BufferedReader 一次读一行
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(pFile)));

        String line;
        String childFileName;
        File childFile;
        PrintWriter pw = null;
        while ((line = br.readLine()) != null) {
            //参照hashMap分桶原理，
            //为防止hashCode值为负，与pCount做&运算
            //pCount需为 2^n
            int bucket = (line.hashCode() & (childCount - 1)) % childCount;//文件分桶
            //子文件命名规则
            String[] pFileNames = pFile.getName().split("\\.");
            childFileName = pFile.getParent() + File.separator +
                    pFileNames[0] + "_" + bucket + "." + pFileNames[1];//保证子文件夹目录与待分割文件相同
            //System.out.println(childFileName);

            childFile = new File(childFileName);
            //子文件不存在，则创建
            if (!childFile.exists()) {
                childFile.createNewFile();
                childFileSet.add(childFile);//添加进childFileSet
            }
            //子文件大小超过实际内存大小，则新建
//            if (childFile.length() >= (size-1) * 1024) {
//                pFileNames = childFile.getName().split("\\.");
//                childFileName = childFile.getParent() + File.separator +
//                        pFileNames[0] + "_spill." + pFileNames[1];
//                childFile = new File(childFileName);
//                if (!childFile.exists()) {
//                    childFile.createNewFile();
//                    childFileSet.add(childFile);//添加进childFileSet
//                }
//            }

            pw = new PrintWriter(new FileWriter(childFile, true));
            pw.println(line);
            pw.flush();
        }

        pw.close();
        br.close();

        //遍历childFileSet，发现超出实际内存大小的子文件
        //递归 → 不妥，∵不能防止相同单词的数量超过内存限制；且遍历时间成本较高
        //解决 → 插入元素前判断子文件空间大小，如果超过内存限制，则新建子文件夹
//        for (File cFile : childFileSet) {
//            if(cFile.length() > size*1024){
//                getSplitFile(cFile,2);
//            }
//        }

        return childFileSet;
    }

    /**
     * 根据实际内存大小，返回文件分割分数
     *
     * @param pFile 待分割文件
     * @param size  实际内存大小（单位：KB）
     * @return 文件分割分数
     */
    private static int autoGetCountSplit(File pFile, long size) {
        long pSize = pFile.length();
        int count = BigDecimal.valueOf(pSize).divide(BigDecimal.valueOf(size * 1024), 0, RoundingMode.FLOOR).intValue();

        //获取最接近的2的指数作为初始容量
        return closeToPowerOfTwo(count);
    }

    /**
     * 借鉴HashMap指定容量时，自动找最接近的2的指数作为初始容量
     *
     * @param num
     * @return
     */
    private static int closeToPowerOfTwo(int num) {
        int n = num - 1;     //-1防止本身num就是2的指数
        n |= n >>> 1;       //n=n|(n>>>1)
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }

    /**
     * 生成每一行是一个词的文件，每个文件不超过16个字节
     *
     * @param srcFileName
     * @param destFileName
     * @throws IOException
     */
    public static void generateWordTxt(String srcFileName, String destFileName) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(srcFileName)));
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = br.readLine()) != null) {
            stringBuffer.append(line);
        }

        String[] words = stringBuffer.toString().split("[^a-zA-Z]+");

        PrintWriter pw = new PrintWriter(new FileWriter(destFileName, true));
        for (String word : words) {

//          ASCII码、UTF-8、GBK 一个英文字母等于一个字节
//          Unicode、utf-16be   一个英文字母等于两个字节
            if (word.length() < 16) {//每个单词的长度小于16字节
                pw.println(word);
            }
        }

        pw.close();
        br.close();
    }
}
