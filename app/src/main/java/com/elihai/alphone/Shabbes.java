package com.elihai.alphone;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import alphone.com.elihai.alphone.R;

public class Shabbes extends AppCompatActivity {
    private SharedPreferences sp;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabbes);
        final WebView image = (WebView) this.findViewById(R.id.shabbatYeshiva);

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://pastebin.com/raw.php?i=U4D1zfcA");
                    Scanner in = new Scanner(url.openStream());
                    sp=getPreferences(MODE_PRIVATE);
                    sp.edit().putString("shabatot",in.nextLine()).commit();
                }catch (IOException e){}
            }
        }).start();
        sp=getPreferences(MODE_PRIVATE);
        String s = sp.getString("shabatot","");
        image.getSettings().setBuiltInZoomControls(true);
        image.setInitialScale(65);
        image.loadUrl(s);
    }
}
