package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.ForumComment;
import com.grd.gradingbe.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Long>
{
    Long countByForumPost(ForumPost forumPost);

    List<ForumComment> findByParentComment(ForumComment forumComment);
}
