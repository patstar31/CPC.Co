package org.SongPlaying;

import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

// class that contains code to manage the functionalities of playing songs
public class SongPlayer {
        //constructors for this class
        private Clip clip;
        private int pausePosition = 0;
        private String fileName;

        public SongPlayer(String fileName) {
            this.fileName = fileName;//filename is being passed from a different class
        }

        //to play, pause, resume. songs
        //connect to mouseclicker of the UI of songpanel, all songs, and playlists.
        public void play() {
            try {
                File musicPath = new File(fileName);
                if (musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.start();
                } else {
                    System.out.println("File Does Not Exist: " + fileName);
                }
            } catch (Exception e) {
                System.out.println("Error playing file: " + e.getMessage());
            }
            /*
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            clip = Audio System.getClip();
            clip.open(audioInput);
            clip.start();
             */
        }

        public void pause() {
            if (clip != null && clip.isRunning()) {
                pausePosition = clip.getFramePosition();
                clip.stop();
            }
        }

        public void resume() {
            if (clip != null && !clip.isRunning()) {
                clip.setFramePosition(pausePosition);
                clip.start();
            }
        }
    // we can remove stop because it basically does the same thing as pause
        public void stop() {
            if (clip != null) {
                clip.stop();
                clip.close();  // clean up resources
            }
        }

        // must change to play all songs minus the UI options displayed here
        public static boolean playAllSongs(ArrayList<Song> songs, boolean shuffle) {
            Scanner console = new Scanner(System.in);
            int count = songs.size();
            int songPlayedCounter = 0;

            boolean exitToSongMenu = false;

            ArrayList<Integer> order = new ArrayList<>();
            for (int i = 0; i < count; i++) order.add(i);

            if (shuffle) {

                for (int i = 0; i < count; i++) {
                    int randIndex = i + (int)(Math.random() * (count - i));
                    int temp = order.get(i);
                    order.set(i, order.get(randIndex));
                    order.set(randIndex, temp);
                }
            }

            for (int i = 0; i < count; i++) {
                Song song = songs.get(order.get(i));
                SongPlayer player = new SongPlayer(song.getSongFileName());
                player.play();

                boolean playing = true;
                while (playing) {
                    songPlayedCounter++;
                    System.out.print((shuffle ?
                            "\n=== PLAYING ALL SONGS SHUFFLED ===" :
                            "\n== PLAYING ALL SONGS UNSHUFLED ==") +
                            "\n---------------------------------" +
                            "\nNow Running... " +
                            "\n▶ " + song.getSongTitle() + " by " + song.getSongArtist() +
                            "\n---------------------------------" +
                            "\na. Pause" +
                            "\nb. Resume" +
                            "\nc. Next Song" +
                            "\nd. Stop and Return" +
                            "\ne. Back to Main Menu" +
                            "\n---------------------------------" +
                            "\nEnter an Option: ");
                    String act = console.next();

                    switch (act.toLowerCase()) {
                        case "a": player.pause(); break;
                        case "b": player.resume(); break;
                        case "c": player.stop(); playing = false; break;
                        case "d": player.stop(); playing = false; exitToSongMenu = true; break;
                        case "e": player.stop(); return true; // tell viewAllSongs to exit
                        default: System.out.println("Invalid input.");
                    }
                }
                if (exitToSongMenu) break;
            }
            if (songPlayedCounter == count) System.out.println("\n✅ Finished playing all songs!");
            return false; // don't exit to main
        }


    /* can be removed because it makes no sense to only playone song
        public static boolean playSingleSong(Song song) {
            Scanner console = new Scanner(System.in);
            SongPlayer player = new SongPlayer(song.getSongFileName());
            player.play();

            boolean stop = false;
            while (!stop) {
                System.out.print("\n====== PLAYING A SONG ONLY ======" +
                        "\n---------------------------------" +
                        "\nNow Running... " +
                        "\n▶ " + song.getSongTitle() + " by " + song.getSongArtist() +
                        "\n---------------------------------" +
                        "\na. Pause" +
                        "\nb. Resume" +
                        "\nc. Stop and Return" +
                        "\nd. Back to Main Menu" +
                        "\n---------------------------------" +
                        "\nEnter an Option: ");
                String act = console.next();

                switch (act.toLowerCase()) {
                    case "a": player.pause(); break;
                    case "b": player.resume(); break;
                    case "c": player.stop(); stop = true; break;
                    case "d": player.stop(); return true; // signal to exit to Main Menu
                    default: System.out.println("Invalid input.");
                }
            }
            return false;
        }
        */

}

