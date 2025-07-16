import React from 'react';

const Navbar: React.FC = () => {
  return (
    <nav className="w-full flex items-center justify-between px-6 py-3 bg-white dark:bg-gray-900 shadow">
      <div className="flex items-center space-x-2">
        <img src="/logo.png" alt="Logo" className="h-8 w-8" />
        <span className="font-bold text-lg text-primary">GradingWeb</span>
      </div>
      <div className="flex items-center space-x-4">
        <a href="/" className="text-gray-700 dark:text-gray-200 hover:text-primary">Home</a>
        <a href="/schools" className="text-gray-700 dark:text-gray-200 hover:text-primary">Schools</a>
        <a href="/majors" className="text-gray-700 dark:text-gray-200 hover:text-primary">Majors</a>
        <a href="/forum" className="text-gray-700 dark:text-gray-200 hover:text-primary">Forum</a>
        <button className="ml-4 px-2 py-1 rounded bg-gray-200 dark:bg-gray-700" aria-label="Toggle dark mode">
          🌙
        </button>
      </div>
    </nav>
  );
};

export default Navbar; 