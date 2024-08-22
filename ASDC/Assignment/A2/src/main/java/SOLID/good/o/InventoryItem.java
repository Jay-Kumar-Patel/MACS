package SOLID.good.o;

public abstract class InventoryItem {

    private String name;
    private int quantity;
    private int price;

    public InventoryItem(){}

    public InventoryItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public abstract int calculateInventoryValue();

    public abstract boolean isAvailable();

    public abstract void increasedQuantity();

    public abstract boolean decreasedQuantity();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
