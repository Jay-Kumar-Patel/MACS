package com.Cloud.Project.Slackify.Repository;

import com.Cloud.Project.Slackify.Entities.GitHubRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GitHubRepoRepository extends JpaRepository<GitHubRepo, Integer> {

    @Query(value = "SELECT * FROM repository WHERE connect_id = :connectID", nativeQuery = true)
    Optional<GitHubRepo> findByConnectID(int connectID);

}
