package com.ck.whatsappstatusdownloader;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;


public class Activity_galleryview extends Activity {

    String path;
    VideoView vv_video;
    ImageView iv_image;
    String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleryview);
        vv_video = findViewById(R.id.vv_video);
        iv_image = findViewById(R.id.iv_image);
        init();
    }

    private void init() {

        format = getIntent().getStringExtra("format");
        path = getIntent().getStringExtra("path");

        if (format.equals(".mp4")) {

            vv_video.setVideoPath(path);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(vv_video);
            vv_video.setMediaController(mediaController);
            vv_video.start();
            }
        else if (format.equals(".jpg")) {

            iv_image.setImageURI(Uri.parse(path));
        }

    }
}
