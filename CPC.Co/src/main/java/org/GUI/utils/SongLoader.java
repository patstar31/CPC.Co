package org.GUI.utils;

import org.GUI.Functionalities.Songs;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
}