import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class DriverClass {

    private static MongoClient mongoClient; // MongoDB client instance
    private static MongoCollection<Document> collection; // MongoDB collection instance
    private static FileOperations fileOperation; // File operations instance

    public static void main(String[] args) {
        // Initialize FileOperations singleton instance
        fileOperation = FileOperations.getInstance();

        // Get news data from the specified file
        List<News> response = fileOperation.getNews("reut2-014.sgm");

        // Connect to MongoDB
        ConnectMongoDB();

        // Get the database and collection
        MongoDatabase database = mongoClient.getDatabase("ReuterDb");
        collection = database.getCollection("News");

        // Insert data into MongoDB
        insertDataToDB(response);
    }

    // Method to connect to MongoDB using the specified URI
    private static void ConnectMongoDB(){
        MongoClientURI uri = new MongoClientURI("mongodb+srv://root:root@default-cluster.xn53b0c.mongodb.net/?retryWrites=true&w=majority&appName=default-cluster");
        mongoClient = new MongoClient(uri);
    }

    // Method to insert the list of news data into MongoDB
    private static void insertDataToDB(List<News> response){
        List<Document> data = new ArrayList<>();

        // Convert each News object to a MongoDB Document
        for (News currNew : response){
            Document doc = new Document("title", currNew.getTitle())
                    .append("body", currNew.getBody());
            data.add(doc);
        }

        // Insert the list of documents into the collection
        collection.insertMany(data);
        System.out.println("Data Inserted Successfully To MongoDB Database!!");
    }
}