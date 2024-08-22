package com.Cloud.Project.Slackify.Repository;

import com.Cloud.Project.Slackify.Entities.Comment;
import com.Cloud.Project.Slackify.Entities.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT * FROM comment WHERE comment_id = :commentID", nativeQuery = true)
    Optional<Comment> findByCommentID(String commentID);
}
