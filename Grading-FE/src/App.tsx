import { useState, useEffect } from 'react';
import { Auth, Home } from './components';
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
        const urlParams = new URLSearchParams(window.location.search);
        const hasOAuth2Params = urlParams.has('accessToken') || urlParams.has('refreshToken') || urlParams.has('error');
        
        if (hasOAuth2Params) {
          console.log('OAuth2 callback detected');
          setIsOAuth2Callback(true);
          setIsInitialLoading(false);
          return;
        }

        const token = authAPI.getAccessToken();
        const refreshToken = authAPI.getRefreshToken();
        
        if (token) {
          setIsAuthenticated(true);
        } else if (refreshToken) {
          // Try to refresh token
          try {
            await authAPI.refreshToken(refreshToken);
            setIsAuthenticated(true);
          } catch (error) {
            // Refresh failed, clear storage
            await authAPI.logout();
            setIsAuthenticated(false);
          }
        }
      } catch (error) {
        console.error('Auth check failed:', error);
        setIsAuthenticated(false);
      } finally {
        setIsInitialLoading(false);
      }
    };

    checkAuth();
  }, []);

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

  const handleOAuth2Success = () => {
    setIsOAuth2Callback(false);
    setIsAuthenticated(true);
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
        <OAuth2Callback onSuccess={handleOAuth2Success} onError={handleOAuth2Error} />
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
        <Home onLogout={handleLogout} />
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
    </div>
  );
}

export default App;