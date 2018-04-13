package com.example.vikas.musica;

import android.net.Uri;

public class Song {

    private long id;
    private String title;
    private String artist, songUrl;


    public Song(long id, String title, String artist, String url) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.songUrl = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongUrl() {
        return songUrl;
    }
}
