package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.ForumComment;
import com.grd.gradingbe.model.ForumPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Long>
{
    Long countByForumPost(ForumPost forumPost);

    List<ForumComment> findByParentComment(ForumComment forumComment);

    @Query("SELECT f FROM ForumComment f WHERE f.parentComment IS NULL")
    Page<ForumComment> findAllRootComment(Pageable pageable);
}
