package com.ck.whatsappstatusdownloader;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class Adapter_VideoFolder extends RecyclerView.Adapter<Adapter_VideoFolder.ViewHolder> {

    ArrayList<Status_Item> al_video;
    Context context;
    Activity activity;

    public Adapter_VideoFolder() { }

    public Adapter_VideoFolder(Context context, ArrayList<Status_Item> al_video, Activity activity) {

        this.al_video = al_video;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.iv_image.setImageBitmap(al_video.get(position).str_thumb);
        Vholder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
        Vholder.rl_select.setAlpha(0);

        Log.d(TAG, "onBindViewHolder: " + al_video.get(position).str_path + "     " + al_video.get(position).getFormat());

        if (!al_video.get(position).getFormat().equals(".mp4")) {
            Vholder.play_btn.setVisibility(View.INVISIBLE);

        } else
            Vholder.play_btn.setVisibility(View.VISIBLE);

        Vholder.rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_gallery = new Intent(context, Activity_galleryview.class);
                intent_gallery.putExtra("format", al_video.get(position).getFormat());
                intent_gallery.putExtra("path", al_video.get(position).getStr_path());
                activity.startActivity(intent_gallery);
            }
        });

        Vholder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(al_video.get(position).getStr_path()));
                sendIntent.setType("file/*");
                activity.startActivity(Intent.createChooser(sendIntent, "Send Status via:"));
            }
        });

        Vholder.wsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(al_video.get(position).getStr_path()));
                sendIntent.setType("file/*");
                activity.startActivity(Intent.createChooser(sendIntent, "Send Status via:"));
            }
        });

        Vholder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourcePath = al_video.get(position).getStr_path();
                File source = new File(sourcePath);
                File myDirectory = new File(Environment.getExternalStorageDirectory(), "WSD");

                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }

                String pattern = ".mp4";
                String pattern2 = ".jpg";

                String destinationPath = Environment.getExternalStorageDirectory() + "/WSD/WSDStatus" + position + pattern;
                String destinationPath2 = Environment.getExternalStorageDirectory() + "/WSD/WSDStatus" + position + pattern2;

                File destination = new File(destinationPath);
                File destination2 = new File(destinationPath2);

                try {
                    if (sourcePath.endsWith(pattern)) {
                        FileUtils.copyFile(source, destination);
                    } else {
                        FileUtils.copyFile(source, destination2);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle("Status Saved Successfully at location: sdcard/WSD");
                alertDialog.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do the stuff..
                            }
                        }
                );

                alertDialog.show();
            }
        });
    }

    @Override
    public Adapter_VideoFolder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_videos, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);
        return viewHolder1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image;
        RelativeLayout rl_select;
        Button wsp;
        Button share;
        Button download;
        ImageView play_btn;

        public ViewHolder(View v) {
            super(v);
            iv_image = v.findViewById(R.id.iv_image);
            rl_select = v.findViewById(R.id.rl_select);
            wsp = v.findViewById(R.id.whatsapp);
            share = v.findViewById(R.id.share);
            download = v.findViewById(R.id.download);
            play_btn = v.findViewById(R.id.play_btn);
        }
    }


    @Override
    public int getItemCount() {
        return al_video.size();
    }
}
