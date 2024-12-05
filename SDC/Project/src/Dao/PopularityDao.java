package Dao;

public class PopularityDao {

    private int stockID;
    private int popularity;

    private boolean buyOrSell;

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean isBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(boolean buyOrSell) {
        this.buyOrSell = buyOrSell;
    }
}
