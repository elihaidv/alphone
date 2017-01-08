package alphone.com.elihai.alphone;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Ins extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins);
    }

    public void imageClicked(View v)
    {
        Uri uriUrl = Uri.parse("https://docs.google.com/document/d/100jmLI9W0b4ceQYAMUnOtkLJX1mlRA1q_orKKpVxgEI/edit?usp=sharing");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
