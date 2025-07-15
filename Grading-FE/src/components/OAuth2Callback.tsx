import React, { useEffect, useState } from 'react';
import { authAPI } from '../services/api';
import { useErrorHandler } from '../hooks/useErrorHandler';

interface OAuth2CallbackProps {
  onSuccess: () => void;
  onError: () => void;
}

const OAuth2Callback: React.FC<OAuth2CallbackProps> = ({ onSuccess, onError }) => {
  const { showError, showSuccess } = useErrorHandler();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const handleCallback = async () => {
      try {
        const urlParams = new URLSearchParams(window.location.search);
        const accessToken = urlParams.get('accessToken');
        const refreshToken = urlParams.get('refreshToken');
        const error = urlParams.get('error');

        console.log('OAuth2 callback params:', { accessToken: !!accessToken, refreshToken: !!refreshToken, error });

        if (error) {
          const decodedError = decodeURIComponent(error);
          console.error('OAuth2 error:', decodedError);
          
          // Show user-friendly error message
          if (decodedError.includes('authorization_request_not_found')) {
            showError('Phiên đăng nhập OAuth2 đã hết hạn. Vui lòng thử đăng nhập lại.');
          } else if (decodedError.includes('access_denied')) {
            showError('Đăng nhập Google đã bị hủy. Vui lòng thử lại.');
          } else {
            showError('Đăng nhập Google thất bại. Vui lòng thử lại.');
          }
          
          // Clean up URL and redirect to login
          window.history.replaceState({}, document.title, '/');
          setTimeout(() => onError(), 1000);
          return;
        }

        if (accessToken && refreshToken) {
          console.log('OAuth2 tokens received, processing...');
          await authAPI.handleOAuth2Callback(accessToken, refreshToken);
          showSuccess('Đăng nhập Google thành công!');
          
          // Clean up URL parameters
          window.history.replaceState({}, document.title, '/');
          setTimeout(() => onSuccess(), 1000);
        } else {
          console.error('Invalid OAuth2 callback - missing tokens');
          showError('Phản hồi xác thực không hợp lệ. Vui lòng thử lại.');
          window.history.replaceState({}, document.title, '/');
          setTimeout(() => onError(), 1000);
        }
      } catch (error) {
        console.error('OAuth2 callback error:', error);
        showError('Xác thực thất bại. Vui lòng thử lại.');
        window.history.replaceState({}, document.title, '/');
        setTimeout(() => onError(), 1000);
      } finally {
        setLoading(false);
      }
    };

    // Only handle callback if we have URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('accessToken') || urlParams.has('refreshToken') || urlParams.has('error')) {
      handleCallback();
    } else {
      console.log('No OAuth2 callback parameters found');
      setLoading(false);
      onError();
    }
  }, [onSuccess, onError, showError, showSuccess]);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="w-8 h-8 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Đang hoàn thành đăng nhập Google...</p>
        </div>
      </div>
    );
  }

  return null;
};

export default OAuth2Callback;
