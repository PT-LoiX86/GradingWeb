## Redis Caching Strategy cho Dự án Grading Web

Dựa trên phân tích các API hiện có trong dự án, đây là chiến lược caching Redis được khuyến nghị.

### 🎯 Nguyên tắc chung về Caching

**Nên cache những gì:**
- Dữ liệu **ít thay đổi** (provinces, majors, universities, schools)
- Dữ liệu **được truy cập thường xuyên** (danh sách public, profile data)
- **Kết quả tính toán phức tạp** (search results, aggregations)
- **Dữ liệu master data** (reference data)

**Không nên cache:**
- Dữ liệu **thay đổi liên tục** (real-time notifications)
- **Thông tin nhạy cảm** (passwords, tokens)
- **Dữ liệu cá nhân hóa cao** (user-specific real-time data)

### 📊 APIs cần Cache (theo độ ưu tiên)

#### 🔴 Priority 1 - Master Data (Cache lâu dài: 24-48h)

**1. Provinces API**
```java
// GET /api/provinces
@Cacheable(value = "provinces", key = "'all'")
public List<ProvinceResponse> getAllProvinces()

// GET /api/provinces/{id}  
@Cacheable(value = "provinces", key = "#id")
public Optional<ProvinceResponse> getProvinceById(Long id)
```
**Lý do:** Tỉnh thành ít khi thay đổi, được truy cập nhiều khi user đăng ký

**2. Schools API**
```java
// GET /api/schools
@Cacheable(value = "schools", key = "'all'")
public List<SchoolResponse> getAllSchools()

// GET /api/schools/{id}
@Cacheable(value = "schools", key = "#id") 
public Optional<SchoolResponse> getSchoolById(Long id)
```
**Lý do:** Danh sách trường THPT ổn định, cần thiết cho form đăng ký

#### 🟠 Priority 2 - Reference Data (Cache trung hạn: 6-12h)

**3. Majors API**
```java
// GET /api/majors (with pagination & search)
@Cacheable(value = "majors", key = "'page:' + #page + ':size:' + #size + ':sort:' + #sortBy + ':' + #sortDir + ':search:' + (#search ?: 'none')")
public PageResponse<MajorResponse> getAllMajors(int page, int size, String sortBy, String sortDir, String search)

// GET /api/majors/{id}
@Cacheable(value = "majors", key = "#id")
public MajorResponse getMajorById(Long id)
```

**4. Universities API**
```java
// GET /api/universities (with pagination & search)
@Cacheable(value = "universities", key = "'page:' + #page + ':size:' + #size + ':sort:' + #sortBy + ':' + #sortDir + ':search:' + (#search ?: 'none')")
public PageResponse<UniversityResponse> getAllUniversities(int page, int size, String sortBy, String sortDir, String search)

// GET /api/universities/{id}
@Cacheable(value = "universities", key = "#id")
public UniversityResponse getUniversityById(Long id)
```

#### 🟡 Priority 3 - User Data (Cache ngắn hạn: 15-30 phút)

**5. Student Profiles (public view)**
```java
// GET /api/student-profiles (pagination)
@Cacheable(value = "student-profiles", key = "'page:' + #page + ':size:' + #size", condition = "#page < 10")
public List<StudentProfileResponse> getAllStudentProfiles(int page, int size)

// GET /api/student-profiles/{id} (public profile view)
@Cacheable(value = "student-profiles", key = "#id")
public Optional<StudentProfileResponse> getStudentProfileById(Long id)
```

**6. User Profile Data**
```java
// GET /api/users/me
@Cacheable(value = "user-profiles", key = "#userId", condition = "#userId != null")
public UserDataResponse getUserData(String userId)
```

### 🛠️ Implementation Strategy

#### 1. Cache Configuration

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
            // Master data - cache lâu
            "provinces", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(48))
                .disableCachingNullValues(),
                
            "schools", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24))
                .disableCachingNullValues(),
                
            // Reference data - cache trung bình  
            "majors", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(12))
                .disableCachingNullValues(),
                
            "universities", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(12))
                .disableCachingNullValues(),
                
            // User data - cache ngắn
            "student-profiles", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues(),
                
            "user-profiles", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15))
                .disableCachingNullValues()
        );

        return RedisCacheManager.builder(connectionFactory)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build();
    }
}
```

#### 2. Cache Eviction Strategy

```java
// Update operations
@CacheEvict(value = "provinces", allEntries = true)
public ProvinceResponse updateProvince(Long id, ProvinceRequest request)

@CacheEvict(value = "provinces", key = "#id")  
public void deleteProvince(Long id)

// Evict specific patterns for pagination
@CacheEvict(value = "majors", allEntries = true)
public MajorResponse createMajor(MajorRequest request)
```

#### 3. Conditional Caching

```java
// Chỉ cache những page đầu tiên của pagination
@Cacheable(value = "majors", key = "'page:' + #page + ':size:' + #size", 
           condition = "#page < 5 && #search == null")
public PageResponse<MajorResponse> getAllMajors(int page, int size, String search)

// Không cache khi có search query phức tạp
@Cacheable(value = "universities", condition = "#search == null || #search.length() > 2")
public PageResponse<UniversityResponse> searchUniversities(String search)
```

### 📈 Monitoring và Optimization

#### 1. Cache Metrics
```java
// Track cache hit/miss rates
@Component
public class CacheMetrics {
    
    @EventListener
    public void onCacheHit(CacheEvent event) {
        // Log cache hit metrics
    }
    
    @EventListener  
    public void onCacheMiss(CacheEvent event) {
        // Log cache miss metrics
    }
}
```

#### 2. Cache Warming Strategy
```java
@Component
public class CacheWarmer {
    
    @EventListener(ApplicationReadyEvent.class)
    public void warmCache() {
        // Pre-load frequently accessed data
        provinceService.getAllProvinces();
        schoolService.getAllSchools();
        majorService.getAllMajors(0, 20, "id", "asc", null);
    }
}
```

### ⚠️ Cache Invalidation Patterns

#### 1. Time-based Eviction
- **Master data**: 24-48h TTL
- **Reference data**: 6-12h TTL  
- **User data**: 15-30 phút TTL

#### 2. Event-based Eviction
```java
// When admin updates major data
@CacheEvict(value = {"majors", "universities"}, allEntries = true)
public void onMajorDataChanged()

// When user updates profile
@CacheEvict(value = "user-profiles", key = "#userId")
public void onUserProfileUpdated(String userId)
```

### 🚫 APIs KHÔNG nên Cache

1. **Authentication APIs** (`/api/auth/*`)
   - Login, logout, token refresh
   - Lý do: Security sensitive, session-based

2. **User-specific Write Operations**
   - Profile updates, password changes
   - Lý do: Real-time consistency required

3. **File Upload APIs**
   - Document uploads, avatar changes
   - Lý do: Large data, không phù hợp với Redis

4. **Real-time Notifications**
   - System alerts, status updates
   - Lý do: Cần fresh data

### 📋 Implementation Checklist

- [ ] Setup Redis cache configuration với TTL phù hợp
- [ ] Implement caching cho Provinces API (Priority 1)
- [ ] Implement caching cho Schools API (Priority 1)  
- [ ] Implement caching cho Majors API (Priority 2)
- [ ] Implement caching cho Universities API (Priority 2)
- [ ] Setup cache eviction cho update operations
- [ ] Add cache monitoring và metrics
- [ ] Test cache performance và hit rates
- [ ] Document cache keys và TTL strategy
- [ ] Setup cache warming strategy

### 🔍 Testing Cache Strategy

```java
@Test
public void testProvinceCaching() {
    // First call - should hit database
    var provinces1 = provinceService.getAllProvinces();
    
    // Second call - should hit cache
    var provinces2 = provinceService.getAllProvinces();
    
    // Verify cache was used (mock database calls)
    verify(provinceRepository, times(1)).findAll();
}
```
