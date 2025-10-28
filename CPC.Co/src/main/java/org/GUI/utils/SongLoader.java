package org.GUI.utils;

import org.GUI.Functionalities.Songs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// --- ADD THESE IMPORTS ---
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
// --- END IMPORTS ---

public class SongLoader {

    public static List<Songs> loadSongs(String filePath) {
        System.out.println("--- Attempting to load songs from: " + filePath + " ---");
        List<Songs> songs = new ArrayList<>();
        
        // --- DIAGNOSTIC STEP 1: Check if the file can be found ---
        InputStream is = SongLoader.class.getResourceAsStream(filePath);
        if (is == null) {
            System.err.println("FATAL ERROR: Could not find the file '" + filePath + "'.");
            System.err.println("Please make sure 'songs.txt' is inside the 'src/main/resources' folder.");
            return songs; // Return the empty list
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Songs newSong = new Songs();
                    newSong.songName = parts[0].trim();
                    newSong.songArtist = parts[1].trim();
                    newSong.songPhotoCover = parts[2].trim();
                    songs.add(newSong);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading the songs file.");
            e.printStackTrace();
        }
        
        // --- DIAGNOSTIC STEP 2: Check how many songs were loaded ---
        System.out.println("--- Successfully loaded " + songs.size() + " songs. ---");
        
        return songs;
    }
    

    // --- NEW METHODS BELOW ---

    /**
     * Gets the main folder for saving playlists (e.g., C:/Users/YourName/MyMusicApp/playlists)
     * @return File object pointing to the base save directory
     */
    public static File getPlaylistsDirectory() {
        String homeDir = System.getProperty("user.home");
        // Create a folder in the user's home directory
        File playlistsDir = new File(homeDir, "MyMusicApp/playlists"); 
        
        // Create the folders if they don't exist
        if (!playlistsDir.exists()) {
            playlistsDir.mkdirs();
        }
        return playlistsDir;
    }

    /**
     * Loads a list of songs from an *external* file path.
     * @param filePath The full path to the file (e.g., C:/Users/YourName/MyMusicApp/playlists/ad.txt)
     * @return A list of songs. Returns an EMPTY list if the file is not found.
     */
    public static List<Songs> loadSongsFromExternalPath(String filePath) {
        List<Songs> songs = new ArrayList<>();
        File file = new File(filePath);

        // If the file doesn't exist (like a new playlist), just return an empty list.
        // This FIXES your crash.
        if (!file.exists()) {
            return songs; 
        }

        // Use FileInputStream for external files
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse using the same format as your main loadSongs method
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Songs newSong = new Songs();
                    newSong.songName = parts[0].trim();
                    newSong.songArtist = parts[1].trim();
                    newSong.songPhotoCover = parts[2].trim();
                    songs.add(newSong);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading external song file: " + filePath);
            e.printStackTrace();
        }
        return songs;
    }

    /**
     * Appends a single song to a playlist file.
     * @param playlistName The name of the playlist (e.g., "ad")
     * @param song The song to add
     */
    public static void addSongToPlaylistFile(String playlistName, Songs song) {
        File playlistFile = new File(getPlaylistsDirectory(), playlistName + ".txt");
        
        // FileWriter(file, true) means "append" to the file
        try (FileWriter fw = new FileWriter(playlistFile, true);
             PrintWriter pw = new PrintWriter(fw)) {
             
            // Save in the same CSV format: Name,Artist,Cover
            pw.println(song.songName + "," + song.songArtist + "," + song.songPhotoCover);
            
        } catch (IOException e) {
            System.err.println("Error saving song to playlist file: " + playlistFile.getAbsolutePath());
            e.printStackTrace();
        }
    }
}