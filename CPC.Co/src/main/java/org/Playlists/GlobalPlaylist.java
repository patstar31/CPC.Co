package org.Playlists;

import org.SongPlaying.Song;
import org.SongPlaying.SongsFileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

    public class GlobalPlaylist extends PlaylistManager {
        final String playlistFileName = "allSongs.txt";
        public static ArrayList<Song> songs = new ArrayList<Song>();
        static int songCounter;

        public GlobalPlaylist() {
        }

        public GlobalPlaylist(String file) {
        }

        @Override
        public ArrayList<Song> loadFromFile() throws FileNotFoundException {
            return songs = SongsFileManager.loadSongs(playlistFileName);
        }

        @Override
        public void saveToFile() {}

        @Override
        public void addSong(Song song) throws IOException {
            File file = new File(playlistFileName);
            boolean fileHasContent = file.length() > 0;

            FileWriter fw = new FileWriter(playlistFileName, true);
            fw.append(fileHasContent ? "\n" : "").append(song.toFileFormat());
            fw.close();
        }
        @Override
        public void removeSong(Song song) {

        }
        @Override
        public String listSongs() {
            String songList = "";
            songCounter = 0;
            for(Song song : songs) {
                songCounter++;
                songList += songCounter + ". " + song.getSongTitle() + " — by " + song.getSongArtist() + "\n";
            }
            return songList;
        }
        @Override
        public int getSongLastNumber() {
            return songs.size();
        }

    }

