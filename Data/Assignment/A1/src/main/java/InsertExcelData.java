import Models.Tweet;
import Models.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertExcelData {

    Map<String, Tweet> tweetData;
    Map<String, User> userData;
    private List<String> tweetDatabaseConfig;
    private List<String> userDatabaseConfig;
    int tweetRepetitionCount = 0;
    int userRepetitionCount = 0;

    private String EXCEL_SHEET_FILE_PATH;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    public InsertExcelData(String filePath, List<String> tweetDatabaseConfig, List<String> userDatabaseConfig){
        this.EXCEL_SHEET_FILE_PATH = filePath;
        tweetData = new HashMap<>();
        userData = new HashMap<>();
        this.tweetDatabaseConfig = tweetDatabaseConfig;
        this.userDatabaseConfig = userDatabaseConfig;
    }

    public void readData(){

        boolean isHeader = false;

        tweetData.clear();
        userData.clear();
        tweetRepetitionCount = 0;
        userRepetitionCount = 0;

        try(CSVReader reader = new CSVReader(new FileReader(EXCEL_SHEET_FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {

                Tweet tweet = new Tweet();
                User user = new User();

                if (!isHeader){
                    isHeader = true;
                    continue;
                }

                if (tweetData.containsKey(nextLine[0])){
                    tweetRepetitionCount++;
                }
                else {
                    tweet.setID(nextLine[0]);
                    tweet.setUserID(nextLine[10]);
                    tweet.setURL(nextLine[1]);
                    tweet.setPostedTime(LocalDateTime.parse(nextLine[2], formatter));
                    tweet.setContent(nextLine[3]);
                    tweet.setType(nextLine[4]);
                    tweet.setClient(nextLine[5]);
                    tweet.setRetweetReceived(Integer.parseInt(nextLine[6]));
                    tweet.setLikeReceived(Integer.parseInt(nextLine[7]));
                    tweet.setLocation(nextLine[8]);
                    tweet.setLanguage(nextLine[9]);


                    tweetData.put(tweet.getID(), tweet);
                }


                if (userData.containsKey(nextLine[10])){
                    userRepetitionCount++;

                    User updateUserValue = userData.get(nextLine[10]);

                    updateUserValue.setFollowers(Integer.parseInt(nextLine[17]));
                    updateUserValue.setFollowing(Integer.parseInt(nextLine[18]));
                    updateUserValue.setImpressions(Integer.parseInt(nextLine[20]));

                    userData.put(nextLine[10], updateUserValue);
                }
                else{
                    user.setID(nextLine[10]);
                    user.setName(nextLine[11]);
                    user.setUserName(nextLine[12]);
                    user.setBio(nextLine[13]);

                    if (nextLine[14].equals("Non-Verified")){
                        user.setVerified(false);
                    }
                    else {
                        user.setVerified(true);
                    }

                    user.setURL(nextLine[15]);

                    if (nextLine[16].equals("Non-Protected")){
                        user.setProtected(false);
                    }
                    else {
                        user.setProtected(true);
                    }

                    user.setFollowers(Integer.parseInt(nextLine[17]));
                    user.setFollowing(Integer.parseInt(nextLine[18]));
                    user.setAccountCreationDate(LocalDateTime.parse(nextLine[19], formatter));
                    user.setImpressions(Integer.parseInt(nextLine[20]));


                    userData.put(user.getID(), user);
                }
            }

        }
        catch (CsvValidationException csvValidationException){
            System.out.println(csvValidationException);
        }
        catch (IOException ioException){
            System.out.println(ioException);
        }
    }



    public void insertDataTweet(){

        String query = "INSERT INTO tweets (ID, UserID, URL, postedTime, content, type, client, retweetReceived, likeReceived, location, language) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(tweetDatabaseConfig.get(0), tweetDatabaseConfig.get(1), tweetDatabaseConfig.get(2))) {

            PreparedStatement statement = connection.prepareStatement(query);

            for (Map.Entry<String, Tweet> entry : tweetData.entrySet()) {

                Tweet tweet = entry.getValue();

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

                statement.addBatch();
            }

            statement.executeBatch();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertDataUser(){

        String query = "INSERT INTO users (ID, name, username, bio, isVerified, URL, isProtected, followers, following, accountCreationDate, impressions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(userDatabaseConfig.get(0), userDatabaseConfig.get(1), userDatabaseConfig.get(2))) {

            PreparedStatement statement = connection.prepareStatement(query);

            for (Map.Entry<String, User> entry : userData.entrySet()) {

                User user = entry.getValue();

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

                statement.addBatch();
            }

            statement.executeBatch();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

}
