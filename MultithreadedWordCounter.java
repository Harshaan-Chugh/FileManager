import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for counting words in a multithreaded manner.
 * It reads a file, counts the occurrences of each word, and displays the top words.
 */
public class MultithreadedWordCounter {
    private static final Logger logger = Logger.getLogger(MultithreadedWordCounter.class.getName());
    private final ConcurrentHashMap<String, Integer> wordCounts = new ConcurrentHashMap<>();

    /**
     * Counts the occurrences of words in the specified file using multiple threads.
     * @param fileName the name of the file to read
     * @param numThreads the number of threads to use for counting
     */
    public void countWords(String fileName, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String finalLine = line;
                executor.submit(() -> countWordsInLine(finalLine));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read the file: " + fileName, e);
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        displayTopWords();
    }

    /**
     * Counts the occurrences of words in the given line.
     * Each word is converted to lowercase and non-alphabetic characters are removed.
     * The word count is updated in the concurrent map.
     *
     * @param line the line of text to process
     */
    private void countWordsInLine(String line) {
        line = line.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
        String[] words = line.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                wordCounts.merge(word, 1, Integer::sum);
            }
        }
    }

    /**
     * Displays the top 10 words and their counts.
     * If there are fewer than 10 unique words, all words are displayed.
     * The words are sorted by their counts in descending order.
     */
    private void displayTopWords() {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCounts.entrySet());

        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        int topN = Math.min(10, entryList.size());
        System.out.println("Top " + topN + " Words:");
        for (int i = 0; i < topN; i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}