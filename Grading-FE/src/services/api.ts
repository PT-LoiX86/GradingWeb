import type { 
  LoginRequest, 
  RegisterRequest, 
  LoginResponse,
  ErrorResponse,
  ValidationErrorResponse
} from '../types/api';
import toast from 'react-hot-toast';

// Base API configuration
const BASE_URL = 'http://localhost:8080/api';

// API client configuration
class ApiClient {
  private baseURL: string;
  private defaultHeaders: Record<string, string>;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
    };
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseURL}${endpoint}`;
    
    // Get token from localStorage if available
    const token = localStorage.getItem('accessToken');
    
    const config: RequestInit = {
      ...options,
      headers: {
        ...this.defaultHeaders,
        ...options.headers,
        ...(token && { Authorization: `Bearer ${token}` }),
      },
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        
        // Handle ValidationErrorResponse
        if (errorData.validationErrors) {
          const validationError = errorData as ValidationErrorResponse;
          throw {
            type: 'validation',
            message: validationError.errorMessage,
            validationErrors: validationError.validationErrors,
            statusCode: response.status
          };
        }
        
        // Handle ErrorResponse
        if (errorData.errorMessage) {
          const error = errorData as ErrorResponse;
          throw {
            type: 'error',
            message: error.errorMessage,
            statusCode: response.status
          };
        }
        
        // Fallback for other errors
        throw {
          type: 'error',
          message: errorData.message || `HTTP error! status: ${response.status}`,
          statusCode: response.status
        };
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  async get<T>(endpoint: string, headers?: Record<string, string>): Promise<T> {
    return this.request<T>(endpoint, { method: 'GET', headers });
  }

  async post<T>(
    endpoint: string,
    body?: any,
    headers?: Record<string, string>
  ): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: JSON.stringify(body),
      headers,
    });
  }

  async put<T>(
    endpoint: string,
    body?: any,
    headers?: Record<string, string>
  ): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: JSON.stringify(body),
      headers,
    });
  }

  async delete<T>(endpoint: string, headers?: Record<string, string>): Promise<T> {
    return this.request<T>(endpoint, { method: 'DELETE', headers });
  }
}

// Create API client instance
const apiClient = new ApiClient(BASE_URL);

// Authentication API
export const authAPI = {
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>('/auth/login', credentials);
    
    // Store tokens in localStorage
    if (response.accessToken) {
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      localStorage.setItem('user', JSON.stringify(response.user));
    }
    
    return response;
  },

  register: async (userData: RegisterRequest): Promise<{ message: string }> => {
    return apiClient.post<{ message: string }>('/auth/register', userData);
  },

  verifyRegistration: async (token: string): Promise<{ message: string }> => {
    return apiClient.get<{ message: string }>(`/auth/register/verify?token=${token}`);
  },

  refreshToken: async (refreshToken: string): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>('/auth/refresh', { refreshToken });
    
    // Update stored tokens
    if (response.accessToken) {
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      localStorage.setItem('user', JSON.stringify(response.user));
    }
    
    return response;
  },

  logout: async (): Promise<void> => {
    const refreshToken = localStorage.getItem('refreshToken');
    
    if (refreshToken) {
      try {
        await apiClient.post('/auth/logout', { refreshToken });
      } catch (error) {
        console.error('Logout API call failed:', error);
      }
    }
    
    // Clear stored tokens
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  },

  forgotPassword: async (email: string): Promise<{ message: string }> => {
    return apiClient.post<{ message: string }>('/auth/forgot-password', { email });
  },

  resetPassword: async (token: string, newPassword: string): Promise<{ message: string }> => {
    return apiClient.put<{ message: string }>('/auth/reset-password', { token, newPassword });
  },

  getOAuth2LoginUrl: async (): Promise<{ loginUrl: string }> => {
    return apiClient.get<{ loginUrl: string }>('/auth/oauth2/login-url');
  },

  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  getAccessToken: () => {
    return localStorage.getItem('accessToken');
  },

  getRefreshToken: () => {
    return localStorage.getItem('refreshToken');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('accessToken');
  },
};

export default apiClient;
