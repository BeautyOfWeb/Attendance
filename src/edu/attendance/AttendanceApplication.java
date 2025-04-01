package edu.attendance;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

/**
 * Main class for the Attendance Application.
 * This class is responsible for launching the JavaFX application.
 */
public class AttendanceApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(new File("src/resources/fxml/AttendanceApp.fxml").toURI().toURL());
        Parent root = loader.load();
        
        // Set up the scene with CSS
        Scene scene = new Scene(root);
        scene.getStylesheets().add(new File("src/resources/css/attendance.css").toURI().toURL().toExternalForm());
        
        // Configure the primary stage
        primaryStage.setTitle("Classroom Attendance System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
