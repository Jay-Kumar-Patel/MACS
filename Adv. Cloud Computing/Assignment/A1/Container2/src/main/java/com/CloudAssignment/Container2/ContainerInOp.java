package com.CloudAssignment.Container2;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class ContainerInOp {

    @Value("${FILE_PATH}")
    private String FILE_PATH;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> operation
            (@RequestParam(value = "file") String file,
             @RequestParam(value = "product") String product)
    {

        System.out.println(file + " " + product);

        Map<String, Object> output = new HashMap<>();

        readFile(output, file, product);

        System.out.println("C2: " + output);

        return ResponseEntity.ok(output);
    }

    private void readFile(Map<String, Object> output, String file, String product){

        Properties properties = new Properties();

        try {
            InputStream inputStream = new FileInputStream(FILE_PATH + "/" + file);

            properties.load(inputStream);

            Set<Object> keys = properties.keySet();

            int sum = 0;

            for (Object data : keys){
                String[] currLine = data.toString().split(",");
                if (currLine.length != 2){
                    output.put("file", output.get("file"));
                    output.put("error", "Input file not in CSV format.");

                    return;
                }
                else{
                    if (currLine[0].equals(product)){
                        sum += Integer.valueOf(currLine[1]);
                    }
                }
            }

            output.put("file", file);
            output.put("sum", sum);
        }
        catch (FileNotFoundException fileNotFoundException){
            output.put("file", file);
            output.put("error", "File not found.");
        }
        catch (IOException ioException){
            output.put("file", file);
            output.put("error", "Input file not in CSV format.");
        }

    }

}
