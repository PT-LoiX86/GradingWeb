import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router';
import { authAPI } from '../services/api';
import { useErrorHandler } from '../hooks/useErrorHandler';

interface OAuth2CallbackProps {
  onSuccess: () => void;
  onError: () => void;
}

const OAuth2Callback: React.FC<OAuth2CallbackProps> = ({ onSuccess, onError }) => {
  const location = useLocation();
  const { showError, showSuccess } = useErrorHandler();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const handleCallback = async () => {
      try {
        const urlParams = new URLSearchParams(location.search);
        const accessToken = urlParams.get('accessToken');
        const refreshToken = urlParams.get('refreshToken');
        const error = urlParams.get('error');

        console.log('OAuth2 callback params:', { 
          pathname: location.pathname,
          accessToken: !!accessToken, 
          refreshToken: !!refreshToken, 
          error 
        });

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
          console.log('Access token length:', accessToken.length);
          console.log('Refresh token length:', refreshToken.length);
          
          try {
            // Store tokens using the API
            await authAPI.handleOAuth2Callback(accessToken, refreshToken);
            
            // Verify tokens were stored correctly
            const storedAccessToken = authAPI.getAccessToken();
            const storedRefreshToken = authAPI.getRefreshToken();
            
            console.log('Token storage verification:', {
              storedAccessToken: !!storedAccessToken,
              storedRefreshToken: !!storedRefreshToken,
              accessTokenMatch: storedAccessToken === accessToken,
              refreshTokenMatch: storedRefreshToken === refreshToken
            });
            
            if (storedAccessToken && storedRefreshToken) {
              console.log('Tokens stored successfully, calling onSuccess');
              
              // Clean up URL parameters immediately
              window.history.replaceState({}, document.title, '/');
              
              // Call onSuccess with a small delay to ensure proper state updates
              setTimeout(() => {
                console.log('Calling onSuccess callback');
                onSuccess();
              }, 100);
            } else {
              console.error('Failed to store tokens properly');
              showError('Không thể lưu thông tin đăng nhập. Vui lòng thử lại.');
              window.history.replaceState({}, document.title, '/');
              setTimeout(() => onError(), 1000);
            }
          } catch (error) {
            console.error('OAuth2 callback processing error:', error);
            showError('Không thể xử lý phản hồi từ Google. Vui lòng thử lại.');
            window.history.replaceState({}, document.title, '/');
            setTimeout(() => onError(), 1000);
          }
        } else {
          console.error('Invalid OAuth2 callback - missing tokens');
          console.log('Received params:', { accessToken: !!accessToken, refreshToken: !!refreshToken });
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
    const urlParams = new URLSearchParams(location.search);
    if (urlParams.has('accessToken') || urlParams.has('refreshToken') || urlParams.has('error')) {
      handleCallback();
    } else {
      console.log('No OAuth2 callback parameters found');
      setLoading(false);
      onError();
    }
  }, [location, onSuccess, onError, showError, showSuccess]);

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
