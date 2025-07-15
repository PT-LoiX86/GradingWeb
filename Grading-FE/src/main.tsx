import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter, Route, Routes } from 'react-router'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
     <Routes>
      <Route path="/" element={<App />} />
      <Route path="/oauth2/callback" element={<App />} />
      <Route path="*" element={<App />} />
    </Routes>
  </BrowserRouter>,
)
