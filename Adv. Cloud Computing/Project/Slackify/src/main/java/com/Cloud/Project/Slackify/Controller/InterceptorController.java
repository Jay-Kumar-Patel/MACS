package com.Cloud.Project.Slackify.Controller;

import com.Cloud.Project.Slackify.Models.*;
import com.Cloud.Project.Slackify.Service.SlackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class InterceptorController {

    private final SlackService slackService;

    @PostMapping("/event")
    public void handleWebhook(@RequestBody String payload, @RequestHeader("X-GitHub-Event") String event){

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        try {
            map = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
        }
        catch (JsonProcessingException exception){
            System.out.println(exception.getMessage());
        }

        EventType eventType = decodeEventType(map);

        if (eventType == null){
            System.out.println("Event not Supported by Slackify!");
        }
        else {
            System.out.println(slackService.performSlackOperation(eventType, map));
        }
    }

    private EventType decodeEventType(Map<String, Object> map){
        if (map != null && map.containsKey("action") && map.get("action").equals("opened")){
            return EventType.OPENED;
        }
        else if(map != null && map.containsKey("action") && map.get("action").equals("closed")){
            return EventType.CLOSED;
        }
        else if (map != null && map.containsKey("action") && map.get("action").equals("reopened")) {
            return EventType.REOPENED;
        }
        else if (map != null && map.containsKey("action") && map.get("action").equals("created")) {
            return EventType.CREATED;
        }
        else if (map != null && map.containsKey("action") && map.get("action").equals("edited")) {
            Map<String, Object> changes = (Map<String, Object>) map.get("changes");

            if (changes.containsKey("title")){
                return EventType.ISSUE_EDITED;
            }
            else {
                return EventType.COMMENT_EDITED;
            }
        }
        else if (map != null && map.containsKey("action") && map.get("action").equals("deleted")) {
            if (map.containsKey("comment")){
                return EventType.COMMENT_DELETED;
            }
            else {
                return EventType.ISSUE_DELETED;
            }
        }
        else {
            return null;
        }
    }
}
