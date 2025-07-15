import { useState, useEffect } from 'react';
import { Auth, Home } from './components';
import { authAPI } from './services/api';
import { useErrorHandler } from './hooks/useErrorHandler';
import { Toaster } from 'react-hot-toast';
import './App.css';

interface LoginFormValues {
  username: string;
  password: string;
  rememberMe?: boolean;
}

interface RegisterFormValues {
  email: string;
  password: string;
  confirmPassword: string;
}

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | undefined>(undefined);
  const [isInitialLoading, setIsInitialLoading] = useState(true);
  const [validationErrors, setValidationErrors] = useState<Record<string, string | undefined>>({});
  const { handleError, showSuccess } = useErrorHandler();

  // Check if user is already authenticated on app load
  useEffect(() => {
    const checkAuth = async () => {
      try {
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
      showSuccess('Login successful!');
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
        username: data.email.split('@')[0], // Generate username from email
        password: data.password,
        email: data.email,
        fullName: data.email.split('@')[0] // Use email prefix as full name for now
      };

      const response = await authAPI.register(registerData);
      console.log('Registration successful:', response);
      
      // Show success message
      setError(undefined);
      showSuccess('Registration successful! Please check your email to verify your account.');
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
        loading={loading}
        error={error}
        validationErrors={validationErrors}
        initialMode="login"
      />
    </div>
  );
}

export default App;