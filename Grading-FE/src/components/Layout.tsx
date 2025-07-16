import React from 'react';
import Header from './Header';
import Footer from './Footer';

interface LayoutProps {
  children: React.ReactNode;
  onLogout: () => Promise<void>;
  onNavigate: (path: string) => void;
}

const Layout: React.FC<LayoutProps> = ({ children, onLogout, onNavigate }) => {
  return (
    <div className="min-h-screen flex flex-col">
      <Header onLogout={onLogout} onNavigate={onNavigate} />
      <main className="flex-1">
        {children}
      </main>
      <Footer />
    </div>
  );
};

export default Layout;
