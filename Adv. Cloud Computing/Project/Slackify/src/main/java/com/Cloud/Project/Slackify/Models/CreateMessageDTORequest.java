package com.Cloud.Project.Slackify.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageDTORequest {

    private String channelName;
    private String message;
    private String slackToken;
}
