package Spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

import java.util.List;

public class DriverClass {

    public static void main(String[] args) {

        String appName = "lab-5";

        SparkSession sparkSession = SparkSession.builder().appName(appName).getOrCreate();

        String filePath = "input.txt";

        Dataset<String> fileData = sparkSession.read().option("multiline", false).textFile(filePath);

        fileData.show();

        List<String> lines = fileData.collectAsList();

        int sum = 0;
        for (String line : lines){
            String[] numbers= line.split(",");
            for (String currNum : numbers){
                sum += Integer.parseInt(currNum);
            }
        }

        System.out.println("Sum is : " + sum);

        sparkSession.stop();
    }

}
