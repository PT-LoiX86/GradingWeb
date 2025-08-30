## Hướng dẫn cho Frontend (Next.js & React)

### 1. Cấu trúc Project (App Router)

Sử dụng App Router của Next.js và tổ chức thư mục như sau:
* `app/`: Chứa các route của ứng dụng.
* `components/`: Chứa các component tái sử dụng (UI, layout).
* `lib/`: Chứa các hàm tiện ích, cấu hình client API.
* `hooks/`: Chứa các custom hooks (ví dụ: `useAuth`, `useUserProfile`).
* `contexts/`: Chứa các React Context.
* `services/`: Chứa các hàm gọi API đã được định nghĩa.

### 2. Quy tắc code

* Luôn sử dụng **Functional Components** với **React Hooks**.
* **Styling:** Sử dụng **Tailwind CSS** cho toàn bộ việc tạo kiểu. Hạn chế tối đa việc viết CSS thuần hoặc inline-style.
* **API:**
    * Tạo một client `axios` hoặc `fetch` được cấu hình sẵn trong `lib/api.js`.
    * Mọi request và response phải tuân thủ `api_design_rules.md`.
    * Sử dụng **React Query** hoặc **SWR** để quản lý state từ server (fetching, caching, mutation).
* **State Management:**
    * Sử dụng **React Context** cho các state đơn giản, global.
    * Sử dụng **Zustand** hoặc **Redux Toolkit** cho các state phức tạp hơn.
* **Form:** Sử dụng thư viện **React Hook Form** để quản lý form và validation.

### 3. Component

* Chia nhỏ component. Mỗi component chỉ nên làm một việc duy nhất.
* Tên file component viết theo dạng `PascalCase.jsx` (ví dụ `UserProfileCard.jsx`).