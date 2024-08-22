package Models;

import java.time.LocalDateTime;

public class User {

    private String ID;

    private String name;

    private String userName;

    private String bio;

    private boolean isVerified;

    private String URL;

    private boolean isProtected;

    private int followers;

    private int following;

    private LocalDateTime accountCreationDate;

    private int impressions;

    public User(){}

    public User(String ID, String name, String userName, String bio, boolean isVerified, String URL, boolean isProtected, int followers, int following, LocalDateTime accountCreationDate, int impressions) {
        this.ID = ID;
        this.name = name;
        this.userName = userName;
        this.bio = bio;
        this.isVerified = isVerified;
        this.URL = URL;
        this.isProtected = isProtected;
        this.followers = followers;
        this.following = following;
        this.accountCreationDate = accountCreationDate;
        this.impressions = impressions;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(LocalDateTime accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }


    @Override
    public String toString() {
        return "Models.User{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", bio='" + bio + '\'' +
                ", isVerified=" + isVerified +
                ", URL='" + URL + '\'' +
                ", isProtected=" + isProtected +
                ", followers=" + followers +
                ", following=" + following +
                ", accountCreationDate=" + accountCreationDate +
                ", impressions=" + impressions +
                '}';
    }
}
