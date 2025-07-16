## Hướng dẫn cho DevOps (Docker & GitHub Actions)

### 1. Dockerfile

Khi được yêu cầu tạo `Dockerfile`, hãy tuân thủ các nguyên tắc sau:

* **Sử dụng Multi-stage builds:**
    * **Backend (Spring Boot):** Stage 1 dùng `maven` hoặc `gradle` để build ra file `.jar`. Stage 2 copy file `.jar` đó vào một image JRE-slim (ví dụ `eclipse-temurin:17-jre-focal`).
    * **Frontend (Next.js):** Stage 1 dùng image `node` để `npm install` và `npm run build`. Stage 2 copy thư mục `.next`, `public` và `node_modules` cần thiết vào một image node-slim.
* **Tối ưu hóa layer caching:** Copy `package.json` hoặc `pom.xml` và cài đặt dependencies trước khi copy toàn bộ mã nguồn.
* Chạy ứng dụng với user không phải root (`USER nonroot:nonroot`).

### 2. GitHub Actions

Khi tạo workflow file (`.github/workflows/*.yml`):

* **Trigger:** Cấu hình trigger chạy khi có `push` hoặc `pull_request` vào các nhánh `main` và `develop`.
* **Jobs:** Chia thành các job riêng biệt: `build`, `test`, `publish-docker-image`.
* **Build & Test:**
    * Thiết lập ma trận build (matrix) nếu cần test trên nhiều phiên bản Java/Node.js.
    * Chạy unit test (`mvn test` hoặc `npm test`). Nếu test thất bại, workflow phải dừng lại.
* **Publish:**
    * Sau khi build và test thành công trên nhánh `main`, job publish sẽ build Docker image.
    * Đăng nhập vào Docker Hub hoặc AWS ECR.
    * Push image với tag là commit hash hoặc version number.
* **Bảo mật:** Luôn sử dụng **GitHub Secrets** để lưu trữ các thông tin nhạy cảm như `DOCKER_USERNAME`, `DOCKER_PASSWORD`, `AWS_ACCESS_KEY_ID`.