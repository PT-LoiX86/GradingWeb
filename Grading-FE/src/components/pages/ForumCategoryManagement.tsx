import React, { useState, useEffect } from 'react';
import { forumAPI } from '../../services/api';
import type { ForumChannel } from '../../types/api';
import { Plus, Edit, Trash2, X, Check } from 'lucide-react';
import Layout from '../Layout';
import { toast } from 'react-hot-toast';

interface ForumCategoryManagementProps {
  onLogout: () => Promise<void>;
}

const ForumCategoryManagement: React.FC<ForumCategoryManagementProps> = ({ onLogout }) => {
  const [categories, setCategories] = useState<ForumChannel[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentCategory, setCurrentCategory] = useState<ForumChannel | null>(null);
  const [categoryName, setCategoryName] = useState('');
  const [categoryDescription, setCategoryDescription] = useState('');
  const [submitting, setSubmitting] = useState(false);
  
  // Pagination state
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  
  // Fetch categories
  const fetchCategories = async () => {
    try {
      setLoading(true);
      const response = await forumAPI.getChannels();
      if (response.data) {
        setCategories(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
        setTotalItems(response.data.totalElements || 0);
      }
    } catch (err) {
      console.error('Error fetching categories:', err);
      setError('Không thể tải danh mục');
    } finally {
      setLoading(false);
    }
  };
  
  // Remove mock data handling functions
  // Since we now have real API endpoints
  
  useEffect(() => {
    fetchCategories();
  }, []);
  
  const handleOpenModal = (category?: ForumChannel) => {
    if (category) {
      setCurrentCategory(category);
      setCategoryName(category.name);
      setCategoryDescription(category.description);
    } else {
      setCurrentCategory(null);
      setCategoryName('');
      setCategoryDescription('');
    }
    setIsModalOpen(true);
  };
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!categoryName.trim()) {
      toast.error('Tên danh mục không được để trống');
      return;
    }
    
    setSubmitting(true);
    try {
      if (currentCategory) {
        // Update category
        const response: any = await forumAPI.updateChannel(currentCategory.id, {
          name: categoryName.trim(),
          description: categoryDescription.trim()
        });
        
        if (response.data) {
          toast.success('Cập nhật danh mục thành công');
          fetchCategories(); // Refresh the list
        }
      } else {
        // Create category
        const response: any = await forumAPI.createChannel({
          name: categoryName.trim(),
          description: categoryDescription.trim()
        });
        
        if (response.data) {
          toast.success('Tạo danh mục thành công');
          fetchCategories(); // Refresh the list
        }
      }
      setIsModalOpen(false);
    } catch (err) {
      console.error('Lỗi khi lưu danh mục:', err);
      toast.error('Có lỗi xảy ra khi lưu danh mục');
    } finally {
      setSubmitting(false);
    }
  };
  
  const handleDeleteCategory = async (id: number) => {
    if (window.confirm('Bạn có chắc muốn xóa danh mục này không?')) {
      try {
        const response: any = await forumAPI.deleteChannel(id);
        
        toast.success('Xóa danh mục thành công');
        fetchCategories(); // Refresh the list
      } catch (err) {
        console.error('Lỗi khi xóa danh mục:', err);
        toast.error('Có lỗi xảy ra khi xóa danh mục');
      }
    }
  };
  
  return (
    <Layout onLogout={onLogout}>
      <div className="container mx-auto p-4 max-w-7xl">
        <div className="bg-white shadow-sm rounded-lg mb-6 p-6">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Quản lý danh mục diễn đàn</h1>
              <p className="text-gray-500 mt-1">Tạo và quản lý các danh mục trong diễn đàn</p>
            </div>
            
            <button 
              onClick={() => handleOpenModal()}
              className="mt-4 md:mt-0 flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              <Plus className="w-4 h-4 mr-2" />
              Thêm danh mục
            </button>
          </div>
          
          <div className="bg-yellow-50 border border-yellow-200 text-yellow-800 px-4 py-3 rounded mb-6">
            <p className="flex items-center">
              <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
              </svg>
              <span className="font-medium">Chế độ demo:</span>
              &nbsp;Đang sử dụng API giả lập. Các thay đổi sẽ chỉ được lưu trữ tạm thời và sẽ bị mất khi làm mới trang.
            </p>
          </div>
          
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-6">
              {error}
            </div>
          )}
          
          {loading ? (
            <div className="text-center py-12">
              <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-blue-600 border-r-transparent"></div>
              <p className="mt-3 text-gray-600">Đang tải dữ liệu...</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      ID
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Tên danh mục
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Mô tả
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Ngày tạo
                    </th>
                    <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Hành động
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {categories.length === 0 ? (
                    <tr>
                      <td colSpan={5} className="px-6 py-4 whitespace-nowrap text-center text-gray-500">
                        Chưa có danh mục nào
                      </td>
                    </tr>
                  ) : (
                    categories.map((category) => (
                      <tr key={category.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {category.id}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {category.name}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {category.description}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {new Date(category.createdAt).toLocaleDateString('vi-VN')}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                          <button
                            onClick={() => handleOpenModal(category)}
                            className="text-blue-600 hover:text-blue-900 mr-3"
                          >
                            <Edit className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDeleteCategory(category.id)}
                            className="text-red-600 hover:text-red-900"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
      
      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-md overflow-hidden">
            <div className="flex items-center justify-between border-b px-6 py-4">
              <h2 className="text-xl font-semibold">
                {currentCategory ? 'Cập nhật danh mục' : 'Thêm danh mục mới'}
              </h2>
              <button 
                onClick={() => setIsModalOpen(false)}
                className="text-gray-500 hover:text-gray-700 focus:outline-none"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
            
            <form onSubmit={handleSubmit} className="p-6">
              <div className="mb-4">
                <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                  Tên danh mục
                </label>
                <input
                  id="name"
                  type="text"
                  value={categoryName}
                  onChange={(e) => setCategoryName(e.target.value)}
                  placeholder="Nhập tên danh mục"
                  className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  disabled={submitting}
                />
              </div>
              
              <div className="mb-6">
                <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                  Mô tả
                </label>
                <textarea
                  id="description"
                  value={categoryDescription}
                  onChange={(e) => setCategoryDescription(e.target.value)}
                  placeholder="Nhập mô tả danh mục"
                  className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  rows={3}
                  disabled={submitting}
                />
              </div>
              
              <div className="flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-500"
                  disabled={submitting}
                >
                  Hủy
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-blue-600 rounded-md text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 flex items-center"
                  disabled={submitting}
                >
                  {submitting ? (
                    <>
                      <span className="animate-spin mr-2">◌</span>
                      Đang xử lý...
                    </>
                  ) : (
                    <>
                      <Check className="w-4 h-4 mr-2" />
                      Lưu
                    </>
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </Layout>
  );
};

export default ForumCategoryManagement; 