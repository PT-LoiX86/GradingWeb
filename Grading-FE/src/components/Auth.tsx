import React, { useState } from 'react';
import AuthLoginForm from './AuthLoginForm';
import AuthRegisterForm from './AuthRegisterForm';

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

interface AuthProps {
  onLogin?: (data: LoginFormValues) => void;
  onRegister?: (data: RegisterFormValues) => void;
  loading?: boolean;
  error?: string;
  validationErrors?: Record<string, string | undefined>;
  initialMode?: 'login' | 'register';
}

const Auth: React.FC<AuthProps> = ({ 
  onLogin, 
  onRegister, 
  loading, 
  error, 
  validationErrors = {},
  initialMode = 'login' 
}) => {
  const [mode, setMode] = useState<'login' | 'register'>(initialMode);

  const handleLogin = (data: LoginFormValues) => {
    onLogin?.(data);
  };

  const handleRegister = (data: RegisterFormValues) => {
    onRegister?.(data);
  };

  const switchToRegister = () => {
    setMode('register');
  };

  const switchToLogin = () => {
    setMode('login');
  };

  if (mode === 'login') {
    return (
      <AuthLoginForm
        onSubmit={handleLogin}
        loading={loading}
        error={error}
        validationErrors={validationErrors}
        onSwitchToRegister={switchToRegister}
      />
    );
  }

  return (
    <AuthRegisterForm
      onSubmit={handleRegister}
      loading={loading}
      error={error}
      validationErrors={validationErrors}
      onSwitchToLogin={switchToLogin}
    />
  );
};

export default Auth;
