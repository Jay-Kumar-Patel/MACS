package Dao;

public class AccountDao {

    private int accountID;
    private int investorID;
    private int advisorID;
    private int profileID;
    private String profileName;
    private String accountName;
    private boolean reinvest;
    private double cash;

    public AccountDao(){}

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getInvestorID() {
        return investorID;
    }

    public void setInvestorID(int investorID) {
        this.investorID = investorID;
    }

    public int getAdvisorID() {
        return advisorID;
    }

    public void setAdvisorID(int advisorID) {
        this.advisorID = advisorID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean getReinvest() {
        return reinvest;
    }

    public void setReinvest(boolean reinvest) {
        this.reinvest = reinvest;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }
}
