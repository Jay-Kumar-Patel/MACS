package com.Cloud.Project.Slackify.Service;

import com.Cloud.Project.Slackify.Entities.Comment;
import com.Cloud.Project.Slackify.Entities.Connect;
import com.Cloud.Project.Slackify.Entities.GitHubRepo;
import com.Cloud.Project.Slackify.Entities.Issue;
import com.Cloud.Project.Slackify.Models.EventType;
import com.Cloud.Project.Slackify.Models.Response;
import com.Cloud.Project.Slackify.Repository.CommentRepository;
import com.Cloud.Project.Slackify.Repository.ConnectRepository;
import com.Cloud.Project.Slackify.Repository.GitHubRepoRepository;
import com.Cloud.Project.Slackify.Repository.IssueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final Requests request;
    private final GitHubRepoRepository gitHubRepoRepository;
    private final ConnectRepository connectRepository;
    private final IssueRepository issueRepository;
    private final CommentRepository commentRepository;
    private String SLACK_TOKEN = "";

    private Issue fetchIssueInfo(Map<String, Object> map){

        System.out.println("Map: " + map);

        Map<String, Object> issueDetails = (Map<String, Object>) map.get("issue");

        String issueID = issueDetails.get("id").toString();
        String issueURL = issueDetails.get("html_url").toString();
        String issueTitle = issueDetails.get("title").toString();
        int issueNumber = Integer.parseInt(issueDetails.get("number").toString());
        String issueBody = issueDetails.get("body").toString();
        LocalDateTime issueCreatedAt = convertToLocalDateTime(issueDetails.get("created_at").toString());
        LocalDateTime issueUpdatedAt = convertToLocalDateTime(issueDetails.get("updated_at").toString());

        Map<String, Object> issueCreatedByDetails = (Map<String, Object>) issueDetails.get("user");

        String issueCreatdBy = issueCreatedByDetails.get("login").toString();
        String issueCreatedByProfileUrl = issueCreatedByDetails.get("html_url").toString();

        Issue issue = Issue.builder()
                .issue_id(issueID)
                .url(issueURL)
                .number(issueNumber)
                .title(issueTitle)
                .createdAt(issueCreatedAt)
                .createdBy(issueCreatdBy)
                .createdByURL(issueCreatedByProfileUrl)
                .updatedAt(issueUpdatedAt)
                .body(issueBody)
                .status(EventType.OPENED)
                .modified(false)
                .build();

        return issue;
    }


    private GitHubRepo fetchRepoInfo(Map<String, Object> map){
        Map<String, Object> repo = (Map<String, Object>) map.get("repository");

        String repoID = repo.get("id").toString();
        boolean isPrivate = Boolean.parseBoolean(repo.get("private").toString());
        String full_name = repo.get("full_name").toString();
        LocalDateTime repoCreatedAt = convertToLocalDateTime(repo.get("created_at").toString());
        String default_branch = repo.get("default_branch").toString();
        String repoURL = repo.get("html_url").toString();

        Map<String, Object> repoOwner = (Map<String, Object>) repo.get("owner");

        String repoOwnerName = repoOwner.get("login").toString();
        String repoOwnerProfileUrl = repoOwner.get("html_url").toString();

        GitHubRepo gitHubRepo = GitHubRepo.builder()
                .owner(repoOwnerName)
                .ownerURL(repoOwnerProfileUrl)
                .repo_id(repoID)
                .createdAt(repoCreatedAt)
                .defaultBranch(default_branch)
                .isPrivate(isPrivate)
                .repoUrl(repoURL)
                .full_name(full_name)
                .build();

        return gitHubRepo;
    }


    private Comment getCommentInfo(Map<String, Object> map){

        Map<String, Object> issueDetails = (Map<String, Object>) map.get("issue");
        String issueID = issueDetails.get("id").toString();

        Issue issue = new Issue();
        issue.setIssue_id(issueID);

        Map<String, Object> commentObject = (Map<String, Object>) map.get("comment");

        String commentID = commentObject.get("id").toString();
        LocalDateTime commentCreatedAt = convertToLocalDateTime(commentObject.get("created_at").toString());
        LocalDateTime commentUpdatedAt = convertToLocalDateTime(commentObject.get("updated_at").toString());
        String commentBody = commentObject.get("body").toString();

        Map<String, Object> commentCreatedByObject = (Map<String, Object>) commentObject.get("user");

        String commentCreatedBy = commentCreatedByObject.get("login").toString();
        String commentCreatedByProfileUrl = commentCreatedByObject.get("html_url").toString();

        Comment comment = Comment.builder()
                .comment_id(commentID)
                .createdAt(commentCreatedAt)
                .updatedAt(commentUpdatedAt)
                .createdBy(commentCreatedBy)
                .createdByURL(commentCreatedByProfileUrl)
                .issue(issue)
                .body(commentBody)
                .build();

        return comment;
    }

    private Connect getConnectInfo(String repoFullName){
        String[] splitfullName = repoFullName.split("/");

        Optional<Connect> connect = connectRepository.findByRepo(splitfullName[0], splitfullName[1]);

        if (connect != null && connect.isPresent()){
            SLACK_TOKEN = connect.get().getSlackToken();
            return connect.get();
        }

        return null;
    }

    private GitHubRepo getGitHubRepo(int connectID){
        Optional<GitHubRepo> optionalExistingRepoDetails = gitHubRepoRepository.findByConnectID(connectID);

        if (optionalExistingRepoDetails != null && optionalExistingRepoDetails.isPresent()){
            return optionalExistingRepoDetails.get();
        }

        return null;
    }

    private Response sendRequestForChannel(String url, Map<String, String> payload){
        Response response = new Response();

        String jsonRequest = "";
        try {
            jsonRequest = new Gson().toJson(payload);
        } catch (JsonIOException exception) {
            response.setStatus("Failure");
            response.setMessage(exception.getMessage());
            return response;
        }

        String channelCreated = request.postRequest(jsonRequest, url, SLACK_TOKEN);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> channelMap;

        try {
            channelMap = objectMapper.readValue(channelCreated, new TypeReference<Map<String, Object>>() {});
        }
        catch (JsonProcessingException exception){
            response.setStatus("Failure");
            response.setMessage(exception.getMessage());
            return response;
        }

        if (channelMap.get("ok").equals(true)){
            System.out.println(channelMap);
            Map<String, Object> channel = (Map<String, Object>) channelMap.get("channel");
            response.setStatus("Success");
            response.setMessage(channel.get("id").toString());
            return response;
        }
        else{
            response.setStatus("Failure");
            response.setMessage(channelMap.toString());
            return response;
        }
    }

    private Response sendRequestForMessage(String url, Map<String, String> payload){
        Response response = new Response();

        String jsonRequest = "";
        try {
            jsonRequest = new Gson().toJson(payload);
        } catch (JsonIOException exception) {
            response.setStatus("Failure");
            response.setMessage(exception.getMessage());
            return response;
        }

        String channelCreated = request.postRequest(jsonRequest, url, SLACK_TOKEN);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> channelMap;

        try {
            channelMap = objectMapper.readValue(channelCreated, new TypeReference<Map<String, Object>>() {});
        }
        catch (JsonProcessingException exception){
            response.setStatus("Failure");
            response.setMessage(exception.getMessage());
            return response;
        }

        if (channelMap.get("ok").equals(true)){
            response.setStatus("Success");
            response.setMessage(channelMap.get("ts").toString());
            return response;
        }
        else{
            response.setStatus("Failure");
            response.setMessage(channelMap.toString());
            return response;
        }
    }


    private Response createChannelOperations(Map<String, Object> map) {

        Response response = new Response();

        Issue issue = fetchIssueInfo(map);

        GitHubRepo gitHubRepo = fetchRepoInfo(map);

        System.out.println(gitHubRepo.getFull_name());
        Connect connect = getConnectInfo(gitHubRepo.getFull_name());

        if (connect == null){
            response.setStatus("Failure");
            response.setMessage("Connection on slackify doesn't exist!");
            return response;
        }

        GitHubRepo existingGitHubDetails = getGitHubRepo(connect.getId());

        if (existingGitHubDetails == null){
            response.setStatus("Failure");
            response.setMessage("Repo info on slackify doesn't exist!");
            return response;
        }

        if (existingGitHubDetails.getRepo_id() == null){
            existingGitHubDetails.setRepo_id(gitHubRepo.getRepo_id());
            existingGitHubDetails.setCreatedAt(gitHubRepo.getCreatedAt());
            existingGitHubDetails.setDefaultBranch(gitHubRepo.getDefaultBranch());
            existingGitHubDetails.setIsPrivate(gitHubRepo.getIsPrivate());
            existingGitHubDetails.setRepoUrl(gitHubRepo.getRepoUrl());
            existingGitHubDetails.setFull_name(gitHubRepo.getFull_name());
            existingGitHubDetails.setOwner(gitHubRepo.getOwner());
            existingGitHubDetails.setOwnerURL(gitHubRepo.getOwnerURL());

            gitHubRepoRepository.save(existingGitHubDetails);
        }

        issue.setRepo(existingGitHubDetails);

        String url = "https://slack.com/api/conversations.create";

        Map<String, String> payload = new HashMap<>();
        payload.put("name", "issue-" + issue.getNumber().toString());

        Response createChannelResponse = sendRequestForChannel(url, payload);

        if (createChannelResponse.equals("Failure")){
            return createChannelResponse;
        }
        else {
            String topicURL = "https://slack.com/api/conversations.setTopic";
            Map<String, String> payloadTopicURL = new HashMap<>();
            payloadTopicURL.put("channel", createChannelResponse.getMessage());
            payloadTopicURL.put("topic", issue.getTitle());

            sendRequestForChannel(topicURL, payloadTopicURL);

            issue.setChannel_id(createChannelResponse.getMessage());
            issueRepository.save(issue);

            String firstMessageUrl = "https://slack.com/api/chat.postMessage";

            String messageText = "🚨 *New GitHub Issue Opened* 🚨\n\n" +
                    "*Issue: [" + issue.getTitle() + "](" + issue.getUrl() + ")*\n\n" +
                    "*Repository*: " + issue.getRepo().getRepoUrl() + "\n" +
                    "*Opened by*: [" + issue.getCreatedBy() + "](" + issue.getCreatedByURL() + ")";

            Map<String, String> firstMessagePayload = new HashMap<>();
            firstMessagePayload.put("channel", createChannelResponse.getMessage());
            firstMessagePayload.put("text", messageText);

            sendRequestForMessage(firstMessageUrl, firstMessagePayload);
        }

        return createChannelResponse;
    }

    private Issue getIssueUpdateDetails(Map<String, Object> map){
        Map<String, Object> issue = (Map<String, Object>) map.get("issue");

        LocalDateTime issueUpdatedAt = convertToLocalDateTime(issue.get("updated_at").toString());

        Map<String, Object> issueUpdatedByObject = (Map<String, Object>) issue.get("user");

        String issueUpdatedBy = issueUpdatedByObject.get("login").toString();
        String issueUpdatedByProfileURL = issueUpdatedByObject.get("html_url").toString();

        Issue updateDetailsIssue = Issue.builder()
                .updatedBy(issueUpdatedBy)
                .updatedByURL(issueUpdatedByProfileURL)
                .updatedAt(issueUpdatedAt)
                .build();

        return updateDetailsIssue;
    }

    private Response updateChannelForClosedReOpened(Map<String, Object> map, EventType eventType){
        Response response = new Response();

        Issue issue = fetchIssueInfo(map);

        GitHubRepo gitHubRepo = fetchRepoInfo(map);

        Connect connect = getConnectInfo(gitHubRepo.getFull_name());

        if (connect == null){
            response.setStatus("Failure");
            response.setMessage("Connection on slackify doesn't exist!");
            return response;
        }

        GitHubRepo existingGitHubDetails = getGitHubRepo(connect.getId());

        if (existingGitHubDetails == null){
            response.setStatus("Failure");
            response.setMessage("Repo info on slackify doesn't exist!");
            return response;
        }

        Optional<Issue> optionalIssue = issueRepository.findByIssueID(issue.getIssue_id());

        if (optionalIssue == null || !optionalIssue.isPresent()){
            response.setStatus("Failure");
            response.setMessage("Issue doesn't exist on Slackify!");
            return response;
        }

        Issue updateDetailsIssue = getIssueUpdateDetails(map);

        Issue existingIssue = optionalIssue.get();
        existingIssue.setUpdatedAt(updateDetailsIssue.getUpdatedAt());
        existingIssue.setUpdatedBy(updateDetailsIssue.getUpdatedBy());
        existingIssue.setUpdatedByURL(updateDetailsIssue.getUpdatedByURL());
        existingIssue.setStatus(eventType);

        Issue savedIssue = issueRepository.save(existingIssue);

        response.setStatus("Success");
        response.setMessage(savedIssue.getChannel_id());

        String messageText;

        if(eventType.equals(EventType.CLOSED)){
            messageText = "🎉 *GitHub Issue Closed* 🎉\n\n" +
                    "*Issue: [" + existingIssue.getTitle() + "](" + existingIssue.getUrl() + ")*\n\n" +
                    "*Repository*: " + existingIssue.getRepo().getRepoUrl() + "\n" +
                    "*Closed by*: [" + issue.getCreatedBy() + "](" + issue.getCreatedByURL() + ")";

        }
        else {
            messageText = "🚨 *GitHub Issue Re-Opened* 🚨\n\n" +
                    "*Issue: [" + existingIssue.getTitle() + "](" + existingIssue.getUrl() + ")*\n\n" +
                    "*Repository*: " + existingIssue.getRepo().getRepoUrl() + "\n" +
                    "*Reopened by*: [" + issue.getCreatedBy() + "](" + issue.getCreatedByURL() + ")";
        }

        String url = "https://slack.com/api/chat.postMessage";

        Map<String, String> payload = new HashMap<>();
        payload.put("channel", response.getMessage());
        payload.put("text", messageText);

        return sendRequestForMessage(url, payload);
    }

    private String getOldTitle(Map<String, Object> map){
        Map<String, Object> modification = (Map<String, Object>) map.get("changes");
        Map<String, Object> oldTitleObject = (Map<String, Object>) modification.get("title");

        return oldTitleObject.get("from").toString();
    }

    private Response editedChannel(Map<String, Object> map){
        Response response = new Response();

        Issue issue = fetchIssueInfo(map);

        GitHubRepo gitHubRepo = fetchRepoInfo(map);

        Connect connect = getConnectInfo(gitHubRepo.getFull_name());

        if (connect == null){
            response.setStatus("Failure");
            response.setMessage("Connection on slackify doesn't exist!");
            return response;
        }

        GitHubRepo existingGitHubDetails = getGitHubRepo(connect.getId());

        if (existingGitHubDetails == null){
            response.setStatus("Failure");
            response.setMessage("Repo info on slackify doesn't exist!");
            return response;
        }

        Optional<Issue> optionalIssue = issueRepository.findByIssueID(issue.getIssue_id());

        if (optionalIssue == null || !optionalIssue.isPresent()){
            response.setStatus("Failure");
            response.setMessage("Issue doesn't exist on Slackify!");
            return response;
        }

        String oldTitle = getOldTitle(map);
        Issue existingIssue = optionalIssue.get();

        Issue updateDetailsIssue = getIssueUpdateDetails(map);

        existingIssue.setUpdatedAt(updateDetailsIssue.getUpdatedAt());
        existingIssue.setUpdatedBy(updateDetailsIssue.getUpdatedBy());
        existingIssue.setUpdatedByURL(updateDetailsIssue.getUpdatedByURL());
        existingIssue.setModified(true);
        existingIssue.setTitle(issue.getTitle());

        Issue savedIssue = issueRepository.save(existingIssue);

        String topicURL = "https://slack.com/api/conversations.setTopic";
        Map<String, String> payloadTopicURL = new HashMap<>();
        payloadTopicURL.put("channel", savedIssue.getChannel_id());
        payloadTopicURL.put("topic", issue.getTitle());

        sendRequestForChannel(topicURL, payloadTopicURL);

        String urlEdited = "https://slack.com/api/chat.postMessage";

        Map<String, String> payloadEdited = new HashMap<>();
        payloadEdited.put("channel", savedIssue.getChannel_id());
        payloadEdited.put("text", "Channel topic changed from '" + oldTitle + "' to '" + issue.getTitle() + "' by " + issue.getCreatedBy());

        Response editChannelResponse = sendRequestForMessage(urlEdited, payloadEdited);

        response.setStatus("Success");
        response.setMessage("Channel title is been edited!");

        return response;
    }

    private Response deletedChannel(Map<String, Object> map){
        Response response = new Response();

        Issue issue = fetchIssueInfo(map);

        GitHubRepo gitHubRepo = fetchRepoInfo(map);

        Connect connect = getConnectInfo(gitHubRepo.getFull_name());

        if (connect == null){
            response.setStatus("Failure");
            response.setMessage("Connection on slackify doesn't exist!");
            return response;
        }

        GitHubRepo existingGitHubDetails = getGitHubRepo(connect.getId());

        if (existingGitHubDetails == null){
            response.setStatus("Failure");
            response.setMessage("Repo info on slackify doesn't exist!");
            return response;
        }

        Optional<Issue> optionalIssue = issueRepository.findByIssueID(issue.getIssue_id());

        if (optionalIssue == null || !optionalIssue.isPresent()){
            response.setStatus("Failure");
            response.setMessage("Issue doesn't exist on Slackify!");
            return response;
        }

        Issue existingIssue = optionalIssue.get();

        Issue updateDetailsIssue = getIssueUpdateDetails(map);

        existingIssue.setUpdatedAt(updateDetailsIssue.getUpdatedAt());
        existingIssue.setUpdatedBy(updateDetailsIssue.getUpdatedBy());
        existingIssue.setUpdatedByURL(updateDetailsIssue.getUpdatedByURL());
        existingIssue.setStatus(EventType.ISSUE_DELETED);

        Issue savedIssue = issueRepository.save(existingIssue);

        String messageText = "🚨 *GitHub Issue Deleted* 🚨\n\n" +
                "*Issue: [" + existingIssue.getTitle() + "](" + existingIssue.getUrl() + ")*\n\n" +
                "*Repository*: " + existingIssue.getRepo().getRepoUrl() + "\n" +
                "*Deleted by*: [" + issue.getCreatedBy() + "](" + issue.getCreatedByURL() + ")";

        String url = "https://slack.com/api/chat.postMessage";

        Map<String, String> payload = new HashMap<>();
        payload.put("channel", savedIssue.getChannel_id());
        payload.put("text", messageText);

        return sendRequestForMessage(url, payload);
    }

    public Response performSlackOperation(EventType eventType, Map<String, Object> map) {

        if (eventType.equals(EventType.OPENED))
        {
            return createChannelOperations(map);
        }
        else if (eventType.equals(EventType.CLOSED))
        {
            return updateChannelForClosedReOpened(map, EventType.CLOSED);
        }
        else if (eventType.equals(EventType.REOPENED))
        {
            return updateChannelForClosedReOpened(map, EventType.REOPENED);
        }
        else if (eventType.equals(EventType.ISSUE_EDITED))
        {
            return editedChannel(map);
        }
        else if (eventType.equals(EventType.ISSUE_DELETED))
        {
            return deletedChannel(map);
        }
        else if (eventType.equals(EventType.CREATED)){
            return createMessage(map);
        }
        else if(eventType.equals(EventType.COMMENT_EDITED)){
            return editDeleteComment(map, EventType.COMMENT_EDITED, "https://slack.com/api/chat.update", true);
        }
        else if(eventType.equals(EventType.COMMENT_DELETED)){
            return editDeleteComment(map, EventType.COMMENT_DELETED, "https://slack.com/api/chat.delete", false);
        }
        else {
            //DO Nothing
            return null;
        }
    }

    private Response createMessage(Map<String, Object> map){

        Response response = new Response();

        Comment comment = getCommentInfo(map);

        Optional<Issue> optionalIssue = issueRepository.findByIssueID(comment.getIssue().getIssue_id());

        if (optionalIssue == null || !optionalIssue.isPresent()){
            response.setStatus("Failure");
            response.setMessage("Issue doesn't exist on Slackify!");
            return response;
        }

        Issue existingIssue = optionalIssue.get();

        comment.setIssue(existingIssue);
        comment.setStatus(EventType.CREATED);

        SLACK_TOKEN = existingIssue.getRepo().getConnect().getSlackToken();

        String url = "https://slack.com/api/chat.postMessage";

        Map<String, String> payload = new HashMap<>();
        payload.put("channel", existingIssue.getChannel_id());
        payload.put("text", comment.getBody() + "\nsent by: " + comment.getCreatedBy());

        Response commentResponse = sendRequestForMessage(url, payload);

        if (commentResponse.getStatus().equals("Success")){
            comment.setTs(commentResponse.getMessage());
            commentRepository.save(comment);
        }

        return commentResponse;
    }

    private Response editDeleteComment(Map<String, Object> map, EventType eventType, String url, boolean isText){
        Response response = new Response();

        Comment comment = getCommentInfo(map);

        Optional<Comment> optionalComment = commentRepository.findByCommentID(comment.getComment_id());

        if (optionalComment == null || !optionalComment.isPresent()){
            response.setStatus("Failure");
            response.setMessage("Comment doesn't exist on Slackify!");
            return response;
        }

        Optional<Issue> optionalIssue = issueRepository.findByIssueID(comment.getIssue().getIssue_id());

        if (optionalIssue == null || !optionalIssue.isPresent()){
            response.setStatus("Failure");
            response.setMessage("Issue doesn't exist on Slackify!");
            return response;
        }

        Comment existingComment = optionalComment.get();
        existingComment.setUpdatedAt(comment.getCreatedAt());
        existingComment.setUpdatedBy(comment.getCreatedBy());
        existingComment.setStatus(eventType);

        SLACK_TOKEN = optionalIssue.get().getRepo().getConnect().getSlackToken();

        Map<String, String> payload = new HashMap<>();
        payload.put("channel", optionalIssue.get().getChannel_id());
        payload.put("ts", existingComment.getTs());

        if (isText){
            payload.put("text", comment.getBody() + "\nedited by: " + comment.getCreatedBy());
        }

        Response commentResponse = sendRequestForMessage(url, payload);

        if (commentResponse.getStatus().equals("Success")){
            commentRepository.save(existingComment);
        }

        return commentResponse;
    }


    private LocalDateTime convertToLocalDateTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(timestamp, formatter);
    }
}