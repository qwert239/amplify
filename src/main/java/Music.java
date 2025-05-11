public class Music {
    // Stores information for music
    private String name;
    private String artist;
    private String stream_url;

    public Music() {
        this("None", "None", "None");
    }

    public Music(String name, String artist, String stream_url) {
        // Overloaded constructor
        this.name = name;
        this.artist = artist;
        this.stream_url = stream_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }
}
