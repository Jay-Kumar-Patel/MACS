package com.CloudAssignment.Container1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@RestController
@RequestMapping("/calculate")
public class ContainerIpOp {

    @Value("${FILE_PATH}")
    private String FILE_PATH;

    @PostMapping()
    public ResponseEntity<Map<String, Object>> operation(@RequestBody Map<Object, Object> input){

        Map<String, Object> output = new HashMap<>();

        checkFile(input, output);

        return ResponseEntity.ok(output);
    }

    private void checkFile(Map<Object, Object> input, Map<String, Object> output){

        if (!input.containsKey("file") || !input.containsKey("product") || input.get("file") == null){
            output.put("file", null);
            output.put("error", "Invalid JSON input.");
        }else if (readFile(input, output)){
            System.out.println("Call Container2...");

            String url = "http://container2:7000/calculate?file=" + input.get("file") + "&product="+ input.get("product");
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> result = restTemplate.getForObject(url, Map.class);

            System.out.println("Result: " + result);

            output.putAll(result);
        }
    }

    private boolean readFile(Map<Object, Object> input, Map<String, Object> output){

        Properties properties = new Properties();

        try {
            InputStream inputStream = new FileInputStream(FILE_PATH + "/" + input.get("file"));

            properties.load(inputStream);

            Set<Object> keys = properties.keySet();

            for (Object data : keys){
                String[] currLine = data.toString().split(",");
                if (currLine.length != 2){
                    output.put("file", input.get("file"));
                    output.put("error", "Input file not in CSV format.");

                    return false;
                }
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            output.put("file", input.get("file"));
            output.put("error", "File not found.");

            return false;
        }
        catch (IOException ioException){
            output.put("file", input.get("file"));
            output.put("error", "Input file not in CSV format.");

            return false;
        }

        return true;
    }

}
