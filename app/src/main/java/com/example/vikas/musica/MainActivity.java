package com.example.vikas.musica;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<Song> songList;

    ListView songView;


    SongAdapter songAdapter;
    private MediaPlayer mp = new MediaPlayer();


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songView = findViewById(R.id.music_list_view);
        songList = new ArrayList<Song>();
        getSongList();

        //sorting the song list
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getTitle().compareTo(t1.getTitle());
            }
        });

        checkExternalStoragePermission();

    }


    //setting the onClick attribute of the layout for the each item in the song list


    private void checkExternalStoragePermission() {
        //check if permission for reading external storage is granted, otherwise ask for the permission

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

            }
        } else {
            doStuff();
        }
    }


    public void doStuff() {
        songView = findViewById(R.id.music_list_view);
        songList = new ArrayList<>();
        getSongList();
        songAdapter = new SongAdapter(this, songList);
        songView.setAdapter(songAdapter);


        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = view.findViewById(R.id.song_title);
                String songName = title.getText().toString();


                try {
                    mp.reset();
                    Song currentSong = songList.get(i);
                    mp.setDataSource(currentSong.getSongUrl());
                    mp.prepareAsync();
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {

                            mp.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), songName, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void releaseMediaPlayer() {

        if (mp != null) {
            mp.release();
        }
        mp = null;
    }

    public void getSongList() {

        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor musicCursor = musicResolver.query(musicUri, null, selection, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {

            //get columns
            int songName = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int songArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songId = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int data = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //add songs to the list
            do {
                String currentTitle = musicCursor.getString(songName);

                String currentArtist = musicCursor.getString(songArtist);

                long currentId = musicCursor.getLong(songId);

                String currentData = musicCursor.getString(data);

                songList.add(new Song(currentId, currentTitle, currentArtist, currentData));
            } while (musicCursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                        doStuff();

                    }
                } else {
                    Toast.makeText(this, "NO permission granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }

    }


}


