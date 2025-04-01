package edu.attendance.model;

import java.time.LocalDate;

/**
 * Represents a student in the attendance system.
 */
public class Student {
    private int id;
    private String name;
    private String favoriteQuote;
    private int timesCalled;
    private int timesPresent;
    private int timesAbsent;
    private int timesExcused;
    private LocalDate lastCalledDate;

    public Student(int id, String name, String favoriteQuote) {
        this.id = id;
        this.name = name;
        this.favoriteQuote = favoriteQuote;
        this.timesCalled = 0;
        this.timesPresent = 0;
        this.timesAbsent = 0;
        this.timesExcused = 0;
    }

    public Student(int id, String name, String favoriteQuote, int timesCalled, 
                  int timesPresent, int timesAbsent, int timesExcused, LocalDate lastCalledDate) {
        this.id = id;
        this.name = name;
        this.favoriteQuote = favoriteQuote;
        this.timesCalled = timesCalled;
        this.timesPresent = timesPresent;
        this.timesAbsent = timesAbsent;
        this.timesExcused = timesExcused;
        this.lastCalledDate = lastCalledDate;
    }

    /**
     * Calculates the selection probability for this student based on their history.
     * Students who have been called less frequently or who have been absent have a higher probability.
     * @return A value representing the relative probability (higher is more likely to be selected)
     */
    public double getSelectionProbability() {
        // Base probability
        double probability = 1.0;
        
        // Decrease probability if called recently
        if (timesCalled > 0) {
            probability *= (1.0 / Math.sqrt(timesCalled));
        } else {
            // Boost probability if never called
            probability *= 2.0;
        }
        
        // Boost probability for students who have been absent
        if (timesAbsent > 0) {
            probability *= (1.0 + (0.5 * timesAbsent));
        }
        
        // Reduce probability for recently called students
        if (lastCalledDate != null) {
            long daysSinceLastCall = LocalDate.now().toEpochDay() - lastCalledDate.toEpochDay();
            if (daysSinceLastCall < 7) {
                probability *= (daysSinceLastCall / 7.0);
            }
        }
        
        return probability;
    }

    public void markPresent() {
        this.timesCalled++;
        this.timesPresent++;
        this.lastCalledDate = LocalDate.now();
    }

    public void markAbsent() {
        this.timesCalled++;
        this.timesAbsent++;
        this.lastCalledDate = LocalDate.now();
    }

    public void markExcused() {
        this.timesCalled++;
        this.timesExcused++;
        this.lastCalledDate = LocalDate.now();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFavoriteQuote() {
        return favoriteQuote;
    }

    public int getTimesCalled() {
        return timesCalled;
    }

    public int getTimesPresent() {
        return timesPresent;
    }

    public int getTimesAbsent() {
        return timesAbsent;
    }

    public int getTimesExcused() {
        return timesExcused;
    }

    public LocalDate getLastCalledDate() {
        return lastCalledDate;
    }

    public void setLastCalledDate(LocalDate lastCalledDate) {
        this.lastCalledDate = lastCalledDate;
    }

    @Override
    public String toString() {
        return name;
    }
}
