package org.SongPlaying;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class SongPlayer {
        private Clip clip;
        private int pausePosition = 0;
        private String fileName;

        public SongPlayer(String fileName) {
            this.fileName = fileName;
        }

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

        public void stop() {
            if (clip != null) {
                clip.stop();
                clip.close();  // clean up resources
            }
        }

        // Updated: Play all songs (normal or shuffled)
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


        // Updated: Play a single song
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
}

