package edu.attendance.util;

import edu.attendance.model.Student;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Service class to handle loading and saving student data from/to CSV files.
 */
public class StudentDataService {
    private static final Logger LOGGER = Logger.getLogger(StudentDataService.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Path dataDir;
    private final Path csvFilePath;
    private final Path imagesDir;
    private final Path audioDir;
    
    public StudentDataService() {
        // Get the application's base directory
        String baseDir = System.getProperty("user.dir");
        dataDir = Paths.get(baseDir, "resources", "data");
        csvFilePath = dataDir.resolve("students.csv");
        imagesDir = Paths.get(baseDir, "resources", "images");
        audioDir = Paths.get(baseDir, "resources", "audio");
        
        // Create directories if they don't exist
        createDirectoriesIfNotExist();
    }
    
    private void createDirectoriesIfNotExist() {
        try {
            Files.createDirectories(dataDir);
            Files.createDirectories(imagesDir);
            Files.createDirectories(audioDir);
            
            // Create empty CSV file if it doesn't exist
            if (!Files.exists(csvFilePath)) {
                try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath)) {
                    writer.write("id,name,favorite_quote,times_called,times_present,times_absent,times_excused,last_called_date");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create required directories", e);
            throw new RuntimeException("Failed to initialize data directories", e);
        }
    }
    
    /**
     * Loads students from the CSV file.
     * @return List of students
     */
    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(csvFilePath)) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // Split the line while respecting quoted fields
                    List<String> data = parseCsvLine(line);
                    if (data.size() < 7) {
                        LOGGER.warning("Invalid data format in CSV: " + line);
                        continue;
                    }
                    
                    int id = Integer.parseInt(data.get(0));
                    String name = data.get(1);
                    String quote = data.get(2);
                    int timesCalled = Integer.parseInt(data.get(3));
                    int timesPresent = Integer.parseInt(data.get(4));
                    int timesAbsent = Integer.parseInt(data.get(5));
                    int timesExcused = Integer.parseInt(data.get(6));
                    
                    LocalDate lastCalledDate = null;
                    if (data.size() > 7 && !data.get(7).trim().isEmpty()) {
                        try {
                            lastCalledDate = LocalDate.parse(data.get(7), DATE_FORMATTER);
                        } catch (DateTimeParseException e) {
                            LOGGER.warning("Invalid date format in CSV: " + data.get(7));
                        }
                    }
                    
                    Student student = new Student(id, name, quote, timesCalled, 
                                                 timesPresent, timesAbsent, timesExcused, lastCalledDate);
                    students.add(student);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Error parsing student data: " + line, e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading student data", e);
            throw new RuntimeException("Failed to load student data", e);
        }
        
        return students;
    }
    
    /**
     * Parses a CSV line while respecting quoted fields.
     * @param line The CSV line to parse
     * @return List of parsed fields
     */
    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields;
    }
    
    /**
     * Saves students to the CSV file.
     * @param students List of students to save
     */
    public void saveStudents(List<Student> students) {
        if (students == null) {
            throw new IllegalArgumentException("Students list cannot be null");
        }
        
        // Create a temporary file for atomic write
        Path tempFile = csvFilePath.resolveSibling("students.tmp");
        
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            // Write header
            writer.write("id,name,favorite_quote,times_called,times_present,times_absent,times_excused,last_called_date");
            writer.newLine();
            
            // Write student data
            for (Student student : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(student.getId()).append(",");
                sb.append(escapeCsvField(student.getName())).append(",");
                sb.append(escapeCsvField(student.getFavoriteQuote())).append(",");
                sb.append(student.getTimesCalled()).append(",");
                sb.append(student.getTimesPresent()).append(",");
                sb.append(student.getTimesAbsent()).append(",");
                sb.append(student.getTimesExcused()).append(",");
                
                LocalDate lastCalledDate = student.getLastCalledDate();
                if (lastCalledDate != null) {
                    sb.append(lastCalledDate.format(DATE_FORMATTER));
                }
                
                writer.write(sb.toString());
                writer.newLine();
            }
            
            // Atomically move the temporary file to the target file
            Files.move(tempFile, csvFilePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving student data", e);
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Failed to delete temporary file", ex);
            }
            throw new RuntimeException("Failed to save student data", e);
        }
    }
    
    private String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
    
    /**
     * Gets the image path for a student.
     * @param studentId The student ID
     * @return Path to the student's image or null if not found
     */
    public String getStudentImagePath(int studentId) {
        Path imagePath = imagesDir.resolve(studentId + ".jpg");
        return Files.exists(imagePath) ? imagePath.toString() : null;
    }
    
    /**
     * Gets the audio path for a student's name pronunciation.
     * @param studentId The student ID
     * @return Path to the student's audio file or null if not found
     */
    public String getStudentAudioPath(int studentId) {
        Path audioPath = audioDir.resolve(studentId + ".mp3");
        return Files.exists(audioPath) ? audioPath.toString() : null;
    }
}
