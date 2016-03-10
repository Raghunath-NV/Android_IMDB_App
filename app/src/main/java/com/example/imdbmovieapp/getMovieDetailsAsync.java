package com.example.imdbmovieapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class getMovieDetailsAsync extends AsyncTask<String, Void, String> {

    IMoviesDetails activity;

    public getMovieDetailsAsync(IMoviesDetails activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                line = reader.readLine();
                while (line != null) {
                    sb.append(line);
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return sb.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject root = new JSONObject(s);
            Movie movie = Movie.getDetails(root);
                activity.passMovieDetails(movie);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static public interface IMoviesDetails
    {
        public void passMovieDetails(Movie movie);
    }

}

