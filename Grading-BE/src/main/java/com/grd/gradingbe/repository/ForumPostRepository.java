package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long>
{
}
