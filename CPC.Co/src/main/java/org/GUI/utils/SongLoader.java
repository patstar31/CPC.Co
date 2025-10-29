package org.GUI.utils;
// a class that contains the methods to load songs from the songs.txt file
import org.GUI.Functionalities.Songs;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.nio.*;

public class SongLoader {

    //loads songs from the song.txt
    public static List<Songs> loadSongs(String filePath) {
        System.out.println("--- Attempting to load songs from: " + filePath + " ---");
        ArrayList<Songs> songs = new ArrayList<>();
        
        // Check if the file can be found
        InputStream is = SongLoader.class.getResourceAsStream(filePath);// uses "getResourceAsStream"rather than try catch
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
        
        //  Display and check the number of songs that were loaded.
        System.out.println("--- Successfully loaded " + songs.size() + " songs. ---");
        return songs;


        /*
        another way load this would be using try catch statements.
        // to be placed after the instantiation of the songs ArrayList

       try(Scanner songScanner = new Scanner(new File(filepath)){

        while(songScanner.hasNextLine){
                String songInfo = songScanner.nextLine();
                String[] parts = songInfo.split(",");
                if (parts.length == 3) {
                    Songs newSong = new Songs();
                    newSong.songName = parts[0].trim();
                    newSong.songArtist = parts[1].trim();
                    newSong.songPhotoCover = parts[2].trim();
                    songs.add(newSong);
                }
            }
            catch (Exception e) {
            System.err.println("Error reading the songs file.");
            e.printStackTrace();
            }
       }
         */
    }
}