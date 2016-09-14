package org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by david on 14.09.2016.
 */
public class ContinueActivity extends AppCompatActivity {
    File[] files;
    String[] saveGameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
    }

    @Override
    protected void onResume() {
        super.onResume();
        File dir= getFilesDir();
        this.files=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.contains(".save");
            }
        });
        this.saveGameList= new String[files.length];
        for(int f=0;f<files.length;f++){
            String dateAndTime="";
            String mode="";
            String name=files[f].getName();
            String[]elements=name.split("_");
            dateAndTime=elements[0]+"."+elements[1]+"."+elements[2]+"\t\t"+elements[3]+":"+elements[4];
            int modeID=Integer.valueOf(elements[5].charAt(0));
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
            saveGameList[f]=result;
            }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,saveGameList);
        ((ListView)findViewById(R.id.saveGameList)).setAdapter(adapter);
        ((ListView)findViewById(R.id.saveGameList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ContinueActivity.this,GameActivity.class);
                intent.putExtra("file",files[i].getAbsolutePath());
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();
    }
}
