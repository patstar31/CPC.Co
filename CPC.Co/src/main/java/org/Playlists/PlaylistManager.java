package org.Playlists;



import org.GUI.Functionalities.Song;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public abstract class PlaylistManager {
    private String playlistName;
    ArrayList<Song> songs = new ArrayList<Song>();
    static int songCounter = 0;


    public PlaylistManager() {}

    public abstract ArrayList<Song> loadFromFile() throws FileNotFoundException;
    public abstract void saveToFile();

    public void addSong(Song song) throws IOException {}
    public void removeSong(Song song) {}

    public abstract String listSongs();
    public abstract int getSongLastNumber();
}