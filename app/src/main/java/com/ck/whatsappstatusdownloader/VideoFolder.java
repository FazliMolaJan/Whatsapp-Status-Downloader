package com.ck.whatsappstatusdownloader;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.crashlytics.android.Crashlytics;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class VideoFolder extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
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

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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

    public void fn_video() {

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Whatsapp/Media/.Statuses");

        String pattern = ".mp4";
        String pattern2 = ".jpg";

        //Get the listfile of that folder
        final File listFile[] = dir.listFiles();
        if (listFile != null) {
        Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < listFile.length; i++) {
                // final int x = i;
                if (listFile[i].isDirectory()) {
                    //directory do nothing
                    Log.d("directory", "fn_video: directory do nothing ");
                } else {
                    if (listFile[i].getName().endsWith(pattern)) {
                        Status_Item obj_model = new Status_Item();
                        obj_model.setBoolean_selected(false);
                        obj_model.setStr_path(listFile[i].getAbsolutePath());
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(listFile[i].getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        obj_model.setStr_thumb(thumb);
                        obj_model.setFormat(".mp4");
                        al_video.add(obj_model);
                    }

                    else if (listFile[i].getName().endsWith(pattern2)) {
                        Status_Item obj_model = new Status_Item();
                        obj_model.setBoolean_selected(false);
                        obj_model.setStr_path(listFile[i].getAbsolutePath());
                       // Bitmap thumb = ThumbnailUtils.createVideoThumbnail(listFile[i].getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                           Bitmap thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(listFile[i].getAbsolutePath()), 256, 256);
                        obj_model.setStr_thumb(thumb);
                        obj_model.setFormat(".jpg");
                        al_video.add(obj_model);
                    }
                }
            }
        }

        else
        {
            Toast.makeText(VideoFolder.this,"no files found",Toast.LENGTH_SHORT).show();
        }
        obj_adapter = new Adapter_VideoFolder(getApplicationContext(),al_video,VideoFolder.this);
        recyclerView.setAdapter(obj_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        // if (id == R.menu.nav) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            // Set Message
            TextView msg = new TextView(this);
            // Message Properties
            msg.setText("Developer - Chand Kiran Singh \n Contributor - Ritik Channa");
            msg.setGravity(Gravity.CENTER_HORIZONTAL);
            msg.setTextColor(Color.BLACK);
            msg.setTextSize(25);
            alertDialog.setView(msg);

            new Dialog(getApplicationContext());
            alertDialog.show();

        //    return true;

        return super.onOptionsItemSelected(item);
    }
}
