import java.io.File;

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

    private File get_music_path() {
        return new File("/Amplify_MusicFiles/" + artist + "-" + name + ".mp3");
    }
}
