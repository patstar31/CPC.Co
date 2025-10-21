package org.SongPlaying;
public class Song {
    private String songTitle;
    private String songArtist;
    private String songFileName;

    public Song(String songTitle, String songArtist) {
        this.songTitle = songTitle;
        this.songArtist = songArtist;
    }

    public Song(String songTitle, String songArtist, String songFileName) {
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songFileName = songFileName;
    }
    public String toFileFormat() {
        return songTitle + "|" + songArtist;
    }

    public static Song fromFileFormat(String songLine) {
        int vb1 = songLine.indexOf("|");

        String songTitle = songLine.substring(0, vb1);
        String songArtist = songLine.substring(vb1 + 1);
        String songFileName = songTitle + ".wav"; // Assuming .wav file matches title

        return new Song(songTitle, songArtist, songFileName);
    }

    public String getSongTitle() { return songTitle; }
    public String getSongArtist() { return songArtist; }
    public String getSongFileName() { return songFileName; }
}