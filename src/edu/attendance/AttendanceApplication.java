package edu.attendance;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main class for the Attendance Application.
 * This class is responsible for launching the JavaFX application.
 */
public class AttendanceApplication extends Application {
    private static final Logger LOGGER = Logger.getLogger(AttendanceApplication.class.getName());

    @Override
    public void start(Stage primaryStage) {
        try {
            // Get the application's base directory
            String baseDir = System.getProperty("user.dir");
            Path fxmlPath = Paths.get(baseDir, "resources", "fxml", "AttendanceApp.fxml");
            Path cssPath = Paths.get(baseDir, "resources", "css", "attendance.css");
            
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(fxmlPath.toUri().toURL());
            Parent root = loader.load();
            
            // Set up the scene with CSS
            Scene scene = new Scene(root);
            scene.getStylesheets().add(cssPath.toUri().toURL().toExternalForm());
            
            // Configure the primary stage
            primaryStage.setTitle("Classroom Attendance System");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start application", e);
            System.err.println("Failed to start application: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
