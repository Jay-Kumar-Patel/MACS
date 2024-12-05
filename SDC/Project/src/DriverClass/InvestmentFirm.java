package DriverClass;

import java.util.Map;
import java.util.Set;

import Analysis.*;
import Dao.*;
import Reporting.*;
import Sector.*;
import Stock.*;
import User.*;
import Profile.*;
import Account.*;

public class InvestmentFirm implements InvFirm {

    User user;
    Sector sector;
    Stock stock;
    Profile profile;
    Account account;
    Reporting reporting;
    Analysis analysis;

    public InvestmentFirm(){
        user = new UserImpl();
        sector = new SectorImpl();
        stock = new StockImpl();
        profile = new ProfileImpl();
        account = new AccountImpl();
        reporting = new ReportingImpl();
        analysis = new AnalysisImpl();
    }

    public static void main(String[] args) {
        InvestmentFirm investmentFirm = new InvestmentFirm();
    }

    @Override
    public int defineSector(String sectorName) {

        if (sectorName == null || sectorName.isBlank())
            return -1;

        SectorDao newSector = new SectorDao();
        newSector.setSectorName(sectorName.trim().toUpperCase());

        return sector.defineSector(newSector);
    }

    @Override
    public int defineStock(String companyName, String stockSymbol, String sector) {

        if (companyName == null || companyName.isBlank() || stockSymbol == null || stockSymbol.isBlank() || sector == null || sector.isBlank()){
            return -1;
        }

        StockDao newStock = new StockDao();
        newStock.setCompanyName(companyName.trim().toUpperCase());
        newStock.setStockSymbol(stockSymbol.trim().toUpperCase());
        newStock.setSectorName(sector.trim().toUpperCase());

        return stock.defineStock(newStock);
    }

    @Override
    public boolean setStockPrice(String stockSymbol, double perSharePrice) {

        if (stockSymbol == null || stockSymbol.isBlank() || perSharePrice < 0)
            return false;

        StockDao modifyStock = new StockDao();
        modifyStock.setStockSymbol(stockSymbol.trim().toUpperCase());
        modifyStock.setPrice(perSharePrice);

        return stock.setStockPrice(modifyStock);
    }

    @Override
    public int defineProfile(String profileName, Map<String, Integer> sectorHoldings) {

        if (profileName == null || profileName.isBlank() || sectorHoldings == null || sectorHoldings.isEmpty())
            return -1;

        if (!sectorHoldings.containsKey("Cash") && !sectorHoldings.containsKey("cash"))
            return -1;

        int totalHolding = 0;
        for (Map.Entry<String, Integer> entry : sectorHoldings.entrySet()){
            totalHolding += entry.getValue();
        }

        if (totalHolding != 100)
            return -1;

        ProfileDao newProfile = new ProfileDao();
        newProfile.setProfileName(profileName.trim().toUpperCase());
        newProfile.setSectorHoldings(sectorHoldings);

        return profile.defineProfile(newProfile);
    }

    @Override
    public int addAdvisor(String advisorName) {

        if (advisorName == null || advisorName.isBlank())
            return -1;

        UserDao newAdvisor = new UserDao();
        newAdvisor.setName(advisorName.trim().toUpperCase());
        newAdvisor.setRole(UserDao.Role.ADVISOR);

        return user.addUser(newAdvisor);
    }

    @Override
    public int addClient(String clientName) {

        if (clientName == null || clientName.isBlank())
            return -1;

        UserDao newClient = new UserDao();
        newClient.setName(clientName.trim().toUpperCase());
        newClient.setRole(UserDao.Role.INVESTOR);

        return user.addUser(newClient);
    }

    @Override
    public int createAccount(int clientId, int financialAdvisor, String accountName, String profileType, boolean reinvest) {

        if (clientId < 0 || financialAdvisor < 0 || accountName == null || accountName.isBlank() || profileType == null || profileType.isBlank())
            return -1;

        AccountDao newAccount = new AccountDao();
        newAccount.setInvestorID(clientId);
        newAccount.setAdvisorID(financialAdvisor);
        newAccount.setAccountName(accountName.trim().toUpperCase());
        newAccount.setProfileName(profileType.trim().toUpperCase());
        newAccount.setReinvest(reinvest);

        return account.createAccount(newAccount);
    }

    @Override
    public boolean tradeShares(int account, String stockSymbol, int sharesExchanged) {

        if (account < 0 || stockSymbol == null || stockSymbol.isBlank() || sharesExchanged == 0)
            return false;

        return stock.tradeShares(account, stockSymbol, sharesExchanged);
    }

    @Override
    public boolean changeAdvisor(int accountId, int newAdvisorId) {

        if (accountId < 0 || newAdvisorId < 0)
            return false;

        AccountDao accountDao = new AccountDao();
        accountDao.setAdvisorID(newAdvisorId);
        accountDao.setAccountID(accountId);

        return account.changeAdvisor(accountDao);
    }

    @Override
    public double accountValue(int accountId) {

        if (accountId < 0)
            return -1;

        return reporting.accountValue(accountId);
    }

    @Override
    public double advisorPortfolioValue(int advisorId) {

        if (advisorId < 0)
            return -1;

        return reporting.advisorPortfolioValue(advisorId);
    }

    @Override
    public Map<Integer, Double> investorProfit(int clientId) {

        if (clientId < 0)
            return null;

        return reporting.investorProfit(clientId);
    }

    @Override
    public Map<String, Integer> profileSectorWeights(int accountId) {

        if (accountId < 0)
            return null;

        return reporting.profileSectorWeights(accountId);
    }

    @Override
    public Set<Integer> divergentAccounts(int tolerance) {

        if (tolerance < 0)
            return null;

        return reporting.divergentAccounts(tolerance);
    }

    @Override
    public int disburseDividend(String stockSymbol, double dividendPerShare) {

        if (stockSymbol == null || stockSymbol.isBlank() || dividendPerShare <= 0)
            return -1;

        return stock.disburseDividend(stockSymbol, dividendPerShare);
    }

    @Override
    public Map<String, Boolean> stockRecommendations(int accountId, int maxRecommendations, int numComparators) {

        if (accountId < 0 || maxRecommendations <= 0 || numComparators <= 0)
            return null;

        return analysis.stockRecommendations(accountId, maxRecommendations, numComparators);
    }

    @Override
    public Set<Set<Integer>> advisorGroups(double tolerance, int maxGroups) {

        if (tolerance < 0 || maxGroups <= 0){
            return null;
        }

        return analysis.advisorGroups(tolerance, maxGroups);
    }
}
