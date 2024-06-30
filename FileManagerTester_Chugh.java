import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/*
    @author Harshaan Chugh
*/

public class FileManagerTester_Chugh {
    public static void main(String[] args) {
        LinkedList<EditableFile> files = new LinkedList<>();
        FileManager fileManager = new FileManager(files);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Note: JGrasp doesn't refresh files until the end.");
        System.out.println("VSCode is Recommended.");
        boolean exit = false;
        while (!exit) {
            System.out.println("\nFile Manager Menu:");
            System.out.println("1. Create a new file");
            System.out.println("2. Delete a file");
            System.out.println("3. Delete duplicates");
            System.out.println("4. Keyword search");
            System.out.println("5. View File Statistics");
            System.out.println("6. Count Words in a File");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("Invalid choice. Enter a number between 1 and 7.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter the file name (with .txt): ");
                    String fileName = scanner.nextLine();
                    System.out.print("Enter the file content: ");
                    String fileContent = scanner.nextLine();
                    fileManager.createFile(fileName, fileContent, true);
                    break;

                case 2:
                    System.out.print("Enter the file name to delete: ");
                    String fileToDelete = scanner.nextLine();
                    EditableFile file = findFileByName(files, fileToDelete);
                    if (file != null) {
                        fileManager.deleteFile(file);
                        System.out.println("File deleted successfully.");
                    } else {
                        System.out.println("File not found.");
                    }
                    break;

                case 3:
                    fileManager.deleteDuplicates();
                    System.out.println("Duplicate files deleted.");
                    break;

                case 4:
                    System.out.print("Enter the keyword to search: ");
                    String keyword = scanner.nextLine();
                    String filePath = printList(fileManager.keywordSearch(keyword));
                    if (!filePath.isEmpty()) {
                        System.out.println("Files that are a match: " + filePath);
                    } else {
                        System.out.println("File not found.");
                    }
                    break;

                case 5:
                    viewAllFiles(fileManager);
                    break;

                case 6:
                    System.out.print("Enter the file name to count words: ");
                    String fileToCount = scanner.nextLine();
                    System.out.print("Enter the number of threads to use: ");
                    int numThreads = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    MultithreadedWordCounter wordCounter = new MultithreadedWordCounter();
                    wordCounter.countWords(fileToCount, numThreads);
                    break;

                case 7:
                    exit = true;
                    System.out.println("Exiting File Manager. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Enter a number between 1 and 7.");
                    break;
            }
        }

        scanner.close();
    }

    private static EditableFile findFileByName(LinkedList<EditableFile> files, String fName) {
        for (EditableFile file : files) {
            if (file.getFilePath().endsWith(fName)) {
                return file;
            }
        }
        return null;
    }

    private static void viewAllFiles(FileManager fileManager) {
        System.out.println("\nList of Files:");
        for (EditableFile file : fileManager.files) {
            System.out.println(">File Name: " + getFileName(file.getFilePath()));
            System.out.print(">File Content: " + file.getContent());
            System.out.print(">File WordCount, CharCount: " + file.getWordCount());
            System.out.println(", " + file.getCharCount());
            System.out.println("---------------------------");
        }
    }

    private static String printList(LinkedList<String> list) {
        StringBuilder output = new StringBuilder();

        for (String s : list) {
            output.append(s).append(" ");
        }

        if (!output.isEmpty()) {
            return output.substring(0, output.length() - 1);
        } else {
            return "";
        }
    }

    private static String getFileName(String path) {
        int lastSeparatorIndex = path.lastIndexOf(File.separator);
        return (lastSeparatorIndex == -1) ? path : path.substring(lastSeparatorIndex + 1);
    }
}