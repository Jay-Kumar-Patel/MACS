package SOLID.bad.o;

public class DriverClass {

    public static void main(String[] args) {

        Stationary stationary = new Stationary();

        InventoryItem TenthStdBook = new InventoryItem();
        TenthStdBook.setPrice(100);
        TenthStdBook.setQuantity(5);
        TenthStdBook.setName("10th Standard Books");
        TenthStdBook.increasedQuantity();

        InventoryItem EleventhStdBook = new InventoryItem();
        EleventhStdBook.setPrice(400);
        EleventhStdBook.setQuantity(5);
        EleventhStdBook.setName("11th Standard Books");
        EleventhStdBook.decreasedQuantity();

        InventoryItem TwelveStdBook = new InventoryItem();
        TwelveStdBook.setPrice(600);
        TwelveStdBook.setQuantity(5);
        TwelveStdBook.setName("12th Standard Books");

        InventoryItem pen = new InventoryItem();
        pen.setQuantity(45);
        pen.setPrice(3);
        pen.setName("Flair");

        stationary.addItem(TenthStdBook);
        stationary.addItem(EleventhStdBook);
        stationary.addItem(TwelveStdBook);
        stationary.addItem(pen);

        System.out.println(TenthStdBook.calculateInventoryValue());
        System.out.println(EleventhStdBook.calculateInventoryValue());
        System.out.println(TwelveStdBook.calculateInventoryValue());
        System.out.println(pen.calculateInventoryValue());
        System.out.println(stationary.calculateTotalInventoryValue());
        System.out.println(stationary.calculateTotalQuantity());

        System.out.println(TwelveStdBook.isAvailable());
        TwelveStdBook.setQuantity(0);
        System.out.println(TwelveStdBook.isAvailable());

        stationary.printInventory();
        stationary.deleteItem(TenthStdBook);
        System.out.println(stationary.getItem("11th Standard Books"));

        EleventhStdBook.setPrice(523);
        stationary.updateItem(EleventhStdBook);
        System.out.println(stationary.getItem("11th Standard Books"));
    }

}
