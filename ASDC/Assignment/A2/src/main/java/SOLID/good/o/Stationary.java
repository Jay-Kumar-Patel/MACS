package SOLID.good.o;

import java.util.ArrayList;
import java.util.List;

public class Stationary {

    private List<InventoryItem> items;

    public Stationary() {
        items = new ArrayList<>();
    }

    public boolean addItem(InventoryItem item) {
        if (items.contains(item)){
            return false;
        }
        this.items.add(item);
        return true;
    }

    public boolean deleteItem(InventoryItem item){
        if (!items.contains(item)){
            return false;
        }
        this.items.remove(item);
        return true;
    }

    public InventoryItem getItem(String bookName){

        for (InventoryItem item : items){
            if (item.getName().equals(bookName)){
                return item;
            }
        }

        return null;
    }

    public int calculateTotalInventoryValue() {
        int value = 0;
        for (InventoryItem book : items) {
            value += book.calculateInventoryValue();
        }
        return value;
    }

    public int calculateTotalQuantity(){
        int quantity = 0;
        for (InventoryItem book : items) {
            quantity += book.getQuantity();
        }
        return quantity;
    }

    public void printInventory() {
        System.out.println("Inventory Items:");
        for (InventoryItem item : items) {
            System.out.println(item.getName() + ": Quantity = " + item.getQuantity() + ", Price = $" + item.getPrice());
        }
    }

}
