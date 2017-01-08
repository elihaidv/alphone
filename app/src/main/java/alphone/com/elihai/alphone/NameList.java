package alphone.com.elihai.alphone;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class NameList extends AppCompatActivity {

    private String selected="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        final ListView nl = (ListView) findViewById(R.id.NameList);

        File in = new File(getFilesDir()+"/Phones.txt");
        String nlist[] = Alphone.getList(in);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nlist);
        nl.setAdapter(adapter);
        nl.setClickable(true);
        registerForContextMenu(nl);

        nl.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                selected = (String) nl.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + selected.substring(selected.length() - 10)));
                Toast.makeText(getApplicationContext(), selected.substring(0, selected.length() - 10), Toast.LENGTH_LONG).show();
                startActivity(intent);

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
                Toast.makeText(getApplicationContext(), "המספר הועתק", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.sms:{
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:+972" + selected.substring(selected.length() - 10)));
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
}
