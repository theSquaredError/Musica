package com.example.vikas.musica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.LayoutInflater.from;

public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;

    private LayoutInflater songInf;

    public SongAdapter(Context context, ArrayList<Song> songsList) {

        songs = songsList;

        songInf = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return songs.size();

    }

    @Override
    public Object getItem(int i) {

        //TODO auto generated method stub
        return null;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //map to the song layout

        LinearLayout songLay = (LinearLayout) songInf.inflate(R.layout.song_list_item, viewGroup, false);
        //get title and artist views
        TextView songView = songLay.findViewById(R.id.song_title);
        TextView titleView = songLay.findViewById(R.id.song_title);

        //get the song using the position
        Song currentSong = songs.get(position);
        //setting the songView and titleView
        songView.setText(currentSong.getTitle());
        songView.setText(currentSong.getArtist());
        //set position as a tag
        songLay.setTag(position);
        return songLay;
    }

    @Override
    public long getItemId(int i) {
        //TODO auto generated method stub
        return 0;
    }
}
