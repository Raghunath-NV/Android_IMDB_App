package com.example.imdbmovieapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class MovieWebViewActivity extends AppCompatActivity {

    String movieURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_web_view);
        setTitle("Movie Webview");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        movieURL="http://m.imdb.com/title/" + getIntent().getExtras().getString("movieID");

        TextView myUrl=(TextView)findViewById(R.id.textView12);
        myUrl.setText(movieURL + "/");
        myUrl.setBackgroundColor(Color.YELLOW);
        myUrl.setTextColor(Color.WHITE);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl(movieURL);
    }
}

