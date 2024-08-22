package Models;

import java.time.LocalDateTime;

public class Tweet {

    private String ID;

    private String UserID;

    private String URL;

    private LocalDateTime postedTime;

    private String content;

    private String type;

    private String client;

    private int retweetReceived;

    private int likeReceived;

    private String location;

    private String language;


    public Tweet(){}

    public Tweet(String ID, String userID, String URL, LocalDateTime postedTime, String content, String type, String client, int retweetReceived, int likeReceived, String location, String language) {
        this.ID = ID;
        UserID = userID;
        this.URL = URL;
        this.postedTime = postedTime;
        this.content = content;
        this.type = type;
        this.client = client;
        this.retweetReceived = retweetReceived;
        this.likeReceived = likeReceived;
        this.location = location;
        this.language = language;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public LocalDateTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(LocalDateTime postedTime) {
        this.postedTime = postedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getRetweetReceived() {
        return retweetReceived;
    }

    public void setRetweetReceived(int retweetReceived) {
        this.retweetReceived = retweetReceived;
    }

    public int getLikeReceived() {
        return likeReceived;
    }

    public void setLikeReceived(int likeReceived) {
        this.likeReceived = likeReceived;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Models.Tweet{" +
                "ID='" + ID + '\'' +
                ", UserID='" + UserID + '\'' +
                ", URL='" + URL + '\'' +
                ", postedTime=" + postedTime +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", client='" + client + '\'' +
                ", retweetReceived=" + retweetReceived +
                ", likeReceived=" + likeReceived +
                ", location='" + location + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
