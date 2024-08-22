package com.Cloud.Project.Slackify.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChannelDTORequest {

    private String name;
    private String title;
    private String description;
    private String slackToken;
}
