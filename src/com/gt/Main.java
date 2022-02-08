package com.gt;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

class Response {
    String status;
    int totalResults;
    List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
class Article {
    private String author;
    private String title;
    private String description;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

public class Main {

    private static HttpURLConnection connection;
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        FileWriter fileWriter = null;
        String fileName;
        System.out.println("Zaczynamy");
        System.out.println("Podaj nazwę pliku wyjściowgo łącznie z rozszerzeniem");
        fileName = scanner.nextLine();

        if (fileName.length() == 0) {
            fileName = "file.txt";
        }
        System.out.println("Nazwa pliku wyjściowego: "+ fileName);
        try {
            //URL url = new URL("https://newsapi.org/v2/top-headlines/sources?category=business&country=pl&apiKey=54db3a69fde5441283c9a4a528dce2a6");
            URL url = new URL("https://newsapi.org/v2/top-headlines?category=business&country=pl&apiKey=54db3a69fde5441283c9a4a528dce2a6");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
            }
           // System.out.println(responseContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        Response response = new Gson().fromJson(responseContent.toString(), Response.class);
        try {
            fileWriter = new FileWriter(fileName);
            for (int i = 0; i < response.getArticles().size() ; i++) {
                fileWriter.write(response.getArticles().get(i).getTitle() + ":");
                fileWriter.write(response.getArticles().get(i).getDescription() + ":");
                fileWriter.write(response.getArticles().get(i).getAuthor() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
        System.out.println("Informacje zostałe zapisane w pliku: " + fileName);
    }
}
