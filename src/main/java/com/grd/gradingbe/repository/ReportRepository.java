package com.grd.gradingbe.repository;

import com.grd.gradingbe.dto.enums.ContentType;
import com.grd.gradingbe.dto.enums.ReasonType;
import com.grd.gradingbe.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>
{
    @Query("SELECT r FROM Report r WHERE "
            + "(:contentType IS NULL OR r.content_type = :contentType) "
            + "AND (:reason IS NULL OR r.reason = :reason)")
    Page<Report> findAllByContentTypeAndReason(Pageable pageable, @Param("contentType") ContentType contentType, @Param("reason") ReasonType reason);
}
