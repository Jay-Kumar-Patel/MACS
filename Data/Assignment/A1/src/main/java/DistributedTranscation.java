import Models.Tweet;
import Models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributedTranscation {

    private List<String> tweetDatabaseConfig;
    private List<String> userDatabaseConfig;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");


    public DistributedTranscation(List<String> tweetDatabaseConfig, List<String> userDatabaseConfig){
        this.tweetDatabaseConfig = tweetDatabaseConfig;
        this.userDatabaseConfig = userDatabaseConfig;
    }


    public void executeInsertTransaction(List<String> quries){

        Connection tweetFragmentConnection = null;
        Connection userFragmentConnection = null;

        try {
            tweetFragmentConnection = DriverManager.getConnection(tweetDatabaseConfig.get(0), tweetDatabaseConfig.get(1), tweetDatabaseConfig.get(2));
            userFragmentConnection = DriverManager.getConnection(userDatabaseConfig.get(0), userDatabaseConfig.get(1), userDatabaseConfig.get(2));

            tweetFragmentConnection.setAutoCommit(false);
            userFragmentConnection.setAutoCommit(false);

            try {

                for (String query : quries){

                    Map<String, Object> dataMap = prepareData(query);

                    Tweet tweet = (Tweet) dataMap.get("Tweet");
                    User user = (User) dataMap.get("User");

                    int rowsAffectedTweet = insertTweetData(tweet, tweetFragmentConnection);
                    int rowsAffectedUser = insertUserData(user, userFragmentConnection);

                    if (rowsAffectedTweet > 0 && rowsAffectedUser > 0){
                        tweetFragmentConnection.commit();
                        userFragmentConnection.commit();
                        System.out.println("Transaction Successfully Done!");
                    }
                    else {
                        tweetFragmentConnection.rollback();
                        userFragmentConnection.rollback();
                        System.out.println("Transaction Failed");
                    }
                }
            }
            catch (SQLException exception){
                tweetFragmentConnection.rollback();
                userFragmentConnection.rollback();
                System.out.println("Transaction Failed");
                System.out.println("Error: " + exception);
            }
        }
        catch (SQLException exception){
            System.out.println("Database Connection Problem");
            System.out.println("Error : " + exception);
        }
        finally {
            try {
                if (tweetFragmentConnection != null){
                    tweetFragmentConnection.close();
                }

                if (userFragmentConnection != null){
                    userFragmentConnection.close();
                }
            }
            catch (SQLException sqlException){
                System.out.println("Error in Closing Connection!");
                System.out.println("Error: " + sqlException);
            }
        }
    }

    private int insertTweetData(Tweet tweet, Connection connection){

        String query = "INSERT INTO tweets (ID, UserID, URL, postedTime, content, type, client, retweetReceived, likeReceived, location, language) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, tweet.getID());
            statement.setString(2, tweet.getUserID());
            statement.setString(3, tweet.getURL());
            statement.setTimestamp(4, Timestamp.valueOf(tweet.getPostedTime()));
            statement.setString(5, tweet.getContent());
            statement.setString(6, tweet.getType());
            statement.setString(7, tweet.getClient());
            statement.setInt(8, tweet.getRetweetReceived());
            statement.setInt(9, tweet.getLikeReceived());
            statement.setString(10, tweet.getLocation());
            statement.setString(11, tweet.getLanguage());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected;
        }
        catch (SQLException exception){
            System.out.println(exception);
            return 0;
        }
    }

    private int insertUserData(User user, Connection connection)
    {
        String query = "INSERT INTO users (ID, name, username, bio, isVerified, URL, isProtected, followers, following, accountCreationDate, impressions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getID());
            statement.setString(2, user.getName());
            statement.setString(3, user.getUserName());
            statement.setString(4, user.getBio());
            statement.setBoolean(5, user.isVerified());
            statement.setString(6, user.getURL());
            statement.setBoolean(7, user.isProtected());
            statement.setInt(8, user.getFollowers());
            statement.setInt(9, user.getFollowing());
            statement.setTimestamp(10, Timestamp.valueOf(user.getAccountCreationDate()));
            statement.setInt(11, user.getImpressions());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected;
        }
        catch (SQLException exception){
            System.out.println(exception);
            return 0;
        }

    }

    private Map<String, Object> prepareData(String query){

        Map<String, Object> response = new HashMap<>();

        String values = query.substring(query.indexOf("values") + 6).trim();
        values = values.substring(1, values.length() - 1);
        String[] insertionData = values.split(",");

        Tweet tweet = new Tweet();
        tweet.setID(insertionData[0]);
        tweet.setUserID(insertionData[10]);
        tweet.setURL(insertionData[1]);
        tweet.setPostedTime(LocalDateTime.parse(insertionData[2], formatter));
        tweet.setContent(insertionData[3]);
        tweet.setType(insertionData[4]);
        tweet.setClient(insertionData[5]);
        tweet.setRetweetReceived(Integer.parseInt(insertionData[6]));
        tweet.setLikeReceived(Integer.parseInt(insertionData[7]));
        tweet.setLocation(insertionData[8]);
        tweet.setLanguage(insertionData[9]);


        User user = new User();
        user.setID(insertionData[10]);
        user.setName(insertionData[11]);
        user.setUserName(insertionData[12]);
        user.setBio(insertionData[13]);

        if (insertionData[14].equals("Non-Verified")){
            user.setVerified(false);
        }
        else {
            user.setVerified(true);
        }

        user.setURL(insertionData[15]);

        if (insertionData[16].equals("Non-Protected")){
            user.setProtected(false);
        }
        else {
            user.setProtected(true);
        }

        user.setFollowers(Integer.parseInt(insertionData[17]));
        user.setFollowing(Integer.parseInt(insertionData[18]));
        user.setAccountCreationDate(LocalDateTime.parse(insertionData[19], formatter));
        user.setImpressions(Integer.parseInt(insertionData[20]));


        response.put("Tweet", tweet);
        response.put("User", user);

        return  response;
    }

}
