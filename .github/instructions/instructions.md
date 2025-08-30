## Hướng dẫn tổng quan cho GitHub Copilot

Chào Copilot, bạn đang hỗ trợ dự án "Hệ thống Tuyển sinh THPT Trực tuyến". Đây là một hệ thống phức tạp với nhiều thành phần.

**QUY TẮC QUAN TRỌNG NHẤT: Luôn luôn phản hồi, giải thích mã nguồn bằng Tiếng Việt, sinh mã nguồn phải là Tiếng Anh. Chỉ sửa code và không cần phải giải thích gì thêm**

### Bối cảnh dự án

* **Mục tiêu:** Xây dựng nền tảng cho phép học sinh nộp hồ sơ xét tuyển học bạ online, và các trường THPT quản lý, xét duyệt hồ sơ đó. Hệ thống cũng bao gồm một diễn đàn để tư vấn và trao đổi thông tin.
* **Công nghệ chính:**
    * **Frontend:** React js & Tailwind CSS
    * **Backend:** Spring Boot (Java) & PostgreSQL
    * **DevOps:** Docker & GitHub Actions

### Nguyên tắc chung cần tuân thủ

1.  **Clean Code:** Mã nguồn phải dễ đọc, dễ hiểu và dễ bảo trì. Ưu tiên các tên biến, tên hàm rõ ràng bằng Tiếng Anh (ví dụ: `getUserProfile`, `updateGradeScroe`).
2.  **Bảo mật là trên hết (Security First):** Mọi mã nguồn sinh ra phải luôn cân nhắc đến các yếu tố bảo mật, đặc biệt là với các chức năng xác thực, xử lý dữ liệu người dùng và upload file.
3.  **Tài liệu hóa (Documentation):**
    * Viết Javadoc cho các class và method public ở Backend.
    * Viết comment giải thích các logic phức tạp ở Frontend.

### Tham chiếu đến các hướng dẫn chi tiết

Để có hướng dẫn cụ thể cho từng phần, hãy tham khảo các file sau trong thư mục này:

* `backend.md`: Hướng dẫn cho Spring Boot, Java, và PostgreSQL.
* `frontend.md`: Hướng dẫn cho React js, và Tailwind CSS.
* `devops.md`: Hướng dẫn cho Docker và GitHub Actions.
* `api_design_rules.md`: **QUAN TRỌNG**, quy tắc thiết kế RESTful API bắt buộc phải tuân thủ.