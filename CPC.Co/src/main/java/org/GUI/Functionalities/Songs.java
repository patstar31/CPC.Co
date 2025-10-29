package org.GUI.Functionalities;

import java.util.Objects;

/**
 * Data model for songs used primarily by the GUI components (SongPanel, AllSongsPanel).
 */
public class Songs {
    public String songName;
    public String songArtist;
    public String songPhotoCover; // Path to the album cover image resource
    public String songFileName;   // The actual audio file name (e.g., "SongTitle.wav")

    public Songs() {}

    // This is required to compare and identify which SongPanel is currently playing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Songs songs = (Songs) o;
        return Objects.equals(songName, songs.songName) &&
                Objects.equals(songArtist, songs.songArtist) &&
                Objects.equals(songFileName, songs.songFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songName, songArtist, songFileName);
    }
}