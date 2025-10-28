package org.GUI.Functionalities;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private List<String> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void addSong(String song) {
        songs.add(song);
    }

    public void removeSong(String song) {
        songs.remove(song);
    }
}
