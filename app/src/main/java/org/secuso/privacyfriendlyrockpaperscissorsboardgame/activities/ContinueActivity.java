package org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Created by david on 14.09.2016.
 */
public class ContinueActivity extends AppCompatActivity {
    File[] files;
    ArrayList<String> saveGameList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
    }

    @Override
    protected void onResume() {
        super.onResume();
        File dir= getFilesDir();
        //get save games
        this.files=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.contains(".save");
            }
        });
        //build the string savegames
        this.saveGameList= new ArrayList<>();
        for(int f=0;f<files.length;f++){
            String dateAndTime="";
            String mode="";
            String name=files[f].getName();
            String[]elements=name.split("_");
            dateAndTime=elements[0]+"."+elements[1]+"."+elements[2]+"\t\t"+elements[3]+":"+elements[4];
            int modeID=Integer.parseInt(elements[5].charAt(0)+"");
            switch (modeID){
                case 1:
                    mode=getString(R.string.sMode2);
                    break;
                case 2:
                    mode=getString(R.string.sMode3);
                    break;
                case 3:
                    mode=getString(R.string.sMode4);
                    break;
                default:
                    mode=getString(R.string.sMode1);
                    break;
            }
            String result= dateAndTime+"\n"+mode;
            saveGameList.add(result);
            }
        this.adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,saveGameList);
        ((ListView)findViewById(R.id.saveGameList)).setAdapter(adapter);
        ((ListView)findViewById(R.id.saveGameList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ContinueActivity.this,GameActivity.class);
                intent.putExtra("file",files[i].getAbsolutePath());
                startActivity(intent);
            }
        });
        //Allow deletion on long click
        ((ListView)findViewById(R.id.saveGameList)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContinueActivity.this);
                builder.setTitle(R.string.sDeleteTitle);
                builder.setMessage(R.string.sDeleteMessage);
                builder.setPositiveButton(R.string.sDialogHandOverOkButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int j) {
                        File f=ContinueActivity.this.files[i];
                        if(f.exists())
                            f.delete();
                        ContinueActivity.this.refreshFiles(i);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton(R.string.sNoDeletion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * Remove deleted save games from the list
     * @param i the index of the savegame in the list
     */
    private void refreshFiles(int i){
        this.saveGameList.remove(i);
        this.adapter.notifyDataSetChanged();
    }
}
