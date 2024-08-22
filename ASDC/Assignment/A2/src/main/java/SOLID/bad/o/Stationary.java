package SOLID.bad.o;

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

    public InventoryItem getItem(String itemName){

        for (InventoryItem item : items){
            if (item.getName().equals(itemName)){
                return item;
            }
        }

        return null;
    }

    public boolean updateItem(InventoryItem item){

        boolean isUpdated = false;

        for (InventoryItem currItem : items){
            if (item.getName().equals(item.getName())){
                currItem.setQuantity(item.getQuantity());
                currItem.setPrice(item.getPrice());
                currItem.setLevel(item.getLevel());
                currItem.setCompanyName(item.getCompanyName());
                isUpdated = true;
            }
        }

        return isUpdated;

    }

    public int calculateTotalInventoryValue() {
        int value = 0;
        for (InventoryItem item : items) {
            value += item.calculateInventoryValue();
        }
        return value;
    }

    public int calculateTotalQuantity(){
        int quantity = 0;
        for (InventoryItem item : items) {
            quantity += item.getQuantity();
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
