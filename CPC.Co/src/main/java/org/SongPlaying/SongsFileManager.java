package org.SongPlaying;

import java.io.File; // FIXED: Import unified Song class
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.GUI.Functionalities.Song;

public class SongsFileManager {
    static int songNotYetAddedCounter;

    /**
     * Load songs from a file using Scanner
     * Format: Title|Artist|CoverImage|AudioFile
     */
    public static ArrayList<Song> loadSongs(String fileName) throws FileNotFoundException {
        ArrayList<Song> songs = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            
            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            
            try {
                Song song = Song.fromFileLine(line); // FIXED: Use fromFileLine method
                songs.add(song);
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing line: " + line);
                System.err.println("  " + e.getMessage());
            }
        }
        
        scanner.close();
        return songs;
    }
    
    /**
     * Save songs to a file
     */
    public static void saveSongs(String fileName, List<Song> songs) {
        try (FileWriter fw = new FileWriter(fileName)) {
            for (Song song : songs) {
                fw.write(song.toFileLine() + "\n"); // FIXED: Use toFileLine method
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Append a single song to the file
     */
    public void appendSong(String fileName, Song song) throws IOException {
        File file = new File(fileName);
        boolean fileHasContent = file.length() > 0;

        FileWriter fw = new FileWriter(fileName, true);
        fw.append(fileHasContent ? "\n" : "").append(song.toFileLine()); // FIXED: Use toFileLine
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
                if (globalSong.title.equalsIgnoreCase(userSong.title) &&
                        globalSong.artist.equalsIgnoreCase(userSong.artist)) {
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
    */

    public int getSongNotYetAddedCounter() {
        return songNotYetAddedCounter;
    }
}

