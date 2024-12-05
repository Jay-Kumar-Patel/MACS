package Dao;

import java.util.Map;

public class ProfileDao {

    private int id;
    private String profileName;

    private Map<String, Integer> sectorHoldings;

    public ProfileDao(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Map<String, Integer> getSectorHoldings() {
        return sectorHoldings;
    }

    public void setSectorHoldings(Map<String, Integer> sectorHoldings) {
        this.sectorHoldings = sectorHoldings;
    }
}
