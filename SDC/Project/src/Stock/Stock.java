package Stock;

import Dao.StockDao;

public interface Stock {
    int defineStock(StockDao stock);

    boolean setStockPrice( StockDao stock );

    boolean tradeShares( int account, String stockSymbol, int sharesExchanged );

    int disburseDividend( String stockSymbol, double dividendPerShare );
}
