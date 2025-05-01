import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MusicPlayer {
    private Queue<Music> queue;
    private boolean looping;
    public MusicPlayer() {
        // Initialize an empty music player
        this.queue = new LinkedList<Music>();
        this.looping = false;
    }

    public void play() {
        try {
            // Start playing the song
            Platform.startup(()->{
                if (queue.peek() != null) {
                    Media media = new Media(queue.peek().get_music_path().toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setVolume(0.5);
                    mediaPlayer.play();
                    mediaPlayer.setOnEndOfMedia(this::next);
                }
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(-1);
        }
    }

    public void next(){
        if (this.looping) {

        }
    }

    public void pause() {
        // Pause the song
    }

    public void loop(boolean state){
        this.looping = state;
    }

    public void remove(String music_name) {
        // Look for the music in queue and remove it
        // TODO: Probably add a remove button in GUI
    }

    public void add(String name) {
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

    private String download_music(String url) throws IOException, InterruptedException {
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
