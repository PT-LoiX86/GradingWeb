import React, { useState } from 'react';
import AuthLoginForm from './AuthLoginForm';
import AuthRegisterForm from './AuthRegisterForm';

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

interface AuthProps {
  onLogin?: (data: LoginFormValues) => void;
  onRegister?: (data: RegisterFormValues) => void;
  onGoogleLogin?: () => void;
  loading?: boolean;
  googleLoading?: boolean;
  error?: string;
  validationErrors?: Record<string, string | undefined>;
  initialMode?: 'login' | 'register';
  onModeChange?: (mode: 'login' | 'register') => void;
}

const Auth: React.FC<AuthProps> = ({ 
  onLogin, 
  onRegister, 
  onGoogleLogin,
  loading, 
  googleLoading,
  error, 
  validationErrors = {},
  initialMode = 'login',
  onModeChange
}) => {
  const [mode, setMode] = useState<'login' | 'register'>(initialMode);

  // Update mode when initialMode changes
  React.useEffect(() => {
    setMode(initialMode);
  }, [initialMode]);

  const handleLogin = (data: LoginFormValues) => {
    onLogin?.(data);
  };

  const handleRegister = (data: RegisterFormValues) => {
    onRegister?.(data);
  };

  const switchToRegister = () => {
    setMode('register');
    onModeChange?.('register');
  };

  const switchToLogin = () => {
    setMode('login');
    onModeChange?.('login');
  };

  if (mode === 'login') {
    return (
      <AuthLoginForm
        onSubmit={handleLogin}
        onGoogleLogin={onGoogleLogin || (() => {})}
        loading={loading}
        googleLoading={googleLoading}
        error={error}
        validationErrors={validationErrors}
        onSwitchToRegister={switchToRegister}
      />
    );
  }

  return (
    <AuthRegisterForm
      onSubmit={handleRegister}
      onGoogleLogin={onGoogleLogin || (() => {})}
      loading={loading}
      googleLoading={googleLoading}
      error={error}
      validationErrors={validationErrors}
      onSwitchToLogin={switchToLogin}
    />
  );
};

export default Auth;
