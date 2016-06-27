package com.example.rain.sdexplorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;

    File currentParent;
    File[] currentFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.path);

        File root = new File("/mnt/sdcard");

        if(root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();
            inflateListView(currentFiles);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentFiles[position].isFile()) {
                    return;
                }
                File[] tmp = currentFiles[position].listFiles();
                if(tmp == null || tmp.length == 0) {
                    Toast.makeText(MainActivity.this, "no file or no path", Toast.LENGTH_SHORT);
                }
                else {
                    currentParent = currentFiles[position];
                    currentFiles = tmp;
                    inflateListView(currentFiles);
                }
            }
        });
    }



    private void inflateListView(File[] files) {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < files.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            if(files[i].isDirectory()) {
                listItem.put("icon", R.drawable.wjj);
            }
            else{
                listItem.put("icon", R.drawable.wj);
            }
            listItem.put("fileName", files[i].getName());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.line
                , new String[]{"icon", "fileName"}
                , new int[]{R.id.icon, R.id.file_name});
        listView.setAdapter(simpleAdapter);
        try {
            textView.setText("path: " + currentParent.getCanonicalPath());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
