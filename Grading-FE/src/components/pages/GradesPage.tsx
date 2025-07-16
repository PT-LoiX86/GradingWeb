import React from 'react';
import { Header } from '../index';

interface GradesPageProps {
  onLogout: () => Promise<void>;
}

const GradesPage: React.FC<GradesPageProps> = ({ onLogout }) => {
  return (
    <div className="min-h-screen bg-gray-50">
      <Header onLogout={onLogout} />
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="border-4 border-dashed border-gray-200 rounded-lg h-96 flex items-center justify-center">
            <div className="text-center">
              <h1 className="text-2xl font-bold text-gray-900 mb-4">Điểm số</h1>
              <p className="text-gray-600">Trang quản lý điểm số sẽ được phát triển tại đây</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GradesPage;
