import React, { useState, useEffect } from "react";
import { forumAPI } from "../../services/api";
import type { ForumChannel } from "../../types/api";
import { Plus, Edit, Trash2, X, Check } from "lucide-react";
import Layout from "../Layout";
import { toast } from "react-hot-toast";

interface ForumCategoryManagementProps {
    onLogout: () => Promise<void>;
}

const ForumCategoryManagement: React.FC<ForumCategoryManagementProps> = ({
    onLogout,
}) => {
    const [categories, setCategories] = useState<ForumChannel[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentCategory, setCurrentCategory] = useState<ForumChannel | null>(
        null
    );
    const [categoryName, setCategoryName] = useState("");
    const [categoryDescription, setCategoryDescription] = useState("");
    const [submitting, setSubmitting] = useState(false);

    // Fetch categories
    const fetchCategories = async () => {
        try {
            setLoading(true);
            const response = await forumAPI.getChannels();
            setCategories(response);
        } catch (err) {
            console.error("Error fetching categories:", err);
            setError("Không thể tải danh mục");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const resetForm = () => {
        setCategoryName("");
        setCategoryDescription("");
        setCurrentCategory(null);
    };

    const handleOpenModal = (category?: ForumChannel) => {
        if (category) {
            setCurrentCategory(category);
            setCategoryName(category.name);
            setCategoryDescription(category.description);
        } else {
            resetForm();
        }
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        resetForm();
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!categoryName.trim() || !categoryDescription.trim()) {
            toast.error("Vui lòng điền đầy đủ thông tin");
            return;
        }

        setSubmitting(true);

        try {
            if (currentCategory) {
                // Update category
                await forumAPI.updateChannel(currentCategory.id, {
                    name: categoryName.trim(),
                    description: categoryDescription.trim(),
                });

                toast.success("Cập nhật danh mục thành công");
                fetchCategories(); // Refresh the list
            } else {
                // Create category
                await forumAPI.createChannel({
                    name: categoryName.trim(),
                    description: categoryDescription.trim(),
                    color: "#3B82F6", // Default blue color
                    isActive: true,
                });

                toast.success("Tạo danh mục thành công");
                fetchCategories(); // Refresh the list
            }

            setIsModalOpen(false);
            resetForm();
        } catch (err) {
            console.error("Error saving category:", err);
            toast.error("Có lỗi xảy ra khi lưu danh mục");
        } finally {
            setSubmitting(false);
        }
    };

    const handleDeleteCategory = async (id: number) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
            return;
        }

        try {
            await forumAPI.deleteChannel(id);
            toast.success("Xóa danh mục thành công");
            fetchCategories(); // Refresh the list
        } catch (err) {
            console.error("Error deleting category:", err);
            toast.error("Có lỗi xảy ra khi xóa danh mục");
        }
    };

    return (
        <Layout onLogout={onLogout}>
            <div className="max-w-4xl mx-auto">
                <div className="mb-8">
                    <div className="flex justify-between items-center">
                        <h1 className="text-3xl font-bold text-gray-900">
                            Quản lý danh mục Forum
                        </h1>
                        <button
                            onClick={() => handleOpenModal()}
                            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center space-x-2"
                        >
                            <Plus className="w-4 h-4" />
                            <span>Thêm danh mục</span>
                        </button>
                    </div>
                </div>

                {/* Error state */}
                {error && (
                    <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
                        {error}
                    </div>
                )}

                {/* Loading state */}
                {loading && (
                    <div className="text-center py-8">
                        <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                        <p className="mt-2 text-gray-600">Đang tải...</p>
                    </div>
                )}

                {/* Categories table */}
                {!loading && (
                    <div className="bg-white shadow rounded-lg">
                        <div className="overflow-x-auto">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Tên danh mục
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Mô tả
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Trạng thái
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Ngày tạo
                                        </th>
                                        <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                                            Thao tác
                                        </th>
                                    </tr>
                                </thead>
                                <tbody className="bg-white divide-y divide-gray-200">
                                    {categories.length === 0 ? (
                                        <tr>
                                            <td
                                                colSpan={5}
                                                className="px-6 py-12 text-center text-gray-500"
                                            >
                                                Chưa có danh mục nào
                                            </td>
                                        </tr>
                                    ) : (
                                        categories.map((category) => (
                                            <tr
                                                key={category.id}
                                                className="hover:bg-gray-50"
                                            >
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="flex items-center">
                                                        <div
                                                            className="w-3 h-3 rounded-full mr-3"
                                                            style={{
                                                                backgroundColor:
                                                                    category.color,
                                                            }}
                                                        ></div>
                                                        <div className="text-sm font-medium text-gray-900">
                                                            {category.name}
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4">
                                                    <div className="text-sm text-gray-900 max-w-xs truncate">
                                                        {category.description}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <span
                                                        className={`inline-flex px-2 text-xs font-semibold rounded-full ${
                                                            category.isActive
                                                                ? "bg-green-100 text-green-800"
                                                                : "bg-red-100 text-red-800"
                                                        }`}
                                                    >
                                                        {category.isActive
                                                            ? "Hoạt động"
                                                            : "Không hoạt động"}
                                                    </span>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                    {new Date(
                                                        category.createdAt
                                                    ).toLocaleDateString(
                                                        "vi-VN"
                                                    )}
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                                    <div className="flex justify-end space-x-2">
                                                        <button
                                                            onClick={() =>
                                                                handleOpenModal(
                                                                    category
                                                                )
                                                            }
                                                            className="text-blue-600 hover:text-blue-900 p-1"
                                                            title="Chỉnh sửa"
                                                        >
                                                            <Edit className="w-4 h-4" />
                                                        </button>
                                                        <button
                                                            onClick={() =>
                                                                handleDeleteCategory(
                                                                    category.id
                                                                )
                                                            }
                                                            className="text-red-600 hover:text-red-900 p-1"
                                                            title="Xóa"
                                                        >
                                                            <Trash2 className="w-4 h-4" />
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        ))
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {/* Modal */}
                {isModalOpen && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
                        <div className="bg-white rounded-lg max-w-md w-full">
                            <div className="p-6">
                                <div className="flex justify-between items-center mb-4">
                                    <h3 className="text-lg font-medium">
                                        {currentCategory
                                            ? "Chỉnh sửa danh mục"
                                            : "Thêm danh mục mới"}
                                    </h3>
                                    <button
                                        onClick={handleCloseModal}
                                        className="text-gray-400 hover:text-gray-600"
                                    >
                                        <X className="w-5 h-5" />
                                    </button>
                                </div>

                                <form onSubmit={handleSubmit}>
                                    <div className="space-y-4">
                                        <div>
                                            <label
                                                htmlFor="categoryName"
                                                className="block text-sm font-medium text-gray-700 mb-1"
                                            >
                                                Tên danh mục
                                            </label>
                                            <input
                                                type="text"
                                                id="categoryName"
                                                value={categoryName}
                                                onChange={(e) =>
                                                    setCategoryName(
                                                        e.target.value
                                                    )
                                                }
                                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                                placeholder="Nhập tên danh mục"
                                                required
                                            />
                                        </div>

                                        <div>
                                            <label
                                                htmlFor="categoryDescription"
                                                className="block text-sm font-medium text-gray-700 mb-1"
                                            >
                                                Mô tả
                                            </label>
                                            <textarea
                                                id="categoryDescription"
                                                value={categoryDescription}
                                                onChange={(e) =>
                                                    setCategoryDescription(
                                                        e.target.value
                                                    )
                                                }
                                                rows={3}
                                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                                placeholder="Nhập mô tả danh mục"
                                                required
                                            />
                                        </div>
                                    </div>

                                    <div className="flex justify-end space-x-3 mt-6">
                                        <button
                                            type="button"
                                            onClick={handleCloseModal}
                                            className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 border border-gray-300 rounded-md hover:bg-gray-200"
                                        >
                                            Hủy
                                        </button>
                                        <button
                                            type="submit"
                                            disabled={submitting}
                                            className="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md hover:bg-blue-700 disabled:opacity-50 flex items-center space-x-2"
                                        >
                                            {submitting ? (
                                                <>
                                                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                                                    <span>Đang lưu...</span>
                                                </>
                                            ) : (
                                                <>
                                                    <Check className="w-4 h-4" />
                                                    <span>
                                                        {currentCategory
                                                            ? "Cập nhật"
                                                            : "Tạo mới"}
                                                    </span>
                                                </>
                                            )}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </Layout>
    );
};

export default ForumCategoryManagement;
