package SOLID.good.o;

public class EleventhStdBook extends InventoryItem{

    private String companyName;
    private String level;

    public EleventhStdBook()
    {
        super();
    }

    public EleventhStdBook(String name, int quantity, int price, String companyName, String level){
        super(name, quantity, price);
        this.companyName = companyName;
        this.level = level;
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
    public String toString() {
        return "EleventhStdBook{" +
                "companyName='" + companyName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    @Override
    public void increasedQuantity() {
        increasedQuantity();
    }

    @Override
    public boolean decreasedQuantity() {
        return decreasedQuantity();
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
}
