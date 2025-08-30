package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.ForumPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long>
{
    @Query("SELECT p FROM ForumPost p WHERE LOWER(p.title) LIKE LOWER(CONCAT(:search, '%')) OR :search IS NULL")
    Page<ForumPost> findAllByName(Pageable pageable, @Param("search") String search);
}
