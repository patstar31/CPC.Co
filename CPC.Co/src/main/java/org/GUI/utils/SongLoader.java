package org.GUI.utils;

import org.GUI.Functionalities.Song;
import java.io.*;
import java.util.*;

/**
 * Loads songs from songs.txt using Scanner and File (as requested)
 */
public class SongLoader {
    private static final String DEFAULT_SONGS_FILE = "src/main/resources/songs.txt";
    
    /**
     * Load songs from default location
     */
    public static List<Song> loadSongs() {
        return loadSongs(DEFAULT_SONGS_FILE);
    }
    
    /**
     * Load songs from specific file path
     */
    public static List<Song> loadSongs(String filePath) {
        List<Song> songs = new ArrayList<>();
        File songFile = new File(filePath);
        
        System.out.println("🔍 Loading songs from: " + songFile.getAbsolutePath());
        
        if (!songFile.exists()) {
            System.err.println("❌ File not found: " + filePath);
            System.err.println("   Creating sample file...");
            createSampleFile(filePath);
            return songs;
        }
        
        try (Scanner scanner = new Scanner(songFile)) {
            int lineNumber = 0;
            
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                
                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                try {
                    Song song = Song.fromFileLine(line);
                    songs.add(song);
                    System.out.println("  ✓ Loaded: " + song.title + " by " + song.artist);
                } catch (IllegalArgumentException e) {
                    System.err.println("  ✗ Error on line " + lineNumber + ": " + e.getMessage());
                }
            }
            
            System.out.println("✅ Successfully loaded " + songs.size() + " songs");
            
        } catch (FileNotFoundException e) {
            System.err.println("❌ Unexpected error: File disappeared");
        } catch (Exception e) {
            System.err.println("❌ Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return songs;
    }
    
    /**
     * Create sample songs.txt if missing
     */
    private static void createSampleFile(String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Create directories if needed
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("# Songs Format: Title|Artist|CoverImage|AudioFile");
            writer.println("Decode|Paramore|decode_album_art.jpg|Decode-Paramore.wav");
            writer.println("Televised|HUNNY|televised_album_art.jpg|Televised-Hunny.wav");
            writer.println("Complicated|Avril Lavigne|complicated_album_art.jpg|Complicated-AvrilLavigne.wav");
            writer.println("Still into you|Paramore|still_into_you_album_art.jpg|Still_Into_You-Paramore.wav");
            System.out.println("✅ Created sample file: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ Could not create sample file: " + e.getMessage());
        }
    }
}