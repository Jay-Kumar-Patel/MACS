import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BOW {

    private static MongoClient mongoClient;
    private static MongoCollection<Document> collection;
    private static Calculations calculations;

    // Method to connect to MongoDB using a MongoDB URI
    private static void connectMongoDB() {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://root:root@default-cluster.xn53b0c.mongodb.net/?retryWrites=true&w=majority&appName=default-cluster");
        mongoClient = new MongoClient(uri);
    }

    public static void main(String[] args) {
        FileOperations fileOperation = FileOperations.getInstance(); // Get instance of FileOperations
        List<String[]> data = fileOperation.getBOW(); // Get Bag of Words data
        List<String> negativeWords = fileOperation.getWords("negativeWords.txt"); // Get negative words
        List<String> positiveWords = fileOperation.getWords("positiveWords.txt"); // Get positive words
        calculations = new Calculations(); // Initialize calculations

        // Calculate sentiments using the obtained data and words
        List<TitleModel> response = calculations.calculateSentiments(data, negativeWords, positiveWords);
        Workbook workbook = createExcel(response); // Create an Excel workbook from the sentiments

        connectMongoDB(); // Connect to MongoDB
        MongoDatabase database = mongoClient.getDatabase("SentimentAnalysis"); // Get the database
        collection = database.getCollection("Sentiments"); // Get the collection
        uploadExcelSheet(workbook); // Upload the Excel sheet data to MongoDB
    }

    // Method to create an Excel workbook from a list of sentiments
    private static Workbook createExcel(List<TitleModel> sentiments) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sentiments");

        // Define the headers for the Excel sheet
        String[] headers = {"ID", "Title", "Match", "Positive Match", "Negative Match", "Score", "Polarity"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Populate the Excel sheet with sentiment data
        for (TitleModel sentiment : sentiments) {
            Row row = sheet.createRow(sentiment.getId());
            row.createCell(0).setCellValue(sentiment.getId());
            row.createCell(1).setCellValue(sentiment.getTitle());
            row.createCell(2).setCellValue(String.join(", ", sentiment.getMatch()));
            row.createCell(3).setCellValue(String.join(", ", sentiment.getPositiveMatch()));
            row.createCell(4).setCellValue(String.join(", ", sentiment.getNegativeMatch()));
            row.createCell(5).setCellValue(sentiment.getScore());
            row.createCell(6).setCellValue(sentiment.getPolarity().toString());
        }

        return workbook; // Return the created workbook
    }

    // Method to upload the Excel sheet data to MongoDB
    private static void uploadExcelSheet(Workbook workbook) {
        List<Document> documents = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                // Create a MongoDB document for each row in the Excel sheet
                Document doc = new Document()
                        .append("ID", (int) row.getCell(0).getNumericCellValue())
                        .append("Title", row.getCell(1).getStringCellValue())
                        .append("Match", row.getCell(2).getStringCellValue())
                        .append("Positive Match", row.getCell(3).getStringCellValue())
                        .append("Negative Match", row.getCell(4).getStringCellValue())
                        .append("Score", row.getCell(5).getStringCellValue())
                        .append("Polarity", row.getCell(6).getStringCellValue());

                documents.add(doc); // Add the document to the list
            }
        }
        collection.insertMany(documents); // Insert all documents into the MongoDB collection
        System.out.println("Data Inserted Successfully To MongoDB Database!!");
    }
}