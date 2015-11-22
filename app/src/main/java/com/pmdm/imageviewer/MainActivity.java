package com.pmdm.imageviewer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserActivity;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    ImageView iv;
    String path = null;
    String dir = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView)findViewById(R.id.IV);
        iv.setOnClickListener(this);

        dir = Environment.getExternalStorageDirectory() + "/Download/";
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FileChooserActivity.class);
        intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*jpg|.*png|.*gif|.*jpeg|.*JPG|.*PNG|.*GIF|.*JPEG");
        intent.putExtra(FileChooserActivity.INPUT_SHOW_ONLY_SELECTABLE, true);
        intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, dir);
        this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            boolean fileCreated = false;
            String filePath = "";

            Bundle bundle = data.getExtras();
            if(bundle != null)  {
                if(bundle.containsKey(FileChooserActivity.OUTPUT_NEW_FILE_NAME)) {
                    fileCreated = true;
                    File folder = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
                    String name = bundle.getString(FileChooserActivity.OUTPUT_NEW_FILE_NAME);
                    filePath = folder.getAbsolutePath() + "/" + name;
                } else {
                    fileCreated = false;
                    File file = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
                    filePath = file.getAbsolutePath();
                }
            }

            String message = fileCreated? "File created" : "File opened";
            message += ": " + filePath;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            path = filePath;
            dir = path.substring(0, path.lastIndexOf("/")+1);
            Bitmap bm = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bm);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (path!=null){
            Bitmap bm = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bm);
        }
    }
}
