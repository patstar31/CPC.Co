package org.SongPlaying;

import java.io.File;

import org.GUI.Functionalities.Song;

/**
 * SINGLETON PATTERN: Only one instance manages all playback
 * OBSERVER PATTERN: Notifies UI when song/state changes
 * 
 * This is the "bridge" between UI and SongPlayer (audio engine)
 */
public class MusicPlayerService {
    // Singleton instance
    private static MusicPlayerService instance;
    
    // Audio engine
    private SongPlayer songPlayer;
    
    // Current state
    private Song currentSong;
    private boolean isPlaying = false;

    /**
     * OBSERVER PATTERN: UI components implement this to get notified
     */
    public interface PlaybackListener {
        void onSongChanged(Song song);
        void onPlaybackStateChanged(boolean isPlaying);
    }

    private PlaybackListener listener;

    // Private constructor (Singleton)
    private MusicPlayerService() {}

    /**
     * Get the single instance
     */
    public static synchronized MusicPlayerService getInstance() {
        if (instance == null) {
            instance = new MusicPlayerService();
        }
        return instance;
    }

    /**
     * Register UI component to receive updates
     */
    public void setListener(PlaybackListener listener) {
        this.listener = listener;
        // Immediately sync with current state
        if (listener != null && currentSong != null) {
            listener.onSongChanged(currentSong);
            listener.onPlaybackStateChanged(isPlaying);
        }
    }

    /**
     * Play a new song (called when user clicks song in UI)
     */
    public void playSong(Song song) {
        System.out.println("\n🎵 MusicPlayerService.playSong()");
        System.out.println("   Song: " + song.title + " by " + song.artist);
        System.out.println("   Audio: " + song.audioFile);

        // Stop current song
        stopCurrent();

        // Find audio file
        File audioFile = findAudioFile(song.audioFile);
        
        if (audioFile == null || !audioFile.exists()) {
            System.err.println("❌ Audio file not found: " + song.audioFile);
            System.err.println("   Searched locations:");
            System.err.println("   - music/" + song.audioFile);
            System.err.println("   - src/main/resources/music/" + song.audioFile);
            return;
        }

        System.out.println("✅ Found: " + audioFile.getAbsolutePath());

        // Create player and start
        try {
            songPlayer = new SongPlayer(audioFile.getAbsolutePath());
            songPlayer.play();
            
            currentSong = song;
            isPlaying = true;

            // Notify UI
            if (listener != null) {
                listener.onSongChanged(song);
                listener.onPlaybackStateChanged(true);
            }
            
            System.out.println("✅ Playback started");
        } catch (Exception e) {
            System.err.println("❌ Playback error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Toggle play/pause
     */
    public void togglePlayback() {
        if (songPlayer == null || currentSong == null) {
            System.out.println("⚠️ No song loaded");
            return;
        }

        if (isPlaying) {
            songPlayer.pause();
            isPlaying = false;
            System.out.println("⏸ Paused");
        } else {
            songPlayer.resume();
            isPlaying = true;
            System.out.println("▶ Resumed");
        }

        if (listener != null) {
            listener.onPlaybackStateChanged(isPlaying);
        }
    }

    /**
     * Stop current song
     */
    public void stopCurrent() {
        if (songPlayer != null) {
            songPlayer.stop();
            songPlayer = null;
        }
        isPlaying = false;
    }

    // TODO: Implement playlist for these
    public void nextSong() {
        System.out.println("⏭ Next song - not implemented yet");
    }

    public void previousSong() {
        System.out.println("⏮ Previous song - not implemented yet");
    }

    // Getters
    public Song getCurrentSong() { 
        return currentSong; 
    }
    
    public boolean isPlaying() { 
        return isPlaying; 
    }

    /**
     * Search for audio file in common locations
     */
    private File findAudioFile(String fileName) {
        String[] searchPaths = {
            "music/" + fileName,
            "src/main/resources/music/" + fileName,
            fileName
        };

        for (String path : searchPaths) {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
        }

        return null;
    }
}
