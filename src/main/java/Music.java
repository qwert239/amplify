public class Music {
    // Stores information for music
    public String name;
    public String artist;
    public float song_length;
    public String thumbnail; //TODO use an image class

    public Music() {
        this("None", "None", 0.0f, "None");
    }

    public Music(String name, String artist, float song_length, String thumbnail) {
        // Overloaded constructor
        this.name = name;
        this.artist = artist;
        this.song_length = song_length;
        this.thumbnail = thumbnail;
    }
}
