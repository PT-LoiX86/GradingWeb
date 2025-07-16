import { useState, useEffect } from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router';
import { Auth, Home } from './components';
import AdminDashboard from './components/AdminDashboard';
import ProtectedRoute from './components/ProtectedRoute';
import AssignmentsPage from './components/pages/AssignmentsPage';
import GradesPage from './components/pages/GradesPage';
import StudentsPage from './components/pages/StudentsPage';
import ReportsPage from './components/pages/ReportsPage';
import ProfilePage from './components/pages/ProfilePage';
import SettingsPage from './components/pages/SettingsPage';
import { authAPI } from './services/api';
import { useErrorHandler } from './hooks/useErrorHandler';
import { Toaster } from 'react-hot-toast';
import OAuth2Callback from './components/OAuth2Callback';
import './App.css';

interface LoginFormValues {
  username: string;
  password: string;
  rememberMe?: boolean;
}

interface RegisterFormValues {
  username: string;
  password: string;
  confirmPassword: string;
  email: string;
  fullName: string;
}

function App() {
  const location = useLocation();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(false);
  const [googleLoading, setGoogleLoading] = useState(false);
  const [error, setError] = useState<string | undefined>(undefined);
  const [isInitialLoading, setIsInitialLoading] = useState(true);
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const [authMode, setAuthMode] = useState<'login' | 'register'>('login');
  const [isOAuth2Callback, setIsOAuth2Callback] = useState(false);
  const { handleError, showSuccess, showError } = useErrorHandler();

  const handleModeChange = (mode: 'login' | 'register') => {
    setAuthMode(mode);
    setError(undefined);
    setValidationErrors({});
  };

  // Check if user is already authenticated on app load
  useEffect(() => {
    const checkAuth = async () => {
      try {
        
        // Check if this is an OAuth2 callback
        const urlParams = new URLSearchParams(location.search);
        const hasOAuth2Params = urlParams.has('accessToken') || urlParams.has('refreshToken') || urlParams.has('error');
        const isOAuth2CallbackPath = location.pathname === '/oauth2/callback';
        
        
        if (isOAuth2CallbackPath && hasOAuth2Params) {
          console.log('App.tsx: OAuth2 callback detected, setting isOAuth2Callback=true');
          setIsOAuth2Callback(true);
          setIsInitialLoading(false);
          return;
        }

        const token = authAPI.getAccessToken();
        const refreshToken = authAPI.getRefreshToken();
        
        if (token) {
          console.log('App.tsx: Valid access token found, setting authenticated=true');
          setIsAuthenticated(true);
        } else if (refreshToken) {
          console.log('App.tsx: No access token but refresh token found, attempting refresh...');
          // Try to refresh token
          try {
            await authAPI.refreshToken(refreshToken);
            console.log('App.tsx: Token refresh successful');
            setIsAuthenticated(true);
          } catch (error) {
            console.error('App.tsx: Token refresh failed:', error);
            // Refresh failed, clear storage
            await authAPI.logout();
            setIsAuthenticated(false);
          }
        } else {
          console.log('App.tsx: No valid tokens found, user not authenticated');
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error('App.tsx: Auth check failed:', error);
        setIsAuthenticated(false);
      } finally {
        console.log('App.tsx: Auth check completed, setting isInitialLoading=false');
        setIsInitialLoading(false);
      }
    };

    checkAuth();
  }, [location]);

  const handleGoogleLogin = async () => {
    setGoogleLoading(true);
    try {
      const response = await authAPI.getOAuth2LoginUrl();
      // Redirect to Google OAuth2 login URL
      window.location.href = response.loginUrl;
    } catch (error) {
      console.error('Google login failed:', error);
      showError('Failed to connect to Google. Please try again.');
      setGoogleLoading(false);
    }
  };

  const handleOAuth2Success = async () => {
    console.log('OAuth2 success handler called');
    setIsOAuth2Callback(false);
    
    // Re-check authentication state after OAuth2 tokens are stored
    try {
      const token = authAPI.getAccessToken();
      const refreshToken = authAPI.getRefreshToken();
      
      console.log('Checking stored tokens:', { 
        hasAccessToken: !!token, 
        hasRefreshToken: !!refreshToken 
      });
      
      if (token && refreshToken) {
        setIsAuthenticated(true);
        console.log('OAuth2 authentication successful, redirecting to home');
        
        // Clear the URL completely to remove OAuth2 callback parameters
        window.history.replaceState({}, document.title, '/');
        
        // Optional: Show success message
        showSuccess('Đăng nhập Google thành công!');
      } else {
        console.error('OAuth2 success but no tokens found');
        setIsAuthenticated(false);
        showError('Lỗi lưu trữ token. Vui lòng thử lại.');
      }
    } catch (error) {
      console.error('Error checking authentication after OAuth2:', error);
      setIsAuthenticated(false);
      showError('Lỗi xác thực. Vui lòng thử lại.');
    }
  };

  const handleOAuth2Error = () => {
    setIsOAuth2Callback(false);
  };

  const handleLogin = async (data: LoginFormValues) => {
    setLoading(true);
    setError(undefined);
    setValidationErrors({});
    
    try {
      // Use username directly as it's now coming from the form
      const loginData = {
        username: data.username,
        password: data.password
      };

      const response = await authAPI.login(loginData);
      console.log('Login successful:', response);
      
      setIsAuthenticated(true);
      showSuccess('Đăng nhập thành công!');
    } catch (error) {
      console.error('Login failed:', error);
      const fieldErrors = handleError(error);
      setValidationErrors(fieldErrors);
      
      // Set general error if no specific field errors
      if (Object.keys(fieldErrors).length === 0) {
        setError('Login failed. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (data: RegisterFormValues) => {
    setLoading(true);
    setError(undefined);
    setValidationErrors({});
    
    try {
      const registerData = {
        username: data.username,
        password: data.password,
        email: data.email,
        fullName: data.fullName
      };

      const response = await authAPI.register(registerData);
      console.log('Registration successful:', response);
      
      // Show success message and switch to login mode after a delay
      showSuccess('Registration successful! Please login to continue.');
      
      // Switch to login mode after a short delay to let the user see the success message
      setTimeout(() => {
        setAuthMode('login');
        setError(undefined);
        setValidationErrors({});
      }, 2000);
    } catch (error) {
      console.error('Registration failed:', error);
      const fieldErrors = handleError(error);
      setValidationErrors(fieldErrors);
      
      // Set general error if no specific field errors
      if (Object.keys(fieldErrors).length === 0) {
        setError('Registration failed. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    setLoading(true);
    
    try {
      await authAPI.logout();
      setIsAuthenticated(false);
      setError(undefined);
      setValidationErrors({});
      showSuccess('Logout successful!');
      console.log('Logout successful');
    } catch (error) {
      console.error('Logout failed:', error);
      // Even if logout API fails, we should still log out locally
      setIsAuthenticated(false);
      setError(undefined);
      setValidationErrors({});
      showSuccess('Logout successful!');
    } finally {
      setLoading(false);
    }
  };

  // Show OAuth2 callback handler if needed
  if (isOAuth2Callback) {
    return (
      <div className="App">
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              style: {
                background: '#4ade80',
              },
            },
            error: {
              style: {
                background: '#ef4444',
              },
            },
          }}
        />
        <Routes>
          <Route path="/oauth2/callback" element={
            <OAuth2Callback onSuccess={handleOAuth2Success} onError={handleOAuth2Error} />
          } />
          <Route path="*" element={<Navigate to="/oauth2/callback" replace />} />
        </Routes>
      </div>
    );
  }

  // Show loading spinner while checking authentication
  if (isInitialLoading) {
    return (
      <div className="App">
        <div className="min-h-screen flex items-center justify-center">
          <div className="text-center">
            <div className="w-8 h-8 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
            <p className="text-gray-600">Loading...</p>
          </div>
        </div>
      </div>
    );
  }

  // Show home page if authenticated
  if (isAuthenticated) {
    return (
      <div className="App">
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              style: {
                background: '#4ade80',
              },
            },
            error: {
              style: {
                background: '#ef4444',
              },
            },
          }}
        />
        <Routes>
          <Route path="/" element={
            <ProtectedRoute>
              <Home onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="/assignments" element={
            <ProtectedRoute>
              <AssignmentsPage onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="/grades" element={
            <ProtectedRoute>
              <GradesPage onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="/students" element={
            <ProtectedRoute>
              <StudentsPage onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="/reports" element={
            <ProtectedRoute>
              <ReportsPage onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="/profile" element={
            <ProtectedRoute>
              <ProfilePage onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="/settings" element={
            <ProtectedRoute>
              <SettingsPage onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="/admin/dashboard" element={
            <ProtectedRoute requireAdmin={true}>
              <AdminDashboard onLogout={handleLogout} />
            </ProtectedRoute>
          } />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    );
  }

  // Show auth forms if not authenticated
  return (
    <div className="App">
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            background: '#363636',
            color: '#fff',
          },
          success: {
            style: {
              background: '#4ade80',
            },
          },
          error: {
            style: {
              background: '#ef4444',
            },
          },
        }}
      />
      <Routes>
        <Route path="/login" element={
          <Auth 
            onLogin={handleLogin}
            onRegister={handleRegister}
            onGoogleLogin={handleGoogleLogin}
            loading={loading}
            googleLoading={googleLoading}
            error={error}
            validationErrors={validationErrors}
            initialMode={authMode}
            onModeChange={handleModeChange}
          />
        } />
        <Route path="/oauth2/callback" element={
          <OAuth2Callback onSuccess={handleOAuth2Success} onError={handleOAuth2Error} />
        } />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </div>
  );
}

export default App;