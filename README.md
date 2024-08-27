# Java Game Project

## Description
This is a simple Java-based game where the player moves a square to collect items while avoiding enemies. 
The game includes a home screen where you can enter your name, select the starting level, and view the leaderboard.

## Features
- Home screen with name entry and level selection.
- Leaderboard that saves and displays scores.
- Increasing difficulty with more enemies as the game progresses.

## Project Structure
- `src/main/java`: Contains the Java source code.
- `src/main/resources`: Resources like images, sounds, etc. (currently empty).
- `src/test/java`: Placeholder for any test classes you may add in the future.
- `docs`: Documentation for the project.
- `bin`: Compiled class files (not committed to version control).

## How to Build and Run
1. **Compile the Java files**:
   ```
   javac -d bin src/main/java/*.java
   ```
2. **Create the JAR file**:
   ```
   jar cvfm MyGame.jar docs/Manifest.txt -C bin .

   ```
3. **Run the game**:
   ```
   java -jar MyGame.jar
   ```

## Author
- Paganelli UMBERTO
