import React from 'react';
import { Navigate } from 'react-router';
import { authAPI } from '../services/api';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requireAdmin?: boolean;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requireAdmin = false }) => {
  const token = authAPI.getAccessToken();
  
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (requireAdmin) {
    try {
      const user = authAPI.getCurrentUser();
      if (!user || user.role !== 'ADMIN') {
        return <Navigate to="/" replace />;
      }
    } catch (error) {
      return <Navigate to="/login" replace />;
    }
  }

  return <>{children}</>;
};

export default ProtectedRoute;
