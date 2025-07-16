import React from 'react';

interface PageContainerProps {
  children: React.ReactNode;
  title?: string;
  subtitle?: string;
  className?: string;
  fullWidth?: boolean;
}

const PageContainer: React.FC<PageContainerProps> = ({ 
  children, 
  title, 
  subtitle, 
  className = '', 
  fullWidth = false 
}) => {
  return (
    <div className={`${fullWidth ? 'w-full' : 'max-w-7xl mx-auto'} ${className}`}>
      {(title || subtitle) && (
        <div className="mb-8">
          {title && (
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{title}</h1>
          )}
          {subtitle && (
            <p className="text-gray-600 text-lg">{subtitle}</p>
          )}
        </div>
      )}
      {children}
    </div>
  );
};

export default PageContainer;
