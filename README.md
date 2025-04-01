# Classroom Attendance System

A simple JavaFX application for tracking classroom attendance and enabling instructors to randomly select students for participation, with features designed to ensure fair selection over time.

## Overview

This application helps instructors manage classroom attendance by:
- Randomly selecting students based on a weighted probability system
- Tracking student attendance records (present, absent, excused)
- Displaying student information including photos and favorite quotes
- Playing audio pronunciation of student names
- Ensuring all students get a chance to participate over time

## Requirements

- Java 11 or higher
- JavaFX SDK (version 11 or higher)

## Project Structure

```
src/
├── edu/attendance/
│   ├── model/           # Data models
│   │   └── Student.java
│   ├── controller/      # UI controllers
│   │   └── AttendanceController.java
│   ├── util/            # Utility classes
│   │   └── StudentDataService.java
│   └── AttendanceApplication.java  # Main application class
│
├── resources/
│   ├── data/            # CSV data files
│   │   └── students.csv
│   ├── fxml/            # UI layout files
│   │   └── AttendanceApp.fxml
│   ├── css/             # Styling files
│   │   └── attendance.css
│   ├── images/          # Student photos (named as [id].jpg)
│   └── audio/           # Name pronunciation audio files (named as [id].mp3)
```

## Setup Instructions

1. Download and install Java JDK 11 or higher
2. Download JavaFX SDK from https://gluonhq.com/products/javafx/
3. Unzip the JavaFX SDK to a location on your computer
4. Update the `JAVAFX_PATH` in the compile script:
   - For Windows: Edit `compile_and_run.bat`
   - For macOS/Linux: Edit `compile_and_run.sh`
5. Run the appropriate script for your operating system to compile and run the application

## Features

- **Random Student Selection**: Algorithm prioritizes students who have been called less frequently or have been absent
- **Attendance Tracking**: Mark students as present, absent, or excused
- **Student Visualization**: Display student photos and favorite quotes
- **Name Pronunciation**: Play audio files for correct name pronunciation
- **Data Persistence**: Save attendance records to CSV files

## Design and Development Process

### 1. Problem Analysis

The problem is centered around classroom participation and attendance tracking:
- Instructors need to call on students fairly
- Students who miss class should be caught up
- Some students might require excused absences
- Keeping track of participation manually is difficult

### 2. Conceptual Design

We identified key entities and behaviors:
- **Student**: The primary data entity with attributes like name, quote, attendance history
- **Selection Algorithm**: Determines which students should be called more frequently
- **UI Components**: How the instructor will interact with the system
- **Data Storage**: How to persist attendance records between sessions

### 3. Architectural Design

The application follows the MVC (Model-View-Controller) pattern:
- **Model**: `Student` class represents student data
- **View**: FXML and CSS files define the UI
- **Controller**: `AttendanceController` manages user interactions

### 4. Implementation

The code implementation demonstrates several Java programming concepts:
- Object-oriented design
- File I/O for data persistence
- JavaFX for GUI development
- Event-driven programming
- Collection manipulation

## How to Use

1. **Start the Application**: Run the application using the provided scripts
2. **View Students**: The left panel shows the list of all students
3. **Random Selection**: Click "Random Selection" to select a student based on the weighted algorithm
4. **Mark Attendance**: When a student is selected, mark them as:
   - Present: Student is in class and has responded
   - Absent: Student is not in class
   - Excused: Student has a valid reason for absence
5. **Play Name**: If audio is available, click "Play Name" to hear the pronunciation
6. **Save Records**: Click "Save Records" to persist the attendance data

## Educational Value

This project demonstrates several important programming concepts:
- **Object-Oriented Programming**: Classes, inheritance, encapsulation
- **File I/O**: Reading and writing CSV files
- **Data Structures**: Using collections like Lists
- **GUI Programming**: JavaFX, FXML, CSS
- **Event Handling**: Responding to user actions
- **Probability and Algorithms**: Weighted selection algorithm

## Expansion Ideas

Students can enhance this project by:
1. Adding authentication for instructors
2. Creating a student view that shows only the selected student
3. Implementing statistical reports on attendance patterns
4. Adding export functionality for attendance reports
5. Creating a calendar view for tracking attendance over time
6. Implementing a web-based version using JavaFX WebView or as a separate web application
