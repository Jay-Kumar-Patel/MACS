package SOLID.good.o;

public class Pen extends InventoryItem{

    private String companyName;
    private String type;
    private String color;

    public Pen(){
        super();
    }

    @Override
    public String toString() {
        return "Pen{" +
                "companyName='" + companyName + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public Pen(String name, int quantity, int price, String companyName, String type, String color){
        super(name, quantity, price);
        this.companyName = companyName;
        this.type = type;
        this.color = color;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int calculateInventoryValue() {
        return getQuantity() * getPrice();
    }

    @Override
    public boolean isAvailable() {
        if (getQuantity() > 0){
            return true;
        }
        return false;
    }

    @Override
    public void increasedQuantity() {
        increasedQuantity();
    }

    @Override
    public boolean decreasedQuantity() {
        return decreasedQuantity();
    }
}
