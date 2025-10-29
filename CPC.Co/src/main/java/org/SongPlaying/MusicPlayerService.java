package org.SongPlaying;

import org.GUI.Functionalities.Songs;
import java.io.File;

/**
 * A central service (Singleton) to manage song state and UI updates.
 */
public class MusicPlayerService {
    private static MusicPlayerService instance;
    // Replace with your actual SongPlayer class if it has been implemented
    private Object songPlayer; // Use Object as a placeholder for SongPlayer

    private Songs currentSong;

    // Interface that the CurrentlyPlayingPanel must implement
    public interface PlaybackListener {
        void onSongChanged(Songs song);
        void onPlaybackStateChanged(boolean isPlaying);
    }

    private PlaybackListener listener;

    private MusicPlayerService() {
        // Private constructor for Singleton pattern
    }

    public static MusicPlayerService getInstance() {
        if (instance == null) {
            instance = new MusicPlayerService();
        }
        return instance;
    }

    public void setListener(PlaybackListener listener) {
        this.listener = listener;
        // Immediately update new listener if a song is already playing
        if (currentSong != null && listener != null) {
            listener.onSongChanged(currentSong);
            listener.onPlaybackStateChanged(true); // Assuming playback starts immediately
        }
    }

    /**
     * This method starts the UI process when a song is clicked.
     */
    public void playSong(Songs newSong) {
        System.out.println("Starting UI display for: " + newSong.songName);

        // --- TODO: Actual Playback Logic Goes Here ---
        // if (songPlayer != null) { ((SongPlayer) songPlayer).stop(); }
        // String songPath = new File(newSong.songFileName).getAbsolutePath();
        // songPlayer = new SongPlayer(songPath);
        // ((SongPlayer) songPlayer).play();
        // ---------------------------------------------

        this.currentSong = newSong;

        if (listener != null) {
            listener.onSongChanged(newSong);
            listener.onPlaybackStateChanged(true);
        }
    }

    public void togglePlayback() {
        // Placeholder for pausing/resuming
        System.out.println("Toggle Playback clicked for: " + currentSong.songName);
        if (listener != null) {
            // For UI testing, we'll just toggle the state
            // NOTE: In the final code, this should check the actual player state
            listener.onPlaybackStateChanged(true);
        }
    }

    public void nextSong() {
        System.out.println("NEXT SONG button clicked.");
    }

    public void previousSong() {
        System.out.println("PREVIOUS SONG button clicked.");
    }
}
