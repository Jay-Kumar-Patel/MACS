package Stock;

import Dao.DividendDao;
import Dao.StockDao;
import Database.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import Exception.*;

public class Dividend {


    /**
     * Method to Get Data of those Accounts who hold stock for dividend.
     * @param stockSymbol : Stock Symbol
     * @return Return Account Details.
     */
    public List<DividendDao> getDisburseAccount(String stockSymbol)
    {
        try {

            List<DividendDao> data = new ArrayList<>();

            //Create Query to Fetch Data
            String query = String.format("SELECT a.ID, a.Cash, a.Reinvest, asi.Stokes, asi.ACB, s.ID AS SID, s.Price From stock s, account_stock_info asi, account a " +
                    "WHERE s.Symbol = '%s' AND s.ID=asi.Stock_ID AND asi.Account_ID=a.ID", stockSymbol);

            //Execute Query
            ResultSet resultSet = MysqlConnection.getInstance().FetchData(query, "Fetch Data for Dividend.");

            //Work on ResultSet
            while (resultSet.next()){
                DividendDao dividendDao = new DividendDao();
                dividendDao.setAccountID(resultSet.getInt("ID"));
                dividendDao.setCashBalance(resultSet.getDouble("Cash"));
                dividendDao.setReinvest(resultSet.getByte("Reinvest"));
                dividendDao.setStokes(resultSet.getDouble("Stokes"));
                dividendDao.setStockID(resultSet.getInt("SID"));
                dividendDao.setStockPrice(resultSet.getDouble("Price"));
                dividendDao.setACB(resultSet.getDouble("ACB"));

                data.add(dividendDao);
            }

            return data;
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return null;
        }
    }


    /**
     * Buy Stocks by Firm
     * @param extraStokes : Stokes that Firm have to buy so that it can give some fraction of those to investors.
     * @param stockID : Stock ID of that stock.
     * @param price : Price of that Stock
     * @return : Return the number of new shares firm have to buy and also return the new holding of that stock by the firm.
     */
    public int buyShareForDividend(Double extraStokes, int stockID, double price)
    {
        try {
            double currentHolding = getStockCompanyHolding(stockID);

            int T_S = -1;
            double updatedHolding = 0;

            //Calculate new holding of that stock by the firm.
            if (extraStokes < currentHolding){
                updatedHolding = currentHolding - extraStokes;
            }
            else{
                T_S = (int)Math.abs(Math.ceil(extraStokes - currentHolding));
                updatedHolding = currentHolding + T_S;
            }

            //Create Queries to Update stock from Company to Firm.
            String updateQuery = String.format("INSERT INTO company_holdings (Stock_ID, Shares) VALUES ('%d', '%f') ON DUPLICATE KEY UPDATE Shares='%f'", stockID, updatedHolding, updatedHolding);
            String insertQuery = String.format("INSERT INTO transaction (Type, Stock_ID, Stocks, Per_Stock_Price) VALUES ('%s', '%d', '%f', '%f')", StockDao.stockTransactionType.COMPANY_TO_FIRM, stockID, (double)T_S, price);

            //Execute Query
            List<Integer> updateCompanyHoldings = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(updateQuery, insertQuery)), new ArrayList<Boolean>(Arrays.asList(false, false)), "Update Company Holdings for Dividend");

            if (updateCompanyHoldings == null || updateCompanyHoldings.get(0) < 0 || updateCompanyHoldings.get(1) < 0){
                throw new CustomException("Failed to Buy Share From Company to Firm!");
            }

            return T_S;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return Integer.MIN_VALUE;
        }
    }


    /**
     * Method to Update Account Details based on Reinvest Value.
     * @param dividendDao : All account details (Only those who are eligible for dividend)
     * @param dividendPerShare : Dividend Money Per Share
     * @return : If dividend are successfully applied than return true, else return false.
     */
    public boolean applyDividend(List<DividendDao> dividendDao, double dividendPerShare)
    {
        List<DividendDao> updateCashBalance = new ArrayList<>();
        List<DividendDao> updateStokes = new ArrayList<>();

        //Separate Account based on reinvest value.
        for (DividendDao d : dividendDao){
            if (d.getReinvest() == 1){
                updateStokes.add(d);
            }
            else{
                updateCashBalance.add(d);
            }
        }

        boolean status = true;

        //If Reinvest value is false, just update Cash Balance.
        if (updateCashBalance.size() > 0){
            status &= updateCashBalanceDividend(dividendDao, dividendPerShare);
        }

        //If Reinvest value is true update amount of stock and ACB.
        if (updateStokes.size() > 0){
            status &= updateStokesDividend(dividendDao, dividendPerShare);
        }

        return status;
    }


    /**
     * Method to Update Cash Balance in Account for Dividend.
     * @param dividendDao : All account details (Only those who have reinvest value as false)
     * @param dividendPerShare: Dividend Money Per Share
     * @return : If Cash Balance are successfully Updated than return true, else return false.
     */
    private boolean updateCashBalanceDividend(List<DividendDao> dividendDao, double dividendPerShare)
    {
        try {
            List<String> quries = new ArrayList<>();
            List<Boolean> wantLastInsertedID = new ArrayList<>();

            //Create Query for all accounts.
            for(DividendDao d : dividendDao){

                quries.add(String.format("UPDATE account SET Cash='%f' WHERE ID='%d'", d.getCashBalance() + (d.getStokes() * dividendPerShare),d.getAccountID()));
                quries.add(String.format("INSERT INTO transaction (Type, Account_ID, Stock_ID, Stocks, Per_Stock_Price) VALUES ('%s', '%d', '%d', '%f', '%f')", StockDao.stockTransactionType.DIVIDEND_CASH, d.getAccountID(), d.getStockID(), d.getStokes(), d.getStockPrice()));
                wantLastInsertedID.add(false);
                wantLastInsertedID.add(false);
            }

            //Execute all queries.
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(quries, wantLastInsertedID, "Update account cash balance for dividend");

            //Result of Queries
            if (lastInsertedId != null){
                return true;
            }
            else {
                throw new CustomException("Failed to Update Cash Balance for Dividend!");
            }
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }

    /**
     * Method to Update Amount of Stock and ACB value.
     * @param dividendDao : All account details (Only those who have reinvest value as true)
     * @param dividendPerShare: Dividend Money Per Share
     * @return : If Stock Amount and ACB are successfully Updated than return true, else return false.
     */
    private boolean updateStokesDividend(List<DividendDao> dividendDao, double dividendPerShare)
    {
        try {
            List<String> quries = new ArrayList<>();
            List<Boolean> wantLastInsertedID = new ArrayList<>();

            //Get the Current Holding of this Stock by the firm.
            double currentCompanyHolding = getStockCompanyHolding(dividendDao.get(0).getStockID());

            for(DividendDao d : dividendDao){

                double money = dividendPerShare * d.getStokes();
                double buyStokes = money/d.getStockPrice();

                //Buy Stocks directly from company
                double buyFromStockCompany = (int)buyStokes;

                //Buy fraction of stock from Firm.
                double buyFromFirm = buyStokes - (int)buyStokes;

                //Create Query to buy stocks directly from company.
                quries.add(String.format("UPDATE account_stock_info SET Stokes='%f', ACB='%f' WHERE Account_ID='%d' AND Stock_ID='%d'", d.getStokes() + buyFromStockCompany,d.getACB() + (buyFromStockCompany * d.getStockPrice()),d.getAccountID(), d.getStockID()));
                quries.add(String.format("INSERT INTO transaction (Type, Account_ID, Stock_ID, Stocks, Per_Stock_Price) VALUES ('%s', '%d', '%d', '%f', '%f')", StockDao.stockTransactionType.BUY, d.getAccountID(), d.getStockID(), buyFromStockCompany, d.getStockPrice()));

                wantLastInsertedID.add(false);
                wantLastInsertedID.add(false);

                //Create Query to buy fraction of stock from investment firm
                if (buyFromFirm > 0){
                    quries.add(String.format("INSERT INTO company_holdings (Stock_ID, Shares) VALUES ('%d', '%f') ON DUPLICATE KEY UPDATE Shares='%f'", d.getStockID(), currentCompanyHolding - buyFromFirm, currentCompanyHolding - buyFromFirm));
                    currentCompanyHolding -= buyFromFirm;
                    quries.add(String.format("INSERT INTO transaction (Type, Account_ID, Stock_ID, Stocks, Per_Stock_Price) VALUES ('%s', '%d', '%d', '%f', '%f')", StockDao.stockTransactionType.FIRM_TO_INVESTOR, d.getAccountID(), d.getStockID(), buyFromFirm, d.getStockPrice()));

                    quries.add(String.format("UPDATE account_stock_info SET Stokes='%f', ACB='%f' WHERE Account_ID='%d' AND Stock_ID='%d'", d.getStokes() + buyFromStockCompany + buyFromFirm, d.getACB() + ((buyFromStockCompany+buyFromFirm) * d.getStockPrice()),d.getAccountID(), d.getStockID()));
                    quries.add(String.format("INSERT INTO transaction (Type, Account_ID, Stock_ID, Stocks, Per_Stock_Price) VALUES ('%s', '%d', '%d', '%f', '%f')", StockDao.stockTransactionType.BUY, d.getAccountID(), d.getStockID(), buyFromFirm, d.getStockPrice()));

                    wantLastInsertedID.add(false);
                    wantLastInsertedID.add(false);
                    wantLastInsertedID.add(false);
                    wantLastInsertedID.add(false);
                }
            }

            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(quries, wantLastInsertedID, "Update account stokes and ACB for dividend");

            if (lastInsertedId != null){
                return true;
            }
            else {
                throw new CustomException("Failed to Update Stock Amount and ACB for Dividend!");
            }
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }

    /**
     * Method to Get the Holding of that Stock by firm.
     * @param stockID : Stock ID of Dividend Stock.
     * @return : If company had that stock than return current holding of that stock else, return 0.
     */
    private double getStockCompanyHolding(int stockID)
    {
        try {
            double currentHolding = 0;

            //Create Query to get current holding of that stock.
            String query = String.format("SELECT * from company_holdings Where Stock_ID='%d'", stockID);

            //Execute Query
            ResultSet resultSet = MysqlConnection.getInstance().FetchData(query, "Read Company Holdings for Dividend.");

            //Work on ResultSet
            if (resultSet.next()){
                currentHolding += resultSet.getDouble("Shares");
            }
            else{
                currentHolding = 0;
            }

            return currentHolding;
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return -1;
        }
    }

    /**
     * Method to Update Stock History.
     * @param stockID : Stock ID for which we have to update history.
     * @param dividendPerShare : Dividend Money Per Share.
     * @return :
     */
    public boolean UpdateStockHistory(int stockID, double dividendPerShare)
    {
        try {
            //Create Query to update stock history.
            String insertQuery = String.format("INSERT INTO stock_history (Stock_ID, Type, DividendPerSharePrice) VALUES ('%d', '%s', '%f')", stockID,  StockDao.stockHistoryType.DIVIDEND, dividendPerShare);

            //Execute Query.
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(insertQuery)), new ArrayList<Boolean>(Arrays.asList(false)), "Insert Stock History for Dividend.");

            //Result
            if (lastInsertedId != null && lastInsertedId.get(0) > 0){
                return true;
            }
            else {
                throw new CustomException("Failed to Update Stock History.");
            }
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }


}
