import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperations {

    private static FileOperations fileOperation;

    // Singleton pattern implementation to get a single instance of FileOperations
    public static FileOperations getInstance() {
        if (fileOperation == null) {
            // Synchronized block to ensure only one thread creates the instance
            synchronized (FileOperations.class) {
                if (fileOperation == null) {
                    fileOperation = new FileOperations();
                }
            }
        }
        return fileOperation;
    }

    // Method to get Bag of Words from the file
    public List<String[]> getBOW(){
        StringBuilder rawData = readFile("reut2-014.sgm"); // Read the raw file data
        List<String> fetchInfo = getInfoFromRawData(rawData); // Extract information using regex
        return filterData(fetchInfo); // Filter and clean the data
    }

    // Method to get words from a specified file (e.g., positive or negative words)
    public List<String> getWords(String filepath){
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line); // Add each line to the list of words
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Handle exceptions
        }
        return words;
    }

    // Method to read the file content into a StringBuilder
    private StringBuilder readFile(String filepath){
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line); // Append each line to the StringBuilder
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Handle exceptions
        }
        return content;
    }

    // Method to extract information from raw data using regex
    private List<String> getInfoFromRawData(StringBuilder content) {
        List<String> results = new ArrayList<>();
        String regex = "<REUTERS.*?>(.*?)</REUTERS>"; // Regex pattern to find REUTERS tags
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            results.add(matcher.group(1)); // Add the matched content to results
        }
        return results;
    }

    // Method to filter and clean data
    private List<String[]> filterData(List<String> info){
        List<String[]> response = new ArrayList<>();

        for (String currInfo : info) {
            String title = getTitleAndBody(currInfo, "TITLE"); // Extract title from the content
            String filteredData = cleanTitleAndBody(title); // Clean the extracted title
            if (filteredData != null){
                String[] words = filteredData.split(" "); // Split the cleaned title into words
                response.add(words); // Add the words array to the response
            }
        }

        return response;
    }

    // Method to extract specific tags (e.g., TITLE) from the content using regex
    private String getTitleAndBody(String content, String tagName) {
        String regex = "<" + tagName + ".*?>(.*?)</" + tagName + ">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1); // Return the matched content
        }
        return null;
    }

    // Method to clean the extracted title and body content
    private String cleanTitleAndBody(String content) {
        if(content == null)
            return content;
        return content.replaceAll("[^a-zA-Z0-9\\s]", "") // Remove non-alphanumeric characters
                .replace("&lt;", "").replace(">", "") // Replace specific characters
                .replaceAll("\\s+", " ").trim(); // Replace multiple spaces with a single space and trim
    }
}