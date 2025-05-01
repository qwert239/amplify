public class Music {
    // Stores information for music
    public String name;
    public String artist;
    public float song_length;
    public String thumbnail; //TODO use an image class

    public Music() {
        this("None", "None");
    }

    public Music(String name, String artist) {
        // Overloaded constructor
        this.name = name;
        this.artist = artist;
    }
}
