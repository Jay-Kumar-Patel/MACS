package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hello implements RequestHandler<Map<String, String>, Map<String, Object>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SnsClient snsClient = SnsClient.create();

    @Override
    public Map<String, Object> handleRequest(Map<String, String> input, Context context) {

        System.out.println("Hello");

        Map<String, Object> response = new HashMap<>();

        String userName = input.get("userName");
        String repoName = input.get("repoName");
        String githubToken = input.get("githubToken");
        String slackToken = input.get("slackToken");
        String userEmail = input.get("userEmail");

        // Validate userEmail
        if (userEmail == null || userEmail.isBlank()) {
            response.put("status", "Failure");
            response.put("message", "User email can't be empty!");
            return response;
        }

        // Create SNS topic name based on userEmail
        String userSNSTopic = "topic-" + userEmail.replaceAll("@", "_").replaceAll("\\.", "_");

        // Fetch all SNS topics
        List<SNS> allSNS = listAllSNS();

        if (allSNS == null) {
            response.put("status", "Failure");
            response.put("message", "Failed to fetch SNS topics.");
            return response;
        }

        boolean isPresent = false;
        String topicArn = null;

        // Check if the user topic already exists
        for (SNS currSNS : allSNS) {
            if (currSNS.getTopicName().equals(userSNSTopic)) {
                isPresent = true;
                topicArn = currSNS.getTopicArn();
                break;
            }
        }

        // If topic does not exist, create one and subscribe the user
        if (!isPresent) {
            String userTopicArn = createUserTopic(userSNSTopic);
            subscribeUserToSnsTopic(userEmail, userTopicArn);
            response.put("status", "Failure");
            response.put("message", "Subscription email sent. Please confirm your subscription.");
            return response;
        }

        // Check if the user is subscribed to the topic
        if (!isUserSubscribed(userEmail, topicArn)) {
            response.put("status", "Failure");
            response.put("message", "Please confirm your email subscription.");
            return response;
        }

        if (userName == null || userName.isBlank()) {
            response.put("status", "Failure");
            response.put("message", "User name of Github can't be empty!");
            return response;
        }

        if (repoName == null || repoName.isBlank()) {
            response.put("status", "Failure");
            response.put("message", "Repo name of Github can't be empty!");
            return response;
        }

        if (githubToken == null || githubToken.isBlank()) {
            response.put("status", "Failure");
            response.put("message", "Github token can't be empty!");
            return response;
        }

        if (slackToken == null || slackToken.isBlank()) {
            response.put("status", "Failure");
            response.put("message", "Slack token can't be empty!");
            return response;
        }

        String webHookCreated = createGitHubWebhook(userName, repoName, githubToken);

        Map<String, Object> map;
        try {
            map = objectMapper.readValue(webHookCreated, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException exception) {
            response.put("status", "Failure");
            response.put("message", "Error processing JSON response: " + exception.getMessage());
            return response;
        }

        if (!map.containsKey("errors")) {

            sendThankYouMessage(topicArn, userEmail);

            long connectID = storeConnectDataToDB(input);
            if (connectID > 0){
                if (storeRepoDataTODB(input, connectID)){
                    response.put("status", "Success");
                    response.put("message", webHookCreated);
                }
                else {
                    response.put("status", "Failed");
                    response.put("message", "Hook created successfully and connect data also stored, but failed to store repo data into RDS!");
                }
            }
            else {
                response.put("status", "Failed");
                response.put("message", "Hook created successfully, but failed to store data into RDS!");
            }
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) map.get("errors");
            String errorMessage = (String) errors.get(0).get("message");

            response.put("status", "Failed");
            response.put("message", errorMessage);
        }

        return response;
    }

    private void sendThankYouMessage(String topicArn, String userEmail) {
        try {

            setTopicDisplayName(topicArn, "Slackify");

            String message = String.format(
                    "Hello %s,\n\n" +
                            "Thank you for choosing Slackify! We're excited to have you on board and can't wait for you to explore all the features we offer.\n\n" +
                            "Slackify helps you streamline your workflows and keep your team connected effortlessly. If you have any questions or need assistance, feel free to reach out to our support team.\n\n" +
                            "Best Regards,\n" +
                            "The Slackify Team\n\n" +
                            "Your productivity partner in the digital age!",
                    userEmail
            );

            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .subject("Welcome to Slackify!")
                    .build();

            PublishResponse publishResponse = snsClient.publish(request);
            System.out.println(publishResponse);
        } catch (SnsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
    }


    private boolean isUserSubscribed(String userEmail, String topicArn) {
        try {
            ListSubscriptionsByTopicRequest request = ListSubscriptionsByTopicRequest.builder()
                    .topicArn(topicArn)
                    .build();

            ListSubscriptionsByTopicResponse result = snsClient.listSubscriptionsByTopic(request);
            for (Subscription subscription : result.subscriptions()) {
                if (subscription.endpoint().equals(userEmail) &&
                        subscription.subscriptionArn() != null &&
                        !subscription.subscriptionArn().equals("PendingConfirmation")) {
                    return true;
                }
            }
        } catch (SnsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return false;
    }

    private String createUserTopic(String topicName) {
        try {
            CreateTopicRequest request = CreateTopicRequest.builder()
                    .name(topicName)
                    .build();

            CreateTopicResponse result = snsClient.createTopic(request);
            return result.topicArn();
        } catch (SnsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Error creating SNS topic", e);
        }
    }

    public List<SNS> listAllSNS() {
        try {
            List<SNS> snsList = new ArrayList<>();
            String nextToken = null;

            do {
                ListTopicsRequest request = ListTopicsRequest.builder().nextToken(nextToken).build();
                ListTopicsResponse result = snsClient.listTopics(request);

                for (Topic topic : result.topics()) {
                    SNS sns = new SNS();
                    sns.setTopicArn(topic.topicArn());
                    sns.setTopicName(topic.topicArn().substring(topic.topicArn().lastIndexOf(":") + 1));
                    snsList.add(sns);
                }

                nextToken = result.nextToken();
            } while (nextToken != null);

            return snsList;
        } catch (SnsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
            return null;
        }
    }

    private void subscribeUserToSnsTopic(String userEmail, String userSNSTopicArn) {
        try {

            setTopicDisplayName(userSNSTopicArn, "Slackify");

            SubscribeRequest request = SubscribeRequest.builder()
                    .topicArn(userSNSTopicArn)
                    .protocol("email")
                    .endpoint(userEmail)
                    .build();

            snsClient.subscribe(request);
        } catch (SnsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
    }

    private void setTopicDisplayName(String topicArn, String displayName) {
        try {
            SetTopicAttributesRequest request = SetTopicAttributesRequest.builder()
                    .topicArn(topicArn)
                    .attributeName("DisplayName")
                    .attributeValue(displayName)
                    .build();

            snsClient.setTopicAttributes(request);
            System.out.println("DisplayName set successfully.");

        } catch (SnsException e) {
            System.err.println("Error setting topic attributes: " + e.awsErrorDetails().errorMessage());
        }
    }

    private Map<String, String> getSecret() {

        String secretManagerName = System.getenv("SECRET_MANAGER_NAME");

        Region region = Region.US_EAST_1;
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretManagerName)
                .build();

        GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

        String secret = getSecretValueResponse.secretString();

        try {
            return objectMapper.readValue(secret, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing secret JSON", e);
        }
    }

    private long storeConnectDataToDB(Map<String, String> input) {

        Map<String, String> dbDetails = getSecret();
        String dbUrl = dbDetails.get("host");
        String dbUser = dbDetails.get("username");
        String dbPassword = dbDetails.get("password");

        System.out.println(dbUrl);
        System.out.println(dbUser);
        System.out.println(dbPassword);

        String query = "INSERT INTO connect (github_token, repo_name, slack_token, timestamp, username) VALUES (?, ?, ?, ?, ?)";
        long connectId = -1;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + dbUrl + ":3306/slackify", dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, input.get("githubToken"));
            preparedStatement.setString(2, input.get("repoName"));
            preparedStatement.setString(3, input.get("slackToken"));
            preparedStatement.setString(4, LocalDateTime.now().toString());
            preparedStatement.setString(5, input.get("userName"));

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        connectId = generatedKeys.getLong(1);
                    }
                }
            }

            return connectId;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private boolean storeRepoDataTODB(Map<String, String> input, long connectID){

        Map<String, String> dbDetails = getSecret();
        String dbUrl = dbDetails.get("host");
        String dbUser = dbDetails.get("username");
        String dbPassword = dbDetails.get("password");

        System.out.println(dbUrl);
        System.out.println(dbUser);
        System.out.println(dbPassword);

        String query = "INSERT INTO repository (full_name, connect_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + dbUrl + ":3306/slackify", dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, input.get("userName") + "/" + input.get("repoName"));
            preparedStatement.setString(2, String.valueOf(connectID));

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    private String createGitHubWebhook(String userName, String repoName, String githubToken) {

        System.out.println("Hello5");

        String EC2_IP = System.getenv("EC2_IP_ADDRESS");

        System.out.println(EC2_IP);

        String url = "https://api.github.com/repos/" + userName + "/" + repoName + "/hooks";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            String authHeader = "token " + githubToken;
            httpPost.setHeader("Authorization", authHeader);
            httpPost.setHeader("Accept", "application/vnd.github.v3+json");
            httpPost.setHeader("Content-Type", "application/json");

            JsonObject config = new JsonObject();
            config.addProperty("url", "http://" + EC2_IP + ":9090/api/event");
            config.addProperty("content_type", "json");

            JsonObject payload = new JsonObject();
            payload.addProperty("name", "web");
            payload.add("config", config);
            payload.addProperty("active", true);
            payload.add("events", JsonParser.parseString("[\"issues\", \"issue_comment\"]"));

            StringEntity entity = new StringEntity(payload.toString());
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}