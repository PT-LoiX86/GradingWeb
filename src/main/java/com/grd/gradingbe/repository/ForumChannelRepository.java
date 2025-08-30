package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.ForumChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ForumChannelRepository extends JpaRepository<ForumChannel, Long>
{
    @Query("SELECT c FROM ForumChannel c WHERE LOWER(c.name) LIKE LOWER(CONCAT(:search, '%')) OR :search IS NULL")
    Page<ForumChannel> findAllByName(Pageable pageable, @Param("search") String search);
}
