# Classroom Attendance System

A JavaFX application for tracking classroom attendance and enabling instructors to randomly select students for participation, with features designed to ensure fair selection over time.

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
.
├── src/
│   └── edu/attendance/
│       ├── model/           # Data models
│       │   └── Student.java
│       ├── controller/      # UI controllers
│       │   └── AttendanceController.java
│       ├── util/            # Utility classes
│       │   └── StudentDataService.java
│       └── AttendanceApplication.java  # Main application class
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
│
├── bin/                 # Compiled classes and resources
├── compile_and_run.sh   # Unix/Linux build script
└── compile_and_run.bat  # Windows build script
```

## Setup Instructions

1. Download and install Java JDK 11 or higher
2. Download JavaFX SDK from https://gluonhq.com/products/javafx/
3. Unzip the JavaFX SDK to a location on your computer
4. Update the JavaFX path in the appropriate build script:
   - For Windows: Edit `compile_and_run.bat` and set `JAVAFX_PATH` to your JavaFX SDK path
   - For macOS/Linux: Edit `compile_and_run.sh` and set `JAVAFX_PATH` to your JavaFX SDK path
5. Run the appropriate script for your operating system:
   - Windows: Double-click `compile_and_run.bat` or run from command prompt
   - macOS/Linux: Open terminal and run `./compile_and_run.sh`

## Features

- **Random Student Selection**: Algorithm prioritizes students who have been called less frequently or have been absent
- **Attendance Tracking**: Mark students as present, absent, or excused
- **Student Visualization**: Display student photos and favorite quotes
- **Name Pronunciation**: Play audio files for correct name pronunciation
- **Data Persistence**: Save attendance records to CSV files
- **Modern UI**: Clean and intuitive interface with responsive design

## Technical Details

### Data Storage
- Student data is stored in CSV format in `resources/data/students.csv`
- Each student has a unique ID used for their photo and audio files
- Photos are stored as JPG files in `resources/images/`
- Audio files are stored as MP3 files in `resources/audio/`

### Selection Algorithm
The random selection algorithm uses a weighted probability system that:
- Gives higher priority to students who have been called less frequently
- Increases probability for students who have been absent
- Reduces probability for students called recently
- Ensures fair participation over time

### Error Handling
- Robust file operations with atomic writes
- Proper CSV field escaping
- Comprehensive error logging
- Graceful handling of missing resources

## Development

### Building from Source
1. Clone the repository
2. Set up Java and JavaFX as described in Setup Instructions
3. Run the appropriate build script
4. The compiled application will be in the `bin` directory

### Adding New Students
1. Add student data to `resources/data/students.csv`
2. Add student photo as `[id].jpg` to `resources/images/`
3. Add name pronunciation as `[id].mp3` to `resources/audio/`

## Troubleshooting

### Common Issues
1. **JavaFX not found**: Ensure JavaFX path is correctly set in build script
2. **Missing resources**: Check that all required files are in the correct locations
3. **File permissions**: Ensure write permissions for the resources directory
4. **Audio not playing**: Verify audio files are valid MP3 format

### Logging
The application logs errors and warnings to help diagnose issues:
- Severe errors are logged with full stack traces
- Warnings are logged for data parsing issues
- File operation errors are logged with details

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
