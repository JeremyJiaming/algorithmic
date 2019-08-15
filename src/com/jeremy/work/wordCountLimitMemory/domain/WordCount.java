package com.jeremy.work.wordCountLimitMemory.domain;
//词频实体类
public class WordCount implements Comparable<WordCount>{
    private String word;//单词
    private int count;//频率

    public WordCount() {
    }

    public WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "WordCount{" +
                "word='" + word + '\'' +
                ", count=" + count +
                '}';
    }


    @Override
    public int compareTo(WordCount wordCount) {
        return wordCount.count - this.count;//根据频次降序排序
    }
}
