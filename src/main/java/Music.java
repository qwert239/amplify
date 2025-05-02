public class Music {
    // Stores information for music
    public String name;
    public String artist;
    public String stream_url;

    public Music() {
        this("None", "None", "None");
    }

    public Music(String name, String artist, String stream_url) {
        // Overloaded constructor
        this.name = name;
        this.artist = artist;
        this.stream_url = stream_url;
    }
}
