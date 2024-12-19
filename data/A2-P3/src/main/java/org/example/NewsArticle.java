package org.example;

import java.util.HashMap;
import java.util.Map;


public class NewsArticle {
    int id;
    String title;
    Map<String, Integer> bowMap;
    int score;
    String polarity;

    /**
     * Constructor
     * @param id
     * @param title
     */
    public NewsArticle(int id, String title) {
        this.id = id;
        this.title = title;
        this.bowMap = new HashMap<>();
        this.score = 0;
        this.polarity = "Neutral";
    }
}