package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Hello implements RequestHandler<Map<String, String>, Map<String, String>> {

    private static String sha256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            System.out.println("Encoded: " + encodedhash);

            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        System.out.println("Hex: " + hexString.toString());
        return hexString.toString();
    }

    private void sendPostRequest(String payload, String course_uri) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(course_uri);
            httpPost.setEntity(new StringEntity(payload, "UTF-8"));
            httpPost.setHeader("Content-Type", "application/json");
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                System.out.println("Response Status: " + response.getStatusLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Map<String, String> handleRequest(Map<String, String> input, Context context) {

        System.out.println(input);

        String course_uri = input.get("course_uri");
        String action = input.get("action");
        String value = input.get("value");

        System.out.println(value);

        String sha256Hashed = sha256Hash(value);

        Map<String, String> payload = new HashMap<>();
        payload.put("banner", "B00982253");
        payload.put("result", sha256Hashed);
        payload.put("arn", "arn:aws:lambda:us-east-1:650857762222:function:SHA-256");
        payload.put("action", "sha256");
        payload.put("value", value);

        System.out.println("Payload: " + payload);

        String jsonResponse = "";
        try {
            jsonResponse = new Gson().toJson(payload);
        }
        catch (JsonParseException exception){
            System.out.println("Json Parse Exception: " + exception.getMessage());
        }

        sendPostRequest(jsonResponse, course_uri);

        return payload;
    }
}