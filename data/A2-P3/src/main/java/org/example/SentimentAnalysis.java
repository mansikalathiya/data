package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.*;
import java.io.*;

public class SentimentAnalysis {
    /**
     * Positive words
     * Negative words
     */
    private static Map<String, Integer> positiveWords = new HashMap<>();
    private static Map<String, Integer> negativeWords = new HashMap<>();

    /**
     * Load positive and negative words from files
     * Load news articles from MongoDB
     * Analyze sentiment of each article
     * Write results to a CSV file
     */
    public static void main(String[] args) {
        loadWords("positive-words.txt", positiveWords);
        loadWords("negative-words.txt", negativeWords);
        String mongoUri = "mongodb+srv://mkalathiya287:mansi1712@a2.6kkdj73.mongodb.net/?retryWrites=true&w=majority&appName=A2";
        String dbName = "ReuterDb";
        String collectionName = "articles";
        List<NewsArticle> newsArticles = loadNewsArticlesFromMongo(mongoUri, dbName, collectionName);
        for (NewsArticle article : newsArticles) {
            analyzeSentiment(article);
        }
        writeResultsToCSV(newsArticles, "results.csv");
    }
    /**
     * Load positive and negative words from files
     * @param filename
     * @param wordMap
     */
    private static void loadWords(String filename, Map<String, Integer> wordMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordMap.put(line.toLowerCase().trim(), 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Load news articles from MongoDB
     * @param mongoUri
     * @param dbName
     * @param collectionName
     * @return
     */
    private static List<NewsArticle> loadNewsArticlesFromMongo(String mongoUri, String dbName, String collectionName) {
        List<NewsArticle> articles = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            int id = 1;
            for (Document doc : collection.find()) {
                String title = doc.getString("title");
                if(title.isEmpty()){
                    continue;
                }
                articles.add(new NewsArticle(id++, title));
            }
        }
        return articles;
    }
    /**
     * Create bag of words for the article
     * @param article
     */
    private static void createBagOfWords(NewsArticle article) {
        String[] words = article.title.toLowerCase().split("\\W+");
        for (String word : words) {
            article.bowMap.put(word, article.bowMap.getOrDefault(word, 0) + 1);
        }
    }
    /**
     * Analyze sentiment of the article
     * @param article
     */
    private static void analyzeSentiment(NewsArticle article) {
        createBagOfWords(article);

        for (String word : article.bowMap.keySet()) {
            if (positiveWords.containsKey(word)) {
                article.score += article.bowMap.get(word);
            }
            if (negativeWords.containsKey(word)) {
                article.score -= article.bowMap.get(word);
            }
        }

        if (article.score > 0) {
            article.polarity = "Positive";
        } else if (article.score < 0) {
            article.polarity = "Negative";
        } else {
            article.polarity = "Neutral";
        }
    }
    /**
     * Write results to a CSV file
     * @param articles
     * @param filename
     */
    private static void writeResultsToCSV(List<NewsArticle> articles, String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.println("News#,Title Content, match,Score,Polarity");
            for (NewsArticle article : articles) {
                writer.printf("%d,\"%s\",%s,%d,%s%n",
                        article.id,
                        article.title.replace("\"", "\"\""),
                        getContentMatch(article),
                        article.score,
                        article.polarity);
            }

            System.out.println("Results have been written to " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    /**
     * Get content match
     * @param article
     * @return
     */
    private static String getContentMatch(NewsArticle article) {
        StringBuilder sb = new StringBuilder();
        for (String word : article.bowMap.keySet()) {
            if (positiveWords.containsKey(word) || negativeWords.containsKey(word)) {
                sb.append(word).append(", ");
            }
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }
}

