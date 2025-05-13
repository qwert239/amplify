import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Driver code
        check_yt_dlp();
        check_vlcj();
        
        init_gui(stage);
    }

    private static void init_gui(Stage stage) {
        // GUI
        stage.setTitle("Amplify");
        GridPane grid = new GridPane();

        // Add Buttons
        Button add_button = new Button("Add Music");
        Button search_button = new Button("Search Music");
        Button play = new Button("Start Playing");
        Button pause = new Button("Pause Music");
        Button unpause = new Button("Unpause Music");
        Button skip = new Button("Skip Music");

        grid.add(add_button, 0, 0);
        grid.add(search_button, 1, 0);
        grid.add(play, 2, 0);
        grid.add(pause, 0, 2);
        grid.add(unpause, 1, 2);
        grid.add(skip, 2, 2);

        // Create textarea for input
        TextArea textArea = new TextArea();
        textArea.setEditable(true);
        textArea.setWrapText(true);
        textArea.setMaxWidth(100);
        textArea.setMaxHeight(20);
        grid.add(textArea, 0, 1);

        // Add event listeners
        MusicPlayer musicPlayer = new MusicPlayer(); // Initialize music player
        add_button.setOnAction(e -> musicPlayer.add(textArea.getText())); // Add textbox text to queue

        search_button.setOnAction(e -> {                                  // Search for music
            String[][] results = musicPlayer.search(textArea.getText());
            for (String[] result : results) {
                System.out.println(result[0]);
            }
        });

        play.setOnAction(e -> musicPlayer.play()); // Start playing music in queue

        pause.setOnAction(e -> musicPlayer.pause_toggle(true)); // Pause music

        unpause.setOnAction(e -> musicPlayer.pause_toggle(false)); // Unpause music

        skip.setOnAction(e -> musicPlayer.next()); // Unpause music

        // Create scene
        Scene scene = new Scene(grid, 300, 300);
        stage.setScene(scene);
        stage.show();
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
