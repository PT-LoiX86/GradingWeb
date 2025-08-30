package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.ForumMedia;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ForumMediaRepository extends JpaRepository<ForumMedia, Long>
{
    List<ForumMedia> findAllByForumPostId(Long postId);

    List<ForumMedia> findAllByForumCommentId(Long commentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ForumMedia m WHERE m.forumPost.id = :postId AND m.url IN :urls")
    void deleteByPostIdAndUrl(@Param("postId") Long postId, @Param("urls") Collection<String> urls);

    @Modifying
    @Transactional
    @Query("DELETE FROM ForumMedia m WHERE m.forumComment.id = :commentId AND m.url IN :urls")
    void deleteByCommentIdAndUrl(@Param("commentId") Long commentId, @Param("urls") Collection<String> urls);
}
