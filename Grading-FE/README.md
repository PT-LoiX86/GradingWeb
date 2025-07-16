# Grading-FE (Frontend)

## Tech Stack
- React 19 (Vite)
- TypeScript
- Tailwind CSS
- Zustand (state management)
- Axios (API)
- React Hook Form (form)
- ...

## Folder Structure

```
/src
  /components     # UI components (Button, Modal, ...)
  /contexts       # React Contexts (Auth, Theme, ...)
  /hooks          # Custom hooks (useAuth, ...)
  /lib            # Tiện ích, cấu hình API
  /services       # Hàm gọi API
  /store          # State management (Zustand)
  /styles         # Tailwind, global CSS
  /types          # TypeScript types/interfaces
  /assets         # Static assets (images, svg, ...)
  App.tsx         # Root component
  main.tsx        # Entry point
```

## Development
```bash
npm install
npm run dev
```

## Build
```bash
npm run build
```

## Lint
```bash
npm run lint
```
