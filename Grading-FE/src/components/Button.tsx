import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'accent';
  loading?: boolean;
  icon?: React.ReactNode;
  fullWidth?: boolean;
}

const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  loading = false,
  icon,
  fullWidth = false,
  className = '',
  ...props
}) => {
  const base = 'inline-flex items-center justify-center px-4 py-2 rounded font-medium focus:outline-none focus:ring-2 focus:ring-offset-2 transition';
  const variants: Record<string, string> = {
    primary: 'bg-primary text-white hover:bg-blue-700',
    secondary: 'bg-secondary text-white hover:bg-gray-600',
    accent: 'bg-accent text-white hover:bg-orange-500',
  };
  return (
    <button
      className={[
        base,
        variants[variant],
        fullWidth ? 'w-full' : '',
        loading ? 'opacity-60 cursor-not-allowed' : '',
        className,
      ].join(' ')}
      disabled={loading || props.disabled}
      {...props}
    >
      {loading ? (
        <span className="animate-spin mr-2">⏳</span>
      ) : icon ? (
        <span className="mr-2">{icon}</span>
      ) : null}
      {children}
    </button>
  );
};

export default Button; 