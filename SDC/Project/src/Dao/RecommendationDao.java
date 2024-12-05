package Dao;

import java.util.Map;

public class RecommendationDao {

    private double cash;
    private Map<Integer, Double> stokes;

    public RecommendationDao(){}

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public Map<Integer, Double> getStokes() {
        return stokes;
    }

    public void setStokes(Map<Integer, Double> stokes) {
        this.stokes = stokes;
    }
}
