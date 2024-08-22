package SOLID.bad.o;

public class InventoryItem {

    private String name;
    private int quantity;
    private int price;
    private String companyName;
    private String level;

    public InventoryItem(){}

    public int calculateInventoryValue(){
        return quantity * price;
    }

    public boolean isAvailable(){
        if (quantity > 0){
            return true;
        }
        return false;
    }

    public void increasedQuantity(){
        quantity++;
    }

    public boolean decreasedQuantity(){
        if (quantity == 0){
            return false;
        }
        quantity--;
        return true;
    }

    public InventoryItem(String name, int quantity, int price, String companyName, String level) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.companyName = companyName;
        this.level = level;
    }

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", companyName='" + companyName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
