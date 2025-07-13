package com.grd.gradingbe.configuration;

import com.grd.gradingbe.service.MajorService;
import com.grd.gradingbe.service.ProvinceService;
import com.grd.gradingbe.service.SchoolService;
import com.grd.gradingbe.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Cache Warming Component
 * Pre-load frequently accessed data vào cache khi application khởi động
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheWarmer {
    
    private final ProvinceService provinceService;
    private final SchoolService schoolService;
    private final MajorService majorService;
    private final UniversityService universityService;
    
    /**
     * Warm cache sau khi application ready
     * Chạy async để không block application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void warmCache() {
        log.info("Starting cache warming process...");
        
        try {
            // Warm master data cache (provinces, schools)
            warmMasterDataCache();
            
            // Warm reference data cache (majors, universities) 
            warmReferenceDataCache();
            
            log.info("Cache warming completed successfully");
            
        } catch (Exception e) {
            log.error("Error during cache warming: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Pre-load master data (provinces, schools)
     */
    private void warmMasterDataCache() {
        log.info("Warming master data cache...");
        
        // Load all provinces
        provinceService.getAllProvinces();
        log.debug("Provinces cache warmed");
        
        // Load all schools  
        schoolService.getAllSchools();
        log.debug("Schools cache warmed");
    }
    
    /**
     * Pre-load reference data (majors, universities)
     * Load first few pages để warm pagination cache
     */
    private void warmReferenceDataCache() {
        log.info("Warming reference data cache...");
        
        // Load first 3 pages of majors
        for (int page = 0; page < 3; page++) {
            majorService.getAllMajors(page, 10, "id", "asc", null);
        }
        log.debug("Majors cache warmed (pages 0-2)");
        
        // Load first 3 pages of universities
        for (int page = 0; page < 3; page++) {
            universityService.getAllUniversities(page, 10, "id", "asc", null);
        }
        log.debug("Universities cache warmed (pages 0-2)");
    }
}
