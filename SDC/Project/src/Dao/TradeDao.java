package Dao;

public class TradeDao {

    private int accountID;
    private int stockID;
    private double sharesExchanged;
    private double cashBalance;
    private double shares;
    private double price;
    private String tradeType;
    private double ACB;


    public TradeDao(){}

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public double getSharesExchanged() {
        return sharesExchanged;
    }

    public void setSharesExchanged(double sharesExchanged) {
        this.sharesExchanged = sharesExchanged;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public double getACB() {
        return ACB;
    }

    public void setACB(double ACB) {
        this.ACB = ACB;
    }
}
