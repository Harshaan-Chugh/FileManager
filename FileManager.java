import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
     The FileManager class manages a list of EditableFile objects and provides
     various file-related operations such as creating, deleting, searching, and
     managing files.

    @author Harshaan Chugh
 */
public class FileManager {
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());
    LinkedList<EditableFile> files;

    public FileManager(LinkedList<EditableFile> files) {
        this.files = files;
        loadFilesFromDirectory();
    }

    private void loadFilesFromDirectory() {
        File cd = new File(System.getProperty("user.dir"));
        File[] txtFiles = cd.listFiles((_, name) -> name.toLowerCase().endsWith(".txt"));
        if (txtFiles != null) {
            for (File txtFile : txtFiles) {
                String fileName = txtFile.getName();
                String contents = readContentsFromFile(txtFile);
                createFile(fileName, contents, false);
            }
        }
    }

    private String readContentsFromFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file: " + file.getName(), e);
        }
        return content.toString();
    }

    public void createFile(String fileName, String contents, boolean writeToDisk) {
        if (!containsFileWithName(files, fileName)) {
            EditableFile newFile = new EditableFile(fileName);
            if (writeToDisk) {
                newFile.write(contents);
            }
            files.add(newFile);
            logger.log(Level.INFO, "File created successfully: " + fileName);
        }
        else {
            logger.log(Level.WARNING, "File with the same name already exists: " + fileName);
        }
    }

    private boolean containsFileWithName(LinkedList<EditableFile> fileList, String fileName) {
        for (EditableFile file : fileList) {
            if (file.getFilePath().endsWith(fileName)) {
                return true;
            }
        }
        return false;
    }

    public void deleteFile(EditableFile file) {
        deleteFileFromOS(file);
        files.remove(file);
    }

    private void deleteFileFromOS(EditableFile editableFile) {
        File file = new File(editableFile.getFilePath());
        if (file.exists()) {
            if (file.delete()) {
                logger.log(Level.INFO, "File deleted successfully: " + getFileName(editableFile.getFilePath()));
            }
            else {
                logger.log(Level.SEVERE, "Failed to delete file: " + getFileName(editableFile.getFilePath()));
            }
        }
        else {
            logger.log(Level.WARNING, "File not found: " + getFileName(editableFile.getFilePath()));
        }
    }

    private String getFileName(String path) {
        int lastSeparatorIndex = path.lastIndexOf(File.separator);
        return (lastSeparatorIndex == -1) ? path : path.substring(lastSeparatorIndex + 1);
    }

    public LinkedList<String> keywordSearch(String keyword) {
        LinkedList<String> matchingFiles = new LinkedList<>();

        for (EditableFile file : files) {
            if (file.hasKeyword(keyword)) {
                matchingFiles.add(getFileName(file.getFilePath()));
            }
        }

        if (matchingFiles.isEmpty()) {
            logger.log(Level.INFO, "No files found with the keyword: " + keyword);
        }

        return matchingFiles;
    }

    public void deleteDuplicates() {
        LinkedList<EditableFile> uniqueFiles = new LinkedList<>();
        for (EditableFile file : files) {
            if (!containsFileWithContent(uniqueFiles, file.getContent())) {
                uniqueFiles.add(file);
            }
            else {
                deleteFileFromOS(file);
            }
        }
        files = uniqueFiles;
        logger.log(Level.INFO, "Duplicate files deleted.");
    }

    public boolean containsFileWithContent(LinkedList<EditableFile> fileList, String content) {
        for (EditableFile file : fileList) {
            if (file.getContent().equals(content)) {
                return true;
            }
        }
        return false;
    }
}