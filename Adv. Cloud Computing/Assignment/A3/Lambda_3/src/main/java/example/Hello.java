package example;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.mindrot.jbcrypt.BCrypt;

public class Hello implements RequestHandler<Map<String, String>, Map<String, String>> {

    private static String bcryptHash(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt(12));
    }

    @Override
    public Map<String, String> handleRequest(Map<String, String> input, Context context) {

        System.out.println(input);

        String course_uri = input.get("course_uri");
        String action = input.get("action");
        String value = input.get("value");

        System.out.println(value);

        String bcryptHashed = bcryptHash(value);

        Map<String, String> payload = new HashMap<>();
        payload.put("banner", "B00982253");
        payload.put("result", bcryptHashed);
        payload.put("arn", "arn:aws:lambda:us-east-1:650857762222:function:Bcrypt");
        payload.put("action", "bcrypt");
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
}
