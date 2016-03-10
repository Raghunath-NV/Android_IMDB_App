package com.example.imdbmovieapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Raghu on 2/23/16.
 */
public class MovieUtil {
    public static ArrayList<Movie> parseMovies(String result) throws JSONException {
        {
            ArrayList<Movie> personList = new ArrayList<Movie>();
            JSONObject root = new JSONObject(result);
            JSONArray personJSONArray = root.getJSONArray("Search");
            for (int i = 0; i < personJSONArray.length(); i++) {
                JSONObject personJSON = personJSONArray.getJSONObject(i);
                Movie movie = Movie.createMovie(personJSON);
                personList.add(movie);
            }

            Collections.sort(personList);
            return personList;
        }

    }
}
