package com.Cloud.Project.Slackify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.session.token}")
    private String awsSessionToken;

    @Value("${secret.manager.name}")
    private String secretManagerName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public DataSource dataSource() {

        Map<String, String> dbDetails = getSecret();
        String dbUrl = dbDetails.get("host");
        String dbUser = dbDetails.get("username");
        String dbPassword = dbDetails.get("password");

        checkAndCreateDatabase(dbUrl, dbUser, dbPassword);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + dbUrl + ":3306/slackify");
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    private void checkAndCreateDatabase(String dbUrl, String dbUser, String dbPassword) {
        String jdbcUrl = "jdbc:mysql://" + dbUrl + ":3306/";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            Statement statement = connection.createStatement();
            String databaseName = "slackify";

            boolean databaseExists = false;
            var resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + databaseName + "'");
            if (resultSet.next()) {
                databaseExists = true;
            }

            if (!databaseExists) {
                statement.executeUpdate("CREATE DATABASE " + databaseName);
                System.out.println("Database '" + databaseName + "' created.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking or creating database", e);
        }
    }

    private Map<String, String> getSecret() {

        System.out.println(awsAccessKey);
        System.out.println(awsSecretKey);
        System.out.println(awsSessionToken);
        System.out.println(secretManagerName);

        Region region = Region.US_EAST_1;
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(awsAccessKey, awsSecretKey, awsSessionToken)))
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
}