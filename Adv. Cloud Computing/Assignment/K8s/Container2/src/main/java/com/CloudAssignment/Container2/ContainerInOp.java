package com.CloudAssignment.Container2;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@RestController
@RequestMapping("/")
public class ContainerInOp {

    @Value("${FILE_PATH}")
    private String FILE_PATH;

    @PostMapping("calculate")
    public ResponseEntity<Map<String, Object>> calculateFile(@RequestBody Map<Object, Object> input) {

        System.out.println("testing");

        Map<String, Object> output = new HashMap<>();
        readFile(output, input);
        return ResponseEntity.ok(output);
    }

    private void readFile(Map<String, Object> output, Map<Object, Object> input) {

        if (input.get("file") == null || input.get("file").toString().isBlank()){
            output.put("file", null);
            output.put("error", "Invalid JSON input.");
            return;
        }

        String file = input.get("file").toString();
        String product = input.get("product").toString();

        String newFileName = file.replace(".dat", ".csv");

        File csvFile = new File(FILE_PATH + "/" + newFileName);

        if (!csvFile.exists()) {
            output.put("file", file);
            output.put("error", "File not found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int sum = 0;
            while ((line = br.readLine()) != null) {
                String[] currLine = line.split(",");
                if (currLine.length != 2) {
                    output.put("file", file);
                    output.put("error", "Input file not in CSV format.");
                    return;
                } else {
                    if (currLine[0].trim().equals(product)) {
                        sum += Integer.parseInt(currLine[1].trim());
                    }
                }
            }
            output.put("file", file);
            output.put("sum", sum);
        } catch (FileNotFoundException fileNotFoundException) {
            output.put("file", file);
            output.put("error", "File not found.");
        } catch (IOException ioException) {
            output.put("file", file);
            output.put("error", "Error reading the file.");
        } catch (NumberFormatException numberFormatException) {
            output.put("file", file);
            output.put("error", "Error parsing number from the file.");
        }
    }

}
