# SmartTaskManager

A simple command-line task management application written in Java.

## Features
- Add new tasks with categories and priorities
- View all tasks
- Delete tasks
- Simple command-line interface

## For Users - How to Run

### Option 1: Download JAR (Recommended)
1. Go to the [Releases](https://github.com/amandeepc0des/SmartTaskManager/releases) page
2. Download the latest `SmartTaskManager.jar` file
3. Make sure you have Java installed on your computer
4. Open terminal/command prompt and run:
   ```bash
   java -jar SmartTaskManager.jar
   ```

### Option 2: Check Java Installation
If you don't have Java installed:
- **Windows**: Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
- **Mac**: `brew install openjdk` or download from the links above
- **Linux**: `sudo apt install openjdk-11-jdk` (Ubuntu/Debian) or equivalent for your distro

## For Developers

### Building from Source
1. Clone the repository:
   ```bash
   git clone https://github.com/amandeepc0des/SmartTaskManager.git
   cd SmartTaskManager
   ```

2. Compile and build:
   ```bash
   # On Windows
   build.bat
   
   # On Linux/Mac
   mkdir -p out
   javac -d out src/com/taskmanager/*.java src/com/taskmanager/model/*.java src/com/taskmanager/model/enums/*.java src/com/taskmanager/service/*.java
   jar cfe SmartTaskManager.jar com.taskmanager.Main -C out .
   ```

3. Run:
   ```bash
   java -jar SmartTaskManager.jar
   ```

## Usage
Once you run the application, you'll see a menu with options:
1. Add Task - Create a new task
2. View Tasks - Display all tasks
3. Delete Task - Remove a task
4. Exit - Close the application

## Requirements
- Java 8 or higher
- No additional dependencies required