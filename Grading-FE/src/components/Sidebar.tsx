import React from 'react';

const Sidebar: React.FC = () => {
  return (
    <aside className="h-full w-64 bg-white dark:bg-gray-900 shadow-lg p-4 hidden md:block">
      <nav className="flex flex-col space-y-2">
        <a href="/dashboard" className="px-3 py-2 rounded hover:bg-primary/10 text-gray-700 dark:text-gray-200">Dashboard</a>
        <a href="/dashboard/applications" className="px-3 py-2 rounded hover:bg-primary/10 text-gray-700 dark:text-gray-200">Applications</a>
        <a href="/dashboard/profile" className="px-3 py-2 rounded hover:bg-primary/10 text-gray-700 dark:text-gray-200">Profile</a>
        <a href="/dashboard/admin" className="px-3 py-2 rounded hover:bg-primary/10 text-gray-700 dark:text-gray-200">Admin</a>
      </nav>
    </aside>
  );
};

export default Sidebar; 