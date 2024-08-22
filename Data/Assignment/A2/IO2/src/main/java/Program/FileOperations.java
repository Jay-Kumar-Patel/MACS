package Program;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class FileOperations {

    // Method to read data from a file and return it as a JavaRDD
    public JavaRDD<String> getData(SparkSession sparkSession, String filePath){
        Dataset<String> fileData = sparkSession.read().option("multiline", false).textFile(filePath);
        // Print the content of the file to the console
        System.out.println("Content of File: ");
        fileData.show(); // Show the content of the file
        return fileData.javaRDD(); // Return the file data as a JavaRDD
    }

    // Method to read stop words from a file and return them as a JavaRDD
    public JavaRDD<String> getStopWords(SparkSession sparkSession, String filePath){
        Dataset<String> fileData = sparkSession.read().option("multiline", false).textFile(filePath);
        // Print the stop words to the console
        System.out.println("Stop Words: ");
        fileData.show(); // Show the stop words
        return fileData.javaRDD(); // Return the stop words as a JavaRDD
    }
}