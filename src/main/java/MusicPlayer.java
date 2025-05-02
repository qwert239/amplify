import javafx.application.Platform;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MusicPlayer {
    private LinkedList<Music> queue;
    MediaPlayer mediaPlayer;
    private boolean looping;
    private int volume;

    public MusicPlayer() {
        // Initialize an empty music player
        this.queue = new LinkedList<Music>();
        this.looping = false;
        this.volume = 50;

        // Create a new media player
        MediaPlayerFactory factory = new MediaPlayerFactory("--network-caching=5000");
        this.mediaPlayer = factory.mediaPlayers().newMediaPlayer();
    }

    public void play() {
        /* Starts playing the first song in the playlist*/
        try {
            // Start playing a new song
            Platform.startup(()->{
                Music current_song = queue.peek();
                if (current_song != null) {
                    mediaPlayer.audio().setVolume(this.volume); // Change volume everytime new song is played
                    mediaPlayer.media().play(current_song.stream_url);
                    mediaPlayer.audio().setMute(true);

                    mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                        @Override
                        public void buffering(MediaPlayer mediaPlayer, float newCache) {
                            if (newCache >= 80) { // Play only when buffer reaches 80%
                                mediaPlayer.audio().setMute(false);
                            }
                        }

                        @Override
                        public void finished(MediaPlayer mediaPlayer) {
                            next();
                        }
                        @Override
                        public void error(MediaPlayer mediaPlayer) {
                            System.out.println("An error occurred during playback");
                        }
                    });
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
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().pause();
        }

        // Remove music
        Music popped_music = queue.remove(); // Remove song from queue

        if (this.looping) {
            queue.add(popped_music);
        }
        if (queue.isEmpty()) {
            mediaPlayer = null; // If queue is empty after removing song, no song is playing
        } else {
            play(); // Start playing again if queue is not empty
        }
    }

    public void pause_toggle(boolean state) {
        // Pause/unpause the song
        if (mediaPlayer.status().isPlayable()) {
            if (state){
                mediaPlayer.controls().pause();
            } else {
                mediaPlayer.controls().play();
            }
        }
    }

    public void loop(boolean state){
        // TODO hasn't been tested
        /* Turns on the loop state for the music player */
        this.looping = state;
    }

    public void set_volume(int volume){
        // TODO hasn't been tested
        mediaPlayer.audio().setVolume(volume);
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
                mediaPlayer.controls().stop();
            } else {
                queue.remove(index);
            }
        }
    }

    public void add(String name) {
        /* Adds a new song to the playlist */
        String url = parse_name(name); // Could be given a name or url

        // Get music from youtube_dl
        String[] info = null;
        try {
            info = download_music(url); // If null, an exception is thrown
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        // Create a music object to store info
        System.out.println(info[2]);

        Music obj = new Music(info[0], info[1], info[2]); // Store the author and name of song

        // Add to queue
        this.queue.add(obj);
    }

    public boolean is_empty(){
        return this.queue.isEmpty();
    }

    private String[] download_music(String url) throws IOException, InterruptedException {
        // Downloads music through yt-dlp with process builder


        ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "-f",
                "m4a",
                "--print",
                "%(title)s\n%(uploader)s\n%(url)s",
                url
        );
        Process process = pb.start();
        process.waitFor();

        return getResults(process);
    }

    private static String[] getResults(Process process) throws IOException {
        String[] results = new String[3];
        // Read input stream from yt_dlp
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for (int i = 0; i < 3; i++) {
            results[i] = reader.readLine();
        }

        return results;
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
