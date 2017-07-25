package com.example.android.icecreamyouscream;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.icecreamyouscream.MainActivity.LOG_TAG;

public final class Utils {

    private Utils() {
    }

    public static List<Article> fetchArticleData(String requestUrl) {
        Log.i(LOG_TAG, "TEST: fetchArticleData() Started");
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static List<Article> extractFeatureFromJson(String articleJSON) {
        Log.i(LOG_TAG, "TEST: extractFeatureFromJson called");
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            JSONArray bookArray;
            if (baseJsonResponse.getJSONObject("response").has("results")){
                bookArray = baseJsonResponse.getJSONObject("response").getJSONArray("results");
            }else {
                return null;
            }

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentArticle = bookArray.getJSONObject(i);

                String title = currentArticle.getString("webTitle");

                String date;
                if (currentArticle.has("webPublicationDate")) {
                    date = currentArticle.getString("webPublicationDate");
                } else {
                    date = "Date N/A";
                }

                String section;
                if (currentArticle.has("sectionName")) {
                    section = currentArticle.getString("sectionName");
                }else {
                    section = "Section N/A";
                }

                String url = currentArticle.getString("webUrl");

                Article article = new Article(title, date, url, section);

                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e("Utils", "extractFeatureFromJson: Problem parsing the JSON results", e);
            e.printStackTrace();
        }
        return articles;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}

