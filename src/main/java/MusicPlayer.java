import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MusicPlayer {
    private LinkedList<Music> queue;
    MediaPlayer current_player;
    private boolean looping;
    private double volume;

    public MusicPlayer() {
        // Initialize an empty music player
        this.queue = new LinkedList<Music>();
        this.looping = false;
        this.volume = 0.25;
    }

    public void play() {
        /* Starts playing the first song in the playlist*/
        try {
            // Start playing a new song
            Platform.startup(()->{
                Music current_song = queue.peek();
                if (current_song != null) {
                    Media media = new Media(current_song.get_music_path().toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    current_player = mediaPlayer;
                    mediaPlayer.setVolume(this.volume); // Set volume since it is pretty loud

                    mediaPlayer.play(); // Start playing song
                    mediaPlayer.setOnEndOfMedia(this::next); // Once song is done, play next music
                }
            });
        } catch (Exception e) {
            // Print out error without exiting if there is an error
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void next(){
        // TODO hasn't been tested
        /* Can be used to skip current song playing*/
        // Pause current music first
        if (current_player.getStatus() == MediaPlayer.Status.PLAYING) {
            current_player.pause();
        }

        // Remove music
        Music popped_music = queue.remove(); // Remove song from queue

        if (this.looping) {
            queue.add(popped_music);
        }
        if (queue.isEmpty()) {
            current_player = null; // If queue is empty after removing song, no song is playing
        } else {
            play(); // Start playing again if queue is not empty
        }
    }

    public void pause_toggle(boolean state) {
        // Pause/unpause the song
        if (current_player != null) {
            if (state){
                current_player.pause();
            } else {
                current_player.play();
            }
        }
    }

    public void loop(boolean state){
        // TODO hasn't been tested
        /* Turns on the loop state for the music player */
        this.looping = state;
    }

    public void set_volume(double volume){
        // TODO hasn't been tested
        current_player.setVolume(volume);
        this.volume = volume;
    }

    public void remove(String music_name) {
        // TODO: Probably add a remove button in GUI
        // TODO Hasnt been tested
        /* Look for the music in queue and remove it */
        int i = 0;
        for (Music music : queue) {
            if (music.name.equals(music_name + " ")) {
                queue.remove(i);
            }
            i++;
        }
    }

    public void remove(int index){
        // TODO only tested with 1 song
        // Overloads remove func, to remove based on index instead of name
        if (index < queue.size()) {
            if (index == 0){
                current_player.stop();
            } else {
                queue.remove(index);
            }
        }
    }

    public void add(String name) {
        /* Adds a new song to the playlist */
        String url = parse_name(name); // Could be given a name or url

        // Get music from youtube_dl
        String downloaded_name = null;
        try {
            downloaded_name = download_music(url); // If null, an exception is thrown
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        // Create a music object to store info
        String[] info = downloaded_name.split("-");
        Music obj = new Music(info[1], info[0]); // Store the author and name of song

        // Add to queue
        this.queue.add(obj);
    }

    public boolean is_empty(){
        return this.queue.isEmpty();
    }

    private String download_music(String url) throws IOException, InterruptedException {
        // Downloads music through yt-dlp with process builder

        String download_path = "/Amplify_MusicFiles/";
        ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "-x",
                "--audio-format", "mp3",
                "-o", download_path + "%(title)s.%(ext)s",
                url
        );
        Process process = pb.start();
        process.waitFor();

        return getName(process);
    }

    private static String getName(Process process) throws IOException {
        // Read input stream from yt_dlp
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String name = null;
        while ((line = reader.readLine()) != null){
            String[] split = line.split(" ");
            if (split[0].equals("[download]")){
                String[] new_split = line.split("\\\\");
                name = new_split[2].split(" has already been downloaded")[0];
                break;
            }
        }
        if (name == null) throw new NullPointerException("name is null");
        return name;
    }

    private String parse_name(String name){
        String url = name;
        String[] split_string = url.split("/"); // Split string for searching

        // Search through split string for link name
        boolean is_link = false;
        for (String s : split_string) {
            if (s.equals("www.youtube.com")) {
                is_link = true;
                break;
            }
        }
        if (!is_link) url = "ytsearch:" + name;
        return url;
    }
}
