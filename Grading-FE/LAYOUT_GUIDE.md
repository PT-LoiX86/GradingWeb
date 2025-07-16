# Layout System Documentation

## Tổng quan

Layout system mới được thiết kế để tạo ra một giao diện chuyên nghiệp với:
- Header và Footer có full width
- Content body có padding 2 bên
- Responsive design cho mọi kích thước màn hình
- Các components tái sử dụng

## Components

### 1. Layout (Main Layout)
```tsx
import { Layout } from './components';

<Layout onLogout={handleLogout} onNavigate={handleNavigate}>
  <YourContent />
</Layout>
```

**Đặc điểm:**
- Header full width với max-width container bên trong
- Footer full width với max-width container bên trong
- Main content có max-width 7xl và padding responsive
- Background color: bg-gray-50

### 2. FullWidthLayout (Full Width Layout)
```tsx
import { FullWidthLayout } from './components';

<FullWidthLayout 
  onLogout={handleLogout} 
  onNavigate={handleNavigate}
  showContainer={false} // Để content full width
>
  <YourContent />
</FullWidthLayout>
```

**Đặc điểm:**
- Tương tự Layout nhưng có thể tùy chọn container hoặc full width
- showContainer={true}: Có container
- showContainer={false}: Full width content

### 3. PageContainer (Page Container)
```tsx
import { PageContainer } from './components';

<PageContainer 
  title="Trang chủ"
  subtitle="Chào mừng bạn đến với hệ thống"
  className="additional-classes"
  fullWidth={false}
>
  <YourContent />
</PageContainer>
```

**Đặc điểm:**
- Tự động thêm title và subtitle
- Có thể tùy chọn full width hoặc container
- Margin bottom cho title/subtitle

### 4. ResponsiveWrapper (Responsive Wrapper)
```tsx
import { ResponsiveWrapper } from './components';

<ResponsiveWrapper 
  maxWidth="7xl"
  className="py-8"
>
  <YourContent />
</ResponsiveWrapper>
```

**Đặc điểm:**
- Flexible max-width: 'sm' | 'md' | 'lg' | 'xl' | '2xl' | '7xl' | 'full'
- Responsive padding
- Tự động center content

## Sử dụng

### Trang thông thường
```tsx
const MyPage = () => {
  return (
    <Layout onLogout={handleLogout} onNavigate={handleNavigate}>
      <PageContainer title="Trang của tôi" subtitle="Mô tả trang">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Content */}
        </div>
      </PageContainer>
    </Layout>
  );
};
```

### Trang cần full width
```tsx
const FullWidthPage = () => {
  return (
    <FullWidthLayout 
      onLogout={handleLogout} 
      onNavigate={handleNavigate}
      showContainer={false}
    >
      <div className="w-full">
        {/* Full width content */}
      </div>
    </FullWidthLayout>
  );
};
```

### Dashboard với sections khác nhau
```tsx
const Dashboard = () => {
  return (
    <Layout onLogout={handleLogout} onNavigate={handleNavigate}>
      {/* Hero section - full width trong container */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white rounded-lg mb-8">
        <div className="px-8 py-12">
          {/* Hero content */}
        </div>
      </div>

      {/* Stats - container width */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {/* Stats cards */}
      </div>
    </Layout>
  );
};
```

## Breakpoints

- **Mobile**: < 640px
- **Tablet**: 640px - 1024px  
- **Desktop**: > 1024px

## Padding System

- **Mobile**: px-4 (16px)
- **Tablet**: px-6 (24px)
- **Desktop**: px-8 (32px)

## Max-width System

- **Default container**: max-w-7xl (1280px)
- **Flexible options**: sm, md, lg, xl, 2xl, 7xl, full

## CSS Utilities

```css
/* Available utility classes */
.container-fluid         /* Full width container */
.main-content           /* Max-width container with responsive padding */
.section-padding        /* Standard section padding */
.card                   /* Card styling */
.card-header           /* Card header */
.card-body             /* Card body */
.shadow-professional    /* Professional shadow */
.shadow-professional-lg /* Large professional shadow */
```

## Best Practices

1. **Sử dụng Layout làm wrapper chính** cho hầu hết các trang
2. **Sử dụng FullWidthLayout** cho các trang cần full width như dashboard, landing pages
3. **Sử dụng PageContainer** để có title và subtitle đồng nhất
4. **Sử dụng ResponsiveWrapper** cho các sections cần max-width khác nhau
5. **Luôn test trên mobile và desktop** để đảm bảo responsive

## Migration Guide

### Từ layout cũ sang layout mới:

**Cũ:**
```tsx
<div className="min-h-screen bg-gray-50">
  <Header />
  <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <Content />
  </main>
  <Footer />
</div>
```

**Mới:**
```tsx
<Layout onLogout={handleLogout} onNavigate={handleNavigate}>
  <Content />
</Layout>
```
