package com.example.imdbmovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Raghu on 2/23/16.
 */
public class GetMovieAsync extends AsyncTask<String, Void, ArrayList<Movie>> {

    IMovies activity;
    public GetMovieAsync()
    {

    }

    public GetMovieAsync(IMovies activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MovieActivity.pd.show();
    }

    protected ArrayList<Movie> doInBackground(String... params)
    {
        URL url = null;
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int statusCode = 0;
        try {
            statusCode = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (statusCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (line != null) {
                sb.append(line);
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                return MovieUtil.parseMovies(sb.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        activity.getMovies(movies);

    }


    static public interface IMovies
    {
        public void getMovies(ArrayList<Movie> NList);
    }


}
