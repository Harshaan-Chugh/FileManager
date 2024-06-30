import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

/**
   Represents a file with functionality for reading, writing, and managing content.
   @author Harshaan Chugh
*/
@SuppressWarnings("ALL")
public class EditableFile {
   public String path;
   private int wordCount;
   private int charCount;

   /**
      Constructs an EditableFile with the specified fileName in the current directory
      @param fileName (The name of the file to be created or opened)
   */
   public EditableFile(String fileName) {
      String currentDirectory = System.getProperty("user.dir");
      this.path = currentDirectory + File.separator + fileName;
      
      try {
         File file = new File(path);
         if (!file.exists()) //noinspection ResultOfMethodCallIgnored
             file.createNewFile();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      
      updateBothCounts();
   }

   /**
      Accessor method that returns the file path.
      @return path
   */
   public String getFilePath() {
      return path;
   }
   
   /**
      Appends the specified content to the end of the file.
      Updates wordCount and charCount accordingly.
    
      @param contents The content to be written to the file.
    */
   public void write(String contents) {
      //Creates a writer that does not overwrite to the file.
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
         writer.write(contents);
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      
      updateBothCounts();
   }
   
   /**
      Reads the content of the file.
      @return The content of the file as a string
   */
   public String getContent() {
      try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
         StringBuilder content = new StringBuilder();
         String line;
         while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append("\n");
         }
         return content.toString();
      }
      catch (IOException e) { //In case of problem with reading file.
         e.printStackTrace();
         return "";
      }
   }
   
   /**
      Method that returns the word count of an EditableFile.
      @return wordCount
   */
   public int getWordCount() {
      return wordCount;
   }
    
   /**
      Method that returns the character count of an EditableFile
      @return charCount
   */
   public int getCharCount() {
      return charCount;
   }
   
   /**
      Updates both the character and word counts.
   */
   public void updateBothCounts() {
      String input = getContent();
      charCount = input.length() - 1; //-1 for last index
      if (input.isEmpty()) {
            wordCount = 0;
      }
      else {
         String[] words = input.split("\\s+");
         wordCount = words.length;
      }
   }
   
   /*
      @param keyword The keyword to search for in the file content.
      @return true, if the keyword is found else false.

   */
   public boolean hasKeyword(String keyword) {
      String content = getContent();
      return content.contains(keyword);
   }
}