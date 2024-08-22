package com.Cloud.Project.Slackify.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "repository")
public class GitHubRepo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String repo_id;

    private String full_name;

    @ManyToOne
    @JoinColumn(name = "connect_id")
    private Connect connect;

    @Column(name = "owner_name")
    private String owner;

    @Column(name = "owner_url")
    private String ownerURL;

    private LocalDateTime createdAt;

    private String defaultBranch;

    private Boolean isPrivate;

    private String repoUrl;
}
