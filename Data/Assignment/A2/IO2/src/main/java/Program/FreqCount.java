package Program;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FreqCount {

    public static void main(String[] args) {

        String appName = "A2"; // Application name
        SparkSession sparkSession = SparkSession.builder().appName(appName).getOrCreate(); // Initialize Spark session

        FileOperations fileOperations = new FileOperations(); // Initialize FileOperations

        // Read news data and stop words from files
        JavaRDD<String> news = fileOperations.getData(sparkSession, "reut2-014.sgm");
        JavaRDD<String> stopWordsList = fileOperations.getStopWords(sparkSession, "StopWords.txt");
        List<String> stopWordList = new ArrayList<>(stopWordsList.collect()); // Collect stop words into a list

        // Clean the text data
        JavaRDD<String> cleanedText = news.flatMap(line -> Arrays.asList(
                line.replaceAll("[^a-zA-Z\\s]", " ") // Replace non-alphabetic characters with space
                        .toLowerCase(Locale.ROOT) // Convert to lowercase
                        .trim() // Trim leading and trailing spaces
                        .split("\\s+") // Split by spaces
        ).iterator());

        // Filter out stop words and non-alphabetic strings
        JavaRDD<String> words = cleanedText
                .filter(s -> !s.isEmpty()) // Remove empty strings
                .filter(s -> !stopWordList.contains(s)) // Remove stop words
                .filter(s -> s.matches("[a-z]+")); // Keep only alphabetic strings

        // Further clean the words
        words = words.map(s -> s.replaceAll("(&lt;)|>|[^\\w]+", "")); // Remove non-word characters

        // Count word frequencies
        JavaPairRDD<String, Integer> wordCounts = words.mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b); // Reduce by key to get word counts

        // Print word counts
        wordCounts.collect().forEach(
                wordCount -> System.out.println("(" + wordCount._1() + ", " + wordCount._2() + ")"));

        // Find the word with maximum frequency
        Tuple2<Integer, String> maxFreqWord = wordCounts.mapToPair(Tuple2::swap)
                .sortByKey(false)
                .take(1)
                .get(0);

        // Find the word with minimum frequency
        Tuple2<Integer, String> minFreqWord = wordCounts.mapToPair(Tuple2::swap)
                .sortByKey(true)
                .take(1)
                .get(0);

        // Print the word with maximum frequency
        System.out.println(
                "Word with maximum frequency :  (" + maxFreqWord._2() + " ," + maxFreqWord._1() + ")");

        // Print the word with minimum frequency
        System.out.println(
                "Word with minimum frequency : (" + minFreqWord._2() + " ," + minFreqWord._1() + ")");

        // Stop the Spark session
        sparkSession.stop();
    }
}