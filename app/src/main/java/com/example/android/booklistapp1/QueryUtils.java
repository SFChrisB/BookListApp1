package com.example.android.booklistapp1;

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

/**
 * Created by AgiChrisPC on 03/07/2017.
 */

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static List<BookListParcel> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<BookListParcel> bookList = extractFeatureFromJson(jsonResponse);

        return bookList;
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

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
        } catch (IOException e) { Log.e(LOG_TAG, "Problem retrieving the bookList JSON results.", e);
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
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

    private static List<BookListParcel> extractFeatureFromJson(String bookListJSON) {
        if (TextUtils.isEmpty(bookListJSON)) return null;

        List<BookListParcel> bookList = new ArrayList<>();

        try {
            JSONObject base = new JSONObject(bookListJSON);
            JSONArray bookListArray = base.getJSONArray("items");

            for (int i = 0; i < bookListArray.length(); i++) {

                JSONObject currentBook = bookListArray.getJSONObject(i);

                //Searching the two areas of the JSON code that hold the search results
                JSONObject vi = currentBook.getJSONObject("volumeInfo");

                String title, auth, currentAuth, publ, pubD, desc, rawDesc, descShort, bUrl;

                JSONArray authArray;
                int authCount;

                title = vi.getString("title");

                //for loop to check and list authors. If none, displays "Author Unknown"
                if (vi.has("authors")) {
                    authArray = vi.getJSONArray("authors");
                    auth = "";
                    authCount = authArray.length();
                    for (int j = 0; j < authCount; j++) {
                        currentAuth = authArray.getString(j);
                        if (auth.isEmpty()) auth = currentAuth;
                        else auth = auth + ", " + currentAuth;
                    }
                }
                else auth = "Author Unknown";

                if (vi.has("publisher")) publ = vi.getString("publisher");
                else publ = "Publisher Unknown";

                if (vi.has("publishedDate")) pubD = vi.getString("publishedDate");
                else pubD = "Publication Date Unknown";

                if (vi.has("description")) {
                    rawDesc = vi.getString("description");
                    descShort = rawDesc.substring(0, 200);
                    desc = descShort + "...";
                } else desc = "No description of book available.";

                if (vi.has("infoLink")) bUrl = vi.getString(("infoLink"));
                else bUrl = null;

                BookListParcel bookListParcel = new BookListParcel(title, auth, publ, pubD, desc, bUrl);

                bookList.add(bookListParcel);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the bookList JSON results", e);
        }
        return bookList;
    }
}