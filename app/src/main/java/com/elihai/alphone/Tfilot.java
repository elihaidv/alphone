package com.elihai.alphone;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import alphone.com.elihai.alphone.R;

public class Tfilot extends AppCompatActivity {

    private final String SITE = "http://www.tifart.com/B/";
    private WebView webView;
    private LinearLayout linearLayout;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilot);
        webView=(WebView)findViewById(R.id.webview);
        webView.setVisibility(View.GONE);
        sp=getPreferences(MODE_PRIVATE);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    int place = 0;
                    Document doc = Jsoup.connect(SITE + "tefilot.html").get();

                    Elements newsHeadlines = doc.select("p");
                    for (int i = 0;i < newsHeadlines.size(); i++) {
                        if (newsHeadlines.get(i).toString().contains("אברהם אוהבי")) {
                           place = i;
                        }

                    }

                    Element elm = newsHeadlines.get(place + 1).getElementsByTag("img").get(0);
                    sp.edit().putString("shabes",SITE + elm.attr("src")).commit();

                    elm = newsHeadlines.get(place + 2).getElementsByTag("img").get(0);
                    sp.edit().putString("yomhol",SITE + elm.attr("src")).commit();
                }catch (IOException e){}
            }
        });
        thread.start();

        linearLayout =(LinearLayout)findViewById(R.id.buttonsL);
    }

    public void shabbes(View view)
    {
        webView.loadUrl(sp.getString("shabes",""));
        loadURL();
    }
    public void yomhol(View view)
    {
        webView.loadUrl(sp.getString("yomhol",""));
        loadURL();
    }

    private void loadURL() {
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(130);
        linearLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }
}
