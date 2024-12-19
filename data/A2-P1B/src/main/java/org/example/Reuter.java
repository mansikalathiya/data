package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * Analyze word frequencies in a Reuters news article
 */
public class Reuter {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Reuter").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile("file:///home/mkalathiya12/reut2-014.sgm");

        JavaRDD<String> filteredWords = lines
                .flatMap(line -> Arrays.asList(line.toLowerCase().split("\\W+")).iterator())
                .filter(word -> !word.isEmpty())
                .filter(word -> !word.matches(".*\\d.*"))
                .filter(word -> !isStopWord(word));


        JavaPairRDD<String, Integer> wordCounts = filteredWords
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum);

        List<Tuple2<String, Integer>> sortedWordCounts = wordCounts
                .mapToPair(Tuple2::swap)
                .sortByKey(false)
                .mapToPair(Tuple2::swap)
                .collect();

        System.out.println("Word Frequencies:");
        for (Tuple2<String, Integer> tuple : sortedWordCounts) {
            System.out.println(tuple._1 + ": " + tuple._2);
        }

        if (!sortedWordCounts.isEmpty()) {
            System.out.println("Highest frequency word: " + sortedWordCounts.get(0)._1 +
                    " (" + sortedWordCounts.get(0)._2 + ")");
            System.out.println("Lowest frequency word: " + sortedWordCounts.get(sortedWordCounts.size() - 1)._1 +
                    " (" + sortedWordCounts.get(sortedWordCounts.size() - 1)._2 + ")");
        }

        sc.stop();
    }
    /**
     * Check if the word is a stop word
     * defined in the list
     * @param word
     * @return
     */
    private static boolean isStopWord(String word) {
        List<String> stopWords = Arrays.asList("the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
                "with", "of", "by", "from", "up", "down", "about", "into", "onto", "off", "out", "over", "under",
                "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any",
                "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own",
                "same", "so", "than", "too", "very", "can", "will", "just", "done", "should", "now");
        return stopWords.contains(word);
    }
}