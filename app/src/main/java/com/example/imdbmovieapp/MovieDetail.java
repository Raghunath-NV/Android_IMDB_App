package com.example.imdbmovieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieDetail extends AppCompatActivity implements getMovieDetailsAsync.IMoviesDetails{

    TextView title,releaseDate,genre,director,actors,plot;
    ImageView image;
    ArrayList<Movie> mList =null;
    int index=0;
    String movieID,name;
    RatingBar rating;
    static ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        title = (TextView) findViewById(R.id.textView);
        releaseDate= (TextView) findViewById(R.id.textView3);
        genre = (TextView) findViewById(R.id.textView5);
        director = (TextView) findViewById(R.id.textView7);
        image= (ImageView) findViewById(R.id.imageView2);
        actors = (TextView) findViewById(R.id.textView9);
        plot= (TextView) findViewById(R.id.textView11);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        rating.setNumStars(5);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading Movie");
        rating.setClickable(false);
        setTitle("Movie Details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Intent intent=getIntent();

        if(intent.getExtras()!=null)
        {
             mList=intent.getParcelableArrayListExtra("movie_list");
             index = intent.getIntExtra("movie_key", 0);
            name=intent.getStringExtra("movie_name");
             callMethod(index);

        }
    }

    public void callMethod(int id)
    {
        pd.show();
        Movie m = mList.get(index);
        movieID =m.getImdbID();
        if(m.getPlot()==null)           //checking whether this movie details already fetched or not
        {
            new getMovieDetailsAsync(this).execute("http://www.omdbapi.com/?i=" + movieID);
        }
        else
        {
            displayDetails(m);
        }
    }

    public void finishClicked(View v)
    {

        Intent i =new Intent(this,MovieActivity.class);
        i.putExtra("movie_key",name);
        finish();
        startActivity(i);

    }


    public void leftClicked(View v)
    {
        if(index -1 <0)
        {
            Toast.makeText(this,"No Movies to show previous.  Move Right",Toast.LENGTH_SHORT).show();
        }
        else
        {
            callMethod(index--);
        }
    }
    public void rightClicked(View v)
    {
        if(index+1 >= mList.size())
        {
            Toast.makeText(this,"No Movies to show next.  Move Left",Toast.LENGTH_SHORT).show();
        }
        else
        {
            callMethod(index++);
        }


    }
    public void imageClicked(View v)
    {
        Intent intent = new Intent(MovieDetail.this,MovieWebViewActivity.class);
        intent.putExtra("movieID",mList.get(index).getImdbID());
        startActivity(intent);
    }

    @Override
    public void passMovieDetails(Movie movie) {
        mList.get(index).setReleased(movie.getReleased());
        mList.get(index).setActors(movie.getActors());
        mList.get(index).setImdbRating(movie.getImdbRating());
        mList.get(index).setGenre(movie.getGenre());
        mList.get(index).setPlot(movie.getPlot());
        mList.get(index).setDirector(movie.getDirector());

        displayDetails(mList.get(index));
    }

    private void displayDetails(Movie m) {
        new getMovieImage().execute(mList.get(index).getPoster());
        title.setText(m.getTitle() + "(" + m.getYear() + ")");

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat new_formatter = new SimpleDateFormat("MMM dd yyyy");
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(m.getReleased());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        releaseDate.setText(new_formatter.format(parsedDate));
        genre.setText(m.getGenre());
        director.setText(m.getDirector());
        actors.setText(m.getActors());
        plot.setText(m.getPlot());
        rating.setRating(m.getImdbRating()/2);


    }


    public class getMovieImage extends AsyncTask<String,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... params)
        {
            try {
                URL url = new URL(params[0]);

                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                InputStream in = urlConn.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(in);
                return image;
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
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);
            image.setImageBitmap(bitmap);
            pd.dismiss();
        }

    }
}
