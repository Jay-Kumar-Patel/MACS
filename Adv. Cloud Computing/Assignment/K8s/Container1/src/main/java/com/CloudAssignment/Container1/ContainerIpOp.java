package com.CloudAssignment.Container1;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ContainerIpOp {

    @Value("${FILE_PATH}")
    private String FILE_PATH;

    @PostMapping("store-file")
    public ResponseEntity<Map<String, Object>> storeFile(@RequestBody Map<Object, Object> input){

        System.out.println("testing");

        Map<String, Object> output = new HashMap<>();

        checkFile(input, output);

        return ResponseEntity.ok(output);
    }

    private void checkFile(Map<Object, Object> input, Map<String, Object> output){

        if (!input.containsKey("file") || !input.containsKey("data") || input.get("data") == null){
            output.put("file", null);
            output.put("error", "Invalid JSON input.");
        } else {
            storeFile(input, output);
        }
    }

    private boolean storeFile(Map<Object, Object> input, Map<String, Object> output){

        String fileName = input.get("file").toString();
        String fileData = input.get("data").toString();

        String newFileName = "";

        if (!fileName.endsWith(".csv")) {
            newFileName = fileName.replace(".dat", ".csv");
        }

        File file = new File(FILE_PATH + "/" + newFileName);

        file.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileData.getBytes());
            output.put("file", fileName);
            output.put("message", "Success.");
            return true;
        } catch (IOException e) {
            output.put("file", fileName);
            output.put("error", "Error while storing the file to the storage.");
            return false;
        }
    }

    @PostMapping("calculate")
    public ResponseEntity<Map<String, Object>> calculateFile(@RequestBody Map<Object, Object> input) {
        try {
            System.out.println("Call Container2...");

            URI uri = new URI("http://container2-service:90/calculate");

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Map<Object, Object>> requestEntity = new HttpEntity<>(input);
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Map.class);

            System.out.println("Result: " + response.getBody());

            return ResponseEntity.ok(response.getBody());
        } catch (URISyntaxException e) {
            System.err.println("Invalid URI syntax: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid URI syntax"));
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

}