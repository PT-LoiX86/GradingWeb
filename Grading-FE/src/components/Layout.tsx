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
    <div className="min-h-screen flex flex-col bg-gray-50">
      {/* Header với full width */}
      <Header onLogout={onLogout} onNavigate={onNavigate} />
      
      {/* Main content với padding 2 bên */}
      <main className="flex-1 w-full">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {children}
        </div>
      </main>
      
      {/* Footer với full width */}
      <Footer />
    </div>
  );
};

export default Layout;
