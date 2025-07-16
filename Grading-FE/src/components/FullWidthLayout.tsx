import React from 'react';
import Header from './Header';
import Footer from './Footer';

interface FullWidthLayoutProps {
  children: React.ReactNode;
  onLogout: () => Promise<void>;
  onNavigate: (path: string) => void;
  showContainer?: boolean;
}

const FullWidthLayout: React.FC<FullWidthLayoutProps> = ({ 
  children, 
  onLogout, 
  onNavigate, 
  showContainer = true 
}) => {
  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      {/* Header full width */}
      <Header onLogout={onLogout} onNavigate={onNavigate} />
      
      {/* Main content có thể full width hoặc có container */}
      <main className="flex-1 w-full">
        {showContainer ? (
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            {children}
          </div>
        ) : (
          <div className="w-full">
            {children}
          </div>
        )}
      </main>
      
      {/* Footer full width */}
      <Footer />
    </div>
  );
};

export default FullWidthLayout;
