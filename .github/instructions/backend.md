## Hướng dẫn cho Backend (Spring Boot & Java)

### 1. Cấu trúc Project

Tuân thủ cấu trúc package tiêu chuẩn của Spring Boot:
* `com.tuyensinh. ... .controller`: Chứa các REST Controller.
* `com.tuyensinh. ... .service`: Chứa business logic.
* `com.tuyensinh. ... .repository`: Chứa các Spring Data JPA repository.
* `com.tuyensinh. ... .model`: Chứa các JPA entity.
* `com.tuyensinh. ... .dto`: Chứa các Data Transfer Objects.
* `com.tuyensinh. ... .config`: Chứa các lớp cấu hình.
* `com.tuyensinh. ... .exception`: Chứa các exception handler.

### 2. Quy tắc code

* Sử dụng **Lombok** triệt để (`@Data`, `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`).
* Luôn sử dụng **Dependency Injection**.
* Tên cột trong CSDL PostgreSQL phải theo định dạng `snake_case`. Tên thuộc tính trong Entity Java phải theo `camelCase`. Dùng `@Column(name = "ten_cot")` để mapping.
* **API:** Luôn tuân thủ file `api_design_rules.md`. Sử dụng DTO để trao đổi dữ liệu với client, không bao giờ trả về trực tiếp Entity.
* **Xử lý ngoại lệ:** Sử dụng `@ControllerAdvice` và `@ExceptionHandler` để xử lý exception tập trung và trả về response lỗi theo chuẩn trong `api_design_rules.md`.

### 3. Bảo mật (Spring Security)

* Triển khai xác thực bằng JWT và OAuth2 (Google).
* Sử dụng phân quyền dựa trên vai trò (Role-based) với các annotation `@PreAuthorize("hasRole('ADMIN')")`.

### 4. Tích hợp dịch vụ

* **AWS S3:** Tạo một `S3Service` để xử lý logic upload, download và xóa file.
* **Tesseract OCR:** Tạo một `OcrService` để nhận file ảnh học bạ và trích xuất text.
* **WebSocket:** Cấu hình `WebSocket` cho module chat/diễn đàn.

### 5. Unit Test

* Sử dụng **JUnit 5** và **Mockito**.
* Mỗi `Service` phải có lớp Test tương ứng (ví dụ `HoSoServiceTest`).
* Test các logic nghiệp vụ quan trọng và các trường hợp biên.