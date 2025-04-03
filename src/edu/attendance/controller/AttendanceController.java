package edu.attendance.controller;

import edu.attendance.model.Student;
import edu.attendance.util.StudentDataService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.media.*;
import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the Attendance Application UI.
 */
public class AttendanceController implements Initializable {

    @FXML private ListView<Student> studentListView;
    @FXML private ImageView studentImageView;
    @FXML private Label studentNameLabel;
    @FXML private Label quoteLabel;
    @FXML private Label statsLabel;
    @FXML private Label statusLabel;
    @FXML private Button presentButton;
    @FXML private Button absentButton;
    @FXML private Button excusedButton;
    @FXML private Button playNameButton;
    @FXML private Button randomSelectButton;
    @FXML private Button saveButton;
    
    private StudentDataService dataService;
    private ObservableList<Student> students;
    private Student selectedStudent;
    private Random random = new Random();
    private MediaPlayer mediaPlayer;
    
    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataService = new StudentDataService();
        students = FXCollections.observableArrayList(dataService.loadStudents());
        
        // Set up student list view
        studentListView.setItems(students);
        studentListView.setCellFactory(lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    setText(student.getName());
                }
            }
        });
        
        // Add selection listener
        studentListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayStudentDetails(newValue);
                    selectedStudent = newValue;
                    enableAttendanceButtons(true);
                } else {
                    clearStudentDetails();
                    enableAttendanceButtons(false);
                }
            }
        );
        
        // Initialize attendance buttons
        enableAttendanceButtons(false);
        
        // Set initial status
        updateStatus("Ready. " + students.size() + " students loaded.");
    }
    
    /**
     * Displays the details of the selected student.
     */
    private void displayStudentDetails(Student student) {
        // Display name
        studentNameLabel.setText(student.getName());
        
        // Display quote
        quoteLabel.setText("\"" + student.getFavoriteQuote() + "\"");
        
        // Display statistics
        String stats = String.format("Called: %d | Present: %d | Absent: %d | Excused: %d",
                student.getTimesCalled(), student.getTimesPresent(), 
                student.getTimesAbsent(), student.getTimesExcused());
        statsLabel.setText(stats);
        
        // Display image if available
        String imagePath = dataService.getStudentImagePath(student.getId());
        if (imagePath != null) {
            try {
                Image image = new Image(new File(imagePath).toURI().toString());
                studentImageView.setImage(image);
            } catch (Exception e) {
                studentImageView.setImage(null);
            }
        } else {
            studentImageView.setImage(null);
        }
        
        // Check if audio is available
        String audioPath = dataService.getStudentAudioPath(student.getId());
        playNameButton.setDisable(audioPath == null);
    }
    
    /**
     * Clears the student details from the UI.
     */
    private void clearStudentDetails() {
        studentNameLabel.setText("");
        quoteLabel.setText("");
        statsLabel.setText("");
        studentImageView.setImage(null);
        selectedStudent = null;
    }
    
    /**
     * Enables or disables the attendance buttons.
     */
    private void enableAttendanceButtons(boolean enable) {
        presentButton.setDisable(!enable);
        absentButton.setDisable(!enable);
        excusedButton.setDisable(!enable);
        playNameButton.setDisable(!enable);
    }
    
    /**
     * Updates the status message.
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    /**
     * Handles the random student selection.
     */
    @FXML
    private void handleRandomSelect() {
        if (students.isEmpty()) {
            updateStatus("No students available for selection.");
            return;
        }
        
        // Calculate selection probabilities
        double[] probabilities = new double[students.size()];
        double totalProbability = 0;
        
        for (int i = 0; i < students.size(); i++) {
            probabilities[i] = students.get(i).getSelectionProbability();
            totalProbability += probabilities[i];
        }
        
        // Normalize probabilities
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= totalProbability;
        }
        
        // Select a student based on weighted probability
        double value = random.nextDouble();
        double cumulativeProbability = 0.0;
        int selectedIndex = 0;
        
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (value <= cumulativeProbability) {
                selectedIndex = i;
                break;
            }
        }
        
        // Select and show the student
        studentListView.getSelectionModel().select(selectedIndex);
        studentListView.scrollTo(selectedIndex);
        
        // Update status
        updateStatus("Randomly selected: " + students.get(selectedIndex).getName());
    }
    
    /**
     * Handles marking a student as present.
     */
    @FXML
    private void handlePresent() {
        if (selectedStudent != null) {
            selectedStudent.markPresent();
            updateStudentDisplay();
            updateStatus(selectedStudent.getName() + " marked as PRESENT.");
        }
    }
    
    /**
     * Handles marking a student as absent.
     */
    @FXML
    private void handleAbsent() {
        if (selectedStudent != null) {
            selectedStudent.markAbsent();
            updateStudentDisplay();
            updateStatus(selectedStudent.getName() + " marked as ABSENT.");
        }
    }
    
    /**
     * Handles marking a student as excused.
     */
    @FXML
    private void handleExcused() {
        if (selectedStudent != null) {
            selectedStudent.markExcused();
            updateStudentDisplay();
            updateStatus(selectedStudent.getName() + " marked as EXCUSED.");
        }
    }
    
    /**
     * Updates the student display after status change.
     */
    private void updateStudentDisplay() {
        displayStudentDetails(selectedStudent);
        studentListView.refresh();
    }
    
    /**
     * Handles playing the student's name audio.
     */
    @FXML
    private void handlePlayName() {
        if (selectedStudent == null) return;
        
        String audioPath = dataService.getStudentAudioPath(selectedStudent.getId());
        if (audioPath != null) {
            try {
                // Stop any currently playing audio
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                
                // Create and play the new audio
                Media media = new Media(new File(audioPath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                
                updateStatus("Playing name pronunciation for " + selectedStudent.getName());
            } catch (Exception e) {
                updateStatus("Error playing audio: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handles saving the attendance records.
     */
    @FXML
    private void handleSave() {
        dataService.saveStudents(new ArrayList<>(students));
        updateStatus("Attendance records saved successfully. " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
