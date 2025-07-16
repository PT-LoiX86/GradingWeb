import React, { useState, useEffect, useRef } from 'react';
import { Bell, Search, User, Menu, X, LogOut, Settings, Shield } from 'lucide-react';
import { authAPI } from '../services/api';

interface HeaderProps {
  onLogout: () => Promise<void>;
  onNavigate?: (path: string) => void;
}

const Header: React.FC<HeaderProps> = ({ onLogout, onNavigate }) => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState<any>(null);
  const userMenuRef = useRef<HTMLDivElement>(null);

  // Get current user info
  useEffect(() => {
    const user = authAPI.getCurrentUser();
    setCurrentUser(user);
  }, []);

  const isAdmin = currentUser?.role === 'ADMIN';

  const handleLogout = async () => {
    setIsUserMenuOpen(false);
    await onLogout();
  };

  const handleNavigation = (path: string) => {
    if (onNavigate) {
      onNavigate(path);
    }
  };

  // Close user menu when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target as Node)) {
        setIsUserMenuOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <header className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-50 w-full">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex items-center">
            <div className="flex items-center space-x-2">
              <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
                <div className="w-4 h-4 bg-white rounded-sm"></div>
              </div>
              <span className="text-xl font-semibold text-gray-900">Grading</span>
            </div>
          </div>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex space-x-8">
            <a 
              onClick={() => handleNavigation('/')}
              className="text-blue-600 hover:text-blue-800 px-3 py-2 text-sm font-medium transition-colors cursor-pointer"
            >
              Trang chủ
            </a>
            {isAdmin && (
              <a 
                onClick={() => handleNavigation('/admin/dashboard')}
                className="text-blue-600 hover:text-blue-800 px-3 py-2 text-sm font-medium transition-colors cursor-pointer flex items-center space-x-1"
              >
                <Shield className="w-4 h-4" />
                <span>Dashboard</span>
              </a>
            )}
            <a 
              onClick={() => handleNavigation('/assignments')}
              className="text-blue-600 hover:text-blue-800 px-3 py-2 text-sm font-medium transition-colors cursor-pointer"
            >
              Bài tập
            </a>
            <a 
              onClick={() => handleNavigation('/grades')}
              className="text-blue-600 hover:text-blue-800 px-3 py-2 text-sm font-medium transition-colors cursor-pointer"
            >
              Điểm số
            </a>
            <a 
              onClick={() => handleNavigation('/students')}
              className="text-blue-600 hover:text-blue-800 px-3 py-2 text-sm font-medium transition-colors cursor-pointer"
            >
              Học sinh
            </a>
            <a 
              onClick={() => handleNavigation('/reports')}
              className="text-blue-600 hover:text-blue-800 px-3 py-2 text-sm font-medium transition-colors cursor-pointer"
            >
              Báo cáo
            </a>
          </nav>

          {/* Desktop Actions */}
          <div className="hidden md:flex items-center space-x-4">
            {/* Search */}
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <input
                type="text"
                placeholder="Search..."
                className="pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-sm"
              />
            </div>

            {/* Notifications */}
            <button className="relative p-2 text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 rounded-md">
              <Bell className="w-5 h-5" />
              <span className="absolute top-0 right-0 block h-2 w-2 rounded-full bg-red-400 ring-2 ring-white"></span>
            </button>

            {/* User Profile */}
            <div className="relative" ref={userMenuRef}>
              <button 
                onClick={() => setIsUserMenuOpen(!isUserMenuOpen)}
                className="flex items-center space-x-2 rounded-full bg-white hover:bg-gray-50 hover:border-gray-400 focus:outline-none transition-all duration-200"
              >
                <div className="p-0 w-8 h-8 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center">
                  <User className="w-4 h-4 text-white" />
                </div>
                <div className="hidden sm:block">
                  <span className="text-xs font-medium text-gray-700">
                    {currentUser?.fullName || currentUser?.username || 'User'}
                  </span>
                  {isAdmin && (
                    <div className="text-xs text-blue-600 font-medium">Admin</div>
                  )}
                </div>
                <svg className={`w-4 h-4 text-gray-400 transition-transform duration-200 ${isUserMenuOpen ? 'transform rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              
              {/* User dropdown menu */}
              {isUserMenuOpen && (
                <div className="absolute right-0 mt-3 w-60 rounded-xl shadow-xl bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50 border border-gray-100 overflow-hidden">
                  <div className="py-1">
                    <div className="px-4 py-3 bg-gradient-to-r from-blue-50 to-indigo-50 border-b border-gray-100">
                      <div className="flex items-center space-x-3">
                        <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center">
                          <User className="w-5 h-5 text-white" />
                        </div>
                        <div className="flex-1">
                          <p className="text-sm font-semibold text-gray-900">
                            {currentUser?.fullName || currentUser?.username || 'User'}
                          </p>
                          <p className="text-xs text-gray-600">{currentUser?.email || ''}</p>
                          {isAdmin && (
                            <span className="inline-flex items-center px-2 py-1 text-xs font-medium bg-blue-100 text-blue-700 rounded-full mt-1">
                              <Shield className="w-3 h-3 mr-1" />
                              Admin
                            </span>
                          )}
                        </div>
                      </div>
                    </div>
                    <div className="py-1">
                      <button
                        onClick={() => {
                          handleNavigation('/profile');
                          setIsUserMenuOpen(false);
                        }}
                        className="flex items-center w-full px-4 py-3 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-700 transition-colors group"
                      >
                        <User className="w-4 h-4 mr-3 text-gray-400 group-hover:text-blue-500" />
                        <span>Hồ sơ cá nhân</span>
                      </button>
                      <button
                        onClick={() => {
                          handleNavigation('/settings');
                          setIsUserMenuOpen(false);
                        }}
                        className="flex items-center w-full px-4 py-3 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-700 transition-colors group"
                      >
                        <Settings className="w-4 h-4 mr-3 text-gray-400 group-hover:text-blue-500" />
                        <span>Cài đặt</span>
                      </button>
                      <hr className="my-1 border-gray-200" />
                      <button
                        onClick={handleLogout}
                        className="flex items-center w-full px-4 py-3 text-sm text-red-600 hover:bg-red-50 hover:text-red-700 transition-colors group"
                      >
                        <LogOut className="w-4 h-4 mr-3 text-red-500 group-hover:text-red-700" />
                        <span>Đăng xuất</span>
                      </button>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden">
            <button
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              className="p-2 rounded-md text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              {isMobileMenuOpen ? (
                <X className="w-6 h-6" />
              ) : (
                <Menu className="w-6 h-6" />
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile menu */}
      {isMobileMenuOpen && (
        <div className="md:hidden border-t border-gray-200">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3 bg-white">
            <a 
              onClick={() => handleNavigation('/')}
              className="block w-full text-left px-3 py-2 text-base font-medium text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded-md transition-colors cursor-pointer"
            >
              Trang chủ
            </a>
            {isAdmin && (
              <a 
                onClick={() => handleNavigation('/admin/dashboard')}
                className="flex items-center w-full px-3 py-2 text-base font-medium text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded-md transition-colors cursor-pointer"
              >
                <Shield className="w-5 h-5 mr-2" />
                Dashboard
              </a>
            )}
            <a 
              onClick={() => handleNavigation('/assignments')}
              className="block w-full text-left px-3 py-2 text-base font-medium text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded-md transition-colors cursor-pointer"
            >
              Bài tập
            </a>
            <a 
              onClick={() => handleNavigation('/grades')}
              className="block w-full text-left px-3 py-2 text-base font-medium text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded-md transition-colors cursor-pointer"
            >
              Điểm số
            </a>
            <a 
              onClick={() => handleNavigation('/students')}
              className="block w-full text-left px-3 py-2 text-base font-medium text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded-md transition-colors cursor-pointer"
            >
              Học sinh
            </a>
            <a 
              onClick={() => handleNavigation('/reports')}
              className="block w-full text-left px-3 py-2 text-base font-medium text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded-md transition-colors cursor-pointer"
            >
              Báo cáo
            </a>
          </div>
          <div className="pt-4 pb-3 border-t border-gray-200 bg-gray-50">
            <div className="px-5 flex items-center">
              <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center shadow-sm">
                <User className="w-5 h-5 text-white" />
              </div>
              <div className="ml-3">
                <div className="text-base font-medium text-gray-800">
                  {currentUser?.fullName || currentUser?.username || 'User'}
                </div>
                <div className="text-sm text-gray-500">{currentUser?.email || ''}</div>
                {isAdmin && (
                  <span className="inline-flex items-center px-2 py-1 text-xs font-medium bg-blue-100 text-blue-800 rounded-full mt-1">
                    Admin
                  </span>
                )}
              </div>
            </div>
            <div className="mt-3 px-2 space-y-1">
              <button
                onClick={() => handleNavigation('/profile')}
                className="flex items-center w-full px-3 py-2 text-base font-medium text-gray-700 hover:text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
              >
                <User className="w-5 h-5 mr-3" />
                Hồ sơ
              </button>
              <button
                onClick={() => handleNavigation('/settings')}
                className="flex items-center w-full px-3 py-2 text-base font-medium text-gray-700 hover:text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
              >
                <Settings className="w-5 h-5 mr-3" />
                Cài đặt
              </button>
              <button
                onClick={handleLogout}
                className="flex items-center w-full px-3 py-2 text-base font-medium text-red-600 hover:text-red-700 hover:bg-red-50 rounded-md transition-colors"
              >
                <LogOut className="w-5 h-5 mr-3" />
                Đăng xuất
              </button>
            </div>
          </div>
        </div>
      )}
    </header>
  );
};

export default Header;
