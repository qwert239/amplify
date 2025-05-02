import javafx.animation.PauseTransition;
import javafx.util.Duration;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        // Driver code
        check_yt_dlp();
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.add("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        musicPlayer.play();

    }

    private static void check_yt_dlp(){
        try {
            ProcessBuilder pb = new ProcessBuilder("yt-dlp", "--version");
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                System.out.println("yt-dlp version: " + line);
                found = true;
            }

            int exitCode = process.waitFor();
            if (exitCode == 0 && found) {
                System.out.println("yt-dlp is installed.");
            } else {
                System.out.println("yt-dlp not found.");
                display_warning();
            }
        } catch (Exception e) {
            System.out.println("yt-dlp not found or an error occurred.");
            display_warning();
        }
    }

    private static void display_warning(){
        JOptionPane.showMessageDialog(
                null,
                "yt-dlp is not installed on this system. \nRun 'pip install yt-dlp' on terminal or look up installation",
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
        System.exit(-1);
    }
}
