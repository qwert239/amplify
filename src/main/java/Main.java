import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        // Driver code
        check_yt_dlp();
        check_vlcj();
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.loop(true);

        String[] playlist = new String[]{
                "roundabout",
                "bury the light",
                "wheels on the bus",
                "kahoot theme beginning"
        };
        musicPlayer.play_playlist(playlist);
        Thread thread = new Thread(new music_thread(musicPlayer));
        thread.start();
    }

    static class music_thread implements Runnable {
        MusicPlayer musicPlayer;
        public music_thread(MusicPlayer musicPlayer) {
            this.musicPlayer = musicPlayer;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println("Muting song");
                musicPlayer.set_volume(0);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void check_yt_dlp(){
        // Checks whether yt_dlp is on the system and sends a warning if not
        try {
            // Run yt-dlp on CLI and check whether a valid version is returned

            // Start a process builder for the command yt-dlp --version
            ProcessBuilder pb = new ProcessBuilder("yt-dlp", "--version");
            Process process = pb.start();

            // Read the input from process builder
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                System.out.println("yt-dlp version: " + line);
                found = true;
            }

            // Checks for the version info
            int exitCode = process.waitFor();
            if (exitCode == 0 && found) {
                System.out.println("yt-dlp is installed.");
            } else {
                System.out.println("yt-dlp not found.");
                display_warning("yt-dlp is not installed on this system. \nRun 'pip install yt-dlp' on terminal or look up installation");
            }
        } catch (Exception e) {
            System.out.println("yt-dlp not found or an error occurred.");
            display_warning("yt-dlp is not installed on this system. \nRun 'pip install yt-dlp' on terminal or look up installation");
        }
    }

    private static void check_vlcj(){
        try {
            // Try to load a VLCJ class
            Class.forName("uk.co.caprica.vlcj.player.base.MediaPlayer");
            System.out.println("VLCJ is available.");
            // Proceed with VLCJ logic
        } catch (ClassNotFoundException e) {
            System.err.println("WARNING: VLCJ library not found. Please install or add it to the classpath.");
            // Optionally show GUI warning
            display_warning("VLCJ library not found. Please install or add it to the classpath.");
        }
    }

    private static void display_warning(String warning){
        JOptionPane.showMessageDialog(
                null,
                warning,
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
        System.exit(-1);
    }
}
