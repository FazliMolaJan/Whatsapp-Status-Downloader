package com.ck.whatsappstatusdownloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoFolder extends AppCompatActivity {

    Adapter_VideoFolder obj_adapter;
    ArrayList<Status_Item> al_video = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videofolder);
        recyclerView = findViewById(R.id.recycler_view1);
        init();
    }

    private void init(){


        recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        fn_checkpermission();

    }

    private void fn_checkpermission(){
        /*RUN TIME PERMISSIONS*/

        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(VideoFolder.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(VideoFolder.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(VideoFolder.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {
            Log.e("Else","Else");
            fn_video();
        }
    }

    public void fn_video() {

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Whatsapp/Media/.Statuses");

        String pattern = ".mp4";
        String pattern2 = ".jpg";

        //Get the listfile of that flder
        final File listFile[] = dir.listFiles();
        Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                // final int x = i;
                if (listFile[i].isDirectory()) {
                    //directory do nothing
                } else {
                    if (listFile[i].getName().endsWith(pattern)) {
                        Status_Item obj_model = new Status_Item();
                        obj_model.setBoolean_selected(false);
                        obj_model.setStr_path(listFile[i].getAbsolutePath());
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(listFile[i].getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                        obj_model.setStr_thumb(thumb);
                        obj_model.setFormat(".mp4");
                        al_video.add(obj_model);
                    } else if (listFile[i].getName().endsWith(pattern2)) {
                        Status_Item obj_model = new Status_Item();
                        obj_model.setBoolean_selected(false);
                        obj_model.setStr_path(listFile[i].getAbsolutePath());
                        Bitmap thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(listFile[i].getAbsolutePath()), 512, 384);
                        obj_model.setFormat(".jpg");
                        obj_model.setStr_thumb(thumb);
                        al_video.add(obj_model);
                    }
                }
            }
        }
        obj_adapter = new Adapter_VideoFolder(getApplicationContext(),al_video,VideoFolder.this);
        recyclerView.setAdapter(obj_adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fn_video();
                    } else {
                        Toast.makeText(VideoFolder.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
