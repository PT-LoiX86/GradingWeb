## API Inconsistencies và Action Items

### Hiện trạng API hiện tại

Sau khi review API hiện tại, có một số điểm cần cải thiện để tuân thủ quy tắc thiết kế:

### ✅ Những gì đã đúng

1. **Endpoint naming**: Hầu hết đã sử dụng kebab-case và danh từ số nhiều
   - `/api/student-profiles`
   - `/api/majors`
   - `/api/universities`
   - `/api/schools`
   - `/api/provinces`

2. **Prefix**: Tất cả đều có `/api` prefix

3. **HTTP Methods**: Đúng ngữ nghĩa REST

4. **Swagger Documentation**: Đã có implement OpenAPI

### ⚠️ Những gì cần cải thiện

#### 1. Response Structure Inconsistency

**Hiện tại có 2 patterns:**

**Pattern A** (UserController, AuthController):
```java
// Trả về direct object
public ResponseEntity<UserDataResponse> getUserData()
public ResponseEntity<LoginResponse> login()
```

**Pattern B** (MajorController):
```java
// Sử dụng ApiResponse wrapper
public ResponseEntity<ApiResponse<PageResponse<MajorResponse>>> getAllMajors()
```

**🎯 Action Required:**
- Migrate tất cả endpoints sang sử dụng `ApiResponse<T>` wrapper
- Update UserController và AuthController

#### 2. Missing `/api` prefix

**UserController và AdminController:**
```java
@RequestMapping(path = "api/users")  // Missing leading slash
@RequestMapping(path = "api/admin")  // Missing leading slash
```

**🎯 Action Required:**
```java
@RequestMapping(path = "/api/users")
@RequestMapping(path = "/api/admin")
```

#### 3. Inconsistent Error Handling

Cần implement global exception handler để đảm bảo tất cả errors đều trả về format nhất quán.

#### 4. Missing Pagination on Some Endpoints

**StudentProfileController:**
```java
@GetMapping
public ResponseEntity<List<StudentProfileResponse>> getAllStudentProfiles()
```

**🎯 Action Required:**
- Add pagination parameters
- Return `PageResponse<StudentProfileResponse>`
- Wrap trong `ApiResponse`

### 📋 Priority Action Items

#### Priority 1 (High) - Fix ngay

1. **Fix missing `/` prefix**
   - UserController: `"api/users"` → `"/api/users"`
   - AdminController: `"api/admin"` → `"/api/admin"`

2. **Standardize Response Format**
   - Tạo global response wrapper strategy
   - Update UserController endpoints
   - Update AuthController endpoints

#### Priority 2 (Medium) - Trong sprint tới

1. **Add Pagination**
   - StudentProfileController.getAllStudentProfiles()
   - Các list endpoints khác chưa có pagination

2. **Global Exception Handler**
   - Implement @ControllerAdvice
   - Standardize error response format

#### Priority 3 (Low) - Technical debt

1. **Documentation Enhancement**
   - Improve Swagger descriptions
   - Add more detailed @ApiResponse annotations

### 🔧 Quick Fixes

#### Fix 1: UserController
```java
@RequestMapping(path = "/api/users") // Add leading slash
```

#### Fix 2: AdminController  
```java
@RequestMapping(path = "/api/admin") // Add leading slash
```

#### Fix 3: StudentProfileController
```java
@GetMapping
public ResponseEntity<ApiResponse<PageResponse<StudentProfileResponse>>> getAllStudentProfiles(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
    
    PageResponse<StudentProfileResponse> pageResponse = studentProfileService
        .getAllStudentProfiles(page, size);
    
    return ResponseEntity.ok(
        ApiResponse.success("Lấy danh sách hồ sơ thành công", pageResponse)
    );
}
```

### 📝 Implementation Notes

1. **Backward Compatibility**: Khi migrate response format, cần đảm bảo Frontend không bị break
2. **Testing**: Mỗi change cần có corresponding test updates
3. **Documentation**: Update API documentation sau mỗi change
