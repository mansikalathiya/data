package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReutReader {
    public static void main(String[] args) {

        String filename = "reut2-014.sgm";
        System.out.println("Reading file: " + filename);

        String mongoUri = "mongodb+srv://mkalathiya287:mansi1712@a2.6kkdj73.mongodb.net/?retryWrites=true&w=majority&appName=A2";
        System.out.println("Connecting to MongoDB...");

        String dbName = "ReuterDb";
       
        String collectionName = "articles";


        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            System.out.println("Connection established.");
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);


            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            String fileContent = content.toString();
            Pattern pattern = Pattern.compile("<REUTERS[^>]*>(.*?)</REUTERS>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(fileContent);

            while (matcher.find()) {
                String article = matcher.group(1);
                String title = extractData(article, "TITLE");
                String body = extractData(article, "BODY");

                title = removeSpecialCharacters(title);

                Document doc = new Document("title", title)
                        .append("body", body);


                collection.insertOne(doc);
            }

            System.out.println("Articles have been successfully inserted into MongoDB.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Extracts data from a tag.

     * @param article the article to extract content from
     * @param tag     the tag to extract content from
     * @return the extracted content
     */
    private static String extractData(String article, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(article);
        if (matcher.find()) {
            return matcher.group(1).trim().replaceAll("\\s+", " ");
        }
        return "";
    }

    /**
     * Removes special characters from text.
     * @param text
     * @return
     */
    private static String removeSpecialCharacters(String text) {
        return text.replaceAll("&lt;[^&]*&gt;", "").trim();
    }
}
