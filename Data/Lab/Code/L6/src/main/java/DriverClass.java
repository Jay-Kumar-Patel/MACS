import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class DriverClass {

    private static MongoClient mongoClient;
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {

        ConnectMongoDB();

        MongoDatabase database = mongoClient.getDatabase("G-Store");
        collection = database.getCollection("Invoice");

        /*

        //Insert
        Invoice invoice = new Invoice();
        invoice.setItem("Laptop");
        invoice.setQuantity(2);
        invoice.setPrice(1200);

        insert(invoice);


        //Get
        get("Laptop");


        //Update
        update("Laptop", 4560);


        //Delete
        delete("Laptop");


        */

    }

    private static void ConnectMongoDB(){
        MongoClientURI uri = new MongoClientURI("mongodb+srv://root:root@default-cluster.xn53b0c.mongodb.net/?retryWrites=true&w=majority&appName=default-cluster");
        mongoClient = new MongoClient(uri);
    }

    private static void insert(Invoice invoice){
        Document doc = new Document("item", invoice.getItem())
                .append("quantity", invoice.getQuantity())
                        .append("price", invoice.getPrice());

        collection.insertOne(doc);

        ObjectId id = doc.getObjectId("_id");
        System.out.println("New Item Inserted ID: " + id.toHexString());
    }

    private static void update(String invoiceName, int updatePrice){
        Document foundItem = collection.find(eq("item", invoiceName)).first();
        if (foundItem != null) {
            collection.updateOne(eq("item", invoiceName), new Document("$set", new Document("price", updatePrice)));
            get(invoiceName);
        } else {
            System.out.println("Item not found, so it can't be updated!");
        }
    }

    private static void get(String invoiceName){
        Document doc = collection.find(eq("item", invoiceName)).first();
        if (doc != null) {
            Invoice invoice = new Invoice(doc.getString("item"), doc.getInteger("quantity"), doc.getInteger("price"));
            System.out.println("Item: " + invoice.getItem() + ", Quantity: " + invoice.getQuantity() + ", Price: " + invoice.getPrice());
        }
        else {
            System.out.println("Item not found!");
        }
    }

    private static void delete(String invoiceName){
        Document foundItem = collection.find(eq("item", invoiceName)).first();
        if (foundItem != null) {
            ObjectId id = foundItem.getObjectId("_id");
            collection.deleteOne(eq("_id", id));
            System.out.println("Item deleted successfully!");
        } else {
            System.out.println("Item not found, so it can't be deleted!");
        }
    }
}
