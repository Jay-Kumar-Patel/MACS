package Dao;

public class StockDao {

    private int id;
    private String stockSymbol;
    private String companyName;
    private String sectorName;
    private int sectorID;
    private double price;

    public enum stockHistoryType{
        SETPRICE,
        DIVIDEND
    }

    public enum stockTransactionType{
        BUY,
        SELL,
        COMPANY_TO_FIRM,
        FIRM_TO_INVESTOR,
        DIVIDEND_CASH,
        CASH
    }

    public StockDao(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getSectorID() {
        return sectorID;
    }

    public void setSectorID(int sectorID) {
        this.sectorID = sectorID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
