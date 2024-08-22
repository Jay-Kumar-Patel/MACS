package SOLID.good.o;

public class DriverClass {
    public static void main(String[] args) {

        Stationary stationary = new Stationary();

        InventoryItem TenthStdBook = new TenthStdBook();
        TenthStdBook.setPrice(100);
        TenthStdBook.setQuantity(5);
        TenthStdBook.setName("10th Standard Books");

        InventoryItem EleventhStdBook = new EleventhStdBook();
        EleventhStdBook.setPrice(400);
        EleventhStdBook.setQuantity(5);
        EleventhStdBook.setName("11th Standard Books");

        InventoryItem TwelveStdBook = new TwelveStdBook();
        TwelveStdBook.setPrice(600);
        TwelveStdBook.setQuantity(5);
        TwelveStdBook.setName("12th Standard Books");

        Pen pen = new Pen();
        pen.setQuantity(45);
        pen.setPrice(3);
        pen.setColor("Black");
        pen.setType("Gel");
        pen.setName("Flair");

        stationary.addItem(TenthStdBook);
        stationary.addItem(EleventhStdBook);
        stationary.addItem(TwelveStdBook);
        stationary.addItem(pen);

        System.out.println(TenthStdBook.calculateInventoryValue());
        System.out.println(EleventhStdBook.calculateInventoryValue());
        System.out.println(TwelveStdBook.calculateInventoryValue());
        System.out.println(pen.calculateInventoryValue());
        System.out.println(pen.getColor());
        System.out.println(stationary.calculateTotalInventoryValue());

        System.out.println(TwelveStdBook.isAvailable());
        TwelveStdBook.setQuantity(0);
        System.out.println(TwelveStdBook.isAvailable());

        stationary.printInventory();
    }
}
