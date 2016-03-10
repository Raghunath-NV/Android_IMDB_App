package com.example.imdbmovieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements View.OnClickListener,GetMovieAsync.IMovies {

    LinearLayout layout;
    LinearLayout.LayoutParams params;
    ArrayList<Movie> moviesList;
    String name;
   static  ProgressDialog pd;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        layout = (LinearLayout) findViewById(R.id.movieLinearLayout);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        pd= new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Movie List");
        params.topMargin = 10;
        params.bottomMargin = 10;
        params.leftMargin = 5;
        params.rightMargin = 5;

        setTitle("Search Movies");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

       Intent intent=getIntent();
        if(intent.getExtras()!=null)
        {
             name = intent.getStringExtra("movie_key");

            new GetMovieAsync(this).execute("http://www.omdbapi.com/?type=movie&s=" + name);


        }

    }


    @Override
    public void onClick(View v)
    {

      int n= (int) v.getTag();
        Intent i=new Intent(this,MovieDetail.class);
        i.putParcelableArrayListExtra("movie_list", moviesList);
        i.putExtra("movie_key", n);
        i.putExtra("movie_name",name);
        finish();
        startActivity(i);

    }

    @Override
    public void getMovies(ArrayList<Movie> NList)
    {
        moviesList = NList;

        if(NList==null)
        {
            Intent i=new Intent(this,MainActivity.class);
            pd.dismiss();
            finish();
            Toast.makeText(MovieActivity.this,"Please Enter Correct Movie Name",Toast.LENGTH_SHORT).show();
            startActivity(i);

        }
        else {
            for (Movie m : moviesList) {

                TextView text = new TextView(this);
                text.setText(m.getTitle() + " (" + m.getYear() + ")");
                text.setTag(i);
                i++;
                text.setTextSize(15);
                text.setPadding(5, 5, 5, 5);
                text.setLayoutParams(params);

                text.setOnClickListener(this);
                layout.addView(text);

                ImageView divider = new ImageView(this);
                divider.setLayoutParams(params);


                layout.addView(divider);

            }
            pd.dismiss();
        }
    }
}
