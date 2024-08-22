import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperations {

    private static FileOperations fileOperation;

    public static FileOperations getInstance() {
        if (fileOperation == null) {
            //Use Synchronized, so that in case of multithreading or multiple user create object at the same time
            // only one thread goes inside this block.
            synchronized (FileOperations.class) {
                if (fileOperation == null){
                    //Create one time object.
                    fileOperation = new FileOperations();
                }
            }
        }
        return fileOperation;
    }


    // Method to get news data from the specified file path
    public List<News> getNews(String filepath){
        // Read the file and get its raw data
        StringBuilder rawData = readFile(filepath);

        // If raw data is null, return null
        if (rawData == null){
            return null;
        }

        // Extract information from the raw data
        List<String> fetchInfo = getInfoFromRawData(rawData);

        // Filter the extracted information and return it as a list of News objects
        return filterData(fetchInfo);
    }

    // Method to read the file and return its content as a StringBuilder
    private StringBuilder readFile(String filepath){
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            // Read each line from the file and append it to the content
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            return content;
        } catch (IOException e) {
            // Print the exception message if an error occurs
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Method to extract information from the raw data using regex
    private List<String> getInfoFromRawData(StringBuilder content) {
        List<String> results = new ArrayList<>();
        String regex = "<REUTERS.*?>(.*?)</REUTERS>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        // Find all matches and add them to the results list
        while (matcher.find()) {
            results.add(matcher.group(1));
        }
        return results;
    }

    // Method to filter the extracted information and convert it to a list of News objects
    private List<News> filterData(List<String> info){
        List<News> response = new ArrayList<>();

        // Iterate over each extracted information
        for (String currInfo : info) {
            News currNew = new News();
            // Get the title and body from the current information
            String title = getTitleAndBody(currInfo, "TITLE");
            String body = getTitleAndBody(currInfo, "BODY");
            // If title or body is not null, clean and set them in the News object
            if (title != null || body != null) {
                currNew.setTitle(cleanTitleAndBody(title));
                currNew.setBody(cleanTitleAndBody(body));
                response.add(currNew);
            }
        }

        return response;
    }

    // Method to get the content inside a specified tag from the given content
    private String getTitleAndBody(String content, String tagName) {
        String regex = "<" + tagName + ".*?>(.*?)</" + tagName + ">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        // Return the content inside the tag if found
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Method to clean the extracted title and body content
    private String cleanTitleAndBody(String content) {
        if(content == null)
            return content;
        return content.replaceAll("[^a-zA-Z0-9\\s]", "") // Remove non-alphanumeric characters
                .replace("&lt;", "").replace(">", "") // Remove HTML entities and tags
                .replaceAll("\\s+", " ").trim(); // Replace multiple spaces with a single space and trim
    }

}
