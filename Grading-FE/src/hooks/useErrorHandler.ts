import { useCallback } from 'react';
import toast from 'react-hot-toast';

interface ApiError {
  type: 'error' | 'validation';
  message: string;
  validationErrors?: Record<string, string>;
  statusCode: number;
}

interface FormFieldErrors {
  [key: string]: string | undefined;
}

export const useErrorHandler = () => {
  const handleError = useCallback((error: any): FormFieldErrors => {
    const fieldErrors: FormFieldErrors = {};

    if (error && typeof error === 'object' && error.type) {
      const apiError = error as ApiError;
      
      if (apiError.type === 'validation' && apiError.validationErrors) {
        // Handle validation errors - show them on specific fields
        Object.entries(apiError.validationErrors).forEach(([field, message]) => {
          fieldErrors[field] = message;
        });
        
        // Also show a general validation error toast
        toast.error(apiError.message || 'Validation failed');
      } else if (apiError.type === 'error') {
        // Handle general errors - show as toast
        toast.error(apiError.message);
      }
    } else {
      // Handle unexpected errors
      const message = error?.message || 'An unexpected error occurred';
      toast.error(message);
    }

    return fieldErrors;
  }, []);

  const showSuccess = useCallback((message: string) => {
    toast.success(message);
  }, []);

  const showError = useCallback((message: string) => {
    toast.error(message);
  }, []);

  const showInfo = useCallback((message: string) => {
    toast(message);
  }, []);

  return {
    handleError,
    showSuccess,
    showError,
    showInfo
  };
};
