import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Driver code
        // TODO check if yt_dlp is downloaded on system, prompt user to download if not
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.add("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        musicPlayer.play();

    }
}
