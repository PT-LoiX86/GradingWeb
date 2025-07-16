## Quy tắc Thiết kế RESTful API

Mọi API trong dự án phải tuân thủ nghiêm ngặt các quy tắc dưới đây.

### 1. Endpoint Naming Convention

* Sử dụng danh từ số nhiều (plural nouns).
* Sử dụng định dạng `kebab-case` (gạch nối giữa các từ) cho URL paths.
* Luôn có tiền tố `/api` (không cần version nếu chưa có multiple versions).
* **Ví dụ hiện tại trong dự án:**
    * `GET /api/student-profiles` (Lấy danh sách hồ sơ học sinh)
    * `POST /api/student-profiles` (Tạo hồ sơ mới)
    * `GET /api/student-profiles/{id}` (Lấy chi tiết một hồ sơ)
    * `GET /api/majors` (Lấy danh sách ngành học)
    * `GET /api/universities` (Lấy danh sách trường đại học)
    * `GET /api/schools` (Lấy danh sách trường phổ thông)
    * `GET /api/provinces` (Lấy danh sách tỉnh thành)

### 2. Phương thức HTTP (HTTP Methods)

* `GET`: Lấy dữ liệu.
* `POST`: Tạo mới tài nguyên.
* `PUT`: Cập nhật toàn bộ tài nguyên.
* `PATCH`: Cập nhật một phần tài nguyên.
* `DELETE`: Xóa tài nguyên.

### 3. Định dạng JSON

* **Bắt buộc:** Tất cả các key trong JSON request và response phải ở định dạng **`camelCase`**.
* **Ví dụ:** `{ "hoTen": "Nguyễn Văn A", "diemTrungBinh": 8.5 }`

### 4. Cấu trúc Response chuẩn

Dự án hiện tại có 2 pattern response chính:

**Pattern 1 - ApiResponse Wrapper (khuyến nghị sử dụng):**
```json
{
  "message": "Operation completed successfully",
  "data": { ... }, // hoặc [ ... ]
  "success": true
}
```

**Pattern 2 - Direct Response (đang được sử dụng trong một số endpoint):**
```json
{
  "statusCode": 200,
  "message": "Yêu cầu thành công",
  "data": { ... } // hoặc [ ... ]
}
```

**Thất bại (4xx, 5xx):**
```json
{
  "message": "Dữ liệu đầu vào không hợp lệ",
  "success": false,
  "errors": [
    {
      "field": "email",
      "message": "Email không đúng định dạng"
    }
  ]
}
```

**Quy tắc:**
- Sử dụng `ApiResponse<T>` wrapper cho tất cả các endpoint mới
- Gradually migrate các endpoint hiện tại sang pattern này
- Các key JSON phải sử dụng `camelCase`

### 5. Phân trang (Pagination)

Sử dụng query parameters `page` và `size` cho các API trả về danh sách.

**Format:**
- `GET /api/majors?page=0&size=10` (page bắt đầu từ 0 theo Spring Boot convention)
- `GET /api/student-profiles?page=1&size=20` 

**Response với phân trang sử dụng PageResponse:**
```json
{
  "message": "Lấy dữ liệu thành công",
  "data": {
    "content": [...], 
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false
  },
  "success": true
}
```

### 6. Xác thực và Authorization

* Sử dụng `Authorization: Bearer <JWT_TOKEN>` header cho các endpoint cần bảo vệ.
* Các endpoint public nên được đặt trong `/api/public/*`
* Các endpoint auth nên được đặt trong `/api/auth/*`
* Các endpoint admin nên được đặt trong `/api/admin/*`

### 7. Validation và Error Handling

* Sử dụng Bean Validation annotations (`@Valid`, `@NotNull`, `@Email`, etc.)
* Error response phải có cấu trúc nhất quán
* HTTP Status codes phải đúng ngữ nghĩa:
  - 200: OK (thành công)
  - 201: Created (tạo mới thành công)
  - 400: Bad Request (dữ liệu đầu vào không hợp lệ)
  - 401: Unauthorized (chưa xác thực)
  - 403: Forbidden (không có quyền)
  - 404: Not Found (không tìm thấy)
  - 500: Internal Server Error (lỗi server)

### 8. Documentation

* Sử dụng Swagger/OpenAPI annotations cho tất cả endpoints
* Mỗi endpoint phải có `@Operation` với summary và description
* Sử dụng `@Tag` để nhóm các endpoints theo chức năng
* Sử dụng `@ApiResponse` để mô tả các response codes

### 9. Content Type và Media Type

* Mặc định sử dụng `application/json` cho request và response
* Khai báo explicit trong `@RequestMapping` nếu cần: `produces = MediaType.APPLICATION_JSON_VALUE`

### 10. Naming Convention cho Resource IDs

* Sử dụng `{id}` cho path variables (không phải `{resourceId}`)
* ID phải là kiểu Long trong Java
* **Ví dụ:** `/api/majors/{id}`, `/api/student-profiles/{id}`


### 11. Best Practices

#### Controller Structure
```java
@RestController
@RequestMapping(value = "/api/resources", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Resource Management", description = "API endpoints for managing resources")
public class ResourceController {
    
    private final ResourceService resourceService;
    
    @GetMapping
    @Operation(summary = "Get all resources", description = "Retrieve paginated list of resources")
    public ResponseEntity<ApiResponse<PageResponse<ResourceResponse>>> getAllResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Implementation
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get resource by ID")
    public ResponseEntity<ApiResponse<ResourceResponse>> getResourceById(@PathVariable Long id) {
        // Implementation
    }
    
    @PostMapping
    @Operation(summary = "Create new resource")
    public ResponseEntity<ApiResponse<ResourceResponse>> createResource(
            @Valid @RequestBody ResourceRequest request) {
        // Implementation
    }
}
```

#### Response Wrapper Usage
```java
// Trong Service
public ApiResponse<ResourceResponse> createResource(ResourceRequest request) {
    // Logic xử lý
    ResourceResponse response = // ... tạo response
    return ApiResponse.success("Tạo resource thành công", response);
}

// Trong Controller
@PostMapping
public ResponseEntity<ApiResponse<ResourceResponse>> createResource(@Valid @RequestBody ResourceRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(resourceService.createResource(request));
}
```

### 12. Consistency Checklist

Trước khi tạo hoặc sửa API, hãy kiểm tra:

- [ ] Endpoint sử dụng danh từ số nhiều và kebab-case
- [ ] Có prefix `/api`
- [ ] HTTP methods đúng ngữ nghĩa
- [ ] Request/Response sử dụng camelCase
- [ ] Có sử dụng `ApiResponse<T>` wrapper
- [ ] Có validation cho request body
- [ ] Có Swagger documentation đầy đủ
- [ ] Error handling được implement
- [ ] Authentication/Authorization được kiểm tra (nếu cần)
- [ ] Phân trang được implement đúng (nếu có)
- [ ] **Caching strategy được áp dụng (nếu phù hợp)**

### 13. Caching Guidelines với Redis

#### Các API nên implement caching:

**Master Data (TTL: 24-48h):**
- `GET /api/provinces` - Danh sách tỉnh thành (ít thay đổi)
- `GET /api/schools` - Danh sách trường THPT (ổn định)

**Reference Data (TTL: 6-12h):**
- `GET /api/majors` - Danh sách ngành học (có pagination)
- `GET /api/universities` - Danh sách trường đại học (có search)

**User Data (TTL: 15-30 phút):**
- `GET /api/student-profiles` (public view, pagination)
- `GET /api/users/me` - User profile data

#### Caching annotations:
```java
// Cache simple list
@Cacheable(value = "provinces", key = "'all'")
public List<ProvinceResponse> getAllProvinces()

// Cache with pagination  
@Cacheable(value = "majors", 
           key = "'page:' + #page + ':size:' + #size + ':search:' + (#search ?: 'none')",
           condition = "#page < 5")
public PageResponse<MajorResponse> getAllMajors(int page, int size, String search)

// Cache eviction on updates
@CacheEvict(value = "provinces", allEntries = true)
public ProvinceResponse updateProvince(Long id, ProvinceRequest request)
```

#### Các API KHÔNG nên cache:
- Authentication endpoints (`/api/auth/*`)
- File upload operations
- Real-time user-specific data
- Write operations (POST, PUT, DELETE)

📖 **Chi tiết:** Xem `redis_caching_strategy.md` cho implementation guide đầy đủ.