package com.Cloud.Project.Slackify.Repository;

import com.Cloud.Project.Slackify.Entities.Connect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectRepository extends JpaRepository<Connect, Integer> {

    @Query(value = "SELECT * FROM connect WHERE username = :username AND repo_name = :repoName", nativeQuery = true)
    Optional<Connect> findByRepo(String username, String repoName);
}
