package edu.attendance.util;

import edu.attendance.model.Student;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service class to handle loading and saving student data from/to CSV files.
 */
public class StudentDataService {
    private static final String CSV_FILE_PATH = "src/resources/data/students.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Loads students from the CSV file.
     * @return List of students
     */
    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        Path path = Paths.get(CSV_FILE_PATH);
        
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String quote = data[2];
                int timesCalled = Integer.parseInt(data[3]);
                int timesPresent = Integer.parseInt(data[4]);
                int timesAbsent = Integer.parseInt(data[5]);
                int timesExcused = Integer.parseInt(data[6]);
                
                LocalDate lastCalledDate = null;
                if (data.length > 7 && !data[7].trim().isEmpty()) {
                    lastCalledDate = LocalDate.parse(data[7], DATE_FORMATTER);
                }
                
                Student student = new Student(id, name, quote, timesCalled, 
                                             timesPresent, timesAbsent, timesExcused, lastCalledDate);
                students.add(student);
            }
        } catch (IOException e) {
            System.err.println("Error reading student data: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * Saves students to the CSV file.
     * @param students List of students to save
     */
    public void saveStudents(List<Student> students) {
        Path path = Paths.get(CSV_FILE_PATH);
        
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            // Write header
            writer.write("id,name,favorite_quote,times_called,times_present,times_absent,times_excused,last_called_date");
            writer.newLine();
            
            // Write student data
            for (Student student : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(student.getId()).append(",");
                sb.append(student.getName()).append(",");
                sb.append(student.getFavoriteQuote()).append(",");
                sb.append(student.getTimesCalled()).append(",");
                sb.append(student.getTimesPresent()).append(",");
                sb.append(student.getTimesAbsent()).append(",");
                sb.append(student.getTimesExcused()).append(",");
                
                if (student.getLastCalledDate() != null) {
                    sb.append(student.getLastCalledDate().format(DATE_FORMATTER));
                }
                
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving student data: " + e.getMessage());
        }
    }
    
    /**
     * Gets the image path for a student.
     * @param studentId The student ID
     * @return Path to the student's image or null if not found
     */
    public String getStudentImagePath(int studentId) {
        String imagePath = "src/resources/images/" + studentId + ".jpg";
        File imageFile = new File(imagePath);
        
        if (imageFile.exists()) {
            return imagePath;
        } else {
            return null;
        }
    }
    
    /**
     * Gets the audio path for a student's name pronunciation.
     * @param studentId The student ID
     * @return Path to the student's audio file or null if not found
     */
    public String getStudentAudioPath(int studentId) {
        String audioPath = "src/resources/audio/" + studentId + ".mp3";
        File audioFile = new File(audioPath);
        
        if (audioFile.exists()) {
            return audioPath;
        } else {
            return null;
        }
    }
}
