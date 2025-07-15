import React, { forwardRef } from 'react';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

const Input = forwardRef<HTMLInputElement, InputProps>(({ label, error, ...props }, ref) => (
  <div className="mb-4">
    {label && <label className="block mb-1 text-sm font-medium text-gray-700 dark:text-gray-200">{label}</label>}
    <input
      ref={ref}
      className={`w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-800 dark:text-white ${error ? 'border-red-500' : 'border-gray-300'}`}
      {...props}
    />
    {error && <p className="mt-1 text-xs text-red-500">{error}</p>}
  </div>
));

Input.displayName = 'Input';

export default Input; 