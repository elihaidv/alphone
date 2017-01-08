package alphone.com.elihai.alphone;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.*;

import com.elihai.alphone.Shabbes;
import com.elihai.alphone.Tfilot;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    private String selected="";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://pastebin.com/raw.php?i=FdWpvZd2");
                    Scanner in = new Scanner(url.openStream());
                    File out = new File(getFilesDir()+"/Phones.txt");
                    PrintWriter wp = new PrintWriter(out);

                        while(in.hasNext()){
                            String s=in.nextLine();
                            String c="";
                            for (int i = 0; i <s.length(); i++) {
                                char a=s.charAt(i);
                                a--;
                                c=c.concat(""+a);
                            }
                            wp.println(c);
                        }
                    wp.close();

                }catch (IOException e){
                    try{
                        InputStream is = getAssets().open("phones.txt");
                        Scanner in = new Scanner(is);
                        File out = new File(getFilesDir()+"/Phones.txt");
                        if(!out.exists()) {
                            PrintWriter wp = new PrintWriter(out);
                            while (in.hasNext())
                                 wp.println(in.nextLine());
                            wp.close();
                        }
                    }catch (IOException el){}
                }
            }
        });

        thread.start();
        final EditText editText=(EditText)findViewById(R.id.edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String message = editText.getText().toString();

                File in = new File(getFilesDir() + "/Phones.txt");
                String[] result = Alphone.searchPhone(in, message);

                final ListView list = (ListView) findViewById(R.id.resultList);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, result);
                list.setAdapter(adapter);
                list.setClickable(true);
                registerForContextMenu(list);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        selected = (String) list.getItemAtPosition(position);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + selected.substring(selected.length() - 10)));
                        Toast.makeText(getApplicationContext(), selected.substring(0, selected.length() - 10), Toast.LENGTH_LONG).show();
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
        ListView list=(ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selected = (String) list.getItemAtPosition(acmi.position);
        menu.setHeaderTitle(selected.substring(0,selected.length()-10));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.add: {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, selected.substring(0, selected.length() - 10));
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, selected.substring(selected.length() - 10));
                startActivity(intent);
                return true;
            }
            case R.id.copy:{
                selected = selected.substring(selected.length() - 11);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(selected);
                Toast.makeText(MainActivity.this, "המספר הועתק", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.sms:{
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:+972"+selected.substring(selected.length() - 10)));
                startActivity(sendIntent);
                return true;
            }
            case R.id.share:{
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, selected);
                startActivity(Intent.createChooser(sharingIntent, "שתף מספר"));
            }
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent instructions=new Intent(this, Ins.class);
            startActivity(instructions);
            return true;
        }
        if(id==R.id.list){
            Intent list =new Intent(this, NameList.class);
            startActivity(list);
            return true;
        }
        if(id==R.id.shabbes){
            Intent shabbes =new Intent(this, Shabbes.class);
            startActivity(shabbes);
            return true;
        }
        if(id==R.id.tfilot){
            Intent tfilot =new Intent(this, Tfilot.class);
            startActivity(tfilot);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
