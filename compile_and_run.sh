#!/bin/bash

# Setting up variables
JAVAFX_PATH="/Users/tianlema/java/javafx-sdk" # User needs to update this to their JavaFX SDK path
SRC_DIR="src"
OUTPUT_DIR="bin"
MAIN_CLASS="edu.attendance.AttendanceApplication"

# Create output directory if it doesn't exist
mkdir -p $OUTPUT_DIR

echo "Compiling Java files..."
# Compile Java files
javac --module-path $JAVAFX_PATH/lib \
      --add-modules javafx.controls,javafx.fxml,javafx.media \
      -d $OUTPUT_DIR \
      $(find $SRC_DIR -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed"
    exit 1
fi

echo "Copying resources..."
# Copy resources
mkdir -p $OUTPUT_DIR/resources
cp -R $SRC_DIR/resources/* $OUTPUT_DIR/resources/

echo "Running application..."
# Run the application
java --module-path $JAVAFX_PATH/lib \
     --add-modules javafx.controls,javafx.fxml,javafx.media \
     -cp $OUTPUT_DIR $MAIN_CLASS

echo "Done"
