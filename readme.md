
# FileManager

## Description

FileManager is a Java-based file management tool that allows users to create, delete, search, and manage text files. It includes advanced features like word counting with multithreading strategies to ensure efficient and race-condition-free operations.

This tool serves as an easy-to-use solution for performing various file-related operations.

## Getting Started

To use the FileManager program, follow these simple steps:

1. **Compile the Code**
   ```sh
   javac FileManagerTester_Chugh.java
   ```

2. **Run the Program**
   ```sh
   java FileManagerTester_Chugh
   ```
## Usage

1. **Create a New File**
   Select option 1 from the menu, enter the desired file name (including the ".txt" extension), and input the content for the file when prompted. The program will create the file, and you'll receive a success message.

2. **Delete a File**
   Choose option 2, enter the name of the file you want to delete (including the ".txt" extension), and the program will remove the file. You will be notified of the success or failure of the deletion.

3. **Delete Duplicates**
   Option 3 allows you to delete duplicate files based on their contents. This operation cleans up the file manager and provides feedback on the deleted duplicates.

4. **Keyword Search**
   For option 4, enter a keyword, and the program will display a list of files containing that keyword in their content.

5. **View File Statistics**
   Option 5 displays detailed statistics for all files, such as file name, content, word count, and character count.

6. **Word Counting with Multithreading**
   The program can efficiently count the occurrences of each word in a file using multiple threads. This feature uses the `MultithreadedWordCounter` class to read the file, count words, and display the top words. The multithreading strategy reduces race conditions and ensures efficient processing.

7. **Exit**
   Select option 6 to exit the FileManager program.
