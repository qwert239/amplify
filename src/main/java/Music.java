import java.io.File;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    // Stores information for music
    public String name;
    public String artist;
    private File music_path;

    public Music() {
        this("None", "None");
    }

    public Music(String name, String artist) {
        // Overloaded constructor
        this.name = name;
        this.artist = artist;
        this.music_path = this.get_music_path();
    }

    public File get_music_path() {
        if (music_path == null) {
            return new File("/Amplify_MusicFiles/" + artist + "-" + name);
        } else {
            return music_path;
        }
    }
}
