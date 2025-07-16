import React from 'react';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  children: React.ReactNode;
  actions?: React.ReactNode;
}

const Modal: React.FC<ModalProps> = ({ isOpen, onClose, title, children, actions }) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
      <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg w-full max-w-md p-6 relative">
        <button
          className="absolute top-2 right-2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200"
          onClick={onClose}
          aria-label="Close"
        >
          &times;
        </button>
        {title && <h2 className="text-lg font-semibold mb-4">{title}</h2>}
        <div>{children}</div>
        {actions && <div className="mt-4 flex justify-end space-x-2">{actions}</div>}
      </div>
    </div>
  );
};

export default Modal; 