import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {
    private List<Music> queue;
    private boolean looping;
    public MusicPlayer() {
        // Initialize an empty music player
        this.queue = new ArrayList<>();
        this.looping = false;
    }

    public void play() {

    }

    public void pause() {

    }

    public void remove(String music_name) {
        // Look for the music in queue and remove it
    }

    public void add(String name) {
        String url = parse_name(name);
        // Get music from youtube_dl
        try {
            download_music(url);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        // Create a music object to store info
        // Add to queue
    }

    private void download_music(String url) throws IOException, InterruptedException {
        String download_path = "/Amplify_MusicFiles/";
        ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "-x",
                "--audio-format", "mp3",
                "-o", download_path + "%(title)s.%(ext)s",
                url
        );
        pb.inheritIO(); // TODO temp, remove when finished
        pb.start().waitFor();
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
