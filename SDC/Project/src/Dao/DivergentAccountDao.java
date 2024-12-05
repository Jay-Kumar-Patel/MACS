package Dao;

import java.util.Map;

public class DivergentAccountDao
{
    private int profileID;
    private double Cash;
    private Map<String, Double> sectors;

    public DivergentAccountDao(){}

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public double getCash() {
        return Cash;
    }

    public void setCash(double cash) {
        Cash = cash;
    }

    public Map<String, Double> getSectors() {
        return sectors;
    }

    public void setSectors(Map<String, Double> sectors) {
        this.sectors = sectors;
    }
}
