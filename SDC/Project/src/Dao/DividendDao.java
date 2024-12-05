package Dao;

public class DividendDao
{
    private  int accountID;
    private double cashBalance;
    private byte reinvest;
    private double stokes;
    private double ACB;
    private int stockID;
    private double stockPrice;

    public DividendDao(){}


    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public byte getReinvest() {
        return reinvest;
    }

    public void setReinvest(byte reinvest) {
        this.reinvest = reinvest;
    }

    public double getStokes() {
        return stokes;
    }

    public void setStokes(double stokes) {
        this.stokes = stokes;
    }

    public double getACB() {
        return ACB;
    }

    public void setACB(double ACB) {
        this.ACB = ACB;
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }
}
