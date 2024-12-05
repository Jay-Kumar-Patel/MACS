package Stock;

import Dao.DividendDao;
import Dao.StockDao;
import Dao.TradeDao;
import Database.MysqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import Exception.*;

public class StockImpl implements Stock{

    /**
     * Method to Add new Stock to the database.
     * @param stock : Stock Data (Sector Name, Stock Symbol)
     * @return If stock is successfully added than return ID of that stock, else return -1.
     */
    @Override
    public int defineStock(StockDao stock) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Input Validation
            int sectorID = newStockValidation(stock.getSectorName(), stock.getStockSymbol());

            if (sectorID < 0){
                throw new CustomException("Failed to Add New Stock!");
            }

            //Create Query to Add Stock
            String insertQuery = String.format("INSERT INTO stock (Symbol, Company_Name, Sector_ID, Price) VALUES ('%s', '%s', '%d', '%f')", stock.getStockSymbol(), stock.getCompanyName(), sectorID, 1.0);

            //Execute Query
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(insertQuery)), new ArrayList<Boolean>(Arrays.asList(true)), "Creating New Stock of Sector: " + stock.getSectorName() + ", and stockSymbol: " + stock.getStockSymbol());

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            //Check result of query execution
            if (lastInsertedId != null && lastInsertedId.get(0) > 0){
                return lastInsertedId.get(0);
            }
            else {
                throw new CustomException("Unable to add new Stock: " + stock.getStockSymbol() + "!");
            }
        }
        catch (SQLException sqlException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return -1;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return -1;
        }
    }


    /**
     * Method for input Validation for Adding new Stock.
     * @param sector : Sector Name to which the new stock belong.
     * @param stockSymbol : Symbol of new stock.
     * @return : Return ID of sector to which this stock symbol belong.
     * In case that sector does not exist, stock symbol is already present or for any other errors, return -1.
     */
    private int newStockValidation(String sector, String stockSymbol)
    {
        try {

            //Create Query for Sector Validation
            String readQuerySector = String.format("Select * from sector Where Name='%s'", sector);
            int[] sectorID = new int[1];

            //Execute Query
            if (!MysqlConnection.getInstance().isDataPresent(readQuerySector, "Check " + sector + " sector is already exist or not to create new stock", true, sectorID)){
                throw new CustomException("Sector (" + sector + ") to which this stock ("+ stockSymbol + ") belong is not Exist.");
            }

            //Create Query for Stock Symbol Validation
            String readQueryStockSymbol = String.format("Select * from stock Where Symbol='%s'", stockSymbol);

            //Execute Query
            if (MysqlConnection.getInstance().isDataPresent(readQueryStockSymbol, "Check " + stockSymbol + " is already exist or not to create new stock", false, new int[]{})){
                throw new CustomException("Stock Symbol (" + stockSymbol + ") is already Exist.");
            }

            return sectorID[0];
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return -1;
        }
    }


    /**
     * Method to Set Stock Price.
     * @param stock : Stock Data (Stock Symbol, New Price)
     * @return : If stock price is successfully set than return true, else return false.
     */
    @Override
    public boolean setStockPrice(StockDao stock) {

        try{

            //Start Connection
            Connection connection = (Connection) MysqlConnection.getInstance().StartConnection();

            if (connection == null){
                throw new SQLException();
            }

            //Create Query to Fetch Stock ID and Current Price of the given stock.
            String readQueryStockSymbol = String.format("Select * from stock Where Symbol='%s'", stock.getStockSymbol());

            //Execute Query
            ResultSet resultSet = MysqlConnection.getInstance().FetchData(readQueryStockSymbol, "Read Stock Table for SetStockPrice");

            int stockID = -1;
            double currentPrice = -1;

            //Set Data
            if (resultSet.next()){
                stockID = resultSet.getInt("ID");
                currentPrice = resultSet.getInt("Price");
            }

            if (stockID < 0 || currentPrice < 0)
            {
                throw new CustomException("Failed to Set Price because Stock Symbol is not exist.");
            }

            //Change AutoCommit to false;
            connection.setAutoCommit(false);

            //Query to Update Stock Price
            String updateQuery = String.format("UPDATE stock SET Price='%f' Where ID='%d'", stock.getPrice(), stockID);

            //Query to Update Stock History.
            String insertQuery = String.format("INSERT INTO stock_history (Stock_ID, Type, Current_Price, New_Price) VALUES ('%d', '%s', '%f', '%f')", stockID,  StockDao.stockHistoryType.SETPRICE.toString(), currentPrice, stock.getPrice());

            //Execute Queries
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(updateQuery, insertQuery)), new ArrayList<Boolean>(Arrays.asList(false, false)), "Update Stock Price of stock symbol: " + stock.getStockSymbol());

            //Check result of query execution
            if (lastInsertedId != null && lastInsertedId.get(0) > 0 && lastInsertedId.get(1) > 0){
                connection.commit();
                connection.setAutoCommit(true);
                MysqlConnection.getInstance().CloseConnection();
                return true;
            }
            else {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new CustomException("Unable to Set Stock (" + stock.getStockSymbol() + ") Price!");
            }
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return false;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return false;
        }
    }

    /**
     * Method to Trade Share
     * @param account : Account ID
     * @param stockSymbol : Stock Symbol
     * @param sharesExchanged : Number of Shares to Trade (If Positive Buy Shares, Negative Sell Shares)
     * @return : If trade is successfully done than return true, else return false.
     */
    @Override
    public boolean tradeShares(int account, String stockSymbol, int sharesExchanged) {

        try {

            //Open Connection
            Connection connection = (Connection) MysqlConnection.getInstance().StartConnection();

            if (connection == null){
                throw new SQLException();
            }

            //Set Trade Properties.
            TradeDao tradeDoa = new TradeDao();
            tradeDoa.setAccountID(account);
            tradeDoa.setSharesExchanged(sharesExchanged);

            //Set Trade Type
            if (sharesExchanged > 0){
                tradeDoa.setTradeType(StockDao.stockTransactionType.BUY.toString());
            }
            else{
                tradeDoa.setTradeType(StockDao.stockTransactionType.SELL.toString());
            }

            Trade trade = new Trade();

            //Check Stock is exist or not. If exist set stock properties to tradeDao Object.
            if (!trade.tradeStockValidation(tradeDoa, stockSymbol)){
                throw new CustomException("Failed to Trade Shares!");
            }

            //Check Account is exist or not. If exist set account properties to tradeDao Object.
            if (!trade.tradeAccountValidation(tradeDoa, stockSymbol)){
                throw new CustomException("Failed to Trade Shares!");
            }

            //Check Account had this stock in his account or not. If exist set stock properties to tradeDao Object.
            if (!trade.tradeAccountStockValidation(tradeDoa, stockSymbol)){
                throw new CustomException("Failed to Trade Shares!");
            }

            boolean tradeStatus;

            //Change Autocommit to false.
            connection.setAutoCommit(false);

            //If Stock Symbol is Cash (Special Case)
            if (stockSymbol.equalsIgnoreCase("Cash"))
            {
                tradeDoa.setTradeType(StockDao.stockTransactionType.CASH.toString());

                String type;
                if (sharesExchanged > 0){
                    type = "In";
                }
                else type = "Out";

                tradeStatus = trade.UpdateCashBalance(tradeDoa, type, connection);
            }
            //Else Perform Regular Trade
            else{
                if (sharesExchanged > 0){
                    tradeStatus = trade.buyStock(tradeDoa, connection);
                }
                else{
                    tradeStatus = trade.sellStock(tradeDoa, connection);
                }
            }

            //Check Trade Status
            if (tradeStatus){
                connection.commit();
                connection.setAutoCommit(true);
                MysqlConnection.getInstance().CloseConnection();
                return true;
            }
            else{
                connection.rollback();
                connection.setAutoCommit(true);
                throw new CustomException("Failed to Trade Stock!");
            }
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return false;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return false;
        }
    }


    /**
     * Method to Disburse Dividend
     * @param stockSymbol : Stock Symbol which give dividend.
     * @param dividendPerShare : Money per Share
     * @return : Number of shares that the company has had to purchase to manage the dividends
     */
    @Override
    public int disburseDividend(String stockSymbol, double dividendPerShare)
    {
        try {

            //Open Connection
            Connection connection = (Connection) MysqlConnection.getInstance().StartConnection();

            if (connection == null){
                throw new SQLException();
            }

            Dividend dividend = new Dividend();

            //Get account details of all those accounts who hold this Stock
            List<DividendDao> accountDetails = dividend.getDisburseAccount(stockSymbol);

            if (accountDetails == null){
                throw new CustomException("Failed to Disburse Dividend!");
            }

            //Check is there any data present or not,
            if (accountDetails.isEmpty()){
                MysqlConnection.getInstance().CloseConnection();
                return 0;
            }

            double extraStokes = 0;
            double totalShares = 0;

            //Get that how many stokes are taken by all investors from the firm.
            for (DividendDao d : accountDetails){
                if(d.getReinvest() == 1){
                    double totalMoney = d.getStokes() * dividendPerShare;
                    double shareToBuy = totalMoney/d.getStockPrice();
                    totalShares += shareToBuy;
                    extraStokes += shareToBuy - (int)shareToBuy;
                }
            }

            //Change AutoCommit to false.
            connection.setAutoCommit(false);

            //First Step is the Firm buy Whole stokes from that company and hold them to disburse some part of that stock to investors.
            int T_S = Integer.MIN_VALUE;
            if (extraStokes > 0){

                T_S = dividend.buyShareForDividend(extraStokes, accountDetails.get(0).getStockID(), accountDetails.get(0).getStockPrice());

                //Check Trade Status
                if (T_S == Integer.MIN_VALUE){
                    connection.rollback();
                    connection.setAutoCommit(true);
                    throw new CustomException("Failed to Disburse Dividend!");
                }
            }

            //Now Give dividend to the those accounts who hold this particular Share.
            boolean dividendStatus = dividend.applyDividend(accountDetails, dividendPerShare);

            //Now Update Stock History.
            boolean UpdateStockDividendHistory = false;
            if (dividendStatus){
                UpdateStockDividendHistory = dividend.UpdateStockHistory(accountDetails.get(0).getStockID(), dividendPerShare);
            }

            //Check Trade Status
            if (dividendStatus && UpdateStockDividendHistory){
                connection.commit();
                connection.setAutoCommit(true);
                MysqlConnection.getInstance().CloseConnection();

                if (T_S == -1)
                    return (int)totalShares;
                else
                    return T_S;
            }
            else {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new CustomException("Failed to Disburse Dividend!");
            }
        }
        catch (SQLException sqlException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return Integer.MIN_VALUE;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return Integer.MIN_VALUE;
        }
    }
}
