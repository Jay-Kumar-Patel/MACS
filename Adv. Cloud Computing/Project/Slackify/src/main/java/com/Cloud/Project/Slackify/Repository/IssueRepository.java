package com.Cloud.Project.Slackify.Repository;

import com.Cloud.Project.Slackify.Entities.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {

    @Query(value = "SELECT * FROM issue WHERE issue_id = :issueID", nativeQuery = true)
    Optional<Issue> findByIssueID(String issueID);
}
