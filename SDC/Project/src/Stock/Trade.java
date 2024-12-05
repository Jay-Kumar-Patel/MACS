package Stock;

import Dao.StockDao;
import Dao.TradeDao;
import Database.MysqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Exception.*;

public class Trade {

    /**
     * Method to Check the given stock is valid or not.
     * @param tradeDao : Data is passed to set stock properties in it.
     * @param stockSymbol : Stock Symbol for which we have to perform validation.
     * @return : Return true if stock is valid, else return false.
     */
    public boolean tradeStockValidation(TradeDao tradeDao, String stockSymbol)
    {
        try{

            //If Cash In or Out than no need to check further.
            if(stockSymbol.equalsIgnoreCase("Cash")){
                return true;
            }

            //Create Query for Stock Validation
            String readQueryStockSymbol = String.format("Select * from stock Where Symbol='%s'", stockSymbol);

            //Execute Query
            ResultSet resultSetStock = MysqlConnection.getInstance().FetchData(readQueryStockSymbol, "Read Stock Table for tradeShare");

            //ResultSet
            if (resultSetStock.next()){
                //Set Properties.
                tradeDao.setStockID(resultSetStock.getInt("ID"));
                tradeDao.setPrice(resultSetStock.getDouble("Price"));
                return true;
            }
            else{
                throw new CustomException("Stock (" + stockSymbol + ") does not exist!");
            }
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return false;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }


    /**
     * Method to check the given account is valid or not.
     * @param tradeDao : Data is passed to set account properties in it.
     * @param stockSymbol : Account for which we have to perform validation.
     * @return : Return true if account is valid, else return false.
     */
    public boolean tradeAccountValidation(TradeDao tradeDao, String stockSymbol)
    {
        try{

            //Create Query for Account Validation
            String readQueryAccountID = String.format("Select * from account Where ID='%d'", tradeDao.getAccountID());

            //Execute Query
            ResultSet resultSetAccount = MysqlConnection.getInstance().FetchData(readQueryAccountID, "Read account Table for tradeShare");

            //Work on ResultSet.
            if (resultSetAccount.next()){

                //Set Properties
                tradeDao.setAccountID(resultSetAccount.getInt("ID"));
                tradeDao.setCashBalance(resultSetAccount.getDouble("Cash"));

                //If stock symbol is "Cash" than check the amount of cash out must be present in account.
                if(stockSymbol.equalsIgnoreCase("Cash")){
                    if (tradeDao.getSharesExchanged() > 0)
                        return true;
                    else{
                        if (tradeDao.getCashBalance() < Math.abs(tradeDao.getSharesExchanged()))
                            throw new CustomException("User doesn't have enough cash balance to perform this transaction!");
                        else
                            return true;
                    }
                }

                //If trade type is BUY than check the amount of money to buy new shares must be present in account.
                if (tradeDao.getTradeType().equals(StockDao.stockTransactionType.BUY.toString())){
                    if (tradeDao.getCashBalance() < (tradeDao.getPrice() * tradeDao.getSharesExchanged())){
                        throw new CustomException("User doesn't have enough cash balance to perform this transaction!");
                    }
                }
                return true;
            }
            else{
                throw new CustomException("Account does not exist for trade!");
            }
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return false;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }


    /**
     * Method to set the properties like current account stoke holding and value of ACB.
     * @param tradeDao :  Data is passed to set account properties in it.
     * @param stockSymbol : Stock Symbol for which we have to check in user account.
     * @return : Return true if account had enough stock in case of selling, else return false.
     */
    public boolean tradeAccountStockValidation(TradeDao tradeDao, String stockSymbol)
    {
        try{
            //If Cash In or Out than no need to check further.
            if(stockSymbol.equalsIgnoreCase("Cash")){
                return true;
            }

            //Create Query for validation
            String readQuery = String.format("Select * from account_stock_info Where Account_ID='%d' AND Stock_ID='%d'", tradeDao.getAccountID(), tradeDao.getStockID());

            //Execute Query
            ResultSet resultSet = MysqlConnection.getInstance().FetchData(readQuery, "Read account_stock_info Table for tradeShare");

            //ResultSet
            if (resultSet.next()){

                //Set Properties.
                tradeDao.setShares(resultSet.getDouble("Stokes"));
                tradeDao.setACB(resultSet.getDouble("ACB"));
                if (tradeDao.getTradeType().equals(StockDao.stockTransactionType.SELL.toString())){
                    if (tradeDao.getShares() < Math.abs(tradeDao.getSharesExchanged()))
                        throw new CustomException("User doesn't have enough Share to perform this transaction!");
                }
                return true;
            }
            else{
                //If trade type is BUY than set Number of share and ACB for that account to 0.
                if (tradeDao.getTradeType().equals(StockDao.stockTransactionType.BUY.toString())){
                    tradeDao.setShares(0);
                    tradeDao.setACB(0);
                    return true;
                }
                else
                    throw new CustomException("User doesn't have enough Share to perform this transaction!");
            }
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return false;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }


    /**
     * Method to Buy Stock
     * @param tradeDao : Transaction Info like stockID, Account CashBalance, ACB and many more.
     * @param connection : Mysql Connection
     * @return : Return true, if trade is successfully performed else return false.
     */
    public boolean buyStock(TradeDao tradeDao, Connection connection)
    {
        //Calculate Parameters
        double newCashBalance = tradeDao.getCashBalance() - (tradeDao.getPrice() * tradeDao.getSharesExchanged());
        double newShareHolding = tradeDao.getShares() + tradeDao.getSharesExchanged();
        double newACB = tradeDao.getACB()  + (tradeDao.getPrice() * tradeDao.getSharesExchanged());

        //Perform Transaction (BUY)
        return performTrade(tradeDao, newCashBalance, newShareHolding, newACB, connection);
    }


    /**
     * Method to Sell Stock
     * @param tradeDao : Transaction Info like stockID, Account CashBalance, ACB and many more.
     * @param connection : Mysql Connection
     * @return : Return true, if trade is successfully performed else return false.
     */
    public boolean sellStock(TradeDao tradeDao, Connection connection)
    {
        //Calculate Parameters
        double newCashBalance = tradeDao.getCashBalance() + (tradeDao.getPrice() * Math.abs(tradeDao.getSharesExchanged()));
        double newShareHolding = tradeDao.getShares() - Math.abs(tradeDao.getSharesExchanged());
        double multiple = 1 - (Math.abs(tradeDao.getSharesExchanged())/tradeDao.getShares());
        double newACB = tradeDao.getACB() * multiple;

        //Perform Transaction (SELL)
        return performTrade(tradeDao, newCashBalance, newShareHolding, newACB, connection);
    }


    /**
     * Method to update Cash Balance in case of stock symbol=Cash
     * @param tradeDao : Transaction Info like stockID, Account CashBalance, ACB and many more.
     * @param type : Cash In or Cash Out.
     * @param connection : Mysql Connection
     * @return : Return true, if Cash is successfully updated else return false.
     */
    public boolean UpdateCashBalance(TradeDao tradeDao, String type, Connection connection)
    {
        try{

            boolean transactionStatus = false;

            //Calculate New Cash Balance.
            double newCashBalance;
            if (type.equalsIgnoreCase("In")){
                newCashBalance = tradeDao.getCashBalance() + tradeDao.getSharesExchanged();
            }
            else{
                newCashBalance = tradeDao.getCashBalance() - Math.abs(tradeDao.getSharesExchanged());
            }

            //Create Query to Update Cash Balance.
            String updateQueryAccount = String.format("UPDATE account SET Cash='%f' Where ID='%d'", newCashBalance, tradeDao.getAccountID());
            String insertQuery = String.format("INSERT INTO transaction (Type, Account_ID, Stocks) VALUES ('%s', '%d', '%f')", tradeDao.getTradeType(), tradeDao.getAccountID(), tradeDao.getSharesExchanged());

            //Execute Query
            List<Integer> updateAccount = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(updateQueryAccount, insertQuery)), new ArrayList<Boolean>(Arrays.asList(false, false)), "Update Cash Balance for Trade Cash " + type);

            //Result
            if (updateAccount != null && updateAccount.get(0) > 0 && updateAccount.get(1) > 0){
                transactionStatus = true;
            }
            else{
                throw new CustomException("Failed to Update Cash Balance for Trade Cash " + type);
            }

            return transactionStatus;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }


    /**
     * Method to Perform Trade Buy or Sell
     * @param tradeDao : Transaction Info like stockID, Account CashBalance, ACB and many more.
     * @param newCashBalance : New Cash Balance after performing transaction.
     * @param newShareHolding : New Share holding  after performing transaction.
     * @param newACB : New ACB after performing transaction.
     * @param connection : Mysql Connection
     * @return : Return true, if trade is successfully performed else return false.
     */
    private boolean performTrade(TradeDao tradeDao, double newCashBalance, double newShareHolding, double newACB, Connection connection)
    {
        boolean transactionStatus = false;

        try{

            //Create Queries for Transaction
            String updateQueryAccount = String.format("UPDATE account SET Cash='%f' Where ID='%d'", newCashBalance, tradeDao.getAccountID());
            String updateQueryAccountStockInfo = String.format("INSERT INTO account_stock_info (Account_ID, Stock_ID, Stokes, ACB) VALUES ('%d', '%d', '%f', '%f') ON DUPLICATE KEY UPDATE Stokes='%f', ACB='%f'", tradeDao.getAccountID(), tradeDao.getStockID(), newShareHolding, newACB, newShareHolding, newACB);
            String insertQuery = String.format("INSERT INTO transaction (Type, Account_ID, Stock_ID, Stocks, Per_Stock_Price) VALUES ('%s', '%d', '%d', '%f', '%f')", tradeDao.getTradeType(), tradeDao.getAccountID(), tradeDao.getStockID(), tradeDao.getSharesExchanged(), tradeDao.getPrice());

            List<String> queries = new ArrayList<>();
            queries.add(updateQueryAccount);
            queries.add(updateQueryAccountStockInfo);
            queries.add(insertQuery);

            //Execute Queries.
            List<Integer> updateAccount = MysqlConnection.getInstance().ExecuteQueries(queries, new ArrayList<Boolean>(Arrays.asList(false, false, false)), "Update account details for trade share");

            //Result
            if (updateAccount != null && updateAccount.get(0) > 0 && updateAccount.get(1) > 0 && updateAccount.get(2) > 0){
                transactionStatus = true;
            }
            else{
                throw new CustomException("Failed to Update Account Details like Cash Balance and ACB!");
            }

            return transactionStatus;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }

}
