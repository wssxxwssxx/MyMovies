package com.example.mymoves.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";


    private static final String API_KEY = "c400d90296544010f62d3aebe4d5aa25";
    private static final String LANGUAGE_VALUES = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    private static final String MIN_VOTE_COUNT = "1000";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    public static URL buildUrlToReviews(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS,id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildUrlToVideos(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS,id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE,LANGUAGE_VALUES)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildURL(int sortBy,int page) {
        String methodOfSort;
        URL result = null;
        if(sortBy == POPULARITY){
            methodOfSort = SORT_BY_POPULARITY;
        }
        else{
            methodOfSort = SORT_BY_TOP_RATED;
        }
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE,LANGUAGE_VALUES)
                .appendQueryParameter(PARAMS_SORT_BY,methodOfSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT,MIN_VOTE_COUNT)
                .appendQueryParameter(PARAMS_PAGE,Integer.toString(page))
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{

        private Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;

        public interface OnStartLoadingListener{
            void onStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            if(onStartLoadingListener != null){
                onStartLoadingListener.onStartLoading();
            }
            super.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if(bundle == null){
                return null;
            }
            String urlAsString = bundle.getString("url");
            try {
                URL url = new URL(urlAsString);
                JSONObject result = null;
                if(url == null){
                    return null;
                }
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();
                    InputStreamReader ir = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(ir);
                    StringBuilder builder = new StringBuilder();
                    String line = br.readLine();
                    while(line != null){
                        builder.append(line);
                        line = br.readLine();
                    }
                    result = new JSONObject(builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static JSONObject getJSONForReviews(int id){
        JSONObject result = null;
        URL url = buildUrlToReviews(id);
        try {
            result = new JSONDownloadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForVideos(int id){
        JSONObject result = null;
        URL url = buildUrlToVideos(id);
        try {
            result = new JSONDownloadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONFromNetwork(int sortBy,int page){
        JSONObject result = null;
        URL url = buildURL(sortBy,page);
        try {
            result = new JSONDownloadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JSONDownloadTask extends AsyncTask<URL, Void, JSONObject>{


        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            if(urls == null || urls.length == 0){
                return null;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader ir = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(ir);
                StringBuilder builder = new StringBuilder();
                String line = br.readLine();
                while(line != null){
                    builder.append(line);
                    line = br.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return result;
        }
    }

}
