package com.example.currency;

import android.os.AsyncTask;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader {

    public interface ApiDataListener {
        void onDataDownloaded(String data);
    }

    public static void getValuesFromApi(String apiCode, ApiDataListener listener) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                InputStream apiContentStream = null;
                String result = "";
                try {
                    switch (apiCode) {
                        case Constants.FLOATRATES_API_URL:
                            apiContentStream = downloadUrlContent("http://www.floatrates.com/daily/usd.xml");
                            result = Parser.getCurrencyRatesBaseUsd(apiContentStream);
                            break;
                        default:
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (apiContentStream != null) {
                        try {
                            apiContentStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (listener != null) {
                    listener.onDataDownloaded(result);
                }
            }
        }.execute(apiCode);
    }

    private static InputStream downloadUrlContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
