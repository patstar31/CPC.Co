
package org.SongPlaying;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SongsFileManager {
    static int songNotYetAddedCounter;

    public static ArrayList<Song> loadSongs(String fileName) throws FileNotFoundException {
        ArrayList<Song> songs = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Song song = Song.fromFileFormat(line);
                songs.add(song);
            }
        return songs;
    }
    //  ???
    public static void saveSongs(String fileName, List<Song> songs) {
        try (FileWriter fw = new FileWriter(fileName)) {
            for (Song song : songs) {
                fw.write(song.toFileFormat() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    // ???
    public void appendSong(String fileName, Song song) throws IOException {
        File file = new File(fileName);
        boolean fileHasContent = file.length() > 0;

        FileWriter fw = new FileWriter(fileName, true);
        fw.append(fileHasContent ? "\n" : "").append(song.toFileFormat());
        fw.close();
    }
/* 
    public ArrayList<Song> getListOfSongsNotInPlaylist(String playlistFileName) throws FileNotFoundException {
        ArrayList<Song> songsToAdd = new ArrayList<>();

        Playlist globalPlaylist = new GlobalPlaylist("allSongs.txt");
        ArrayList<Song> allSongs = globalPlaylist.loadFromFile();

        Playlist userPlaylist = new UserDefinedPlaylist(playlistFileName);
        ArrayList<Song> userSongs = userPlaylist.loadFromFile();

        for (Song globalSong : allSongs) {
            boolean exists = false;
            for (Song userSong : userSongs) {
                if (globalSong.getSongTitle().equalsIgnoreCase(userSong.getSongTitle()) &&
                        globalSong.getSongArtist().equalsIgnoreCase(userSong.getSongArtist())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                songsToAdd.add(globalSong);
            }
        }
        return songsToAdd;
    }

    public int getSongNotYetAddedCounter() {
        return songNotYetAddedCounter;
    }
    */
}

