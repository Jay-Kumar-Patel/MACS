import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection localConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/locallab4", "root",
                    "root");

            Connection remoteConnection = DriverManager.getConnection("jdbc:mysql://104.155.137.199:3306/remotelab4", "root",
                    "Jay@4112000");

            showInventory(remoteConnection);

            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter userId : ");
            int userID = sc.nextInt();
            sc.nextLine();

            System.out.println("Please enter item name you want to buy : ");
            String itemName = sc.nextLine();

            System.out.println("Please enter Quantity of item name you want to buy : ");
            int itemQuantity = sc.nextInt();

            updateOrderInfo(localConnection, userID, itemName, itemQuantity);
            updateInventory(remoteConnection, itemQuantity, itemName);
        } catch (Exception e) {
            System.out.println("EXCEPTION?" + e.getMessage());
        }

    }

    static void showInventory(Connection conn) {
        try {
            String query = "select * from Inventory";
            long startTime = System.currentTimeMillis();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getString("item_id") + " | " + rs.getString("item_name") + " | " + rs.getString("available_quantity"));
                System.out.println(" ");
            }
            long stopTime = System.currentTimeMillis();

            System.out.println("Taken time for Fetching remote table Inventory : - " + (stopTime - startTime));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    static void updateOrderInfo(Connection conn, int user_id, String item_name, int item_qty) {
        try {

            String query = "INSERT INTO Order_info (user_id, item_name, quantity, order_date) VALUES (" + user_id + ", '" + item_name + "', " + item_qty + ", '2024-02-17')";
            long startTime = System.currentTimeMillis();
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            long stopTime = System.currentTimeMillis();
            System.out.println("Taken time for Insertion in local table order info : - " + (stopTime - startTime));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void updateInventory(Connection conn, int qty, String item_name) {
        try {
            String query = "UPDATE Inventory SET available_quantity = available_quantity - " + qty + " WHERE item_name = '" + item_name + "'";
            long startTime = System.currentTimeMillis();
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            long stopTime = System.currentTimeMillis();
            System.out.println("Taken time for Updation in remote table Inventory : - " + (stopTime - startTime));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
